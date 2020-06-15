package src;

import java.util.List;

import src.MDPAction;

public class MDPState {
    public int x;
    public int y;
    public List<MDPAction> actions;

    public MDPState( int x, int y ) {
        this.x = x;
        this.y = y;
    }

    public MDPState( int x, int y, List<MDPAction> actions ) {
        this.x = x;
        this.y = y;
        this.actions = actions;
    }
}