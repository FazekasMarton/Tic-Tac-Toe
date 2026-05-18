import org.example.tictactoe.MemorySlice;
import org.example.tictactoe.Move;
import org.junit.Test;

import static org.junit.Assert.*;

public class MemorySliceTest {
    @Test
    public void testAddNext() {
        MemorySlice slice = new MemorySlice(new Move(0, 0));
        MemorySlice next = new MemorySlice(new Move(1, 1));

        slice.addNext(next);

        assertEquals(1, slice.getNext().size());
        assertEquals(next, slice.getNext().getFirst());
    }
}