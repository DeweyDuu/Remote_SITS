package remote.server;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import game.IteratedPrisonersDilemma;
import game.PrisonerAction;
import tournament.RoundRobin;

@SpringBootApplication
public class ServerApp {

    public static void main(String[] args) {
        SpringApplication.run(ServerApp.class, args);
    }

    @Bean
    ApplicationRunner initTournaments(TournamentRegistry registry) {
        return args -> {
            NetworkedTournament ipd1 = new NetworkedTournament(
                "ipd1",
                "Iterated Prisoner's Dilemma",
                new RoundRobin(),
                new IteratedPrisonersDilemma(5),
                PrisonerAction::valueOf
            );
            registry.add(ipd1);
            System.out.println("Tournament 'ipd1' is open for registration.");
        };
    }
}