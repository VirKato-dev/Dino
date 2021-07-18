package my.virkato.dino;

import android.content.Context;
import android.view.View;

public class BonusEntity extends GameEntity {

    public BonusEntity(Context context) {
        super(context);
        setSize(25, 25);
        image.setTranslationX(context.getResources().getDisplayMetrics().widthPixels);
        image.setTranslationY(-120 * dp);
        image.setTag(this);
        //image.setBackgroundColor(0xFFddff88);
        setType(EntityType.BONUS_NORMAL);

    }

    /**
     * если монетка столкнулась с игроком
     * @return COIN_CATCH
     */
    @Override
    protected Events collapsed() {
        return Events.COIN_CATCH;
    }
}
