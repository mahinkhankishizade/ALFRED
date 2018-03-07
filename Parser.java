import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Scanner;

public class Parser {

    // variables
    ArrayList<String> hints;
    int[] colorsOfBlocks;
    int[] numbersOfBlocks;
    String html;
    String textFile = "7MarchPuzzleCode";


    public Parser() {
        // initialization
        hints = new ArrayList<String>();
        colorsOfBlocks = new int[25];
        numbersOfBlocks = new int[25];

        html = fetch( "https://www.nytimes.com/crosswords/game/mini");

        // convertToString(); // use for old puzzles
    }

    // getters and setters
    public void setHTMLString( String html) {
        this.html = html;
    }
    public String getHTMLString() {
        return html;
    }
    public int[] getNumbersOfBlocks() { return numbersOfBlocks; }
    public int[] getColorsOfBlocks() { return colorsOfBlocks; }
    public ArrayList<String> getAllHints() { return hints; }

    // for using old puzzles
    public void convertToString() throws FileNotFoundException {
        String entireFileText = new Scanner( new File( textFile + ".txt")).useDelimiter("\\A").next();
        setHTMLString( entireFileText);
        getHints( entireFileText);
        getColors( entireFileText);
        getNumbers( entireFileText);
    }


    // this method fetchs the html code from the website and writes it to the textfile
    public String fetch( String html) {
        try {
            Document doc = Jsoup.connect( html).get();
            String htmlDocument = doc.toString();
            Elements links = doc.select( "link");
            Elements scripts = doc.select( "script");
            for( Element element : links) {
                htmlDocument += element.absUrl( "href");
            }
            for( Element element : scripts) {
                htmlDocument += element.absUrl("src");
            }

            // save html code in the text file
            String path = "C:/Users/User/Desktop/School/cs461/Project/" + textFile + ".txt";
            Files.write( Paths.get(path), htmlDocument.getBytes(), StandardOpenOption.CREATE);

            getHints( htmlDocument);
            getColors( htmlDocument);
            getNumbers( htmlDocument);

            return htmlDocument;

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void getHints( String html) {
        Document document = Jsoup.parse( html);
        Elements numbers = document.getElementsByClass("Clue-label--2IdMY");
        Elements clues = document.getElementsByClass("Clue-text--3lzl7");

        for( int i = 0; i < numbers.size() && i < clues.size(); i++) {
            hints.add( numbers.get(i).text() + ". " + clues.get(i).text());
            System.out.println( numbers.get(i).text() + ". " + clues.get(i).text());
        }
    }

    // extract the grid part from the text file
    public String cutHTML( String html) {
        String startIndex = "<g data-group=\"cells\"";
        String endIndex = "<g data-group=\"grid\"";

        int start = html.indexOf( startIndex) + startIndex.length();
        int end = html.indexOf( endIndex) - 1;

        return html.substring( start, end);
    }
    // i need to get colors for blocks
    public void getColors( String html) {
        int size = 25;

        // cutting html only cell parts left
        String newHTML = cutHTML( html);

        for (int i = 0; i < size; i++){
            int indexStart = newHTML.indexOf("class=\"") + 7;
            int indexEnd = indexStart + 16;
            String color = newHTML.substring(indexStart, indexEnd);

            if (color.equals("Cell-block--1oNa"))
                colorsOfBlocks[i] = 1; //black
            else
                colorsOfBlocks[i] = 0; //white

            newHTML = newHTML.substring(indexEnd);
        }
        for( int i = 0; i < 25; i++)
            System.out.print(colorsOfBlocks[i] + " ");
        System.out.println("end");
    }

    // i need to get numbers for blocks
    public void getNumbers( String html) {
        int size = 25;

        String newHTML = cutHTML( html);
        Document document = Jsoup.parse( newHTML);
        Elements elements = document.getElementsByTag("g");

        for( int i = 0; i < elements.size() && i < size; i++) {
            String text = elements.get(i).text();
            if( !text.isEmpty()) {
                numbersOfBlocks[i] = Integer.parseInt( text);
            }
            else
                numbersOfBlocks[i] = 0;
        }
        for( int i = 0; i < 25; i++)
            System.out.print(numbersOfBlocks[i] + " ");
    }

    // i need to get the blocked grid texts
    public void extractLetters( String html) throws IOException {
        ArrayList<String> letters = new ArrayList<>();

        String url = "https://www.nytimes.com/crosswords/game/mini";
        Document document = Jsoup.connect(url).get();
        String htmlCode = document.html();
        Document doc = Jsoup.parse( htmlCode);
        Elements elements = doc.getElementsByTag("g");
        for( int i = 0; i < elements.size(); i++) {
            if( elements.get(i).hasClass("Cell-cell--1p4gH")) {

            }
        }
    }
}
