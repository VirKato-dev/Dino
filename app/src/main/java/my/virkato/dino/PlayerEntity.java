package my.virkato.dino;

import android.content.Context;

public class PlayerEntity extends GameEntity {

    private float jump_force = Const.MIN_FORCE;
    private boolean fly = false;

    public PlayerEntity(Context context) {
        super(context);
        setSize(64,96);
        image.setTag("player");
        setType(EntityType.PLAYER_NORMAL);
    }

    /**
     * движение игрока - только полёт в прыжке
     */
    @Override
    public void move() {
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

    /**
     * стартовый импульс прыжка и озвучка
     */
    public void jump() {
        if (!fly) {
            playSound(JUMP);
            jump_force = Const.JUMP_FORCE;
            fly = true;
        }
    }

}
