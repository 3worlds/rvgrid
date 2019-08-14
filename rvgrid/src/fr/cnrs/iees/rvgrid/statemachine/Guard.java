package fr.cnrs.iees.rvgrid.statemachine;

import fr.cnrs.iees.rvgrid.rendezvous.GridNode;
import fr.cnrs.iees.rvgrid.rendezvous.RVMessage;

/**
 * an ancestor class to guards for state machines - run before calling a state procedure
 * 
 * @author Shayne Flint - 2012
 *
 */
public class Guard {

	// a convenience to retrieve the graph, basically
	protected StateMachineNode myStateMachine = null;
	
	// Override to define your own procedures
	//
	public boolean proceed(GridNode node, RVMessage message) {
		return true;
	};

	// override to define your own inits
	//
	public void initialise() {	}

	public String toString() {
		return "[Guard " + this.getClass().getSimpleName() + "]";
	}
	
	public final void setStateMachine(StateMachineNode n) {
		myStateMachine = n;
	}

}
