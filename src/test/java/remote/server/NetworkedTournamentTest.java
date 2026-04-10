package remote.server;

import game.IteratedPrisonersDilemma;
import game.PrisonerAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import remote.dto.RegistrationRequest;
import tournament.RoundRobin;
import tournament.TournamentResult;

import static org.junit.jupiter.api.Assertions.*;

class NetworkedTournamentTest {

    private NetworkedTournament tournament;

    @BeforeEach
    void setup() {
        tournament = new NetworkedTournament(
            "ipd1", "Test Tournament",
            new RoundRobin(),
            new IteratedPrisonersDilemma(1),
            PrisonerAction::valueOf
        );
    }

    @Test
    void testInitialStatus() {
        assertEquals(TournamentStatus.REGISTERING, tournament.getStatus());
    }

    @Test
    void testGetIdAndName() {
        assertEquals("ipd1", tournament.getId());
        assertEquals("Test Tournament", tournament.getName());
    }

    @Test
    void testAddParticipantWhenRegistering() {
        tournament.addRemoteParticipant(new RegistrationRequest("p1", "127.0.0.1", 9001));
        tournament.addRemoteParticipant(new RegistrationRequest("p2", "127.0.0.1", 9002));

        assertEquals(2, tournament.getParticipantNames().size());
        assertTrue(tournament.getParticipantNames().contains("p1"));
        assertTrue(tournament.getParticipantNames().contains("p2"));
    }

    @Test
    void testAddParticipantRejectedAfterStart() {
        // start with no participants — returns null but transitions status
        tournament.start();
        assertEquals(TournamentStatus.COMPLETED, tournament.getStatus());

        // now try to register — should be rejected
        tournament.addRemoteParticipant(new RegistrationRequest("LateBot", "127.0.0.1", 9003));
        assertFalse(tournament.getParticipantNames().contains("LateBot"));
    }

    @Test
    void testStartTransitionsStatus() {
        tournament.start();
        assertEquals(TournamentStatus.COMPLETED, tournament.getStatus());
    }

    @Test
    void testStartWhenAlreadyCompletedReturnsNull() {
        tournament.start();
        TournamentResult secondCall = tournament.start();
        assertNull(secondCall);
    }

    @Test
    void testResultIsNullBeforeStart() {
        assertNull(tournament.getResult());
    }

    @Test
    void testResultAvailableAfterStart() {
        tournament.start();
        assertNotNull(tournament.getResult());
    }

    @Test
    void testParticipantNamesEmptyInitially() {
        assertTrue(tournament.getParticipantNames().isEmpty());
    }
}
