package remote.server;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.test.web.servlet.client.RestTestClient;

import remote.dto.RegistrationRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = ServerApp.class)
@AutoConfigureRestTestClient
class ServerTest {

    @Autowired
    private TournamentRegistry registry;

    @Autowired
    private RestTestClient tClient;

    @Test
    void testRegistrationInteraction()
    {
        tClient.get().uri("/tournaments")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(body -> assertThat(body).contains("ipd1"));
        tClient.get().uri("/tournaments/ipd1/participants")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("[]");

        tClient.get().uri("/tournaments/ipd1/result")
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();

        tClient.post().uri("/tournaments/ipd1/register")
                .body(new RegistrationRequest("p1", "127.0.0.1", 9001))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("successfully registered: p1");


        tClient.get().uri("/tournaments/ipd1/participants")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(body -> assertThat(body).contains("p1"));

        tClient.post().uri("/tournaments/ipd1/register")
                .body(new RegistrationRequest("p2", "127.0.0.1", 9002))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("successfully registered: p2");

        tClient.get().uri("/tournaments/ipd1/participants")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(body -> {
                    assertThat(body).contains("p1");
                    assertThat(body).contains("p2");
                });

        tClient.post().uri("/tournaments/unknown/register")
                .body(new RegistrationRequest("p1", "127.0.0.1", 9001))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("error: Tournament not found!");

        // check internal state directly
        assertThat(registry.get("ipd1")).isNotNull();
        assertThat(registry.get("ipd1").getParticipantNames()).contains("p1", "p2");
        assertThat(registry.get("ipd1").getStatus()).isEqualTo(TournamentStatus.REGISTERING);
    }
}
