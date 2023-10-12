package tictactoe.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ComputerStartController implements Initializable {
private List<Button> buttonList = new ArrayList<>();
private List<Button> levelList = new ArrayList<>();
@FXML
private HBox playerSymbols;
@FXML
private HBox computerLevels;
@FXML
private String playerSymbol;
@FXML
private String computerLevel;
@FXML
private Label errorLabel;
@FXML
private VBox computerStartWindow;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
     computerStartWindow.getStyleClass().add("computer_start");
        for(Node nd: playerSymbols.getChildren()){
        if(nd instanceof Button){
            buttonList.add((Button) nd);
        }
     }
        for(Node nd: computerLevels.getChildren()){
            if(nd instanceof Button){
                levelList.add((Button) nd);
            }
        }
    }

    public void setPlayerSymbol(ActionEvent event) {
        Button pressedButton = (Button) event.getSource();
        this.playerSymbol=pressedButton.getText();
        changeClass(buttonList,"clicked",pressedButton);
    }

    public void setComputerLevel(ActionEvent event) {
        Button pressedButton = (Button) event.getSource();
        this.computerLevel=pressedButton.getText();
        changeClass(levelList,"clicked",pressedButton);
    }

    private void changeClass(List<Button> buttons,String className,Button pressedButton){
        for(Button btn: buttons){
            if(btn.getText().equals(pressedButton.getText())){
                if(!btn.getStyleClass().contains("clicked")){
                    btn.getStyleClass().add(className);
                }
            }else{
                btn.getStyleClass().remove(className);
            }
        }
    }
    private void removeClass(List<Button> buttons,String className){
        for(Button btn:buttons){
            btn.getStyleClass().remove(className);
        }
    }



    public void startGame(ActionEvent event) throws IOException {
        if(playerSymbol==null||computerLevel==null){
            errorLabel.setText("Please select a symbol and a level!!!");
        }else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/ComputerView.fxml"));
            Parent root  = loader.load();
            ComputerController controller = loader.getController();
            controller.setComputerLevel(computerLevel);
            controller.setPlayerSymbol(playerSymbol);
            if(playerSymbol.equals("O")){
                controller.handleComputerStart();
            }else{
                controller.setLblPlayer(playerSymbol);
                controller.setCompFirstMove();
            }
            Scene scene =new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Player vc Computer");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
            errorLabel.setText("");
            removeClass(buttonList,"clicked");
            removeClass(levelList,"clicked");
            playerSymbols=null;
            computerLevel=null;
        }

    }
}
