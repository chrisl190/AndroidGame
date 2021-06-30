package uk.ac.qub.eeecs.game.actualGame;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.icu.text.IDNA;
import android.text.Html;
import android.util.Log;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.jar.Attributes;
import java.util.ArrayList;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.util.ViewportHelper;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;

// class created by Anna-Lise
public class NameScreen extends GameScreen
{
    //Prompt user to enter name
    private  String name = "Please enter your name: ";
    private StringBuffer playerName = new StringBuffer();
    String KeyLabels = "abcdefghijklmnopqrstuvwxyz1234567890";
    ArrayList<Key> Keys = new ArrayList<>();


    private PushButton NextButton;
    private PushButton BackButton;
    private PushButton BackSpaceButton;
    private  PushButton SpaceButton;

    public NameScreen(Game game) {
        super("NameScreen", game);

        //Create Next Button
        AssetManager assetManager = mGame.getAssetManager();

        getGame().getAssetManager().loadAndAddBitmap("Key", "img/key.png");
        createAndPositionKeys();
        assetManager.loadAndAddBitmap("Next", "img/Next1.png");
        assetManager.loadAndAddBitmap("NextSelected", "img/NextSelected1.png");
        assetManager.loadAndAddBitmap("Back", "img/NBack.png");
        assetManager.loadAndAddBitmap("BackSelected", "img/NBackSelected.png");
        assetManager.loadAndAddBitmap("BackSpace", "img/BackSpace.png");
        assetManager.loadAndAddBitmap("SpaceButton", "img/SpaceButton1.png");



        //Button Spacing
        int spacingX = (int) mDefaultLayerViewport.getWidth() / 10;
        int spacingY = (int) mDefaultLayerViewport.getHeight() / 6;

        //Button Location
        NextButton = new PushButton
                (
                spacingX * 9.5f, spacingY * 1.75f, spacingX, spacingY,
                "Next", "NextSelected", this);
        NextButton.setPlaySounds(true, true);


        BackButton = new PushButton
                (
                spacingX * 9.5f, spacingY * 0.5f, spacingX, spacingY,
                "Back", "BackSelected", this);
        BackButton.setPlaySounds(true, true);

        BackSpaceButton = new PushButton
                (
                spacingX * 8.0f, spacingY * 0.5f, spacingX, spacingY,
                "BackSpace",this);

        SpaceButton = new PushButton
                (
                        spacingX * 7.0f, spacingY * 0.5f, spacingX, spacingY,
                        "SpaceButton", this);




}

    private void createAndPositionKeys()
    {
        //Creation of keyboard

        final int rowLength = 10;
        final float topLeftKeyX = 50.0f, topLeftKeyY = 150.0f;
        final float keyWidth = 35.0f, keyHeight = 35.0f;
        final float keyXSpacing = 5.0f, keyYSpacing = 5.0f;

        float keyX = topLeftKeyX, keyY = topLeftKeyY;
        for(int keyIdx = 0; keyIdx < KeyLabels.length(); keyIdx++)
        {
            Key key = new Key(keyX, keyY, keyWidth, keyHeight, KeyLabels.charAt(keyIdx), this);
            key.setLinkedStringBuffer(playerName);
            Keys.add(key);

            if(keyIdx > 0 && (keyIdx+1) % rowLength == 0)
            {
                keyY -= keyHeight + keyYSpacing;
                keyX = topLeftKeyX;
            }
            else keyX += keyWidth + keyXSpacing;
        }

    }


    public void update(ElapsedTime elapsedTime) {
        // Get any touch events that have occurred since the last update
        List<TouchEvent> touchEvents = mGame.getInput().getTouchEvents();
        for (Key key : Keys)
            key.update(elapsedTime);

        if (touchEvents.size() > 0) {
            //Update each button - switch screens when necessary
            NextButton.update(elapsedTime);
            BackButton.update(elapsedTime);
            BackSpaceButton.update(elapsedTime);
            SpaceButton.update(elapsedTime);

            if (BackSpaceButton.isPushTriggered())
                playerName.delete(playerName.length()-1,playerName.length());

            if (SpaceButton.isPushTriggered())
                playerName.insert(playerName.length(), " ");


            //Load the start screen when name is entered
            if(BackButton.isPushTriggered()) {
                mGame.getScreenManager().removeScreen(this);
            }
            if (!playerName.toString().isEmpty()) {
                if (NextButton.isPushTriggered())
                    mGame.getScreenManager().addScreen(new StartScreen(mGame, playerName.toString().toUpperCase()));



            }
            }

        }

    private Paint textPaint = new Paint();
    private Paint textPaintTitle = new Paint();

    /**
     * Draw the demo screen
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D)
    {
        //Design of keys on the keyboard
        graphics2D.clear(Color.parseColor("#D7FFD7"));

        for(Key key : Keys)
            key.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);


        textPaint.setTextSize(80.0f);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(Typeface.MONOSPACE);

        textPaintTitle.setTextSize(120.0f);
        textPaintTitle.setTextAlign(Paint.Align.LEFT);
        textPaintTitle.setTypeface(Typeface.DEFAULT_BOLD);



        graphics2D.drawText(playerName.toString(), 50.0f, 200.0f, textPaint);
        graphics2D.drawText(name, 50.0f, 100.0f, textPaintTitle);
        BackButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        BackSpaceButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        SpaceButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);



        if (!playerName.toString().isEmpty())
        {
            NextButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        }



    }



}






