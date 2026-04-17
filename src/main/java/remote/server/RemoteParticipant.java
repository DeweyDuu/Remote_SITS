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
    private RestTemplate restTemplate;

    @Override
    public String getName() {
        return name;
    }
    public RemoteParticipant(String name, String clientUrl, Function<String, Action> actionFactory) {
        this.name = name;
        this.clientUrl = clientUrl;
        this.actionFactory = actionFactory;
        this.restTemplate = new RestTemplate();
    }

    public RemoteParticipant(String name, String clientUrl, Function<String, Action> actionFactory, RestTemplate restTemplate) {
        this.name = name;
        this.clientUrl = clientUrl;
        this.actionFactory = actionFactory;
        this.restTemplate = restTemplate; 
    }
    
    
    @Override
    public Action chooseAction(GameHistory history) {
    	GameHistoryDTO dto = GameHistoryDTO.fromGameHistory(history);
    	String label = restTemplate.postForObject(clientUrl + "/action", dto, String.class);
    	return actionFactory.apply(label);
}

    @Override
    public void reset() {
    	restTemplate.postForObject(clientUrl + "/reset", null, Void.class);
    }
}