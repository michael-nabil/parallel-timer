/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parallel_timer_project;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author MICHAEL NABIL
 */
public class StartClass extends Application{
    public static boolean errorMessageFlag = false;
    
    @Override
    public void start(Stage stage){
        
        Group root = new Group();
        
        Text label = new Text();
        label.setText("Enter the number of the threads: ");
        label.setX(0);
        label.setY(30);
        label.setFont(Font.font("Verdana",30));
        
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setLayoutX(0);
        textArea.setLayoutY(50);
        textArea.setPrefSize(250,30 );
        textArea.setFont(Font.font("Verdana",30));
        textArea.setPromptText("Enter the text here");
        
        Button submit = new Button();
        submit.setText("Submit");
        submit.setLayoutX(200);
        submit.setLayoutY(190);
        submit.setPrefSize(200, 60);
        submit.setFont(new Font("Arial",25));
        submit.setOnAction(e -> submitButtonAction(textArea, root));
        
        root.getChildren().add(label);
        root.getChildren().add(textArea);
        root.getChildren().add(submit);
        Scene scene = new Scene(root);
        Stage window = new Stage();
        window.setScene(scene);
        window.show();

    }
    
    public static void main(String[] args) {
        launch(args);
    }

    public void submitButtonAction(TextArea textArea,Group root){
        
        if(errorMessageFlag)
            root.getChildren().remove(3);
        
        errorMessageFlag = false;
        
        try{
            int threadsNumber = Integer.parseInt(textArea.getText().trim());
            if(threadsNumber <= 0)
                throw new NumberFormatException();
            MainClass.setThreadNumber(threadsNumber);
            synchronized(MainClass.lock){
                MainClass.lock.notify();
            }
        }
        catch(NumberFormatException nFormat){
            Text errorMessage = new Text();
            errorMessage.setText("Invalid Input !");
            errorMessage.setStroke(Color.RED);
            errorMessage.setStrokeWidth(2);
            errorMessage.setFont(Font.font("Verdana",30));
            errorMessage.setX(0);
            errorMessage.setY(180);
            root.getChildren().add(errorMessage);
            errorMessageFlag = true;
        }
    }
}