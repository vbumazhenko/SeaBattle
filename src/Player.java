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

    public ResultShot shot(String line, Player enemy) {

        String[] xy = line.split(",");
        int x = Integer.parseInt(xy[0]);
        int y = Integer.parseInt(xy[1]);

        if (enemy.getBattleField().getUnit(x, y) == Unit.EMPTY
                || enemy.getBattleField().getUnit(x, y) == Unit.OREOL
                || enemy.getBattleField().getUnit(x, y) == Unit.VISIBLE_OREOL
                || enemy.getBattleField().getUnit(x, y) == Unit.SHOT) {
            enemy.getBattleField().setUnit(Unit.SHOT, x, y);
            return ResultShot.MISTAKE;
        } else {
            Ship ship = enemy.getBattleField().findShip(x, y).orElseThrow();
            ship.removeDeck();
            enemy.getBattleField().setUnit(Unit.SHIP_DESTROY, x, y);
            if (ship.isDead()) {
                enemy.getBattleField().setOreol(ship, Unit.VISIBLE_OREOL);
                return ResultShot.DEAD;
            } else {
                return ResultShot.HURT;
            }
        }

    }

    public int getCountShips() {
        int count = 0;
        for (Ship ship : battleField.getShips()) {
            if (!ship.isDead()) {
                count++;
            }
        }
        return count;
    }
}
