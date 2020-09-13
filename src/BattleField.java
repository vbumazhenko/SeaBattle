import java.util.Arrays;

public class BattleField {

    // Основное игровое поле
    private final Unit[][] field;

    // Для хранения количества оставшихся кораблей
    // Индекс 0: 4-палубный, индекс 1: 3-х палубный ...
    private final int[] ships;

    public BattleField() {
        field = new Unit[10][10];
        for (Unit[] unitLine : field) {
            Arrays.fill(unitLine, Unit.EMPTY);
        }
        ships = new int[4];
    }

    public Unit[][] getField() {
        return field;
    }

    // Возвращает количество оставшихся N-палубных кораблей
    public int getCountShip(int deck) {
        return ships[ships.length - deck];
    }

    // Добавляет N-палубный корабль с переданными координатами, проверяя его валидность
    public void addShip(String coordinates, int deck) {

        String errorFormat = "Неверный формат координат корабля";
        String[] decks = coordinates.split(";");
        if (decks.length != deck) {
            System.out.println(errorFormat);
            return;
        }

        Point[] points = new Point[deck];
        for (int i = 0; i < decks.length; i++) {
            if (!decks[i].matches("[0-9],[0-9]")) {
                System.out.println(errorFormat);
                return;
            }
            String[] xy = decks[i].split(",");
            points[i] = new Point(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));
        }

        if (!isValidShip(points)) {
            System.out.println("Невозможно разместить судно по введенным координатам");
            return;
        }

        // Все проверки пройдены, можно размещать судно на поле
        for (Point point : points) {
            setUnit(Unit.SHIP, point.getX(), point.getY());
            setOreol(point.getX(), point.getY());
        }

        ships[ships.length - deck]++;
    }

    // Возвращает true, если корабль можно разместить по заданному набору координат
    private boolean isValidShip(Point[] points) {

        // Проверка формы и целостности корабля
        if (!isValidShape(points)) {
            return false;
        }

        // Проверка пересечения кораблей на поле
        for (Point point : points) {
            if (getUnit(point.getX(), point.getY()) != Unit.EMPTY) {
                return false;
            }
        }

        return true;
    }

    // Проверка формы и целостности корабля
    private boolean isValidShape(Point[] points) {
        int minX = 9, minY = 9;
        int maxX = 0, maxY = 0;
        for (Point point : points) {
            minX = Math.min(minX, point.getX());
            minY = Math.min(minY, point.getY());
            maxX = Math.max(maxX, point.getX());
            maxY = Math.max(maxY, point.getY());
        }

        return minX == maxX && maxY - minY + 1 == points.length
                || minY == maxY && maxX - minX + 1 == points.length;
    }

    // Возвращает количество N-палубных кораболей, необходимых для полноной комплектации
    public int getNeedShip(int deck) {
        return ships.length - deck + 1 - getCountShip(deck);
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
    public void setOreol(int x, int y) {

        Point[] points = new Point[8];
        points[0] = new Point(x - 1, y - 1);
        points[1] = new Point(x, y - 1);
        points[2] = new Point(x + 1, y - 1);
        points[3] = new Point(x - 1, y);
        points[4] = new Point(x + 1, y);
        points[5] = new Point(x - 1, y + 1);
        points[6] = new Point(x, y + 1);
        points[7] = new Point(x + 1, y + 1);

        for (Point point : points) {
            if (point.getX() >= 0
                    && point.getX() < 10
                    && point.getY() >= 0
                    && point.getY() < 10
                    && getUnit(point.getX(), point.getY()) == Unit.EMPTY) {
                setUnit(Unit.OREOL, point.getX(), point.getY());
            }
        }

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

}
