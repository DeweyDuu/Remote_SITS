package remote.client;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import participants.AlwaysCooperate;
import participants.Participant;

@TestConfiguration
public class TestParticipantConfig {

    @Bean(name = "humanParticipant")
    @Primary
    public Participant participant() {
        return new AlwaysCooperate();
    }
}
