import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int SIZE = 8;
    private Checker[][] checkers = new Checker[SIZE][SIZE];
    private List<BoardChangeListener> listeners = new ArrayList<>();

    public Board() {
        initializeBoard();
    }

    private void initializeBoard() {
        MoveStrategy normalMoveStrategy = new NormalCheckerMoveStrategy();
        ImageIcon checker1Icon = new ImageIcon("resources/images/checker1.png");
        ImageIcon checker2Icon = new ImageIcon("resources/images/checker2.png");

        // Inicjalizacja pionków dla gracza 1 (np. na dolnych rzędach)
        for (int i = 5; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if ((i + j) % 2 != 0) {
                    checkers[i][j] = new Checker(true, checker1Icon, normalMoveStrategy); // Gracz 1
                }
            }
        }
        // Inicjalizacja pionków dla gracza 2 (np. na górnych rzędach)
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < SIZE; j++) {
                if ((i + j) % 2 != 0) {
                    checkers[i][j] = new Checker(false, checker2Icon, normalMoveStrategy); // Gracz 2
                }
            }
        }
    }

    public void addChangeListener(BoardChangeListener listener) {
        listeners.add(listener);
    }

    public void notifyListeners() {
        for (BoardChangeListener listener : listeners) {
            listener.boardChanged();
        }
    }
    public Checker getChecker(int x, int y) {
        return checkers[x][y];
    }

    public void setChecker(int x, int y, Checker checker) {
        checkers[x][y] = checker;
//        notifyListeners();
    }

    public int getSize() {
        return SIZE;
    }
}
