package org.ifsp.agenda.modelo;

/**
 *
 * Classe usuada para definir a informações das mensagens de erro
 * atilizadas para exibir mensagem de erros no site inteiro através
 * do servlets.
 *
 * @author Ton
 */
public class MensagemErro {

    private String categoria;
    private String mensagem;

    /**
     *
     */
    public MensagemErro() {
    }

    /**
     *
     * @param categoria
     * @param mensagem
     */
    public MensagemErro(String categoria, String mensagem) {
        this.categoria = categoria;
        this.mensagem = mensagem;
    }

    /**
     *
     * @return
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     *
     * @param categoria
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     *
     * @return
     */
    public String getMensagem() {
        return mensagem;
    }

    /**
     *
     * @param mensagem
     */
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
