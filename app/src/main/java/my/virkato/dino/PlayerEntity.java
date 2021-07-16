package my.virkato.dino;

import android.content.Context;

public class PlayerEntity extends GameEntity {

    private float jump_force = Const.MIN_FORCE;
    private boolean fly = false;

    public PlayerEntity(Context context) {
        super(context);
        setSize(80,80);
        image.setTag("player");
        setType(EntityType.PLAYER_NORMAL);
    }

    public void fly() {
        if (fly) {
            if (jump_force != Const.MIN_FORCE) {
                image.setTranslationY(image.getTranslationY() - jump_force);
                jump_force -= Const.GRAVITY;
            }
            if (image.getTranslationY() >= 0) {
                image.setTranslationY(0);
                jump_force = Const.MIN_FORCE;
                fly = false;
            }
        }
    }

    public void jump() {
        if (!fly) {
            fly = true;
            jump_force = Const.JUMP_FORCE;
            playSound();
        }
    }

    @Override
    protected void playSound() {
        float curVolume = audioManager.getStreamVolume(STREAM_MUSIC);
        float maxVolume = audioManager.getStreamMaxVolume(STREAM_MUSIC);
        float leftVolume = curVolume / maxVolume;
        float rightVolume = curVolume / maxVolume;
        int priority = 1;
        int no_loop = 0;
        float normal_playback_rate = 1f;
        streamId = soundPool.play(JUMP, leftVolume, rightVolume, priority, no_loop, normal_playback_rate);
    }

}
