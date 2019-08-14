package fr.cnrs.iees.rvgrid.statemachine;

import java.util.ArrayList;
import java.util.List;

import fr.cnrs.iees.rvgrid.RvgridException;

/**
 * 
 * @author Shayne Flint - 2012
 *
 */
public class StateMachineImpl implements StateMachine {

	public static final String INITIAL_PSEUDO_STATE = "<initial pseudo state>";

	private String name = "";

	private   List<State>      stateList = new ArrayList<State>();
	private   List<Transition> initialPseudoStateList = new ArrayList<Transition>();
	protected State                  currentState = null;

	public StateMachineImpl() {
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void addState(State state) {
		stateList.add(state);
	}

	public void addInitialPseudoState(Transition transition) {
		initialPseudoStateList.add(transition);
	}

	public List<State> getStates() {
		return stateList;
	}

	public List<Transition> getInitialPseudoStates() {
		return initialPseudoStateList;
	}

	public void setCurrentState(State state) {
		currentState = state;
	}

	public State getCurrentState() {
		return currentState;
	}

	public List<Transition> getTransitions() {
		List<Transition> result = new ArrayList<Transition>();
		for (State s : stateList) 
			result.addAll(s.getTransitions());
		return result;
	}

	public List<Event> getEvents() {
		List<Event> result = new ArrayList<Event>();
		for (Transition t : getTransitions())
			if (!result.contains(t.getEvent()))
				result.add(t.getEvent());
		for (Transition t : getInitialPseudoStates())
			if (!result.contains(t.getEvent()))
				result.add(t.getEvent());
		return result;
	}

	public State findState(String name) {
		if (name == null || name.equals(INITIAL_PSEUDO_STATE)) {
			return null;
		} else {
			for (State s : stateList) {
				if (s.getName().equals(name))
					return s;
			}
		}
		throw new RvgridException("StateModel: Cannot find state named '" + name + "'");
	}

	public boolean inInitialState() {
		for (Transition t : getTransitions()) {
			if (t.getToState().equals(currentState))
				return false;
		}
		return true;
	}


	public String currentStateString() {
		String result = "[Current State";
		if (currentState == null)
			return StateMachineImpl.INITIAL_PSEUDO_STATE;
		else {
			result = currentState.toString();
			if (inInitialState())
				result = result + " (initial state)";
			if (currentState.isQuiescent())
				result = result + " (quiescent state)";
			return result;
		}

	}

	public String toString() {
		return "[StateMachine " + getName() + ", " + getStates().size() + " states, " + getTransitions().size() + " transitions, " + getEvents().size() + " events, currentState=" + currentStateString();
	}


}

