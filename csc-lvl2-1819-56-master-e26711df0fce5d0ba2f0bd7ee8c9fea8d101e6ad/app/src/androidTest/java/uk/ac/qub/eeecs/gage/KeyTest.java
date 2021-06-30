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

public class KeyTest {


        private Context context;
        private DemoGame game;
        private NameScreen nameScreen;

        @Before
        public void setUp()
        {
            context = InstrumentationRegistry.getTargetContext();
            setupGameManager();
            game = setupGameManager();
            game.mScreenManager.addScreen(new MainMenu(game));
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
    public void loadAndAddBitmap_ValidData_TestIsSuccess()
    {
        AssetManager assetManager = new AssetManager(game);
        String assetName = "Key", assetPath = "img/Key.png";
        Assert.assertTrue(assetManager.loadAndAddBitmap(assetName, assetPath));
    }
}
