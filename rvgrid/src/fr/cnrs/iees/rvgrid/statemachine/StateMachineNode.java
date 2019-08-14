package fr.cnrs.iees.rvgrid.statemachine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import fr.cnrs.iees.rvgrid.observer.Observable;
import fr.cnrs.iees.rvgrid.observer.Observer;
import fr.cnrs.iees.rvgrid.rendezvous.GridNode;
import fr.cnrs.iees.rvgrid.rendezvous.AbstractGridNode;
import fr.cnrs.iees.rvgrid.rendezvous.RVMessage;
import fr.ens.biologie.generic.Resettable;

/**
 * @author Shayne Flint - 2012
 *
 */
public class StateMachineNode<O extends GridNode & Observer> 
		extends AbstractGridNode 
		implements Resettable, StateMachine, Observable<O> {

	private Logger log = Logger.getLogger(StateMachineNode.class.getName());
	
	private Set<O> stateMachineListenerNodeList = new HashSet<O>();

	protected StateMachineImpl stateMachine = new StateMachineImpl();
	
	/** JG-added 30/9/2015 - a convenience to quickly get the event codes from their names 	 */
	private Map<String, Integer> eventCodes = new HashMap<String, Integer>();

	/** JG-added: a list of instances of inner procedures, if present, so that the 
	 * initialisation can find them - External Procedures are not really usable if they
	 * have to know about the state machine. All current implementations of state machine
	 * (eg simulator) use inner classes for procedures, but at the moment these cannot
	 * be properly setup from a dsl. descendant classes should fill this procedure list
	 * with an instance of every inner procedure  class they have, and the initialise()
	 * method here will get the one it finds when reading the dsl. */
	private List<Procedure> innerProcedureClassList = new LinkedList<Procedure>();
	/** use this method in descendants to fill the previous list */


	protected final void registerProcedure (Procedure p) {
		innerProcedureClassList.add(p);
	}

	private List<Guard> innerGuardClassList = new LinkedList<Guard>();
	/** use this method in descendants to fill the previous list */


	protected final void registerGuard (Guard p) {
		innerGuardClassList.add(p);
	}

//	public StateMachineNode(SimplePropertyList propertyList, Uid uid) {
//		super(propertyList, uid);
//	}

	public StateMachineNode() {
		super();
	}

	public int getEventCode(String name) {
		return eventCodes.get(name);
	}

	

	// The following method can be used to set the state machine programatically
	//
	public void setStateMachine(StateMachineImpl stateMachine) {
		this.stateMachine = stateMachine;
		sendStructureToListeners();
	}

	public StateMachineImpl getStateMachine() {
		return stateMachine;
	}

	private void sendStructureToListeners() {
		for (GridNode l : stateMachineListenerNodeList) {
			RVMessage structureMessage = new RVMessage(StateTransitionProcess.STATE_MACHINE_STRUCTURE,stateMachine,this,l);
			l.callRendezvous(structureMessage);			
		}
	}

	/**
	 * For specific house-keeping in descendants. It is expected to be called whenever the StateMachine 
	 * goes into initialPseudoState.
	 * Of course other uses are possible, the consistency is left to the user
	 */
	@Override
	public void reset() {
	}

	@Override
	public Iterable<State> getStates() {
		return stateMachine.getStates();
	}

	@Override
	public Iterable<Transition> getInitialPseudoStates() {
		return stateMachine.getInitialPseudoStates();
	}

	@Override
	public Iterable<Transition> getTransitions() {
		return stateMachine.getTransitions();
	}

	@Override
	public void setCurrentState(State state) {
		stateMachine.setCurrentState(state);
	}

	@Override
	public State getCurrentState() {
		return stateMachine.getCurrentState();
	}

	@Override
	public Iterable<Event> getEvents() {
		return stateMachine.getEvents();
	}

	@Override
	public boolean inInitialState() {
		return stateMachine.inInitialState();
	}

	@Override
	public void addObserver(O listener) {
		stateMachineListenerNodeList.add(listener);
	}

	@Override
	public void sendMessage(int msgType, Object payload) {
		for (O l : stateMachineListenerNodeList) {
			RVMessage statusMessage = new RVMessage(msgType,payload,this,l);
			l.callRendezvous(statusMessage);
		}
	}


}
