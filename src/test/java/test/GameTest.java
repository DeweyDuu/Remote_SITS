package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import game.*;
import observer.GameObserver;
import observer.MoveEvent;
import participants.*;
public class GameTest {

    @Test
    public void testPrisonersDilemmaPayoffs() {
        Game game = new IteratedPrisonersDilemma(10);
        Participant p1 = new AlwaysCooperate();
        Participant p2 = new AlwaysDefect();
        GameHistory history = new GameHistory(p1.getName(), p2.getName());

        RoundResult result = game.doRound(p1, p2, history);


        assertEquals(PrisonerAction.COOPERATE, result.getActionP1());
        assertEquals(PrisonerAction.DEFECT, result.getActionP2(), "Player 2 defected");
        assertEquals(0, result.getScoreP1());
        assertEquals(5, result.getScoreP2());
    }
    
    @Test
    public void testGameIsOverCondition() {

        Game game = new IteratedPrisonersDilemma(2);
        GameHistory history = new GameHistory("P1", "P2");
        assertFalse(game.isOver(history));

        history.getRounds().add(new RoundResult(PrisonerAction.COOPERATE, PrisonerAction.COOPERATE, 3, 3, 1));
        history.getRounds().add(new RoundResult(PrisonerAction.COOPERATE, PrisonerAction.COOPERATE, 3, 3, 2));
        assertTrue(game.isOver(history));
    }
    @Test
    public void testGetPayoffThrowsExceptionOnBadAction() {
        Game game = new IteratedPrisonersDilemma(1);
        GameHistory history = new GameHistory("P1", "P2");
        
        Action wrongAction = new Action() {
            @Override
            public String getLabel() { return "WRONG_MOVE"; }
        };
        Participant wrongBot = new Participant() {
            @Override public String getName() { return "WrongBot"; }
            @Override public void reset() {}
            @Override public Action chooseAction(GameHistory h) { return wrongAction; }
        };
        Participant niceBot = new AlwaysCooperate();

        // Prove that trying to play a round with a wrong action crashes the game correctly
        assertThrows(IllegalArgumentException.class, () -> {
            game.doRound(wrongBot, niceBot, history);
        });
    }
    
    
    //testing the observer pattern and the game loop
    @Test
    public void testFullGameLoopAndObservers() {
        // Start a game with 2 rounds
        Game game = new IteratedPrisonersDilemma(2);
        Participant p1 = new AlwaysCooperate();
        Participant p2 = new AlwaysDefect();
        
        GameObserver testObserver = new GameObserver() {
            @Override
            public void onMoveMade(MoveEvent e) {}
            
            @Override
            public void onGameOver(GameResult e) {}
            
            @Override
            public void onTournamentOver(tournament.TournamentResult e) {} 
        };

        // Test addObserver() and getObservers()
        game.addObserver(testObserver);
        assertEquals(1, game.getObservers().size());

        // Run the game loop Here
        GameResult result = game.play(p1, p2);

        // Check the game run successfully, Cooperate vs Defect should = 0 to 10 after 2 rounds)
        assertEquals(0, result.getTotalScoreP1());
        assertEquals(10, result.getTotalScoreP2());

        // Test removeObserver()
        game.removeObserver(testObserver);
        assertEquals(0, game.getObservers().size());
    }
}
