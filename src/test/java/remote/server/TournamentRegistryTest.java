package remote.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import game.IteratedPrisonersDilemma;
import game.PrisonerAction;
import tournament.RoundRobin;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TournamentRegistryTest {

    private TournamentRegistry registry;

    @BeforeEach
    public void setup() {
        registry = new TournamentRegistry();
    }

    @Test
    public void testAddGetTournament() {
    	
        NetworkedTournament testGame = new NetworkedTournament(
                "test", 
                "Test Game", 
                new RoundRobin(), 
                new IteratedPrisonersDilemma(1), 
                PrisonerAction::valueOf
        ); 

      
        registry.add(testGame);

        NetworkedTournament regiesteredGame = registry.get("test");
        assertNotNull(regiesteredGame);
        
        assertEquals("test", regiesteredGame.getId());
        assertEquals("Test Game", regiesteredGame.getName());
    }

    @Test
    public void testRegiesterOpenGames() {
       
        NetworkedTournament openGame = new NetworkedTournament(
                "open", "Open Gmae", new RoundRobin() , new IteratedPrisonersDilemma(1), PrisonerAction::valueOf
        );
        
        NetworkedTournament closedGame = new NetworkedTournament(
                "closed", "Closed Game", new RoundRobin(), new IteratedPrisonersDilemma(1), PrisonerAction::valueOf
        ); 
        
        closedGame.start(); 
        registry.add(openGame);
        registry.add(closedGame);

        
        List<NetworkedTournament> game_avail = registry.listRegistering();

        assertEquals(1, game_avail.size());
        assertEquals("open", game_avail.get(0).getId());
    }
}