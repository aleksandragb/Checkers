public interface MoveStrategy {
    boolean isValidMove(int startX, int startY, int endX, int endY, Board board);
}
