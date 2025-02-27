package com.exemplo.spotifydownloader.service;

import com.exemplo.spotifydownloader.exceptions.PlaylistNotPublicException;
import com.exemplo.spotifydownloader.model.PlaylistInfoDTO;
import com.exemplo.spotifydownloader.model.TrackInfoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class SpotifyService {

    @Autowired
    private SpotifyAuthService spotifyAuthService;

    @Autowired
    private YouTubeService youTubeService;

    public TrackInfoDTO getTrackInfo(String trackId) {
        String accessToken = spotifyAuthService.getAccessToken();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken); // Usar o token de acesso

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.spotify.com/v1/tracks/" + trackId,
                HttpMethod.GET,
                entity,
                String.class
        );

        // Verificar se a requisição foi bem-sucedida
        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                // Converter o JSON para a classe TrackInfoDTO
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(response.getBody(), TrackInfoDTO.class);
            } catch (Exception e) {
                throw new RuntimeException("Erro ao processar o JSON de resposta", e);
            }
        } else {
            throw new RuntimeException("Falha ao obter informações da música: " + response.getStatusCode());
        }
    }

    // Método para obter informações da playlist
    public PlaylistInfoDTO getPlaylistInfo(String playlistId) {
        String accessToken = spotifyAuthService.getAccessToken();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.spotify.com/v1/playlists/" + playlistId,
                HttpMethod.GET,
                entity,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                PlaylistInfoDTO playlistInfo = mapper.readValue(response.getBody(), PlaylistInfoDTO.class);

                // Verifica se a playlist é pública
                if (playlistInfo.getIsPublic() != null && !playlistInfo.getIsPublic()) {
                    throw new PlaylistNotPublicException("A playlist não é pública.");
                }

                return playlistInfo;
            } catch (Exception e) {
                throw new RuntimeException("Erro ao processar o JSON da playlist", e);
            }
        } else {
            throw new RuntimeException("Falha ao obter informações da playlist: " + response.getStatusCode());
        }
    }


    // Método para baixar uma playlist
    public void downloadPlaylist(String playlistId, HttpServletResponse response) throws IOException {
        PlaylistInfoDTO playlistInfo = getPlaylistInfo(playlistId);

        if (playlistInfo != null && playlistInfo.getTracks() != null) {
            // Cria um arquivo ZIP temporário
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ZipOutputStream zipOut = new ZipOutputStream(baos)) {
                for (PlaylistInfoDTO.TrackItem trackItem : playlistInfo.getTracks().getItems()) {
                    TrackInfoDTO trackInfo = trackItem.getTrack();
                    if (trackInfo != null) {
                        // Gera o arquivo MP3 para cada música
                        String query = trackInfo.getName() + " " + trackInfo.getArtists()[0].getName() + " official audio lyrics";
                        Process process = youTubeService.downloadMp3ToStream(query);

                        // Adiciona o arquivo MP3 ao ZIP
                        ZipEntry zipEntry = new ZipEntry(trackInfo.getName() + " - " + trackInfo.getArtists()[0].getName() + ".mp3");
                        zipOut.putNextEntry(zipEntry);
                        InputStream processOutput = process.getInputStream();
                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        while ((bytesRead = processOutput.read(buffer)) != -1) {
                            zipOut.write(buffer, 0, bytesRead);
                        }
                        zipOut.closeEntry();
                        processOutput.close();
                        process.destroy();
                    }
                }
            }

            // Configura a resposta HTTP para enviar o ZIP
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=\"playlist.zip\"");
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao processar a playlist.");
        }
    }

    // Método para baixar uma música individual
    public void downloadTrack(String trackName, String artistName, HttpServletResponse response) throws IOException {
        // Combina o nome da música e do artista para a busca no YouTube
        String query = trackName + " " + artistName + " official audio lyrics";
        youTubeService.downloadMp3(query, response); // Passa o HttpServletResponse para o YouTubeService
    }

}