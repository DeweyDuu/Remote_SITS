package remote.dto;

import game.GameHistory;
import game.RoundResult;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class GameHistoryDTOTest {

	@Test
    void testFromHistory() {

        GameHistory history = new GameHistory("tftBot", "DefectBot");
        history.getRounds().add(new RoundResult(new StringAction("COOPERATE"),new StringAction("DEFECT"), 0,5,1));

        history.getRounds().add(new RoundResult(new StringAction("DEFECT"), new StringAction("DEFECT"),1, 1, 2));

        GameHistoryDTO dto = GameHistoryDTO.fromGameHistory(history);

        assertEquals("tftBot", dto.nameP1);
        assertEquals("DefectBot", dto.nameP2);

        assertNotNull(dto.rounds);
        assertEquals(2, dto.rounds.size());

        RoundResultDTO round1 = dto.rounds.get(0);
        assertEquals("COOPERATE", round1.actionP1);
        assertEquals("DEFECT", round1.actionP2);
        assertEquals(0, round1.scoreP1);
        assertEquals(5, round1.scoreP2);

        RoundResultDTO round2 = dto.rounds.get(1);
        assertEquals("DEFECT", round2.actionP1);
        assertEquals("DEFECT", round2.actionP2);
        assertEquals(1, round2.scoreP1);
        assertEquals(1, round2.scoreP2);
    }
	@Test
    void testToGameHistory() {
        List<RoundResultDTO> rounddtos = new ArrayList<>();
        
        rounddtos.add(new RoundResultDTO("COOPERATE", "DEFECT", 0, 5));
        rounddtos.add(new RoundResultDTO("DEFECT", "DEFECT", 1, 1));

        GameHistoryDTO dto = new GameHistoryDTO("tftBot", "DefectBot", rounddtos);

        GameHistory history = dto.toGameHistory();

        assertEquals("tftBot", history.getP1Name());
        assertEquals("DefectBot", history.getP2Name());

        List<RoundResult> rounds = history.getRounds();
        assertEquals(2, rounds.size());

        RoundResult round1 = rounds.get(0);
        assertEquals("COOPERATE", round1.getActionP1().getLabel());
        assertEquals("DEFECT", round1.getActionP2().getLabel());
        assertEquals(0, round1.getScoreP1());
        assertEquals(5, round1.getScoreP2());

        RoundResult round2 = rounds.get(1);
        assertEquals("DEFECT", round2.getActionP1().getLabel());
        assertEquals("DEFECT", round2.getActionP2().getLabel());
        assertEquals(1, round2.getScoreP1());
        assertEquals(1, round2.getScoreP2());
    }
}