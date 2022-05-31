package it.polimi.ingsw.observer;

/**
 * Object that notifies the observers when it gets an update.
 */
public interface Observer<T> {
    void update(T message);
}
