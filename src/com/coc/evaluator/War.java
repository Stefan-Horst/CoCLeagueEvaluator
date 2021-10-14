package com.coc.evaluator;

import java.util.ArrayList;

public class War {

    private ArrayList<String> players = new ArrayList<>();
    private ArrayList<WarEvent> events = new ArrayList<>();
    private String id;

    public War() {}

    public void init() {
        WarEvent[] array = new WarEvent[events.size()];
        array = events.toArray(array);

        for (int i = 1; i < array.length; i++) {
            WarEvent current = array[i];
            int j = i - 1;
            while(j >= 0 && current.rank > array[j].rank) {
                array[j+1] = array[j];
                j--;
            }
            array[j+1] = current;
        }
        for (WarEvent warEvent : array) {
            players.add(warEvent.name + warEvent.tag);
        }
        System.out.println(players.toString());
    }

    public ArrayList<WarEvent> getEvents() {
        return events;
    }

    public void addEvent(WarEvent warEvent) {
        events.add(warEvent);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
