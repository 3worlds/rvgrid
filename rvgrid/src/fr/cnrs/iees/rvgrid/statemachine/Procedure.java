package fr.cnrs.iees.rvgrid.statemachine;

import fr.cnrs.iees.rvgrid.rendezvous.GridNode;
import fr.cnrs.iees.rvgrid.rendezvous.RVMessage;

/**
 * an ancestor class to actions run when entering a state or a transition in a stateMachine
 * 
 * @author Shayne Flint - 2012
 *
 */
public class Procedure {

	// Override to define your own procedures
	//
	public void run(GridNode node, RVMessage message) { }

	public String toString() {
		return "[Procedure " + this.getClass().getSimpleName() + "]";
	}
		
}
