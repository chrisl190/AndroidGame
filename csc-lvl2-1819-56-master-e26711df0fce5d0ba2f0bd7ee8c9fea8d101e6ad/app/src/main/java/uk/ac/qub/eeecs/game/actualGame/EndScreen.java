package uk.ac.qub.eeecs.game.actualGame;
import android.graphics.Color;

import java.util.List;

import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.Sprite;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;


public class EndScreen extends GameScreen {
    //Background for screen
    private GameObject WinBackground;
    private GameObject LoseBackground;
    private LayerViewport EndScreenLayerViewPort;
    private PushButton HomeButton;
    private String pName;
    private Sprite confetti,confetti2,confetti3,confetti4,confetti5,confetti6,confetti7;
    private Sprite Sad,Sad2,Sad3,Sad4,Sad5,Sad6,Sad7;
    private int count = 0;



    private boolean playerWonTheGame;

    public EndScreen(Game game, boolean playerWon, String playerName) {
        super("EndScreen", game);
        mGame.getAssetManager().loadAssets("txt/assets/EndScreen.JSON");
        pName = playerName;



        AssetManager assetManager = mGame.getAssetManager();

        assetManager.loadAndAddBitmap("lose", "img/lose1.png");
        assetManager.loadAndAddBitmap("win", "img/win1.png");
        assetManager.loadAndAddBitmap("Home", "img/Home.png");
        assetManager.loadAndAddBitmap("HomeSelected", "img/HomeSelected.png");
        assetManager.loadAndAddBitmap("confetti", "img/confetti.png");
        assetManager.loadAndAddBitmap("Sad", "img/Sad.png");

        confetti = new Sprite(400f,200f,assetManager.getBitmap("confetti"), this );
        confetti3 = new Sprite(1500f,500f,assetManager.getBitmap("confetti"), this );
        confetti2= new Sprite(1000f,200f,assetManager.getBitmap("confetti"), this );
        confetti4 = new Sprite(700f,200f,assetManager.getBitmap("confetti"), this );
        confetti5 = new Sprite(400f,800f,assetManager.getBitmap("confetti"), this );
        confetti6 = new Sprite(1500f,200f,assetManager.getBitmap("confetti"), this );
        confetti7 = new Sprite(800f,900f,assetManager.getBitmap("confetti"), this );

        Sad = new Sprite(400f,180f,assetManager.getBitmap("Sad"), this );
        Sad3 = new Sprite(1500f,500f,assetManager.getBitmap("Sad"), this );
        Sad2= new Sprite(1000f,180f,assetManager.getBitmap("Sad"), this );
        Sad4 = new Sprite(700f,180f,assetManager.getBitmap("Sad"), this );
        Sad5 = new Sprite(400f,780f,assetManager.getBitmap("Sad"), this );
        Sad6 = new Sprite(1500f,180f,assetManager.getBitmap("Sad"), this );
        Sad7 = new Sprite(800f,900f,assetManager.getBitmap("Sad"), this );
        int spacingX = (int)mDefaultLayerViewport.getWidth() / 6;
        int spacingY = (int)mDefaultLayerViewport.getHeight() /8;

        HomeButton = new PushButton(
                spacingX * 1.1f, spacingY * 1f, spacingX, spacingY,
                "Home", "HomeSelected", this);
        HomeButton.setPlaySounds(true, true);


        setupViewports();

        playerWonTheGame = playerWon;


        WinBackground = new GameObject(mGame.getScreenWidth() /8f, mGame.getScreenHeight() / 8f,
                getGame().getAssetManager().getBitmap("win"), this);
        LoseBackground = new GameObject(mGame.getScreenWidth() / 8f, mGame.getScreenHeight() / 8f,
                getGame().getAssetManager().getBitmap("lose"), this);
    }
    private void setupViewports() {
        // Setup the screen viewport to use the full screen.
        mDefaultScreenViewport.set( 0, 0, mGame.getScreenWidth(), mGame.getScreenHeight());

        // Calculate the layer height that will preserved the screen aspect ratio
        // given an assume 480 layer width.
        float layerHeight = mGame.getScreenHeight() * (480.0f / mGame.getScreenWidth());

        mDefaultLayerViewport.set(240.0f, layerHeight/2.0f, 240.0f, layerHeight/2.0f);
        EndScreenLayerViewPort = new LayerViewport(240.0f, layerHeight/2.0f, 240.0f, layerHeight/2.0f);
    }

    public void update(ElapsedTime elapsedTime) {
        count += 1;
        Input input = mGame.getInput();

        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {
           HomeButton.update(elapsedTime);
            if (HomeButton.isPushTriggered())
                mGame.getScreenManager().addScreen(new MainMenu(mGame));
        }
    }
    private Paint textPaint = new Paint();
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        // Create the screen to black and define a clip based on the viewport
        graphics2D.clear(Color.parseColor("#BEF3D7"));
        graphics2D.clipRect(mDefaultScreenViewport.toRect());


        // Draw the background first of all
        textPaint.setTextSize(100.0f);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);


        if (playerWonTheGame) {
            WinBackground.draw(elapsedTime, graphics2D, EndScreenLayerViewPort, mDefaultScreenViewport);
            graphics2D.drawText("Well Done "+pName.toUpperCase()+"!" ,500f, 550f, textPaint);
            graphics2D.drawText("You conquered the Tech world!" ,280f, 650f, textPaint);
            if(count > 0 && count <15)
            {
                confetti.draw(elapsedTime, graphics2D);
                confetti5.draw(elapsedTime, graphics2D);
                confetti2.draw(elapsedTime, graphics2D);
            }
            if(count > 15 && count <30)
            {
                confetti3.draw(elapsedTime, graphics2D);
                confetti4.draw(elapsedTime, graphics2D);
            }
            if(count > 30 && count < 45)
            {
                confetti5.draw(elapsedTime, graphics2D);
                confetti.draw(elapsedTime, graphics2D);
                confetti6.draw(elapsedTime, graphics2D);

            }
            if(count > 45 && count < 60)
            {
                confetti4.draw(elapsedTime, graphics2D);
                confetti3.draw(elapsedTime, graphics2D);
                confetti2.draw(elapsedTime, graphics2D);
            }
            if(count > 60 && count < 75)
            {
                confetti5.draw(elapsedTime, graphics2D);
                confetti.draw(elapsedTime, graphics2D);
            }
            if(count > 75 && count < 90)
            {
                confetti6.draw(elapsedTime, graphics2D);
                confetti4.draw(elapsedTime, graphics2D);
                confetti2.draw(elapsedTime, graphics2D);
                confetti6.draw(elapsedTime, graphics2D);
            }
            if(count > 90)
            {
                confetti7.draw(elapsedTime, graphics2D);
                confetti.draw(elapsedTime, graphics2D);
                confetti3.draw(elapsedTime, graphics2D);
            }






        } else {
            LoseBackground.draw(elapsedTime, graphics2D, EndScreenLayerViewPort, mDefaultScreenViewport);
            graphics2D.drawText("Sorry "+pName.toUpperCase()+"!" ,700f, 550f, textPaint);
            graphics2D.drawText("Please try again!" ,650f, 650f, textPaint);
            if(count > 0 && count <15)
            {
                Sad.draw(elapsedTime, graphics2D);
                Sad5.draw(elapsedTime, graphics2D);
                Sad2.draw(elapsedTime, graphics2D);
            }
            if(count > 15 && count <30)
            {
                Sad3.draw(elapsedTime, graphics2D);
                Sad4.draw(elapsedTime, graphics2D);
            }
            if(count > 30 && count < 45)
            {
                Sad5.draw(elapsedTime, graphics2D);
                Sad.draw(elapsedTime, graphics2D);
                Sad6.draw(elapsedTime, graphics2D);

            }
            if(count > 45 && count < 60)
            {
                Sad4.draw(elapsedTime, graphics2D);
                Sad3.draw(elapsedTime, graphics2D);
                Sad2.draw(elapsedTime, graphics2D);
            }
            if(count > 60 && count < 75)
            {
                Sad5.draw(elapsedTime, graphics2D);
                Sad.draw(elapsedTime, graphics2D);
            }
            if(count > 75 && count < 90)
            {
                Sad6.draw(elapsedTime, graphics2D);
                Sad4.draw(elapsedTime, graphics2D);
                Sad2.draw(elapsedTime, graphics2D);
                Sad6.draw(elapsedTime, graphics2D);
            }
            if(count > 90)
            {
                Sad7.draw(elapsedTime, graphics2D);
                Sad.draw(elapsedTime, graphics2D);
                Sad3.draw(elapsedTime, graphics2D);
            }


        }

        HomeButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

    }
}
