package com.exemplo.spotifydownloader.service;

public class SpotifyUtils {

    public static String extractTrackId(String url) {
        // Verifica se o link contém "/track/"
        if (url.contains("/track/")) {
            // Remove tudo antes do ID
            String[] parts = url.split("/track/");
            // Pega a parte após "/track/" e remove qualquer parâmetro após "?"
            String id = parts[1].split("\\?")[0];
            return id;
        } else {
            throw new IllegalArgumentException("Link do Spotify inválido.");
        }
    }

    public static String extractPlaylistId(String url) {
        // Verifica se o link contém "/playlist/"
        if (url.contains("/playlist/")) {
            // Remove tudo antes do ID
            String[] parts = url.split("/playlist/");
            // Pega a parte após "/playlist/" e remove qualquer parâmetro após "?"
            String id = parts[1].split("\\?")[0];
            return id;
        } else {
            throw new IllegalArgumentException("Link do Spotify inválido.");
        }
    }
}