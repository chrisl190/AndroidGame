package uk.ac.qub.eeecs.game.actualGame;

import android.graphics.Bitmap;

import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.Sprite;

/**
 * Simple base class of all space-based entities used in this demo
 */
public class BoardEntity extends Sprite {

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create a board game entity
     *
     * @param startX     x location of the entity
     * @param startY     y location of the entity
     * @param gameScreen Gamescreen to which entity belongs
     */

    private Vector2 targetPos = new Vector2();
    protected BoundingBox bound = new BoundingBox();
    private boolean selected;

    public BoardEntity(float startX, float startY,
                       float width, float height, Bitmap bitmap, GameScreen gameScreen) {
        super(startX, startY, width, height, bitmap, gameScreen);
    }

    private void setTarget(float targetX, float targetY) {
        bound.x = targetX;
        bound.y = targetY;
    }

    public void boundingEntity(LayerViewport layerViewport) {
        BoundingBox bound = getBound();

        if(bound.getLeft() < 0) {
            setTarget(position.x -= bound.getLeft(), position.y);
        } else if(bound.getRight() > layerViewport.getWidth()) {
            setTarget(position.x -= (bound.getRight() - layerViewport.getWidth()), position.y);
        }

        if(bound.getBottom() < 0) {
            setTarget(position.x, position.y -= bound.getBottom());
        } else if(bound.getTop() > layerViewport.getHeight()) {
            setTarget(position.x, position.y -= (bound.getTop() - layerViewport.getHeight()));
        }
    }

    public void setSelected(boolean selected) {this.selected = selected;}
    public boolean isSelected() {return selected;}

    public float startXPos = 700;
    public float startYPos = 125;
}
