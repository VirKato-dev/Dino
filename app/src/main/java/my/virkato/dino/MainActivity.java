package my.virkato.dino;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    FrameLayout l_frame;
    Timer timer = new Timer();
    TimerTask loop;
    PlayerEntity player;
    EnemyEntity entity;
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

    void createPlayer() {
        player = new PlayerEntity(this);
        player.setImage(R.drawable.idle);
        player.addEntityTo(l_frame);
    }

    void mainLoop() {
        loop = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        player.fly();
                        addNewEntity();
                        if (entity.move()) {
                            System.out.println("break");
                            timer.cancel();
                        }
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(loop, 0, 10);
    }

    void createEnemy() {
        entity = new EnemyEntity(this);
        entity.setImage(R.drawable.fire);
        entity.addEntityTo(l_frame);
        entity.setSpeed(5);
        delay = 300;
    }

    void addNewEntity() {
        if (delay < 0) {
            createEnemy();
        }
        delay--;
    }
}