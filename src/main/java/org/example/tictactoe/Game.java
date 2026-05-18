package org.example.tictactoe;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Game {

    @FXML
    private GridPane board;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Text percentText;

    private Character[][] table;
    private int round = 0;
    private final AI ai = new AI();

    /**
     * JavaFX inicializáláskor betölti a memóriát és elindít egy új játékot.
     */
    @FXML
    private void initialize() {
        ai.loadMemory();
        newGame();
    }

    /**
     * Új játékot indít, és alaphelyzetbe állítja a táblát és az AI állapotát.
     */
    private void newGame() {
        this.resetBoard();
        showMemory();

        System.out.println("New Game");
        ai.printMemory();

        Random rand = new Random();

        if (rand.nextBoolean()) {
            try {
                ai.newGame(true);
                aiPlace(null, null, ai);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ai.newGame(false);
        }
    }

    /**
     * Játéktábla alaphelyzetbe állítása.
     */
    private void resetBoard() {
        for (Node node : board.getChildren()) {
            if (node instanceof Button button) {
                button.setText("");
            }
        }

        table = new Character[3][3];
        round = 0;
    }

    /**
     * Memória állapot megjelenítése a felületen.
     */
    private void showMemory() {
        double percent = ai.getMemoryPercent();
        percentText.setText(percent + "% (" + ai.getMemorySize() + ")");
        progressBar.setProgress(percent);
    }

    /**
     * Egy lépést helyez el a táblán.
     *
     * @param row sor index
     * @param col oszlop index
     * @param symbol lerakott jel
     * @throws Exception ha a mező már foglalt
     */
    private void step(int row, int col, Character symbol) throws Exception {
        if (table[row][col] == null) {
            table[row][col] = symbol;
            getButton(row, col).setText(symbol.toString());
            round++;
        } else {
            throw new Exception("Cell already occupied!");
        }
    }

    /**
     * Eldönti, hogy a legutóbbi lépés győzelmet eredményezett-e.
     *
     * @param row sor index
     * @param col oszlop index
     */
    private boolean isWin(int row, int col) {
        Character symbol = table[row][col];
        return isHorizontalWin(row, symbol)
                || isVerticalWin(col, symbol)
                || isDiagonalWin(row, col, symbol);
    }

    /**
     * Sorban ellenőrzi a győzelmet.
     *
     * @param row sor index
     * @param symbol vizsgált jel
     */
    private boolean isHorizontalWin(int row, Character symbol) {
        for (int i = 0; i < 3; i++) {
            if (table[row][i] != symbol) return false;
        }
        return true;
    }

    /**
     * Oszlopban ellenőrzi a győzelmet.
     *
     * @param col oszlop index
     * @param symbol vizsgált jel
     */
    private boolean isVerticalWin(int col, Character symbol) {
        for (int i = 0; i < 3; i++) {
            if (table[i][col] != symbol) return false;
        }
        return true;
    }

    /**
     * Átlós győzelem ellenőrzése.
     *
     * @param row sor index
     * @param col oszlop index
     * @param symbol vizsgált jel
     */
    private boolean isDiagonalWin(int row, int col, Character symbol) {
        return isDiagonalDownWin(row, col, symbol)
                || isDiagonalUpWin(row, col, symbol);
    }

    /**
     * Főátló ellenőrzése.
     *
     * @param row sor index
     * @param col oszlop index
     * @param symbol vizsgált jel
     */
    private boolean isDiagonalDownWin(int row, int col, Character symbol) {
        if (row - col != 0) return false;

        for (int i = 0; i < 3; i++) {
            if (table[i][i] != symbol) return false;
        }
        return true;
    }

    /**
     * Mellékátló ellenőrzése.
     *
     * @param row sor index
     * @param col oszlop index
     * @param symbol vizsgált jel
     */
    private boolean isDiagonalUpWin(int row, int col, Character symbol) {
        if (row + col != 2) return false;

        for (int i = 0; i < 3; i++) {
            if (table[2 - i][i] != symbol) return false;
        }
        return true;
    }

    /**
     * Új játék indítása UI gombból.
     */
    @FXML
    private void startNewGame() {
        newGame();
    }

    /**
     * Felhasználói kattintás kezelése.
     *
     * @param event kattintás esemény
     */
    @FXML
    private void place(ActionEvent event) {
        Button button = (Button) event.getSource();
        int row = GridPane.getRowIndex(button);
        int col = GridPane.getColumnIndex(button);

        try {
            playerPlace(row, col);
            Move playerLastMove = new Move(row, col);

            boolean isEnd = checkResult(playerLastMove, true, true);

            if (!isEnd) {
                checkResult(aiPlace(row, col, ai), true, false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Játékos lépése.
     *
     * @param row sor index
     * @param col oszlop index
     * @throws Exception ha a mező foglalt
     */
    private void playerPlace(int row, int col) throws Exception {
        step(row, col, 'x');
    }

    /**
     * AI lépés végrehajtása.
     *
     * @param pRow játékos utolsó sora
     * @param pCol játékos utolsó oszlopa
     * @param aiInPlay aktív AI
     */
    private Move aiPlace(Integer pRow, Integer pCol, AI aiInPlay) throws Exception {
        Move AIMove = aiInPlay.step(pRow, pCol, table);
        step(AIMove.getRow(), AIMove.getCol(), aiInPlay.symbol);
        return AIMove;
    }

    /**
     * Játék végeredmény ellenőrzése.
     *
     * @param lastMove utolsó lépés
     * @param dialog popup megjelenjen-e
     * @param isPlayer játékos lépett-e
     */
    private boolean checkResult(Move lastMove, boolean dialog, boolean isPlayer) {
        boolean isWin = isWin(lastMove.getRow(), lastMove.getCol());
        boolean isEnd = false;

        if (isWin) {
            if (isPlayer) {
                ai.saveNewMemory(false, lastMove);
                if (dialog) showResultDialog("Nyertél!");
            } else {
                ai.saveNewMemory(true, null);
                if (dialog) showResultDialog("Vesztettél!");
            }
            isEnd = true;
        }

        if (round >= 9) {
            if (dialog) showResultDialog("Döntetlen!");
            isEnd = true;
        }

        return isEnd;
    }

    /**
     * Eredmény popup megjelenítése.
     *
     * @param message kiírt üzenet
     */
    private void showResultDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Játék vége");
        alert.setHeaderText(message);
        alert.setContentText("Szeretnél új játékot?");

        ButtonType yes = new ButtonType("Igen");
        ButtonType no = new ButtonType("Nem");

        alert.getButtonTypes().setAll(yes, no);

        alert.showAndWait().ifPresent(response -> {
            if (response == yes) startNewGame();
            else System.exit(0);
        });
    }

    /**
     * Gomb lekérése koordináták alapján.
     *
     * @param row sor index
     * @param col oszlop index
     */
    private Button getButton(int row, int col) {
        for (Node node : board.getChildren()) {

            Integer r = GridPane.getRowIndex(node);
            Integer c = GridPane.getColumnIndex(node);

            if (r == null) r = 0;
            if (c == null) c = 0;

            if (node instanceof Button && r == row && c == col) {
                return (Button) node;
            }
        }
        return null;
    }

    /**
     * AI önmagával játszik és tanul.
     *
     * @param event UI esemény
     */
    @FXML
    private void selfTeach(ActionEvent event) {

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Öntanítás");

        Label label = new Label("Az AI tanul...");
        VBox root = new VBox(label);
        root.setPadding(new Insets(20));

        stage.setScene(new Scene(root, 250, 100));

        Task<Void> task = new Task<>() {

            @Override
            protected Void call() throws Exception {

                try {
                    while (!isCancelled()) {

                        AI ai2 = ai.getCloneAI();

                        ai.printMemory();
                        showMemory();

                        Platform.runLater(() -> {
                            resetBoard();
                            ai.newGame(true);
                            ai2.newGame(false);
                        });

                        Thread.sleep(10);

                        boolean isEnd = false;
                        Move lastMove = null;

                        for (int i = 0; i < 9; i++) {

                            if (isCancelled()) return null;
                            if (isEnd) break;

                            AI current = i % 2 == 0 ? ai : ai2;

                            final Move previousMove = lastMove;
                            final AI currentAI = current;

                            CountDownLatch latch = new CountDownLatch(1);

                            Platform.runLater(() -> {
                                try {
                                    Move move = previousMove == null
                                            ? aiPlace(null, null, currentAI)
                                            : aiPlace(previousMove.getRow(), previousMove.getCol(), currentAI);

                                    movesHolder[0] = move;
                                    resultHolder[0] = checkResult(move, false, currentAI == ai2);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    latch.countDown();
                                }
                            });

                            latch.await();

                            lastMove = movesHolder[0];
                            isEnd = resultHolder[0];

                            Thread.sleep(100);
                        }
                    }

                } finally {
                    Platform.runLater(() -> newGame());
                }

                return null;
            }

            private final Move[] movesHolder = new Move[1];
            private final boolean[] resultHolder = new boolean[1];
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        stage.setOnCloseRequest(e -> task.cancel());
        stage.showAndWait();
    }

    /**
     * Memória törlése.
     *
     * @param event UI esemény
     */
    @FXML
    public void deleteMemory(ActionEvent event) {
        ai.deleteMemory();
        this.newGame();
    }

    /**
     * Program leállításakor memória mentése.
     */
    public void stop() {
        ai.saveMemory();
    }
}