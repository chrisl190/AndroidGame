package uk.ac.qub.eeecs.game.actualGame;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.util.ViewportHelper;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;


public class Key extends PushButton
{
    private String keyCharacter;
    private Paint keyDesign;
    private StringBuffer mLinkedStringBuffer = null;

    public Key(float x, float y, float width, float height, char keyChar, GameScreen gameScreen)
    {
        super(x, y, width, height, "Key", gameScreen);
        processInLayerSpace(true);

        float fontSize = ViewportHelper.convertXDistanceFromLayerToScreen(height, gameScreen.getDefaultLayerViewport(), gameScreen.getDefaultScreenViewport());

        keyCharacter = String.valueOf(keyChar);
        keyDesign = new Paint();
        keyDesign.setTextSize(fontSize);
        keyDesign.setTextAlign(Paint.Align.CENTER);
        keyDesign.setColor(Color.BLACK);
        keyDesign.setTypeface(Typeface.SANS_SERIF);

        // Setup push bitmap if desired - or pass via constructor
    }

    public void setLinkedStringBuffer(StringBuffer linkedStringBuffer)
    {
        mLinkedStringBuffer = linkedStringBuffer;
    }

    @Override
    public void update(ElapsedTime elapsedTime)
    {
        super.update(elapsedTime);

        if(mLinkedStringBuffer != null && isPushTriggered())
        {
            mLinkedStringBuffer.append(keyCharacter);
        }
    }

    Rect textBounds = new Rect();
    Vector2 screenPos = new Vector2();

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D,
                     LayerViewport layerViewport, ScreenViewport screenViewport)
    {
        super.draw(elapsedTime, graphics2D, layerViewport, screenViewport);
        ViewportHelper.convertLayerPosIntoScreen(layerViewport, mBound.x, mBound.y, screenViewport, screenPos);

        // Align in the middle of the y-axis
        keyDesign.getTextBounds(keyCharacter, 0, keyCharacter.length(), textBounds);
        screenPos.y -= textBounds.exactCenterY();

        graphics2D.drawText(keyCharacter, screenPos.x, screenPos.y, keyDesign);
    }
}

