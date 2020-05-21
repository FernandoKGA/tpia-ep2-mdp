import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class MDP {
    static final String file_prefix = "navigation_";
    static final String file_format = ".net";
    static final String fixedGoalInitialState = "FixedGoalInitialState";
    static final String randomGoalInitialState = "RandomGoalInitialState";
    
    public static void main(String[] args) throws Exception {
        // -f -> fixed - navigation_number
        // -r -> random - navigation_number
        // navigation_number goes from 1 to 10
        
        FileReader file;
        switch(args[0]) {
            case "-f":
                file = getFileReader(args[1], fixedGoalInitialState);
                file.close();
                break;
            case "-r":
                file = getFileReader(args[1], randomGoalInitialState);
                file.close();
                break;
            default:
                throw new IllegalArgumentException("Parameter not recognized.");
        }
    }

    public static FileReader getFileReader(String fileNumber, String folder) throws FileNotFoundException{
        String absolutePath = new File("").getAbsolutePath();
        return new FileReader(absolutePath+"/../files/"+folder+"/"+file_prefix+fileNumber+file_format);
    }
}