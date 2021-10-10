package com.coc.evaluator;

public class Main {

    public static void main(String[] args) {
	    Program p = new Program();
        try {
            p.init("league.csv"); //change this filename to load other .csv files
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
