import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.EventHandler;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main extends Application{

    final int CELLWIDTH = 50;
    final int CELLHEIGHT = 50;
    Stage window;
    Scene scene1, scene2;
    Button button1;
    Label date;
    BorderPane border;
    ArrayList<String> clueList;
    ArrayList<Label> labelList;
    ArrayList<Rectangle> cellList;
    ArrayList<TextField> textList;
    Label[] numLabelList;
    int[] blockColors;
    Parser parser;
    Button solution;
    Label across, down;
    GridPane gridPane;
    VBox cluePane;
    HBox crossword, label_and_button;
    SolutionBox box;

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        parser = new Parser();
        box = new SolutionBox();

        StackPane layout = new StackPane();

        // add background image
        Image image = new Image(Paths.get("C:/Users/User/Desktop/School/cs461/Project/background.jpg").toUri().toString(), true);
        layout.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));

        // initialize
        clueList = new ArrayList<>();
        clueList = parser.getAllHints();
        window = primaryStage;
        labelList = new ArrayList<>();
        cellList = new ArrayList<>();
        textList = new ArrayList<>();
        blockColors = parser.getColorsOfBlocks();
        numLabelList = new Label[25];
        border = new BorderPane();

        for(int i = 0; i < 10; i++){
            labelList.add( new Label( clueList.get(i) ));
        }

        // style labels
        styleLabels();
        // Create Cells
        fillCells();
        // adding clues to VBox
        addToVbox();
        // creating grid and adding cells
        createGridPane();
        // create HBoxes
        createHBoxes();
       // createTextFields();

        // add to borderpane
        border.setTop(label_and_button);
        border.setCenter(crossword);
        border.setPadding(new Insets(40, 50, 10, 50));

        layout.getChildren().add(border);

        // add style
        String style = this.getClass().getResource("style.css").toExternalForm();
        scene1 = new Scene(layout, 710, 450);
        scene1.getStylesheets().add(style);

        window.setScene(scene1);
        window.setTitle("NYT Crossword");
        window.show();
    }

    public void showSolution() throws FileNotFoundException {
        SolutionBox box = new SolutionBox();
        box.display();
    }

    public void styleLabels() {
        // date label
        date = new Label(parser.getPuzzleDate());
        date.setId("date-text");
        date.setTextAlignment(TextAlignment.CENTER);
        date.setPadding(new Insets(20, 300, 30, 10));

        // solution button
        solution = new Button("Show Solution");
        solution.setId("green");
        solution.setOnAction(e -> {
            try {
                showSolution();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });

        // across and down labels
        across = new Label("Across");
        across.setId("across_and_down");
        down = new Label("Down");
        down.setId("across_and_down");
    }

    public void createGridPane() {
        gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(0);
        gridPane.setHgap(0);

        int row = -1, col = 0;
        for(int i = 0; i < 25; i++){
            if(col % 5 == 0){
                col = 0;
                row += 1;
            }
            GridPane.setConstraints(cellList.get(i), col, row);
            //GridPane.setConstraints(textList.get(i), col, row);
            col += 1;
        }

        for(Rectangle rect : cellList) {
            gridPane.getChildren().add(rect);
        }

        // making number labels for blocks
        row = -1; col = 0;
        for(int i = 0; i < 25; i++){
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

        for(int i = 0; i < 25; i++){
            if(numLabelList[i] != null){
                gridPane.getChildren().add(numLabelList[i]);
            }
        }
    }

    public void fillCells() {
        int i = 0;
        for (int value : blockColors) {
            // white cells
            if(value == 0){
                Rectangle temp1 = new Rectangle(CELLWIDTH, CELLHEIGHT);
                temp1.setFill(Color.WHITE);
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
    }

    public void createTextFields() {
        for( int value: blockColors) {
            if( value == 0) {
                TextField area = new TextField();
                textList.add(area);
            }
        }
        for( int i = 0; i < 25; i++) {
            gridPane.getChildren().add(textList.get(i));
        }
    }

    public void addToVbox() {
        cluePane = new VBox(10);
        cluePane.getChildren().add(across);

        for( int i = 0; i < 10; i++){
            if(i == 5)
                cluePane.getChildren().add(down);
            cluePane.getChildren().add(labelList.get(i));
        }
    }

    public void createHBoxes() {
        crossword = new HBox();
        crossword.getChildren().addAll(gridPane, cluePane);
        crossword.setSpacing( 100);

        label_and_button = new HBox();
        label_and_button.getChildren().addAll( date, solution);
        label_and_button.setSpacing( 25);
    }
}
