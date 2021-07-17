package my.virkato.dino;

import android.content.Context;
import android.view.View;

public class EnemyEntity extends GameEntity{

    private int speed = 1;

    public EnemyEntity(Context context) {
        super(context);
        setSize(50,80);
        image.setTranslationX(context.getResources().getDisplayMetrics().widthPixels);
        image.setTranslationY(0 * dp);
        image.setTag(this);
        setType(EntityType.ENEMY_NORMAL);
    }

    public boolean move() {
        image.setTranslationX(image.getTranslationX() - speed);
        if (image.getTranslationX() < -image.getLayoutParams().width) {
            removeEntity();
        }
        return checkCollapse("player");
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

}
