//ANTES
package com.exemplo.spotifydownloader.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Service
public class YouTubeService {

    @Value("${yt-dlp.path}")
    private String ytDlpPath;

    @Value("${ffmpeg.path}")
    private String ffmpegPath;

    // Método para baixar um MP3 e transmitir diretamente para o HttpServletResponse
    public void downloadMp3(String query, HttpServletResponse response) throws IOException {
        Process process = null;
        InputStream processOutput = null;
        OutputStream responseOutput = null;

        try {
            // Comando para buscar e transmitir o áudio diretamente
            String command = String.format(
                    "\"%s\" -x --audio-format mp3 --audio-quality 0 --ffmpeg-location \"%s\" -o - --quiet \"ytsearch1:%s\"",
                    ytDlpPath, ffmpegPath, query
            );

            // Usa ProcessBuilder para maior controle sobre o processo
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            processBuilder.redirectErrorStream(true); // Redireciona stderr para stdout
            process = processBuilder.start();

            // Configura a resposta HTTP para streaming de áudio
            response.setContentType("audio/mpeg");
            response.setHeader("Content-Disposition", "attachment; filename=\"music.mp3\"");

            // Transmite a saída do processo diretamente para a resposta HTTP
            processOutput = process.getInputStream();
            responseOutput = response.getOutputStream();
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = processOutput.read(buffer)) != -1) {
                responseOutput.write(buffer, 0, bytesRead);
            }

            // Fecha os streams
            responseOutput.flush();
        } catch (IOException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao processar a requisição: " + e.getMessage());
        } finally {
            // Fecha os recursos
            if (processOutput != null) {
                try {
                    processOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (responseOutput != null) {
                try {
                    responseOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (process != null) {
                process.destroy(); // Encerra o processo
            }
        }
    }

    // Método auxiliar para baixar o MP3 e retornar o processo
    public Process downloadMp3ToStream(String query) throws IOException {
        String command = String.format(
                "\"%s\" -x --audio-format mp3 --audio-quality 0 --ffmpeg-location \"%s\" -o - --quiet \"ytsearch1:%s\"",
                ytDlpPath, ffmpegPath, query
        );
        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
        processBuilder.redirectErrorStream(true);
        return processBuilder.start();
    }


}