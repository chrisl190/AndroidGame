package uk.ac.qub.eeecs.game.actualGame;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.animation.Animation;
import uk.ac.qub.eeecs.gage.engine.animation.AnimationManager;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.gage.world.Sprite;
    /*
    Created by Chris
    Used Gage as an example
     */

public class AnimationDemo extends Sprite {

    public enum explosionState {
        IDLE, SMALL, LARGE
    }

    private Map<explosionState, Float> mStateTransition = new HashMap<>();

    public enum explosionFacing {
        LEFT, RIGHT
    }

    private explosionState mCurrentState = explosionState.IDLE;

    private explosionFacing mCurrentfacing = explosionFacing.RIGHT;

    private AnimationManager mAnimationManager;


    public AnimationDemo(
            float startX, float startY, float width, float height, GameScreen gameScreen) {
        super(startX, startY, width, height, null, gameScreen);

        mStateTransition.put(explosionState.IDLE, 1.0f);
        mStateTransition.put(explosionState.SMALL, 0.5f);
        mStateTransition.put(explosionState.LARGE, 1.0f);

        mAnimationManager = new AnimationManager(this);
        mAnimationManager.addAnimation("txt/animation/explosionAnimations.JSON");
        mAnimationManager.setCurrentAnimation("explosionIdle");

    }


    public void setFacing(explosionFacing facing) {
        mCurrentfacing = facing;
        mAnimationManager.setFacing(facing == explosionFacing.LEFT ?
                Animation.Facing.Left : Animation.Facing.Right);
    }

    public void changeDirection() {
        setFacing(mCurrentfacing == AnimationDemo.explosionFacing.LEFT ? AnimationDemo.explosionFacing.RIGHT : AnimationDemo.explosionFacing.LEFT);
        velocity.x = -velocity.x;
    }

    private Random random = new Random();

    @Override
    public void update(ElapsedTime elapsedTime) {
        mAnimationManager.update(elapsedTime);
        super.update(elapsedTime);
        setState(elapsedTime);
    }

    private void setState(ElapsedTime elapsedTime) {
        float triggerProbability = mStateTransition.get(mCurrentState);
        if (random.nextFloat() < elapsedTime.stepTime * triggerProbability) {

            // Randomly pick a new (but different) state
            int newState;
            explosionState[] states = explosionState.values();
            do {
                newState = random.nextInt(states.length);
            } while (states[newState] == mCurrentState);

            mCurrentState = states[newState];

            // As the state has changed update the zombie so it is playing an
            // appropriate animation and has an appropriate velocity.
            switch (mCurrentState) {
                case IDLE:
                    velocity.x = 0;
                    mAnimationManager.setCurrentAnimation("explosionIdle");
                    mAnimationManager.play(elapsedTime);
                    break;
                case SMALL:
                    float VELOCITY = 40.0f;
                    velocity.x = mCurrentfacing == explosionFacing.RIGHT ? VELOCITY : -VELOCITY;
                    mAnimationManager.setCurrentAnimation("explosionSmall");
                    mAnimationManager.play(elapsedTime);
                    break;
                case LARGE:
                    velocity.x = 0;
                    mAnimationManager.setCurrentAnimation("explosionLarge");
                    mAnimationManager.play(elapsedTime);
                    break;

            }
        }
    }


    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D,
                     LayerViewport layerViewport, ScreenViewport screenViewport) {


        mAnimationManager.draw(elapsedTime, graphics2D, layerViewport, screenViewport);
    }
}
