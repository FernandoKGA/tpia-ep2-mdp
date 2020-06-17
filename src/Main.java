package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

import src.MDPAction;
import src.MDPState;

public class Main {
    static final String file_prefix = "navigation_";
    static final String file_format = ".net";
    static final String fixedGoalInitialState = "FixedGoalInitialState";
    static final String randomGoalInitialState = "RandomGoalInitialState";
    
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
        switch(args[0]) {
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
            default:
                throw new IllegalArgumentException("Parameter not recognized.");
        }

        //executa algoritmos
        //Iteração de valor
        IterationValue(problem);
        //Iteração de política
    }

    public static FileReader getFileReader (String fileNumber, String folder ) throws FileNotFoundException{
        String absolutePath = new File("").getAbsolutePath();
        return new FileReader(absolutePath+"/files/"+folder+"/"+file_prefix+fileNumber+file_format);
    }

    public static double computeResidual(double newValueFunction, double oldValueFunction) {
        return Math.abs(newValueFunction - oldValueFunction);
    }

    public static double computeValueFunctionWithBellmanBackup( MDPState state ) {
        double sum = 0;
        //state.sucessorAndPossibility.
        for ( MDPAction action : state.actions ) {
            if ( action.currentState == action.successorState && action.probabilityOfAction == 1) continue;
            else {

            }
        }
        return sum;
    }

    public static void IterationValue( Problem problem ) {
        long initTime = System.currentTimeMillis();

        //initialize V0
        for (MDPState state : problem.states) {
            state.valuesFunctions.add(0.0);
        }
        int iterations = 0;
        double maxResidual = 0;
        
        while ( maxResidual < problem.epsilon ) {
            iterations++;
            maxResidual = 0;
            for (MDPState state : problem.states) {
                double newValueFunction = computeValueFunctionWithBellmanBackup(state);
                maxResidual = Math.max(
                    maxResidual,
                    computeResidual(newValueFunction,state.valuesFunctions.get(state.valuesFunctions.size()-1))
                );
                state.valuesFunctions.add(newValueFunction);
            }
        }

        long finishTime = System.currentTimeMillis();
        long diff = finishTime - initTime;
        System.out.println("Iteration Value Time: " + diff + "ms");

        //retorna política ?
    }

    public static void IterationPolicy( Problem problem ) {
        long initTime = System.currentTimeMillis();
        


        long finishTime = System.currentTimeMillis();
        long diff = finishTime - initTime;
        System.out.println("Iteration Policy Time: " + diff + "ms");
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