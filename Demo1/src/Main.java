import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application{

    final int CELLWIDTH = 100;
    final int CELLHEIGHT = 50;
    Stage window;
    Scene scene1, scene2;
    Button button1;
    Label label1;
    BorderPane border;
    ArrayList<String> clueList;
    ArrayList<Label> labelList;
    ArrayList<Rectangle> cellList;
    Label[] numLabelList;
    int[] blockColors;
    Parser parser;

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        parser = new Parser();
        clueList = new ArrayList<>();
        clueList = parser.getAllHints();
        window = primaryStage;
        labelList = new ArrayList<>();
        cellList = new ArrayList<>();
        blockColors = parser.getColorsOfBlocks();
        numLabelList = new Label[25];

        for(int i = 0; i < 10; i++){
            labelList.add( new Label( clueList.get(i) ));
        }

        label1 = new Label("New York Times Mini-Crossword Puzzle");
        label1.setTextAlignment(TextAlignment.CENTER);

        // Create Cells
        int i = 0;
        for (int value : blockColors) {
            // white cells
            if(value == 0){
                Rectangle temp1 = new Rectangle(CELLWIDTH, CELLHEIGHT);
                temp1.setFill(Color.TRANSPARENT);
                temp1.setStroke(Color.BLACK);
                temp1.setStrokeWidth(1);
                cellList.add(temp1);
            }
            // black cells
            else{
                Rectangle temp2 = new Rectangle(CELLWIDTH, CELLHEIGHT);
                temp2.setFill(Color.BLACK);
                cellList.add(temp2);
            }
            i += 1;
        }

        border = new BorderPane();

        // setup clues
        VBox cluePane = new VBox(10);
        cluePane.setAlignment(Pos.TOP_LEFT);
        cluePane.getChildren().add(new Label("Across"));
        for( i = 0; i < 10; i++){
            if(i == 5)
                cluePane.getChildren().add(new Label("Down"));
            cluePane.getChildren().add(labelList.get(i));
        }


        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(0);
        gridPane.setHgap(0);


        int row = -1, col = 0;
        for(i = 0; i < 25; i++){
            if(col % 5 == 0){
                col = 0;
                row += 1;
            }
            GridPane.setConstraints(cellList.get(i), col, row);
            col += 1;
        }

        for(Rectangle rect : cellList){
            gridPane.getChildren().add(rect);
        }

        // making number labels for blocks
        row = -1; col = 0;
        for(i = 0; i < 25; i++){
            if(col % 5 == 0){
                col = 0;
                row += 1;
            }
            if(parser.getNumbersOfBlocks()[i] != 0) {
                numLabelList[i] = new Label(" " + String.valueOf(parser.getNumbersOfBlocks()[i]));
                GridPane.setConstraints(numLabelList[i], col, row);
            }
            else{
                numLabelList[i] = null;
            }
            col += 1;
        }

        for(i = 0; i < 25; i++){
            if(numLabelList[i] != null){
                gridPane.getChildren().add(numLabelList[i]);
            }
        }

//        //gridPane.setAlignment(Pos.CENTER);
//        for(Rectangle rect : cellList){
//            gridPane.getChildren().add(rect);
//        }
        //gridPane.getChildren().addAll(label1, rect1);


        border.setTop(label1);

        //border.
        border.setLeft(gridPane);
        border.setRight(cluePane);

        scene1 = new Scene(border,800, 500);

        window.setScene(scene1);
        window.setTitle("FX");
        window.show();

    }
}
