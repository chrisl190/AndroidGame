package uk.ac.qub.eeecs.game.actualGame;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import java.util.List;

import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;


public class StartScreen extends GameScreen
{
    // Create territory buttons - user starting position

    private PushButton Territory1Button;
    private PushButton Territory2Button;
    private PushButton Territory3Button;
    private PushButton Territory4Button;
    private PushButton Territory5Button;
    private PushButton BackButton;
    private PushButton BeginButton;
    public String pName;
    public String welcome;
    public String select;

    private int startingTerritoryNum = 0;

    //Start Screen - player chooses territory
    public StartScreen(Game game, String playerName)
    {
        super("StartScreen", game);
        this.pName = playerName;

        welcome = "Welcome to the Game: " + pName;
        select ="Please select your starting territory";

        //Load button images and change colour when selected
        AssetManager assetManager = mGame.getAssetManager();
        assetManager.loadAndAddBitmap("Territory1", "img/T1.png");
        assetManager.loadAndAddBitmap("Territory2", "img/T2.png");
        assetManager.loadAndAddBitmap("Territory3", "img/T3.png");
        assetManager.loadAndAddBitmap("Territory4", "img/T4.png");
        assetManager.loadAndAddBitmap("Territory5", "img/T5.png");
        assetManager.loadAndAddBitmap("Back", "img/BackButton1.png");
        assetManager.loadAndAddBitmap("Begin", "img/Begin1.png");

        assetManager.loadAndAddBitmap("Territory1Selected", "img/T1S.png");
        assetManager.loadAndAddBitmap("Territory2Selected", "img/T2S.png");
        assetManager.loadAndAddBitmap("Territory3Selected", "img/T3S.png");
        assetManager.loadAndAddBitmap("Territory4Selected", "img/T4S.png");
        assetManager.loadAndAddBitmap("Territory5Selected", "img/T5S.png");
        assetManager.loadAndAddBitmap("BackSelected", "img/BackButtonSelected1.png");
        assetManager.loadAndAddBitmap("BeginSelected", "img/BeginSelected1.png");

        //Button Spacing
        int spacingX = (int) mDefaultLayerViewport.getWidth() / 5;
        int spacingY = (int) mDefaultLayerViewport.getHeight() / 5;

        //Button Location
        BeginButton = new PushButton(
                spacingX * 4.5f, spacingY * 0.5f, spacingX, spacingY,
                "Begin", "BeginSelected", this);
        BeginButton.setPlaySounds(true, true);
        BackButton = new PushButton(
                spacingX * 0.5f, spacingY * 0.5f, spacingX, spacingY,
                "Back", "BackSelected", this);
        BackButton.setPlaySounds(true, true);
        Territory1Button = new PushButton(
                spacingX * 0.75f, spacingY * 2.75f, spacingX, spacingY,
                "Territory1", "Territory1Selected", this);
        Territory1Button.setPlaySounds(true, true);
        Territory2Button = new PushButton(
                spacingX * 2.5f, spacingY * 2.75f, spacingX, spacingY,
                "Territory2", "Territory2Selected", this);
        Territory2Button.setPlaySounds(true, true);
        Territory3Button = new PushButton(
                spacingX * 4.25f, spacingY * 2.75f, spacingX, spacingY,
                "Territory3", "Territory3Selected", this);
        Territory3Button.setPlaySounds(true, true);
        Territory4Button = new PushButton(
                spacingX * 1.75f, spacingY * 1.5f, spacingX, spacingY,
                "Territory4", "Territory4Selected", this);
        Territory4Button.setPlaySounds(true, true);
        Territory5Button = new PushButton(
                spacingX * 3.25f, spacingY * 1.5f, spacingX, spacingY,
                "Territory5", "Territory5Selected", this);
        Territory5Button.setPlaySounds(true, true);
    }


    public void update(ElapsedTime elapsedTime) {

        // Process any touch events
        Input input = mGame.getInput();

        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {
            updateButtons(elapsedTime);
            performActionsForPressedButtons();
        }
    }


            private void updateButtons(ElapsedTime elapsedTime) {
            //Update each button - switch screens when necessary
            BeginButton.update(elapsedTime);
            BackButton.update(elapsedTime);

            // Tested on emulator when running the program
            if (BeginButton.isPushTriggered())
            {
                if (startingTerritoryNum != 0) {
                    mGame.getScreenManager().addScreen(new BoardDemoGame(mGame, startingTerritoryNum, pName));
                }
            }
            if (BackButton.isPushTriggered())
            {
                mGame.getScreenManager().removeScreen(this);
            }

            //user should select their starting territory

            Territory1Button.update(elapsedTime);
            Territory2Button.update(elapsedTime);
            Territory3Button.update(elapsedTime);
            Territory4Button.update(elapsedTime);
            Territory5Button.update(elapsedTime);



        

    }
    private void performActionsForPressedButtons() {
        if (Territory1Button.isPushTriggered()) {
            Territory1Button.setBitmap(mGame.getAssetManager().getBitmap("Territory1Selected"));
            Territory2Button.setBitmap(mGame.getAssetManager().getBitmap("Territory2"));
            Territory3Button.setBitmap(mGame.getAssetManager().getBitmap("Territory3"));
            Territory4Button.setBitmap(mGame.getAssetManager().getBitmap("Territory4"));
            Territory5Button.setBitmap(mGame.getAssetManager().getBitmap("Territory5"));
            startingTerritoryNum = 1;
        }
        if (Territory2Button.isPushTriggered()) {
            Territory1Button.setBitmap(mGame.getAssetManager().getBitmap("Territory1"));
            Territory2Button.setBitmap(mGame.getAssetManager().getBitmap("Territory2Selected"));
            Territory3Button.setBitmap(mGame.getAssetManager().getBitmap("Territory3"));
            Territory4Button.setBitmap(mGame.getAssetManager().getBitmap("Territory4"));
            Territory5Button.setBitmap(mGame.getAssetManager().getBitmap("Territory5"));
            startingTerritoryNum = 2;

        }
        if (Territory3Button.isPushTriggered()) {
            Territory1Button.setBitmap(mGame.getAssetManager().getBitmap("Territory1"));
            Territory2Button.setBitmap(mGame.getAssetManager().getBitmap("Territory2"));
            Territory3Button.setBitmap(mGame.getAssetManager().getBitmap("Territory3Selected"));
            Territory4Button.setBitmap(mGame.getAssetManager().getBitmap("Territory4"));
            Territory5Button.setBitmap(mGame.getAssetManager().getBitmap("Territory5"));
            startingTerritoryNum = 3;
        }
        if (Territory4Button.isPushTriggered()) {
            Territory1Button.setBitmap(mGame.getAssetManager().getBitmap("Territory1"));
            Territory2Button.setBitmap(mGame.getAssetManager().getBitmap("Territory2"));
            Territory3Button.setBitmap(mGame.getAssetManager().getBitmap("Territory3"));
            Territory4Button.setBitmap(mGame.getAssetManager().getBitmap("Territory4Selected"));
            Territory5Button.setBitmap(mGame.getAssetManager().getBitmap("Territory5"));
            startingTerritoryNum = 4;
        }
        if (Territory5Button.isPushTriggered()) {
            Territory1Button.setBitmap(mGame.getAssetManager().getBitmap("Territory1"));
            Territory2Button.setBitmap(mGame.getAssetManager().getBitmap("Territory2"));
            Territory3Button.setBitmap(mGame.getAssetManager().getBitmap("Territory3"));
            Territory4Button.setBitmap(mGame.getAssetManager().getBitmap("Territory4"));
            Territory5Button.setBitmap(mGame.getAssetManager().getBitmap("Territory5Selected"));
            startingTerritoryNum = 5;
        }
    }
        private Paint textPaintTitle = new Paint();
    private Paint textPaintSelect = new Paint();

    public void draw (ElapsedTime elapsedTime, IGraphics2D graphics2D){

        // Clear the screen and draw the buttons
        graphics2D.clear(Color.parseColor("#D7FFD7"));

        textPaintSelect.setTextSize(80.0f);
        textPaintSelect.setTextAlign(Paint.Align.LEFT);
        textPaintSelect.setTypeface(Typeface.DEFAULT_BOLD);


        textPaintTitle.setTextSize(100.0f);
        textPaintTitle.setTextAlign(Paint.Align.LEFT);
        textPaintTitle.setTypeface(Typeface.DEFAULT_BOLD);

        graphics2D.drawText(welcome, 250f, 100.0f, textPaintTitle);
        graphics2D.drawText(select, 300.0f, 200.0f, textPaintSelect);
        BeginButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        BackButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        Territory1Button.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        Territory2Button.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        Territory3Button.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        Territory4Button.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        Territory5Button.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);


    }




}

