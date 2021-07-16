package my.virkato.dino;

public enum EntityType {
    PLAYER(new int[] {R.drawable.idle}),
    PLAYER_NORMAL(new int[] {R.drawable.idle, R.drawable.idle2}),
    ENEMY(new int[] {R.drawable.fire}),
    ENEMY_NORMAL(new int[] {R.drawable.fire}),
    BONUS(new int[] {});

    int[] id;

    EntityType(int[] id) {
        this.id = id;
    }

    int getPhases() {
        return id.length;
    }
}
