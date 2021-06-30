package uk.ac.qub.eeecs.game.actualGame;

import uk.ac.qub.eeecs.gage.world.GameScreen;

public class Unit extends BoardEntity {

    private boolean isBigUnit;

    private boolean isExhausted = false;

    public Unit(float startX, float startY, GameScreen gameScreen, boolean isABigUnit) {
        super(startX, startY, 20, 20, gameScreen.getGame().getAssetManager().getBitmap("Unit"), gameScreen);
        if (isABigUnit) {
            this.setBitmap(gameScreen.getGame().getAssetManager().getBitmap("BigUnit"));
        }
        isBigUnit = isABigUnit;

        if (isBigUnit) {
            this.setWidth(30);
            this.setHeight(30);
        }
    }

    public boolean isBigUnit() {
        return isBigUnit;
    }

    public boolean isExhausted() {
        return isExhausted;
    }

    public void setExhausted(boolean exhausted) {
        isExhausted = exhausted;
    }
}
