import org.example.tictactoe.Memory;
import org.example.tictactoe.MemorySlice;
import org.example.tictactoe.Move;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class MemoryTest {
    Memory memory;

    @Before
    public void setUp() {
        memory = new Memory();
    }

    @Test
    public void testGenerateBaseMemorySize() {
        ArrayList<MemorySlice> base = memory.generateBaseMemory();

        assertEquals(9, base.size());
    }

    @Test
    public void testAddGoingSecondMove() {
        MemorySlice m1 = new MemorySlice(new Move(0, 0));
        m1.addNext(new MemorySlice(new Move(0, 1)));
        m1.addNext(new MemorySlice(new Move(0, 2)));

        memory.addGoingSecondMove(m1);

        assertEquals(2, memory.getGoingSecond().getFirst().getNext().size());
    }
}