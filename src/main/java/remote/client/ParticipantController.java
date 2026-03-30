package remote.client;

import remote.dto.GameHistoryDTO;
import game.Action;
import game.GameHistory;
import participants.Participant;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class ParticipantController {
    private Participant participant;

    public ParticipantController(Participant participant) {
        this.participant=participant;
    }
    
    @GetMapping("/name")
    public String getName() {
        return participant.getName();
    }
 
    @PostMapping("/action")
    public String getAction(@RequestBody GameHistoryDTO dto) {
     
        GameHistory history = dto.toGameHistory();
        Action chosenAction = participant.chooseAction(history);
        return chosenAction.getLabel();
    }
    @PostMapping("/reset")
    public void reset() {
    	participant.reset();
    }
}