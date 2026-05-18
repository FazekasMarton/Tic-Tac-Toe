import org.example.tictactoe.AI;
import org.example.tictactoe.Move;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AITest {
    private AI AI;

    @Before
    public void setUp() {
        AI = new AI();
    }

    @Test
    public void testRandomMove() {
        Character[][] table = {
                {'#', '#', '#'},
                {'#', '#', '#'},
                {'#', '#', null}
        };
        Move move = new Move(2, 2);
        assertEquals(move, AI.getRandomMove(table));
    }

    @Test
    public void testGetCloneAI() {
        AI clone = AI.getCloneAI();
        assertNotSame(clone, AI);
    }

    @Test
    public void testStepReturnsMove() {
        AI.newGame(true);

        Move move = AI.step(null, null, new Character[][]{
                {null, null, null},
                {null, null, null},
                {null, null, null}
        });

        assertNotNull(move);
    }

    @Test
    public void testSaveNewMemoryDoesNotCrash() {
        AI.newGame(true);

        AI.step(null, null, new Character[][]{
                {null, null, null},
                {null, null, null},
                {null, null, null}
        });

        AI.saveNewMemory(false, new Move(0, 0));

        assertTrue(true);
    }

    @Test
    public void testGetMemorySize(){
        assertEquals(18, AI.getMemorySize());
    }
}