public class KingMoveStrategy implements MoveStrategy {
    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Board board) {
        // Sprawdzenie ruchu po przekątnej
        if (Math.abs(startX - endX) != Math.abs(startY - endY)) return false;

        int deltaX = endX > startX ? 1 : -1;
        int deltaY = endY > startY ? 1 : -1;
        int x = startX + deltaX;
        int y = startY + deltaY;

        boolean hasJumped = false;

        while (x != endX && y != endY) {
            Checker currentChecker = board.getChecker(x, y);
            if (currentChecker != null) {
                if (hasJumped) return false; // Nie można przeskoczyć nad dwoma pionkami
                if (currentChecker.isPlayerOne() == board.getChecker(startX, startY).isPlayerOne()) return false; // Nie można przeskoczyć nad własnym pionkiem
                hasJumped = true;
            }
            x += deltaX;
            y += deltaY;
        }

        return hasJumped || board.getChecker(endX, endY) == null; // Musi zbić pionek lub przenieść się na puste pole
    }
}
