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

    // ArrayLists for across hints
    ArrayList<String> hints;

    // ArrayList for colors of the blocks
    int[] colorsOfBlocks; // 1 is white, 0 is black

    // ArrayList for numebrs of the blocks
    int[] numbersOfBlocks; // 1 is white, 0 is black


    String html; //= fetch( "https://www.nytimes.com/crosswords/game/mini");
    String textFile = "7MarchPuzzleCode";


    public Parser() {
        // initializing the ArrayLists
        /*acrossHints = new ArrayList<>();
        acrossHintsOnly = new ArrayList<>();
        acrossHintsNums = new ArrayList<>();
        downHints = new ArrayList<>();
        downHintsOnly = new ArrayList<>();
        downHintsNums = new ArrayList<>();*/
        hints = new ArrayList<String>();
        colorsOfBlocks = new int[25];
        numbersOfBlocks = new int[25];

        html = fetch( "https://www.nytimes.com/crosswords/game/mini");
    }

    public void setHTMLString( String html) {
        this.html = html;
    }

    public String getHTMLString() {
        return html;
    }

    // for using old puzzles
    public void convertToString() throws FileNotFoundException {
        String entireFileText = new Scanner( new File( textFile + ".txt")).useDelimiter("\\A").next();
        setHTMLString( entireFileText);
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
            System.out.println(color);

            if (color.equals("Cell-block--1oNa"))
                colorsOfBlocks[i] = 1; //black
            else
                colorsOfBlocks[i] = 0; //white

            newHTML = newHTML.substring(indexEnd);
        }
        for( int i = 0; i < 25; i++)
            System.out.println(colorsOfBlocks[i] + " ");
    }

    // i need to get numbers for blocks
    public int[] extractNumbers( String html) {
        int size = 25;
        int[] numbers = new int[size];

        Document document = Jsoup.parse( html);
        Elements elements = document.getElementsByTag("g");

        for( int i = 0; i < elements.size() && i < size; i++) {
            String text = elements.get(i).text();
            System.out.println(text);
            /*int l = text.length() - 1;
            while( !text.isEmpty() && (Character.isLetter( text.charAt(i)))) {
                text = text.substring(0 , 1);
                l--;
            }*/
            if( !text.isEmpty()) {
                numbers[i] = Integer.parseInt( text);
            }
            else
                numbers[i] = 0;
        }
        return numbers;
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
