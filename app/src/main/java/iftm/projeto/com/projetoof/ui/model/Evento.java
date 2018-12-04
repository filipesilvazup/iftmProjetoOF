package iftm.projeto.com.projetoof.ui.model;

import java.io.Serializable;

public class Evento implements Serializable {
    private String titulo;
    private String endereco;
    private String hora;
    private String data;
    private String imagem;
    private String valorIngresso;
    private String key;
    private String classificacao;

    public Evento(String titulo, String endereco, String hora, String data, String imagem, String valorIngresso, String key,  String classificacao) {
        this.titulo = titulo;
        this.endereco = endereco;
        this.hora = hora;
        this.data = data;
        this.imagem = imagem;
        this.valorIngresso = valorIngresso;
        this.key = key;
        this.classificacao = classificacao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getValorIngresso() {
        return valorIngresso;
    }

    public void setValorIngresso(String valorIngresso) {
        this.valorIngresso = valorIngresso;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }
}
