/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ifsp.agenda.modelo;

import java.util.Date;

/**
 *
 * Classe usuada para definir a informações de um Contato pessoal
 * que extende a classe contato para adicionar a esta classe novo
 * atributos.
 *
 * @author Ton
 */
public class ContatoPessoal extends Contato {

    private Date dataNascimento;

    /**
     *
     * @return
     */
    public Date getDataNascimento() {
        return dataNascimento;
    }

    /**
     *
     * @param dataNascimento
     */
    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
    private String celular;

    /**
     *
     * @return
     */
    public String getCelular() {
        return celular;
    }

    /**
     *
     * @param celular
     */
    public void setCelular(String celular) {
        this.celular = celular;
    }

    /**
     *
     */
    public ContatoPessoal() {
        super();
    }
}
