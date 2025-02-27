package com.exemplo.spotifydownloader.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaylistInfoDTO {

    private String name; // Nome da playlist
    private String description; // Descrição da playlist
    private Boolean isPublic; // Indica se a playlist é pública
    private String id; // ID da playlist
    private String type; // Tipo do objeto (playlist)
    private String uri; // URI do Spotify
    private String href; // Link da API

    @JsonProperty("snapshot_id")
    private String snapshotId; // ID do snapshot da playlist

    @JsonProperty("external_urls")
    private ExternalUrls externalUrls; // URLs externas

    private Owner owner; // Dono da playlist

    private Followers followers; // Informações de seguidores

    private List<Image> images; // Imagens da playlist

    private TracksInfo tracks; // Informações das músicas

    // Classe interna para representar URLs externas
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ExternalUrls {
        private String spotify;
    }

    // Classe interna para representar o dono da playlist
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Owner {
        private String display_name;
        private String id;
        private String type;
        private String uri;
        private ExternalUrls externalUrls;
    }

    // Classe interna para representar os seguidores
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Followers {
        private Integer total;
    }

    // Classe interna para representar as imagens
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Image {
        private String url;
        private Integer height;
        private Integer width;
    }

    // Classe interna para representar as músicas da playlist
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TracksInfo {
        @JsonProperty("items")
        private List<TrackItem> items; // Lista de músicas (TrackItem)
        private Integer total; // Total de músicas na playlist
    }

    // Classe interna para representar cada item da playlist (uma música)
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TrackItem {
        @JsonProperty("track")
        private TrackInfoDTO track; // Informações da música
    }
}