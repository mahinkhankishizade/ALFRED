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
    int[] blockColors;
    Parser parser;

    public static void main(String[] args) throws IOException {
//        ArrayList<String> list = new ArrayList<String>();
//        Parser parser = new Parser();
        //list = parser.findClues();
        //parser.getBlackBlockCoordinates();
       /*for(int i = 0; i < 10; i++ ) {
           System.out.println(list.get(i));
       }*/
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

        for(int i = 0; i < 10; i++){
            labelList.add( new Label( clueList.get(i) ));
        }

        label1 = new Label("New York Times Mini-Crossword Puzzle");
        label1.setTextAlignment(TextAlignment.CENTER);

        Rectangle rect1 = new Rectangle(10, 10, 500, 500);
        rect1.setFill(Color.FLORALWHITE);

        // Create Cells
        int xpos = 0;
        int ypos = -CELLHEIGHT;
        int i = 0;
        for (int value : blockColors) {
            // white cells
            if(i % 5 == 0){
                xpos = 0;
                ypos += CELLHEIGHT;
            }
            if(value == 0){
                //cellList.add(new Rectangle(xpos, ypos, CELLWIDTH, CELLHEIGHT));
                System.out.println("value == 0");
                Rectangle temp1 = new Rectangle(CELLWIDTH, CELLHEIGHT);
                temp1.setFill(Color.TRANSPARENT);
                temp1.setStroke(Color.BLACK);
                temp1.setStrokeWidth(2);
                cellList.add(temp1);
            }
            // black cells
            else{
                System.out.println("value == 1");
                //Rectangle temp = new Rectangle(xpos, ypos, CELLWIDTH, CELLHEIGHT);
                Rectangle temp2 = new Rectangle(CELLWIDTH, CELLHEIGHT);
                temp2.setFill(Color.BLACK);
                cellList.add(temp2);
            }
            i += 1;
            xpos += CELLWIDTH;
        }

        border = new BorderPane();

        VBox cluePane = new VBox(10);
        cluePane.setAlignment(Pos.CENTER_LEFT);
        for( i = 0; i < 10; i++){
            cluePane.getChildren().add(labelList.get(i));
        }


        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(1);
        gridPane.setHgap(1);


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

//        //gridPane.setAlignment(Pos.CENTER);
//        for(Rectangle rect : cellList){
//            gridPane.getChildren().add(rect);
//        }
        //gridPane.getChildren().addAll(label1, rect1);


        border.setTop(label1);

        //border.
        border.setLeft(gridPane);
        border.setRight(cluePane);

        scene1 = new Scene(border,1200, 700);

        window.setScene(scene1);
        window.setTitle("FX");
        window.show();

    }
}
