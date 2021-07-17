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

        event = new OnEvent() { // создаём проводок
            @Override
            public void action(ImageView img, Events event) {
                System.out.println(event.name());
                if (event == Events.COIN_CATCH) {
                    GameEntity entity = (GameEntity)img.getTag();
                    entity.playSound(GameEntity.COIN);
                    entity.removeEntity();
                    score += 10;
                    t_score.setText(String.valueOf(score));
                }
            }
        };

        createPlayer();
        mainLoop();
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

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
                            GameEntity entity;
                            if (tag != null) {
                                if (tag instanceof GameEntity) { // нужны только теги игровых персонажей
                                    entity = ((GameEntity) tag);
                                    entity.applyType(); // смена картинки анимации
                                    entity.move(); // передвигаем
                                } else if (tag instanceof String && tag.equals("player")) {
                                    entity = player;
                                } else {
                                    entity = null;
                                }
                                if (entity != null) {
                                    entity.applyType(); // смена картинки анимации
                                    entity.move(); // передвигаем
                                }
                            }
                        }
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(loop, 0, Const.TIC);
    }

    void createPlayer() {
        // игрок всего один, поэтому храним его в отдельной переменной
        player = new PlayerEntity(this);
        player.addEntityTo(l_frame);
    }

    void createEnemy() {
        // объектов столкновения много и храним их в теге картинки
        Random random = new Random();
        if (random.nextBoolean()) { // орёл или решка )
            EnemyEntity enemy;
            enemy = new EnemyEntity(this);
            enemy.setSpeed(3);
            enemy.addEntityTo(l_frame);
            enemy.setOnEventListener(event); // подключаем проводок
        } else {
            BonusEntity bonus;
            bonus = new BonusEntity(this);
            bonus.setSpeed(3);
            bonus.addEntityTo(l_frame);
            bonus.setOnEventListener(event); // подключаем проводок
        }
        delay = 200;
    }

    void addNewEntity() {
        if (delay < 0) {
            createEnemy();
        }
        delay--;
    }
}