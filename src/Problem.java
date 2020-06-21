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
import src.PD;

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

        long initTime = System.currentTimeMillis();

        while ( br.ready() ) {
            String line = br.readLine();
            
            switch( line ) {
                case "states":
                    line = br.readLine();
                    line = line.trim();
                    statesStr = line.split(", ");
                    
                    for ( String state : statesStr ) {
                        states.put(state, new MDPState(state));
                    }

                    break;

                case "cost":
                    line = br.readLine();
                    line = line.trim();
                    while( !line.equals("endcost") ) {
                        String[] costLine = line.split(" ");

                        String currentState = costLine[0];
                        String actionName = costLine[1];
                        double cost = Double.parseDouble(costLine[2]);

                        MDPState state = states.get(currentState);
                        for ( MDPAction action : state.actions ) {
                            if ( action.actionName.equals(actionName) ) {
                                action.cost = cost;
                                break;
                            }
                        }

                        line = br.readLine();
                        line = line.trim();
                    }
                    break;

                case "initialstate":
                    line = br.readLine();
                    line = line.trim();
                    while( !line.equals("endinitialstate") ) {
                        
                        problem.initialState = states.get(line);

                        line = br.readLine();
                    }
                    break;
                
                case "goalstate":
                    line = br.readLine();
                    line = line.trim();
                    while( !line.equals("endgoalstate") ) {

                        problem.goalState = states.get(line);

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

                                String currentStateStr = action[0]; //usar para pesquisar no mapa
                                MDPState currentState = states.get(currentStateStr);

                                String sucessorStateStr = action[1]; //pegar o estado sucessor no mapa
                                MDPState sucessorState = states.get(sucessorStateStr);

                                double probability = Double.parseDouble(action[2]);
                                double discard = Double.parseDouble(action[3]);
                                PD pd = new PD(probability, discard);

                                boolean hasAction = false;
                                for ( MDPAction mdpAction : currentState.actions ) {
                                    if ( mdpAction.actionName.equals(actionName) ) {
                                        mdpAction.sucessorAndPossibility.put(sucessorState, pd);
                                        hasAction = true;
                                        break;
                                    }
                                }

                                if ( !hasAction ) {
                                    MDPAction mdpAction = new MDPAction(actionName);
                                    mdpAction.sucessorAndPossibility.put(sucessorState, pd);
                                    currentState.actions.add(mdpAction);
                                }

                                line = br.readLine();
                            }
                        }
                    }
                    break;
            }
        }
        
        problem.states = states.values().toArray(new MDPState[states.size()]);
        // for ( MDPState state : problem.states ) {
            
        //         System.out.println(
        //             "x: " + state.x + " y: " + state.y
        //         );

        //         for ( MDPAction action : state.actions ) {
        //             System.out.println(
        //                 "action: " + action.actionName + " cost: " + action.cost
        //             );

        //             for ( Map.Entry<MDPState, PD> pair : action.sucessorAndPossibility.entrySet() ) {
        //                 MDPState auxState = pair.getKey();
        //                 PD pd = pair.getValue();
        //                 System.out.println(
        //                     "suc: " + "x" + auxState.x + "y" + auxState.y + " prob: " + pd.probabilityOfAction
        //                 );
        //             }
        //         }
            
        // }

        long finishTime = System.currentTimeMillis();
        long diff = finishTime - initTime;
        System.out.println("Parsing time: " + diff + "ms");

        return problem;
    }
}