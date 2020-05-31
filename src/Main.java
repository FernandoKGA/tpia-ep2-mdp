package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

import src.MDPAction;

public class Main {
    static final String file_prefix = "navigation_";
    static final String file_format = ".net";
    static final String fixedGoalInitialState = "FixedGoalInitialState";
    static final String randomGoalInitialState = "RandomGoalInitialState";
    
    // Program must be run from top level folder (tpia-ep2-mdp) with command: java src/Main.java [-f | -r] [1-10]
    public static void main(String[] args) throws Exception, java.io.IOException {
        /**
         * -f navigation_number
         * -r navigation_number
         * 
         * -f -> fixed
         * -r -> random
         * navigation_number goes from 1 to 10
         */

        FileReader file;
        BufferedReader br;
        Problem problem;
        switch(args[0]) {
            case "-f":
                file = getFileReader(args[1], fixedGoalInitialState);
                br = new BufferedReader(file);
                problem = createProblem(br);
                file.close();
                break;
            case "-r":
                file = getFileReader(args[1], randomGoalInitialState);
                br = new BufferedReader(file);
                problem = createProblem(br);
                file.close();
                break;
            default:
                throw new IllegalArgumentException("Parameter not recognized.");
        }

        //executa algoritmos
    }

    public static FileReader getFileReader(String fileNumber, String folder) throws FileNotFoundException{
        String absolutePath = new File("").getAbsolutePath();
        return new FileReader(absolutePath+"/files/"+folder+"/"+file_prefix+fileNumber+file_format);
    }

    public static Problem createProblem( BufferedReader br ) throws java.io.IOException {
        Problem problem = new Problem();
        problem.actions = new HashMap<>();
        problem.costs = new HashMap<>();

        // public String[] states;
        // public Map<String, List<MDPAction>> actions;
        // public Map<String, List<SimpleEntry<String, Double>>> costs;
        // public String initialState;
        // public String goalState;

        while ( br.ready() ) {
            String line = br.readLine();
            
            switch( line ) {
                case "states":
                    line = br.readLine();
                    line = line.trim();
                    problem.states = line.split(", ");
                    break;

                case "cost":
                    line = br.readLine();
                    line = line.trim();
                    while( !line.equals("endcost") ) {
                        String[] cost_line = line.split(" ");

                        String currentState = cost_line[0];
                        String actionName = cost_line[1];
                        double cost = Double.parseDouble(cost_line[2]);

                        if ( problem.costs.containsKey(currentState) ) {
                            List<SimpleEntry<String, Double>> costs = problem.costs.get(currentState);
                            costs.add(new SimpleEntry<>(actionName, cost));
                            problem.costs.replace(currentState, costs);
                        }
                        else {
                            List<SimpleEntry<String, Double>> costs = new ArrayList<>();
                            costs.add(new SimpleEntry<>(actionName, cost));
                            problem.costs.put(currentState, costs);
                        }

                        line = br.readLine();
                    }
                    break;

                case "initialstate":
                    line = br.readLine();
                    line = line.trim();
                    while( !line.equals("endinitialstate") ) {

                        problem.initialState = line;

                        line = br.readLine();
                    }
                    break;
                
                case "goalstate":
                    line = br.readLine();
                    line = line.trim();
                    while( !line.equals("endgoalstate") ) {

                        problem.goalState = line;

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

                                if ( problem.actions.containsKey(mdpAction.currentState) ) {
                                    List<MDPAction> currentStateAction = problem.actions.get(mdpAction.currentState);
                                    currentStateAction.add(mdpAction);
                                    problem.actions.replace(mdpAction.currentState, currentStateAction);
                                }
                                else {
                                    List<MDPAction> mdpActions = new ArrayList<>();
                                    mdpActions.add(mdpAction);
                                    problem.actions.put(mdpAction.currentState, mdpActions);
                                }
                                
                                line = br.readLine();
                            }
                        }
                    }
                    break;
            }
        }
        
        //System.out.println(problem.states.length);
        // System.out.println(problem.actions.size());

        // for (List<MDPAction> list : problem.actions.values()) {
        //     for (MDPAction mdpAction : list) {
        //         System.out.println(mdpAction.actionName + " " + mdpAction.currentState + 
        //         " " + mdpAction.successorState + " " + mdpAction.probabilityOfAction + " "
        //         + mdpAction.discard);
        //     }
        // }

        //System.out.println(problem.costs.size());

        // for ( List<SimpleEntry<String, Double>> costs : problem.costs.values() ) {
        //     for ( SimpleEntry<String, Double> pair : costs) {
        //         System.out.println(pair.getKey() + " " + pair.getValue());
        //     }
        // }

        // System.out.println(problem.goalState);
        // System.out.println(problem.initialState);
    }
}