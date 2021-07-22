package com.educacao.salvador.conexao.model;

/* Classe de configuração dos dados retornados pelo json de vídeos de uma playlists */

public class VideoListDataModel {

    private String id;
    private String titulo;
    private String descricao;
    private String publishedAt;
    private String thumbnailURL;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() { return descricao; }

    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getPublishedAt() { return publishedAt; }

    public void setPublishedAt(String publishedAt) { this.publishedAt = publishedAt; }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

}
