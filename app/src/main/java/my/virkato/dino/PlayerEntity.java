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

    /** движение игрока
     * реализуется только полёт в прыжке
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

    /** прыжок
     * страртовый импульс и озвучка
     */
    public void jump() {
        if (!fly) {
            fly = true;
            jump_force = Const.JUMP_FORCE;
            playSound(JUMP);
        }
    }

}
