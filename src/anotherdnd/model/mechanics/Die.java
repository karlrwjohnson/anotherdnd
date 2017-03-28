package anotherdnd.model.mechanics;

public enum Die {
    // "D1" folds flat bonus calculations into dice data structures
    D1 (1) {
        public int roll() { return 1; }
        public int roll(int n) { return n; }
    },
    D2 (2),
    D3 (3),
    D4 (4),
    D6 (6),
    D8 (8),
    D10 (10),
    D12 (12),
    D20 (20),
    D100 (100);

    public final int size;
    public final String label;

    Die(int size) {
        this.size = size;
        if (size > 1) {
            this.label = "d" + size;
        } else {
            this.label = "";
        }
    }

    public int roll() {
        return (int) (Math.random() * size + 1);
    }

    public int roll(int n) {
        int total = 0;
        for (int i = 0; i < n; i++) {
            total += (int) (Math.random() * size + 1);
        }
        return total;
    }
}
