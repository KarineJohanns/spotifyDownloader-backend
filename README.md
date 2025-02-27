# Spotify Downloader

Spotify Downloader é uma aplicação Spring Boot que permite obter informações de músicas e playlists do Spotify e baixá-las como arquivos MP3 através da integração com o YouTube.

## Funcionalidades
- Obter informações simplificadas de uma faixa do Spotify via ID ou URL
- Obter informações detalhadas de playlists públicas do Spotify
- Baixar músicas individuais como MP3 usando o YouTube
- Baixar playlists inteiras como arquivos ZIP contendo MP3s

## Tecnologias Utilizadas
- **Java 17**
- **Spring Boot**
- **Spring Web**
- **Spotify API**
- **YouTube DL (yt-dlp)**
- **FFmpeg**

## Pré-requisitos
- **Java 17+** instalado
- **FFmpeg** instalado e configurado no PATH
- **yt-dlp** instalado
- Conta de desenvolvedor no Spotify para obter as credenciais da API

## Configuração
1. Clone o repositório:
```sh
git clone https://github.com/seu-usuario/spotify-downloader.git
cd spotify-downloader
```

2. Configure as credenciais do Spotify em `application.properties`:
```properties
spotify.client.id=SEU_CLIENT_ID
spotify.client.secret=SEU_CLIENT_SECRET
yt-dlp.path=/caminho/para/yt-dlp
ffmpeg.path=/caminho/para/ffmpeg
```

3. Instale as dependências e execute a aplicação:
```sh
./mvnw spring-boot:run
```

## Endpoints Disponíveis
- `GET /api/token` - Retorna o token de acesso do Spotify
- `GET /api/track/{id}` - Retorna informações simplificadas de uma faixa pelo ID
- `GET /api/track?url={url}` - Retorna informações simplificadas de uma faixa pela URL
- `GET /api/playlist?url={url}` - Retorna informações simplificadas de uma playlist pela URL
- `POST /api/track/download` - Baixa uma faixa como MP3
- `POST /api/playlist/download` - Baixa uma playlist como ZIP contendo MP3s

## Exemplo de Requisição
```sh
curl -X GET 'http://localhost:8080/api/track?url=https://open.spotify.com/track/XXXXXXXXXXXX'
```

## Licença
Este projeto está licenciado sob a Licença MIT - veja o arquivo [LICENSE](LICENSE) para mais detalhes.
