package com.coc.evaluator;

import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Program {

    private Map<String, Double> players = new HashMap<>();
    private ArrayList<War> wars = new ArrayList<>();

    public Program() {
        for (int i = 0; i < 7; i++)
            wars.add(new War());
    }

    public void init(String filename) throws Exception{
        java.util.List<WarEvent> beans =
                new CsvToBeanBuilder(new FileReader(filename))
                .withType(WarEvent.class).build().parse();

        Iterator iterator = beans.iterator();
        while (iterator.hasNext()) {
            WarEvent we = (WarEvent) iterator.next();
            if (!we.attacker_is_home_clan)
                iterator.remove();
        }

        int i = 0;
        War currentWar = wars.get(i);
        currentWar.setId(beans.get(i).warID);
        for (WarEvent we : beans) {
            if (!we.warID.equals(currentWar.getId())) {
                currentWar.init();

                i++;
                currentWar = wars.get(i);
                currentWar.setId(we.warID);
            }
            currentWar.addEvent(we);

            players.put(we.name+we.tag, 0.0);
        }

        createRanking();
    }

    public void createRanking() {
        for (War w : wars) {
            for (WarEvent we : w.getEvents()) {
                for (String player : players.keySet()) {
                    if ((we.name+we.tag).equals(player)) {
                        double val = we.stars * 1.2 + we.destructionPercentage * 0.02;

                        if (we.thLevel < we.defenderTH)
                            val *= 1.0 + (we.defenderTH - we.thLevel) * 0.3;
                        else if (we.thLevel > we.defenderTH)
                            val *= 1.0 - (we.thLevel - we.defenderTH) * 0.1;
                        val *= 1.0 + we.thLevel / 200.0;

                        val += players.get(player);
                        players.put(player, Math.round(val * 100.0) / 100.0);
                    }
                }

                //if (w.getPlayers().indexOf(we.name+we.tag) < w.getEnemies().indexOf(we.defenderName+we.defenderTag))

            }
            System.out.println(players);
        }

        Map<String,Double> sorted =
                players.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        System.out.println("\nCWL Ranking:\n==============================");
        int i = 1;
        for (String player : sorted.keySet()) {
            System.out.println(i + ": " + player + " = " + players.get(player));
            i++;
        }
    }
}
