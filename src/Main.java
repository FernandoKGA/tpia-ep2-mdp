package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
import src.DD;

public class Main {
    static final String file_prefix = "navigation_";
    static final String file_format = ".net";
    static final String fixedGoalInitialState = "FixedGoalInitialState";
    static final String randomGoalInitialState = "RandomGoalInitialState";
    static final String example = "RunningExample";
    
    // Program must be run from top level folder (tpia-ep2-mdp) with command: java src/Main.java [-f | -r] [1-10]
    public static void main( String[] args ) throws Exception, java.io.IOException {
        /**
         * -f navigation_number
         * -r navigation_number
         * 
         * -f -> FixedGoalInitialState
         * -r -> RandomGoalInitialState
         * navigation_number goes from 1 to 10
         */

        FileReader file;
        BufferedReader br;
        Problem problem;

        String mode = args[0].trim();
        switch(mode) {
            case "-f":
                file = getFileReader(args[1], fixedGoalInitialState);
                br = new BufferedReader(file);
                problem = Problem.createProblem(br);
                file.close();
                break;
            case "-r":
                file = getFileReader(args[1], randomGoalInitialState);
                br = new BufferedReader(file);
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

        //Iteração de valor
        //IterationValue(problem);

        //Iteração de política
        IterationPolicy(problem);
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

    public static void IterationValue( Problem problem ) {
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
        System.out.println("Iteration Value Time: " + diff + "ms");
        System.out.println("Iterations: " + iterations);
    }

    public static void evaluatePolicy( Problem problem ) {
        int iterations = 0;
        double maxResidual = 0;
        
        // lista de valores local dos estados por iteracao (nao a que esta no problema)
        Map<MDPState, DD> localValuesFunction = new HashMap<>();

        for ( MDPState state : problem.states ) {
            DD dd = new DD();
            dd.firstDouble = state.valuesFunctions.get(state.valuesFunctions.size() - 1);
            localValuesFunction.put(state, dd);
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

                        v += (localValuesFunction.get(sucessorState).firstDouble * probability);
                    }

                    // state.printStateCoords();
                    // System.out.println(v);

                    maxResidual = Math.max(
                        maxResidual, 
                        computeResidual(
                            localValuesFunction.get(state).firstDouble,
                            v
                        )
                    );
                    
                    localValuesFunction.get(state).secondDouble = v;
                }
                else {
                    localValuesFunction.get(state).secondDouble = 0.0;
                }
            }

            for ( MDPState state : problem.states ) {
                DD aux = localValuesFunction.get(state);
                aux.firstDouble = aux.secondDouble;
            }

            //minResidual = Math.min(minResidual, maxResidual);

            System.out.println("itr: " + iterations + " res: " + maxResidual);
        } while ( maxResidual > problem.epsilon);

        // atualiza os valuefunctions dos states com o valor do ultimo localvaluesfunction
        for ( MDPState state : problem.states ) {
            DD aux = localValuesFunction.get(state);
            state.valuesFunctions.add(aux.secondDouble);
        }
    } 

    public static void IterationPolicy( Problem problem ) {
        long initTime = System.currentTimeMillis();
        
        //assign an arbitrary assignment of pi0 to each state
        for ( MDPState state : problem.states ) {
            // se estado goal, nao atribui acao para ele
            if ( state.x == problem.goalState.x && state.y == problem.goalState.y ) continue;

            for ( MDPAction action : state.actions ) {
                
                // se acao so tem 1 sucessor
                if ( action.sucessorAndPossibility.size() == 1 ) {
                    Map.Entry<MDPState, PD> sucessorAndPossibility = action.sucessorAndPossibility.entrySet().iterator().next();
                    MDPState sucessor = sucessorAndPossibility.getKey();

                    // e o sucessor eh o proprio estado, vai pra proxima acao
                    if ( state.x == sucessor.x && state.y == sucessor.y ) continue;
                    else {
                        state.bestAction = action;
                        break;
                    }
                }
                else {
                    state.bestAction = action;
                    break;
                }
            }
            // state.printStateCoords();
            // System.out.println(state.bestAction.actionName + " " + state.bestAction.cost);
        }

        for ( MDPState state : problem.states ) {
            state.valuesFunctions.add(0.0);    
            // if ( state.x == problem.goalState.x && state.y == problem.goalState.y ) {
                
            //     continue;
            // }
            // state.valuesFunctions.add(state.bestAction.cost);
        }
        
        boolean hasChanged = true;
        int iterations = 0;

        do {
            hasChanged = false;
            iterations++;
            System.out.println(iterations);

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
        System.out.println("Iteration Policy Time: " + diff + "ms");
        System.out.println("Iterations: " + iterations);
    }

    public static MDPState[][] createGrid( Problem problem ) {
        // int maximum_x = 0;
        // int maximum_y = 0;
        
        // for ( String state : problem.states ) {
            
        //     if ( maximum_x < x ) maximum_x = x;
        //     if ( maximum_y < y ) maximum_y = y;
        // }

         // builds grid
        // MDPState[][] grid = new MDPState[maximum_x+1][maximum_y+1];
        //     grid[x][y] = new MDPState(x, y, problem.actions.get(state));
        // }

        // for ( int i = 0; i < grid.length; i++ ) {
        //     for ( int j = 0; j < grid[0].length; j++ ) {
        //         if ( grid[i][j] != null) {
        //             System.out.println(i + " " + j);
        //         }
        //     }
        // }
        return null;
    }
}