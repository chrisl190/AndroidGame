package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.io.FileIO;
import uk.ac.qub.eeecs.game.actualGame.BoardDemoGame;
import uk.ac.qub.eeecs.game.actualGame.DemoGame;
import uk.ac.qub.eeecs.game.actualGame.Unit;

import static org.junit.Assert.assertFalse;

public class UnitsTest {
    private Context context;
    private DemoGame game;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        game = setupGameManager();
        game.mFileIO = new FileIO(context);
    }

    private DemoGame setupGameManager() {

        game = new DemoGame();

        game.mFileIO = new FileIO(context);
        game.mAssetManager = new AssetManager(game);

        game.mScreenManager = new ScreenManager(game);
        return game;
    }

    //Patrick
    @Test
    public void loadAndAddUnitBitmap_ValidData_TestIsSuccess() {
        AssetManager assetManager = new AssetManager(game);
        String assetName = "Unit", assetPath = "img/Unit.png";
        Assert.assertTrue(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    //Patrick
    @Test
    public void loadAndAddUnitBitmap_AlreadyLoadedAsset_TestErrorReport() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Load a bitmap and test it cannot be added a second time
        String assetName = "Unit", assetPath = "img/Unit.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
        assertFalse(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    //Patrick
    @Test
    public void getUnitBitmap_ValidGet_TestIsSuccess() {
        AssetManager assetManager = new AssetManager(game);
        String assetName = "Unit", assetPath = "img/Unit.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
        Assert.assertNotNull(assetManager.getBitmap(assetName));
    }

    //Patrick
    @Test
    public void unit_ValidCreation_TestIsSuccess() {

        BoardDemoGame boardDemoGameScreen = new BoardDemoGame(game);
        Unit unit = new Unit(0, 0, boardDemoGameScreen, false);

        Assert.assertNotNull(unit);
    }
}
