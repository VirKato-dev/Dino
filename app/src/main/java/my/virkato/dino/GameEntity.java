package my.virkato.dino;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

public abstract class GameEntity {

    protected Context context;
    protected ImageView image;
    protected EntityType type;
    protected ViewGroup vg;
    protected float dp;


    public GameEntity(Context context) {
        dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
        this.context = context;
        image = new ImageView(context);

        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        flp.gravity = Gravity.BOTTOM | Gravity.START;
        image.setLayoutParams(flp);
    }

    protected void setImage(int id) {
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        image.setImageResource(id);
    }

    protected void setType(EntityType type) {
        this.type = type;
    }

    protected void addEntityTo(ViewGroup vg) {
        this.vg = vg;
        vg.addView(image);
    }

    protected void removeEntity() {
        vg.removeView(image);
    }

    protected void setSize(int width, int height) {
        FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams) image.getLayoutParams();
        flp.width = (int)(width * dp);
        flp.height = (int)(height * dp);
        image.setLayoutParams(flp);
    }

}
