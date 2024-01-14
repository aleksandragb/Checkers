import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardView extends JFrame implements BoardChangeListener {
    private GameController controller;
    private int selectedX = -1, selectedY = -1;
    private static final int SIZE = 8;
    private static final int SQUARE_SIZE = 400/10;
    private JPanel[][] squares = new JPanel[SIZE][SIZE];
    private JPanel selectedSquare = null;
    private JLabel playerOneCheckersLabel;
    private JLabel playerTwoCheckersLabel;

    public BoardView(GameController controller) {
        this.controller = controller;
        setTitle("Warcaby");
        setSize(400, 400);
        setLayout(new BorderLayout()); // Zmiana na BorderLayout

        JPanel boardPanel = new JPanel(new GridLayout(SIZE, SIZE)); // Tworzenie panelu planszy
        initializeBoard(boardPanel); // Inicjalizacja planszy na osobnym panelu
        add(boardPanel, BorderLayout.CENTER); // Dodawanie planszy do środka

        playerOneCheckersLabel = new JLabel("Gracz 1: " + controller.getPlayerOneCheckersCount());
        playerTwoCheckersLabel = new JLabel("Gracz 2: " + controller.getPlayerTwoCheckersCount());

        add(playerOneCheckersLabel, BorderLayout.SOUTH); // Dodawanie etykiety gracza 1 na górze
        add(playerTwoCheckersLabel, BorderLayout.NORTH); // Dodawanie etykiety gracza 2 na dole

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initializeBoard(JPanel boardPanel) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                squares[i][j] = new JPanel(new BorderLayout());
                boardPanel.add(squares[i][j]);
                if ((i + j) % 2 == 0) {
                    squares[i][j].setBackground(Color.WHITE);
                } else {
                    squares[i][j].setBackground(Color.BLACK);
                }

                int finalI = i;
                int finalJ = j;
                squares[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        handleSquareClick(finalI, finalJ);
                    }
                });
            }
        }
        loadCheckers();
    }

    private void handleSquareClick(int x, int y) {
        Checker checker = controller.getCheckerAt(x, y);

        // Jeśli żaden pionek nie jest zaznaczony i kliknięty został pionek aktualnego gracza
        if (selectedX == -1 && selectedY == -1 && checker != null && checker.isPlayerOne() == controller.isPlayerOneTurn()) {
            controller.selectChecker(x, y);
            selectedX = x;
            selectedY = y;
            updateSelectedSquare();
        }
        // Jeśli pionek jest już zaznaczony, próbuj wykonać ruch
        else if (selectedX != -1 && selectedY != -1) {
            controller.moveSelectedChecker(x, y);
            if (x != selectedX || y != selectedY) { // Jeśli ruch został wykonany na inne pole
                selectedX = -1;
                selectedY = -1;
                updateSelectedSquare();
            }
        }
    }



    private void loadCheckers() {
        for (int i = 0; i < controller.getBoardSize(); i++) {
            for (int j = 0; j < controller.getBoardSize(); j++) {
                squares[i][j].removeAll(); // Usuń wszystko z kwadratu

                Checker checker = controller.getCheckerAt(i, j);
                if (checker != null) {
                    ImageIcon checkerIcon = scaleIcon(checker.getIcon());
                    JLabel checkerLabel = new JLabel(checkerIcon);
                    squares[i][j].add(checkerLabel, BorderLayout.CENTER);
                }

                squares[i][j].revalidate(); // Dodano revalidate i repaint dla każdego kwadratu
                squares[i][j].repaint();
            }
        }
    }


    private ImageIcon scaleIcon(ImageIcon icon) {
        Image image = icon.getImage();
        Image newImage = image.getScaledInstance(SQUARE_SIZE, SQUARE_SIZE,  java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(newImage);
    }
    private void updateSelectedSquare() {
        // Usuń obramowanie ze wszystkich kwadratów
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                squares[i][j].setBorder(null);
            }
        }
        // Ustaw obramowanie dla zaznaczonego kwadratu, jeśli jest zaznaczony
        if (selectedX != -1 && selectedY != -1) {
            squares[selectedX][selectedY].setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        }

        revalidate();
        repaint();
    }

    @Override
    public void boardChanged() {
        playerOneCheckersLabel.setText("Gracz 1: " + controller.getPlayerOneCheckersCount());
        playerTwoCheckersLabel.setText("Gracz 2: " + controller.getPlayerTwoCheckersCount());
        loadCheckers();
    }
}
