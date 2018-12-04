package iftm.projeto.com.projetoof.ui.model;

import java.io.Serializable;

public class UserDatabase implements Serializable {
    private String email;
    private String senha;
    private String tipo; //0 notUserPermission, 1 admin, 2 userNotPayment, 3 userPayment
    private String uId;
    private String key;

    public UserDatabase(String key, String email, String senha, String tipo, String uId) {
        this.key = key;
        this.email = email;
        this.senha = senha;
        this.tipo = tipo;
        this.uId = uId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUid() {
        return uId;
    }

    public void setUid(String uId) {
        this.uId = uId;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
