package org.ifsp.agenda.exception;

/**
 *
 * @author Ton
 */
public class UsuarioDuplicadoException extends AgendaDAOException {

    private static final long serialVersionUID = 1L;

    /**
     *Está classe extende as Classe AgendaDAOException para verificar o Usuários duplicados
     */
    public UsuarioDuplicadoException() {
        super("Erro, Contato Duplicado!");
    }
}
