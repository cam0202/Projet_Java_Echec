package chess.game;

/**
 * 
 * classe BoardException
 * Construction des exceptions dans le jeu.
 *
 */
public class BoardException extends Exception {

    private static final long serialVersionUID = 1L;

    public BoardException(String message) {
        super(message);
    }

}
