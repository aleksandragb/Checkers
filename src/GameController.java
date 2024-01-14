import javax.swing.*;

public class GameController {
    private Board board; // model
    private BoardView boardView;
    private Checker selectedChecker;
    private int selectedX, selectedY;
    private boolean isPlayerOneTurn = true; // Dodajemy zmienną śledzącą turę
    private int playerOneCheckersCount = 12;
    private int playerTwoCheckersCount = 12;

    public GameController() {
        board = new Board();
        boardView = new BoardView(this);
        board.addChangeListener(boardView); // Rejestrujemy BoardView jako obserwatora
    }

    public int getBoardSize() {
        return board.getSize();
    }
    public Checker getCheckerAt(int x, int y) {
        return board.getChecker(x, y);
    }
    // Dodajemy metodę do sprawdzania, czyj jest obecnie ruch
    public boolean isPlayerOneTurn() {
        return isPlayerOneTurn;
    }

    public boolean moveChecker(int startX, int startY, int endX, int endY) {
        Checker checker = board.getChecker(startX, startY);

        if (checker == null || !isValidMove(checker, startX, startY, endX, endY)) {
            return false;
        }

        board.setChecker(endX, endY, checker); // ustawienie pionka na nowe pole
        board.setChecker(startX, startY, null); // usunięcie pionka ze starego pola

        // Usuwanie zbitego pionka
        if (Math.abs(startX - endX) > 1 || Math.abs(startY - endY) > 1) {
            removeJumpedCheckers(startX, startY, endX, endY);
        }

        // Promowanie do króla, jeśli to możliwe
        if (checker != null && canBePromotedToKing(checker, endX, endY)) {
            promoteToKing(checker);
        }

        if(playerTwoCheckersCount < 1 || playerOneCheckersCount < 1) {
            handleGameOver();
        }

        // Zmiana gracza wykonującego ruch
        isPlayerOneTurn = !isPlayerOneTurn;

        board.notifyListeners();
        return true;
    }

    private void removeJumpedCheckers(int startX, int startY, int endX, int endY) {
        int deltaX = endX > startX ? 1 : -1;
        int deltaY = endY > startY ? 1 : -1;
        int x = startX + deltaX;
        int y = startY + deltaY;

        while (x != endX && y != endY) {
            Checker checkerToRemove = board.getChecker(x, y);
            if (checkerToRemove != null) {
                if (checkerToRemove.isPlayerOne()) {
                    playerOneCheckersCount--;
                } else {
                    playerTwoCheckersCount--;
                }
                board.setChecker(x, y, null); // Usuwa pionki na trasie
            }
            x += deltaX;
            y += deltaY;
        }
    }

    private boolean isValidMove(Checker checker, int startX, int startY, int endX, int endY) {
        return checker.getMoveStrategy().isValidMove(startX, startY, endX, endY, board);
    }

    private boolean canBePromotedToKing(Checker checker, int x, int y) {
        return (checker.isPlayerOne() && x == 0) || (!checker.isPlayerOne() && x == board.getSize() - 1);

    }
    private void promoteToKing(Checker checker) {
        checker.setMoveStrategy(new KingMoveStrategy());

        String kingIconPath = checker.isPlayerOne() ? "resources/images/checkerKing1.png" : "resources/images/checkerKing2.png";
        checker.setIcon(new ImageIcon(kingIconPath));

        board.notifyListeners();
    }
    private void handleGameOver() {
        String winner = playerOneCheckersCount == 0 ? "Gracz 2" : "Gracz 1";
        JOptionPane.showMessageDialog(null, "Gra zakończona! Zwycięzca: " + winner);
    }
    public void selectChecker(int x, int y) {
        System.out.println("Selecting Checker:");
        Checker checker = board.getChecker(x, y);
        if (checker != null && checker.isPlayerOne() == isPlayerOneTurn) {
            selectedChecker = checker;
            selectedX = x;
            selectedY = y;
            System.out.println("Selecting Checker Success!");
        }
    }
    public void moveSelectedChecker(int x, int y) {
        if (selectedChecker != null && moveChecker(selectedX, selectedY, x, y)) {
            selectedChecker = null;
        }
    }

    public int getPlayerTwoCheckersCount() {
        return playerTwoCheckersCount;
    }

    public int getPlayerOneCheckersCount() {
        return playerOneCheckersCount;
    }
}
