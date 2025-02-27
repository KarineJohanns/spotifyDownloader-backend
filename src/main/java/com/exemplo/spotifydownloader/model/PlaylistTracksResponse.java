package com.exemplo.spotifydownloader.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class PlaylistTracksResponse {
    private String playlistName; // Nome da playlist
    private String playlistOwner; // Dono da playlist
    private Integer followers;
    private Integer total;
    private String totalDuration;
    private String playlistId;
    private List<Map<String, String>> tracks; // Lista de músicas com informações simplificadas
    private List<Map<String, String>> images; // Lista de imagens com informações simplificadas

}