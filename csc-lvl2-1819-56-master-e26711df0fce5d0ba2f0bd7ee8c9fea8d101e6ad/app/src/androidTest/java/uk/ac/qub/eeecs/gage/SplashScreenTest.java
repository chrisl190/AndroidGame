package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;
import uk.ac.qub.eeecs.gage.engine.io.FileIO;
import uk.ac.qub.eeecs.gage.world.Sprite;
import uk.ac.qub.eeecs.game.actualGame.DemoGame;
import uk.ac.qub.eeecs.game.actualGame.MainMenu;
import uk.ac.qub.eeecs.game.actualGame.SplashScreen;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

//Created 100% by Chris

@RunWith(AndroidJUnit4.class)
public class SplashScreenTest {

    private Context appContext;
    Game game;


    @Before
    public void setup() {
        appContext = InstrumentationRegistry.getTargetContext();
        game = setupGameManager();
        game.mFileIO = new FileIO(appContext);

    }

    private Game setupGameManager()
    {

        game = new DemoGame();

        game.mFileIO = new FileIO(appContext);
        game.mAssetManager = new AssetManager(game);
        game.mAudioManager = new AudioManager(game);
        game.mScreenManager = new ScreenManager(game);

        return game;
    }

 @Test
    public void testCorrectScreenTransition() {
        SplashScreen splashScreen = new SplashScreen("String",game);
        game.getScreenManager().addScreen(splashScreen);
        MainMenu menuScreen = new MainMenu(game);
        game.getScreenManager().addScreen(menuScreen);
        splashScreen.changeToScreen(menuScreen);
        assertEquals(game.getScreenManager().getCurrentScreen().getName(),menuScreen.getName());
    }

    @Test
    public void TestSharedPreferencesMusicPass(){
        SharedPreferences getPreference = PreferenceManager.getDefaultSharedPreferences(appContext);
        SharedPreferences.Editor prefEditor = getPreference.edit();

        prefEditor.putBoolean("RiskThemeSong", true);
        prefEditor.commit();
        assertTrue(getPreference.getBoolean("RiskThemeSong", true));
    }

    @Test
    public void constructor_SplashScreenIsntNull(){
        SplashScreenTest mockSplashScreen = new SplashScreenTest();
        assertNotNull(mockSplashScreen);
    }

    @Test
    public void testSprite(){
        AssetManager assetManager = new AssetManager(game);
        assetManager.loadAssets("txt/assets/SplashScreen.JSON");
        SplashScreen splashDemoScreen = new SplashScreen("Splash Screen" , game);
        Sprite testLoadingBar = new Sprite(splashDemoScreen.getGame().getScreenWidth()*0.5f, splashDemoScreen.getGame().getScreenHeight()*0.90f, assetManager.getBitmap("Loading1"), splashDemoScreen);
        Assert.assertNotNull(testLoadingBar);
    }

    @Test
    public void testLoading2(){
        AssetManager assetManager = new AssetManager(game);
        assetManager.loadAssets("txt/assets/SplashScreen.JSON");
        SplashScreen splashDemoScreen = new SplashScreen("Splash Screen" , game);
        Sprite testLoadingBar = new Sprite(splashDemoScreen.getGame().getScreenWidth()*0.5f, splashDemoScreen.getGame().getScreenHeight()*0.90f, assetManager.getBitmap("Loading2"), splashDemoScreen);
        Assert.assertNotNull(testLoadingBar);
    }

    @Test
    public void testLoading3(){
        AssetManager assetManager = new AssetManager(game);
        assetManager.loadAssets("txt/assets/SplashScreen.JSON");
        SplashScreen splashDemoScreen = new SplashScreen("Splash Screen" , game);
        Sprite testLoadingBar = new Sprite(splashDemoScreen.getGame().getScreenWidth()*0.5f, splashDemoScreen.getGame().getScreenHeight()*0.90f, assetManager.getBitmap("Loading3"), splashDemoScreen);
        Assert.assertNotNull(testLoadingBar);
    }

    @Test
    public void testLoading4(){
        AssetManager assetManager = new AssetManager(game);
        assetManager.loadAssets("txt/assets/SplashScreen.JSON");
        SplashScreen splashDemoScreen = new SplashScreen("Splash Screen" , game);
        Sprite testLoadingBar = new Sprite(splashDemoScreen.getGame().getScreenWidth()*0.5f, splashDemoScreen.getGame().getScreenHeight()*0.90f, assetManager.getBitmap("Loading4"), splashDemoScreen);
        Assert.assertNotNull(testLoadingBar);
    }

    @Test
    public void testLoading5(){
        AssetManager assetManager = new AssetManager(game);
        assetManager.loadAssets("txt/assets/SplashScreen.JSON");
        SplashScreen splashDemoScreen = new SplashScreen("Splash Screen" , game);
        Sprite testLoadingBar = new Sprite(splashDemoScreen.getGame().getScreenWidth()*0.5f, splashDemoScreen.getGame().getScreenHeight()*0.90f, assetManager.getBitmap("Loading5"), splashDemoScreen);
        Assert.assertNotNull(testLoadingBar);
    }

}