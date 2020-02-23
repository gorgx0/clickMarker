package com.gorg;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Counter counter = new Counter();
        Pane root = new Pane();
        Scene scene = new Scene(root);
        scene.setFill(Color.web("blue",0.1));
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        Canvas canvas = new Canvas(bounds.getWidth(),bounds.getHeight());
        root.getChildren().add(canvas);
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        scene.setOnMouseClicked(e -> {
            double sceneX = e.getSceneX();
            double sceneY = e.getSceneY();
            double screenX = e.getScreenX();
            double screenY = e.getScreenY();
            gc.setLineWidth(2.0);
            gc.setStroke(Color.RED);
            gc.strokeOval(sceneX-15, sceneY-15,30, 30);
            gc.setFont(Font.font("Monoid",15));
            gc.strokeText(String.valueOf(counter.counter++),counter.counter<10?sceneX-5:sceneX-10, sceneY+6);
            gc.strokeText(String.valueOf(sceneX), sceneX+20, sceneY-5);
            gc.strokeText(String.valueOf(sceneY), sceneX+20, sceneY+15);
        });
        primaryStage.setTitle("This is primary stage");
        primaryStage.setScene(scene);
        primaryStage.setOpacity(0.2d);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(App.class);
    }

    private static class Counter {
        int counter = 0;
    }
}
