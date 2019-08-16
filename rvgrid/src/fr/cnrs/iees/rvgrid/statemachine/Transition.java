package fr.cnrs.iees.rvgrid.statemachine;

/**
 *	A transition between two states
 * 
 * @author Shayne Flint - 2012
 *
 */
public class Transition {

	private State toState;
	private Event event;
	private Procedure procedure;
	private Guard guard;
	
	// Constructors
	// we are assuming here that transitions must be constructed after their states.
	
	public Transition(State toState, Event event, Procedure procedure, Guard guard) {
		super();
		this.toState = toState;
		this.event =  event;
		this.procedure = procedure;
		this.guard = guard;
	}

	public Transition(State toState, Event event, Procedure procedure) {
		this(toState,event,procedure,new Guard());
	}

	public Transition(State toState, Event event, Guard guard) {
		this(toState,event,new Procedure(),guard);
	}

	public Transition(State toState, Event event) {
		this(toState,event,new Procedure(),new Guard());
	}
	
	// accessors

	public State getToState() {
		return toState;
	}

	public Event getEvent() {
		return event;
	}

	public Procedure getProcedure() {
		return procedure;
	}
	
	public Guard getGuard() {
		return guard;
	}

	// Object

	@Override
	public String toString() {
		return "[Transition to " + toState + " in response to " + event + " calling " + procedure + "]";
	}

}
