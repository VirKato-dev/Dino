package my.virkato.dino;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
//import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;

public abstract class GameEntity {

    protected Context context;
    protected float dp;
    protected ImageView image;
    protected ViewGroup frame;
    protected EntityType type;
    protected int phase;
    protected int phase_delay;
    private int speed = 1;

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

    /**
     * подключить проводок к этому объекту
     * @param event обработчик в главном коде (MainActivity)
     */
    protected void setOnEventListener(OnEvent event) {
        onEvent = event;
    }

    void initAudio() {
        if (soundPool == null) { // один раз для всех объектов
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build();
                soundPool = new SoundPool.Builder()
                        .setMaxStreams(4)
                        .setAudioAttributes(audioAttributes)
                        .build();
//            } else {
//                soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
            }

            // Не стоит ждать окончания загрузки
            COIN = soundPool.load(context, R.raw.coin, 0);
            JUMP = soundPool.load(context, R.raw.jump, 0);
            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
//        }
    }

    /**
     * @param type модель объекта
     */
    protected void setType(EntityType type) {
        this.type = type;
        if (type.getPhaseCount() > 0) { // на случай, если нет ни одного кадра анимации
            int res = type.id[0];
            image.setImageResource(res);
            nextPhase();
        }
    }

    /**
     * применить кадр анимации
     */
    protected void animate() {
        if (type.getPhaseCount() > 1) { // не листаем кадры, если он всего 1
            int res = type.id[phase];
            image.setImageResource(res);
            nextPhase();
        }
    }

    /**
     * изменить счётчик кадров анимации
     */
    protected void nextPhase() {
        phase_delay--;
        if (phase_delay < 0) {
            this.phase_delay = type.delay;
            phase = ++phase % type.getPhaseCount(); // [0...phase_count)
        }
    }

    /**
     * добавить картинку
     * @param frame контейнер для виджета
     */
    protected void addEntityTo(@NonNull ViewGroup frame) {
        this.frame = frame;
        frame.addView(image);
    }

    /**
     * удалить картинку и вместе с ней ссылку на объект (в теге)
     */
    protected void removeEntity() {
        frame.removeView(image);
    }

    /**
     * изменить размер картинки
     * @param width  ширина
     * @param height высота
     */
    protected void setSize(int width, int height) {
        FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams) image.getLayoutParams();
        flp.width = (int) (width * dp);
        flp.height = (int) (height * dp);
        image.setLayoutParams(flp);
    }

    /**
     * @param speed скорость текущего объекта
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * озвучка
     * @param soundId номер звука из базового класса GameEntity
     */
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

    /**
     * стандартное движение по конвейеру
     * и проверка на столкновение с игроком
     */
    protected void move() {
        image.setTranslationX(image.getTranslationX() - speed);
        if (image.getTranslationX() < -image.getLayoutParams().width) {
            removeEntity();
        }
        checkCollapse("player");
    }

    /**
     * сформировать сигнал о столкновении объектов
     * @param tag тег игрока
     */
    protected void checkCollapse(String tag) {
        View player = frame.findViewWithTag(tag); // ищем картинку игрока (он всего один)
        if (player != null) { // вдруг игрок удалён с экрана
            int pH = player.getLayoutParams().height;
            int pW = player.getLayoutParams().width;
            int pTX = (int)player.getTranslationX();
            int pTY = (int)player.getTranslationY();
            int tH = image.getLayoutParams().height;
            int tW = image.getLayoutParams().width;
            int tTX = (int)image.getTranslationX();
            int tTY = (int)image.getTranslationY();
            int padX = (int) (10 * dp);
            int padY = (int) (10 * dp);

            if (tTX + padX < pTX + pW && tTX + tW > pTX - padX) {
                if (tTY + padY < pTY + pH && tTY + tH > pTY - padY) { // столкновение
                    onEvent.action(image, collapsed()); // результат проверки столкновения передаём по проводку приёмнику
                }
            }
        }
    }

    /**
     * результат столкновения
     * @return зависит от реализации в потомке
     */
    protected Events collapsed() {
        return null;
    }

}
