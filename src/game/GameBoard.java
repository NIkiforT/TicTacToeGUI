package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameBoard extends JFrame {
    static int dimension = 3; // размернссть
    static int cellSize = 150; // размер одной клетки
    private char[][]  gameField; // матрица игры
    private GameButton[] gameButtons; // массив кнопок

    static char nullSymbol = '\u0000'; // null
    private Game game; // ссылка на игру

    public GameBoard(Game currentGame) {
        this.game = currentGame;
        initField();
    }

    // отрисовка поля
    private void  initField() {
        setBounds(cellSize*dimension, cellSize*dimension,400,300);
        setTitle("Крестики-нолики");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());
        JPanel controlPanel = new JPanel(); // панель управления игрой
        JButton newGameButton = new JButton("Новая игра");


        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emptyField();
            }
        });

        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.add(newGameButton);
        controlPanel.setSize(cellSize * dimension, 150);

        JPanel gameFieldPanel = new JPanel(); // панель игры
        gameFieldPanel.setLayout(new GridLayout(dimension, dimension));
        gameFieldPanel.setSize(cellSize * dimension, cellSize * dimension);

        gameField = new char[dimension][dimension];
        gameButtons = new GameButton[dimension*dimension];

        // инициализация игрового поля
        for (int i = 0; i < (dimension*dimension); i++) {
            GameButton fieldButton = new GameButton(i, this);
            gameFieldPanel.add(fieldButton);
            gameButtons[i] = fieldButton;
        }

        getContentPane().add(controlPanel, BorderLayout.NORTH);
        getContentPane().add(gameFieldPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // очистка поля
    void  emptyField() {
        for (int i = 0; i<(dimension*dimension); i++) {
            gameButtons[i].setText("");
            int x = i / GameBoard.dimension;
            int y = i % GameBoard.dimension;
            gameField[x][y] = nullSymbol;
        }
    }

    void  emptyFieldOppositePredict(int cellIndex) {

            gameButtons[cellIndex].setText("");
            int x = cellIndex / GameBoard.dimension;
            int y = cellIndex % GameBoard.dimension;
            gameField[y][x] = nullSymbol;

    }

    Game getGame() {
        return game;
    }

    // метод проверки доступности клетки для хода
    boolean isTurnable(int x, int y) {

        boolean result = false;

        if (gameField[y][x]==nullSymbol) {
            result = true;
        }

        return result;

    }


    // обновление матрицы после хода
    void updateGameField(int x, int y) {
        gameField[y][x] = game.getCurrentPlayer().getPlayerSign();
    }

    boolean checkWin() {
        boolean result = false;
        char playerSymbol = getGame().getCurrentPlayer().getPlayerSign();
        if (checkWinDiagonals(playerSymbol) || checkWinLines(playerSymbol)) {
            result = true;
        }

        return result;
    }

    private boolean checkWinDiagonals(char playerSymbol) {

        boolean leftRight, rightLeft, result;
        result = false;
        leftRight = true; rightLeft = true;
        for (int i = 0; i< dimension; i++) {
            leftRight &= (gameField[i][i] == playerSymbol);
            rightLeft &= (gameField[dimension - i - 1][i] == playerSymbol);
        }
        if (leftRight || rightLeft) {
            result = true;
        }


        return result;
    }

    private boolean checkWinLines(char playerSymbol) {

        boolean cols, rows, result;
        result = false;
        for (int col= 0; col< dimension; col++) {
            cols = true;
            rows = true;

            for (int row =0; row< dimension; row++) {
                cols &= (gameField[col][row] == playerSymbol);
                rows &= (gameField[row][col] == playerSymbol);

            }
            if (cols || rows) {
                result = true;
                break;
            }
        }
        return result;
    }

    // метод проверки заполннености поля
    boolean isFull() {
        boolean result = true;
        for (int i= 0; i<dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (gameField[i][j] == nullSymbol)
                    result = false;
            }

        }
        return  result;
    }

    public GameButton getButton(int buttonText) {
        return gameButtons[buttonText];
    }

    // проверка выигрыша противника
    boolean checkWinOpposite(char playerSign) {
        boolean result = false;

        if (checkWinDiagonals(playerSign) || checkWinLines(playerSign)) {
            result = true;
        }

        return result;
    }

    // обновление матрицы после хода противника для прогноза потенциального выигрыша игрока
    void updateGameFieldOppisite(int x, int y, char playerSign) {
        gameField[y][x] = playerSign;
    }

}
