package com.example.minesweeper;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MineSweeperOne extends Application {
    private static int cellSize = 40;
    private static int numX = 10;
    private static int numY = 10;
    private Cell[][] grid = new Cell[numX][numY];
    private Scene scene;
    private Parent CreateContent(){
        Pane root = new Pane();
        root.setPrefSize(cellSize*numX,cellSize*numY);
        for(int y = 0; y < numY; y++){
            for(int x = 0; x < numX; x++){
                double isBomb = Math.random()*11;
                if(isBomb > 9){
                    isBomb = 9;
                }else {
                    isBomb = 0;
                }
                Cell cell = new Cell(x,y,(int)isBomb);
                grid[x][y] = cell;
                root.getChildren().add(cell);
            }
        }
        for(int y = 0; y < numY; y++){
            for(int x = 0; x < numX; x++){
                Cell cell = grid[x][y];
                int bombs = (int)Neighbors(cell).stream().filter(nCell -> nCell.value==9).count();
                if(cell.value ==9){
                    continue;
                }
                if(bombs > 0){
                    cell.text.setText(String.valueOf(bombs));
                }
            }
        }
        return root;
    }
    private List<Cell> Neighbors(Cell cell){
        List<Cell> neighbors = new ArrayList<>();
        int[] coordX = new int[]{-1, -1, -1, 0, 0, 1, 1, 1};
        int[] coordY = new int[]{-1, 0, 1, -1, 1, -1, 0, 1};
        for (int i = 0; i < 8; i++) {
            int dx = coordX[i];
            int dy = coordY[i];

            int relX = cell.x + dx;
            int relY = cell.y + dy;

            if(relX >= 0 && relX < numX && relY >= 0 && relY < numY) {
                neighbors.add(grid[relX][relY]);
            }
        }
        return neighbors;
    }
    private class Cell extends StackPane{
        int x, y, value;
        private boolean isOpen = false;
        private Text text = new Text();
        private Rectangle border = new Rectangle(cellSize-2, cellSize-2);
        public Cell(int x, int y, int value) {
            this.x = x;
            this.y = y;
            this.value = value;

            border.setStroke(Color.BLACK);
            text.setFill(Color.BLACK);
            text.setFont(Font.font(18));
            text.setText(value == 9 ? "X" : "");
            text.setVisible(false);

            getChildren().addAll(border, text);
            setTranslateX((x*cellSize));
            setTranslateY((y*cellSize));

            setOnMouseClicked(e -> open()); //NEED EXPLANATION
        }
        public void open(){
            if(isOpen){
                return;
            }
            if(value == 9){
                System.out.println("Game Over");
                scene.setRoot(CreateContent());
                return;
            }

            isOpen = true;
            text.setVisible(true);
            border.setFill(null);
            if(text.getText().isEmpty()){
                Neighbors(this).forEach(Cell::open);
            }
        }
    }
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(CreateContent());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}