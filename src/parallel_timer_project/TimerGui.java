/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parallel_timer_project;

/**
 *
 * @author MICHAEL NABIL
 */

import java.util.Random;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.Timer;
import java.util.TimerTask;
import static javafx.application.Application.launch;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class TimerGui extends Application {

    private Label timerLabel;
    private TextArea flaggedTimesArea;
    private TextField minutesInput , secondsInput;
    private Button flagButton;
    private long timeElapsed = 0;   //for stopwatch
    private long timerCountdown = 0;    //for counter
    private boolean running = false;
    private boolean isTimerMode = false;    //if false: stopwatch, if true: timer
    private Timer timer;
    private int id;

    public static void main(String[] args) {
        launch(args);
    }
    public void setId(int id){
        this.id = id;
    }
    @Override
    public void start(Stage primaryStage) {
        
        Label currentThreadId = new Label();
        currentThreadId.setText("Timer "+((int)(id+1)));
        currentThreadId.setFont(Font.font("Verdana",19));
        currentThreadId.setLayoutX(5);
        currentThreadId.setLayoutY(32);
        currentThreadId.setTextFill(Color.WHITE);
        
//        System.out.println(id+1);     //print the ID of the thread
        
        timerLabel = new Label("00:00:00");
        timerLabel.setStyle("-fx-font-size: 36px;");
        
        flaggedTimesArea = new TextArea();
        flaggedTimesArea.setEditable(false);
        
        minutesInput = new TextField("0");
        minutesInput.setPromptText("Minutes");

        secondsInput = new TextField("0");
        secondsInput.setPromptText("Seconds");
        
        Button startButton = new Button("Start");
        Button pauseButton = new Button("Pause");
        Button resetButton = new Button("Reset");
        /*Button*/ flagButton = new Button("Flag");
        Button modeButton = new Button("Switch to Timer");
        
        timerLabel.setLayoutX(90);
        timerLabel.setLayoutY(20);

        flaggedTimesArea.setLayoutX(20);
        flaggedTimesArea.setLayoutY(80);
        flaggedTimesArea.setPrefSize(260, 100);

        minutesInput.setLayoutX(20);
        minutesInput.setLayoutY(200);
        minutesInput.setPrefWidth(60);

        secondsInput.setLayoutX(90);
        secondsInput.setLayoutY(200);
        secondsInput.setPrefWidth(60);

        startButton.setLayoutX(160);
        startButton.setLayoutY(240);//200

        pauseButton.setLayoutX(20);
        pauseButton.setLayoutY(240);

        resetButton.setLayoutX(90);
        resetButton.setLayoutY(240);

        flagButton.setLayoutX(220);
        flagButton.setLayoutY(240);

        modeButton.setLayoutX(90);
        modeButton.setLayoutY(280);
        
        
        startButton.setOnAction(e -> start());
        pauseButton.setOnAction(e -> pause());
        resetButton.setOnAction(e -> reset());
        flagButton.setOnAction(e -> flagTime());
        modeButton.setOnAction(e -> switchMode(modeButton));

        // group components
        Group root = new Group(
            timerLabel,
            flaggedTimesArea,
            minutesInput,
            secondsInput,
            flagButton,
            pauseButton,
            resetButton,
            startButton,
            modeButton,
            currentThreadId
        );

        // scene and stage
        Scene scene = new Scene(root, 300, 350);
        
        Random randGenerator = new Random(id+1); //+1
        int rgb[] = new int[3];
        for(int i=0;i<3;i++)
            rgb[i] = randGenerator.nextInt(0, 256);
        scene.setFill(Color.rgb(rgb[0], rgb[1], rgb[2]));
        
        primaryStage.setTitle("Timer & Stopwatch \""+((int)(this.id+1))+"\"");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        primaryStage.setX((id*300)%1500);
        if(id>4)
            primaryStage.setY(420);
        else
            primaryStage.setY(0);
        
        setStopwatchMode();
    }

    private void start() {
        if (isTimerMode) {
            startTimerCountdown();
        } else {
            startStopwatch();
        }
    }

    private void pause() {
        if (running) {
            running = false;
            if (timer != null) {
                timer.cancel();
            }
        }
    }

    private void reset() {
        pause();
        if (isTimerMode) {
            timerCountdown = 0;
            updateTimerLabel();
        } else {
            timeElapsed = 0;
            updateTimerLabel();
            flaggedTimesArea.clear();
        }
    }

    private void flagTime() {
        if (!isTimerMode) {
            flaggedTimesArea.appendText("Flagged time: " + formatTime(timeElapsed) + "\n");
        }
    }

    private void switchMode(Button modeButton) {
        pause();
        if (isTimerMode) {
            setStopwatchMode();
            modeButton.setText("Switch to Timer");
        } else {
            setTimerMode();
            modeButton.setText("Switch to Stopwatch");
        }
    }

    private void setStopwatchMode() {
        isTimerMode = false;
        minutesInput.setVisible(false);
        secondsInput.setVisible(false);
        flaggedTimesArea.setVisible(true);
        flagButton.setVisible(true);
    }

    private void setTimerMode() {
        isTimerMode = true;
        minutesInput.setVisible(true);
        secondsInput.setVisible(true);
        flagButton.setVisible(false);
        flaggedTimesArea.setVisible(false);
    }

    private void startStopwatch() {
        if (!running) {     //to prevent the creation of new object on clicking start, when the timer is already running (so nothing happens when we click start during its running)
            running = true;
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    timeElapsed++;
                    updateTimerLabel();
                }
            }, 0, 1000);
        }
    }

    private void startTimerCountdown() {
        if (!running) {
            try {
                long minutes = Long.parseLong(minutesInput.getText());
                long seconds = Long.parseLong(secondsInput.getText());
                
                if(timerCountdown == 0) //to resume counting on the previously elipsed time befor clicking pause
                    timerCountdown = minutes * 60 + seconds;
                else
                    //do nothing
                
                if (timerCountdown <= 0) {
                    showAlert("Invalid Input in Timer"+((int)(id+1))+" ", "Please enter a valid time greater than 0.",Alert.AlertType.ERROR);
                    return;
                }

                running = true;
                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        if (timerCountdown > 0) {
                            timerCountdown--;
                            updateTimerLabel();
                        } else {
                            timer.cancel();
                            running = false;
                            javafx.application.Platform.runLater(() -> {    // we have used platform.runlater, cause we are making realtime changes on the gui using a thread different from the gui thread
                                showAlert("Timer "+((int)(id+1))+", Notification","Time out",Alert.AlertType.INFORMATION);
                            });
                        }
                    }
                }, 0, 1000);
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input "+((int)(id+1))+" ", "Please enter valid numbers for minutes and seconds.",Alert.AlertType.ERROR);
            }
        }
    }

    private void updateTimerLabel() {
        javafx.application.Platform.runLater(() -> {
            if (isTimerMode) {
                timerLabel.setText(formatTime(timerCountdown));
            } else {
                timerLabel.setText(formatTime(timeElapsed));
            }
        });
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        
        alert.showAndWait();
    }
    
    private String formatTime(long seconds) {
        long hrs = seconds / 3600;
        long mins = (seconds % 3600) / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hrs, mins, secs);
    }
}