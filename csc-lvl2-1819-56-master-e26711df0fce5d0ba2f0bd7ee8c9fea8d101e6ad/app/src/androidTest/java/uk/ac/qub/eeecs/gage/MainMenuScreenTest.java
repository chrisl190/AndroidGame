package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;
import uk.ac.qub.eeecs.gage.engine.io.FileIO;
import uk.ac.qub.eeecs.game.actualGame.DemoGame;
import uk.ac.qub.eeecs.game.actualGame.MainMenu;
import uk.ac.qub.eeecs.game.actualGame.NameScreen;
import uk.ac.qub.eeecs.game.actualGame.OptionsDemoScreen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class MainMenuScreenTest {
    private Context context;
    private DemoGame game;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        setupGameManager();
        game = setupGameManager();
        game.mFileIO = new FileIO(context);
    }

    private DemoGame setupGameManager() {

        game = new DemoGame();

        game.mFileIO = new FileIO(context);
        AssetManager assetManager = new AssetManager(game);
        game.mAssetManager = assetManager;
        game.mAudioManager = new AudioManager(game);

        game.mScreenManager = new ScreenManager(game);
        return game;
    }

    @Test
    public void loadAndAddBitmap_ValidData_TestIsSuccess() {
        AssetManager assetManager = new AssetManager(game);
        String assetName = "StartGame", assetPath = "img/StartGame.png";
        Assert.assertTrue(assetManager.loadAndAddBitmap(assetName, assetPath));
    }
    @Test
    public void loadAndAddBitmap_ValidData_TestIsSuccess1() {
        AssetManager assetManager = new AssetManager(game);
        String assetName = "StartGame", assetPath = "img/StartGame.png";
        Assert.assertTrue(assetManager.loadAndAddBitmap(assetName, assetPath));
    }
    @Test
    public void getBitmap_ValidGet_TestIsSuccess() {
        AssetManager assetManager = new AssetManager(game);
        String assetName = "Options", assetPath = "img/Options.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
        Assert.assertNotNull(assetManager.getBitmap(assetName));
    }

    @Test
    public void loadAndAddBitmap_AlreadyLoadedAsset_TestErrorReport() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Load a bitmap and test it cannot be added a second time
        String assetName = "StartGame", assetPath = "img/StartGame.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
        assertFalse(assetManager.loadAndAddBitmap(assetName, assetPath));
    }


    @Test
    public void unit_CreateScreen_TestIsSuccess() {

        MainMenu mainMenu = new MainMenu(game);

        Assert.assertNotNull(mainMenu);
    }

}
