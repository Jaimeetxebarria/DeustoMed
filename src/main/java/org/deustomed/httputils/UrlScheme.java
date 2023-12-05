package org.deustomed.httputils;

public enum UrlScheme {
    HTTP, HTTPS;

    public int getDefaultPort() {
        return switch (this) {
            case HTTP -> 80;
            case HTTPS -> 443;
        };
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
