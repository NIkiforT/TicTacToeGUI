package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class GameActionListener implements ActionListener {
    private int row;
    private int cell;
    private GameButton button;

    public GameActionListener(int row, int cell, GameButton gButton) {
        this.row = row;
        this.cell = cell;
        this.button = gButton;


    }

    @Override
    public void actionPerformed(ActionEvent e) {

        GameBoard board = button.getBoard();
        if (board.isTurnable(row, cell)) {

            updateByPlayersData(board);
            if (board.isFull()) {
                board.getGame().showMessage("Ничья!");
                board.emptyField();
            }
            else {
                updateByAiData(board);
            }
        }
        else {
            board.getGame().showMessage("Некорректный ход!");
        }
    }

    // ход человека
    private void  updateByPlayersData(GameBoard board) {
        board.updateGameField(row, cell);
        button.setText(Character.toString(board.getGame().getCurrentPlayer().getPlayerSign()));
        if (board.checkWin()) {
            button.getBoard().getGame().showMessage("Вы выиграли!");
            board.emptyField();
        }
        else {
            board.getGame().passTurn();
        }

    }

    private void updateByAiData(GameBoard board) {

        int x = -1;
        int y = -1;
        boolean comp_win = false;
        boolean human_win = false;
        int cellIndex;
        // текущий знак компьютера
        char currentSign = board.getGame().getCurrentPlayer().getPlayerSign();
        char currentOppositeSign = 'X';
        boolean thisComputer = false;


            // чем играет противник
            if (currentSign == 'X') {
                currentOppositeSign = 'O';

            }


        //проверяем вероятность выигрошной комбинации компьютера


            for (int i = 0; i < GameBoard.dimension; i++) {
                for (int j = 0; j < GameBoard.dimension; j++) {
                    if (board.isTurnable(j, i)) {
                      board.updateGameField(j, i);
                        cellIndex = GameBoard.dimension * j + i;
                        board.getButton(cellIndex).setText(Character.toString(currentSign));
                        if (board.checkWin()) {
                            x = j;
                            y = i;
                            comp_win = true;
                        }
                        //board.getButton(cellIndex).setText("");
                        board.emptyFieldOppositePredict(cellIndex);
                    }
                }
            }





      /*


        если удачной комбинациий для компьютера нет, мешаем противнику победить */

        if (!comp_win ) {
            for (int i = 0; i < GameBoard.dimension; i++) {
                for (int j = 0; j < GameBoard.dimension; j++) {
                    if (board.isTurnable(j, i)) {
                          //

                        cellIndex = GameBoard.dimension * j + i;
                        board.getButton(cellIndex).setText(Character.toString(currentOppositeSign));
                        board.updateGameFieldOppisite(j, i, currentOppositeSign);
                        if (board.checkWinOpposite(currentOppositeSign)) {
                            x = j;
                            y = i;
                            human_win = true;
                        }


                        board.emptyFieldOppositePredict(cellIndex);
                        //board.getButton(cellIndex).setText("");
                    }
                }
            }
        }




        /* */
        //ставим в любую ячейку, например, на первом ходу
        // */

        if (!comp_win && !human_win) {
            Random rnd = new Random();

            do {
                x = rnd.nextInt(GameBoard.dimension);
                y = rnd.nextInt(GameBoard.dimension);
            }
            while (!board.isTurnable(x, y));
        }
            board.updateGameField(x, y);
            cellIndex = GameBoard.dimension * x + y;
            board.getButton(cellIndex).setText(Character.toString(board.getGame().getCurrentPlayer().getPlayerSign()));
            if (board.checkWin()) {
                button.getBoard().getGame().showMessage("Компьютер выиграл!");
                board.emptyField();
            } else {
                board.getGame().passTurn();
            }

    }







}
