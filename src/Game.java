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

        // Рандомный выбор текущего игрока (0 или 1)
        currentPlayer = players[new Random().nextInt(2)];

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

    private void printBattlefield(Player player) {
        if (player.getNumber() == 1) {
            BattleField.print(players[0].getBattleField(), players[1].getBattleField());
        } else {
            BattleField.print(players[1].getBattleField(), players[0].getBattleField());
        }
    }

    public void nextMove() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Игра началась! " + currentPlayer.getNumber());

        gameOver = true;
    }

    public void clearConsole() {
        for (int i = 0; i < 80; i++) {
            System.out.println();
        }
    }

//    public void nextPlayer() {
//        if (currentPlayer.getNumber() == 1) {
//            currentPlayer = players[1];
//        } else {
//            currentPlayer = players[0];
//        }
//        clearConsole();
//    }

}
