package it.polimi.ingsw.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an observable object, or "data" in the model-view paradigm. It can be subclassed to represent
 * an object that the application wants to have observed.
 */
public class Observable<T> {

    private final List<Observer<T>> observers = new ArrayList<>();

    /**
     * Given a list of observers, add an observer to the list
     * @param observer list of observers
     */
    public void addObserver(Observer<T> observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    /**
     * Given a list of observers, remove an observer from the list
     * @param observer list of observers
     */
    public void removeObserver(Observer<T> observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    /**
     * Trigger the update on every observer present in the list.
     * @param message the object that has to get notified.
     */
    protected void notify(T message){
        synchronized (observers) {
            for(Observer<T> observer : observers){
                observer.update(message);
            }
        }
    }

}
