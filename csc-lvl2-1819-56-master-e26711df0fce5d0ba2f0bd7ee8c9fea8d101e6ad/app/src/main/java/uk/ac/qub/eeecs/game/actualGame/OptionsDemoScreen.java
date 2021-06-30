package uk.ac.qub.eeecs.game.actualGame;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.audio.Music;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameScreen;

public class OptionsDemoScreen extends GameScreen {
    /*Functionality created by Chris and Visual Design by Nathan
    Used QUB battle as an example
    */

    private Music musicPlayedOnThisScreen;
    private PushButton mArrowup, mArrowdown, mBackButton, mToggleMusicButton, mAnimation,mAnimation2;
    private String mWelcome, mVolume, mMute;
    private Paint title = new Paint();
    private Paint SubHeading = new Paint();

    public OptionsDemoScreen(Game game) {
        super("OptionsDemoScreen", game);
        loadAssets();

    }

    private void loadAssets() {
        AssetManager assetManager = mGame.getAssetManager();

        assetManager.loadAndAddFont(
                "AudiowideFont", "font/Audiowide.ttf");

        assetManager.loadAssets(
                "txt/assets/AssetDemoScreenAssets.JSON");

        assetManager.loadAndAddMusic("RiskThemeSong", "sound/RiskThemeSong.mp3");

        musicPlayedOnThisScreen = mGame.getAssetManager().getMusic("RiskThemeSong");
        //Button Spacing which reduces magic numbers
        int spacingX = (int) mDefaultLayerViewport.getWidth() / 5;
        int spacingY = (int) mDefaultLayerViewport.getHeight() / 5;

        //Push buttons created to perform tasks
        mBackButton = new PushButton(
                spacingX * 4f, spacingY * 1f, spacingX, spacingY,
                "BackButton", "BackButtonSelected", this);
        mBackButton.setPlaySounds(true, true);

        mArrowup = new PushButton(
                spacingX * 0.5f, spacingY * 2.5f, spacingX, spacingY,
                "VolumeUp", "VolumeUpSelected", this);
        mArrowup.setPlaySounds(true, true);

        mArrowdown = new PushButton(
                spacingX * 1.5f, spacingY * 2.5f, spacingX, spacingY,
                "VolumeDown", "VolumeDownSelected", this);
        mArrowdown.setPlaySounds(true, true);

        mToggleMusicButton = new PushButton(
                spacingX * 0.85f, spacingY * 0.8f, spacingX, spacingY,
                "volume", "mute", this);
        mToggleMusicButton.setPlaySounds(true, true);

        mAnimation = new PushButton(
                spacingX * 3.4f, spacingY * 2.5f, spacingX, spacingY,
                "An", "AnSelected", this);
        mAnimation.setPlaySounds(true, true);
        mAnimation2 = new PushButton(
                spacingX * 4.5f, spacingY * 2.5f, spacingX, spacingY,
                "An", "AnSelected", this);
        mAnimation2.setPlaySounds(true, true);

        //Used to change the images when selected
        if (uk.ac.qub.eeecs.game.AudioManager.isMusicEnabled()) {
            mToggleMusicButton.setBitmap(mGame.getAssetManager().getBitmap("volume"));
        } else {
            mToggleMusicButton.setBitmap(mGame.getAssetManager().getBitmap("mute"));
        }
        //Created Strings for text to be displayed
        mWelcome = "Options Menu";
        mVolume = "Volume Control";
        mMute = "Mute Sounds";

    }


    //Used for testing purposes and makes the code extensible
    public PushButton getMuteUnmuteButton() { return mToggleMusicButton; }
    public void setMuteUnmuteButton(PushButton button) { this.mToggleMusicButton = button; }
    public PushButton getAnimationButton() { return mAnimation; }
    public void setAnimationButton(PushButton button) { this.mAnimation = button; }
    public PushButton getUpButton() { return mArrowup; }
    public void setUpButton(PushButton button) { this.mArrowup = button; }
    public PushButton getDownButton() { return mArrowdown; }
    public void setDownButton(PushButton button) { this.mArrowdown = button; }
    public PushButton getBackButton() { return mBackButton; }
    public void setBackButton(PushButton button) { this.mBackButton = button; }

    @Override
    public void update(ElapsedTime elapsedTime) {
        // process any touch events occurring since the last update
        Input input1 = mGame.getInput();
        List<TouchEvent> touchEvents = input1.getTouchEvents();

        if (touchEvents.size() > 0) {
            updateButtons(elapsedTime);
            performActionsForPressedButtons();
        }
    }

    private void updateButtons(ElapsedTime elapsedTime) {

        //Updating all buttons
        mToggleMusicButton.update(elapsedTime);
        mArrowup.update(elapsedTime);
        mArrowdown.update(elapsedTime);
        mAnimation.update(elapsedTime);
        mAnimation2.update(elapsedTime);
        //Brings user back to game instead of main menu if using to options button on board game demo
        mBackButton.update(elapsedTime);
        if (mBackButton.isPushTriggered()) {
            mGame.getScreenManager().removeScreen(this);
        }
    }

     //Check whether any buttons are pressed and perform the relevant actions for pressed buttons.
    private void performActionsForPressedButtons() {

        if (mArrowup.isPushTriggered()) {
            uk.ac.qub.eeecs.game.AudioManager.stopMusic(musicPlayedOnThisScreen);
            uk.ac.qub.eeecs.game.AudioManager.playMusic(musicPlayedOnThisScreen);
            uk.ac.qub.eeecs.game.AudioManager.setMusicVolume(uk.ac.qub.eeecs.game.AudioManager.getMusicVolume() + 0.2f);
        }

        if (mArrowdown.isPushTriggered()) {
            uk.ac.qub.eeecs.game.AudioManager.stopMusic(musicPlayedOnThisScreen);
            uk.ac.qub.eeecs.game.AudioManager.playMusic(musicPlayedOnThisScreen);
            uk.ac.qub.eeecs.game.AudioManager.setMusicVolume(uk.ac.qub.eeecs.game.AudioManager.getMusicVolume() - 0.2f);
        }

        if (mToggleMusicButton.isPushTriggered()) {
        uk.ac.qub.eeecs.game.AudioManager.setMusicEnabled(!uk.ac.qub.eeecs.game.AudioManager.isMusicEnabled());
        if (uk.ac.qub.eeecs.game.AudioManager.isMusicEnabled()) {
            mToggleMusicButton.setBitmap(mGame.getAssetManager().getBitmap("volume"));
        } else {
            mToggleMusicButton.setBitmap(mGame.getAssetManager().getBitmap("mute"));
        }
        uk.ac.qub.eeecs.game.AudioManager.stopMusic(musicPlayedOnThisScreen);
        uk.ac.qub.eeecs.game.AudioManager.playMusic(musicPlayedOnThisScreen);
    }
}

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        //Adds background colour and sets viewport
        graphics2D.clear(Color.parseColor("#D7FFD7"));
        graphics2D.clipRect(mDefaultScreenViewport.toRect());

        drawText(graphics2D);
        drawButtons(elapsedTime, graphics2D);
    }

    private void drawText(IGraphics2D graphics2D) {
        int height = graphics2D.getSurfaceHeight();

        //Creating the text size and loading in the font
        Paint textPaint = new Paint();
        textPaint.setTypeface(mGame.getAssetManager().getFont("AudiowideFont"));
        textPaint.setTextSize(height / 20);
        textPaint.setTextAlign(Paint.Align.CENTER);

        title.setTextSize(100.0f);
        title.setTextAlign(Paint.Align.LEFT);
        title.setTypeface(Typeface.DEFAULT_BOLD);

        SubHeading.setTextSize(100.0f);
        SubHeading.setTextAlign(Paint.Align.LEFT);
        SubHeading.setTypeface(Typeface.DEFAULT_BOLD);


        graphics2D.drawText(mWelcome, 600.0f, 100.0f, title);
        graphics2D.drawText(mVolume, 150.0f, 300.0f, SubHeading);
        graphics2D.drawText(mMute,150.0f,750f,SubHeading);

    }

    private void drawButtons(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        mBackButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        mArrowup.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        mArrowdown.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        mToggleMusicButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
    }

    //Used for testing the transitions between pages
    public void changeToScreen(GameScreen screen) {
        mGame.getScreenManager().removeScreen(this.getName());
        mGame.getScreenManager().dispose();
        mGame.getScreenManager().addScreen(screen);

    }
}
