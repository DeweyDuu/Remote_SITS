package remote.server;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class TournamentRegistry {
   
    private Map<String, NetworkedTournament> tournaments = new HashMap<>();

    public void add(NetworkedTournament t) {
        tournaments.put(t.getId(), t);
    }

    public NetworkedTournament get(String id) {
        return tournaments.get(id);
    }

 
    public List<NetworkedTournament> listRegistering() {
        List<NetworkedTournament> t_available = new ArrayList<>();
        
        for (NetworkedTournament t : tournaments.values()) {

            if (t.getStatus() == TournamentStatus.REGISTERING) {
                t_available.add(t);
            }
        }
        return t_available;
    }
}