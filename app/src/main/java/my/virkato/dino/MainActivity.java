package my.virkato.dino;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    FrameLayout l_frame;
    Timer timer = new Timer();
    TimerTask loop;
    PlayerEntity player;
    int delay = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        l_frame = findViewById(R.id.l_frame);
        l_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.jump();
            }
        });
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

                        player.fly(); // управление полётом
                        player.applyType(); // смена картинки анимации игрока

                        addNewEntity(); // ставим нового противника на конвейер

                        int n = l_frame.getChildCount();
                        for (int i = n-1; i > 0; i--) {
                            View view = l_frame.getChildAt(i);
                            Object tag = view.getTag();
                            if (tag != null) {
                                if (tag instanceof EnemyEntity) { // нужны толькко теги врагов
                                    ((EnemyEntity) view.getTag()).applyType(); // смена картинки анимации врага
                                    if (((EnemyEntity) view.getTag()).move()) {
                                        System.out.println("break");
                                        timer.cancel();
                                        break;
                                    }
                                }
                                if (tag instanceof BonusEntity) { // нужны толькко теги врагов
                                    ((BonusEntity) view.getTag()).applyType(); // смена картинки анимации врага
                                    if (((BonusEntity) view.getTag()).move()) {
                                        System.out.println("catch");
                                        break;
                                    }
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
        player = new PlayerEntity(this);
        player.addEntityTo(l_frame);
    }

    void createEnemy() {
        Random random = new Random();
        if (random.nextBoolean()) { // орёл или решка )
            EnemyEntity enemy;
            enemy = new EnemyEntity(this);
            enemy.setSpeed(5);
            enemy.addEntityTo(l_frame);
        } else {
            BonusEntity bonus;
            bonus = new BonusEntity(this);
            bonus.setSpeed(5);
            bonus.addEntityTo(l_frame);
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