package com.coc.evaluator;

import com.opencsv.bean.CsvBindByName;

public class WarEvent {

    @CsvBindByName
    boolean attacker_is_home_clan;

    @CsvBindByName
    String warID;

    @CsvBindByName
    String name;

    @CsvBindByName
    String tag;

    @CsvBindByName
    int stars;

    @CsvBindByName
    int destructionPercentage;

    @CsvBindByName
    int rank;

    @CsvBindByName
    int thLevel;

    @CsvBindByName
    int defenderTH;

    public WarEvent() {}
}
