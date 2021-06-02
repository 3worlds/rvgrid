/**
 * <p>The rendezvous pattern.</p>
 * 
 * <p>It is based on the observer pattern. The {@link GridNode} interface defines the {@code callRendezvous(...)}
 *  method used to execute an action at a rendezvous.</p> 
 * <p>{@link AbstractGridNode}
 * implements a {@code final} version of {@code callRendezvous(...)}, as this method is 
 * too important to allow overriding.  {@code callRendezvous(...)} is {@code synchronized} so that
 * different threads can access the rendezvous and share information. 
 * {@code AbstractGridNode} maintains a list of
 * different rendezvous that can be called depending on a message type which is just an integer.</p>
 * <p>The {@link RendezvousProcess} interface contains the code to be executed at the rendezvous, i.e.
 * {@code GridNode.callRendezvous(...)} will call the correct {@code RendezvousProcess.execute(...)}.</p>
 * <p>Finally, {@link RVMessage}, which is the argument to {@code GridNode.callRendezvous(...)}
 * contains information about the type of process and any other
 * parameters required by {@code RendezvousProcess.execute(...)}.</p>
 * 
 * <img src="{@docRoot}/../doc/images/rendezvous.svg" align="middle" width="800" 
 * alt="the rendezvous pattern"/>
 * 
 * <p>To use the rendezvous pattern you must declare 
 * your own descendant of {@code AbstractGridNode} and  {@code RendezvousProcess}. 
 * See examples of implementation in referenced packages below.</p>
 * 
 * @see fr.cnrs.iees.rvgrid.rendezvous.examples
 * @see fr.cnrs.iees.rvgrid.statemachine
 * 
 * @author Jacques Gignoux - 1 juin 2021
 *
 */
package fr.cnrs.iees.rvgrid.rendezvous;