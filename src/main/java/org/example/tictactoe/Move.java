package org.example.tictactoe;

import java.util.Objects;

public class Move {

    private int row;
    private int col;

    /**
     * Létrehoz egy új lépést a megadott koordinátákkal.
     *
     * @param row a sor indexe
     * @param col az oszlop indexe
     */
    public Move(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return a sor indexe
     */
    public int getRow() {
        return row;
    }

    /**
     * @param row az új sor index
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * @return az oszlop indexe
     */
    public int getCol() {
        return col;
    }

    /**
     * @param col az új oszlop index
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * @param o az összehasonlítandó objektum
     * @return true, ha a két objektum egyenlő
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return row == move.row && col == move.col;
    }

    /**
     * @return a generált hash kód
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    /**
     * @return a lépés szöveges reprezentációja
     */
    @Override
    public String toString() {
        return "(" + this.row + "-" + this.col + ")";
    }
}