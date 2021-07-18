package my.virkato.dino;

import android.content.Context;
import android.view.View;

public class EnemyEntity extends GameEntity {

    public EnemyEntity(Context context) {
        super(context);
        setSize(48, 72);
        image.setTranslationX(context.getResources().getDisplayMetrics().widthPixels);
        image.setTranslationY(0 * dp);
        image.setTag(this);
        //image.setBackgroundColor(0xFFffdddd);
        setType(EntityType.ENEMY_NORMAL);
    }

    /**
     * если враг столкнулся с игроком
     * @return ENEMY_COLLAPSE
     */
    @Override
    protected Events collapsed() {
        return Events.ENEMY_COLLAPSE;
    }

}
