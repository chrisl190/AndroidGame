package uk.ac.qub.eeecs.game.actualGame;

import java.util.Random;

import uk.ac.qub.eeecs.gage.world.GameScreen;

public class Dice extends BoardEntity {

    /**
     * Create a dice object
     *
     * @param startX     x location of the dice
     * @param startY     y location of the dice
     * @param gameScreen Gamescreen to which the dice belongs
     */

    public Dice(float startX, float startY, GameScreen gameScreen) {
        super(startX, startY, 60.0f, 60.0f, null, gameScreen);


        gameScreen.getGame().getAssetManager().loadAssets("txt/assets/BoardDemoAssets.JSON");

        mBitmap = gameScreen.getGame().getAssetManager()
                .getBitmap("DiceSide1");
    }

    public int generateDiceRoll() {
        int result;

        Random rng = new Random();

        result = rng.nextInt(6) + 1;

        return result;
    }

    public void visuallyRollDice(int result) {

        switch(result) {
            case 0:
                this.setBitmap(this.getGameScreen().getGame().getAssetManager()
                        .getBitmap("DiceSide0"));
                break;
            case 1:
                this.setBitmap(this.getGameScreen().getGame().getAssetManager()
                        .getBitmap("DiceSide1"));
                break;
            case 2:
                this.setBitmap(this.getGameScreen().getGame().getAssetManager()
                        .getBitmap("DiceSide2"));
                break;
            case 3:
                this.setBitmap(this.getGameScreen().getGame().getAssetManager()
                        .getBitmap("DiceSide3"));
                break;
            case 4:
                this.setBitmap(this.getGameScreen().getGame().getAssetManager()
                        .getBitmap("DiceSide4"));
                break;
            case 5:
                this.setBitmap(this.getGameScreen().getGame().getAssetManager()
                        .getBitmap("DiceSide5"));
                break;
            case 6:
                this.setBitmap(this.getGameScreen().getGame().getAssetManager()
                        .getBitmap("DiceSide6"));
                break;
            case 7:
                this.setBitmap(this.getGameScreen().getGame().getAssetManager()
                        .getBitmap("DiceSide7"));
                break;
        }
    }
}
