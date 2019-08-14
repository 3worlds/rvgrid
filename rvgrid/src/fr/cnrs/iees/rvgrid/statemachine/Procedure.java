package fr.cnrs.iees.rvgrid.statemachine;

import fr.cnrs.iees.rvgrid.rendezvous.GridNode;
import fr.cnrs.iees.rvgrid.rendezvous.RVMessage;

/**
 * an ancestor class to actions run when entering a state in a stateMachine
 * 
 * @author Shayne Flint - 2012
 *
 */
public class Procedure {
	
	// a convenience to retrieve the graph, basically
	protected StateMachineNode myStateMachine = null;

	// Override to define your own procedures
	//
	public void run(GridNode node, RVMessage message) { }
	
	// override to define your own inits
	//
	public void initialise() {	}

	public String toString() {
		return "[Procedure " + this.getClass().getSimpleName() + "]";
	}
	
	public final void setStateMachine(StateMachineNode n) {
		myStateMachine = n;
	}
		
}
