public class NormalCheckerMoveStrategy implements MoveStrategy {
    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Board board) {
        Checker checker = board.getChecker(startX, startY);

        // Sprawdzenie ruchu po przekątnej i czy pole docelowe jest wolne
        if (Math.abs(startX - endX) != Math.abs(startY - endY) || board.getChecker(endX, endY) != null) {
            return false;
        }

        // Ruchy o jedno pole - tylko do przodu
        if (Math.abs(startX - endX) == 1) {
            if (!checker.isPlayerOne() && endX <= startX) return false;
            if (checker.isPlayerOne() && endX >= startX) return false;
            return true;
        }

        // Ruchy o dwa pola (bicie) - do przodu
        if (Math.abs(startX - endX) == 2) {
            int midX = (startX + endX) / 2;
            int midY = (startY + endY) / 2;
            Checker middleChecker = board.getChecker(midX, midY);

            // Sprawdź, czy pionek do zbicia jest obecny i należy do przeciwnika
            return middleChecker != null && middleChecker.isPlayerOne() != checker.isPlayerOne();
        }

        return false;
    }
}