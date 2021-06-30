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
import uk.ac.qub.eeecs.game.actualGame.DemoGame;
import uk.ac.qub.eeecs.game.actualGame.MainMenu;
import uk.ac.qub.eeecs.game.actualGame.OptionsDemoScreen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;


@RunWith(AndroidJUnit4.class)
public class OptionsDemoScreenTests {

//Created by Chris

    private Context context;
    private DemoGame game;
    private OptionsDemoScreen optionsDemoScreen;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        game = setupGameManager();
        game.mFileIO = new FileIO(context);
        game.mScreenManager.addScreen(new MainMenu(game));
        optionsDemoScreen = new OptionsDemoScreen(game);
        game.mScreenManager.addScreen(optionsDemoScreen);
    }

    private DemoGame setupGameManager() {

        game = new DemoGame();

        game.mFileIO = new FileIO(context);
        game.mAssetManager = new AssetManager(game);
        game.mAudioManager = new AudioManager(game);
        game.mScreenManager = new ScreenManager(game);
        return game;
    }
    //Chris
    @Test
    public void testOptionsDemoScreenConstructor() {
        optionsDemoScreen = new OptionsDemoScreen(game);
        junit.framework.Assert.assertEquals(optionsDemoScreen.getName(), "OptionsDemoScreen");
    }

    //Nathan
    @Test
    public void loadAndAddBitmap() {
        AssetManager assetManager = new AssetManager(game);
        String assetName = "VolumeUpSelected", assetPath = "img/ VolumeUpSelected.png";
        Assert.assertTrue(assetManager.loadAndAddBitmap(assetName, assetPath));

    }
    //Chris
    @Test
    public void getBitmap_ValidGet() {
        AssetManager assetManager = new AssetManager(game);
        String assetName = "DownArrow", assetPath = "img/DownArrow.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
        Assert.assertNotNull(assetManager.getBitmap(assetName));
    }
    //Chris
    @Test
    public void loadAndAddBitmapAlreadyLoaded() {
        AssetManager assetManager = new AssetManager(game);
        String assetName = "UpArrow", assetPath = "img/UpArrow.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
        assertFalse(assetManager.loadAndAddBitmap(assetName, assetPath));
    }
    //Chris
    @Test
    public void mToggleMusicButtonTest() {

        OptionsDemoScreen optionsDemoScreen = new OptionsDemoScreen(game);
        game.mScreenManager.addScreen(optionsDemoScreen);

        assertNotNull(game.mAssetManager.getBitmap("volume"));
        assertNotNull(game.mAssetManager.getBitmap("mute"));

    }
    //Chris
    @Test
    public void transitionToMenuScreen() {
        MainMenu menuScreen = new MainMenu(game);
        OptionsDemoScreen demoScreen = new OptionsDemoScreen(game);
        demoScreen.changeToScreen(menuScreen);
        assertEquals(menuScreen.getName(), game.getScreenManager().getCurrentScreen().getName());
    }
    //Chris
    @Test
    public void testMuteUnmuteButton() {
        optionsDemoScreen = new OptionsDemoScreen(game);
        junit.framework.Assert.assertNotNull(optionsDemoScreen.getMuteUnmuteButton());
    }
    //Chris
    @Test
    public void testVolumeUpButton() {
        optionsDemoScreen = new OptionsDemoScreen(game);
        junit.framework.Assert.assertNotNull(optionsDemoScreen.getUpButton());
    }
    //Chris
    @Test
    public void testVolumeDownButton() {
        optionsDemoScreen = new OptionsDemoScreen(game);
        junit.framework.Assert.assertNotNull(optionsDemoScreen.getDownButton());
    }
    //Chris
    @Test
    public void testAnimationButton() {
        optionsDemoScreen = new OptionsDemoScreen(game);
        junit.framework.Assert.assertNotNull(optionsDemoScreen.getAnimationButton());
    }
    //Chris
    @Test
    public void testBackButton() {
        optionsDemoScreen = new OptionsDemoScreen(game);
        junit.framework.Assert.assertNotNull(optionsDemoScreen.getBackButton());
    }

}
