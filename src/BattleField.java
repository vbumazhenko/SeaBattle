import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BattleField {

    // Основное игровое поле
    private final Unit[][] field;

    // Для хранения количества оставшихся кораблей
    // Индекс 0: 4-палубный, индекс 1: 3-х палубный ...
    private final int[] countShips;

    // Список кораблей на поле
    private final List<Ship> ships;

    public BattleField() {
        field = new Unit[10][10];
        for (Unit[] unitLine : field) {
            Arrays.fill(unitLine, Unit.EMPTY);
        }
        countShips = new int[4];
        ships = new ArrayList<>();
    }

    public Unit[][] getField() {
        return field;
    }

    // Возвращает количество оставшихся N-палубных кораблей
    public int getCountShip(int deck) {
        return countShips[countShips.length - deck];
    }

    // Добавляет N-палубный корабль с переданными координатами, проверяя его валидность
    public void addShip(String coordinates, int deck) {

        String errorFormat = "Неверный формат координат корабля";
        String[] decks = coordinates.split(";");
        if (decks.length != deck) {
            System.out.println(errorFormat);
            return;
        }

        List<int[]> points = new ArrayList<>();
        for (String d : decks) {
            if (!d.matches("[0-9],[0-9]")) {
                System.out.println(errorFormat);
                return;
            }
            String[] xy = d.split(",");
            points.add(new int[]{Integer.parseInt(xy[0]), Integer.parseInt(xy[1])});
        }

        if (!isValidShip(points)) {
            System.out.println("Невозможно разместить судно по введенным координатам");
            return;
        }

        // Все проверки пройдены, можно размещать судно на поле
        for (int[] point : points) {
            setUnit(Unit.SHIP, point[0], point[1]);
        }

        // Добавление сформированного судна в список кораблей игрового поля
        Ship ship = new Ship(deck);
        ship.addPoints(points);
        ships.add(ship);
        setOreol(ship, Unit.OREOL);

        countShips[countShips.length - deck]++;
    }

    // Возвращает true, если корабль можно разместить по заданному набору координат
    private boolean isValidShip(List<int[]> points) {

        // Проверка формы и целостности корабля
        if (!isValidShape(points)) {
            return false;
        }

        // Проверка пересечения кораблей на поле
        for (int[] point : points) {
            if (getUnit(point[0], point[1]) != Unit.EMPTY) {
                return false;
            }
        }

        return true;
    }

    // Проверка формы и целостности корабля
    private boolean isValidShape(List<int[]> points) {
        int minX = 9, minY = 9;
        int maxX = 0, maxY = 0;
        for (int[] point : points) {
            minX = Math.min(minX, point[0]);
            minY = Math.min(minY, point[1]);
            maxX = Math.max(maxX, point[0]);
            maxY = Math.max(maxY, point[1]);
        }

        return minX == maxX && maxY - minY + 1 == points.size()
                || minY == maxY && maxX - minX + 1 == points.size();
    }

    // Возвращает количество N-палубных кораболей, необходимых для полноной комплектации
    public int getNeedShip(int deck) {
        return countShips.length - deck + 1 - getCountShip(deck);
    }

    // Возвращает значение клетки по заданным координатам
    public Unit getUnit(int x, int y) {
        return field[y][x];
    }

    // Устанавливает значение клетки с заданными координатами
    public void setUnit(Unit unit, int x, int y) {
        field[y][x] = unit;
    }

    // Устанавливает ореол вокург корабля
    public void setOreol(Ship ship, Unit oreol) {

        for (int[] point : ship.getPoints()) {
            List<int[]> points = new ArrayList<>();
            points.add(new int[]{point[0] - 1, point[1] - 1});
            points.add(new int[]{point[0], point[1] - 1});
            points.add(new int[]{point[0] + 1, point[1] - 1});
            points.add(new int[]{point[0] - 1, point[1]});
            points.add(new int[]{point[0] + 1, point[1]});
            points.add(new int[]{point[0] - 1, point[1] + 1});
            points.add(new int[]{point[0], point[1] + 1});
            points.add(new int[]{point[0] + 1, point[1] + 1});

            for (int[] p : points) {
                if (p[0] >= 0
                        && p[0] < 10
                        && p[1] >= 0
                        && p[1] < 10
                        && (getUnit(p[0], p[1]) == Unit.EMPTY || getUnit(p[0], p[1]) == Unit.OREOL)) {
                    setUnit(oreol, p[0], p[1]);
                }
            }
        }
    }

    // Возвращает корабль, найденный по координатам
    public Optional<Ship> findShip(int x, int y) {

        Optional<Ship> result = Optional.empty();
        for (Ship ship : ships) {
            if (ship.hasPoint(x, y)) {
                result = Optional.of(ship);
            }
        }
        return result;
    }

    // Выводит на экран два игровых поля для игрока 1 (открытое) и игрока 2 (скрытое)
    public static void print(BattleField battleField1, BattleField battleField2) {

        System.out.println("Вы:                                            \t\tПротивник:");
        System.out.println("\t0\t1\t2\t3\t4\t5\t6\t7\t8\t9\t\t\t\t0\t1\t2\t3\t4\t5\t6\t7\t8\t9");
        System.out.println("\t--------------------------------------\t\t\t\t--------------------------------------");
        for (int i = 0; i < battleField1.getField().length; i++) {
            System.out.printf("%2d|\t", i);
            for (int j = 0; j < battleField1.getField()[i].length; j++) {
                System.out.print(battleField1.getField()[i][j].getVisible() + "\t");
            }
            System.out.printf("\t\t%2d|\t", i);
            for (int j = 0; j < battleField2.getField()[i].length; j++) {
                System.out.print(battleField2.getField()[i][j].getHidden() + "\t");
            }
            System.out.println();
        }
        System.out.println();

    }

    public List<Ship> getShips() {
        return ships;
    }
}
