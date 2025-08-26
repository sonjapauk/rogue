package org.example;

import javax.sound.sampled.*;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SoundPlayer {

    private static final List<Clip> activeClips = new CopyOnWriteArrayList<>();

    public static void playLoop(String path, float volume) {
        final float adjustedVolume = Math.max(volume, 0.0001f);

        Thread thread = new Thread(() -> {
            try {
                InputStream audioSrc = SoundPlayer.class.getResourceAsStream(path);
                if (audioSrc == null) {
                    System.err.println("Audio file not found: " + path);
                    return;
                }

                AudioInputStream audioIn = AudioSystem.getAudioInputStream(audioSrc);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);

                if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    float dB = (float) (Math.log10(adjustedVolume) * 20);
                    gainControl.setValue(dB);
                }

                clip.loop(Clip.LOOP_CONTINUOUSLY);
                activeClips.add(clip);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    public static void playOnce(String path, float volume) {
        final float adjustedVolume = Math.max(volume, 0.0001f);

        Thread thread = new Thread(() -> {
            try {
                InputStream audioSrc = SoundPlayer.class.getResourceAsStream(path);
                if (audioSrc == null) {
                    System.err.println("Audio file not found: " + path);
                    return;
                }

                AudioInputStream audioIn = AudioSystem.getAudioInputStream(audioSrc);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);

                if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    float dB = (float) (Math.log10(adjustedVolume) * 20);
                    gainControl.setValue(dB);
                }

                clip.start();
                activeClips.add(clip);

                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                        activeClips.remove(clip);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    public static void handleSoundEvents(List<GameEvent> events) {
        for (GameEvent event : events) {
            if (!event.getSound()) {
                switch (event.getCurrentEvent()) {
                    case TREASURE:
                        playOnce("/music/TreasureSound.wav", 0.5f);
                        break;
                    case AGILITY_ELIXIR, MAX_HEALTH_ELIXIR, STRENGTH_ELIXIR,
                            AGILITY_SCROLL, MAX_HEALTH_SCROLL, STRENGTH_SCROLL,
                            APPLE, BREAD, MEAT,
                            AXE, STICK, KNIFE, HAMMER:
                        playOnce("/music/ItemSound.wav", 0.5f);
                        break;
                    case HERO_HIT_SUCCESS, ENEMY_HIT_SUCCESS:
                        playOnce("/music/hitSound.wav", 0.2f);
                        break;
                    case HERO_HIT_FAIL, ENEMY_HIT_FAIL:
                        playOnce("/music/missSound.wav", 0.2f);
                        break;
                    case KILL_ENEMY:
                        playOnce("/music/deadSound2.wav", 0.2f);
                        playOnce("/music/deadSound1.wav", 0.2f);
                        break;
                    default:
                        break;
                }

                event.setSound(true);
            }
        }
    }

    public static void soundClose() {
        for (Clip clip : activeClips) {
            clip.stop();
            clip.close();
        }

        activeClips.clear();
    }
}


