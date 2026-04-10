package remote.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.context.WebServerInitializedEvent;
import org.springframework.context.event.EventListener;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication(scanBasePackages = {"remote", "participants"})//tell spring boot to look those folders
public class ClientApp {

    private int port;
    @Value("${tournament.server.url:http://localhost:8080}")
    private String serverUrl;

    @Value("${tournament.id:ipd1}")
    private String tournamentId;

    @Value("${participant.name:examplebot}")
    private String participantName;

    @Autowired
    private TournamentServerClient client;
    
   

    @EventListener
    public void onApplicationReady(WebServerInitializedEvent event) throws UnknownHostException {
    	this.port = event.getWebServer().getPort();
        String ip = InetAddress.getLocalHost().getHostAddress();
        try {
            client.register(tournamentId, participantName, ip, port);
        } catch (Exception e) {
            System.out.println("Could not reach tournament server: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
   
    	System.setProperty("server.port", "0");
        SpringApplication.run(ClientApp.class, args);
        
    }
    
}