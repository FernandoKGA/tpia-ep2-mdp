package src;

import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import src.MDPAction;
import src.MDPState;

public class Problem {
    public MDPState[] states;
    public MDPState initialState;
    public MDPState goalState;
    public double epsilon = 0.1;

    public Problem() {
        
    }

    public static Problem createProblem( BufferedReader br ) throws IOException {
        Problem problem = new Problem();
        String[] statesStr = new String[1]; //just initialize
        Map<String, MDPState> states = new HashMap<>();
        Map<String, List<MDPAction>> problemActions = new HashMap<>();
        Map<String, Map<String, Double>> costs = new HashMap<>();

        long initTime = System.currentTimeMillis();

        while ( br.ready() ) {
            String line = br.readLine();
            
            switch( line ) {
                case "states":
                    line = br.readLine();
                    line = line.trim();
                    statesStr = line.split(", ");
                    
                    break;

                case "cost":
                    line = br.readLine();
                    line = line.trim();
                    while( !line.equals("endcost") ) {
                        String[] cost_line = line.split(" ");

                        String currentState = cost_line[0];
                        String actionName = cost_line[1];
                        double cost = Double.parseDouble(cost_line[2]);

                        if ( costs.containsKey(currentState) ) {
                            Map<String, Double> costs_aux = costs.get(currentState);
                            costs_aux.put(actionName, cost);
                            costs.replace(currentState, costs_aux);
                        }
                        else {
                            Map<String, Double> costs_aux = new HashMap<>();
                            costs_aux.put(actionName, cost);
                            costs.put(currentState, costs_aux);
                        }

                        line = br.readLine();
                        line = line.trim();
                    }
                    break;

                case "initialstate":
                    line = br.readLine();
                    line = line.trim();
                    while( !line.equals("endinitialstate") ) {
                        
                        problem.initialState = new MDPState(line);

                        line = br.readLine();
                    }
                    break;
                
                case "goalstate":
                    line = br.readLine();
                    line = line.trim();
                    while( !line.equals("endgoalstate") ) {

                        problem.goalState = new MDPState(line);

                        line = br.readLine();
                    }
                    break;

                default:
                    if ( !line.equals("") ){
                        if( line.contains("action") ) {
                            line = line.trim();
                            
                            String[] action = line.split(" ");
                            String actionName = action[1];

                            line = br.readLine();
                            while( !line.equals("endaction") ) {
                                line = line.trim();

                                action = line.split(" ");
                                MDPAction mdpAction = new MDPAction(actionName, action[0],
                                    action[1], Double.parseDouble(action[2]) , Double.parseDouble(action[3]) 
                                );

                                if ( problemActions.containsKey(mdpAction.currentState) ) {
                                    List<MDPAction> currentStateAction = problemActions.get(mdpAction.currentState);
                                    currentStateAction.add(mdpAction);
                                    problemActions.replace(mdpAction.currentState, currentStateAction);
                                }
                                else {
                                    List<MDPAction> mdpActions = new ArrayList<>();
                                    mdpActions.add(mdpAction);
                                    problemActions.put(mdpAction.currentState, mdpActions);
                                }
                                
                                line = br.readLine();
                            }
                        }
                    }
                    break;
            }
        }
        
        problem.states = new MDPState[statesStr.length];
        int cont_state = 0;

        // create MDP state with actions and its costs
        for ( String state : statesStr ) {

            List<MDPAction> actions = problemActions.get(state);
            Map<String,Double> actions_costs = costs.get(state);
            MDPState mdpState = new MDPState(state); 

            if ( actions_costs != null && actions != null ) {
                for ( MDPAction action : actions ) {
                    if ( actions_costs.containsKey(action.actionName) ) {
                        action.cost = actions_costs.get(action.actionName);
                    }
                }    

                problemActions.replace(state, actions);
            }
            else {
                // se um dos dois eh nulo
                if ( ( actions != null && actions_costs == null ) 
                        || ( ( actions == null && actions_costs != null ) ) ) {
                    // verifica se as acoes nao sao para o mesmo estado
                    for ( MDPAction action : actions ) {
                        if ( !action.currentState.equals(action.successorState) ) {
                            String message = "Invalid state '" + state + "' actions join, ";
                            if ( actions == null ) {
                                message += "doesn't has actions.";
                            }
                            if ( actions_costs == null ) {
                                message += "doesn't has costs for actions.";
                            }
                            
                            throw new NullPointerException(message);
                        }
                    }
                }
            }
            mdpState.actions = actions;
            problem.states[cont_state] = mdpState;
            cont_state ++;
        }

        long finishTime = System.currentTimeMillis();
        long diff = finishTime - initTime;
        System.out.println("Parsing time: " + diff + "ms");

        return problem;
    }
}