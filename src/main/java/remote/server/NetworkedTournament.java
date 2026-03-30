package remote.server;

import game.Game;
import game.Action;
import participants.Participant;
import remote.dto.RegistrationRequest;
import tournament.TournamentFormat;
import tournament.TournamentResult;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class NetworkedTournament {
	
	private Function<String, Action> actionFactory;

    private String id;
    private String name;
    private TournamentFormat format;
    private Game game;
    private List<Participant> participants;
    private TournamentStatus status;

    public NetworkedTournament(String id, String name, TournamentFormat format, Game game,Function<String, Action> actionFactory) {
        this.id = id;
        this.name = name;
        this.format = format;
        this.game = game;
        this.participants = new ArrayList<>();
        this.status = TournamentStatus.REGISTERING;
        this.actionFactory = actionFactory;
        
    }

    public String getId(){
    	return id;
    	}
    public String getName(){
    	return name;
    	}
    public TournamentStatus getStatus() {
    	return status;
    	}

   
    public void addRemoteParticipant(RegistrationRequest req) {
    	
        if (this.status == TournamentStatus.REGISTERING) {
            String clientUrl = "http://" + req.ip + ":" + req.port;
            RemoteParticipant remotePlayer = new RemoteParticipant(
                req.name, 
                clientUrl,
                this.actionFactory
            );
            this.participants.add(remotePlayer);
            System.out.println("new remote participant :" + req.name + " at " + clientUrl);
        } 
        else {
            System.out.println("can't to register, tournament in process " + name);
        }
    }


    public TournamentResult start() {
    	if (this.status != TournamentStatus.REGISTERING) {
    		System.out.println(" tournament failed to start. It is already running or completed.");
            return null;
    	}
        this.status = TournamentStatus.RUNNING;
        TournamentResult result = format.run(participants, game);
        this.status = TournamentStatus.COMPLETED;
        return result;
    }
}