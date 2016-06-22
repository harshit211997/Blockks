package com.sdsmdg.bouncyball;

import java.util.ArrayList;
import java.util.List;

public class Subject {

    List<Observer> observers = new ArrayList<>();

    protected void notifyObservers(int flag) {

        for (int i = 0; i < observers.size(); i++) {
            Observer observer = observers.get(i);
            observer.onNotify(flag);
        }

    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

}
