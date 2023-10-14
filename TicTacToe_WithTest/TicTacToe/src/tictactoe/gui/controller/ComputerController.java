package tictactoe.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import tictactoe.bll.*;


import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class ComputerController implements Initializable {
    private String playerSymbol = "";
    private String computerLevel = "";
    @FXML
    private Button btnNewGame;
    @FXML
    private GridPane gridPane;

    @FXML
    private ComputerGameBoard compGame;
    private ComputerPlayer compPlayer;


    @FXML
    private Label lblPlayer;
    private static final String TXT_PLAYER = "Player: ";

    public void handleButtonAction(ActionEvent event) {


        try {
            Integer row = GridPane.getRowIndex((Node) event.getSource());
            Integer col = GridPane.getColumnIndex((Node) event.getSource());
            int r = (row == null) ? 0 : row;
            int c = (col == null) ? 0 : col;
            int player = -1;
            if (playerSymbol.equalsIgnoreCase("x")) {
                player = compGame.changePlayer();
            } else {
                player = compGame.getNextPlayer();
            }
            //int player = compGame.getNextPlayer();
            if (compGame.play(c, r)) {
                compPlayer.addPlayerMoves(r, c);
                if (compGame.isGameOver()) {
                    System.out.println(compGame.getWinner() + "from player");
                    Button btn = (Button) event.getSource();
                    btn.setText(changeSymbol(player));
                    displayWinner(String.valueOf(compGame.getWinner()));
                } else {
                    Button btn = (Button) event.getSource();
                    btn.setText(changeSymbol(player));
                    setPlayer();
                }
            } else {
                return;
            }
            int[] compCoords = new int[2];
            if(computerLevel.equalsIgnoreCase("easy")){
                compCoords=compPlayer.computerMove();
            }else if(computerLevel.equalsIgnoreCase("hard")){
                compCoords=compPlayer.computerMoveSmart();
            }
            int comp = compGame.getNextPlayer();
            if (compCoords != null) {
                if (compGame.play(compCoords[1], compCoords[0])) {
                    if (compGame.isGameOver()) {
                        System.out.println(compGame.getWinner() + " from computer");
                        System.out.println(compCoords[0] + " " + compCoords[1] + "coordscomp");
                        displayMove(compCoords[0], compCoords[1], decideComputerSymbol(playerSymbol));
                        displayWinner(String.valueOf(compGame.getWinner()));
                    } else {
                        displayMove(compCoords[0], compCoords[1], decideComputerSymbol(playerSymbol));
                        setPlayer();
                    }
                } else {
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void handleComputerStart() {
        compGame.setPlayer("X");
        setPlayer();
        if (computerLevel.equalsIgnoreCase("hard")) {
            compPlayer.addComputerMoveWhenFirst(1, 1);
            compGame.play(1, 1);
            displayMove(1, 1, "X");
            return;
        }
        int rowComputer = getRandomNumber(3);
        int columnComputer = getRandomNumber(3);
        compPlayer.addPlayerMoves(rowComputer, columnComputer);
        compGame.play(columnComputer, rowComputer);
        displayMove(rowComputer, columnComputer, "X");
    }

    private void displayMove(int rowComputer, int columnComputer, String value) {
        List<Node> nodes = gridPane.getChildren();
        for (Node node : nodes) {
            int row = gridPane.getRowIndex(node) != null ? gridPane.getRowIndex(node) : 0;
            int column = gridPane.getColumnIndex(node) != null ? gridPane.getColumnIndex(node) : 0;
            if (rowComputer == row && columnComputer == column) {
                ((Button) node).setText(value);
            }
        }
    }

    @FXML
    private void handleNewGame(ActionEvent event) {
        compGame.newGame();
        compPlayer = new ComputerPlayer();

        clearBoard();
        lblPlayer.setText("");
        if (playerSymbol.equalsIgnoreCase("O")) {
            handleComputerStart();
        } else {
            compGame.setPlayer(playerSymbol);
        }
        setPlayer();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        compGame = new ComputerPlayerBoard();
        compPlayer = new ComputerPlayer();
    }
    private void setPlayer() {
        String player = "";
        if (playerSymbol.equalsIgnoreCase("X")) {
            player = compGame.getCurrentPlayer() == 0 ? "X" : "O";
        } else {
            player = (compGame.getCurrentPlayer() == 0 ? "O" : "X");
        }

        lblPlayer.setText(TXT_PLAYER + "=>" + "\"" + player + "\"");
    }

    private void displayWinner(String winner) {
        String message = "";
        switch (winner) {
            case "-1":
                message = "It's a draw :-(";
                break;
            default:
                message = "Player " + winner + " wins!!!";
                break;
        }
        lblPlayer.setText(message);

    }

    private void clearBoard() {
        for (Node n : gridPane.getChildren()) {
            Button btn = (Button) n;
            btn.setText("");
        }
    }

    private String changeSymbol(int player) {
        return player == 0 ? "X" : "O";
    }

    public String getPlayerSymbol() {
        return playerSymbol;
    }

    public void setPlayerSymbol(String playerSymbol) {
        this.playerSymbol = playerSymbol;
    }


    public String getComputerLevel() {
        return computerLevel;
    }

    public void setComputerLevel(String computerLevel) {
        this.computerLevel = computerLevel;
    }


    public Label getLblPlayer() {
        return lblPlayer;
    }

    public void setLblPlayer(String text) {
        this.lblPlayer.setText(TXT_PLAYER + " " + "\"" + text + "\"");
    }

    private int getRandomNumber(int bound) {
        Random random = new Random();
        return random.nextInt(bound);
    }

    private String decideComputerSymbol(String symbol) {
        return playerSymbol.equalsIgnoreCase("X") ? "O" : "X";
    }

    public void setCompFirstMove(){
        this.compPlayer.setFirstMove(true);
    }
}
