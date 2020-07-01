package src;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import src.MDPState;

public class MDPAction {
    public String actionName;
    public Map<MDPState, PD> sucessorAndProbability = new HashMap<>();
    public double cost;

    public MDPAction() {
    }

    public MDPAction(String actionName) {
        this.actionName = actionName;
        this.cost = 0;
    }
}
