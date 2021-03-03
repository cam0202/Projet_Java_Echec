package chess.game;

/**
 * 
 * Location Class 
 * sert à representer une coordonnee sur l'echiquier
 *
 */
public class Location {

    private final int row;
    private final int col;
    
    /**
     * Location constructeur
     * @param letter
     * @param number
     * @throws BoardException
     */
    public Location(char letter, char number) throws BoardException {
        this.row = this.convertRow(letter);
        this.col = this.convertCol(number);
    }
    
    /**
     * Location constructeur
     * @param row
     * @param col
     * @throws BoardException
     */
    public Location(int row, int col) throws BoardException {
        if (!(0 <= row && row < 8)) {
            throw new BoardException("row " + row + " is invalid");
        }

        if (!(0 <= col && col < 8)) {
            throw new BoardException("col " + col + " is invalid");
        }

        this.row = row;
        this.col = col;
    }
    
    /**
     * getLetter, donne la lettre du déplacement
     * @return char
     * @throws BoardException
     */
    public char getLetter() throws BoardException {
        return this.convertRow(this.row);
    }
    
    /**
     * getNumber, numéro du déplacement 
     * @return char
     * @throws BoardException
     */
    public char getNumber() throws BoardException {
        return this.convertCol(this.col);
    }
    
    /**
     * getRow 
     * @return int
     */
    public int getRow() {
        return this.row;
    }
    
    /**
     * getCol
     * @return int
     */
    public int getCol() {
        return this.col;
    }

    /**
     * Fonction de conversion
     * @throws BoardException
     */
    
    private char convertRow(int row) throws BoardException {
        switch (row) {
            case 0:
                return 'a';
            case 1:
                return 'b';
            case 2:
                return 'c';
            case 3:
                return 'd';
            case 4:
                return 'e';
            case 5:
                return 'f';
            case 6:
                return 'g';
            case 7:
                return 'h';
            default:
                throw new BoardException("row " + row + " is invalid");
        }
    }

    private char convertCol(int col) throws BoardException {
        switch (col) {
            case 0:
                return '1';
            case 1:
                return '2';
            case 2:
                return '3';
            case 3:
                return '4';
            case 4:
                return '5';
            case 5:
                return '6';
            case 6:
                return '7';
            case 7:
                return '8';
            default:
                throw new BoardException("col " + col + " is invalid");
        }
    }

    private int convertRow(char row) throws BoardException {
        switch (row) {
            case 'a':
                return 0;
            case 'b':
                return 1;
            case 'c':
                return 2;
            case 'd':
                return 3;
            case 'e':
                return 4;
            case 'f':
                return 5;
            case 'g':
                return 6;
            case 'h':
                return 7;
            default:
                throw new BoardException("row " + row + " is invalid");
        }
    }

    private int convertCol(char col) throws BoardException {
        switch (col) {
            case '1':
                return 0;
            case '2':
                return 1;
            case '3':
                return 2;
            case '4':
                return 3;
            case '5':
                return 4;
            case '6':
                return 5;
            case '7':
                return 6;
            case '8':
                return 7;
            default:
                throw new BoardException("col " + col + " is invalid");
        }
    }

    @Override
    public String toString() {
        try {
            return "" + convertRow(this.row) + convertCol(this.col);
        } catch (BoardException e) {
            return super.toString();
        }
    }

    @Override
    public boolean equals(Object obj) {
        Location other = (Location) obj;
        return (this.row == other.row) && (this.col == other.col);
    }
}
