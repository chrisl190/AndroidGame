package uk.ac.qub.eeecs.game.actualGame;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.preference.PreferenceManager;

import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.Sprite;

    /*Created by Chris
    Used token as an example
     */

public class RulesScreen extends GameScreen {

    private PushButton mTheRules, mBackButton, mArrowForward, mArrowBack;
    private Sprite mRules1, mRules2, mRules3, mRules4, mRules5, mRules6;

    private Context myContext;
    public SharedPreferences mGetPreference;


    // Define the spacing that will be used to position the buttons
    private int mSpacingX, mSpacingY;

    //Used to navigate between rules pages
    private int mBoardImageCounter = 0;

    private boolean mRules = false;
    public boolean mArrowPressed = false;

    //The boolean preferencesRequired is used to help implement testing instead of using a copy constructor
    public RulesScreen(Game game, boolean preferencesRequired) {
        super("RulesScreen", game);

        loadAssets();

        //Used to share preferences between rules page and rules test page
        if (preferencesRequired) {
            myContext = mGame.getActivity();
            mGetPreference = PreferenceManager.getDefaultSharedPreferences(myContext);
        }

    }
    private void loadAssets() {
        AssetManager assetManager = mGame.getAssetManager();
        assetManager.loadAssets(
                "txt/assets/MainMenu.JSON");

        //Button Spacing which reduces magic numbers
        mSpacingX = (int)((mDefaultLayerViewport.halfWidth*2)/5);
        mSpacingY = (int)((mDefaultLayerViewport.halfHeight*2)/3);

        //Variables created to reduce the amount of magic numbers
        float x = 0.5f;
        float mButtonOffScreen = -100.0f;

        //Creating the Sprites
        mRules1 = new Sprite(this.getGame().getScreenWidth()*x, this.getGame().getScreenHeight()*0.70f, assetManager.getBitmap("Rules1"), this );
        mRules2 = new Sprite(this.getGame().getScreenWidth()*x, this.getGame().getScreenHeight()*0.90f, assetManager.getBitmap("Rules2"), this );
        mRules3 = new Sprite(this.getGame().getScreenWidth()*x, this.getGame().getScreenHeight()*0.70f, assetManager.getBitmap("Rules3"), this );
        mRules4 = new Sprite(this.getGame().getScreenWidth()*x, this.getGame().getScreenHeight()*0.75f, assetManager.getBitmap("Rules4"), this );
        mRules5 = new Sprite(this.getGame().getScreenWidth()*x, this.getGame().getScreenHeight()*0.75f, assetManager.getBitmap("Rules5"), this );
        mRules6 = new Sprite(this.getGame().getScreenWidth()*x, this.getGame().getScreenHeight()*0.75f, assetManager.getBitmap("Rules6"), this );


        mTheRules = new PushButton(mSpacingX * 4.0f, mSpacingY * 2.7f, mSpacingX/2, mSpacingY/2, "GameRules", this);
        mBackButton = new PushButton (mSpacingX * 4.5f, mSpacingY *2.7f, mSpacingX/2, mSpacingY/2, "BackButton", this);
        mArrowForward = new PushButton(mSpacingX * mButtonOffScreen, mSpacingY* mButtonOffScreen, mSpacingX/2, mSpacingY/2, "ArrowRight", this);
        mArrowBack = new PushButton(mSpacingX * mButtonOffScreen, mSpacingY*mButtonOffScreen, mSpacingX/2, mSpacingY/2, "ArrowLeft", this);

    }

    //Used for testing purposes and makes the code extensible
    public PushButton getTheRulesButton() { return mTheRules; }
    public void setTheRulesButton(PushButton button) { this.mTheRules = button; }
    public PushButton getBackButton() { return mBackButton; }
    public void setBackButton(PushButton button) { this.mBackButton = button; }
    public PushButton getArrowForward() { return mArrowForward; }
    public void setArrowForward(PushButton button) { this.mArrowForward = button; }
    public PushButton getArrowBack() { return mArrowBack; }
    public void setArrowBack(PushButton button) { this.mArrowBack = button; }

    @Override
    public void update(ElapsedTime elapsedTime) {

        mGetPreference = PreferenceManager.getDefaultSharedPreferences(myContext);
        // Process any touch events occurring since the update
        Input input = mGame.getInput();

        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {
            updateButtons(elapsedTime);
            updateButtonsWhenPressed();

        }
    }

    private void updateButtons(ElapsedTime elapsedTime){
            // Update each button and transition if needed
            mTheRules.update(elapsedTime);
            mArrowForward.update(elapsedTime);
            mArrowBack.update(elapsedTime);
            mBackButton.update(elapsedTime);
             if (mBackButton.isPushTriggered()) {
            changeToScreen(new MainMenu(mGame));
        }
    }

    private void updateButtonsWhenPressed() {
        //Increases the number when the button is pressed to change pages of rules
        if(mArrowForward.isPushTriggered()) {
            mArrowPressed = true;
            if (mBoardImageCounter < 6) {
                mBoardImageCounter++;
            } else {
                mBoardImageCounter = 6;
            }

        }
        if(mArrowBack.isPushTriggered())
        {
            mArrowPressed = true;
            if(mBoardImageCounter >0)
            {
                mBoardImageCounter--;
            }
            else {
                mBoardImageCounter = 0;
            }
        }
        //Brings the arrows onto the screen when clicked
        if(mTheRules.isPushTriggered()){

            mArrowForward.setPosition(mSpacingX * 2.7f, mSpacingY * 2.7f);
            mArrowBack.setPosition(mSpacingX * 2.0f, mSpacingY * 2.7f);
            mRules = true;
            mBoardImageCounter = 0;
        }
    }


    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        //Adds background colour and sets viewport
        graphics2D.clear(Color.parseColor("#D7FFD7"));
        graphics2D.clipRect(mDefaultScreenViewport.toRect());

        drawButtons(elapsedTime, graphics2D);
        drawRulesPages(elapsedTime, graphics2D);
    }

    private void drawButtons(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        mTheRules.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        mArrowForward.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        mArrowBack.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        mBackButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
    }

    private void drawRulesPages(ElapsedTime elapsedTime, IGraphics2D graphics2D){
        //Making a new paint so I can display text (black)
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(45.0f);

        //Setting up a second paint with a different size
        Paint paintRules = new Paint();
        paintRules.setColor(Color.BLACK);
        paintRules.setTextSize(50.0f);

        //Variables created to reduce the amount of magic numbers
        float x = 1.6f;

        //This is displayed before the game rules button is selected
        if(!mRules)
        {

            graphics2D.drawText("Are you not sure how to play?",mSpacingX * x,mSpacingY * 1.0f,paint);
            graphics2D.drawText("All you have to do is select the games rules button",mSpacingX * x,mSpacingY * 1.5f,paint);
            graphics2D.drawText("This will then guide you through the rules of",mSpacingX * x,mSpacingY * 2.0f,paint);
            graphics2D.drawText("The game step by step",mSpacingX * x,mSpacingY * 2.5f,paint);
            graphics2D.drawText("Select the games rules button ",mSpacingX * x,mSpacingY * 3.0f,paint);
            graphics2D.drawText("To get back to the first rule",mSpacingX * x,mSpacingY * 3.5f,paint);
        }
        //A switch statement which allows the different pages to be shown
        if(mRules)
        {
            switch(mBoardImageCounter)
            {
                case 0:
                    mRules1.draw(elapsedTime, graphics2D);
                    graphics2D.drawText("Object of the Game",mSpacingX *x ,mSpacingY * 2.5f,paintRules);
                    graphics2D.drawText("At the start of the game, each player selects a starting location. Each",mSpacingX *x,mSpacingY * 3.0f,paint);
                    graphics2D.drawText("player then receives a Unit to place on their starting location as well",mSpacingX *x,mSpacingY * 3.5f,paint);
                    graphics2D.drawText("as the Tech Card associated with that starting location. ",mSpacingX *x,mSpacingY * 4.0f,paint);
                    break;
                case 1:
                    mRules2.draw(elapsedTime, graphics2D);
                    graphics2D.drawText("Tech Cards", mSpacingX * x, mSpacingY * 2.5f, paintRules);
                    graphics2D.drawText("Tech Cards are cards that are proof of ownership. Each Tech Card is associated ", mSpacingX * x, mSpacingY * 3.0f, paint);
                    graphics2D.drawText("with a Territory and an overall Technology.If you gather all the Tech Cards for ", mSpacingX * x, mSpacingY * 3.5f, paint);
                    graphics2D.drawText("a Technology, you gain a bonus depending on the Technology gathered. These   ", mSpacingX * x, mSpacingY * 4.0f, paint);
                    graphics2D.drawText(" bonuses can benefit you in battle, hinder your opponent or give you better   ", mSpacingX * x, mSpacingY * 4.5f, paint);
                    graphics2D.drawText(" movement options.  ", mSpacingX * x, mSpacingY * 5.0f, paint);
                    break;
                case 2:
                    mRules3.draw(elapsedTime, graphics2D);
                    graphics2D.drawText("Moving a Unit", mSpacingX * x, mSpacingY * 2.7f, paintRules);
                    graphics2D.drawText("To move a Unit, place the Units you want to move to an adjacent allied ", mSpacingX * x, mSpacingY * 3.2f, paint);
                    graphics2D.drawText("Territory. Each Unit can only move once per turn.", mSpacingX * x, mSpacingY * 3.7f, paint);
                    break;
                case 3:

                    graphics2D.drawText("Taking Territories", mSpacingX * x, mSpacingY * 2.5f, paintRules);
                    graphics2D.drawText("To take over an empty Territory, the player rolls a dice against the Territory.", mSpacingX * x, mSpacingY * 3.0f, paint);
                    graphics2D.drawText("If the attacker’s roll exceeds the defending roll,they take over the Territory", mSpacingX * x, mSpacingY * 3.5f, paint);
                    graphics2D.drawText("and gain a Unit + the associated the Tech Card. If they do not exceed the roll,", mSpacingX * x, mSpacingY * 4.0f, paint);
                    graphics2D.drawText("nothing happens. To attack an enemy Territory, you must have Units adjacent to ", mSpacingX * x, mSpacingY * 4.5f, paint);
                    graphics2D.drawText("the Territory you want to attack. Each of your Units attempts to battle the ", mSpacingX * x, mSpacingY * 5.0f, paint);
                    graphics2D.drawText("defending Units.If the attacker defeats every single one of the defending Units,", mSpacingX * x, mSpacingY * 5.5f, paint);
                    graphics2D.drawText("that player gains the territory, a Unit and the associated Tech Card for that ", mSpacingX * x, mSpacingY * 6.0f, paint);
                    graphics2D.drawText("Territory.", mSpacingX * x, mSpacingY * 6.5f, paint);
                    break;

                case 4:
                    mRules4.draw(elapsedTime, graphics2D);
                    graphics2D.drawText("Performing Battles", mSpacingX * x, mSpacingY * 2.5f, paintRules);
                    graphics2D.drawText("To perform a battle, each player rolls a dice (typically a six-sided  dice) and", mSpacingX * x, mSpacingY * 3.0f, paint);
                    graphics2D.drawText("compares the rolls. The attacking player must exceed the dice roll of the ", mSpacingX * x, mSpacingY * 3.5f, paint);
                    graphics2D.drawText("defending player to defeat the defending Unit. A defeated Unit is removed from  ", mSpacingX * x, mSpacingY * 4.0f, paint);
                    graphics2D.drawText("the game board and is not recoverable. If the results are equal or the defend ", mSpacingX * x, mSpacingY * 4.5f, paint);
                    graphics2D.drawText("Unit’s roll is greater, the attacking Unit is defeated and removed from the game", mSpacingX * x, mSpacingY * 5.0f, paint);
                    graphics2D.drawText("board.", mSpacingX * x, mSpacingY * 5.5f, paint);
                    break;

                case 5:
                    mRules5.draw(elapsedTime, graphics2D);
                    graphics2D.drawText("Upgrading Units", mSpacingX * x, mSpacingY * 2.7f, paintRules);
                    graphics2D.drawText("Players can replace 3 Units on a single tile with a Large Unit.Large Units can roll", mSpacingX * x, mSpacingY * 3.2f, paint);
                    graphics2D.drawText("3 dice in an attempt to win a battle. If any of the three dice exceed the defending", mSpacingX * x, mSpacingY * 3.7f, paint);
                    graphics2D.drawText("Unit’s roll, the Large Unit wins. If the Large Unit does not exceed the dice roll", mSpacingX * x, mSpacingY * 4.2f, paint);
                    graphics2D.drawText("however,the Large Unit is defeated.", mSpacingX * x, mSpacingY * 4.7f, paint);
                    break;

                default:
                    mRules6.draw(elapsedTime, graphics2D);
                    graphics2D.drawText("Eliminating a Player/Winning the Game",mSpacingX *x ,mSpacingY * 2.7f,paintRules);
                    graphics2D.drawText("A player is eliminated when all of their Units are removed from the",mSpacingX *x,mSpacingY * 3.2f,paint);
                    graphics2D.drawText("game OR they have no remaining territories. An eliminated player as no  ",mSpacingX *x,mSpacingY * 3.7f,paint);
                    graphics2D.drawText("more turns. The winner of the game is the last player standing, having ",mSpacingX *x,mSpacingY * 4.2f,paint);
                    graphics2D.drawText("all other players ",mSpacingX *x,mSpacingY * 4.7f,paint);
                    break;
            }

        }

    }

    //Used for testing and helps make the back buttons more efficient
    public void changeToScreen(GameScreen screen) {
        mGame.getScreenManager().removeScreen(this.getName());
        mGame.getScreenManager().dispose();
        mGame.getScreenManager().addScreen(screen);

    }
}
