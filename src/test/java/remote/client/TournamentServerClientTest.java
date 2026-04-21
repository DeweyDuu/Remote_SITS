package remote.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import remote.server.NetworkedTournament;

class TournamentServerClientTest {

    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;
    private TournamentServerClient client;

    @BeforeEach
    void setup() {
        restTemplate = new RestTemplate();
        // This allows us to simulate server responses and verify client behavior
        //without requiring a live network connection
        mockServer = MockRestServiceServer.createServer(restTemplate);
        client = new TournamentServerClient("http://localhost:8080", restTemplate);
    }
    
    @Test
    void testlistTournaments() {
        mockServer.expect(requestTo("http://localhost:8080/tournaments"))
                  .andRespond(withSuccess(
                      "[{\"id\":\"ipd1\",\"name\":\"Tournament 1\",\"status\":\"REGISTERING\"}," +
                       "{\"id\":\"ipd2\",\"name\":\"Tournament 2\",\"status\":\"REGISTERING\"}]",
                      MediaType.APPLICATION_JSON
                  ));
        
        List<NetworkedTournament> result = client.listTournaments();
        
        assertEquals(2, result.size());
        assertEquals("ipd1", result.get(0).getId());
        assertEquals("ipd2", result.get(1).getId());
    }
    
    @Test
    void testEmptyList() {
        mockServer.expect(requestTo("http://localhost:8080/tournaments"))
                  .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));
        
        List<NetworkedTournament> result = client.listTournaments();
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testListTournamentsNullResponse() {
        mockServer.expect(requestTo("http://localhost:8080/tournaments"))
                  .andRespond(withSuccess("null", MediaType.APPLICATION_JSON));

        List<NetworkedTournament> result = client.listTournaments();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testRegisterCallsServer() {
        mockServer.expect(requestTo("http://localhost:8080/tournaments/ipd1/register"))
                  .andRespond(withSuccess("successfully registered: p1", MediaType.TEXT_PLAIN));
        client.register("ipd1", "p1", "127.0.0.1", 9001);
        mockServer.verify(); // confirm POST was made to the correct URL
    }
}
