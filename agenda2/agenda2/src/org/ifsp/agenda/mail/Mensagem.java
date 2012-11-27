package org.ifsp.agenda.mail;

/**
 *
 * Este classe é usuada pela classe Mensageiro para definir os 
 * atributos da mensagem de emvial, tais como para quem vai o email, o assunto
 * e a mensagem.
 * 
 * @author Ton
 */
public class Mensagem {

    private String para;
    private String assunto;
    private String mensagem;

    /**
     *
     * @return
     */
    public String getPara() {
        return para;
    }

    /**
     *
     * @param para
     */
    public void setPara(String para) {
        this.para = para;
    }

    /**
     *
     * @return
     */
    public String getAssunto() {
        return assunto;
    }

    /**
     *
     * @param assunto
     */
    public void setAssunto(String assunto) {
        this.assunto = assunto;
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
