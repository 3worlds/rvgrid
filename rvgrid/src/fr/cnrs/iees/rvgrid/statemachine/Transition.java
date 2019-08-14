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
	
	public Transition(State toState, Event event, Procedure procedure, Guard guard) {
		setToState(toState);
		setEvent(event);
		setProcedure(procedure);
		setGuard(guard);
	}

	public Transition(State toState, Event event, Procedure procedure) {
		setToState(toState);
		setEvent(event);
		setProcedure(procedure);
		setGuard(new Guard());
	}

	public Transition(State toState, Event event, Guard guard) {
		setToState(toState);
		setEvent(event);
		setProcedure(new Procedure());
		setGuard(guard);
	}

	public Transition(State toState, Event event) {
		setToState(toState);
		setEvent(event);
		setProcedure(new Procedure());
		setGuard(new Guard());
	}

	public Transition() {
		setToState(null);
		setEvent(null);
		setProcedure(new Procedure());
		setGuard(new Guard());
	}

	public void setToState(State toState) {
		this.toState = toState;
	}

	public State getToState() {
		return toState;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
	
	public Event getEvent() {
		return event;
	}

	public void setProcedure(Procedure procedure) {
		this.procedure = procedure;
	}
	
	public Procedure getProcedure() {
		return procedure;
	}
	
	public void setGuard(Guard guard) {
		this.guard = guard;
	}
	
	public Guard getGuard() {
		return guard;
	}
	

	public String toString() {
		return "[Transition to " + toState + " in response to " + event + " calling " + procedure + "]";
	}
	
}
