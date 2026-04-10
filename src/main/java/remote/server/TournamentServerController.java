package remote.server;


import org.springframework.web.bind.annotation.*;
import remote.dto.RegistrationRequest;
import tournament.TournamentResult;
import java.util.List;

@RestController
@RequestMapping("/tournaments")
public class TournamentServerController {
    private TournamentRegistry registry;
    
    public TournamentServerController(TournamentRegistry registry) {
        this.registry = registry;
    }

    @GetMapping
    public List<NetworkedTournament> getTournaments() {
        return registry.listRegistering();
    }

    @PostMapping("/{id}/register")
    public String register(@PathVariable String id, @RequestBody RegistrationRequest req) {
        NetworkedTournament tournament = registry.get(id);
        
        
        if (tournament != null) {
            tournament.addRemoteParticipant(req);
            return "successfully registered: " + req.name;
        } else {
            return "error: Tournament not found!";
        }
    }
    @GetMapping("/{id}/participants")
    public List<String> getParticipants(@PathVariable String id) {
        NetworkedTournament tournament = registry.get(id);
        if (tournament != null) {
            return tournament.getParticipantNames();
        }
        return List.of();
    }

    @GetMapping("/{id}/result")
    public TournamentResult getResult(@PathVariable String id) {
        NetworkedTournament tournament = registry.get(id);
        if (tournament != null) {
            return tournament.getResult();
        }
        return null;
    }

    @PostMapping("/{id}/start")
    public TournamentResult start(@PathVariable String id) {
        NetworkedTournament tournament = registry.get(id);
        if (tournament != null) {
            return tournament.start();
        }
        return null; 
    
    }
}