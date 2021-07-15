package my.virkato.dino;

import android.content.Context;

public class PlayerEntity extends GameEntity {

    private float gravity_force = 0.9f;
    private float jump_force = -9999f;
    private boolean fly = false;

    public PlayerEntity(Context context) {
        super(context);
        setSize(100,100);
    }

    public void fly() {
        if (fly) {
            if (jump_force!=-9999) {
                image.setTranslationY(image.getTranslationY() - jump_force);
                jump_force -= gravity_force;
            }
            if (image.getTranslationY() >= 0) {
                image.setTranslationY(0);
                jump_force = -9999f;
                fly = false;
            }
        }
    }

    public void jump() {
        if (!fly) {
            fly = true;
            jump_force = 25f;
        }
    }

}
