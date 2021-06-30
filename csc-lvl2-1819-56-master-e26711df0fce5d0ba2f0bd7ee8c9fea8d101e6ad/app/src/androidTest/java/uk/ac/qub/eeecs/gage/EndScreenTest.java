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
import uk.ac.qub.eeecs.game.actualGame.DemoGame;
import uk.ac.qub.eeecs.game.actualGame.EndScreen;
import uk.ac.qub.eeecs.game.actualGame.MainMenu;

import static org.junit.Assert.assertFalse;


@RunWith(AndroidJUnit4.class)
public class EndScreenTest {

    //////////////////////////////////////////////////////////////////////////
    // Board Demo Game Tests
    // /////////////////////////////////////////////////////////////////////////

    private Context context;
    private DemoGame game;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        setupGameManager();
        game = new DemoGame();
        game.mFileIO = new FileIO(context);
    }

    private void setupGameManager() {

        game = new DemoGame();

        game.mFileIO = new FileIO(context);
        AssetManager assetManager = new AssetManager(game);
        game.mAssetManager = assetManager;

        game.mScreenManager = new ScreenManager(game);
    }

    @Test
    public void loadAndAddBitmap_ValidData_TestIsSuccess() {
        AssetManager assetManager = new AssetManager(game);
        String assetName = "win", assetPath = "img/win1.png";
        Assert.assertTrue(assetManager.loadAndAddBitmap(assetName, assetPath));
    }
    @Test
    public void getBitmap_ValidGet_TestIsSuccess() {
        AssetManager assetManager = new AssetManager(game);
        String assetName = "lose", assetPath = "img/lose1.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
        Assert.assertNotNull(assetManager.getBitmap(assetName));
    }
    @Test
    public void loadAndAddBitmap_AlreadyLoadedAsset_TestErrorReport() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Load a bitmap and test it cannot be added a second time
        String assetName = "win", assetPath = "img/win1.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
        assertFalse(assetManager.loadAndAddBitmap(assetName, assetPath));
    }
    @Test
    public void loadAndAddBitmap_ValidData_TestIsSuccess1() {
        AssetManager assetManager = new AssetManager(game);
        String assetName = "Confetti", assetPath = "img/Confetti.png";
        Assert.assertTrue(assetManager.loadAndAddBitmap(assetName, assetPath));
    }




}
