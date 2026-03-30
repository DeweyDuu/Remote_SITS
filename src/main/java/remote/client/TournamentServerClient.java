package remote.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import remote.dto.RegistrationRequest;
import java.util.Arrays;
import java.util.List;

import remote.server.NetworkedTournament; 


@Service
public class TournamentServerClient {
	
	@Value("${tournament.server.url:http://localhost:8080}")
    private String serverUrl;
   
    public List<NetworkedTournament> listTournaments() {
        String targetUrl = serverUrl + "/tournaments";
        RestTemplate rest = new RestTemplate();
        NetworkedTournament[] activeTournaments = rest.getForObject(targetUrl, NetworkedTournament[].class);
        if (activeTournaments != null) {
            return Arrays.asList(activeTournaments);
        } else {
            return Arrays.asList();
        }
    }

    public void register(String tournamentId, String name, String ip, int port) {
        
        RegistrationRequest request = new RegistrationRequest(name, ip, port);
        String targetUrl = serverUrl + "/tournaments/" + tournamentId + "/register";

        RestTemplate rest = new RestTemplate();
        rest.postForObject(targetUrl, request, String.class);
        
        System.out.println("Tournament regiestered");
    }
}