package src;

import java.util.List;
import java.util.Map;

public class MDPAction {
    public String actionName;
    public String currentState;
    public Map<String, List<PD>> sucessorAndPossibility;
    public String successorState;
    public double probabilityOfAction;
    public double discard;
    public double cost;

    public MDPAction() {
    }

    public MDPAction(String actionName, String currentState, String successorState, double probabilityOfAction,
            double discard) {
        this.actionName = actionName;
        this.currentState = currentState;
        this.successorState = successorState;
        this.probabilityOfAction = probabilityOfAction;
        this.discard = discard;
        this.cost = 0;
    }
}
