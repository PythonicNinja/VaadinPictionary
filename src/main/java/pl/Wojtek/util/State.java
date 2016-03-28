package pl.Wojtek.util;

/**
 *
 */
public class State {
    private String name;

    public State(String s) {
        this.name = s;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "State(" + this.getName() + ")";
    }
}