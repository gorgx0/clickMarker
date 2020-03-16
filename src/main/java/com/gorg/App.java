package com.gorg;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.List;


public class App extends Application {

    public static final Color BCKGRND = Color.web("blue", 0.1);
    List<Point2D> points = new ArrayList<>();
    private GraphicsContext gc;
    ContextMenuLocal menuLocal ;
    private Point2D relative;
    private Counter counter;

    @Override
    public void start(Stage primaryStage) throws Exception {
        counter = new Counter();
        menuLocal = new ContextMenuLocal(counter, this);
        Pane root = new Pane();
        Scene scene = new Scene(root);
        scene.setFill(BCKGRND);
        root.setStyle("-fx-background-color: rgba(0,0,255,0.1)");
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        Canvas canvas = new Canvas(bounds.getWidth(),bounds.getHeight());
        root.getChildren().add(canvas);
        gc = canvas.getGraphicsContext2D();


        scene.setOnMouseClicked(e -> {
            double sceneX = e.getSceneX();
            double sceneY = e.getSceneY();
            MouseButton button = e.getButton();
            switch (button) {
                case PRIMARY:
                    addPoint(counter, sceneX, sceneY);
                    break;
                case SECONDARY:
                    menuLocal.show(primaryStage,sceneX,sceneY);
                    break;
            }
        });
        primaryStage.setTitle("This is primary stage");
        primaryStage.setScene(scene);
        primaryStage.setOpacity(0.2d);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    private void addPoint(Counter counter, double sceneX, double sceneY) {
        putPoint(String.valueOf(counter.counter++), sceneX, sceneY, Color.RED);
        points.add(new Point2D(sceneX, sceneY));
    }

    private void putPoint(String marker, double sceneX, double sceneY, Color color) {
        gc.setLineWidth(2.0);
        gc.setStroke(color);
        gc.strokeOval(sceneX-15, sceneY-15,30, 30);
        gc.setFont(Font.font("Monoid",15));
        gc.strokeText(String.valueOf(marker),marker.length()==1?sceneX-5:sceneX-10, sceneY+6);
        gc.strokeText(String.valueOf(sceneX), sceneX+20, sceneY-5);
        gc.strokeText(String.valueOf(sceneY), sceneX+20, sceneY+15);
    }

    public static void main(String[] args) {
        launch(App.class);
    }

    private static class Counter {
        int counter = 0;

        public void reset() {
            counter = 0;
        }
    }

    class ContextMenuLocal {

        ContextMenu contextMenu = new ContextMenu();
        App app ;
        Counter counter ;
        private double clickX;
        private double clickY;

        public ContextMenuLocal(Counter counter, App app) {
            this.counter = counter;
            this.app = app;
            MenuItem resetPointsMenuItem = new MenuItem("Reset points");
            resetPointsMenuItem.setOnAction(e->{
                app.clearPoints();
            });
            MenuItem setRelativePointMenuItem = new MenuItem("Set relative point");
            setRelativePointMenuItem.setOnAction(e->{
                app.setRelative(this.clickX, this.clickY);
            });
            MenuItem setBoundariesMenuItem = new MenuItem("Set boundaries");
            contextMenu.getItems().add(resetPointsMenuItem);
            contextMenu.getItems().add(setRelativePointMenuItem);
            contextMenu.getItems().add(setBoundariesMenuItem);
        }

        public void show(Stage primaryStage, double sceneX, double sceneY) {
            this.clickX = sceneX;
            this.clickY = sceneY;
            contextMenu.show(primaryStage,sceneX,sceneY);
        }
    }

    private void clearPoints() {
        points.forEach(p -> {
            gc.clearRect(p.getX()-25, p.getY()-25, 130, 50);
        });
        points.clear();
        counter.reset();
        if(this.relative!=null) {
            clearRelativePoint();
        }
    }

    private void clearRelativePoint() {
        gc.clearRect(this.relative.getX()-25, this.relative.getY()-25,130,50);
        this.relative = null;
    }

    private void setRelative(double clickX, double clickY) {
        if(this.relative != null) {
            clearRelativePoint();
        }
        putPoint("R", clickX, clickY, Color.GREEN);
        this.relative = new Point2D(clickX, clickY);
    }
}
