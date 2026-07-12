/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parallel_timer_project;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author MICHAEL NABIL
 */


public class MainClass {
    static int threadsNumber; // we will read this value from the StartClass when it calls setThreadNumber()
    static Object lock = new Object();  // prevents the main method from proceeding without receiving user input
    public static void main(String[] args){

        Thread mainThread = new Thread(() -> {
            StartClass.main(args);
        });
        mainThread.start();
        
        //lock to wait for the main gui to get the number of threads from the user.
        
        synchronized(lock){
            try {
                lock.wait();
            } catch (InterruptedException ex) {
                ex.getMessage();
            }   
        }
            
        for (int i=0;i<threadsNumber;i++){
            final int currentId = i;        // we cannot use local variable inside: platform.raunlater ,it must be "final"
            Thread timerThread = new Thread(() -> {
                Platform.runLater(()->{
                    TimerGui t = new TimerGui();
                    Stage primaryStage = new Stage();
                    t.setId(currentId);
                    t.start(primaryStage);
                });
            });
            timerThread.start();
//                Platform.runLater(()->{
//                    TimerGui t = new TimerGui();
//                    Stage primaryStage = new Stage();
////                    t.setId(currentId);
//                    t.start(primaryStage);
//                });
        }   
    }
    
    public static void setThreadNumber(int threadsNumber){
        MainClass.threadsNumber = threadsNumber;
    }
    
}