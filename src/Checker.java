import javax.swing.*;

public class Checker {
    private final boolean isPlayerOne;
    private ImageIcon icon;
    private MoveStrategy moveStrategy;

    public Checker(boolean isPlayerOne, ImageIcon icon, MoveStrategy moveStrategy) {
        this.isPlayerOne = isPlayerOne;
        this.icon = icon;
        this.moveStrategy = moveStrategy;
    }

    public boolean isPlayerOne() {
        return isPlayerOne;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    public void setMoveStrategy(MoveStrategy moveStrategy) {
        this.moveStrategy = moveStrategy;
    }

    public MoveStrategy getMoveStrategy() {
        return moveStrategy;
    }
}
