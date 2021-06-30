package uk.ac.qub.eeecs.game.actualGame;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.util.ViewportHelper;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.gage.world.Sprite;

/**
 * A simple platform-style demo that generates a number of platforms and
 * provides a player controlled entity that can move about the images.
 * <p>
 * Illustrates button based user input, animations and collision handling.
 * <p>
 * Note: See the course documentation for extension/refactoring stories
 * for this class.
 *
 * @version 1.0
 */
public class BoardDemoGame extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Define the width and height of the game world
     */
    private final float LEVEL_WIDTH = 1800.0f;
    private final float LEVEL_HEIGHT = 1000.0f;

    private final int NUM_OF_PLAYERS = 5;

    /**
     * Define the layer viewport used to display the platforms
     */
    private LayerViewport mapLayerViewport;

    //Layer to display UI
    private LayerViewport UILayerViewport;

    private PushButton attackButton;
    private PushButton moveButton;
    private PushButton moveButtonPlus;
    private PushButton moveButtonMinus;
    private PushButton mergeButton;
    private PushButton backButton;
    private PushButton settingsButton;
    private PushButton endTurnButton;

    private PushButton cameraControlsPlus;
    private PushButton cameraControlsMinus;

    //This alters the speed at which the camera moves. Change it to increase or decrease the speed.
    private float moveSpeed = 0.5f;
    private float cameraMagnification = 1;
    private float newMapLayerX;
    private float newMapLayerY;

    private Sprite gameBoard;

    private ArrayList<Territory> territoriesToDraw;

    private Player humanPlayer;
    private Player NPC1;
    private Player NPC2;
    private Player NPC3;
    private Player NPC4;
    private Player neutrallyOwned;


    private ArrayList<Player> listOfPlayers = new ArrayList<Player>();

    private Territory selectedTerritory;

    private String currentTask;

    private int numberToMove;


    private float timeLeftOnDice;
    private boolean showDice = false;
    private boolean showTripleAttackerDice = false;
    private boolean showTripleDefenderDice = false;
    private Dice attackerDice;
    private Dice defenderDice;

    private Dice attackerTripleDice1;
    private Dice attackerTripleDice2;
    private Dice attackerTripleDice3;
    private Dice defenderTripleDice1;
    private Dice defenderTripleDice2;
    private Dice defenderTripleDice3;

    private String pName;


    private PushButton bonus1Button;
    private PushButton bonus2Button;
    private PushButton bonus3Button;
    private PushButton bonus4Button;
    private PushButton bonus5Button;

    private Player bonus1OwningPlayer = neutrallyOwned;
    private Player bonus2OwningPlayer = neutrallyOwned;
    private Player bonus3OwningPlayer = neutrallyOwned;
    private Player bonus4OwningPlayer = neutrallyOwned;
    private Player bonus5OwningPlayer = neutrallyOwned;
    private Player bonus6OwningPlayer = neutrallyOwned;


    private boolean bonus1Active = false;
    private boolean bonus2Active = false;
    private boolean bonus3Active = false;
    private boolean bonus4Active = false;
    private boolean bonus5Active = false;

    private boolean bonus1OnCooldown = false;
    private boolean bonus2OnCooldown = false;
    private boolean bonus3OnCooldown = false;
    private boolean bonus4OnCooldown = false;
    private boolean bonus5OnCooldown = false;

    private boolean staticVoidTurns = false;

    private float timeCardIsDisplayed = 0.0f;


    //Sprites for UI backgrounds
    private Sprite UIBackgroundBottomLeft;
    private Sprite UIBackgroundTopLeft;
    private Sprite UIBackgroundBottomRight;
    private Sprite UIBackgroundTopRight;
    private Sprite UIBackgroundBottomLeftSmaller;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create a simple platform game level
     *
     * @param game Game to which this screen belongs
     */
    public BoardDemoGame(Game game, int startingTerritory, String playerName) {
        super("BoardDemoGame", game);

        AssetManager assetManager = mGame.getAssetManager();
        //assetManager.loadAssets("txt/assets/DemoScreenAssets.JSON");
        mGame.getAssetManager().loadAssets("txt/assets/BoardDemoAssets.JSON");

        pName = playerName;

        //Create players
        setUpPlayers();
        //Create cards
        setUpCards();



        /*
        ------
        LAYERS
        ------
        Two layers are being created. One is the Map Layer, where all of the actual game pieces are
        being placed. This will be the layer that the camera is focused on and moves.
        The other layer is the UI Layer, where the buttons and other objects such as those are placed.
        This layer doesn't move when the camera moves as then the buttons would be located only at
        a certain camera position and that would be bad game design. - Robbie
         */
        mapLayerViewport = new LayerViewport(240, 160, 240 * cameraMagnification, 160 * cameraMagnification);
        UILayerViewport = new LayerViewport(240, 160, 240, 160);

        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

        mDefaultScreenViewport = new ScreenViewport(0, 0, screenWidth + 100, screenHeight);

        attackerDice = new Dice(UILayerViewport.getWidth() * 0.25f, UILayerViewport.getHeight() * 0.5f, this);
        defenderDice = new Dice(UILayerViewport.getWidth() * 0.75f, UILayerViewport.getHeight() * 0.5f, this);

        attackerTripleDice1 = new Dice(UILayerViewport.getWidth() * 0.25f, UILayerViewport.getHeight() * 0.25f, this);
        attackerTripleDice2 = new Dice(UILayerViewport.getWidth() * 0.25f, UILayerViewport.getHeight() * 0.5f, this);
        attackerTripleDice3 = new Dice(UILayerViewport.getWidth() * 0.25f, UILayerViewport.getHeight() * 0.75f, this);

        defenderTripleDice1 = new Dice(UILayerViewport.getWidth() * 0.75f, UILayerViewport.getHeight() * 0.25f, this);
        defenderTripleDice2 = new Dice(UILayerViewport.getWidth() * 0.75f, UILayerViewport.getHeight() * 0.5f, this);
        defenderTripleDice3 = new Dice(UILayerViewport.getWidth() * 0.75f, UILayerViewport.getHeight() * 0.75f, this);

        //Creating the buttons for the UI layer.
        createUI();

        gameBoard = new Sprite(0, 0, mapLayerViewport.getWidth() * 2, mapLayerViewport.getHeight() * 2, mGame.getAssetManager().getBitmap("GameBoard"), this);

        territoriesToDraw = generateTerritories();


        switch (startingTerritory) {
            case 1:
                humanPlayer.getListOfTerritories().add(territoriesToDraw.get(1));
                territoriesToDraw.get(1).setOwner(humanPlayer);
                NPC1.getListOfTerritories().add(territoriesToDraw.get(4));
                territoriesToDraw.get(4).setOwner(NPC1);
                NPC2.getListOfTerritories().add(territoriesToDraw.get(7));
                territoriesToDraw.get(7).setOwner(NPC2);
                NPC3.getListOfTerritories().add(territoriesToDraw.get(10));
                territoriesToDraw.get(10).setOwner(NPC3);
                NPC4.getListOfTerritories().add(territoriesToDraw.get(13));
                territoriesToDraw.get(13).setOwner(NPC4);
                break;
            case 2:
                humanPlayer.getListOfTerritories().add(territoriesToDraw.get(4));
                territoriesToDraw.get(4).setOwner(humanPlayer);
                NPC1.getListOfTerritories().add(territoriesToDraw.get(1));
                territoriesToDraw.get(1).setOwner(NPC1);
                NPC2.getListOfTerritories().add(territoriesToDraw.get(7));
                territoriesToDraw.get(7).setOwner(NPC2);
                NPC3.getListOfTerritories().add(territoriesToDraw.get(10));
                territoriesToDraw.get(10).setOwner(NPC3);
                NPC4.getListOfTerritories().add(territoriesToDraw.get(13));
                territoriesToDraw.get(13).setOwner(NPC4);
                break;
            case 3:
                humanPlayer.getListOfTerritories().add(territoriesToDraw.get(7));
                territoriesToDraw.get(7).setOwner(humanPlayer);
                NPC1.getListOfTerritories().add(territoriesToDraw.get(4));
                territoriesToDraw.get(4).setOwner(NPC1);
                NPC2.getListOfTerritories().add(territoriesToDraw.get(1));
                territoriesToDraw.get(1).setOwner(NPC2);
                NPC3.getListOfTerritories().add(territoriesToDraw.get(10));
                territoriesToDraw.get(10).setOwner(NPC3);
                NPC4.getListOfTerritories().add(territoriesToDraw.get(13));
                territoriesToDraw.get(13).setOwner(NPC4);
                break;
            case 4:
                humanPlayer.getListOfTerritories().add(territoriesToDraw.get(10));
                territoriesToDraw.get(10).setOwner(humanPlayer);
                NPC1.getListOfTerritories().add(territoriesToDraw.get(4));
                territoriesToDraw.get(4).setOwner(NPC1);
                NPC2.getListOfTerritories().add(territoriesToDraw.get(7));
                territoriesToDraw.get(7).setOwner(NPC2);
                NPC3.getListOfTerritories().add(territoriesToDraw.get(1));
                territoriesToDraw.get(1).setOwner(NPC3);
                NPC4.getListOfTerritories().add(territoriesToDraw.get(13));
                territoriesToDraw.get(13).setOwner(NPC4);
                break;
            case 5:
                humanPlayer.getListOfTerritories().add(territoriesToDraw.get(13));
                territoriesToDraw.get(13).setOwner(humanPlayer);
                NPC1.getListOfTerritories().add(territoriesToDraw.get(4));
                territoriesToDraw.get(4).setOwner(NPC1);
                NPC2.getListOfTerritories().add(territoriesToDraw.get(7));
                territoriesToDraw.get(7).setOwner(NPC2);
                NPC3.getListOfTerritories().add(territoriesToDraw.get(10));
                territoriesToDraw.get(10).setOwner(NPC3);
                NPC4.getListOfTerritories().add(territoriesToDraw.get(1));
                territoriesToDraw.get(1).setOwner(NPC4);
                break;
        }

        addNewUnitToTerritory(humanPlayer.getListOfTerritories().get(0), false, false);
        addNewUnitToTerritory(NPC1.getListOfTerritories().get(0), false, false);
        addNewUnitToTerritory(NPC2.getListOfTerritories().get(0), false, false);
        addNewUnitToTerritory(NPC3.getListOfTerritories().get(0), false, false);
        addNewUnitToTerritory(NPC4.getListOfTerritories().get(0), false, false);
    }

    public BoardDemoGame(Game game) {
        super("BoardDemoGame", game);

        AssetManager assetManager = mGame.getAssetManager();
        assetManager.loadAssets("txt/assets/DemoScreenAssets.JSON");
        mGame.getAssetManager().loadAssets("txt/assets/BoardDemoAssets.JSON");


        //Create players
        setUpPlayers();
        //Create cards
        setUpCards();
    }

    private ArrayList<Territory> generateTerritories() {
        ArrayList<Territory> tempTerritories = new ArrayList<>();

        ArrayList<Card> allCards = setUpCards();

        Territory quantumComp1 = new Territory(gameBoard.getWidth() * 0.215f, gameBoard.getHeight() * 0.0f, 25f, 25f, this);
        quantumComp1.setRelatedCard(allCards.get(0));
        tempTerritories.add(quantumComp1);

        Territory quantumComp2 = new Territory(gameBoard.getWidth() * 0.29f, gameBoard.getHeight() * 0.056f, 25f, 25f, this);
        quantumComp2.setRelatedCard(allCards.get(1));
        tempTerritories.add(quantumComp2);

        Territory quantumComp3 = new Territory(gameBoard.getWidth() * 0.29f, -gameBoard.getHeight() * 0.056f, 25f, 25f, this);
        quantumComp3.setRelatedCard(allCards.get(2));
        tempTerritories.add(quantumComp3);

        Territory security1 = new Territory(gameBoard.getWidth() * 0.0f, -gameBoard.getHeight() * 0.3f, 25f, 25f, this);
        security1.setRelatedCard(allCards.get(3));
        tempTerritories.add(security1);

        Territory security2 = new Territory(gameBoard.getWidth() * 0.04f, -gameBoard.getHeight() * 0.41f, 25f, 25f, this);
        security2.setRelatedCard(allCards.get(4));
        tempTerritories.add(security2);

        Territory security3 = new Territory(-gameBoard.getWidth() * 0.04f, -gameBoard.getHeight() * 0.41f, 25f, 25f, this);
        security3.setRelatedCard(allCards.get(5));
        tempTerritories.add(security3);

        Territory robotics1 = new Territory(-gameBoard.getWidth() * 0.215f, gameBoard.getHeight() * 0.0f, 25f, 25f, this);
        robotics1.setRelatedCard(allCards.get(6));
        tempTerritories.add(robotics1);

        Territory robotics2 = new Territory(-gameBoard.getWidth() * 0.29f, -gameBoard.getHeight() * 0.056f, 25f, 25f, this);
        robotics2.setRelatedCard(allCards.get(7));
        tempTerritories.add(robotics2);

        Territory robotics3 = new Territory(-gameBoard.getWidth() * 0.29f, gameBoard.getHeight() * 0.056f, 25f, 25f, this);
        robotics3.setRelatedCard(allCards.get(8));
        tempTerritories.add(robotics3);

        Territory artificialIntelligence1 = new Territory(-gameBoard.getWidth() * 0.175f, gameBoard.getHeight() * 0.252f, 25f, 25f, this);
        artificialIntelligence1.setRelatedCard(allCards.get(9));
        tempTerritories.add(artificialIntelligence1);

        Territory artificialIntelligence2 = new Territory(-gameBoard.getWidth() * 0.255f, gameBoard.getHeight() * 0.28f, 25f, 25f, this);
        artificialIntelligence2.setRelatedCard(allCards.get(10));
        tempTerritories.add(artificialIntelligence2);

        Territory artificialIntelligence3 = new Territory(-gameBoard.getWidth() * 0.2f, gameBoard.getHeight() * 0.37f, 25f, 25f, this);
        artificialIntelligence3.setRelatedCard(allCards.get(11));
        tempTerritories.add(artificialIntelligence3);

        Territory smartProducts1 = new Territory(gameBoard.getWidth() * 0.175f, gameBoard.getHeight() * 0.252f, 25f, 25f, this);
        smartProducts1.setRelatedCard(allCards.get(12));
        tempTerritories.add(smartProducts1);

        Territory smartProducts2 = new Territory(gameBoard.getWidth() * 0.2f, gameBoard.getHeight() * 0.37f, 25f, 25f, this);
        smartProducts2.setRelatedCard(allCards.get(13));
        tempTerritories.add(smartProducts2);

        Territory smartProducts3 = new Territory(gameBoard.getWidth() * 0.255f, gameBoard.getHeight() * 0.28f, 25f, 25f, this);
        smartProducts3.setRelatedCard(allCards.get(14));
        tempTerritories.add(smartProducts3);

        Territory publicStaticVoid1 = new Territory(gameBoard.getWidth() * 0.0f, -gameBoard.getHeight() * 0.105f, 35f, 35f, this);
        publicStaticVoid1.setRelatedCard(allCards.get(15));
        tempTerritories.add(publicStaticVoid1);

        Territory publicStaticVoid2 = new Territory(-gameBoard.getWidth() * 0.075f, gameBoard.getHeight() * 0.005f, 35f, 35f, this);
        publicStaticVoid2.setRelatedCard(allCards.get(16));
        tempTerritories.add(publicStaticVoid2);

        Territory publicStaticVoid3 = new Territory(-gameBoard.getWidth() * 0.075f, gameBoard.getHeight() * 0.115f, 35f, 35f, this);
        publicStaticVoid3.setRelatedCard(allCards.get(17));
        tempTerritories.add(publicStaticVoid3);

        Territory publicStaticVoid4 = new Territory(gameBoard.getWidth() * 0.075f, gameBoard.getHeight() * 0.115f, 35f, 35f, this);
        publicStaticVoid4.setRelatedCard(allCards.get(18));
        tempTerritories.add(publicStaticVoid4);

        Territory publicStaticVoid5 = new Territory(gameBoard.getWidth() * 0.075f, gameBoard.getHeight() * 0.005f, 35f, 35f, this);
        publicStaticVoid5.setRelatedCard(allCards.get(19));
        tempTerritories.add(publicStaticVoid5);


        /////////////////////////////////////////////////////

        quantumComp1.getSurroundingTerritories().add(quantumComp2);
        quantumComp1.getSurroundingTerritories().add(quantumComp3);
        quantumComp1.getSurroundingTerritories().add(publicStaticVoid5);
        quantumComp1.getSurroundingTerritories().add(smartProducts1);
        quantumComp1.getSurroundingTerritories().add(security1);
        quantumComp2.getSurroundingTerritories().add(quantumComp1);
        quantumComp2.getSurroundingTerritories().add(quantumComp3);
        quantumComp2.getSurroundingTerritories().add(smartProducts3);
        quantumComp3.getSurroundingTerritories().add(quantumComp2);
        quantumComp3.getSurroundingTerritories().add(quantumComp1);
        quantumComp3.getSurroundingTerritories().add(security2);

        security1.getSurroundingTerritories().add(security2);
        security1.getSurroundingTerritories().add(security3);
        security1.getSurroundingTerritories().add(publicStaticVoid1);
        security1.getSurroundingTerritories().add(quantumComp1);
        security1.getSurroundingTerritories().add(robotics1);
        security2.getSurroundingTerritories().add(security1);
        security2.getSurroundingTerritories().add(security3);
        security2.getSurroundingTerritories().add(quantumComp3);
        security3.getSurroundingTerritories().add(security1);
        security3.getSurroundingTerritories().add(security2);
        security3.getSurroundingTerritories().add(robotics2);

        robotics1.getSurroundingTerritories().add(robotics2);
        robotics1.getSurroundingTerritories().add(robotics3);
        robotics1.getSurroundingTerritories().add(publicStaticVoid2);
        robotics1.getSurroundingTerritories().add(security1);
        robotics1.getSurroundingTerritories().add(artificialIntelligence1);
        robotics2.getSurroundingTerritories().add(robotics1);
        robotics2.getSurroundingTerritories().add(robotics3);
        robotics2.getSurroundingTerritories().add(security3);
        robotics3.getSurroundingTerritories().add(robotics1);
        robotics3.getSurroundingTerritories().add(robotics2);
        robotics3.getSurroundingTerritories().add(artificialIntelligence2);

        artificialIntelligence1.getSurroundingTerritories().add(artificialIntelligence2);
        artificialIntelligence1.getSurroundingTerritories().add(artificialIntelligence3);
        artificialIntelligence1.getSurroundingTerritories().add(publicStaticVoid3);
        artificialIntelligence1.getSurroundingTerritories().add(robotics1);
        artificialIntelligence1.getSurroundingTerritories().add(smartProducts1);
        artificialIntelligence2.getSurroundingTerritories().add(artificialIntelligence1);
        artificialIntelligence2.getSurroundingTerritories().add(artificialIntelligence3);
        artificialIntelligence2.getSurroundingTerritories().add(robotics3);
        artificialIntelligence3.getSurroundingTerritories().add(artificialIntelligence1);
        artificialIntelligence3.getSurroundingTerritories().add(artificialIntelligence2);
        artificialIntelligence3.getSurroundingTerritories().add(smartProducts2);

        smartProducts1.getSurroundingTerritories().add(smartProducts2);
        smartProducts1.getSurroundingTerritories().add(smartProducts3);
        smartProducts1.getSurroundingTerritories().add(publicStaticVoid4);
        smartProducts1.getSurroundingTerritories().add(artificialIntelligence1);
        smartProducts1.getSurroundingTerritories().add(quantumComp1);
        smartProducts2.getSurroundingTerritories().add(smartProducts1);
        smartProducts2.getSurroundingTerritories().add(smartProducts3);
        smartProducts2.getSurroundingTerritories().add(artificialIntelligence3);
        smartProducts3.getSurroundingTerritories().add(smartProducts1);
        smartProducts3.getSurroundingTerritories().add(smartProducts2);
        smartProducts3.getSurroundingTerritories().add(quantumComp2);

        publicStaticVoid1.getSurroundingTerritories().add(publicStaticVoid2);
        publicStaticVoid1.getSurroundingTerritories().add(publicStaticVoid5);
        publicStaticVoid2.getSurroundingTerritories().add(publicStaticVoid1);
        publicStaticVoid2.getSurroundingTerritories().add(publicStaticVoid3);
        publicStaticVoid3.getSurroundingTerritories().add(publicStaticVoid2);
        publicStaticVoid3.getSurroundingTerritories().add(publicStaticVoid4);
        publicStaticVoid4.getSurroundingTerritories().add(publicStaticVoid3);
        publicStaticVoid4.getSurroundingTerritories().add(publicStaticVoid5);
        publicStaticVoid5.getSurroundingTerritories().add(publicStaticVoid4);
        publicStaticVoid5.getSurroundingTerritories().add(publicStaticVoid1);

        for (int i = 0; i < tempTerritories.size(); i++) {
            tempTerritories.get(i).setOwner(neutrallyOwned);
            neutrallyOwned.getListOfTerritories().add(tempTerritories.get(i));
        }

        return tempTerritories;
    }

    private void setUpPlayers() {
        humanPlayer = new Player(pName);
        NPC1 = new Player("Robotboy");
        NPC2 = new Player("Sackboy");
        NPC3 = new Player("Lavagirl");
        NPC4 = new Player("Sharkboy");
        neutrallyOwned = new Player("NOT_A_PLAYER");

        listOfPlayers.add(humanPlayer);
        listOfPlayers.add(NPC1);
        listOfPlayers.add(NPC2);
        listOfPlayers.add(NPC3);
        listOfPlayers.add(NPC4);

        bonus1OwningPlayer = neutrallyOwned;
        bonus2OwningPlayer = neutrallyOwned;
        bonus3OwningPlayer = neutrallyOwned;
        bonus4OwningPlayer = neutrallyOwned;
        bonus5OwningPlayer = neutrallyOwned;

    }

    public ArrayList<Card> setUpCards() {

        ArrayList<Card> tempCards = new ArrayList<Card>();


        int width = 190;
        int height = 140;
        int x = 300;
        int y = 150;


        Card prosthetics = new Card(x, y, width, height, this.getGame().getAssetManager().getBitmap("Prosthetics"), this);
        Card replicatingHumanMove = new Card(x, y, width, height, this.getGame().getAssetManager().getBitmap("ReplicatingHumanMove"), this);
        Card robotArm = new Card(x, y, width, height, this.getGame().getAssetManager().getBitmap("RobotArm"), this);

        Card quantumDevKit = new Card(x, y, width, height, this.getGame().getAssetManager().getBitmap("QuantumDevKit"), this);
        Card encryption = new Card(x, y, width, height, this.getGame().getAssetManager().getBitmap("Encryption"), this);
        Card superpositionQubits = new Card(x, y, width, height, this.getGame().getAssetManager().getBitmap("SuperpositionQubits"), this);

        Card antiVirus = new Card(x, y, width, height, this.getGame().getAssetManager().getBitmap("AntiVirus"), this);
        Card captcha = new Card(x, y, width, height, this.getGame().getAssetManager().getBitmap("Captcha"), this);
        Card userLogIns = new Card(x, y, width, height, this.getGame().getAssetManager().getBitmap("UserLogIns"), this);

        Card automatedAppliances = new Card(x, y, width, height, this.getGame().getAssetManager().getBitmap("AutomatedAppliances"), this);
        Card smartAssistant = new Card(x, y, width, height, this.getGame().getAssetManager().getBitmap("SmartAssistant"), this);
        Card smartLighting = new Card(x, y, width, height, this.getGame().getAssetManager().getBitmap("SmartLighting"), this);

        Card machineLearning = new Card(x, y, width, height, this.getGame().getAssetManager().getBitmap("MachineLearning"), this);
        Card neuralNetworks = new Card(x, y, width, height, this.getGame().getAssetManager().getBitmap("NeuralNetworks"), this);
        Card turingTest = new Card(x, y, width, height, this.getGame().getAssetManager().getBitmap("TuringTest"), this);

        Card public1 = new Card(x, y, width, height, this.getGame().getAssetManager().getBitmap("public"), this);
        Card static1 = new Card(x, y, width, height, this.getGame().getAssetManager().getBitmap("static"), this);
        Card void1 = new Card(x, y, width, height, this.getGame().getAssetManager().getBitmap("void"), this);
        Card main1 = new Card(x, y, width, height, this.getGame().getAssetManager().getBitmap("main"), this);
        Card brackets = new Card(x, y, width, height, this.getGame().getAssetManager().getBitmap("()"), this);

        tempCards.add(quantumDevKit);
        tempCards.add(encryption);
        tempCards.add(superpositionQubits);

        tempCards.add(antiVirus);
        tempCards.add(captcha);
        tempCards.add(userLogIns);

        tempCards.add(prosthetics);
        tempCards.add(replicatingHumanMove);
        tempCards.add(robotArm);

        tempCards.add(machineLearning);
        tempCards.add(neuralNetworks);
        tempCards.add(turingTest);

        tempCards.add(automatedAppliances);
        tempCards.add(smartAssistant);
        tempCards.add(smartLighting);

        tempCards.add(public1);
        tempCards.add(static1);
        tempCards.add(void1);
        tempCards.add(main1);
        tempCards.add(brackets);


        return tempCards;
    }

    private void createUI() {
        attackButton = new PushButton(
                UILayerViewport.getWidth() * 0.08f, UILayerViewport.getHeight() * 0.10f,
                UILayerViewport.getWidth() * 0.125f, UILayerViewport.getHeight() * 0.15f,
                "FightButtonDisabled", "FightButtonDisabled", this);

        moveButton = new PushButton(
                UILayerViewport.getWidth() * 0.210f, UILayerViewport.getHeight() * 0.10f,
                UILayerViewport.getWidth() * 0.125f, UILayerViewport.getHeight() * 0.15f,
                "MoveButtonDisabled", "MoveButtonDisabled", this);

        moveButtonMinus = new PushButton(
                UILayerViewport.getWidth() * 0.125f, UILayerViewport.getHeight() * 0.25f,
                UILayerViewport.getWidth() * 0.075f, UILayerViewport.getHeight() * 0.10f,
                "ZoomButtonMinus", "ZoomButtonMinus", this);
        moveButtonPlus = new PushButton(
                UILayerViewport.getWidth() * 0.275f, UILayerViewport.getHeight() * 0.25f,
                UILayerViewport.getWidth() * 0.075f, UILayerViewport.getHeight() * 0.10f,
                "ZoomButtonPlus", "ZoomButtonPlus", this);

        mergeButton = new PushButton(
                UILayerViewport.getWidth() * 0.34f, UILayerViewport.getHeight() * 0.10f,
                UILayerViewport.getWidth() * 0.125f, UILayerViewport.getHeight() * 0.15f,
                "MergeButtonDisabled", "MergeButtonDisabled", this);

        backButton = new PushButton(
                UILayerViewport.getWidth() * 0.85f, UILayerViewport.getHeight() * 0.925f,
                UILayerViewport.getWidth() * 0.075f, UILayerViewport.getHeight() * 0.10f,
                "BackButton", "BackButton", this);
        backButton.setPlaySounds(true, true);

        settingsButton = new PushButton(
                UILayerViewport.getWidth() * 0.95f, UILayerViewport.getHeight() * 0.925f,
                UILayerViewport.getWidth() * 0.075f, UILayerViewport.getHeight() * 0.10f,
                "SettingsButton", "SettingsButton", this);
        settingsButton.setPlaySounds(true, true);

        cameraControlsPlus = new PushButton(
                UILayerViewport.getWidth() * 0.2f, UILayerViewport.getHeight() * 0.925f,
                UILayerViewport.getWidth() * 0.075f, UILayerViewport.getHeight() * 0.1f,
                "ZoomButtonPlus", "ZoomButtonPlus", this);

        cameraControlsMinus = new PushButton(
                UILayerViewport.getWidth() * 0.05f, UILayerViewport.getHeight() * 0.925f,
                UILayerViewport.getWidth() * 0.075f, UILayerViewport.getHeight() * 0.1f,
                "ZoomButtonMinus", "ZoomButtonMinus", this);

        endTurnButton = new PushButton(
                UILayerViewport.getWidth() * 0.95f, UILayerViewport.getHeight() * 0.075f,
                UILayerViewport.getWidth() * 0.075f, UILayerViewport.getHeight() * 0.1f,
                "EndTurnButton", "EndTurnButton", this);

        bonus1Button = new PushButton(
                UILayerViewport.getWidth() * 0.5f, UILayerViewport.getHeight() * 0.05f,
                UILayerViewport.getWidth() * 0.0625f, UILayerViewport.getHeight() * 0.075f,
                "bonus1Button", "bonus1Button", this);
        bonus2Button = new PushButton(
                UILayerViewport.getWidth() * 0.575f, UILayerViewport.getHeight() * 0.05f,
                UILayerViewport.getWidth() * 0.0625f, UILayerViewport.getHeight() * 0.075f,
                "bonus2Button", "bonus2Button", this);
        bonus3Button = new PushButton(
                UILayerViewport.getWidth() * 0.65f, UILayerViewport.getHeight() * 0.05f,
                UILayerViewport.getWidth() * 0.0625f, UILayerViewport.getHeight() * 0.075f,
                "bonus3Button", "bonus3Button", this);
        bonus4Button = new PushButton(
                UILayerViewport.getWidth() * 0.725f, UILayerViewport.getHeight() * 0.05f,
                UILayerViewport.getWidth() * 0.0625f, UILayerViewport.getHeight() * 0.075f,
                "bonus4Button", "bonus4Button", this);
        bonus5Button = new PushButton(
                UILayerViewport.getWidth() * 0.8f, UILayerViewport.getHeight() * 0.050f,
                UILayerViewport.getWidth() * 0.0625f, UILayerViewport.getHeight() * 0.075f,
                "bonus5Button", "bonus5Button", this);

        UIBackgroundBottomLeft = new Sprite(75, UILayerViewport.getHeight() * 0.070f,
                300f, 100f, this.getGame().getAssetManager().getBitmap("UIButtonBackground"), this);
        UIBackgroundBottomRight = new Sprite(UILayerViewport.getWidth() - 30, UILayerViewport.getHeight() * 0.050f,
                100f, 100f, this.getGame().getAssetManager().getBitmap("UIButtonBackgroundBottomRight"), this);
        UIBackgroundTopRight = new Sprite(UILayerViewport.getWidth() * 0.9f, UILayerViewport.getHeight() * 0.9215f,
                150f, 50f, this.getGame().getAssetManager().getBitmap("UIButtonBackgroundTopRight"), this);

        UIBackgroundBottomLeftSmaller = new Sprite(300, UILayerViewport.getHeight() * 0.050f,
                275f, 40f, this.getGame().getAssetManager().getBitmap("UIButtonBackground"), this);


        UIBackgroundTopLeft = new Sprite(0, UILayerViewport.getHeight() * 0.9215f,
                275f, -50f, this.getGame().getAssetManager().getBitmap("UIButtonBackground"), this);
    }

    //Made Public for testing purposes
    public void removeAUnitFromTerritory(Territory territory, Unit unitToRemove) {
        if (unitToRemove.isBigUnit()) {
            territory.setNumOfBigUnits(territory.getNumOfBigUnits() - 1);
        } else {
            territory.setNumOfSmallUnits(territory.getNumOfSmallUnits() - 1);
        }
        territory.getListOfUnits().remove(unitToRemove);
    }

    //Made Public for testing purposes
    public void moveUnit(Territory originalTerritory, Territory destinationTerritory, int numOfUnitsToMove) {

        ArrayList<Unit> unitsToRemove = new ArrayList<Unit>();

        System.out.println("IM GOING TO MOVE " + numOfUnitsToMove + " OUT OF A TOTAL OF " + originalTerritory.getListOfUnits().size());

        for (int i = 0; i < numOfUnitsToMove; i++) {
            if (!originalTerritory.getListOfUnits().get(i).isExhausted()) {
                if (originalTerritory.getListOfUnits().get(i).isBigUnit()) {
                    addNewUnitToTerritory(destinationTerritory, true, true);
                    unitsToRemove.add(originalTerritory.getListOfUnits().get(i));
                } else {
                    addNewUnitToTerritory(destinationTerritory, false, true);
                    unitsToRemove.add(originalTerritory.getListOfUnits().get(i));
                }
            }
        }

        for (Unit allUnitsToRemove : unitsToRemove) {
            removeAUnitFromTerritory(originalTerritory, allUnitsToRemove);
        }

    }

    public void endTurn() {

        for (int i = 1; i < listOfPlayers.size(); i++) {

            ArrayList<Territory> attackerQueue = new ArrayList<>();
            ArrayList<Territory> defenderQueue = new ArrayList<>();

            ArrayList<Territory> sourceQueue = new ArrayList<>();
            ArrayList<Territory> destinationQueue = new ArrayList<>();


            for (Territory territoriesOwned : listOfPlayers.get(i).getListOfTerritories()) {
                if (territoriesOwned.getListOfUnits().size() > 0) {

                    System.out.println("ENDTURN01");

                    boolean foundAttackForTerritoryToDo = false;
                    boolean foundMoveForTerritoryToDo = false;

                    for (Territory territoriesSurr : territoriesOwned.getSurroundingTerritories()) {
                        if (territoriesSurr.getOwner().equals(neutrallyOwned)) {
                            System.out.println("ENDTURN02 - neutral");
                            if (!foundAttackForTerritoryToDo) {
                                System.out.println("ENDTURN03");
                                attackerQueue.add(territoriesOwned);
                                defenderQueue.add(territoriesSurr);
                                foundAttackForTerritoryToDo = true;
                            }
                        }
                    }

                    if (!foundAttackForTerritoryToDo) {
                        for (Territory territoriesSurr : territoriesOwned.getSurroundingTerritories()) {
                            if (!territoriesSurr.getOwner().equals(listOfPlayers.get(i))) {
                                System.out.println("ENDTURN02 - not player");
                                attackerQueue.add(territoriesOwned);
                                defenderQueue.add(territoriesSurr);
                                foundAttackForTerritoryToDo = true;
                            }
                        }
                    }


                    if (!foundAttackForTerritoryToDo) {
                        for (Territory territoriesSurr : territoriesOwned.getSurroundingTerritories()) {
                            if (territoriesSurr.getOwner().equals(listOfPlayers.get(i))) {
                                if (!foundMoveForTerritoryToDo) {
                                    sourceQueue.add(territoriesOwned);
                                    destinationQueue.add(territoriesSurr);
                                    foundMoveForTerritoryToDo = true;
                                }

                            }
                        }
                    }
                }
            }

            System.out.println("ATK Q: " + attackerQueue.size());

            for (int j = 0; j < attackerQueue.size(); j++) {
                //If the area you are attacking is STILL not owned by the attacker
                if (!defenderQueue.get(j).getOwner().equals(listOfPlayers.get(i))) {
                    System.out.println("ATTACK BETWEEN " + attackerQueue.get(j).getOwner().getName() + " AND " + defenderQueue.get(j).getOwner().getName());
                    declareAttack(attackerQueue.get(j), defenderQueue.get(j));
                }
            }

            System.out.println("SOURCE Q: " + sourceQueue.size());

            if (sourceQueue.size() > 0) {
                for (int j = 0; j < sourceQueue.size(); j++) {
                    System.out.println("MOVING UNITS");
                    moveUnit(sourceQueue.get(j), destinationQueue.get(j), sourceQueue.get(j).getListOfUnits().size());
                }
            }
        }

        ArrayList<Player> playersToRemove = new ArrayList<>();

        for (Player allPlayers : listOfPlayers) {
            if (allPlayers.getListOfTerritories().size() <= 0) {
                playersToRemove.add(allPlayers);
            }
        }

        for (Player allRemovedPlayers : playersToRemove) {
            if (allRemovedPlayers.equals(humanPlayer)) {
                mGame.getScreenManager().addScreen(new EndScreen(mGame, false, pName));
            }

            listOfPlayers.remove(allRemovedPlayers);

            if (listOfPlayers.size() == 1 && listOfPlayers.contains(humanPlayer)) {
                mGame.getScreenManager().addScreen(new EndScreen(mGame, true, pName));
            }
        }
        if (bonus6OwningPlayer != null) {
            if (staticVoidTurns) {
                if (bonus6OwningPlayer == humanPlayer) {
                    mGame.getScreenManager().addScreen(new EndScreen(mGame, true, pName));
                } else {
                    mGame.getScreenManager().addScreen(new EndScreen(mGame, false, pName));
                }
            } else {
                staticVoidTurns = true;
            }
        } else {
            staticVoidTurns = false;
        }
    }


    public void declareAttack(Territory attacker, Territory defender) {
        /*
        get every non-exhausted attacking unit////////////////
        make them attack each defending unit until either dies
        ---
        if an attacker dies, go the next one
        if all attacker die, stop
        if all defenders die, take it over
         */

        System.out.println("ATTACK IS BEING DECLARED");

        int attackerResult;
        int attackerResult2;
        int attackerResult3;
        int defenderResult;
        int defenderResult2;
        int defenderResult3;

        boolean allAttackersDefeated = false;
        boolean allDefendersDefeated = false;

        while (!allAttackersDefeated) {

            ArrayList<Unit> nonExhaustedAttackers = new ArrayList<Unit>();

            for (Unit allAttackers : attacker.getListOfUnits()) {
                if (!allAttackers.isExhausted()) {
                    nonExhaustedAttackers.add(allAttackers);
                }
            }

            if (nonExhaustedAttackers.size() == 0) {
                break;
            }

            ArrayList<Unit> defeatedAttackersToBeDeleted = new ArrayList<Unit>();

            for (Unit allNonExhaustedUnits : nonExhaustedAttackers) {

                boolean unitIsDefeated = false;

                if (!allDefendersDefeated) {

                    //If they have a unit, standard procedure
                    if (defender.getListOfUnits().size() > 0) {

                        ArrayList<Unit> defeatedDefendersToBeDeleted = new ArrayList<Unit>();

                        if (allNonExhaustedUnits.isBigUnit()) {

                            attackerResult = attackerTripleDice1.generateDiceRoll();
                            attackerResult2 = attackerTripleDice2.generateDiceRoll();
                            attackerResult3 = attackerTripleDice3.generateDiceRoll();

                            if (bonus3Active) {
                                attackerResult++;
                                attackerResult2++;
                                attackerResult3++;
                                bonus3OnCooldown = true;
                                bonus3Active = false;
                            }

                            attackerTripleDice1.visuallyRollDice(attackerResult);
                            attackerTripleDice2.visuallyRollDice(attackerResult2);
                            attackerTripleDice3.visuallyRollDice(attackerResult3);
                            showTripleAttackerDice = true;

                            for (Unit allDefendingUnits : defender.getListOfUnits()) {
                                if (!unitIsDefeated) {

                                    if (allDefendingUnits.isBigUnit()) {
                                        System.out.println("3 DICE VS 3 DICE");

                                        defenderResult = defenderTripleDice1.generateDiceRoll();
                                        defenderResult2 = defenderTripleDice2.generateDiceRoll();
                                        defenderResult3 = defenderTripleDice3.generateDiceRoll();

                                        if (bonus1Active) {
                                            defenderResult--;
                                            defenderResult2--;
                                            defenderResult3--;
                                            bonus1OnCooldown = true;
                                            bonus1Active = false;
                                        }

                                        defenderTripleDice1.visuallyRollDice(defenderResult);
                                        defenderTripleDice2.visuallyRollDice(defenderResult2);
                                        defenderTripleDice3.visuallyRollDice(defenderResult3);
                                        showTripleDefenderDice = true;

                                        int totalAttackerWins = 0;

                                        if (attackerResult > defenderResult) {
                                            totalAttackerWins++;
                                        }
                                        if (attackerResult2 > defenderResult2) {
                                            totalAttackerWins++;
                                        }
                                        if (attackerResult3 > defenderResult3) {
                                            totalAttackerWins++;
                                        }

                                        if (totalAttackerWins >= 2) {
                                            defeatedDefendersToBeDeleted.add(allDefendingUnits);
                                        } else {
                                            defeatedAttackersToBeDeleted.add(allNonExhaustedUnits);
                                            unitIsDefeated = true;
                                        }


                                    } else {
                                        System.out.println("3 DICE VS 1 DICE");

                                        defenderResult = defenderDice.generateDiceRoll();

                                        defenderDice.visuallyRollDice(defenderResult);

                                        if (bonus1Active) {
                                            defenderResult--;
                                            bonus1OnCooldown = true;
                                            bonus1Active = false;
                                        }

                                        showTripleDefenderDice = false;

                                        int totalAttackerWins = 0;

                                        if (attackerResult > defenderResult) {
                                            totalAttackerWins++;
                                        }
                                        if (attackerResult2 > defenderResult) {
                                            totalAttackerWins++;
                                        }
                                        if (attackerResult3 > defenderResult) {
                                            totalAttackerWins++;
                                        }

                                        if (totalAttackerWins > 0) {
                                            defeatedDefendersToBeDeleted.add(allDefendingUnits);
                                        } else {
                                            defeatedAttackersToBeDeleted.add(allNonExhaustedUnits);
                                            unitIsDefeated = true;
                                        }
                                    }
                                }

                            }

                        } else {
                            attackerResult = attackerDice.generateDiceRoll();

                            if (bonus3Active) {
                                attackerResult++;
                                bonus3OnCooldown = true;
                                bonus3Active = false;
                            }

                            attackerDice.visuallyRollDice(attackerResult);

                            showTripleAttackerDice = false;

                            for (Unit allDefendingUnits : defender.getListOfUnits()) {
                                if (!unitIsDefeated) {

                                    if (allDefendingUnits.isBigUnit()) {
                                        System.out.println("1 DICE VS 3 DICE");

                                        defenderResult = defenderTripleDice1.generateDiceRoll();
                                        defenderResult2 = defenderTripleDice2.generateDiceRoll();
                                        defenderResult3 = defenderTripleDice3.generateDiceRoll();

                                        if (bonus1Active) {
                                            defenderResult--;
                                            defenderResult2--;
                                            defenderResult3--;
                                            bonus1OnCooldown = true;
                                            bonus1Active = false;
                                        }

                                        defenderTripleDice1.visuallyRollDice(defenderResult);
                                        defenderTripleDice2.visuallyRollDice(defenderResult2);
                                        defenderTripleDice3.visuallyRollDice(defenderResult3);

                                        showTripleDefenderDice = true;

                                        int totalAttackerWins = 0;

                                        if (attackerResult > defenderResult) {
                                            totalAttackerWins++;
                                        }
                                        if (attackerResult > defenderResult2) {
                                            totalAttackerWins++;
                                        }
                                        if (attackerResult > defenderResult3) {
                                            totalAttackerWins++;
                                        }

                                        if (totalAttackerWins == 3) {
                                            defeatedDefendersToBeDeleted.add(allDefendingUnits);
                                        } else {
                                            defeatedAttackersToBeDeleted.add(allNonExhaustedUnits);
                                            unitIsDefeated = true;
                                        }

                                    } else {
                                        System.out.println("1 DICE VS 1 DICE");

                                        defenderResult = defenderDice.generateDiceRoll();

                                        if (bonus1Active) {
                                            defenderResult--;
                                            bonus1OnCooldown = true;
                                            bonus1Active = false;
                                        }

                                        defenderDice.visuallyRollDice(defenderResult);

                                        showTripleDefenderDice = false;

                                        int totalAttackerWins = 0;

                                        if (attackerResult > defenderResult) {
                                            totalAttackerWins++;
                                        }

                                        if (totalAttackerWins > 0) {
                                            defeatedDefendersToBeDeleted.add(allDefendingUnits);
                                        } else {
                                            defeatedAttackersToBeDeleted.add(allNonExhaustedUnits);
                                            unitIsDefeated = true;
                                        }
                                    }
                                }
                            }
                        }

                        //Delete the defeated defending units
                        for (Unit allDefeatedDefenders : defeatedDefendersToBeDeleted) {
                            removeAUnitFromTerritory(defender, allDefeatedDefenders);
                        }

                        if (defender.getListOfUnits().size() == 0) {
                            allDefendersDefeated = true;
                        }

                    } else {
                        if (defender.getOwner().equals(neutrallyOwned)) {
                            System.out.println("WE ARE ATTACKING SOMETHING NEUTRALLY OWNED, THIS MESSAGE IS LONG IN ORDER TO SEE IT EASILY");

                            if (allNonExhaustedUnits.isBigUnit()) {
                                attackerResult = attackerTripleDice1.generateDiceRoll();
                                attackerResult2 = attackerTripleDice2.generateDiceRoll();
                                attackerResult3 = attackerTripleDice3.generateDiceRoll();

                                if (bonus3Active) {
                                    attackerResult++;
                                    attackerResult2++;
                                    attackerResult3++;
                                    bonus3OnCooldown = true;
                                    bonus3Active = false;
                                }

                                attackerTripleDice1.visuallyRollDice(attackerResult);
                                attackerTripleDice2.visuallyRollDice(attackerResult2);
                                attackerTripleDice3.visuallyRollDice(attackerResult3);
                                showTripleAttackerDice = true;

                                defenderResult = 3;

                                if (bonus1Active) {
                                    defenderResult--;
                                    bonus1OnCooldown = true;
                                    bonus1Active = false;
                                }

                                defenderDice.visuallyRollDice(defenderResult);
                                showTripleDefenderDice = false;

                                int totalAttackerWins = 0;

                                if (attackerResult > defenderResult) {
                                    totalAttackerWins++;
                                }
                                if (attackerResult2 > defenderResult) {
                                    totalAttackerWins++;
                                }
                                if (attackerResult3 > defenderResult) {
                                    totalAttackerWins++;
                                }

                                if (totalAttackerWins > 0) {
                                    takeOverTerritory(attacker, defender);
                                } else {

                                }

                            } else {
                                attackerResult = attackerDice.generateDiceRoll();

                                if (bonus3Active) {
                                    attackerResult++;
                                    bonus3OnCooldown = true;
                                    bonus3Active = false;
                                }

                                attackerDice.visuallyRollDice(attackerResult);
                                showTripleAttackerDice = false;

                                defenderResult = 3;

                                if (bonus1Active) {
                                    defenderResult--;
                                    bonus1OnCooldown = true;
                                    bonus1Active = false;
                                }

                                defenderDice.visuallyRollDice(defenderResult);
                                showTripleDefenderDice = false;

                                if (attackerResult > defenderResult) {
                                    takeOverTerritory(attacker, defender);
                                    allDefendersDefeated = true;
                                }
                            }
                        } else {
                            takeOverTerritory(attacker, defender);
                            allDefendersDefeated = true;
                        }
                    }
                }

                //Exit conditions
                if (allDefendersDefeated && attacker.getListOfUnits().size() > 0) {
                    takeOverTerritory(attacker, defender);
                    break;
                }


            }

            for (Unit allDefeatedAttackers : defeatedAttackersToBeDeleted) {
                if (bonus4Active) {
                    bonus4OnCooldown = true;
                    bonus4Active = false;
                } else {
                    removeAUnitFromTerritory(attacker, allDefeatedAttackers);
                    nonExhaustedAttackers.remove(allDefeatedAttackers);
                }
            }

            System.out.println("SHOW DICE TRUE");

            showDice = true;
            timeLeftOnDice = 3.0f;

        }
    }

    private void addNewUnitToTerritory(Territory territory, boolean isBigUnit, boolean startExhausted) {
        Unit newUnit = new Unit(territory.getTerritoryX(), territory.getTerritoryY(), this, isBigUnit);
        newUnit.setExhausted(startExhausted);
        territory.getListOfUnits().add(newUnit);
        if (isBigUnit) {
            territory.setNumOfBigUnits(territory.getNumOfBigUnits() + 1);
        } else {
            territory.setNumOfSmallUnits(territory.getNumOfSmallUnits() + 1);
        }
    }

    public void takeOverTerritory(Territory attacker, Territory defender) {
        defender.getOwner().getListOfTerritories().remove(defender);
        defender.setOwner(attacker.getOwner());
        attacker.getOwner().getListOfTerritories().add(defender);

        //REMOVE THEIR UNITS FROM THE OLD SPOT.
        moveUnit(attacker, defender, attacker.getListOfUnits().size());

        addNewUnitToTerritory(defender, false, true);
    }

    public void bonus2Takeover(Territory territoryToTake, Player player) {
        if((territoryToTake.getListOfUnits().size() == 0) && territoryToTake.getOwner() != player) {
            territoryToTake.getOwner().getListOfTerritories().remove(territoryToTake);
            territoryToTake.setOwner(player);
            player.getListOfTerritories().add(territoryToTake);
            bonus2Active = false;
            bonus2OnCooldown = true;
        }
    }

    public void convertSmallUnitsToBig(Territory selectedTerritory) {
        int unitCount = 0;

        Unit newBigUnit = new Unit(selectedTerritory.getTerritoryX(), selectedTerritory.getTerritoryY(), this, true);
        newBigUnit.setExhausted(true);
        selectedTerritory.getListOfUnits().add(newBigUnit);

        selectedTerritory.setNumOfBigUnits(selectedTerritory.getNumOfBigUnits() + 1);

        for (int i = 0; i < selectedTerritory.getListOfUnits().size(); i++) {
            if (!selectedTerritory.getListOfUnits().get(i).isBigUnit()) {
                unitCount += 1;
                selectedTerritory.getListOfUnits().remove(i);
                selectedTerritory.setNumOfSmallUnits(selectedTerritory.getNumOfSmallUnits() - 1);
                i--;
            }

            if (unitCount == 3) {
                break;
            }
        }
    }

    private void checkForDeadUnits(Player player) {
        int numOfUnits = 0;

        for (Territory allTerritories : player.getListOfTerritories()) {
            for (Unit allUnits : allTerritories.getListOfUnits()) {
                if (allUnits.isBigUnit()) {
                    numOfUnits += 3;
                } else if (!allUnits.isBigUnit()) {
                    numOfUnits++;
                }
            }
        }
        if (numOfUnits < player.getListOfTerritories().size()) {
            addNewUnitToTerritory(player.getListOfTerritories().get(0), false, false);
        }
    }

    private void checkForBonuses(Player player) {
        if (player.getListOfTerritories().contains(territoriesToDraw.get(0)) && player.getListOfTerritories().contains(territoriesToDraw.get(1)) && player.getListOfTerritories().contains(territoriesToDraw.get(2))) {
            bonus1OwningPlayer = player;
        } else if (bonus1OwningPlayer == player) {
            bonus1OwningPlayer = neutrallyOwned;
        }
        if (player.getListOfTerritories().contains(territoriesToDraw.get(3)) && player.getListOfTerritories().contains(territoriesToDraw.get(4)) && player.getListOfTerritories().contains(territoriesToDraw.get(5))) {
            bonus2OwningPlayer = player;
        } else if (bonus2OwningPlayer == player) {
            bonus2OwningPlayer = neutrallyOwned;
        }
        if (player.getListOfTerritories().contains(territoriesToDraw.get(6)) && player.getListOfTerritories().contains(territoriesToDraw.get(7)) && player.getListOfTerritories().contains(territoriesToDraw.get(8))) {
            bonus3OwningPlayer = player;
        } else if (bonus3OwningPlayer == player) {
            bonus3OwningPlayer = neutrallyOwned;
        }
        if (player.getListOfTerritories().contains(territoriesToDraw.get(9)) && player.getListOfTerritories().contains(territoriesToDraw.get(10)) && player.getListOfTerritories().contains(territoriesToDraw.get(11))) {
            bonus4OwningPlayer = player;
        } else if (bonus4OwningPlayer == player) {
            bonus4OwningPlayer = neutrallyOwned;
        }
        if (player.getListOfTerritories().contains(territoriesToDraw.get(12)) && player.getListOfTerritories().contains(territoriesToDraw.get(13)) && player.getListOfTerritories().contains(territoriesToDraw.get(14))) {
            bonus5OwningPlayer = player;
        } else if (bonus5OwningPlayer == player) {
            bonus5OwningPlayer = neutrallyOwned;
        }
        if (player.getListOfTerritories().contains(territoriesToDraw.get(15)) && player.getListOfTerritories().contains(territoriesToDraw.get(16)) && player.getListOfTerritories().contains(territoriesToDraw.get(17)) && player.getListOfTerritories().contains(territoriesToDraw.get(18)) && player.getListOfTerritories().contains(territoriesToDraw.get(19))) {
            bonus6OwningPlayer = player;
        } else if (bonus6OwningPlayer == player) {
            bonus6OwningPlayer = neutrallyOwned;
        }
    }


    private void checkForPotentialMerges(Player player) {
        for (Territory allTerritories : player.getListOfTerritories()) {
            int numOfSmallUnits = 0;

            for (Unit allUnits : allTerritories.getListOfUnits()) {
                if (!allUnits.isBigUnit()) {
                    numOfSmallUnits++;
                }
            }

            if (numOfSmallUnits >= 3) {
                Random rng = new Random();
                if (rng.nextBoolean() == true) {
                    convertSmallUnitsToBig(allTerritories);
                }
            }
        }
    }

    boolean isFightActive = false;
    boolean isMoveActive = false;


    // /////////////////////////////////////////////////////////////////////////
    // Update and Draw
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Update the platform demo screen
     *
     * @param elapsedTime Elapsed time information
     */
    @Override
    public void update(ElapsedTime elapsedTime) {

        boolean buttonsPushed = false;

        // Update the player
        Input input = this.getGame().getInput();

        backButton.update(elapsedTime);
        if (backButton.isPushTriggered()) {
            mGame.getScreenManager().removeScreen(this);
            buttonsPushed = true;
        }


        for (Territory allTerritories : territoriesToDraw) {

            boolean allUnitsBig = true;
            boolean allUnitsSmall = true;
            boolean allUnitsExhausted = true;
            boolean allUnitsNonExhausted = true;

            for (Unit allUnits : allTerritories.getListOfUnits()) {
                if (allUnits.isExhausted()) {
                    allUnitsNonExhausted = false;
                } else {
                    allUnitsExhausted = false;
                }

                if (allUnits.isBigUnit()) {
                    allUnitsSmall = false;
                } else {
                    allUnitsBig = false;
                }
            }

            boolean allUnitsSameState = (allUnitsNonExhausted || allUnitsExhausted);
            boolean allUnitsSameSize = (allUnitsBig || allUnitsSmall);

            int unitOffset = 15;

            for (Unit allUnits : allTerritories.getListOfUnits()) {
                if (allUnitsSameSize) {
                    if (allUnitsSameState) {
                        allUnits.setPosition(allTerritories.getTerritoryX(), allTerritories.getTerritoryY());
                    } else {
                        if (allUnits.isExhausted()) {
                            allUnits.setPosition(allTerritories.getTerritoryX(), allTerritories.getTerritoryY()+unitOffset);
                        } else {
                            allUnits.setPosition(allTerritories.getTerritoryX(), allTerritories.getTerritoryY()-unitOffset);
                        }
                    }
                } else {
                    if (allUnitsSameState) {
                        if (allUnits.isBigUnit()) {
                            allUnits.setPosition(allTerritories.getTerritoryX()-unitOffset, allTerritories.getTerritoryY());
                        } else {
                            allUnits.setPosition(allTerritories.getTerritoryX()+unitOffset, allTerritories.getTerritoryY());
                        }
                    } else {
                        if (allUnits.isBigUnit()) {
                            if(allUnits.isExhausted()) {
                                allUnits.setPosition(allTerritories.getTerritoryX()-unitOffset, allTerritories.getTerritoryY()+unitOffset);
                            } else {
                                allUnits.setPosition(allTerritories.getTerritoryX()-unitOffset, allTerritories.getTerritoryY()-unitOffset);
                            }
                        } else {
                            if(allUnits.isExhausted()) {
                                allUnits.setPosition(allTerritories.getTerritoryX()+unitOffset, allTerritories.getTerritoryY()+unitOffset);
                            } else {
                                allUnits.setPosition(allTerritories.getTerritoryX()+unitOffset, allTerritories.getTerritoryY()-unitOffset);
                            }
                        }
                    }
                }
            }


        }


        settingsButton.update(elapsedTime);
        if (settingsButton.isPushTriggered()) {
            mGame.getScreenManager().addScreen(new OptionsDemoScreen(mGame));
            buttonsPushed = true;
        }


        endTurnButton.update(elapsedTime);

        //CAMERA CONTROLS
        cameraControlsMinus.update(elapsedTime);
        cameraControlsPlus.update(elapsedTime);
        if (cameraControlsMinus.isPushTriggered()) {
            if (cameraMagnification < 2.0f) {
                cameraMagnification += 0.1f;
            }

            mapLayerViewport.set(mapLayerViewport.x, mapLayerViewport.y, 240 * cameraMagnification, 160 * cameraMagnification);

            buttonsPushed = true;
        } else if (cameraControlsPlus.isPushTriggered() && cameraMagnification > 0.1f) {

            if (cameraMagnification > 0.1f) {
                cameraMagnification -= 0.1f;
            }
            mapLayerViewport.set(mapLayerViewport.x, mapLayerViewport.y, 240 * cameraMagnification, 160 * cameraMagnification);

            buttonsPushed = true;
        }

        moveButton.update(elapsedTime);
        if (moveButton.isPushTriggered()) {
            if (selectedTerritory != null) {
                if (selectedTerritory.getListOfUnits().size() > 0) {
                    currentTask = "MOVE";
                    numberToMove = 1;
                }
            }
            buttonsPushed = true;
        }

        if (currentTask == "MOVE") {
            moveButtonMinus.update(elapsedTime);
            if (moveButtonMinus.isPushTriggered()) {
                numberToMove--;
                if (numberToMove < 1) {
                    numberToMove = 1;
                }
                buttonsPushed = true;
            }
            moveButtonPlus.update(elapsedTime);
            if (moveButtonPlus.isPushTriggered()) {
                numberToMove++;
                if (numberToMove > selectedTerritory.getListOfUnits().size()) {
                    numberToMove = selectedTerritory.getListOfUnits().size();
                }
                buttonsPushed = true;
            }
        }

        attackButton.update(elapsedTime);
        if (attackButton.isPushTriggered()) {
            if (selectedTerritory != null) {
                if (selectedTerritory.getListOfUnits().size() > 0) {
                    currentTask = "ATTACK";
                }
            }
            buttonsPushed = true;
        }

        if (currentTask == "ATTACK") {
            isFightActive = true;
            isMoveActive = false;
        } else if (currentTask == "MOVE") {
            isFightActive = false;
            isMoveActive = true;
        } else {
            isFightActive = false;
            isMoveActive = false;
        }

        mergeButton.update(elapsedTime);
        if (mergeButton.isPushTriggered()) {
            if (selectedTerritory != null) {
                if (selectedTerritory.getNumOfSmallUnits() >= 3) {
                    convertSmallUnitsToBig(selectedTerritory);
                }

            }
            buttonsPushed = true;
        }

        endTurnButton.update(elapsedTime);
        if (endTurnButton.isPushTriggered()) {
            for (Territory allTerritories : territoriesToDraw) {
                for (Unit allUnits : allTerritories.getListOfUnits()) {
                    allUnits.setExhausted(false);
                }
            }
            endTurn();

            for (int i = 1; i < listOfPlayers.size(); i++) {
                checkForPotentialMerges(listOfPlayers.get(i));
            }

            for (Player allPlayers : listOfPlayers) {
                checkForDeadUnits(allPlayers);
                checkForBonuses(allPlayers);
            }

            bonus1Active = false;
            bonus1OnCooldown = false;

            buttonsPushed = true;
        }

        if (!bonus1Active) {
            if (bonus1OwningPlayer.equals(humanPlayer)) {
                bonus1Button.setBitmap(this.getGame().getAssetManager().getBitmap("bonus1Button"));
            } else {
                bonus1Button.setBitmap(this.getGame().getAssetManager().getBitmap("bonus1ButtonDisabled"));
            }
        } else {
            bonus1Button.setBitmap(this.getGame().getAssetManager().getBitmap("bonus1ButtonActive"));
        }


        bonus1Button.update(elapsedTime);
        if (bonus1Button.isPushTriggered()) {
            if (bonus1OwningPlayer.equals(humanPlayer)) {
                if (!bonus1OnCooldown) {
                    if (!bonus1Active) {
                        bonus1Active = true;
                    } else {
                        bonus1Active = false;
                    }
                }
            }
            buttonsPushed = true;
        }

        if (!bonus2Active) {
            if (bonus2OwningPlayer.equals(humanPlayer)) {
                bonus2Button.setBitmap(this.getGame().getAssetManager().getBitmap("bonus2Button"));
            } else {
                bonus2Button.setBitmap(this.getGame().getAssetManager().getBitmap("bonus2ButtonDisabled"));
            }
        } else {
            bonus2Button.setBitmap(this.getGame().getAssetManager().getBitmap("bonus2ButtonActive"));
        }

        bonus2Button.update(elapsedTime);
        if (bonus2Button.isPushTriggered()) {
            if (bonus2OwningPlayer.equals(humanPlayer)) {
                if (!bonus2OnCooldown) {
                    if (!bonus2Active) {
                        System.out.println("BONUS 2 ACTIVE");
                        bonus2Active = true;
                    } else {
                        bonus2Active = false;
                    }
                }
            }
            buttonsPushed = true;
        }

        if (!bonus3Active) {
            if (bonus3OwningPlayer.equals(humanPlayer)) {
                bonus3Button.setBitmap(this.getGame().getAssetManager().getBitmap("bonus3Button"));
            } else {
                bonus3Button.setBitmap(this.getGame().getAssetManager().getBitmap("bonus3ButtonDisabled"));
            }
        } else {
            bonus3Button.setBitmap(this.getGame().getAssetManager().getBitmap("bonus3ButtonActive"));
        }

        bonus3Button.update(elapsedTime);
        if (bonus3Button.isPushTriggered()) {
            if (bonus3OwningPlayer.equals(humanPlayer)) {
                if (!bonus3OnCooldown) {
                    if (!bonus3Active) {
                        bonus3Active = true;
                    } else {
                        bonus3Active = false;
                    }
                }
            }
        }

        if (!bonus4Active) {
            if (bonus4OwningPlayer.equals(humanPlayer)) {
                bonus4Button.setBitmap(this.getGame().getAssetManager().getBitmap("bonus4Button"));
            } else {
                bonus4Button.setBitmap(this.getGame().getAssetManager().getBitmap("bonus4ButtonDisabled"));
            }
        } else {
            bonus4Button.setBitmap(this.getGame().getAssetManager().getBitmap("bonus4ButtonActive"));
        }

        bonus4Button.update(elapsedTime);
        if (bonus4Button.isPushTriggered()) {
            if (bonus4OwningPlayer.equals(humanPlayer)) {
                if (!bonus4OnCooldown) {
                    if (!bonus4Active) {
                        bonus4Active = true;
                    } else {
                        bonus4Active = false;
                    }
                }
            }
        }

        if (!bonus5Active) {
            if (bonus5OwningPlayer.equals(humanPlayer)) {
                bonus5Button.setBitmap(this.getGame().getAssetManager().getBitmap("bonus5Button"));
            } else {
                bonus5Button.setBitmap(this.getGame().getAssetManager().getBitmap("bonus5ButtonDisabled"));
            }
        }

        bonus5Button.update(elapsedTime);
        if (bonus5Button.isPushTriggered()) {
            if (bonus5OwningPlayer.equals(humanPlayer)) {
                if (!bonus5OnCooldown) {
                    if (!bonus5Active) {
                        if (selectedTerritory != null) {
                            if (selectedTerritory.getListOfUnits().size() > 0) {
                                currentTask = "TELEPORT";
                                bonus5Active = true;
                            }
                        }
                    } else {
                        bonus5Active = false;
                    }
                }
            }
            buttonsPushed = true;
        }

        //--------------


        if (selectedTerritory != null) {
            if (!isMoveActive) {
                if (selectedTerritory.getOwner().equals(humanPlayer) && selectedTerritory.getListOfUnits().size() > 0) {
                    moveButton.setBitmap(this.getGame().getAssetManager().getBitmap("MoveButtonAvailable"));
                } else {
                    moveButton.setBitmap(this.getGame().getAssetManager().getBitmap("MoveButtonDisabled"));
                }
            } else {
                moveButton.setBitmap(this.getGame().getAssetManager().getBitmap("MoveButtonActive"));
            }

            if (!isFightActive) {
                if (selectedTerritory.getOwner().equals(humanPlayer) && selectedTerritory.getListOfUnits().size() > 0) {
                    attackButton.setBitmap(this.getGame().getAssetManager().getBitmap("FightButtonAvailable"));
                } else {
                    attackButton.setBitmap(this.getGame().getAssetManager().getBitmap("FightButtonDisabled"));
                }
            } else {
                attackButton.setBitmap(this.getGame().getAssetManager().getBitmap("FightButtonActive"));
            }

            if (selectedTerritory.getOwner().equals(humanPlayer) && selectedTerritory.getNumOfSmallUnits() >= 3) {
                mergeButton.setBitmap(this.getGame().getAssetManager().getBitmap("MergeButtonAvailable"));
            } else {
                mergeButton.setBitmap(this.getGame().getAssetManager().getBitmap("MergeButtonDisabled"));
            }
        } else {
            moveButton.setBitmap(this.getGame().getAssetManager().getBitmap("MoveButtonDisabled"));
            attackButton.setBitmap(this.getGame().getAssetManager().getBitmap("FightButtonDisabled"));
            mergeButton.setBitmap(this.getGame().getAssetManager().getBitmap("MergeButtonDisabled"));
        }

        boolean territoryTouched = false;

        for (TouchEvent touchEvent : input.getTouchEvents()) {

            Vector2 touchPos = new Vector2();
            Vector2 touchScreenPos = new Vector2(touchEvent.x, touchEvent.y);
            ViewportHelper.convertScreenPosIntoLayer(mDefaultScreenViewport, touchScreenPos, mapLayerViewport, touchPos);

            if (touchEvent.type == TouchEvent.TOUCH_SINGLE_TAP) {
                System.out.println(touchPos.x + ", Y: " + touchPos.y + "CURRENT TASK = " + currentTask);

                if (!buttonsPushed) {
                    for (Territory allTerritories : territoriesToDraw) {
                        if (touchPos.x < allTerritories.getTerritoryX() + (allTerritories.getWidth() / 2) && touchPos.x > allTerritories.getTerritoryX() - (allTerritories.getWidth() / 2) && touchPos.y < allTerritories.getTerritoryY() + (allTerritories.getHeight() / 2) && touchPos.y > allTerritories.getTerritoryY() - (allTerritories.getHeight() / 2)) {
                            territoryTouched = true;
                            timeCardIsDisplayed = 3.0f;
                            if (currentTask == null) {
                                if (bonus2Active == true) {
                                    System.out.println("BONUS 2 IS ACTIVE AND THE METHOD IS ABOUT TO BE RUN");
                                    bonus2Takeover(allTerritories, humanPlayer);
                                }
                                selectedTerritory = allTerritories;
                                System.out.println("New Territory Selected: Number of units = " + allTerritories.getListOfUnits().size());
                                System.out.println("Number of Small Units = " + allTerritories.getNumOfSmallUnits());
                                System.out.println("Number of Big Units = " + allTerritories.getNumOfBigUnits());
                                if (selectedTerritory.getOwner().equals(humanPlayer)) {
                                    System.out.println("THIS IS OWNED BY THE PLAYER");
                                } else {
                                    System.out.println("THIS IS NOT OWNED BY THE PLAYER");
                                }
                            } else if (currentTask.equals("MOVE")) {
                                if (selectedTerritory.getSurroundingTerritories().contains(allTerritories)) {
                                    if (allTerritories.getOwner() == selectedTerritory.getOwner()) {
                                        moveUnit(selectedTerritory, allTerritories, numberToMove);
                                        currentTask = null;
                                        selectedTerritory = null;
                                    }
                                }
                            } else if (currentTask.equals("ATTACK")) {
                                if (selectedTerritory.getSurroundingTerritories().contains(allTerritories)) {
                                    if (!allTerritories.getOwner().equals(selectedTerritory.getOwner())) {
                                        declareAttack(selectedTerritory, allTerritories);
                                        currentTask = null;
                                        selectedTerritory = null;
                                    }
                                }
                            } else if (currentTask.equals("TELEPORT")) {
                                if (allTerritories.getOwner() == selectedTerritory.getOwner()) {
                                    moveUnit(selectedTerritory, allTerritories, selectedTerritory.getListOfUnits().size());
                                    bonus5OnCooldown = true;
                                    bonus5Active = false;
                                }
                                currentTask = null;
                                selectedTerritory = null;
                            }

                        }
                    }
                    if (!territoryTouched) {
                        selectedTerritory = null;
                        currentTask = null;
                    }
                }


            }

            if (touchEvent.type == TouchEvent.TOUCH_SCROLL) {
                mapLayerViewport.x += touchEvent.dx * moveSpeed;
                mapLayerViewport.y -= touchEvent.dy * moveSpeed;
            }
        }


        if (timeLeftOnDice > 0) {
            timeLeftOnDice -= elapsedTime.stepTime;
        } else {
            showDice = false;
        }
    }

    /**
     * Draw the platform demo screen
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        graphics2D.clear(Color.parseColor("#315132"));

        gameBoard.draw(elapsedTime, graphics2D, mapLayerViewport, mDefaultScreenViewport);

        //BOARD
        for (int i = 0; i < territoriesToDraw.size(); i++) {
            if (territoriesToDraw.get(i).getOwner() == neutrallyOwned) {
                Sprite neutralTerritory = new Sprite(territoriesToDraw.get(i).getTerritoryX(), territoriesToDraw.get(i).getTerritoryY(),
                        50f, 50f, this.getGame().getAssetManager().getBitmap("NeutralTerritory"), this);
                neutralTerritory.draw(elapsedTime, graphics2D, mapLayerViewport, mDefaultScreenViewport);
            } else if (territoriesToDraw.get(i).getOwner() == humanPlayer) {
                Sprite playerTerritory = new Sprite(territoriesToDraw.get(i).getTerritoryX(), territoriesToDraw.get(i).getTerritoryY(),
                        50f, 50f, this.getGame().getAssetManager().getBitmap("PlayerTerritory"), this);
                playerTerritory.draw(elapsedTime, graphics2D, mapLayerViewport, mDefaultScreenViewport);
            } else if (territoriesToDraw.get(i).getOwner() == NPC1) {
                Sprite NPC1Territory = new Sprite(territoriesToDraw.get(i).getTerritoryX(), territoriesToDraw.get(i).getTerritoryY(),
                        50f, 50f, this.getGame().getAssetManager().getBitmap("NPC1Territory"), this);
                NPC1Territory.draw(elapsedTime, graphics2D, mapLayerViewport, mDefaultScreenViewport);
            } else if (territoriesToDraw.get(i).getOwner() == NPC2) {
                Sprite NPC2Territory = new Sprite(territoriesToDraw.get(i).getTerritoryX(), territoriesToDraw.get(i).getTerritoryY(),
                        50f, 50f, this.getGame().getAssetManager().getBitmap("NPC2Territory"), this);
                NPC2Territory.draw(elapsedTime, graphics2D, mapLayerViewport, mDefaultScreenViewport);
            } else if (territoriesToDraw.get(i).getOwner() == NPC3) {
                Sprite NPC3Territory = new Sprite(territoriesToDraw.get(i).getTerritoryX(), territoriesToDraw.get(i).getTerritoryY(),
                        50f, 50f, this.getGame().getAssetManager().getBitmap("NPC3Territory"), this);
                NPC3Territory.draw(elapsedTime, graphics2D, mapLayerViewport, mDefaultScreenViewport);
            } else if (territoriesToDraw.get(i).getOwner() == NPC4) {
                Sprite NPC4Territory = new Sprite(territoriesToDraw.get(i).getTerritoryX(), territoriesToDraw.get(i).getTerritoryY(),
                        50f, 50f, this.getGame().getAssetManager().getBitmap("NPC4Territory"), this);
                NPC4Territory.draw(elapsedTime, graphics2D, mapLayerViewport, mDefaultScreenViewport);
            }
        }

        Canvas canvas = new Canvas();

        for (int i = 0; i < territoriesToDraw.size(); i++) {
            territoriesToDraw.get(i).draw(elapsedTime, graphics2D, mapLayerViewport, mDefaultScreenViewport);
        }

        for (Player allPlayers : listOfPlayers) {
            for (Territory allOwnedTerritories : allPlayers.getListOfTerritories()) {
                for (Unit unitsWithinTerritory : allOwnedTerritories.getListOfUnits()) {
                    if (unitsWithinTerritory.isBigUnit()) {
                        if (unitsWithinTerritory.isExhausted()) {
                            unitsWithinTerritory.setBitmap(this.getGame().getAssetManager().getBitmap("BigUnitExhausted"));
                        } else {
                            unitsWithinTerritory.setBitmap(this.getGame().getAssetManager().getBitmap("BigUnit"));
                        }
                    } else {
                        if (unitsWithinTerritory.isExhausted()) {
                            unitsWithinTerritory.setBitmap(this.getGame().getAssetManager().getBitmap("UnitExhausted"));
                        } else {
                            unitsWithinTerritory.setBitmap(this.getGame().getAssetManager().getBitmap("Unit"));
                        }
                    }
                    unitsWithinTerritory.draw(elapsedTime, graphics2D, mapLayerViewport, mDefaultScreenViewport);
                }
            }
        }

        for (int i = 0; i < territoriesToDraw.size(); i++) {
            Paint numberPaint = new Paint();
            numberPaint.setColor(Color.parseColor("#FFFFFF"));
            numberPaint.setTextSize(60);
            numberPaint.setTextAlign(Paint.Align.CENTER);

            Vector2 territoriesVector = new Vector2(territoriesToDraw.get(i).getTerritoryX(), territoriesToDraw.get(i).getTerritoryY());
            Vector2 territoriesVectorFixed = new Vector2();
            ViewportHelper.convertLayerPosIntoScreen(mapLayerViewport, territoriesVector, mDefaultScreenViewport, territoriesVectorFixed);
            graphics2D.drawText(Integer.toString(territoriesToDraw.get(i).getListOfUnits().size()), territoriesVectorFixed.x, territoriesVectorFixed.y, numberPaint);
        }

        if (showDice) {
            if (showTripleAttackerDice) {
                attackerTripleDice1.draw(elapsedTime, graphics2D, UILayerViewport, mDefaultScreenViewport);
                attackerTripleDice2.draw(elapsedTime, graphics2D, UILayerViewport, mDefaultScreenViewport);
                attackerTripleDice3.draw(elapsedTime, graphics2D, UILayerViewport, mDefaultScreenViewport);
            } else {
                attackerDice.draw(elapsedTime, graphics2D, UILayerViewport, mDefaultScreenViewport);
            }

            if (showTripleDefenderDice) {
                defenderTripleDice1.draw(elapsedTime, graphics2D, UILayerViewport, mDefaultScreenViewport);
                defenderTripleDice2.draw(elapsedTime, graphics2D, UILayerViewport, mDefaultScreenViewport);
                defenderTripleDice3.draw(elapsedTime, graphics2D, UILayerViewport, mDefaultScreenViewport);
            } else {
                defenderDice.draw(elapsedTime, graphics2D, UILayerViewport, mDefaultScreenViewport);
            }
        }

        if (timeCardIsDisplayed > 0) {
            timeCardIsDisplayed -= elapsedTime.stepTime;
        }

        if (selectedTerritory != null) {
            if (timeCardIsDisplayed > 0.0f) {
                selectedTerritory.getRelatedCard().draw(elapsedTime, graphics2D, UILayerViewport, mDefaultScreenViewport);
            }

        }


        Paint whiteText = new Paint();
        canvas.drawPaint(whiteText);
        whiteText.setColor(Color.parseColor("#FFFFFF"));
        whiteText.setTextSize(60);
        whiteText.setTextAlign(Paint.Align.CENTER);

        UIBackgroundBottomLeftSmaller.draw(elapsedTime, graphics2D, UILayerViewport, mDefaultScreenViewport);
        UIBackgroundBottomLeft.draw(elapsedTime, graphics2D, UILayerViewport, mDefaultScreenViewport);
        UIBackgroundTopLeft.draw(elapsedTime, graphics2D, UILayerViewport, mDefaultScreenViewport);
        UIBackgroundBottomRight.draw(elapsedTime, graphics2D, UILayerViewport, mDefaultScreenViewport);
        UIBackgroundTopRight.draw(elapsedTime, graphics2D, UILayerViewport, mDefaultScreenViewport);


        //UI
        attackButton.draw(elapsedTime, graphics2D, UILayerViewport, mDefaultScreenViewport);
        moveButton.draw(elapsedTime, graphics2D, UILayerViewport, mDefaultScreenViewport);

        if (currentTask == "MOVE") {
            moveButtonMinus.draw(elapsedTime, graphics2D, UILayerViewport, mDefaultScreenViewport);

            Vector2 moveTextPos = new Vector2(UILayerViewport.getWidth() * 0.20f, UILayerViewport.getHeight() * 0.225f);
            Vector2 moveTextPosConverted = new Vector2();
            ViewportHelper.convertLayerPosIntoScreen(UILayerViewport, moveTextPos, mDefaultScreenViewport, moveTextPosConverted);
            graphics2D.drawText(Integer.toString(numberToMove), moveTextPosConverted.x, moveTextPosConverted.y, whiteText);


            moveButtonPlus.draw(elapsedTime, graphics2D, UILayerViewport, mDefaultScreenViewport);
        }

        mergeButton.draw(elapsedTime, graphics2D, UILayerViewport, mDefaultScreenViewport);
        backButton.draw(elapsedTime, graphics2D, UILayerViewport, mDefaultScreenViewport);
        settingsButton.draw(elapsedTime, graphics2D, UILayerViewport, mDefaultScreenViewport);
        cameraControlsMinus.draw(elapsedTime, graphics2D, UILayerViewport, mDefaultScreenViewport);
        cameraControlsPlus.draw(elapsedTime, graphics2D, UILayerViewport, mDefaultScreenViewport);

        Vector2 zoomTextPos = new Vector2(UILayerViewport.getWidth() * 0.125f, UILayerViewport.getHeight() * 0.90f);
        Vector2 zoomTextPosConverted = new Vector2();
        ViewportHelper.convertLayerPosIntoScreen(UILayerViewport, zoomTextPos, mDefaultScreenViewport, zoomTextPosConverted);
        graphics2D.drawText(Integer.toString(100 + (100 - (int) (cameraMagnification * 100))) + "%", zoomTextPosConverted.x, zoomTextPosConverted.y, whiteText);

        endTurnButton.draw(elapsedTime, graphics2D, UILayerViewport, mDefaultScreenViewport);
        bonus1Button.draw(elapsedTime, graphics2D, UILayerViewport, mDefaultScreenViewport);
        bonus2Button.draw(elapsedTime, graphics2D, UILayerViewport, mDefaultScreenViewport);
        bonus3Button.draw(elapsedTime, graphics2D, UILayerViewport, mDefaultScreenViewport);
        bonus4Button.draw(elapsedTime, graphics2D, UILayerViewport, mDefaultScreenViewport);
        bonus5Button.draw(elapsedTime, graphics2D, UILayerViewport, mDefaultScreenViewport);


        Paint player = new Paint();
        canvas.drawPaint(player);
        player.setColor(Color.parseColor("#99D9EA"));
        player.setTextSize(60);
        player.setTextAlign(Paint.Align.RIGHT);

        Vector2 namesLayerPosition = new Vector2(UILayerViewport.getWidth() * 1.01f, UILayerViewport.getHeight() * 0.75f);
        Vector2 names = new Vector2();
        ViewportHelper.convertLayerPosIntoScreen(UILayerViewport, namesLayerPosition, mDefaultScreenViewport, names);
        graphics2D.drawText(humanPlayer.getName() + "(" + humanPlayer.getListOfTerritories().size() + ")", names.x, names.y, player);

        if (listOfPlayers.contains(NPC1)) {
            Paint NPC1Name = new Paint();
            canvas.drawPaint(NPC1Name);
            NPC1Name.setColor(Color.parseColor("#FFAEC9"));
            NPC1Name.setTextSize(60);
            NPC1Name.setTextAlign(Paint.Align.RIGHT);

            Vector2 NPC1LayerPosition = new Vector2(UILayerViewport.getWidth() * 1.01f, UILayerViewport.getHeight() * 0.68f);
            Vector2 NPC1POS = new Vector2();
            ViewportHelper.convertLayerPosIntoScreen(UILayerViewport, NPC1LayerPosition, mDefaultScreenViewport, NPC1POS);
            graphics2D.drawText(NPC1.getName() + "(" + NPC1.getListOfTerritories().size() + ")", NPC1POS.x, NPC1POS.y, NPC1Name);
        }

        if (listOfPlayers.contains(NPC2)) {
            Paint NPC2Name = new Paint();
            canvas.drawPaint(NPC2Name);
            NPC2Name.setColor(Color.parseColor("#B5E61D"));
            NPC2Name.setTextSize(60);
            NPC2Name.setTextAlign(Paint.Align.RIGHT);

            Vector2 NPC2LayerPosition = new Vector2(UILayerViewport.getWidth() * 1.01f, UILayerViewport.getHeight() * 0.61f);
            Vector2 NPC2POS = new Vector2();
            ViewportHelper.convertLayerPosIntoScreen(UILayerViewport, NPC2LayerPosition, mDefaultScreenViewport, NPC2POS);
            graphics2D.drawText(NPC2.getName() + "(" + NPC2.getListOfTerritories().size() + ")", NPC2POS.x, NPC2POS.y, NPC2Name);
        }

        if (listOfPlayers.contains(NPC3)) {
            Paint NPC3Name = new Paint();
            canvas.drawPaint(NPC3Name);
            NPC3Name.setColor(Color.parseColor("#FBEE00"));
            NPC3Name.setTextSize(60);
            NPC3Name.setTextAlign(Paint.Align.RIGHT);

            Vector2 NPC3LayerPosition = new Vector2(UILayerViewport.getWidth() * 1.01f, UILayerViewport.getHeight() * 0.54f);
            Vector2 NPC3POS = new Vector2();
            ViewportHelper.convertLayerPosIntoScreen(UILayerViewport, NPC3LayerPosition, mDefaultScreenViewport, NPC3POS);
            graphics2D.drawText(NPC3.getName() + "(" + NPC3.getListOfTerritories().size() + ")", NPC3POS.x, NPC3POS.y, NPC3Name);
        }

        if (listOfPlayers.contains(NPC4)) {
            Paint NPC4Name = new Paint();
            canvas.drawPaint(NPC4Name);
            NPC4Name.setColor(Color.parseColor("#FF8000"));
            NPC4Name.setTextSize(60);
            NPC4Name.setTextAlign(Paint.Align.RIGHT);

            Vector2 NPC4LayerPosition = new Vector2(UILayerViewport.getWidth() * 1.01f, UILayerViewport.getHeight() * 0.47f);
            Vector2 NPC4POS = new Vector2();
            ViewportHelper.convertLayerPosIntoScreen(UILayerViewport, NPC4LayerPosition, mDefaultScreenViewport, NPC4POS);
            graphics2D.drawText(NPC4.getName() + "(" + NPC4.getListOfTerritories().size() + ")", NPC4POS.x, NPC4POS.y, NPC4Name);
        }


    }

}