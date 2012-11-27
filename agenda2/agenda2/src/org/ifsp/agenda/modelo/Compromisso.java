package org.ifsp.agenda.modelo;

/**
 *
 * Classe usuada para definir a informações de um compromisso.
 * 
 * 
 * @author Ton
 */
public class Compromisso {

    private int id;
    private String local;
    private String descricao;
    private String data;
    private String hora;
    private String contato;
    private String usuario;
    private int status;

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return
     */
    public int isStatus() {
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
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
    public String getLocal() {
        return local;
    }

    /**
     *
     * @param local
     */
    public void setLocal(String local) {
        this.local = local;
    }

    /**
     *
     * @return
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     *
     * @param descricao
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     *
     * @return
     */
    public String getData() {
        return data;
    }

    /**
     *
     * @param data
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     *
     * @return
     */
    public String getHora() {
        return hora;
    }

    /**
     *
     * @param hora
     */
    public void setHora(String hora) {
        this.hora = hora;
    }

    /**
     *
     * @return
     */
    public String getContato() {
        return contato;
    }

    /**
     *
     * @param contato
     */
    public void setContato(String contato) {
        this.contato = contato;
    }

    /**
     *
     * @return
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     *
     * @param usuario
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

}


