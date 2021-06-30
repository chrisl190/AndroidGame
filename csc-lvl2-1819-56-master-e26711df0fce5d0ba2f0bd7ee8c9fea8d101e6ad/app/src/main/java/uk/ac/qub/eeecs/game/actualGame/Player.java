package uk.ac.qub.eeecs.game.actualGame;

import java.util.ArrayList;

public class Player {

    private ArrayList<Territory> listOfTerritories;
    private String name;

    public Player(String playerName) {

        name = playerName;
        listOfTerritories = new ArrayList<Territory>();

    }

    public ArrayList<Territory> getListOfTerritories() {
        return listOfTerritories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
