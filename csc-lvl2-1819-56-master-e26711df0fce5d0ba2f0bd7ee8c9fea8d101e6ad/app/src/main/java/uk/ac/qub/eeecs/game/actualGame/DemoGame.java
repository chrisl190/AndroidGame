package uk.ac.qub.eeecs.game.actualGame;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.game.actualGame.SplashScreen;

    /*
    Created by Chris
    Used token as an example
     */

 //Sample demo game that is create within the MainActivity class

public class DemoGame extends Game {

     //Create a new demo game

    public DemoGame() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Go with a default 20 UPS/FPS
        setTargetFramesPerSecond(20);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Call the Game's onCreateView to get the view to be returned.
        View view = super.onCreateView(inflater, container, savedInstanceState);

        // Create and add a stub game screen to the screen manager. We don't
        // want to do this within the onCreate method as the menu screen
        // will layout the buttons based on the size of the view.
        SplashScreen stubSplashScreen = new SplashScreen("", this);

        mScreenManager.addScreen(stubSplashScreen);

        return view;
    }

    @Override
    public boolean onBackPressed() {
        // If we are already at the menu screen then exit
        if (mScreenManager.getCurrentScreen().getName().equals("MainMenu"))
            return false;

        // Go back to the menu screen
        getScreenManager().removeScreen(mScreenManager.getCurrentScreen().getName());
        MainMenu menuScreen = new MainMenu(this);
        getScreenManager().addScreen(menuScreen);
        return true;
    }
}