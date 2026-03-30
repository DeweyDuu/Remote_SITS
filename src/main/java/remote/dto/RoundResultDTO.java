package remote.dto;

public class RoundResultDTO {
    public String actionP1;
    public String actionP2;
    public int scoreP1;
    public int scoreP2;

    public RoundResultDTO() {}

    public RoundResultDTO(String actionP1, String actionP2, int scoreP1, int scoreP2) {
        this.actionP1 = actionP1;
        this.actionP2 = actionP2;
        this.scoreP1 = scoreP1;
        this.scoreP2 = scoreP2;
    }
}