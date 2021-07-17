package my.virkato.dino;

import android.widget.ImageView;

public interface OnEvent { // создаём проводок
    void action(ImageView img, Events event);
}
