package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;
import uk.ac.qub.eeecs.gage.engine.io.FileIO;
import uk.ac.qub.eeecs.gage.world.Sprite;
import uk.ac.qub.eeecs.game.actualGame.DemoGame;
import uk.ac.qub.eeecs.game.actualGame.MainMenu;
import uk.ac.qub.eeecs.game.actualGame.CreditScreen;

import static org.junit.Assert.assertEquals;

//Created by Chris 100%

@RunWith(AndroidJUnit4.class)
public class CreditScreenTests {

    private Context myContext;
    Game game;
    private CreditScreen creditsDemoScreen;
   

    @Before
    public void setup() {
        myContext = InstrumentationRegistry.getTargetContext();
        game = setupGameManager();
        game.mFileIO = new FileIO(myContext);
        creditsDemoScreen = new CreditScreen(game);
        game.mScreenManager.addScreen(creditsDemoScreen);
    }

    private Game setupGameManager()
    {
        game = new DemoGame();

        game.mFileIO = new FileIO(myContext);
        game.mAssetManager = new AssetManager(game);
        game.mAudioManager = new AudioManager(game);
        game.mScreenManager = new ScreenManager(game);

        return game;
    }

    @Test
    public void testSprite(){
        AssetManager assetManager = new AssetManager(game);
        assetManager.loadAssets("txt/assets/CreditScreen.JSON");
        CreditScreen creditsDemoScreen = new CreditScreen(game);
        Sprite mRules1Test = new Sprite(creditsDemoScreen.getGame().getScreenWidth()*0.5f, creditsDemoScreen.getGame().getScreenHeight()*0.90f, assetManager.getBitmap("Credits1"), creditsDemoScreen);
        Assert.assertNotNull(mRules1Test);
    }

    //Chris
    @Test
    public void testCreditsScreenConstructor(){
        creditsDemoScreen = new CreditScreen(game);
        junit.framework.Assert.assertEquals(creditsDemoScreen.getName(), "CreditScreen");
    }
    //Chris
    @Test
    public void testBackButton() {
        creditsDemoScreen = new CreditScreen(game);
        junit.framework.Assert.assertNotNull(creditsDemoScreen.getBackButton());
    }

    //Chris
    @Test
    public void transitionToMenu(){
        MainMenu menuScreen = new MainMenu(game);
        CreditScreen demoScreen = new CreditScreen(game);
        demoScreen.changeToScreen(menuScreen);
        assertEquals(menuScreen.getName(), game.getScreenManager().getCurrentScreen().getName());
    }

}
