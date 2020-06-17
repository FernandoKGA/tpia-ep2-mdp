package src;

import java.util.ArrayList;
import java.util.List;

import src.MDPAction;

public class MDPState {
    public int x;
    public int y;
    public List<MDPAction> actions;
    public List<Double> valuesFunctions = new ArrayList<>();
    // mudar tipo para guardar acao tambem

    public MDPState( String state ) {
        String[] coords = parseState(state);
        this.x = Integer.parseInt(coords[0]);
        this.y = Integer.parseInt(coords[1]);
    }

    public MDPState( int x, int y ) {
        this.x = x;
        this.y = y;
    }

    public MDPState( int x, int y, List<MDPAction> actions ) {
        this.x = x;
        this.y = y;
        this.actions = actions;
    }

    public static String[] parseState( String state ) {
        //state = robot-at-x18y20
        String[] state_strings = state.split("-");
        //state_strings = robot at x18y20
        String[] coords = state_strings[2].split("y");
        //coords = x18 20
        coords[0] = coords[0].replace("x", "");
        //coords = 18 20
        return coords;
    }
}