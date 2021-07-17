package my.virkato.dino;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
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
    protected int phase_delay;

    // для звука
    protected static int COIN;
    protected static int JUMP;
    protected static SoundPool soundPool;
    protected static AudioManager audioManager;
    protected int streamId;

    protected OnEvent onEvent;


    protected GameEntity(Context context) {
        dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
        this.context = context;
        image = new ImageView(context);
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);

        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        flp.gravity = Gravity.BOTTOM | Gravity.START;
        image.setLayoutParams(flp);
        phase = 0;

        initAudio();
    }

    protected void setOnEventListener(OnEvent event) {
        onEvent = event;
    }

    void initAudio() {
        if (soundPool == null) { // один раз для всего приложения
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build();
                soundPool = new SoundPool.Builder()
                        .setMaxStreams(4)
                        .setAudioAttributes(audioAttributes)
                        .build();
            } else {
                soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
            }

            // Не стоит ждать окончания загрузки
            COIN = soundPool.load(context, R.raw.coin, 0);
            JUMP = soundPool.load(context, R.raw.jump, 0);

            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }
    }

    protected void setType(EntityType type) {
        this.type = type;
        if (type.getPhaseCount() > 0) { // на случай, если нет ни одного кадра анимации
            int res = type.id[0];
            image.setImageResource(res);
            nextPhase();
        }
    }

    protected void applyType() {
        if (type.getPhaseCount() > 1) { // не листаем кадры, если он всего 1
            int res = type.id[phase];
            image.setImageResource(res);
            nextPhase();
        }
    }

    protected void nextPhase() {
        phase_delay--;
        if (phase_delay < 0) {
            this.phase_delay = type.delay;
            phase = ++phase % type.getPhaseCount(); // [0...phase_count)
        }
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

    protected void playSound(int soundId) {
        float curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float leftVolume = curVolume / maxVolume;
        float rightVolume = curVolume / maxVolume;
        int priority = 0;
        int no_loop = 0;
        float normal_playback_rate = 1f;
        streamId = soundPool.play(soundId, leftVolume, rightVolume, priority, no_loop, normal_playback_rate);
    }

    protected void move(int speed) {
        image.setTranslationX(image.getTranslationX() - speed);
        if (image.getTranslationX() < -image.getLayoutParams().width) {
            removeEntity();
        }
        checkCollapse("player");
    }

    protected void checkCollapse(String tag) {
        View player = (View) frame.findViewWithTag(tag); // ищем картинку игрока (он всего один)
        if (player != null) { // вдруг игрок удалён с экрана
            int playerH = player.getLayoutParams().height;
            int playerW = player.getLayoutParams().width;
            int thisH = image.getLayoutParams().height;
            int thisW = image.getLayoutParams().width;
            if (image.getTranslationX() < playerW && image.getTranslationX() + thisW > 0) {
                if (thisH + image.getTranslationY() <= playerH + player.getTranslationY() &&
                        image.getTranslationY() >= player.getTranslationY()) { // столкновение
                    onEvent.action(image, collapsed()); // результат проверки столкновения отправим приёмнику
                }
            }
        }
    }

    protected Events collapsed() {
        return null;
    }

}
