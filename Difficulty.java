public enum Difficulty {
   EASY(1800, 4),
   NORMAL(1300, 5),
   HARD(1000, 6),
   INSANE(700, 10);

   public final int spawnIntervalMs;
   public final int arrowSpeed;

   private Difficulty(int var3, int var4) {
      this.spawnIntervalMs = var3;
      this.arrowSpeed = var4;
   }
}
