/**
 * <p>Implementation of a <a href="https://en.wikipedia.org/wiki/Finite-state_machine">
 * finite-state machine</a> using the rendezvous pattern.</p>
 * 
 * <p>The two main classes in this package are {@link StateMachineEngine} and {@link StateMachineController}.
 * The former represents the system subject to finite state changes and implements its dynamic behaviour; 
 * the latter enables to control the former by sending it messages that trigger transitions. They
 * are related through the observer pattern (see below)</p>
 * <p>Two interfaces enable to implement other types of engine and controller if needed: 
 * {@link StateMachine} declares all the methods needed to implement the dynamics of the state machine;
 * {@link StateMachineObserver} implements the additional method needed by a 
 * {@link fr.cnrs.iees.rvgrid.rendezvous.GridNode GridNode} to
 * interact with a {@code StateMachine}.</p>
 * <p>The other classes implements the parts of a {@code StateMachine}.</p>
 * 
 * <img src="{@docRoot}/../doc/images/statemachine.svg" align="middle" width="1500" 
 * alt="the rendezvous pattern"/>  
 * 
 * <p>If your state machine is meant to run on a single computer in a possibly multi-thread application,
 * these classes can be used with no additional change. If you plan to use it on more complex
 * architectures (multi-process applications on clusters, grids, etc.), then you will need to
 * extend the possibilities of the {@link fr.cnrs.iees.rvgrid.rendezvous.AbstractGridNode AbstractGridNode} 
 * class to allow for inter-process message passing.</p>
 * 
 * @author Jacques Gignoux - 2 juin 2021
 * 
 * @see fr.cnrs.iees.rvgrid.rendezvous
 * @see fr.cnrs.iees.rvgrid.observer
 *
 */
package fr.cnrs.iees.rvgrid.statemachine;