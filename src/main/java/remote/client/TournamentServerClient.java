package remote.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import remote.dto.RegistrationRequest;
import java.util.Arrays;
import java.util.List;

import remote.server.NetworkedTournament; 


@Service
public class TournamentServerClient {

    private String serverUrl;
    private RestTemplate restTemplate;

    @Autowired
    public TournamentServerClient(
            @Value("${tournament.server.url:http://localhost:8080}") String serverUrl) {
        this.serverUrl = serverUrl;
        this.restTemplate = new RestTemplate();
    }
    //testing
    public TournamentServerClient(String serverUrl, RestTemplate restTemplate) {
        this.serverUrl = serverUrl;
        this.restTemplate = restTemplate;
    }

    public List<NetworkedTournament> listTournaments() {
        String targetUrl = serverUrl + "/tournaments";
        NetworkedTournament[] activeTournaments = restTemplate.getForObject(targetUrl, NetworkedTournament[].class);
        if (activeTournaments != null) {
            return Arrays.asList(activeTournaments);
        } else {
            return Arrays.asList();
        }
    }

    public void register(String tournamentId, String name, String ip, int port) {
        RegistrationRequest request = new RegistrationRequest(name, ip, port);
        String targetUrl = serverUrl + "/tournaments/" + tournamentId + "/register";
        restTemplate.postForObject(targetUrl, request, String.class);
        System.out.println("Tournament registered");
    }
}