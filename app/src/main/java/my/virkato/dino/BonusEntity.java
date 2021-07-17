package my.virkato.dino;

import android.content.Context;
import android.view.View;

public class BonusEntity extends GameEntity{

    private int speed = 1;

    public BonusEntity(Context context) {
        super(context);
        setSize(40,40);
        image.setTranslationX(context.getResources().getDisplayMetrics().widthPixels);
        image.setTranslationY(-80 * dp);
        image.setTag(this);
        setType(EntityType.BONUS_NORMAL);
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
