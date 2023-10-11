package tictactoe.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import tictactoe.bll.ComputerGameBoard;
import tictactoe.bll.ComputerPlayerBoard;
import tictactoe.bll.GameBoard;
import tictactoe.bll.IGameModel;


import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class ComputerController implements Initializable {
    private String playerSymbol ="";
    private String computerLevel ="";
    @FXML
    private Button btnNewGame;
    @FXML
    private GridPane gridPane;
    @FXML
    private IGameModel game;
    @FXML
    private ComputerGameBoard compGame;



    @FXML
    private Label lblPlayer;
    private static final String TXT_PLAYER = "Player: ";

    public void handleButtonAction(ActionEvent event) {


        try {
            Integer row = GridPane.getRowIndex((Node) event.getSource());
            Integer col = GridPane.getColumnIndex((Node) event.getSource());
            int r = (row == null) ? 0 : row;
            int c = (col == null) ? 0 : col;
            int player = compGame.getNextPlayer();
            System.out.println(player + " currentPlayer human" );
            if (compGame.play(c, r)) {
                if (compGame.isGameOver()) {
                    Button btn = (Button) event.getSource();
                    btn.setText(changeSymbol(player));
                    displayWinner(String.valueOf(compGame.getWinner()));
                } else {
                    Button btn = (Button) event.getSource();
                    btn.setText(changeSymbol(player));
                    setPlayer();
                }
            } else {
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

    public void handleComputerStart() {
        compGame.setPlayer("X");
        setPlayer();
        int rowComputer = getRandomNumber(3);
        int columnComputer = getRandomNumber(3);

        compGame.play(columnComputer,rowComputer);
        List<Node> nodes = gridPane.getChildren();
        for (Node node : nodes) {
            int row = gridPane.getRowIndex(node) != null ? gridPane.getRowIndex(node) : 0;
            int column = gridPane.getColumnIndex(node) != null ? gridPane.getColumnIndex(node) : 0;
            if (rowComputer == row && columnComputer == column) {
                ((Button) node).setText("X");
            }
        }
    }

    @FXML
    private void handleNewGame(ActionEvent event) {
        compGame.newGame();
        clearBoard();
        lblPlayer.setText("");
        if(playerSymbol.equalsIgnoreCase("O")){
           handleComputerStart();
        }else{
            compGame.setPlayer(playerSymbol);
        }
        setPlayer();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        game = new GameBoard();
        compGame= new ComputerPlayerBoard();
    }


    private void setPlayer() {
        lblPlayer.setText(TXT_PLAYER + "\"" + (compGame.getCurrentPlayer()==0?"O":"X") + "\"");
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
    private  String changeSymbol( int player) {
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

//    private String[][] setGridData(GridPane grisPane) {
//        String[][] gridData = new String[3][3];
//        List<Node> nodes = gridPane.getChildren();
//
//        for (Node node : nodes) {
//            int row = gridPane.getRowIndex(node) != null ? gridPane.getRowIndex(node) : 0;
//            int column = gridPane.getColumnIndex(node) != null ? gridPane.getColumnIndex(node) : 0;
//            gridData[row][column] = ((Button) node).getText();
//        }
//        return gridData;
//    }

    private String decideSymbol(String player, String playerSymbol) {
        String xoro = "";
        if (player.equals("X")) {
            xoro = "X";
        } else if (player.equals("O")) {
            xoro = "O";
        } else if (player.equals("Computer") && playerSymbol.equals("X")) {
            xoro = "O";
        } else if (player.equals("Computer") && playerSymbol.equals("O")) {
            xoro = "X";
        }
        return xoro;
    }

    private String decideSymbolNew(String symbol) {
        String xoro = "";
        if (symbol.equals("X")) {
            xoro = "O";
        } else if (symbol.equals("O")) {
            xoro = "X";
        }
        return xoro;
    }
}
