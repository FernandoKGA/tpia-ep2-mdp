package src;

import java.util.List;

public class MDPState {
    private int x;
    private int y;
    public List<String> actions;

    public MDPState( int x, int y ) {
        this.x = x;
        this.y = y;
    }

}