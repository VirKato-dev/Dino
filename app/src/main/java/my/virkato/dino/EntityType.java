package my.virkato.dino;

public enum EntityType {
    PLAYER_NORMAL(10, new int[] {R.drawable.idle, R.drawable.idle2}),
    ENEMY_NORMAL(10000, new int[] {R.drawable.fire}), // если кадр всего один, то и обновлять его не нужно
    BONUS_NORMAL(10000, new int[] {R.drawable.bonus});

    int[] id;
    int delay;

    EntityType(int delay, int[] id) {
        this.id = id;
        this.delay = delay; // количество тиков задержки анимации
    }

    int getPhases() {
        return id.length;
    }
}
