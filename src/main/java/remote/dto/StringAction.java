package remote.dto;

import game.Action;

public class StringAction implements Action {
    private String label;
    
    public StringAction() {
    }

    public StringAction(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }
}