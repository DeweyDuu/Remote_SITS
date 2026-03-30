package remote.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StringActionTest {

    @Test
    void testGetLabel() {
        StringAction action = new StringAction("DEFECT");
        assertEquals("DEFECT", action.getLabel());
    }
}