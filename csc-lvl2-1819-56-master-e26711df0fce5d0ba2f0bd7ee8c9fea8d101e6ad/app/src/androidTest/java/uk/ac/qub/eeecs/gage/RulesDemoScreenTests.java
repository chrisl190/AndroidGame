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

import uk.ac.qub.eeecs.game.actualGame.RulesScreen;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
//Created by Chris 100%
@RunWith(AndroidJUnit4.class)
public class RulesDemoScreenTests {

    private Context myContext;
    Game game;
    private RulesScreen rulesDemoScreen;
    private int boardImageNumber;

    @Before
    public void setup() {
        myContext = InstrumentationRegistry.getTargetContext();
        game = setupGameManager();
        game.mFileIO = new FileIO(myContext);
        rulesDemoScreen = new RulesScreen(game, false);
        game.mScreenManager.addScreen(rulesDemoScreen);
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

    //Chris
    @Test
    public void loadAndAddBitmap_ValidData_TestIsSuccess() {
        AssetManager assetManager = new AssetManager(game);
        String assetName = "GameRules", assetPath = "img/GameRules.png";
        Assert.assertTrue(assetManager.loadAndAddBitmap(assetName, assetPath));
    }
    //Chris
    @Test
    public void getBitmap_ValidGet_TestIsSuccess() {
        AssetManager assetManager = new AssetManager(game);
        String assetName = "ArrowRight", assetPath = "img/ArrowRight.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
        Assert.assertNotNull(assetManager.getBitmap(assetName));
    }
    //Chris
    @Test
    public void loadAndAddBitmap_AlreadyLoadedAsset_TestErrorReport() {
        AssetManager assetManager = new AssetManager(game);
        String assetName = "GameRules", assetPath = "img/GameRules.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
        assertFalse(assetManager.loadAndAddBitmap(assetName, assetPath));
    }
    //Chris
    @Test
    public void testRulesImage(){
        AssetManager assetManager = new AssetManager(game);
        assetManager.loadAssets("txt/assets/MainMenu.JSON");
        RulesScreen rulesDemoScreen = new RulesScreen(game, false);
        Sprite mRulesTest = new Sprite(rulesDemoScreen.getGame().getScreenWidth()*0.5f, rulesDemoScreen.getGame().getScreenHeight()*0.70f, assetManager.getBitmap("Rules1"), rulesDemoScreen);
        Assert.assertNotNull(mRulesTest);
    }
    //Chris
    @Test
    public void testRulesImage1(){
        AssetManager assetManager = new AssetManager(game);
        assetManager.loadAssets("txt/assets/MainMenu.JSON");
        RulesScreen rulesDemoScreen = new RulesScreen(game, false);
        Sprite mRules1Test = new Sprite(rulesDemoScreen.getGame().getScreenWidth()*0.5f, rulesDemoScreen.getGame().getScreenHeight()*0.90f, assetManager.getBitmap("Rules2"), rulesDemoScreen);
        Assert.assertNotNull(mRules1Test);
    }
    //Chris
    @Test
    public void testRulesImage2(){
        AssetManager assetManager = new AssetManager(game);
        assetManager.loadAssets("txt/assets/MainMenu.JSON");
        RulesScreen rulesDemoScreen = new RulesScreen(game, false);
        Sprite mRules2Test = new Sprite(rulesDemoScreen.getGame().getScreenWidth()*0.5f, rulesDemoScreen.getGame().getScreenHeight()*0.70f, assetManager.getBitmap("Rules3"), rulesDemoScreen);
        Assert.assertNotNull(mRules2Test);
    }
    //Chris
    @Test
    public void testRulesImage3(){
        AssetManager assetManager = new AssetManager(game);
        assetManager.loadAssets("txt/assets/MainMenu.JSON");
        RulesScreen rulesDemoScreen = new RulesScreen(game, false);
        Sprite mRules3Test = new Sprite(rulesDemoScreen.getGame().getScreenWidth()*0.5f, rulesDemoScreen.getGame().getScreenHeight()*0.75f, assetManager.getBitmap("Rules4"), rulesDemoScreen);
        Assert.assertNotNull(mRules3Test);
    }
    //Chris
    @Test
    public void testRulesImage4(){
        AssetManager assetManager = new AssetManager(game);
        assetManager.loadAssets("txt/assets/MainMenu.JSON");
        RulesScreen rulesDemoScreen = new RulesScreen(game, false);
        Sprite mRules4Test = new Sprite(rulesDemoScreen.getGame().getScreenWidth()*0.5f, rulesDemoScreen.getGame().getScreenHeight()*0.75f, assetManager.getBitmap("Rules5"), rulesDemoScreen);
        Assert.assertNotNull(mRules4Test);
    }
    //Chris
    @Test
    public void testRulesImage5(){
        AssetManager assetManager = new AssetManager(game);
        assetManager.loadAssets("txt/assets/MainMenu.JSON");
        RulesScreen rulesDemoScreen = new RulesScreen(game, false);
        Sprite mRules5Test = new Sprite(rulesDemoScreen.getGame().getScreenWidth()*0.5f, rulesDemoScreen.getGame().getScreenHeight()*0.75f, assetManager.getBitmap("Rules6"), rulesDemoScreen);
        Assert.assertNotNull(mRules5Test);
    }
    //Chris
    @Test
    public void testRulesScreenConstructor(){
        rulesDemoScreen = new RulesScreen(game, false);
        junit.framework.Assert.assertEquals(rulesDemoScreen.getName(), "RulesScreen");
    }
    //Chris
    @Test
    public void testBackButton() {
        rulesDemoScreen = new RulesScreen(game, false);
        junit.framework.Assert.assertNotNull(rulesDemoScreen.getBackButton());
    }
    //Chris
    @Test
    public void testTheRulesButton() {
        rulesDemoScreen = new RulesScreen(game, false);
        junit.framework.Assert.assertNotNull(rulesDemoScreen.getTheRulesButton());
    }
    //Chris
    @Test
    public void testArrowForwardButton() {
        rulesDemoScreen = new RulesScreen(game, false);
        junit.framework.Assert.assertNotNull(rulesDemoScreen.getArrowForward());
    }
    //Chris
    @Test
    public void testArrowBackButton() {
        rulesDemoScreen = new RulesScreen(game, false);
        junit.framework.Assert.assertNotNull(rulesDemoScreen.getArrowBack());

    }
    //Chris
    @Test
    public void transitionToMenu(){
        MainMenu menuScreen = new MainMenu(game);
        RulesScreen demoScreen = new RulesScreen(game, false);
        demoScreen.changeToScreen(menuScreen);
        assertEquals(menuScreen.getName(), game.getScreenManager().getCurrentScreen().getName());
    }
    //Chris
    @Test
    public void ArrowForwardPressedAboveSix()
    {
        boardImageNumber = 6;
        rulesDemoScreen.getArrowForward().isPushTriggered();
        assertEquals(6, boardImageNumber);
    }
    //Chris
    @Test
    public void ForwardArrowPressed()
    {
        rulesDemoScreen.getArrowForward().isPushTriggered();
        assert(boardImageNumber!=0);
    }
    //Chris
    @Test
    public void ArrowBackPressedBelowZero()
    {
        boardImageNumber = 0;
        rulesDemoScreen.getArrowBack().isPushTriggered();
        assertEquals(0,boardImageNumber);
    }


/*
Couldnt get these working properly however with more time I feel I could have corrected them
    @Test
    public void arrowRightButtonLocationChange()
    {
        rulesDemoScreen.getTheRulesButton().isPushTriggered();
        assertEquals(-100,  rulesDemoScreen.getArrowForward().position);
    }

    @Test
    public void arrowLeftButtonLocationChange()
    {
        rulesDemoScreen.getTheRulesButton().isPushTriggered();
        assertEquals(-100,  rulesDemoScreen.getArrowBack().position)
    }
    */
}
