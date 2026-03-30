package remote.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;

import org.junit.jupiter.api.Test;

import game.Action;
import game.GameHistory;
import participants.Participant;
import remote.dto.GameHistoryDTO;
import remote.dto.RoundResultDTO;
import remote.dto.StringAction;

class ParticipantControllerTest {

    @Test
    void testControllerProperlyTranslatesAndDelegates() {
        Participant fakeBot = new Participant() {
            @Override
            public String getName() { return "TestBot"; }
            
            @Override
            public Action chooseAction(GameHistory history) { return new StringAction("COOPERATE"); }
            
            @Override
            public void reset() {}
        };

        ParticipantController controller = new ParticipantController(fakeBot);

        GameHistoryDTO incomingRequest = new GameHistoryDTO(
                "TestBot",
                "BadBot",
                List.of(new RoundResultDTO("COOPERATE", "DEFECT", 0, 5))
        );

        assertEquals("TestBot", controller.getName());
        assertEquals("COOPERATE", controller.getAction(incomingRequest));
        assertDoesNotThrow(() -> controller.reset());
    }
}