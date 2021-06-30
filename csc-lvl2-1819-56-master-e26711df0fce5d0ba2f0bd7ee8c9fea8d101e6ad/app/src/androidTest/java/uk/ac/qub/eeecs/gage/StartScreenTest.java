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
import uk.ac.qub.eeecs.gage.engine.io.FileIO;
import uk.ac.qub.eeecs.game.AudioManager;
import uk.ac.qub.eeecs.game.actualGame.DemoGame;
import uk.ac.qub.eeecs.game.actualGame.MainMenu;
import uk.ac.qub.eeecs.game.actualGame.NameScreen;
import uk.ac.qub.eeecs.game.actualGame.OptionsDemoScreen;
import uk.ac.qub.eeecs.game.actualGame.StartScreen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class StartScreenTest {
    //Anna-Lise Keenan - 40204451

    //////////////////////////////////////////////////////////////////////////
    // Board Demo Game Tests
    // /////////////////////////////////////////////////////////////////////////

    private Context context;
    private DemoGame game;
    private StartScreen startScreen;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        game = setupGameManager();
        game.mFileIO = new FileIO(context);
        game.mScreenManager.addScreen(new OptionsDemoScreen(game));
        game.mScreenManager.addScreen(startScreen);
    }

    private DemoGame setupGameManager() {

        game = new DemoGame();

        game.mFileIO = new FileIO(context);
        AssetManager assetManager = new AssetManager(game);
        game.mAssetManager = assetManager;

        game.mAudioManager = new uk.ac.qub.eeecs.gage.engine.audio.AudioManager(game);

        game.mScreenManager = new ScreenManager(game);

        return game;
    }
    //Test to ensure that the images are being loaded - example using the Begin Button and Back Button images
    @Test
    public void loadAndAddBitmap_ValidData_TestIsSuccess() {
        AssetManager assetManager = new AssetManager(game);
        String assetName = "Territory", assetPath = "img/T1.png";
        Assert.assertTrue(assetManager.loadAndAddBitmap(assetName, assetPath));

        AssetManager assetManager1 = new AssetManager(game);
        String assetName1 = "Territory", assetPath1 = "img/T1S.png";
        Assert.assertTrue(assetManager.loadAndAddBitmap(assetName, assetPath));

    }
    @Test
    public void getBitmap_ValidGet_TestIsSuccess() {
        AssetManager assetManager = new AssetManager(game);
        String assetName = "Back", assetPath = "img/BackButton.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
        Assert.assertNotNull(assetManager.getBitmap(assetName));
    }
    @Test
    public void loadAndAddBitmap_AlreadyLoadedAsset_TestErrorReport()
    {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Load a bitmap and test it cannot be added a second time
        String assetName = "Begin";
        String assetPath = "img/BEGIN.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
        assertFalse(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

}
