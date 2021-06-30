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
import uk.ac.qub.eeecs.gage.world.Sprite;

public class MainMenu extends GameScreen {

    //Buttons
    private PushButton StartGameButton;
    private PushButton OptionsButton;
    private PushButton GameRulesButton;
    private PushButton ExitButton;
    private PushButton CreditsButton;
    private Sprite logo;

    //Game Screen
    public MainMenu(Game game) {
        super("MainMenu", game);
// Images for main menu


        AssetManager assetManager = mGame.getAssetManager();
        assetManager.loadAndAddBitmap("StartGame", "img/StartGame.png");
        assetManager.loadAndAddBitmap("GameRules", "img/GameRules.png");
        assetManager.loadAndAddBitmap("Options", "img/Options.png");
        assetManager.loadAndAddBitmap("StartGameSelected", "img/StartGameSelected.png");
        assetManager.loadAndAddBitmap("GameRulesSelected", "img/GameRulesSelected.png");
        assetManager.loadAndAddBitmap("OptionSelected", "img/OptionSelected.png");
        assetManager.loadAndAddBitmap("Exit1", "img/Exit1.png");
        assetManager.loadAndAddBitmap("ExitSelected1", "img/ExitSelected1.png");
        assetManager.loadAndAddBitmap("Credits", "img/Credits.png");
        assetManager.loadAndAddBitmap("CreditsSelected", "img/CreditsSelected.png");
        assetManager.loadAndAddBitmap("Logo", "img/Logo.png");

        logo = new Sprite(mDefaultLayerViewport.halfWidth, mDefaultLayerViewport.getHeight()*0.8f, 200, 100, assetManager.getBitmap("Logo"), this);


        int spacingX = (int)mDefaultLayerViewport.getWidth() / 3;
        int spacingY = (int)mDefaultLayerViewport.getHeight() /4
                ;


        // Create the trigger buttons
        StartGameButton = new PushButton(
                spacingX * 0.5f, spacingY * 2f, spacingX, spacingY,
                "StartGame", "StartGameSelected",this);
        StartGameButton.setPlaySounds(true, true);
        OptionsButton = new PushButton(
                spacingX * 1.5f, spacingY * 2f, spacingX, spacingY,
                "Options", "OptionSelected", this);
        OptionsButton.setPlaySounds(true, true);
        GameRulesButton = new PushButton(
                spacingX * 2.5f, spacingY *2f, spacingX, spacingY,
                "GameRules", "GameRulesSelected", this);
        GameRulesButton.setPlaySounds(true, true);
       ExitButton = new PushButton(
               spacingX * 1f, spacingY * 0.75f, spacingX, spacingY,
               "Exit1", "ExitSelected1", this);
       ExitButton.setPlaySounds(true, true);
       CreditsButton = new PushButton(spacingX * 2f, spacingY * 0.75f, spacingX, spacingY,
               "Credits", "CreditsSelected", this);
       CreditsButton.setPlaySounds(true, true);

    }



    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Update the menu screen
     *
     * @param elapsedTime Elapsed time information
     */
    @Override
    public void update(ElapsedTime elapsedTime) {

        // Process any touch events occurring since the update
        Input input = mGame.getInput();

        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {

            // Update each button and transition if needed
            StartGameButton.update(elapsedTime);
            OptionsButton.update(elapsedTime);
            GameRulesButton.update(elapsedTime);
            ExitButton.update(elapsedTime);
            CreditsButton.update(elapsedTime);


            if (StartGameButton.isPushTriggered())
              mGame.getScreenManager().addScreen(new NameScreen(mGame));
            if (OptionsButton.isPushTriggered())
                mGame.getScreenManager().addScreen(new OptionsDemoScreen(mGame));
            if (GameRulesButton.isPushTriggered())
                mGame.getScreenManager().addScreen(new RulesScreen(mGame, true));
            if (CreditsButton.isPushTriggered())
                mGame.getScreenManager().addScreen(new CreditScreen(mGame));
            if (ExitButton.isPushTriggered()) {
                   mGame.getScreenManager().dispose();
                    mGame.getActivity().finishAndRemoveTask();
                    System.exit(0);
            }


            }
    }

    private Paint textPaint = new Paint();
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        // Clear the screen and draw the buttons
        graphics2D.clear(Color.parseColor("#D7FFD7"));

        logo.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        StartGameButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        OptionsButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        GameRulesButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        ExitButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        CreditsButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        textPaint.setTextSize(100.0f);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);

    }
}
