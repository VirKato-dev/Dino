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
    }

    public boolean move() {
        image.setTranslationX(image.getTranslationX() - speed);
        if (image.getTranslationX()<-image.getLayoutParams().width) {
            removeEntity();
        }
        return checkCollapse();
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    boolean checkCollapse() {
        View player = (View) frame.findViewWithTag("player");
        int playerH = player.getLayoutParams().height;
        int playerW = player.getLayoutParams().width;
        int thisH = image.getLayoutParams().height;
        int thisW = image.getLayoutParams().width;
        if (image.getTranslationX() < playerW && image.getTranslationX() + thisW > 0) {
            return thisH + image.getTranslationY() <= playerH + player.getTranslationY() &&
                    image.getTranslationY() >= player.getTranslationY(); // collapse

        }
        return false;
    }
}
