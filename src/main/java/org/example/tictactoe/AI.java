package org.example.tictactoe;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class AI {

    private Memory memory = new Memory();
    private MemorySlice newMemorySlice;
    private MemorySlice lastStep;
    private MemorySlice currentStep;
    private boolean started;
    protected char symbol = 'o';
    private final Random rand = new Random();

    /**
     * Új játék inicializálása.
     *
     * @param starter igaz, ha az AI kezd
     */
    public void newGame(boolean starter) {
        this.started = starter;
        newMemorySlice = null;
        lastStep = null;
        currentStep = null;
    }

    /**
     * Következő lépés meghatározása: ha van ismert jó lépés, azt adja,
     * különben random lépést választ.
     *
     * @param pRow játékos sor lépése (ha van)
     * @param pCol játékos oszlop lépése (ha van)
     * @param table aktuális játéktábla
     * @return AI lépés
     */
    public Move step(Integer pRow, Integer pCol, Character[][] table) {
        Move move = getRightMove(pRow, pCol);
        if (move != null) return move;
        return randomMove(pRow, pCol, table);
    }

    /**
     * Megpróbálja a memóriából kiválasztani a helyes lépést.
     *
     * @param pRow játékos sor lépése
     * @param pCol játékos oszlop lépése
     * @return következő AI lépés, vagy null ha nincs ismert út
     */
    public Move getRightMove(Integer pRow, Integer pCol) {
        Move playerMove = pRow == null ? null : new Move(pRow, pCol);

        if (currentStep == null && started) {
            ArrayList<MemorySlice> roots = memory.getGoingFirst();
            if (roots.isEmpty()) return null;
            MemorySlice selected = roots.get(rand.nextInt(roots.size()));
            currentStep = selected;
            newMemorySlice = new MemorySlice(selected.getMove());
            lastStep = newMemorySlice;
            return selected.getMove();
        }

        if (currentStep == null) {
            if (playerMove == null) return null;
            ArrayList<MemorySlice> roots = memory.getGoingSecond();
            for (MemorySlice root : roots) {
                if (root.getMove().equals(playerMove)) {
                    currentStep = root;
                    newMemorySlice = new MemorySlice(playerMove);
                    lastStep = newMemorySlice;

                    if (root.getNext().isEmpty()) return null;

                    MemorySlice aiNext = root.getNext().get(rand.nextInt(root.getNext().size()));
                    currentStep = aiNext;

                    MemorySlice aiNode = new MemorySlice(aiNext.getMove());
                    lastStep.addNext(aiNode);
                    lastStep = aiNode;

                    return aiNext.getMove();
                }
            }
            return null;
        }

        if (playerMove == null) return null;

        ArrayList<MemorySlice> next = currentStep.getNext();
        if (next == null || next.isEmpty()) return null;

        for (MemorySlice candidate : next) {
            if (candidate.getMove().equals(playerMove)) {
                currentStep = candidate;

                if (lastStep != null && !lastStep.getMove().equals(playerMove)) {
                    MemorySlice playerNode = new MemorySlice(playerMove);
                    lastStep.addNext(playerNode);
                    lastStep = playerNode;
                }

                if (candidate.getNext().isEmpty()) return null;

                MemorySlice aiNext = candidate.getNext().get(rand.nextInt(candidate.getNext().size()));
                currentStep = aiNext;

                MemorySlice aiNode = new MemorySlice(aiNext.getMove());
                lastStep.addNext(aiNode);
                lastStep = aiNode;

                return aiNext.getMove();
            }
        }

        return null;
    }

    /**
     * Random lépés generálása (ha nincs memóriaalapú döntés).
     *
     * @param pRow játékos sor
     * @param pCol játékos oszlop
     * @param table játéktábla
     * @return véletlen valid lépés
     */
    public Move randomMove(Integer pRow, Integer pCol, Character[][] table) {
        Move random = getRandomMove(table);

        if (currentStep == null && started) {
            newMemorySlice = new MemorySlice(random);
            lastStep = newMemorySlice;

        } else if (currentStep == null) {
            Move playerMove = new Move(pRow, pCol);
            newMemorySlice = new MemorySlice(playerMove);

            MemorySlice aiNode = new MemorySlice(random);
            newMemorySlice.addNext(aiNode);

            lastStep = aiNode;

        } else {
            Move playerMove = new Move(pRow, pCol);

            if (!lastStep.getMove().equals(playerMove)) {
                MemorySlice playerNode = new MemorySlice(playerMove);
                lastStep.addNext(playerNode);
                lastStep = playerNode;
            }

            MemorySlice aiNode = new MemorySlice(random);
            lastStep.addNext(aiNode);
            lastStep = aiNode;
        }

        return random;
    }

    /**
     * Véletlen valid lépés keresése a táblán.
     *
     * @param table játéktábla
     * @return üres mezőre mutató Move
     */
    public Move getRandomMove(Character[][] table) {
        ArrayList<Move> moves = new ArrayList<>();

        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                if (table[i][j] == null) moves.add(new Move(i, j));
            }
        }

        return moves.get(rand.nextInt(moves.size()));
    }

    /**
     * Mentés a tanuló memóriába a játék eredménye alapján.
     *
     * @param won nyert-e az AI
     * @param lastMove utolsó játékos lépés
     */
    public void saveNewMemory(boolean won, Move lastMove) {
        if (newMemorySlice == null) return;

        if (!won && lastMove != null)
            lastStep.addNext(new MemorySlice(lastMove));

        if (started) {
            if (won) memory.addGoingFirstMove(newMemorySlice);
            else memory.addGoingSecondMove(newMemorySlice);
        } else {
            if (won) memory.addGoingSecondMove(newMemorySlice);
            else memory.addGoingFirstMove(newMemorySlice);
        }
    }

    /**
     * Memória kiírása konzolra.
     */
    public void printMemory() {
        System.out.println("First:");
        for (MemorySlice s : memory.getGoingFirst())
            for (String x : s.toStringArray()) System.out.println(x);

        System.out.println("Second:");
        for (MemorySlice s : memory.getGoingSecond())
            for (String x : s.toStringArray()) System.out.println(x);
    }

    /**
     * @return memória mérete
     */
    public long getMemorySize() {
        long sum = 0;
        for (MemorySlice s : memory.getGoingFirst()) sum += s.toStringArray().size();
        for (MemorySlice s : memory.getGoingSecond()) sum += s.toStringArray().size();
        return sum;
    }

    /**
     * @return memória feltöltöttségének százaléka
     */
    public double getMemoryPercent() {
        long size = getMemorySize();
        long max = 510336;

        double percent = (double) size * 100 / max;
        return Math.round(percent * 100.0) / 100.0;
    }

    /**
     * AI másolat készítése (ellenfél AI-hoz).
     *
     * @return klónozott AI
     */
    public AI getCloneAI() {
        AI clone = new AI();
        clone.setMemory(this.memory);
        clone.symbol = 'x';
        return clone;
    }

    /**
     * Memória beállítása másik példányból.
     *
     * @param memory új memória
     */
    public void setMemory(Memory memory) {
        this.memory.setMemory(memory);
    }

    /**
     * Memória mentése fájlba.
     */
    public void saveMemory() {
        try {
            new ObjectMapper().writeValue(new File("memory.json"), memory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Memória betöltése fájlból.
     */
    public void loadMemory() {
        try {
            this.memory = new ObjectMapper().readValue(new File("memory.json"), Memory.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Memória törlése és alapállapot visszaállítása.
     */
    public void deleteMemory() {
        this.memory = new Memory();
        new File("memory.json").delete();
    }
}