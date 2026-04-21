package remote.server;

import game.IteratedPrisonersDilemma;
import game.PrisonerAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import remote.dto.RegistrationRequest;
import tournament.RoundRobin;
import tournament.TournamentResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TournamentServerControllerTest {

    private TournamentRegistry registry;
    private TournamentServerController controller;
    private NetworkedTournament tournament;

    @BeforeEach
    void setup() {
        registry = new TournamentRegistry();
        controller = new TournamentServerController(registry);
        tournament = new NetworkedTournament(
            "ipd1", "Test Tournament",
            new RoundRobin(),
            new IteratedPrisonersDilemma(1),
            PrisonerAction::valueOf
        );
        registry.add(tournament);
    }

    @Test
    void testGetTournamentsReturnsRegistering() {
        List<NetworkedTournament> result = controller.getTournaments();
        assertEquals(1, result.size());
        assertEquals("ipd1", result.get(0).getId());
    }
    
    @Test
    void testGetTournamentsEmptyAfterStart() {
        tournament.start();
        List<NetworkedTournament> result = controller.getTournaments();
        assertTrue(result.isEmpty());
    }

    @Test
    void testRegisterSuccess() {
        String response = controller.register("ipd1", new RegistrationRequest("p1", "127.0.0.1", 9001));
        assertEquals("successfully registered: p1", response);
    }

    @Test
    void testRegisterTournamentNotFound() {
        String response = controller.register("unknown", new RegistrationRequest("p1", "127.0.0.1", 9001));
        assertEquals("error: Tournament not found!", response);
    }

    @Test
    void testGetParticipantsAfterRegistration() {
        controller.register("ipd1", new RegistrationRequest("p1", "127.0.0.1", 9001));
        controller.register("ipd1", new RegistrationRequest("p2", "127.0.0.1", 9002));

        List<String> participants = controller.getParticipants("ipd1");
        assertEquals(2, participants.size());
        assertTrue(participants.contains("p1"));
        assertTrue(participants.contains("p2"));
    }

    @Test
    void testGetParticipantsTournamentNotFound() {
        List<String> participants = controller.getParticipants("unknown");
        assertTrue(participants.isEmpty());
    }

    @Test
    void testGetResultBeforeTournamentIsNull() {
        TournamentResult result = controller.getResult("ipd1");
        assertNull(result);
    }

    @Test
    void testGetResultAfterTournament() {
        tournament.start();
        TournamentResult result = controller.getResult("ipd1");
        assertNotNull(result);
    }

    @Test
    void testStartTournamentNotFoundReturnsNull() {
        TournamentResult result = controller.start("unknown");
        assertNull(result);
    }

    @Test
    void testNullForUnkown() {
        TournamentResult result = controller.getResult("unknown");
        assertNull(result);
    }

    @Test
    void testStart() {
        TournamentResult result = controller.start("ipd1");
        assertNotNull(result);
    }
}
