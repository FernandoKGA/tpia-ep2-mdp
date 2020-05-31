package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

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
        switch(args[0]) {
            case "-f":
                file = getFileReader(args[1], fixedGoalInitialState);
                br = new BufferedReader(file);
                createProblem(br);
                file.close();
                break;
            case "-r":
                file = getFileReader(args[1], randomGoalInitialState);
                br = new BufferedReader(file);
                createProblem(br);
                file.close();
                break;
            default:
                throw new IllegalArgumentException("Parameter not recognized.");
        }
    }

    public static FileReader getFileReader(String fileNumber, String folder) throws FileNotFoundException{
        String absolutePath = new File("").getAbsolutePath();
        return new FileReader(absolutePath+"/files/"+folder+"/"+file_prefix+fileNumber+file_format);
    }

    public static void createProblem( BufferedReader br ) throws java.io.IOException {
        Problem problem = new Problem();

        while ( br.ready() ) {
            String line = br.readLine();
            
            switch( line ) {
                case "states":
                    line = br.readLine();
                    line = line.trim();
                    problem.states = line.split(", ");
                    for (String state : problem.states) {
                        System.out.println(state);
                    }
                    break;

                case "cost":
                    line = br.readLine();
                    while( !line.equals("endcost") ) {
                        line = br.readLine();
                    }
                    break;

                case "initialstate":
                    line = br.readLine();
                    while( !line.equals("endinitialstate") ) {
                        line = br.readLine();
                    }
                    break;
                
                case "goalstate":
                    line = br.readLine();
                    while( !line.equals("endgoalstate") ) {
                        line = br.readLine();
                    }
                    break;

                default:
                    if ( !line.equals("") ){
                        if( line.contains("action") ) {
                            while( !line.equals("endaction") ) {
                                line = br.readLine();
                            }
                        }
                    }
                    break;
            }
        }
    }
}