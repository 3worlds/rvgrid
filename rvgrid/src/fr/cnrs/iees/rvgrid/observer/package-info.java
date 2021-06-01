/**
 * <p>The Observer pattern.</p>
 * <p>From <a href="https://en.wikipedia.org/wiki/Observer_pattern">wikipedia</a>: 
 * <blockquote><em>The observer pattern is a software design pattern in which an object, named the subject, 
 * maintains a list of its dependents, called observers, and notifies them automatically of 
 * any state changes, usually by calling one of their methods.</em></blockquote>
 * Here the subject is the {@link Observable} interface and the observer is the {@link Observer} 
 * interface.</p>
 * <p>This pattern was initially present in java up to version 8 but has been deprecated in version 9.
 * Since we needed it to implement ADA's rendezvous pattern, we re-implemented our own.</p>
 * 
 * <img src="{@docRoot}/../doc/images/observer.svg" align="middle" width="440" alt="the observer pattern"/>
 * 
 * <p>{@code Observer}s are registered into an {@code Observable} using the {@code addObserver(...)}
 * method. Later, the {@code Observable} notifies all its registered {@code Observer}s of any
 * significant change using its {@code sendMessage(...)} method. Note that the list of {@code Observer}s
 * can be dynamic during the life cycle of an {@code Observable}, i.e. they can come and go at 
 * runtime.</p>
 * 
 * @author Jacques Gignoux - 1 juin 2021
 *
 */
package fr.cnrs.iees.rvgrid.observer;