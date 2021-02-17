package chess.game;

/**
 * 
 * Move Class
 * Sert à représenter un déplacement sur le plateau de jeu
 *
 */
public class Move {

    private final Location start;
    private final Location end;

    public Move(char startRow, char startCol, char endRow, char endCol) throws BoardException {
        this.start = new Location(startRow, startCol);
        this.end = new Location(endRow, endCol);
    }
    
    /**
     * getStart, position de départ 
     * @return Location
     */
    public Location getStart() {
        return this.start;
    }
    
    /**
     * getEnd, position de fin 
     * @return Location
     */
    public Location getEnd() {
        return this.end;
    }
    
    /**
     * getDirectionRow
     * @return int
     */
    public int getDirectionRow() {
        return this.end.getRow() - this.start.getRow();
    }
    
    /**
     * getDirectionCol
     * @return int
     */
    public int getDirectionCol() {
        return this.end.getCol() - this.start.getCol();
    }
    
    /**
	 * isNull, permet de savoir si la pièce ce déplace 
	 * @return boolean
	 */
    public boolean isNull() {
        return this.start.equals(this.end);
    }

    @Override
    public String toString() {
        return "" + start.toString() + end.toString();
    }

}
