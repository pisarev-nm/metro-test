package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.File;
import java.util.*;


public class Controller {
    @FXML
    private Button startButton;
    @FXML
    private Circle mainCircle;
    @FXML
    private Circle moveCircle;
    @FXML
    private Label resultLabel;
    @FXML
    private Label timerLabel;
    @FXML
    private TextField tfTestLong;
    @FXML
    private TextField tfRedQuantity;
    @FXML
    private TextField tfTestReact;

    private int secund=0;
    private double testLong = 20; //длительность теста в секундах
    private int quantity = 2; //количество красных светофоров
    private long redS, redF, startTime;
    private long react = 990; //реакция в миллисекундах
    private List<Integer> list = new ArrayList<>();
    private Timer mainTimmer = new Timer();



    private void testResult(String res)
    {
            resultLabel.setTranslateX(mainCircle.getCenterX()*2 -180);
            resultLabel.setTranslateY(mainCircle.getCenterY()*2 -80);
            if (res=="win")
        {
            mainTimmer.cancel();
            resultLabel.setText("Тест пройден :)");
            resultLabel.setTextFill(Color.GREEN);
            resultLabel.setVisible(true);
            System.out.println("test successfully passed");
        }
        else
            {
            mainTimmer.cancel();
            resultLabel.setText("Тест не пройден :(");
            resultLabel.setTextFill(Color.RED);
            resultLabel.setVisible(true);
            System.out.println("test failed");
        }
    }
    private void countdownStart()
    {
        timerLabel.setTranslateX(mainCircle.getCenterX()*2 -100);
        timerLabel.setTranslateY(50);
        timerLabel.setVisible(true);
    }
    private void countdownChange(int seconds)
    {
        timerLabel.setText("" + seconds/3600 + ":" + seconds/60 + ":" + seconds%60);
    }
    private void catchRed()
    {
        if(moveCircle.getFill()==Color.RED)
        {
            redF = System.currentTimeMillis();
            long result = redF - redS;

            System.out.println(result);
            if (result > react)
            {
                Platform.runLater(() -> testResult("lose"));
            }
            moveCircle.setFill(Color.GREEN);
        }
    }
    @FXML
    private void startTest(MouseEvent mouseEvent)
    {

        testLong= Double.parseDouble(tfTestLong.getText());
        quantity = (int) Math.round(((Double.parseDouble(tfRedQuantity.getText()))*testLong));
        // System.out.println(((Double.parseDouble(tfRedQuantity.getText()))*testLong));
        // System.out.println(quantity);
        react = Long.parseLong(tfTestReact.getText());

        tfTestLong.setFocusTraversable(false);
        tfRedQuantity.setFocusTraversable(false);
        tfTestReact.setFocusTraversable(false);

        tfTestLong.setVisible(false);
        tfRedQuantity.setVisible(false);
        tfTestReact.setVisible(false);

        mainCircle.setFocusTraversable(true);
        startTime = System.currentTimeMillis();

        startButton.setVisible(false);
        countdownStart();
        int i=0;
        while (i<quantity)
        {
            list.add((int) (Math.random()*testLong));
            //System.out.println(list.get(i));
            i++;
        }

        mainTimmer.schedule(

                new TimerTask() {
                    @Override

                    public void run() {

                        if (moveCircle.getFill() == Color.RED)
                        {
                            Platform.runLater(() -> testResult("lose"));
                        }

                        //System.out.println(secund);
                        int i=0;
                        while (i<quantity)
                        {
                            if (secund!=list.get(i))
                            {
                                i++;
                            }
                            else
                            {
                                System.out.println("red signal");
                                moveCircle.setFill(Color.RED);
                                redS = System.currentTimeMillis();
                                i++;
                            }
                        }

                            moveCircle.setVisible(true);

                            moveCircle.setCenterX(mainCircle.getCenterX() + 350 * Math.cos((secund + 45) * 6 * (Math.PI / 180)));
                            moveCircle.setCenterY(mainCircle.getCenterY() + 350 * Math.sin((secund + 45) * 6 * (Math.PI / 180)));
                            secund += 1;

                           if((System.currentTimeMillis()-startTime)>testLong*1000)
                            {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        testResult("win");
                                    }
                                });

                            }
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                countdownChange((int)testLong-secund+1);
                            }
                        });
                    }
                },0,1000);
    }
    @FXML
    private void catchRedMouse(MouseEvent mouseEvent)
    {
        System.out.println("mouse clicked");
        catchRed();
    }
    @FXML
    private void catchKey(KeyEvent keyEvent)
    {

        if (keyEvent.getCode()== KeyCode.SPACE)
        {
            System.out.println("space key pressed");
            catchRed();
        }
    }
}
