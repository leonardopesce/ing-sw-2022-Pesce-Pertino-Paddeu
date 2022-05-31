package it.polimi.ingsw.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Object that trigger itself when the object <code>Observer</code> gets an update
 */
public class Observable<T> {

    private final List<Observer<T>> observers = new ArrayList<>();

    /**
     * given a list of observers, add an observer to the list
     * @param observer list of observers
     */
    public void addObserver(Observer<T> observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    /**
     * given a list of observers, remove an observer from the list
     * @param observer list of observers
     */
    public void removeObserver(Observer<T> observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    /**
     * Trigger the update on every server present in the list.
     * @param message the object that triggers the update
     */
    protected void notify(T message){
        synchronized (observers) {
            for(Observer<T> observer : observers){
                observer.update(message);
            }
        }
    }

}
