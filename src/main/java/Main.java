import game.Game;
import game.IteratedPrisonersDilemma;
import observer.MoveLogger;
import observer.ScoreLogger;
import participants.AlwaysCooperate;
import participants.AlwaysDefect;
import participants.Participant;
import participants.TitForTat;
import tournament.RoundRobin;
import tournament.TournamentFormat;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- STARTING THE SIMULATION ---");

        // 1. Setup the Game 
        Game game = new IteratedPrisonersDilemma(200); 

        // 2. Setup the Tournament Format
        TournamentFormat tournament = new RoundRobin();

      
        String currentDirectory = System.getProperty("user.dir");
        System.out.println("ATTENTION: Look for your text files inside this folder:");
        System.out.println(currentDirectory);
        System.out.println("---------------------------------------------------");

        // 3. Attach Loggers
        game.addObserver(new MoveLogger("moves.txt"));
        game.addObserver(new ScoreLogger("scores.txt"));

        // 4. Register Bots
        List<Participant> bots = new ArrayList<>();
        bots.add(new TitForTat());
        bots.add(new AlwaysDefect());
        bots.add(new AlwaysCooperate());

        // 5. Run the Tournament
        System.out.println("Running a Round Robin tournament with " + bots.size() + " bots...");
        tournament.run(bots, game);
        System.out.println("--- TOURNAMENT COMPLETE ---");
    }
}