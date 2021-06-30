package uk.ac.qub.eeecs.game;

import uk.ac.qub.eeecs.gage.engine.audio.Music;
import uk.ac.qub.eeecs.gage.engine.audio.Sound;

public final class AudioManager {

    // volume limits
    private static final float MAX_MUSIC_VOLUME = 1.0f;
    private static final float MIN_MUSIC_VOLUME = 0.0f;

    // audio toggling settings
    private static boolean musicEnabled = true;


    // volume settings (range 0-1)
    private static float musicVolume = 0.8f;


    /**
     * Returns whether music is currently enabled.
     *
     * @return true if music currently enabled, false otherwise
     */
    public static boolean isMusicEnabled() {
        return musicEnabled;
    }


    /**
     * Returns current music volume.
     *
     * @return current music volume
     */
    public static float getMusicVolume() {
        return musicVolume;
    }


    /**
     * Enable or disable music.
     *
     * @param musicEnabled whether music should be enabled
     */
    public static void setMusicEnabled(boolean musicEnabled) {
        AudioManager.musicEnabled = musicEnabled;
    }

    /**
     * Set the music volume.
     *
     * @param musicVolume music volume to be set
     */
    public static void setMusicVolume(float musicVolume) {

        if (musicVolume >= MAX_MUSIC_VOLUME) {
            AudioManager.musicVolume = MAX_MUSIC_VOLUME;
        } else if (musicVolume <= MIN_MUSIC_VOLUME) {
            AudioManager.musicVolume = MIN_MUSIC_VOLUME;
        } else {
            AudioManager.musicVolume = musicVolume;
        }

    }


    /**
     * Play a given music if music enabled and it is not already playing.
     *
     * @param music music to be played
     */
    public static void playMusic(Music music) {

        if (music != null && musicEnabled && !music.isPlaying()) {
            music.setLopping(true);
            music.setVolume(musicVolume);
            music.play();
        }
    }

    /**
     * Stop given music if it is currently playing.
     *
     * @param music music to be stopped
     */
    public static void stopMusic(Music music) {

        if (music != null && music.isPlaying())
            music.stop();
    }

}
