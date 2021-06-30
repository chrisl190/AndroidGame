package uk.ac.qub.eeecs.game.actualGame;

import android.graphics.Bitmap;

import java.util.ArrayList;

import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.Sprite;

public class Territory extends Sprite {

    private ArrayList<Unit> listOfUnits;
    private ArrayList<Territory> surroundingTerritories;
    private Card relatedCard;
    private float territoryX;
    private float territoryY;
    private Player owner;

    private int numOfSmallUnits = 0;
    private int numOfBigUnits = 0;

    public Territory(float startX, float startY,
                     float width, float height, GameScreen gameScreen) {
        super(startX, startY, width, height, gameScreen.getGame().getAssetManager().getBitmap("CopperPad"), gameScreen);

        territoryX = startX;
        territoryY = startY;

        surroundingTerritories = new ArrayList<Territory>();

        listOfUnits = new ArrayList<Unit>();
    }

    public ArrayList<Unit> getListOfUnits() {
        return listOfUnits;
    }

    public ArrayList<Territory> getSurroundingTerritories() {
        return surroundingTerritories;
    }

    public float getTerritoryX() {return territoryX; }

    public float getTerritoryY() {return territoryY; }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player newOwner) {
        this.owner = newOwner;
    }

    public Card getRelatedCard() {
        return relatedCard;
    }

    public void setRelatedCard(Card relatedCard) {
        this.relatedCard = relatedCard;
    }

    public int getNumOfSmallUnits() {
        return numOfSmallUnits;
    }

    public void setNumOfSmallUnits(int numOfSmallUnits) {
        this.numOfSmallUnits = numOfSmallUnits;
    }

    public int getNumOfBigUnits() {
        return numOfBigUnits;
    }

    public void setNumOfBigUnits(int numOfBigUnits) {
        this.numOfBigUnits = numOfBigUnits;
    }
}
