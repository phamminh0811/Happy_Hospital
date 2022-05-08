package classes.states_of_auto_avg;

import classes.states_of_auto_avg.HybridState;


public class IdleState extends HybridState {
    private int _start ;
    private boolean _calculated;

    public IdleState(int start){
        super();
        this._start = start;
        this._calculated = false;
    }

    // public move()
}
