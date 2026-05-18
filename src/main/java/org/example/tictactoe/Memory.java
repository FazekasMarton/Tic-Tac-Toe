package org.example.tictactoe;

import java.util.ArrayList;

public class Memory {

    private ArrayList<MemorySlice> goingFirst;
    private ArrayList<MemorySlice> goingSecond;

    /**
     * Létrehozza az üres alap memóriát mindkét kezdési helyzethez.
     */
    public Memory() {
        this.goingFirst = generateBaseMemory();
        this.goingSecond = generateBaseMemory();
    }

    /**
     * Létrehozza a 3x3-as alap lépéskészletet.
     *
     * @return alap memória lista (minden mező egy Move-val)
     */
    public ArrayList<MemorySlice> generateBaseMemory() {
        ArrayList<MemorySlice> baseMemory = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                baseMemory.add(new MemorySlice(new Move(i, j)));
            }
        }

        return baseMemory;
    }

    /**
     * @return az elsőként lépő memóriájának másolata
     */
    public ArrayList<MemorySlice> getGoingFirst() {
        ArrayList<MemorySlice> copy = new ArrayList<>();
        for (MemorySlice slice : this.goingFirst) {
            copy.add(cloneSlice(slice));
        }
        return copy;
    }

    /**
     * @return a másodikként lépő memóriájának másolata
     */
    public ArrayList<MemorySlice> getGoingSecond() {
        ArrayList<MemorySlice> copy = new ArrayList<>();
        for (MemorySlice slice : this.goingSecond) {
            copy.add(cloneSlice(slice));
        }
        return copy;
    }

    /**
     * @param move új lépés hozzáadása az elsőként lépő memóriájához
     */
    public void addGoingFirstMove(MemorySlice move) {
        ArrayList<MemorySlice> route = new ArrayList<>();
        route.add(cloneSlice(move));
        placeRoute(route, goingFirst);
    }

    /**
     * @param move új lépés hozzáadása a másodikként lépő memóriájához
     */
    public void addGoingSecondMove(MemorySlice move) {
        ArrayList<MemorySlice> route = new ArrayList<>();
        route.add(cloneSlice(move));
        placeRoute(route, goingSecond);
    }

    /**
     * Felülírja a teljes memóriát egy másik memória másolatával.
     *
     * @param memory a forrás memória
     */
    public void setMemory(Memory memory) {
        this.goingFirst = cloneList(memory.getGoingFirst());
        this.goingSecond = cloneList(memory.getGoingSecond());
    }

    /**
     * @param list klónozandó lista
     * @return mély másolat a listáról
     */
    private ArrayList<MemorySlice> cloneList(ArrayList<MemorySlice> list) {
        ArrayList<MemorySlice> copy = new ArrayList<>();
        for (MemorySlice slice : list) {
            copy.add(cloneSlice(slice));
        }
        return copy;
    }

    /**
     * Rekurzívan elhelyez egy lépéssorozatot a memóriában.
     *
     * @param newRoute az új útvonal
     * @param root a cél gyökér lista
     */
    public void placeRoute(ArrayList<MemorySlice> newRoute, ArrayList<MemorySlice> root) {
        for (MemorySlice newSlice : newRoute) {

            MemorySlice existing = null;

            for (MemorySlice r : root) {
                if (r.getMove().equals(newSlice.getMove())) {
                    existing = r;
                    break;
                }
            }

            if (existing == null) {
                root.add(cloneSlice(newSlice));
            } else {
                placeRoute(newSlice.getNext(), existing.getNext());
            }
        }
    }

    /**
     * Mély másolatot készít egy MemorySlice-ról.
     *
     * @param slice az eredeti elem
     * @return klónozott elem
     */
    private MemorySlice cloneSlice(MemorySlice slice) {
        MemorySlice copy = new MemorySlice(slice.getMove());

        for (MemorySlice child : slice.getNext()) {
            copy.getNext().add(cloneSlice(child));
        }

        return copy;
    }
}