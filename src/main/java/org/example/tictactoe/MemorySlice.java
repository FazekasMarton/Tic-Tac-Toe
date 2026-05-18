package org.example.tictactoe;

import java.util.ArrayList;

public class MemorySlice {

    private Move move;
    private ArrayList<MemorySlice> nextMoves = new ArrayList<>();

    /**
     * @param move az aktuális lépés
     */
    public MemorySlice(Move move) {
        this.move = move;
    }

    /**
     * @return az aktuális lépés
     */
    public Move getMove() {
        return move;
    }

    /**
     * @param move az új lépés
     */
    public void setMove(Move move) {
        this.move = move;
    }

    /**
     * @return a következő lehetséges lépések listája
     */
    public ArrayList<MemorySlice> getNext() {
        return nextMoves;
    }

    /**
     * @param nextMoves az új következő lépések listája
     */
    public void setNext(ArrayList<MemorySlice> nextMoves) {
        this.nextMoves = nextMoves;
    }

    /**
     * @param next a hozzáadandó következő lépés
     */
    public void addNext(MemorySlice next) {
        nextMoves.add(next);
    }

    /**
     * Kiírja a memóriafa összes útvonalát a konzolra.
     */
    public void print() {
        for (String s : this.toStringArray()) {
            System.out.println(s);
        }
    }

    /**
     * Szöveges formában visszaadja az összes lehetséges lépéssorozatot.
     *
     * @return a lépéssorozatok listája
     */
    public ArrayList<String> toStringArray() {
        ArrayList<String> strings = new ArrayList<>();

        if (nextMoves.isEmpty()) {
            strings.add(move == null ? "" : move.toString());
            return strings;
        }

        for (MemorySlice next : nextMoves) {
            for (String nextString : next.toStringArray()) {

                if (move == null) {
                    strings.add(nextString);
                } else {
                    strings.add(move + " -> " + nextString);
                }
            }
        }

        return strings;
    }
}