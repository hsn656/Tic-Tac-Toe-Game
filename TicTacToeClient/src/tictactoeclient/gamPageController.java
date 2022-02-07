package tictactoeclient;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;



public class gamPageController implements Initializable{
	@FXML
	private Button exitButton;
	
	@FXML
	private Button playAgainButton;
	
	@FXML
	private Button btn1;
	@FXML
	private Button btn2;
	@FXML
	private Button btn3;
	@FXML
	private Button btn4;
	@FXML
	private Button btn5;
	@FXML
	private Button btn6;
	@FXML
	private Button btn7;
	@FXML
	private Button btn8;
	@FXML
	private Button btn9;
	
	@FXML
	private Text playerOneScoreText;
	@FXML
	private Text playerTwoScoreText;
	
	@FXML
	private Text playerOneName;
	@FXML
	private Text playerTwoName;
	
	private int playerTurn=0;
	private int playerOneScore=0;
	private int playerTwoScore=0;
	
	ArrayList<Button> buttons;
	
	
	public void exit(ActionEvent e) {
		Stage stage = (Stage) exitButton.getScene().getWindow();
	    stage.close();
	}
	
	public void playAgain(ActionEvent e) {
		playerTurn = 0;
		buttons = new ArrayList<>(Arrays.asList(btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9));
		buttons.forEach(button ->{
			setButton(button);
			button.setDisable(false);
			button.setText("");
		});
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		buttons = new ArrayList<>(Arrays.asList(btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9));
		
//		playerOneName.setText("his name");
//		playerTwoName.setText("his name");
		
		buttons.forEach(button ->{
			setButton(button);
			button.setFocusTraversable(false);
		});
	}
	
	public void setButton(Button button) {
		button.setOnMouseClicked(mouseEvent ->{
			setPlayerTurn(button);
			button.setDisable(true);
			checkIfWin();
		});
	}
	
	public void setPlayerTurn(Button button) {
		if(playerTurn % 2 ==0) {
			button.setStyle("-fx-text-fill: #03800f");
			button.setText("X");
			playerTurn = 1;
		}else {
			button.setText("O");
			button.setStyle("-fx-text-fill: #24119c");
			playerTurn = 0;
		}
	}
	
	public void checkIfWin() {
		
//		for (int a = 0; a < 8; a++) {
//            String line = switch (a) {
//                case 0 -> btn1.getText() + btn2.getText() + btn3.getText();
//                case 1 -> btn4.getText() + btn5.getText() + btn6.getText();
//                case 2 -> btn7.getText() + btn8.getText() + btn9.getText();
//                case 3 -> btn1.getText() + btn5.getText() + btn9.getText();
//                case 4 -> btn3.getText() + btn5.getText() + btn7.getText();
//                case 5 -> btn1.getText() + btn4.getText() + btn7.getText();
//                case 6 -> btn2.getText() + btn5.getText() + btn8.getText();
//                case 7 -> btn3.getText() + btn6.getText() + btn9.getText();
//                default -> null;
//            };
//
//            //X winner
//            if (line.equals("XXX")) {
//                System.out.println("x won");
//                playerOneScore++;
//                playerOneScoreText.setText(String.valueOf(playerOneScore));
//                
//                buttons = new ArrayList<>(Arrays.asList(btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9));
//        		buttons.forEach(button ->{
//        			setButton(button);
//        			button.setDisable(true);
//        		});
//                
//            }
//
//            //O winner
//            else if (line.equals("OOO")) {
//            	System.out.println("o won");
//            	playerTwoScore++;
//                playerTwoScoreText.setText(String.valueOf(playerTwoScore));
//                
//                buttons = new ArrayList<>(Arrays.asList(btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9));
//        		buttons.forEach(button ->{
//        			setButton(button);
//        			button.setDisable(true);
//        		});
//            }
//        }
	}
	
}
