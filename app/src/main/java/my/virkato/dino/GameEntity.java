package my.virkato.dino;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

public abstract class GameEntity {

    protected Context context;
    protected float dp;
    protected ImageView image;
    protected EntityType type;
    protected ViewGroup frame;
    protected int style;

    protected GameEntity(Context context) {
        dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
        this.context = context;
        image = new ImageView(context);
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);

        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        flp.gravity = Gravity.BOTTOM | Gravity.START;
        image.setLayoutParams(flp);
    }

    protected void setStyle(int style) {
        this.style = style;
        int res = R.drawable.ic_launcher_foreground;
        switch (style) {
            case Const.PLAYER_NORMAL:
                res = R.drawable.idle;
                break;
            case Const.ENEMY_NORMAL:
                res = R.drawable.fire;
                break;
        }
        image.setImageResource(res);
    }

    protected void setType(EntityType type) {
        this.type = type;
    }

    protected void addEntityTo(ViewGroup frame) {
        this.frame = frame;
        frame.addView(image);
    }

    protected void removeEntity() {
        frame.removeView(image);
    }

    protected void setSize(int width, int height) {
        FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams) image.getLayoutParams();
        flp.width = (int) (width * dp);
        flp.height = (int) (height * dp);
        image.setLayoutParams(flp);
    }

}
