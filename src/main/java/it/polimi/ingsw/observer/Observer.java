package it.polimi.ingsw.observer;

/**
 * A class can implement the Observer interface when it wants to be informed of changes in observable objects.
 */
public interface Observer<T> {
    void update(T message);
}
