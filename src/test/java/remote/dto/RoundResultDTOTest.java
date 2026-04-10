package remote.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoundResultDTOTest {

    @Test
    void testConstructorSetsFields() {
        RoundResultDTO dto = new RoundResultDTO("COOPERATE", "DEFECT", 0, 5);

        assertEquals("COOPERATE", dto.actionP1);
        assertEquals("DEFECT", dto.actionP2);
        assertEquals(0, dto.scoreP1);
        assertEquals(5, dto.scoreP2);
    }

    @Test
    void testDefaultConstructor() {
        RoundResultDTO dto = new RoundResultDTO();

        assertNull(dto.actionP1);
        assertNull(dto.actionP2);
        assertEquals(0, dto.scoreP1);
        assertEquals(0, dto.scoreP2);
    }

    @Test
    void testBothDefect() {
        RoundResultDTO dto = new RoundResultDTO("DEFECT", "DEFECT", 1, 1);

        assertEquals("DEFECT", dto.actionP1);
        assertEquals("DEFECT", dto.actionP2);
        assertEquals(1, dto.scoreP1);
        assertEquals(1, dto.scoreP2);
    }
}
