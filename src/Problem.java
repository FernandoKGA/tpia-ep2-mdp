package src;

import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;

import src.MDPAction;

public class Problem {
    public String[] states;
    public Map<String, List<MDPAction>> actions;
    public Map<String, List<SimpleEntry<String, Double>>> costs;

    public Problem() {
        
    }
}