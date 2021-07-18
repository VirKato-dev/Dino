package my.virkato.dino;

import android.widget.ImageView;

/**
 * проводок соединяющий главный код с объектом для получения информации о столкновении
 */
public interface OnEvent {
    void action(ImageView img, Events event);
}
