package remote.server;

import game.Action;
import game.GameHistory;
import participants.Participant;
import org.springframework.web.client.RestTemplate;
import remote.dto.GameHistoryDTO;

import java.util.function.Function;

public class RemoteParticipant implements Participant {

    private String name;
    private String clientUrl; 
    private Function<String, Action> actionFactory;
   
    @Override
    public String getName() {
        return name;
    }

    public RemoteParticipant(String name, String clientUrl, Function<String, Action> actionFactory) {
        this.name = name;
        this.clientUrl = clientUrl;
        this.actionFactory = actionFactory;
    }

    @Override
    public Action chooseAction(GameHistory history) {
    	GameHistoryDTO dto = GameHistoryDTO.fromGameHistory(history);
    	RestTemplate rest = new RestTemplate();
    	String lable = rest.postForObject(clientUrl + "/action", dto, String.class);
    	return actionFactory.apply(lable);
}

    @Override
    public void reset() {
    	RestTemplate rest = new RestTemplate();
        rest.postForObject(clientUrl + "/reset", null, Void.class);
    }
}