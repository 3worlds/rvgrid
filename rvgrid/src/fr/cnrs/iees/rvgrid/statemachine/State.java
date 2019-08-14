package fr.cnrs.iees.rvgrid.statemachine;

import java.util.LinkedList;

/**
 * A discrete state characteristic of a state machine
 *  
 * @author Shayne Flint - 2012
 *
 */
public class State {

	private String name;
	private Procedure procedure;
	private LinkedList<Transition> transitionList = new LinkedList<Transition>();

	public State(String name, Procedure procedure) {
		setName(name);
		setProcedure(procedure);
	}
	
	public State(String name) {
		setName(name);
		setProcedure(new Procedure());
	}
	
	public State() {
		setName(this.getClass().getSimpleName());
		setProcedure(new Procedure());
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setProcedure(Procedure procedure) {
		this.procedure = procedure;
	}
	
	public Procedure getProcedure() {
		return procedure;
	}
	
	public void addTransition(Transition transition) {
		transitionList.add(transition);
	}

	public LinkedList<Transition> getTransitions() {
		return transitionList;
	}
	
	public String toString() {
		return "[State " + name + " with " + procedure + "]";
	}
	
	public boolean isQuiescent() {
		return transitionList.size() == 0;
	}
	
}

