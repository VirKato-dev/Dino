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
    protected ViewGroup frame;
    protected EntityType type;
    protected int phase;

    protected GameEntity(Context context) {
        dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
        this.context = context;
        image = new ImageView(context);
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);

        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        flp.gravity = Gravity.BOTTOM | Gravity.START;
        image.setLayoutParams(flp);
        phase = 0;
    }

    protected void setType(EntityType type) {
        this.type = type;
        applyType();
    }

    protected void applyType() {
        int res = type.id[phase];
        image.setImageResource(res);
        nextPhase();
    }

    protected void nextPhase() {
        phase = ++phase % type.getPhases();
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
