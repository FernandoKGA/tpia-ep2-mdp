public class MDPState {
    private int x;
    private int y;
    public List<MDPAction> actions_south;
    public List<MDPAction> actions_north;
    public List<MDPAction> actions_west;
    public List<MDPAction> actions_east;

    public MDPState( int x, int y ) {
        this.x = x;
        this.y = y;
    }

}