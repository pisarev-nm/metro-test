package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

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

    int secund=0;
    double testLong = 20; //длительность теста в секундах
    int quantity = 2; //количество красных светофоров
    long redS, redF, startTime;
    long react = 990; //реакция в миллисекундах
    List<Integer> list = new ArrayList<>();
    Timer mainTimmer = new Timer();



    private void testResult(String res)
        {
            resultLabel.setTranslateX(mainCircle.getCenterX()*2 -300);
            resultLabel.setTranslateY(mainCircle.getCenterY()*2 -150);
            if (res=="win")
        {
            mainTimmer.cancel();
            resultLabel.setText("Тест пройден :)");
            resultLabel.setTextFill(Color.GREEN);
            resultLabel.setVisible(true);
        }
        else
            {
            mainTimmer.cancel();
            resultLabel.setText("Тест не пройден :(");
            resultLabel.setTextFill(Color.RED);
            resultLabel.setVisible(true);
        }
    }
    @FXML
    private void startTest(MouseEvent mouseEvent)
    {
        startTime = System.currentTimeMillis();
        startButton.setVisible(false);
        int i=0;
        while (i<quantity)
        {
            list.add((int) (Math.random()*testLong));
            System.out.println(list.get(i));
            i++;
        }

        mainTimmer.schedule(

                new TimerTask() {
                    @Override

                    public void run() {

                        if (moveCircle.getFill() == Color.RED)
                        {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    testResult("lose");
                                }
                            });

                        }

                        System.out.println(secund);
                        int i=0;
                        while (i<quantity)
                        {
                            if (secund!=list.get(i))
                            {
                                i++;
                            }
                            else
                            {
                                System.out.println("catched!");
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
                    }
                },0,1000);
    }
    @FXML
    private void catchRed(MouseEvent mouseEvent)
    {
        redF = System.currentTimeMillis();

        long result = redF-redS;
        moveCircle.setFill(Color.GREEN);
        System.out.println(result);
        if (result>react)
        {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    testResult("lose");
                }
            });

        }
    }
}
