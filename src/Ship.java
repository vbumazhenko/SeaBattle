import java.util.ArrayList;
import java.util.List;

public class Ship {

    private final List<int[]> points;
    private int currentDecks;

    public Ship(int deck) {
        points = new ArrayList<>();
        currentDecks = deck;
    }

    public void addPoints(List<int[]> points) {
        this.points.addAll(points);
    }

    public boolean isDead() {
        return currentDecks == 0;
    }

    public boolean hasPoint(int x, int y) {
        for (int[] point : points) {
            if (point[0] == x && point[1] == y) {
                return true;
            }
        }
        return false;
    }

    public void removeDeck() {
        currentDecks = Math.max(0, currentDecks - 1);
    }

    public List<int[]> getPoints() {
        return points;
    }
}
