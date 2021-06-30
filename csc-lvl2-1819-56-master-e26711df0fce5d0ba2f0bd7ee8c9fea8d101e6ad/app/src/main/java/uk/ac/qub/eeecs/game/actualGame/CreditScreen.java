package uk.ac.qub.eeecs.game.actualGame;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import java.util.Random;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.util.CollisionDetector;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.Sprite;

public class CreditScreen extends GameScreen {
    /*Created by Chris
    Used gage as an example
     */
    private String mLine1, mLine2, mLine3, mLine4, mLine5, mLine6;
    private Paint mTextPaint = new Paint();
    private AssetManager mAssetManager = mGame.getAssetManager();
    private int mCount = 0;
    private Sprite mCredits;
    private PushButton mBackButton;

    //This defines the number of explosions which appear on the screen
    private final static int mNUM_ROWS = 2;
    private final static int mNUM_IN_ROW = 3;

    //Calls the Animation class
    private AnimationDemo[][] mExplosion = new AnimationDemo[mNUM_ROWS][mNUM_IN_ROW];


    public CreditScreen(Game game) {
        super("CreditScreen", game);
        loadAssets();
        loadAnimation();
    }

    private void loadAssets() {
        mAssetManager.loadAssets(
                "txt/assets/CreditScreen.JSON");
        //Creating the strings with the names to be loaded in
        mLine1 = "Created by: ";
        mLine2 = "Christopher Logan";
        mLine3 = "Anna Keenan";
        mLine4 = "Nathan Gilpin";
        mLine5 = "Robbie Connor";
        mLine6 = "Patrick McClintock";

        mCredits = new Sprite(this.getGame().getScreenWidth()*0.5f, this.getGame().getScreenHeight()*0.10f, mAssetManager.getBitmap("Credits1"), this );

        mAssetManager.loadAndAddBitmap("Credits1", "Credits1.png");
        mAssetManager.loadAndAddBitmap("C", "C.png");
        mAssetManager.loadAndAddBitmap("CR", "CR.png");
        mAssetManager.loadAndAddBitmap("CRE", "CRE.png");
        mAssetManager.loadAndAddBitmap("CRED", "CRED.png");
        mAssetManager.loadAndAddBitmap("CREDI", "CREDI.png");
        mAssetManager.loadAndAddBitmap("CREDIT", "CREDIT.png");

        mAssetManager.loadAssets(
                "txt/assets/AssetDemoScreenAssets.JSON");

        //Creating the x and y values for buttons screen positioning which reduces magic numbers
        int spacingX = (int) mDefaultLayerViewport.getWidth() / 5;
        int spacingY = (int) mDefaultLayerViewport.getHeight() / 5;

        mBackButton = new PushButton(
                spacingX * 4f, spacingY * 1f, spacingX, spacingY,
                "BackButton", "BackButtonSelected", this);
        mBackButton.setPlaySounds(true, true);

    }

    private void loadAnimation() {
        float explosionHeight = mDefaultLayerViewport.getHeight() / mNUM_ROWS;
        float explosionWidth = explosionHeight * 0.66f;
        float explosionSpacing = mDefaultLayerViewport.getWidth() / (mNUM_IN_ROW + 1);

        Random random = new Random();
        for (int rowIdx = 0; rowIdx < mNUM_ROWS; rowIdx++)
            for (int explosionIdx = 0; explosionIdx < mNUM_IN_ROW; explosionIdx++){

                AnimationDemo explosion = new AnimationDemo(
                        explosionSpacing * (explosionIdx + 1),
                        explosionHeight * (0.5f + rowIdx),
                        explosionWidth, explosionHeight, this);

                explosion.setFacing(random.nextBoolean() ?
                        AnimationDemo.explosionFacing.LEFT : AnimationDemo.explosionFacing.RIGHT);

                mExplosion[rowIdx][explosionIdx] = explosion;
            }
    }
    //Used for testing purposes and makes the code extensible
    public PushButton getBackButton() { return mBackButton; }
    public void setBackButton(PushButton button) { this.mBackButton = button; }


    @Override
    public void update(ElapsedTime elapsedTime) {
        mCount += 1;

        mBackButton.update(elapsedTime);
        if (mBackButton.isPushTriggered()) {
            changeToScreen(new MainMenu(mGame));
        }
        updateAnimation(elapsedTime);
    }

    private void updateAnimation(ElapsedTime elapsedTime) {
        //Uses the AnimationDemo to create the explosions
        for(int rowIdx = 0; rowIdx < mNUM_ROWS; rowIdx++) {
            for (int explosionIdx = 0; explosionIdx < mNUM_IN_ROW; explosionIdx++) {


                AnimationDemo explosion = mExplosion[rowIdx][explosionIdx];
                explosion.update(elapsedTime);


                for (int otherExplosionIdx = 0; otherExplosionIdx < mNUM_IN_ROW; otherExplosionIdx++) {

                    if (explosionIdx != otherExplosionIdx) {

                        CollisionDetector.CollisionType collisionType =
                                CollisionDetector.determineAndResolveCollision(
                                        explosion, mExplosion[rowIdx][otherExplosionIdx]);

                        if (collisionType != CollisionDetector.CollisionType.None) {
                            explosion.changeDirection();
                        }
                    }
                }

                BoundingBox explosionBound = explosion.getBound();
                if (explosionBound.getLeft() < 0) {
                    explosion.position.x -= explosionBound.getLeft();
                    explosion.changeDirection();
                } else if (explosionBound.getRight() > mDefaultLayerViewport.getWidth()) {
                    explosion.position.x -= (explosionBound.getRight() - mDefaultLayerViewport.getWidth());
                    explosion.changeDirection();
                }

            }
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        //Adds background colour and sets viewport
        graphics2D.clear(Color.parseColor("#D7FFD7"));
        graphics2D.clipRect(mDefaultScreenViewport.toRect());
        //This draws the back button
        mBackButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        drawExplosions(elapsedTime, graphics2D);
        drawNames(graphics2D);
        drawCreditsAnimation(elapsedTime, graphics2D);

    }
    private void drawExplosions(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        for (int rowIdx = 0; rowIdx < mNUM_ROWS; rowIdx++)
            for (int explosionIdx = 0; explosionIdx < mNUM_IN_ROW; explosionIdx++)
                mExplosion[rowIdx][explosionIdx].draw(
                        elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
    }

    private void drawNames(IGraphics2D graphics2D) {

        mTextPaint.setTextSize(100.0f);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);

        //This draws the names using an easily adjustment for loop which provides extensibility
        float x = 150.0f;
        int count = 0;

        for (float k = 200.0f; k < 800.0f; k += 100.0f) {
            count++;
            if (count == 1) {
                graphics2D.drawText(mLine1, x, k, mTextPaint);
            } else if (count == 2) {
                graphics2D.drawText(mLine2, x, k, mTextPaint);
            } else if (count == 3) {
                graphics2D.drawText(mLine3, x, k, mTextPaint);
            } else if (count == 4) {
                graphics2D.drawText(mLine4, x, k, mTextPaint);
            } else if (count == 5) {
                graphics2D.drawText(mLine5, x, k, mTextPaint);
            } else if (count == 6) {
                graphics2D.drawText(mLine6, x, k, mTextPaint);
            }
        }
    }

    private void drawCreditsAnimation(ElapsedTime elapsedTime, IGraphics2D graphics2D){
        mCredits.draw(elapsedTime, graphics2D);
        //Draws the credits animation using a switch statement
        switch(mCount)
        {
            case 0:
                mCredits.setBitmap(mGame.getAssetManager().getBitmap("C"));
                break;

            case 1:
                mCredits.setBitmap(mGame.getAssetManager().getBitmap("CR"));
                break;

            case 2:
                mCredits.setBitmap(mGame.getAssetManager().getBitmap("CRE"));
                break;

            case 3:
                mCredits.setBitmap(mGame.getAssetManager().getBitmap("CRED"));
                break;

            case 4:
                mCredits.setBitmap(mGame.getAssetManager().getBitmap("CREDI"));
                break;

            case 5:
                mCredits.setBitmap(mGame.getAssetManager().getBitmap("CREDIT"));
                break;

            default:
                mCredits.setBitmap(mGame.getAssetManager().getBitmap("Credits1"));
                break;
        }

    }
    //Used for testing the transitions between pages
    public void changeToScreen(GameScreen screen) {
        mGame.getScreenManager().removeScreen(this.getName());
        mGame.getScreenManager().dispose();
        mGame.getScreenManager().addScreen(screen);

    }
}
