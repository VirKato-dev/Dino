package my.virkato.dino;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    FrameLayout l_frame;
    Timer timer = new Timer();
    TimerTask loop;
    PlayerEntity player;
    int delay = -1;

    TextView t_score;
    long score = 0;
    int score_delay = 0;

    /**
     * обработчик событий столкновения
     */
    OnEvent event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t_score = findViewById(R.id.t_score);

        l_frame = findViewById(R.id.l_frame);
        l_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player != null) player.jump();
            }
        });

        event = new OnEvent() {
            // не объект решает, что будет происходить в игре при столкновении с ним
            @Override
            public void action(ImageView img, Events event) {
                GameEntity entity = (GameEntity)img.getTag();
                switch (event) {
                    case COIN_CATCH:
                        entity.playSound(GameEntity.COIN);
                        entity.removeEntity();
                        score += 10;
                        t_score.setText(String.valueOf(score));
                        break;
                    case ENEMY_COLLAPSE:
                        player.removeEntity();
                        timer.cancel(); // прекращаем главный цикл игры
                        break;
                }
            }
        };

        player = createPlayer();
        mainLoop();
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    /**
     * добавление врагов на конвейер, их движение и подсчёт очков
     */
    void mainLoop() {
        loop = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        score_delay--;
                        if (score_delay < 0) {
                            score += 1;
                            t_score.setText(String.valueOf(score));
                            score_delay = Const.SCORE_DELAY;
                        }
                        addNewEntity(); // ставим нового противника на конвейер

                        int n = l_frame.getChildCount();
                        for (int i = n - 1; i > 0; i--) {
                            View view = l_frame.getChildAt(i);
                            Object tag = view.getTag();
                            GameEntity entity = null;
                            if (tag != null) {
                                // в окне могут быть и другие объекты
                                if (tag instanceof GameEntity) { // нужны только теги игровых персонажей
                                    entity = (GameEntity) tag;
                                } else if (tag instanceof String && tag.equals("player")) {
                                    entity = player;
                                }
                                if (entity != null) {
                                    entity.animate(); // смена картинки анимации
                                    entity.move(); // у игрока своя реализация движения
                                }
                            }
                        }
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(loop, 0, Const.TIC);
    }

    /**
     * игрок всего один, поэтому храним его в отдельной переменной
     */
    PlayerEntity createPlayer() {
        PlayerEntity player = new PlayerEntity(this);
        player.addEntityTo(l_frame);
        return player;
    }

    /**
     * объектов столкновения много и храним их в теге картинки
     */
    void createEnemy() {
        Random random = new Random();
        if (random.nextBoolean()) { // орёл или решка - всего 2 объекта пока)
            EnemyEntity enemy;
            enemy = new EnemyEntity(this);
            enemy.setSpeed(5);
            enemy.addEntityTo(l_frame);
            enemy.setOnEventListener(event);
        } else {
            BonusEntity bonus;
            bonus = new BonusEntity(this);
            bonus.setSpeed(5);
            bonus.addEntityTo(l_frame);
            bonus.setOnEventListener(event);
        }
        delay = 200; // позже усложнить
    }

    void addNewEntity() {
        if (delay < 0) {
            createEnemy();
        }
        delay--;
    }
}