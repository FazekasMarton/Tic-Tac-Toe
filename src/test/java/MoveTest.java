import org.example.tictactoe.Move;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MoveTest {
    private Move move;

    @Before
    public void setUp() {
        move = new Move(3, 4);
    }

    @Test
    public void testMove() {
        Move m2 = new Move(3, 4);

        assertEquals(move, m2);
    }

    @Test
    public void testGetRow() {
        assertEquals(3, move.getRow());
    }

    @Test
    public void testSetRow() {
        move.setRow(4);

        assertEquals(4, move.getRow());
    }
}