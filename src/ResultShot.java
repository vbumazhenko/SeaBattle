public enum ResultShot {
    MISTAKE("Промах"),
    HURT("Ранил"),
    DEAD("Убил");

    private final String description;

    ResultShot(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
