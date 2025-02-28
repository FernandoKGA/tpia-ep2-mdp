package src;

import java.util.ArrayList;
import java.util.List;

import src.MDPAction;

public class MDPState {
    public int x;
    public int y;
    public List<MDPAction> actions = new ArrayList<>();
    public List<Double> valuesFunctions = new ArrayList<>();
    public MDPAction bestAction;

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
        String[] state_strings = state.split("-");
        String[] coords = state_strings[2].split("y");
        coords[0] = coords[0].replace("x", "");
        return coords;
    }

    public void printStateCoords() {
        System.out.println("State: x" + x + "y" + y);
    }

    public String toRobotAtString() {
        return "robot-at-x" + x + "y" + y;
    }
}