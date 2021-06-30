package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.io.FileIO;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.Sprite;
import uk.ac.qub.eeecs.game.actualGame.Card;
import uk.ac.qub.eeecs.game.actualGame.DemoGame;
import uk.ac.qub.eeecs.game.actualGame.BoardDemoGame;
import uk.ac.qub.eeecs.game.actualGame.Dice;
import uk.ac.qub.eeecs.game.actualGame.Player;
import uk.ac.qub.eeecs.game.actualGame.Territory;
import uk.ac.qub.eeecs.game.actualGame.Unit;

/**
 * Example instrumentation tests, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class BoardDemoGameTests {

    // /////////////////////////////////////////////////////////////////////////
    // Board Demo Game Tests
    // /////////////////////////////////////////////////////////////////////////

    private Context context;
    private DemoGame game;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        setupGameManager();
    }

    private void setupGameManager() {

        game = new DemoGame();

        game.mFileIO = new FileIO(context);
        game.mAssetManager = new AssetManager(game);

        game.mScreenManager = new ScreenManager(game);
    }

    //Robbie
    @Test
    public void gameBoard_AssetsLoaded_TestIsSuccessful(){
        AssetManager assetManager = new AssetManager(game);
        String assetName = "GameBoard", assetPath = "img/GameBoard.png";
        Assert.assertTrue(assetManager.loadAndAddBitmap(assetName, assetPath));
    }



    //Robbie
    @Test
    public void diceRollTest_Between1And6Boundary() {

        BoardDemoGame boardDemoGameScreen = new BoardDemoGame(game);

        Dice testDice = new Dice(0.0f,0.0f,boardDemoGameScreen);

        int rollResult = testDice.generateDiceRoll();

        boolean rollBetween1And6 = (rollResult >= 1 && rollResult <= 6);

        Assert.assertTrue(rollBetween1And6);
    }

    //Robbie
    @Test
    public void territoryTestGettersAndSetters() {

        BoardDemoGame boardDemoGame = new BoardDemoGame(game);

        Territory testTerritory = new Territory(0.0f, 0.0f, 1.0f, 1.0f, boardDemoGame);

        int newNumOfUnits = 50;

        testTerritory.setNumOfSmallUnits(newNumOfUnits);

        boolean test = (testTerritory.getNumOfSmallUnits() == newNumOfUnits);

        Assert.assertTrue(test);
    }

    //Robbie
    @Test
    public void territoryTestMovingUnits() {

        BoardDemoGame boardDemoGame = new BoardDemoGame(game);

        Territory testTerritory = new Territory(0.0f, 0.0f, 1.0f, 1.0f, boardDemoGame);
        Territory testSecondTerritory = new Territory(10.0f, 0.0f, 1.0f, 1.0f, boardDemoGame);

        Unit newUnit = new Unit(testTerritory.getTerritoryX(), testTerritory.getTerritoryY(), boardDemoGame, false);
        testTerritory.setNumOfSmallUnits(1);
        testTerritory.getListOfUnits().add(newUnit);

        boardDemoGame.moveUnit(testTerritory, testSecondTerritory, 1);

        boolean test = (testTerritory.getListOfUnits().size() == 0 && testSecondTerritory.getListOfUnits().size() != 0);

        Assert.assertTrue((test));
    }

    //Robbie
    @Test
    public void territoryTestRemovingUnits() {

        BoardDemoGame boardDemoGame = new BoardDemoGame(game);

        Territory testTerritory = new Territory(0.0f, 0.0f, 1.0f, 1.0f, boardDemoGame);

        Unit newUnit = new Unit(testTerritory.getTerritoryX(), testTerritory.getTerritoryY(), boardDemoGame, false);
        testTerritory.setNumOfSmallUnits(2);
        testTerritory.getListOfUnits().add(newUnit);
        testTerritory.getListOfUnits().add(newUnit);

        boardDemoGame.removeAUnitFromTerritory(testTerritory, newUnit);

        boolean test = (testTerritory.getListOfUnits().size() == 1);

        Assert.assertTrue((test));
    }

    //Robbie
    @Test
    public void territoryTestMergingUnits() {

        BoardDemoGame boardDemoGame = new BoardDemoGame(game);

        Territory testTerritory = new Territory(0.0f, 0.0f, 1.0f, 1.0f, boardDemoGame);

        Unit newUnit = new Unit(testTerritory.getTerritoryX(), testTerritory.getTerritoryY(), boardDemoGame, false);
        testTerritory.getListOfUnits().add(newUnit);
        testTerritory.getListOfUnits().add(newUnit);
        testTerritory.getListOfUnits().add(newUnit);
        testTerritory.setNumOfSmallUnits(3);

        boardDemoGame.convertSmallUnitsToBig(testTerritory);

        boolean test = (testTerritory.getListOfUnits().size() == 1 && testTerritory.getListOfUnits().get(0).isBigUnit());

        Assert.assertTrue((test));
    }

    //Robbie
    @Test
    public void territoryTestTakingOverTerritories() {

        BoardDemoGame boardDemoGame = new BoardDemoGame(game);

        Player testPlayer = new Player("First");
        Player testSecondPlayer = new Player("Second");

        Territory testTerritory = new Territory(0.0f, 0.0f, 1.0f, 1.0f, boardDemoGame);
        Territory testSecondTerritory = new Territory(10.0f, 0.0f, 1.0f, 1.0f, boardDemoGame);

        testPlayer.getListOfTerritories().add(testTerritory);
        testSecondPlayer.getListOfTerritories().add(testSecondTerritory);

        testTerritory.setOwner(testPlayer);
        testSecondTerritory.setOwner(testSecondPlayer);

        Unit newUnit = new Unit(testTerritory.getTerritoryX(), testTerritory.getTerritoryY(), boardDemoGame, false);
        testTerritory.getListOfUnits().add(newUnit);
        testTerritory.setNumOfSmallUnits(1);

        boardDemoGame.takeOverTerritory(testTerritory, testSecondTerritory);

        boolean test = (testSecondTerritory.getListOfUnits().size() == 2 && testTerritory.getListOfUnits().size() == 0);

        Assert.assertTrue((test));
    }

    //Patrick
    @Test
    public void bonus2TakeoverNoUnits() {

        BoardDemoGame boardDemoGame = new BoardDemoGame(game);

        Player testPlayer = new Player("First");
        Player testSecondPlayer = new Player("Second");

        Territory testTerritory = new Territory(0.0f, 0.0f, 1.0f, 1.0f, boardDemoGame);
        Territory testSecondTerritory = new Territory(10.0f, 0.0f, 1.0f, 1.0f, boardDemoGame);

        testPlayer.getListOfTerritories().add(testTerritory);
        testSecondPlayer.getListOfTerritories().add(testSecondTerritory);

        testTerritory.setOwner(testPlayer);
        testSecondTerritory.setOwner(testSecondPlayer);

        boardDemoGame.bonus2Takeover(testSecondTerritory, testPlayer);

        boolean test = (testPlayer.getListOfTerritories().contains(testSecondTerritory));

        Assert.assertTrue((test));
    }
    //Chris
    @Test
    public void prostheticsCardIsValid(){

        BoardDemoGame boardDemoGame = new BoardDemoGame(game);

        Card testCard = new Card(190, 140, 300, 150, boardDemoGame.getGame().getAssetManager().getBitmap("Prosthetics"), boardDemoGame);

        Assert.assertNotNull(testCard);
    }
    //Chris
    @Test
    public void quantumDevKitCardIsValid(){

        BoardDemoGame boardDemoGame = new BoardDemoGame(game);

        Card testCard = new Card(190, 140, 300, 150, boardDemoGame.getGame().getAssetManager().getBitmap("QuantumDevKit"), boardDemoGame);

        Assert.assertNotNull(testCard);
    }

    //Chris
    @Test
    public void antiVirusCardIsValid(){

        BoardDemoGame boardDemoGame = new BoardDemoGame(game);

        Card testCard = new Card(190, 140, 300, 150, boardDemoGame.getGame().getAssetManager().getBitmap("AntiVirus"), boardDemoGame);

        Assert.assertNotNull(testCard);
    }

    //Chris
    @Test
    public void allCardGetMade(){

        BoardDemoGame boardDemoGame = new BoardDemoGame(game);
        ArrayList<Card> listOfCards = new ArrayList<Card>();

        listOfCards = boardDemoGame.setUpCards();

        Assert.assertEquals(20, listOfCards.size());
    }


    //Patrick
    @Test
    public void bonus2TakeoverUnitsOnTerritory() {

        BoardDemoGame boardDemoGame = new BoardDemoGame(game);

        Player testPlayer = new Player("First");
        Player testSecondPlayer = new Player("Second");

        Territory testTerritory = new Territory(0.0f, 0.0f, 1.0f, 1.0f, boardDemoGame);
        Territory testSecondTerritory = new Territory(10.0f, 0.0f, 1.0f, 1.0f, boardDemoGame);

        testPlayer.getListOfTerritories().add(testTerritory);
        testSecondPlayer.getListOfTerritories().add(testSecondTerritory);

        testTerritory.setOwner(testPlayer);
        testSecondTerritory.setOwner(testSecondPlayer);

        Unit enemyUnit = new Unit(0, 0, boardDemoGame, false);

        testSecondTerritory.getListOfUnits().add(enemyUnit);

        boardDemoGame.bonus2Takeover(testSecondTerritory, testPlayer);

        boolean test = (testPlayer.getListOfTerritories().contains(testSecondTerritory));

        Assert.assertFalse((test));
    }
}
