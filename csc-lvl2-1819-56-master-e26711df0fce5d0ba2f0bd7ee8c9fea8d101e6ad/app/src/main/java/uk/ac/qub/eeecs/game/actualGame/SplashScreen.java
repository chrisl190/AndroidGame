package uk.ac.qub.eeecs.game.actualGame;

import android.graphics.Rect;
import java.util.List;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.Sprite;
/*
    Created by Chris
    Used token as an example
 */

public class SplashScreen extends GameScreen {

    private Rect background = new Rect();

    private AssetManager assetManager = mGame.getAssetManager();

    //Used as a timer and set to zero
    private int count = 0;

    private Sprite loadingBar;

    public SplashScreen(String name, Game game)
    {
        super(name, game);
        loadAssets();
    }

    private void loadAssets()
    {
        assetManager.loadAssets(
                "txt/assets/SplashScreen.JSON");
        loadingBar = new Sprite(this.getGame().getScreenWidth()*0.5f, this.getGame().getScreenHeight()*0.90f, assetManager.getBitmap("Loading1"), this );
        assetManager.loadAndAddBitmap("SplashScreenBackground", "SplashScreenBackground.png");
        assetManager.loadAndAddBitmap("Loading1", "Loading1.png");
        assetManager.loadAndAddBitmap("Loading2", "Loading2.png");
        assetManager.loadAndAddBitmap("Loading3", "Loading3.png");
        assetManager.loadAndAddBitmap("Loading4", "Loading4.png");
        assetManager.loadAndAddBitmap("Loading5", "Loading5.png");
    }
    //Used for testing the transitions between pages
    public void changeToScreen(GameScreen screen) {
        mGame.getScreenManager().removeScreen(this.getName());
        mGame.getScreenManager().addScreen(screen);
    }

    @Override
    public void update(ElapsedTime elapsedTime) {

        Input input = mGame.getInput();
        List<TouchEvent> touchEvents = input.getTouchEvents();

        //Changes to main menu if the splash screen is selected before its loaded
        if (!touchEvents.isEmpty()) {
            changeToScreen(new MainMenu(mGame));
        }

        count += 1;
        //This means the splash screen moves to the main menu after 5 seconds
        if (count == 100)
        {
            changeToScreen(new MainMenu(mGame));
        }

    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D)
    {
        //Increases the usability of the code meaning the logo can be easily moved on the splash screen
        background.top = 0;
        background.left = 0;
        background.bottom = mGame.getScreenHeight();
        background.right = mGame.getScreenWidth();
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("SplashScreenBackground"),null,background, null);

        loadingBar.draw(elapsedTime, graphics2D);

        //Used to created animation in the loading bar using the count
       if(count > 20 && count <40)
        {
            loadingBar.setBitmap(mGame.getAssetManager().getBitmap("Loading2"));
        }
        if(count > 40 && count < 60)
        {
            loadingBar.setBitmap(mGame.getAssetManager().getBitmap("Loading3"));
        }
        if(count > 60 && count < 80)
        {
            loadingBar.setBitmap(mGame.getAssetManager().getBitmap("Loading4"));
        }
        if(count > 80 && count < 100)
        {
            loadingBar.setBitmap(mGame.getAssetManager().getBitmap("Loading5"));
        }
    }
}







