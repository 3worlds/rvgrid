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
	
	// Override to define your own procedures
	//
	public boolean proceed(GridNode node, RVMessage message) {
		return true;
	};

	public String toString() {
		return "[Guard " + this.getClass().getSimpleName() + "]";
	}

}
