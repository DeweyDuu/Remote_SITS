package remote.client;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.client.RestTestClient;

import remote.dto.GameHistoryDTO;
import remote.dto.RoundResultDTO;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = ClientApp.class)
@AutoConfigureRestTestClient
@ActiveProfiles("test")
class ClientTest {

    @Autowired
    private RestTestClient tClient;

    @Test
    void testClientInteraction()
    {
    	
        tClient.get().uri("/name")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Always Cooperate");

        GameHistoryDTO emptyHistory = new GameHistoryDTO("Always Cooperate", "Opponent", new ArrayList<>());

        tClient.post().uri("/action")
                .body(emptyHistory)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(action -> assertThat(action).isNotBlank());
        
        GameHistoryDTO withHistory = new GameHistoryDTO("Always Cooperate", "Opponent",
                List.of(new RoundResultDTO("COOPERATE", "DEFECT", 0, 5)));
        
        tClient.post().uri("/action")
                .body(withHistory)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("COOPERATE");
        
        tClient.post().uri("/reset")
                .exchange()
                .expectStatus().isOk();
    }
}
