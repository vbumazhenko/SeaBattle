import java.util.Random;
import java.util.Scanner;

public class Game {

    private final Player[] players;
    private Player currentPlayer;
    private boolean gameOver;

    public Game() {
        gameOver = false;
        players = new Player[2];
        players[0] = new Player(1);
        players[1] = new Player(2);
    }

    public void start() {

        // Ввод данных о кораблях для каждого из игроков
        for (Player player : players) {
            System.out.println("Игрок " + player.getNumber());
            enterShipData(player,4, "формат: x,y;x,y;x,y;x,y");
            enterShipData(player,3, "формат: x,y;x,y;x,y");
            enterShipData(player,2, "формат: x,y;x,y");
            enterShipData(player,1, "формат: x,y");
            clearConsole();
        }

        // ++TEST
//        for (Player player : players) {
//            player.getBattleField().addShip("1,1;1,2;1,4;1,3", 4);
//            player.getBattleField().addShip("9,5;8,5;7,5", 3);
//            player.getBattleField().addShip("9,7;8,7;7,7", 3);
//            player.getBattleField().addShip("9,3;8,3", 2);
//            player.getBattleField().addShip("4,0;5,0", 2);
//            player.getBattleField().addShip("4,2;5,2", 2);
//            player.getBattleField().addShip("9,0", 1);
//            player.getBattleField().addShip("0,9", 1);
//            player.getBattleField().addShip("3,7", 1);
//            player.getBattleField().addShip("5,9", 1);
//        }
//        clearConsole();
        // --TEST

        // Рандомный выбор текущего игрока (0 или 1)
        currentPlayer = players[new Random().nextInt(2)];

        System.out.println("Первый ход делает игрок " + currentPlayer.getNumber());
        System.out.println("Для продолжения нажмите ENTER...");
        new Scanner(System.in).nextLine();
        clearConsole();

        // Обработка хода текущего игрока
        while (!gameOver) {
            nextMove();
        }

    }

    private void enterShipData(Player player, int deck, String format) {
        Scanner scanner = new Scanner(System.in);
        while (player.getBattleField().getNeedShip(deck) > 0) {
            printBattlefield(player);
            System.out.print("Введите координаты " + deck + "-х палубного корабля (" + format + ")");
            System.out.println(" - осталось: " + player.getBattleField().getNeedShip(deck));
            String command = scanner.nextLine();
            if (command.toLowerCase().equals("поле")) {
                printBattlefield(player);
                continue;
            }
            player.getBattleField().addShip(command, deck);
        }
    }

    public void nextMove() {
        Scanner scanner = new Scanner(System.in);
        printBattlefield(currentPlayer);
        System.out.println("Игрок " + currentPlayer.getNumber() + ", ваш ход:");
        String line = scanner.nextLine();
        if (!line.matches("[0-9],[0-9]")) {
            System.out.println("Неверный формат координат");
            return;
        }

        ResultShot resultShot = currentPlayer.shot(line, getEnemy());
        System.out.println(resultShot.getDescription());

        if (getEnemy().getCountShips() == 0) {
            printBattlefield(currentPlayer);
            System.out.println("Игра окончена. Победил игрок " + currentPlayer.getNumber());
            gameOver = true;
        }

        if (resultShot == ResultShot.MISTAKE) {
            System.out.println("Ход передается следующему игроку");
            System.out.println("Для продолжения нажмите ENTER...");
            scanner.nextLine();
            nextPlayer();
        }
    }

    private void printBattlefield(Player player) {
        if (player.getNumber() == 1) {
            BattleField.print(players[0].getBattleField(), players[1].getBattleField());
        } else {
            BattleField.print(players[1].getBattleField(), players[0].getBattleField());
        }
    }

    public void clearConsole() {
        for (int i = 0; i < 80; i++) {
            System.out.println();
        }
    }

    public Player getEnemy() {
        if (currentPlayer.getNumber() == players[0].getNumber()) {
            return players[1];
        } else {
            return players[0];
        }
    }

    public void nextPlayer() {
        if (currentPlayer.getNumber() == 1) {
            currentPlayer = players[1];
        } else {
            currentPlayer = players[0];
        }
        clearConsole();
    }

}
