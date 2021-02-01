package chess.game;

public class Move {

    private final Location start;
    private final Location end;

    public Move(char startRow, char startCol, char endRow, char endCol) throws BoardException {
        this.start = new Location(startRow, startCol);
        this.end = new Location(endRow, endCol);
    }

    public Location getStart() {
        return this.start;
    }

    public Location getEnd() {
        return this.end;
    }

    public int getDirectionRow() {
        return this.end.getRow() - this.start.getRow();
    }

    public int getDirectionCol() {
        return this.end.getCol() - this.start.getCol();
    }

    public boolean isNull() {
        return this.start.equals(this.end);
    }

    @Override
    public String toString() {
        return "" + start.toString() + end.toString();
    }

}
