package org.ifsp.agenda.modelo;

/**
 *
 * Classe usuada para definir a informações de um usuario.
 *
  * @author Ton
 */
public class Usuario {

    private int id;
    private String login;
    private String senha;
    private String nome;
    private String email;
    private String senhaEmail;
    private int alerta;

    /**
     *
     * @return
     */
    public String getSenhaEmail() {
        return senhaEmail;
    }

    /**
     *
     * @param senhaEmail
     */
    public void setSenhaEmail(String senhaEmail) {
        this.senhaEmail = senhaEmail;
    }

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     */
    public int getAlerta() {
        return alerta;
    }

    /**
     *
     * @param alerta
     */
    public void setAlerta(int alerta) {
        this.alerta = alerta;
    }

    /**
     *
     * @return
     */
    public String getLogin() {
        return login;
    }

    /**
     *
     * @param login
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     *
     * @return
     */
    public String getSenha() {
        return senha;
    }

    /**
     *
     * @param senha
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     *
     * @return
     */
    public String getNome() {
        return nome;
    }

    /**
     *
     * @param nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
}
