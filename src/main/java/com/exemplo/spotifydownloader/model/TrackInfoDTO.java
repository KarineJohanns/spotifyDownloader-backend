package com.exemplo.spotifydownloader.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true) // Ignora campos desconhecidos
public class TrackInfoDTO {

    private String name; // Nome da música

    @JsonProperty("artists") // Mapeia o array de artistas
    private ArtistDTO[] artists;

    @JsonProperty("album") // Mapeia o objeto album
    private AlbumDTO album;

    @JsonProperty("duration_ms")
    private long durationMs; // Duração em milissegundos

    @JsonProperty("id")
    private String id; // ID da música

    @JsonProperty("popularity")
    private int popularity; // Popularidade da música (0 a 100)

    @JsonProperty("explicit")
    private boolean explicit; // Se a música é explícita

    @JsonProperty("external_urls")
    private ExternalUrlsDTO externalUrls; // URLs externas (por exemplo, link para o Spotify)

    @JsonProperty("href")
    private String href; // Link para a API do Spotify

    @JsonProperty("uri")
    private String uri; // URI da música no Spotify

    // Método utilitário para formatar a duração
    public String getFormattedDuration() {
        long totalSeconds = durationMs / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    // Método utilitário para obter o nome do artista principal
    public String getMainArtist() {
        return artists != null && artists.length > 0 ? artists[0].getName() : "Desconhecido";
    }

    // Método utilitário para obter a URL da imagem do álbum
    public String getAlbumImageUrl() {
        return album != null && album.getImages() != null && album.getImages().length > 0
                ? album.getImages()[0].getUrl()
                : null;
    }

    // Classe interna para representar o artista
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true) // Ignora campos desconhecidos
    public static class ArtistDTO {
        private String name; // Nome do artista

        @JsonProperty("external_urls")
        private ExternalUrlsDTO externalUrls; // URLs externas do artista

    }

    // Classe interna para representar o álbum
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true) // Ignora campos desconhecidos
    public static class AlbumDTO {
        private String name; // Nome do álbum

        @JsonProperty("images") // Mapeia o array de imagens
        private ImageDTO[] images;

        @JsonProperty("release_date")
        private String releaseDate; // Data de lançamento do álbum

        @JsonProperty("total_tracks")
        private int totalTracks; // Número total de faixas no álbum
    }

    // Classe interna para representar a imagem
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true) // Ignora campos desconhecidos
    public static class ImageDTO {
        private String url; // URL da imagem
    }

    // Classe interna para representar URLs externas
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true) // Ignora campos desconhecidos
    public static class ExternalUrlsDTO {
        @JsonProperty("spotify")
        private String spotify; // Link para o Spotify
    }
}