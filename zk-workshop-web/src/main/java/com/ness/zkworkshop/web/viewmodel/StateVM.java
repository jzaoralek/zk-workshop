package com.ness.zkworkshop.web.viewmodel;

import com.ness.zkworkshop.web.model.State;
import com.ness.zkworkshop.web.model.Type;
import org.zkoss.bind.annotation.Init;

import java.util.*;

public class StateVM {
    private List<State> states;

    @Init
    public void init(){
        queryStates();
    }

    private void queryStates() {
        states = new LinkedList<>();
        for (int i = 0 ; i < 4 ; i++){
            State state = new State();
            state.setType(Type.values()[i % 4]);
            state.setValue(1317 * (i + 1));
            state.setRatio(0.329);
            states.add(state);
        }
    }

    public List<State> getStates() {
        return states;
    }

}
