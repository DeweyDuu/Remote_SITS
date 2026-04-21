package remote.server;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import game.Action;
import game.GameHistory;
import game.PrisonerAction;

class RemoteParticipantTest {

    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;
    private RemoteParticipant participant;

    @BeforeEach
    void setup() {
        restTemplate = new RestTemplate();
        //Since our design choose to use RestTemplate and we don't have a running client server during the unit test
        //seems using MockRestServiceServer will be a good way to test RestTemplate after discussing
        //It is a tool to recieve calls from RestTemplate and return
        //Use the expected responses, allowing us to test RemoteParticipant's behavior without
        // a live network connection.
        mockServer = MockRestServiceServer.createServer(restTemplate);
        // Use the 4-param constructor to input the RestTemplate
        participant = new RemoteParticipant("p1", "http://localhost:9001", PrisonerAction::valueOf, restTemplate);
    }

    @Test
    void testgetName() {
        assertEquals("p1", participant.getName());
    }

    @Test
    void testChooseActionCoo() {
        mockServer.expect(requestTo("http://localhost:9001/action"))
                  .andRespond(withSuccess("COOPERATE", MediaType.TEXT_PLAIN));
        GameHistory history = new GameHistory("p1", "p2");
        Action action = participant.chooseAction(history);
        assertEquals(PrisonerAction.COOPERATE, action);

    }

    @Test
    void testChooseActionReturnsDef() {
        mockServer.expect(requestTo("http://localhost:9001/action"))
                  .andRespond(withSuccess("DEFECT", MediaType.TEXT_PLAIN));
        GameHistory history = new GameHistory("p1", "p2");
        Action action = participant.chooseAction(history);
        assertEquals(PrisonerAction.DEFECT, action);
    }

    @Test
    void testReset() {
        mockServer.expect(requestTo("http://localhost:9001/reset"))
                  .andRespond(withSuccess());

        participant.reset();
        mockServer.verify(); // check the POST to /reset was actually made
    }
}
