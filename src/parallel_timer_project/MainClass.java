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
    static int threadsNumber; //hana5od el value bta3etha mn el startClass lama te3mel call lel setThreadNumber()
    static Object lock = new Object();  //bymna3 el main method enaha tkamel mn8er ma el user yda5al el input
    
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
            final int currentId = i;        // maynfa34 nesta5dem local variable gwa el: platform.raunlater ,lazem ykon final
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
