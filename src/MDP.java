import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class MDP {
    static final String file_prefix = "navigation_";
    static final String file_format = ".net";
    static final String fixedGoalInitialState = "FixedGoalInitialState";
    static final String randomGoalInitialState = "RandomGoalInitialState";
    
    // Program must be run from top level folder (tpia-ep2-mdp) with command: java src/MDP.java
    public static void main(String[] args) throws Exception {
        /**
         * -f navigation_number
         * -r navigation_number
         * 
         * -f -> fixed
         * -r -> random
         * navigation_number goes from 1 to 10
         */

        FileReader file;
        switch(args[0]) {
            case "-f":
                file = getFileReader(args[1], fixedGoalInitialState);
                BufferedReader br = new BufferedReader(file);
                while (br.ready()) {
                    System.out.println(br.readLine());
                }
                file.close();
                break;
            case "-r":
                file = getFileReader(args[1], randomGoalInitialState);
                BufferedReader br = new BufferedReader(file);
                while (br.ready()) {
                    System.out.println(br.readLine());
                }
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
}