package com.exemplo.spotifydownloader.exceptions;

public class PlaylistNotPublicException extends RuntimeException {
    public PlaylistNotPublicException(String message) {
        super(message);
    }
}