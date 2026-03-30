package remote.dto;

import game.GameHistory;
import game.RoundResult;
import java.util.ArrayList;
import java.util.List;

public class GameHistoryDTO {
    public String nameP1;
    public String nameP2;
    public List<RoundResultDTO> rounds;

    public GameHistoryDTO() {}

    public GameHistoryDTO(String nameP1, String nameP2, List<RoundResultDTO> rounds) {
        this.nameP1 = nameP1;
        this.nameP2 = nameP2;
        this.rounds = rounds;
    }


    public static GameHistoryDTO fromGameHistory(GameHistory h) {
        List<RoundResultDTO> dtos = new ArrayList<>();
        
        for (int i = 0; i < h.getRounds().size(); i++) {
            RoundResult r = h.getRounds().get(i);
            dtos.add(new RoundResultDTO(
                r.getActionP1().getLabel(),
                r.getActionP2().getLabel(),
                r.getScoreP1(),
                r.getScoreP2()
            ));
        }
        
        return new GameHistoryDTO(h.getP1Name(), h.getP2Name(), dtos);
    }

    public GameHistory toGameHistory() {
        GameHistory h = new GameHistory(nameP1, nameP2);
        
        if (rounds != null) {
            for (int i = 0; i < rounds.size(); i++) {
                RoundResultDTO r = rounds.get(i);
                int roundNum = i + 1;                 
                h.getRounds().add(new RoundResult(
                    new StringAction(r.actionP1),
                    new StringAction(r.actionP2),
                    r.scoreP1,
                    r.scoreP2,
                    roundNum
                ));
            }
        }
        return h;
    }
}