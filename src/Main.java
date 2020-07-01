package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

import src.MDPAction;
import src.MDPState;
import src.PD;
import src.Problem;
import src.ValueFunction;

public class Main {
    static final String file_prefix = "navigation_";
    static final String file_format = ".net";
    static final String fixedGoalInitialState = "FixedGoalInitialState";
    static final String randomGoalInitialState = "RandomGoalInitialState";
    static final String example = "RunningExample";
    
    // Program must be run from top level folder (tpia-ep2-mdp) with command: java src/Main [-f | -r] [-iv | -ip] [1-10] [-p]
    public static void main( String[] args ) throws Exception, java.io.IOException {
        /**
         * args 0 -> type of file to use
         * args 1 -> type of algorithm
         * args 2 -> if is not example, the navigation_number
         * args 3 -> print grid
         * 
         * Types of files
         * -f -> FixedGoalInitialState
         * -r -> RandomGoalInitialState
         * -ex -> RunningExample
         * 
         * Algorithms
         * -iv -> Value Iteration
         * -ip -> Policy Iteration
         * 
         * Example
         * java src/Main -ex -iv
         * java src/Main -f -ip 1
         * java src/Main -f -ip 1 -p
         */

        FileReader file;
        BufferedReader br;
        Problem problem;
        String jsonString = "";

        String mode = args[0].trim();
        switch( mode ) {
            case "-f":
                file = getFileReader(args[2], fixedGoalInitialState);
                br = new BufferedReader(file);
                jsonString = new BufferedReader(
                    new FileReader(
                        createFilePath(
                            args[2], 
                            "PoliticasFixedRandom/" + fixedGoalInitialState, file_format + "_politicas.json"
                        )
                    )
                ).readLine();
                problem = Problem.createProblem(br);
                file.close();
                break;
            case "-r":
                file = getFileReader(args[2], randomGoalInitialState);
                br = new BufferedReader(file);
                jsonString = new BufferedReader(
                    new FileReader(
                        createFilePath(
                            args[2], 
                            "PoliticasFixedRandom/" + randomGoalInitialState, file_format + "_politicas.json"
                        )
                    )
                ).readLine();
                problem = Problem.createProblem(br);
                file.close();
                break;
            case "-ex":
                file = getFileReaderForExample(example);
                br = new BufferedReader(file);
                problem = Problem.createProblem(br);
                file.close();
                break;
            default:
                throw new IllegalArgumentException("Parameter " + "'" + mode + "'" + " not recognized.");
        }

        //executa algoritmos
        String alg = args[1].trim();
        switch( alg ) {
            case "-iv":
                //Iteração de valor
                valueIteration(problem);
                break;
            case "-ip":
                //Iteração de política    
                policyIteration(problem, jsonString);
                break;
            default:
                throw new IllegalArgumentException("Parameter " + "'" + alg + "'" + " not recognized. Choose between '-iv' or '-ip'.");
        }

        if ( args.length == 4 ) {
            if ( args[3].equals("-p") ) {
                //printa grid
                printGrid(problem);
            }
            else {
                throw new IllegalArgumentException("Parameter " + "'" + args[3] + "'" + " not recognized.");
            }
        }                
        else {
            if ( mode.equals("-ex") ) {
                if ( args.length > 2 ) {
                    if ( args[2].equals("-p") ) {
                        //printa grid
                        printGrid(problem);
                    }
                    else {
                        throw new IllegalArgumentException("Parameter " + "'" + args[2] + "'" + " not recognized.");
                    }   
                }
            }
        }
    }

    public static String getAbsolutePath() {
        return new File("").getAbsolutePath();
    }

    public static String createFilePath( String fileNumber, String folder, String file_format ) {
        return getAbsolutePath() + "/files/" + folder + "/" + file_prefix + fileNumber + file_format;
    }

    public static FileReader getFileReader ( String fileNumber, String folder ) throws FileNotFoundException {
        String absolutePath = new File("").getAbsolutePath();
        return new FileReader(absolutePath+"/files/"+folder+"/"+file_prefix+fileNumber+file_format);
    }

    public static FileReader getFileReaderForExample ( String folder ) throws FileNotFoundException {
        String absolutePath = new File("").getAbsolutePath();
        return new FileReader(absolutePath+"/files/"+folder+"/example"+file_format);
    }

    public static double computeResidual(double newValueFunction, double oldValueFunction) {
        return Math.abs(newValueFunction - oldValueFunction);
    }

    public static Map.Entry<Double, MDPAction> computeValueFunctionWithBellmanBackup( MDPState state, int iteration ) {
        
        double minimal_value = Double.MAX_VALUE;
        MDPAction argmin = null;

        for ( MDPAction action : state.actions ) {
            
            double sum = 0;

            if ( action.sucessorAndPossibility.size() == 1 ) {
                Map.Entry<MDPState, PD> sucessorAndPossibility = action.sucessorAndPossibility.entrySet().iterator().next();
                MDPState sucessor = sucessorAndPossibility.getKey();
                if ( state.x == sucessor.x && state.y == sucessor.y ) continue;
                else {
                    sum += (action.cost + sucessor.valuesFunctions.get(iteration-1));
                    // ALERTA PARA: ele realmente pega o valor da iteracao atual ou pega de outra iteracao?
                }
            }
            else {
                sum += action.cost;
                for (Map.Entry<MDPState, PD> pair : action.sucessorAndPossibility.entrySet()) {
                    MDPState sucessor = pair.getKey();
                    PD possibility = pair.getValue();
                    sum += (possibility.probabilityOfAction * sucessor.valuesFunctions.get(iteration-1));
                }
            }

            if (minimal_value > sum) {
                minimal_value = sum;
                argmin = action;
            }
        }

        SimpleEntry<Double, MDPAction> result = new SimpleEntry<Double,MDPAction>(minimal_value, argmin);
        return result;
    }

    public static void valueIteration( Problem problem ) {
        long initTime = System.currentTimeMillis();

        //initialize V0
        for (MDPState state : problem.states) {
            state.valuesFunctions.add(0.0);
        }
        int iterations = 0;
        double minResidual = Double.MAX_VALUE;
        
        do {
            iterations++;
            double localResidual = 0;

            for (MDPState state : problem.states) {
                if (!state.equals(problem.goalState)) {
                    Map.Entry<Double, MDPAction> pair = computeValueFunctionWithBellmanBackup(state, iterations);
                    double newValueFunction = pair.getKey();
                    MDPAction argmin = pair.getValue();
                    
                    localResidual = Math.max(
                        localResidual,
                        computeResidual(newValueFunction,state.valuesFunctions.get(state.valuesFunctions.size()-1))
                    );

                    state.valuesFunctions.add(newValueFunction);
                    state.bestAction = argmin;
                }
                else {
                    state.valuesFunctions.add(0.0);
                }
            }

            minResidual = Math.min(minResidual, localResidual);
        } while ( minResidual > problem.epsilon);

        long finishTime = System.currentTimeMillis();
        long diff = finishTime - initTime;
        System.out.println("Value Iteration Time: " + diff + "ms");
        System.out.println("Iterations: " + iterations);
    }

    public static void evaluatePolicy( Problem problem ) {
        int iterations = 0;
        double maxResidual = 0;
        
        // lista de valores local dos estados por iteracao (nao a que esta no problema)
        Map<MDPState, ValueFunction> localValuesFunction = new HashMap<>();

        for ( MDPState state : problem.states ) {
            ValueFunction valueFunction = new ValueFunction();
            valueFunction.oldValue = state.valuesFunctions.get(state.valuesFunctions.size() - 1);
            localValuesFunction.put(state, valueFunction);
        }

        //double minResidual = Double.MAX_VALUE;
        
        do {       
            iterations++;
            maxResidual = 0;

            for ( MDPState state : problem.states ) {
                if (!state.equals(problem.goalState)) {
                    MDPAction bestAction = state.bestAction;
                    
                    double v = bestAction.cost;
                    for ( Map.Entry<MDPState, PD> pair : bestAction.sucessorAndPossibility.entrySet() ) {
                        MDPState sucessorState = pair.getKey();
                        double probability = pair.getValue().probabilityOfAction;

                        v += (localValuesFunction.get(sucessorState).oldValue * probability);
                    }

                    // state.printStateCoords();
                    // System.out.println(v);

                    maxResidual = Math.max(
                        maxResidual, 
                        computeResidual(
                            localValuesFunction.get(state).oldValue,
                            v
                        )
                    );
                    
                    localValuesFunction.get(state).newValue = v;
                }
                else {
                    localValuesFunction.get(state).newValue = 0.0;
                }
            }

            for ( MDPState state : problem.states ) {
                ValueFunction aux = localValuesFunction.get(state);
                aux.oldValue = aux.newValue;
            }

            //minResidual = Math.min(minResidual, maxResidual);

            //System.out.println("itr: " + iterations + " res: " + maxResidual);
        } while ( maxResidual > problem.epsilon);

        // atualiza os valuefunctions dos states com o valor do ultimo localvaluesfunction
        for ( MDPState state : problem.states ) {
            ValueFunction aux = localValuesFunction.get(state);
            state.valuesFunctions.add(aux.newValue);
        }
    } 

    public static void policyIteration( Problem problem, String jsonString ) {
        long initTime = System.currentTimeMillis();
        
        //preprocess json
        // remove brackets
        jsonString = jsonString.substring(1);
        jsonString = jsonString.substring(0, jsonString.length()-1);
        
        Map<String, String> stateAndAction = new HashMap<>();
        String[] keyValues = jsonString.split(",");
        for ( String keyValue : keyValues ) {
            keyValue = keyValue.trim();
            String[] auxValues = keyValue.split("\": \"");
            String state = auxValues[0].substring(1);
            String action = auxValues[1].substring(0,auxValues[1].length()-1);
            stateAndAction.put(state, "move-" + action);
        }

        //assign an arbitrary assignment of pi0 to each state
        for ( MDPState state : problem.states ) {
            // se estado goal, nao atribui acao para ele
            if ( state.x == problem.goalState.x && state.y == problem.goalState.y ) continue;

            String actionName = stateAndAction.get(state.toRobotAtString());
            for ( MDPAction action : state.actions ) {
                if ( action.actionName.equals(actionName) ) {
                    state.bestAction = action;
                    break;
                }
            }
        }
        
        for ( MDPState state : problem.states ) {
            state.valuesFunctions.add(0.0);
        }
        
        boolean hasChanged = true;
        int iterations = 0;

        do {
            hasChanged = false;
            iterations++;
            //System.out.println(iterations);

            evaluatePolicy(problem);

            // melhora a politica
            for ( MDPState state : problem.states ) {
                //state.printStateCoords();
                if ( state.x == problem.goalState.x && state.y == problem.goalState.y ) continue;
                Map.Entry<Double, MDPAction> result = computeValueFunctionWithBellmanBackup(state, iterations+1);
                
                //System.out.println("V: " + result.getKey() + " " + result.getValue().actionName + " cost: " + result.getValue().cost);
                if ( !state.bestAction.actionName.equals(result.getValue().actionName)) {
                    hasChanged = true;
                    state.bestAction = result.getValue();
                }
            }

        } while ( hasChanged );

        long finishTime = System.currentTimeMillis();
        long diff = finishTime - initTime;
        System.out.println("Policy Iteration Time: " + diff + "ms");
        System.out.println("Iterations: " + iterations);
    }

    public static void printGrid( Problem problem ) throws UnsupportedEncodingException{
        int maximum_x = 0;
        int maximum_y = 0;
        
        for ( MDPState state : problem.states ) {
            if ( maximum_x < state.x ) maximum_x = state.x;
            if ( maximum_y < state.y ) maximum_y = state.y;
        }

        // builds grid
        MDPState[][] grid = new MDPState[maximum_x+1][maximum_y+1];
        for ( MDPState state : problem.states ) {
            grid[state.x][state.y] = state;
        }

        //https://rosettacode.org/wiki/Terminal_control/Display_an_extended_character
        PrintStream writer = new PrintStream(System.out, true, "UTF-8");
        for ( int j = grid[0].length-1; j >= 1; j-- ) {
            for ( int i = 1; i < grid.length; i++ ) {
                MDPState state = grid[i][j];
                if ( state != null ) {
                    if ( !state.equals(problem.goalState) ) {
                        //https://unicode-table.com/pt/sets/arrow-symbols/
                        switch (state.bestAction.actionName) {
                            case "move-east":
                                writer.print(" → "); //u+2192
                                break;
                            case "move-north":
                                writer.print(" ↑ "); //u+2191
                                break;
                            case "move-west":
                                writer.print(" ← "); //u+2190
                                break;
                            case "move-south":
                                writer.print(" ↓ "); //u+2193
                                break;
                        }
                    }
                    else {
                        writer.print(" G ");
                    }
                }
                else {
                    writer.print("   ");
                }
            }
            writer.print("\n");
        }
    }
} 