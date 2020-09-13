public enum Unit {

    EMPTY("⬜", "⬜"),
    SHIP("\uD83D\uDEE5", "⬜"),
    SHIP_DESTROY("\uD83D\uDFE5", "\uD83D\uDFE5"),
    OREOL("\uD83D\uDFE6", "⬜"),
    VISIBLE_OREOL("\uD83D\uDFE6", "\uD83D\uDFE6"),
    SHOT("X", "X");

    private final String visible;
    private final String hidden;

    Unit(String visible, String hidden) {
        this.visible = visible;
        this.hidden = hidden;
    }

    public String getVisible() {
        return visible;
    }

    public String getHidden() {
        return hidden;
    }
}
