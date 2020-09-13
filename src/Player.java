public class Player {

    private final int number;
    private final BattleField battleField;

    public Player(int number) {
        this.number = number;
        battleField = new BattleField();
    }

    public int getNumber() {
        return number;
    }

    public BattleField getBattleField() {
        return battleField;
    }

}
