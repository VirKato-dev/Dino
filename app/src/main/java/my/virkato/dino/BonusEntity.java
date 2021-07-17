package my.virkato.dino;

import android.content.Context;
import android.view.View;

public class BonusEntity extends GameEntity {

    public BonusEntity(Context context) {
        super(context);
        setSize(40, 40);
        image.setTranslationX(context.getResources().getDisplayMetrics().widthPixels);
        image.setTranslationY(-120 * dp);
        image.setTag(this);
        setType(EntityType.BONUS_NORMAL);
    }

    protected void removeEntity() {
        super.removeEntity();
    }

    @Override
    protected Events collapsed() {
        return Events.COIN_CATCH;
    }
}
