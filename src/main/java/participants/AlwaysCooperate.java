package participants;

import game.Action;
import game.GameHistory;
import game.PrisonerAction;
import org.springframework.stereotype.Service;

@Service
public class AlwaysCooperate implements Participant {
    
    @Override 
    public String getName() { 
        return "Always Cooperate"; 
    } 
    
    @Override 
    public void reset() {
    } 

    @Override
    public Action chooseAction(GameHistory h) { 
        return PrisonerAction.COOPERATE;
    }
}