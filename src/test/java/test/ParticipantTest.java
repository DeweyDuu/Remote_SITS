package test;
import participants.*;
import game.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ParticipantTest {

    @Test
    public void testAlwaysCooperate() {
        Participant niceBot = new AlwaysCooperate();
        GameHistory emptyHistory = new GameHistory("Bot 1", "Bot 2");
        Action choice = niceBot.chooseAction(emptyHistory);
        assertEquals(PrisonerAction.COOPERATE, choice, "AlwaysCooperate should always return COOPERATE");
    }

    @Test
    public void testAlwaysDefect() {

        Participant meanBot = new AlwaysDefect();
        GameHistory emptyHistory = new GameHistory("Bot 1", "Bot 2");

        Action choice = meanBot.chooseAction(emptyHistory);

        assertEquals(PrisonerAction.DEFECT, choice, "AlwaysDefect should always return DEFECT");
    }
    
    @Test
    public void testTitForTatMirrorsHistory() {
        Participant titForTat = new TitForTat();
        GameHistory history = new GameHistory("P1", "TitForTat");
        assertEquals(PrisonerAction.COOPERATE, titForTat.chooseAction(history), 
                     "TitForTat should cooperate on the very first turn.");

        history.getRounds().add(new RoundResult(PrisonerAction.COOPERATE, PrisonerAction.DEFECT, 0, 5, 1));
        assertEquals(PrisonerAction.DEFECT, titForTat.chooseAction(history), 
                     "TitForTat should mirror the opponent's previous cooperate");
    }
}