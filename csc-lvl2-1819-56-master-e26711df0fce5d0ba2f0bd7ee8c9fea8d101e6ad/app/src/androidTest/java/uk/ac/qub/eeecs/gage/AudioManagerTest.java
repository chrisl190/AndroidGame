package uk.ac.qub.eeecs.gage;


import android.support.test.runner.AndroidJUnit4;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import org.junit.runner.RunWith;

import org.junit.Test;

import uk.ac.qub.eeecs.game.AudioManager;

//Created 100% by Chris

@RunWith(AndroidJUnit4.class)
public class AudioManagerTest {


    @Test
    public void isMusicEnabled() {
        AudioManager.setMusicEnabled(true);
        assertTrue(AudioManager.isMusicEnabled());
    }

    @Test
    public void getMusicVolume() {
        AudioManager.setMusicVolume(0.8f);
        assertEquals(0.8f, AudioManager.getMusicVolume());
    }

    @Test
    public void setMusicEnabled() {
        AudioManager.setMusicEnabled(true);
        assertTrue(AudioManager.isMusicEnabled());
    }

    @Test
    public void setMusicVolume() {
        AudioManager.setMusicVolume(0.4f);
        assertEquals(0.4f, AudioManager.getMusicVolume());
    }

    @Test
    public void setMusicVolumeMax() {
        AudioManager.setMusicVolume(0.6f);
        AudioManager.setMusicVolume(1.0f);
        assertEquals(1.0f, AudioManager.getMusicVolume());
    }

    @Test
    public void setMusicVolumeMin() {
        AudioManager.setMusicVolume(0.5f);
        AudioManager.setMusicVolume(0.0f);
        assertEquals(0.0f, AudioManager.getMusicVolume());
    }

    @Test
    public void setMusicVolumeToMax() {
        AudioManager.setMusicVolume(0.6f);
        AudioManager.setMusicVolume(1.1f);
        assertEquals(1.0f, AudioManager.getMusicVolume());
    }

    @Test
    public void setMusicVolumeToMin() {
        AudioManager.setMusicVolume(0.6f);
        AudioManager.setMusicVolume(-0.1f);
        assertEquals(0.0f, AudioManager.getMusicVolume());
    }

}
