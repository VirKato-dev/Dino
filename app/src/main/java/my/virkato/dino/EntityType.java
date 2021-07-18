package my.virkato.dino;

public enum EntityType {
    BONUS_NORMAL(2, new int[]{R.drawable.coin1, R.drawable.coin2, R.drawable.coin3,
            R.drawable.coin4, R.drawable.coin5}),
    ENEMY_NORMAL(1000, new int[]{R.drawable.cactus}), // если кадр всего один, то и обновлять его не нужно
    PLAYER_NORMAL(10, new int[]{R.drawable.step1, R.drawable.step2});

    int[] id;
    int delay;

    EntityType(int delay, int[] id) {
        this.id = id;
        this.delay = delay; // количество тиков задержки анимации
    }

    int getPhaseCount() {
        return id.length;
    }
}
