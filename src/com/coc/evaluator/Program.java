package com.coc.evaluator;

import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class Program {

    private static final double WEIGHT_STARS = 1.2;
    private static final double WEIGHT_DESTRUCTION = 0.02;
    private static final double WEIGHT_HIGHER_TH = 0.1;
    private static final double WEIGHT_LOWER_TH = 0.3;
    private static final double WEIGHT_TH_LEVEL = 0.005;

    private Map<String, Double> players = new HashMap<>();
    private ArrayList<War> wars = new ArrayList<>();

    public Program() {
        for (int i = 0; i < 7; i++)
            wars.add(new War());
    }

    public void init(String filename) throws Exception{
        List<WarEvent> beans =
                new CsvToBeanBuilder(new FileReader(filename))
                .withType(WarEvent.class).build().parse();

        beans.removeIf(we -> !we.attacker_is_home_clan);

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
                        double val = we.stars * WEIGHT_STARS + we.destructionPercentage * WEIGHT_DESTRUCTION;

                        if (we.thLevel < we.defenderTH)
                            val *= 1.0 + (we.defenderTH - we.thLevel) * WEIGHT_LOWER_TH;
                        else if (we.thLevel > we.defenderTH)
                            val *= 1.0 - (we.thLevel - we.defenderTH) * WEIGHT_HIGHER_TH;
                        val *= 1.0 + we.thLevel * WEIGHT_TH_LEVEL;

                        val += players.get(player);
                        players.put(player, Math.round(val * 100.0) / 100.0);
                    }
                }
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
