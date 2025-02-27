package com.exemplo.spotifydownloader.controller;

import com.exemplo.spotifydownloader.exceptions.PlaylistNotPublicException;
import com.exemplo.spotifydownloader.model.PlaylistInfoDTO;
import com.exemplo.spotifydownloader.model.PlaylistTracksResponse;
import com.exemplo.spotifydownloader.model.TrackInfoDTO;
import com.exemplo.spotifydownloader.service.SpotifyAuthService;
import com.exemplo.spotifydownloader.service.SpotifyService;
import com.exemplo.spotifydownloader.service.SpotifyUtils;
import com.exemplo.spotifydownloader.service.YouTubeService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SpotifyController {

    @Autowired
    private SpotifyAuthService spotifyAuthService;

    @Autowired
    private SpotifyService spotifyService;

    @Autowired
    private YouTubeService youTubeService;


    @GetMapping("/token")
    public String getToken() {
        return spotifyAuthService.getAccessToken();
    }

    @GetMapping("/track/{id}")
    public Map<String, String> getSimplifiedTrackInfo(@PathVariable String id) {
        TrackInfoDTO trackInfo = spotifyService.getTrackInfo(id);

        Map<String, String> simplifiedInfo = new HashMap<>();
        simplifiedInfo.put("name", trackInfo.getName());
        simplifiedInfo.put("artist", trackInfo.getArtists()[0].getName());
        simplifiedInfo.put("imageUrl", trackInfo.getAlbum().getImages()[0].getUrl());

        return simplifiedInfo;
    }

    @GetMapping("/track")
    public Map<String, String> getSimplifiedTrackInfoByUrl(@RequestParam String url) {
        // Extrai o ID da música do link
        String trackId = SpotifyUtils.extractTrackId(url);
        // Obtém as informações da música usando o ID
        TrackInfoDTO trackInfo = spotifyService.getTrackInfo(trackId);


        Map<String, String> simplifiedInfo = new HashMap<>();
        simplifiedInfo.put("name", trackInfo.getName() != null ? trackInfo.getName() : "Desconhecido");

        if (trackInfo.getArtists() != null && trackInfo.getArtists().length > 0) {
            simplifiedInfo.put("artist", trackInfo.getArtists()[0].getName());
        } else {
            simplifiedInfo.put("artist", "Desconhecido");
        }

        if (trackInfo.getAlbum() != null && trackInfo.getAlbum().getImages() != null && trackInfo.getAlbum().getImages().length > 0) {
            simplifiedInfo.put("imageUrl", trackInfo.getAlbum().getImages()[0].getUrl());
        } else {
            simplifiedInfo.put("imageUrl", "URL não disponível");
        }

        if (trackInfo.getAlbum() != null) {
            simplifiedInfo.put("album", trackInfo.getAlbum().getName());
        } else {
            simplifiedInfo.put("album", "Álbum desconhecido");
        }

        simplifiedInfo.put("duration", trackInfo.getFormattedDuration()); // Duração formatada (mm:ss)

        return simplifiedInfo;
    }

    @GetMapping("/playlist")
    public ResponseEntity<?> getSimplifiedPlaylistInfoByUrl(@RequestParam String url) {
        try {
            // Extrai o ID da playlist do link
            String playlistId = SpotifyUtils.extractPlaylistId(url);
            PlaylistInfoDTO playlistInfo = spotifyService.getPlaylistInfo(playlistId);

            // Cria a resposta
            PlaylistTracksResponse response = new PlaylistTracksResponse();
            response.setPlaylistName(playlistInfo.getName()); // Define o nome da playlist
            response.setTotal(playlistInfo.getTracks().getTotal());
            response.setFollowers(playlistInfo.getFollowers().getTotal());
            response.setPlaylistOwner(playlistInfo.getOwner().getDisplay_name());
            response.setPlaylistId(playlistInfo.getId());

            // Lista para armazenar as informações simplificadas das músicas
            List<Map<String, String>> simplifiedTracks = new java.util.ArrayList<>();

            // Variável para armazenar a duração total em milissegundos
            long totalDurationMs = 0;

            // Percorre cada música na playlist
            if (playlistInfo != null && playlistInfo.getTracks() != null) {
                for (PlaylistInfoDTO.TrackItem trackItem : playlistInfo.getTracks().getItems()) {
                    if (trackItem != null && trackItem.getTrack() != null) {
                        TrackInfoDTO trackInfo = trackItem.getTrack();

                        Map<String, String> simplifiedInfo = new HashMap<>();
                        simplifiedInfo.put("name", trackInfo.getName());

                        // Verifica se há artistas disponíveis e pega o primeiro
                        if (trackInfo.getArtists() != null && trackInfo.getArtists().length > 0) {
                            simplifiedInfo.put("artist", trackInfo.getArtists()[0].getName());
                        } else {
                            simplifiedInfo.put("artist", "Artista Desconhecido");
                        }

                        // Verifica se há informações do álbum e da imagem
                        if (trackInfo.getAlbum() != null && trackInfo.getAlbum().getImages() != null && trackInfo.getAlbum().getImages().length > 0) {
                            simplifiedInfo.put("imageUrl", trackInfo.getAlbum().getImages()[0].getUrl());
                        } else {
                            simplifiedInfo.put("imageUrl", "URL não disponível");
                        }

                        if (trackInfo.getAlbum() != null) {
                            simplifiedInfo.put("album", trackInfo.getAlbum().getName());
                        } else {
                            simplifiedInfo.put("album", "Álbum desconhecido");
                        }

                        // Adiciona a duração formatada (mm:ss)
                        simplifiedInfo.put("duration", trackInfo.getFormattedDuration());

                        // Soma a duração da música ao total
                        totalDurationMs += trackInfo.getDurationMs();

                        simplifiedTracks.add(simplifiedInfo);
                    }
                }
            }

            response.setTracks(simplifiedTracks); // Define a lista de músicas na resposta

            // Preenche as imagens da playlist
            List<Map<String, String>> simplifiedImages = new java.util.ArrayList<>();
            if (playlistInfo.getImages() != null) {
                for (PlaylistInfoDTO.Image image : playlistInfo.getImages()) {
                    Map<String, String> imageInfo = new HashMap<>();
                    imageInfo.put("url", image.getUrl());
                    imageInfo.put("height", String.valueOf(image.getHeight()));
                    imageInfo.put("width", String.valueOf(image.getWidth()));
                    simplifiedImages.add(imageInfo);
                }
            }
            response.setImages(simplifiedImages); // Define a lista de imagens na resposta

            // Calcula a duração total da playlist no formato "Xh Ymin"
            response.setTotalDuration(formatDurationToHoursMinutes(totalDurationMs));

            return ResponseEntity.ok(response);
        } catch (PlaylistNotPublicException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Essa playlist não é pública e precisa de login");
        }
    }

    // Método para converter milissegundos em "Xh Ymin"
    private String formatDurationToHoursMinutes(long durationMs) {
        long seconds = durationMs / 1000;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;

        if (hours > 0) {
            return String.format("%dh %02dmin", hours, minutes);
        } else {
            return String.format("%dmin", minutes);
        }
    }

    // Método para converter milissegundos em "hh:mm:ss"
    private String formatDuration(long durationMs) {
        long seconds = durationMs / 1000;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long remainingSeconds = seconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
    }

    // Endpoint para baixar a música do YouTube como MP3
    @PostMapping("track/download")
    public void downloadTrack(@RequestBody Map<String, String> request, HttpServletResponse response) throws IOException {
        String trackName = request.get("name");
        String artistName = request.get("artist");
        spotifyService.downloadTrack(trackName, artistName, response); // Passa o HttpServletResponse
    }

    // Endpoint para baixar uma playlist
    @PostMapping("playlist/download")
    public void downloadPlaylist(@RequestBody Map<String, String> request, HttpServletResponse response) throws IOException {
        String playlistId = request.get("playlistId");
        spotifyService.downloadPlaylist(playlistId, response); // Passa o HttpServletResponse
    }

}
