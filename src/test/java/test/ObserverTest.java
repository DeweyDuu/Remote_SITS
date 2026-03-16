package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.io.File;

import game.*;
import participants.*;
import observer.*;
import tournament.*;

public class ObserverTest {
	
	@Test
    public void testMoveEventGetters() {
        RoundResult rr = new RoundResult(PrisonerAction.COOPERATE, PrisonerAction.DEFECT, 0, 5, 1);
        GameHistory gh = new GameHistory("P1", "P2");
        MoveEvent event = new MoveEvent(rr, gh);
        
        assertEquals(rr, event.getRoundResult());
        assertEquals(gh, event.getGameHistory());
    }

    @Test
    public void testLogging() {
        String moveFile = "test_moves.txt";
        String scoreFile = "test_scores.txt";
        
        MoveLogger moveLogger = new MoveLogger(moveFile);
        ScoreLogger scoreLogger = new ScoreLogger(scoreFile);
        
        Game game = new IteratedPrisonersDilemma(1);
        game.addObserver(moveLogger);
        game.addObserver(scoreLogger);
        
        Participant p1 = new AlwaysCooperate();
        Participant p2 = new AlwaysDefect();
        game.play(p1, p2);
        
        TournamentFormat tournament = new RoundRobin();
        tournament.run(Arrays.asList(p1, p2), game);
        
        File mFile = new File(moveFile);
        File sFile = new File(scoreFile);
        
        assertTrue(mFile.exists());
        assertTrue(sFile.exists());
        
        // Clean up
        mFile.delete();
        sFile.delete();
    }

    @Test
    public void testLoggerInvalidPath() {
        ScoreLogger badScoreLogger = new ScoreLogger("");
        MoveLogger badMoveLogger = new MoveLogger("");
        Participant p1 = new AlwaysCooperate();
        Participant p2 = new AlwaysDefect();
        
       
        Game game = new IteratedPrisonersDilemma(1);
        GameResult gr = game.play(p1, p2);
        
        TournamentFormat tournament = new RoundRobin();
        TournamentResult tr = tournament.run(Arrays.asList(p1, p2), game);
        
        RoundResult rr = new RoundResult(PrisonerAction.COOPERATE, PrisonerAction.DEFECT, 0, 5, 1);
        GameHistory gh = new GameHistory(p1.getName(), p2.getName());
        MoveEvent event = new MoveEvent(rr, gh);
      
        assertThrows(NullPointerException.class, () -> {
            badScoreLogger.onGameOver(gr);
        });
        
        assertThrows(NullPointerException.class, () -> {
            badScoreLogger.onTournamentOver(tr);
        });

        assertThrows(NullPointerException.class, () -> {
            badMoveLogger.onMoveMade(event);
        });
        
        assertThrows(NullPointerException.class, () -> {
            badMoveLogger.onTournamentOver(tr);
        });
        

    }
    @Test
    //Scenario where we have a solid path but fail to write
    public void testWrite() {
        ScoreLogger badScoreLogger = new ScoreLogger("score_write.txt");
        MoveLogger badMoveLogger = new MoveLogger("score_move.txt");
        Participant p1 = new AlwaysCooperate();
        Participant p2 = new AlwaysDefect();
        
       
        Game game = new IteratedPrisonersDilemma(1);
        GameResult gr = game.play(p1, p2);
        
        TournamentFormat tournament = new RoundRobin();
        TournamentResult tr = tournament.run(Arrays.asList(p1, p2), game);
        
        RoundResult rr = new RoundResult(PrisonerAction.COOPERATE, PrisonerAction.DEFECT, 0, 5, 1);
        GameHistory gh = new GameHistory(p1.getName(), p2.getName());
        MoveEvent event = new MoveEvent(rr, gh);
        badScoreLogger.onTournamentOver(tr);
        badScoreLogger.onGameOver(gr);
        badMoveLogger.onTournamentOver(tr);
        badMoveLogger.onMoveMade(event);
        

    }
    
}