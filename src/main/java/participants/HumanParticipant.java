package participants;
import game.Action;
import game.GameHistory;
import remote.dto.StringAction;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!test")
public class HumanParticipant implements Participant {

    private String name;
    private Scanner scanner;

    public HumanParticipant(@Value("${participant.name:HumanPlayer}") String name) {
        this.name = name;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Action chooseAction(GameHistory history) {
        if (history.getRounds().isEmpty()) {
            System.out.println("No previous interactions. This is Round 1!");
        } else {
            System.out.println("Previous Interactions:");
            int roundNum = 1;
            for (var round : history.getRounds()) {
                System.out.println("  Round " + roundNum + ": " + 
                                   round.getActionP1().getLabel() + " vs " + 
                                   round.getActionP2().getLabel());
                roundNum++;
            }
        }
        System.out.print("choose your action: ");
        return new StringAction(scanner.nextLine());
    }

    @Override
    public void reset() {
    }
}