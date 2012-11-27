package org.ifsp.agenda.modelo;

import java.io.Serializable;

/**
 *
 * Esta classe será usada para definir todos atributos para enviar emails para
 * os avisos de compromissos aos usuarios. Ela será usuada pela Classe Agendador
 * para configurar os emails antes de enviá-los.
 * 
 * @author Ton
 */
public class Aviso implements Serializable {

    private int compromissoId;
    private String usuarioNome;
    private String usuarioEmail;
    private String senhaGmail;
    private int usuarioAlerta;
    private String compromissoHora;
    private String compromissoData;
    private String compromissoLocal;
    private String compromissoDescricao;
    private int compromissoStatus;
    private String compromissoContato;

    /**
     *
     */
    public Aviso() {
    }

    /**
     *
     * @return
     */
    public String getUsuarioNome() {
        return usuarioNome;
    }

    /**
     *
     * @return
     */
    public int getCompromissoId() {
        return compromissoId;
    }

    /**
     *
     * @param compromissoId
     */
    public void setCompromissoId(int compromissoId) {
        this.compromissoId = compromissoId;
    }

    /**
     *
     * @param usuarioNome
     */
    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }

    /**
     *
     * @return
     */
    public String getUsuarioEmail() {
        return usuarioEmail;
    }

    /**
     *
     * @param usuarioEmail
     */
    public void setUsuarioEmail(String usuarioEmail) {
        this.usuarioEmail = usuarioEmail;
    }

    /**
     *
     * @return
     */
    public String getSenhaGmail() {
        return senhaGmail;
    }

    /**
     *
     * @param senhaGmail
     */
    public void setSenhaGmail(String senhaGmail) {
        this.senhaGmail = senhaGmail;
    }

    /**
     *
     * @return
     */
    public int getUsuarioAlerta() {
        return usuarioAlerta;
    }

    /**
     *
     * @param usuarioAlerta
     */
    public void setUsuarioAlerta(int usuarioAlerta) {
        this.usuarioAlerta = usuarioAlerta;
    }

    /**
     *
     * @return
     */
    public String getCompromissoHora() {
        return compromissoHora;
    }

    /**
     *
     * @param compromissoHora
     */
    public void setCompromissoHora(String compromissoHora) {
        this.compromissoHora = compromissoHora;
    }

    /**
     *
     * @return
     */
    public String getCompromissoData() {
        return compromissoData;
    }

    /**
     *
     * @param compromissoData
     */
    public void setCompromissoData(String compromissoData) {
        this.compromissoData = compromissoData;
    }

    /**
     *
     * @return
     */
    public String getCompromissoLocal() {
        return compromissoLocal;
    }

    /**
     *
     * @param compromissoLocal
     */
    public void setCompromissoLocal(String compromissoLocal) {
        this.compromissoLocal = compromissoLocal;
    }

    /**
     *
     * @return
     */
    public String getCompromissoDescricao() {
        return compromissoDescricao;
    }

    /**
     *
     * @param compromissoDescricao
     */
    public void setCompromissoDescricao(String compromissoDescricao) {
        this.compromissoDescricao = compromissoDescricao;
    }

    /**
     *
     * @return
     */
    public int CompromissoStatus() {
        return compromissoStatus;
    }

    /**
     *
     * @param compromissoStatus
     */
    public void setCompromissoStatus(int compromissoStatus) {
        this.compromissoStatus = compromissoStatus;
    }

    /**
     *
     * @return
     */
    public String getCompromissoContato() {
        return compromissoContato;
    }

    /**
     *
     * @param compromissoContato
     */
    public void setCompromissoContato(String compromissoContato) {
        this.compromissoContato = compromissoContato;
    }
}
