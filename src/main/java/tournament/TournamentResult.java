package tournament;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class TournamentResult {
    private Map<String, Integer> scores; 

    public TournamentResult(Map<String, Integer> scores) {
        this.scores = scores;
    }

    public List<String> getRankings() { 
        List<String> rankings = new ArrayList<>(scores.keySet());
        // Sort from highest score to lowest
        rankings.sort((a, b) -> scores.get(b).compareTo(scores.get(a))); 
        return rankings;
    }

    public Map<String, Integer> getScores() {
        return scores;
    }

    public int getScore(String name) {
        return scores.getOrDefault(name, 0);
    }
}