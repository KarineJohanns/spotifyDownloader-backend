package com.exemplo.spotifydownloader.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.exemplo.spotifydownloader.model.SpotifyTokenResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SpotifyAuthService {

    @Value("${spotify.client.id}")
    private String clientId;

    @Value("${spotify.client.secret}")
    private String clientSecret;

    public String getAccessToken() {
        RestTemplate restTemplate = new RestTemplate();

        // Configurar os headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Configurar o corpo da requisição
        String body = "grant_type=client_credentials";
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        // Fazer a requisição POST para obter o token
        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://accounts.spotify.com/api/token",
                request,
                String.class
        );

        // Verificar se a requisição foi bem-sucedida
        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                // Converter o JSON para a classe SpotifyTokenResponse
                ObjectMapper mapper = new ObjectMapper();
                SpotifyTokenResponse tokenResponse = mapper.readValue(response.getBody(), SpotifyTokenResponse.class);

                // Retornar o token de acesso
                return tokenResponse.getAccessToken();
            } catch (Exception e) {
                throw new RuntimeException("Erro ao processar o JSON de resposta", e);
            }
        } else {
            throw new RuntimeException("Falha ao obter token de acesso: " + response.getStatusCode());
        }
    }
}