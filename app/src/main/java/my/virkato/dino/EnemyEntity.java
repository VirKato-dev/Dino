package my.virkato.dino;

import android.content.Context;
import android.view.View;

public class EnemyEntity extends GameEntity {

    public EnemyEntity(Context context) {
        super(context);
        setSize(50, 80);
        image.setTranslationX(context.getResources().getDisplayMetrics().widthPixels);
        image.setTranslationY(0 * dp);
        image.setTag(this);
        setType(EntityType.ENEMY_NORMAL);
    }

    @Override
    protected Events collapsed() {
        return Events.ENEMY_COLLAPSE;
    }

}
