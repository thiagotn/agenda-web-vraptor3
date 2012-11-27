/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ifsp.agenda.modelo;

/**
 *
 * Classe usuada para definir a informações de um Contato Profissional
 * que extende a classe contato para adicionar a esta classe novo atributos.
 * 
 * @author Ton
 */
public class ContatoProfissional extends Contato {

    private String nomeEmpresa;

    /**
     *
     */
    public ContatoProfissional() {
        super();
    }

    /**
     *
     * @return
     */
    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    /**
     *
     * @param nomeEmpresa
     */
    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }
}
