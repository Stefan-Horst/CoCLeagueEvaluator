package com.coc.evaluator;

import java.util.ArrayList;

public class War {

    private ArrayList<String> players = new ArrayList<>();
    private ArrayList<String> enemies = new ArrayList<>();
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
        for (int i = 0; i < array.length; i++) {
            players.add(array[i].name + array[i].tag);
        }
        System.out.println(players.toString());

        for (int i = 1; i < array.length; i++) {
            WarEvent current = array[i];
            int j = i - 1;
            while(j >= 0 && current.defenderRank > array[j].defenderRank) {
                array[j+1] = array[j];
                j--;
            }
            array[j+1] = current;
        }
        for (int i = 0; i < array.length; i++) {
            enemies.add(array[i].defenderName + array[i].defenderTag);
        }
        System.out.println(enemies.toString());
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public ArrayList<String> getEnemies() {
        return enemies;
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