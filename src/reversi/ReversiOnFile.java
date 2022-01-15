package reversi;

import java.awt.Color;
import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * ReversiOnFile is a subclass of Reversi, adding File I/O capabilities
 * for loading and saving game.
 *
 * I declare that the work here submitted is original
 * except for source material explicitly acknowledged,
 * and that the same or closely related material has not been
 * previously submitted for another course.
 * I also acknowledge that I am aware of University policy and
 * regulations on honesty in academic work, and of the disciplinary
 * guidelines and procedures applicable to breaches of such
 * policy and regulations, as contained in the website.
 *
 * University Guideline on Academic Honesty:
 *   http://www.cuhk.edu.hk/policy/academichonesty
 * Faculty of Engineering Guidelines to Academic Honesty:
 *   https://www.erg.cuhk.edu.hk/erg/AcademicHonesty
 *
 * Student Name: Dai Zijie 
 * Student ID  : 1155141656 
 * Date        : 2020/11/26 
 * 
 */
public class ReversiOnFile extends Reversi {
    
    public static final char UNICODE_BLACK_CIRCLE = '\u25CF';
    public static final char UNICODE_WHITE_CIRCLE = '\u25CB';
    public static final char UNICODE_WHITE_SMALL_SQUARE = '\u25AB';
    
    // constructor to give a new look to new subclass game
    public ReversiOnFile()
    {
        window.setTitle("ReversiOnFile");
        gameBoard.setBoardColor(Color.BLUE);
    }


    // STUDENTS' WORK HERE    
    public void loadBoard(String filename)
    {
        // 1)prepare an empty board
        pieces[4][4] = EMPTY;
        pieces[4][5] = EMPTY;
        pieces[5][4] = EMPTY;
        pieces[5][5] = EMPTY;
        // 2) load board and current player from file in UTF-8 Charset encoding
        try{
            Scanner fromFile=new Scanner(new File(filename));
            int row=0,column=1;
            char aChar;
            String aLine;

            while(fromFile.hasNextLine()){
                row++;
                aLine=fromFile.nextLine();
                if(row>8){
                    aChar=aLine.charAt(0);
                    if(aChar==UNICODE_BLACK_CIRCLE)
                        currentPlayer=BLACK;
                    else 
                        currentPlayer=WHITE;
                    break;
                }     
                for( column=0;column<aLine.length();column++){
                    aChar=aLine.charAt(column);
                    switch (aChar){
                        case '\n':
                            break;
                        case UNICODE_BLACK_CIRCLE:
                            pieces[row][column+1]=BLACK;
                            break;
                        case UNICODE_WHITE_CIRCLE:
                            pieces[row][column+1]=WHITE;
                            break;
                        default:
                            pieces[row][column+1]=EMPTY;
                            break;
                    } 
                }      
            }
             // 3) display successful messages and update game status on screen:
            gameBoard.addText("Loaded board from " + filename);
            System.out.println("Loaded board from " + filename);
            gameBoard.updateStatus(pieces, currentPlayer);
            //Close the file stream:
            fromFile.close();
        }
         // 4) in case of any Exception:
        catch(Exception e){
            gameBoard.addText("Cannot load board from " + filename);
            System.out.println("Cannot load board from " + filename);
            // you may implement a method to setupBoardDefaultGame();
        pieces[4][4] = WHITE;
        pieces[4][5] = BLACK;
        pieces[5][5] = WHITE;
        pieces[5][4] = BLACK;
        currentPlayer=BLACK;
        gameBoard.updateStatus(pieces, currentPlayer);
        }
    }

    

    // STUDENTS' WORK HERE    
    public void saveBoard(String filename)
    {
        try{
        // 1) open/overwrite a file for writing text in UTF-8 Charset encoding
        PrintStream inputFile=new PrintStream(filename);
        // 2) save board to the file on 8 lines of 8 Unicode char on each line
        int row=1,column=1;
        int aPosition;
        for(;row<=8;row++){
            for(column=1;column<=8;column++){
                aPosition=pieces[row][column];
                switch(aPosition){
                    case BLACK:
                        inputFile.print(UNICODE_BLACK_CIRCLE);
                        break;
                    case WHITE:
                        inputFile.print(UNICODE_WHITE_CIRCLE);
                        break;
                    case EMPTY:
                        inputFile.print(UNICODE_WHITE_SMALL_SQUARE);
                        break;
                    default://A default place for self-debugging
                        System.out.println("Something wrong with writing in the file!");
                        break;
                }
            }
            inputFile.print('\n');
        }
        // 3) save current player on line 9 and display successful messages
             if(currentPlayer==BLACK)
            inputFile.print(UNICODE_BLACK_CIRCLE);
        else
            inputFile.print(UNICODE_WHITE_CIRCLE);
            gameBoard.addText("Saved board to " + filename);
            System.out.println("Saved board to " + filename);
            //Close the file stream:
            inputFile.close();
        }
        // 4) in case of any Exception:
         catch(Exception e){
            gameBoard.addText("Cannot save board to " + filename);
            System.out.println("Cannot save board to " + filename);
            }
         
    }
        
    

    
    // STUDENTS' WORK HERE    
    /**
     * Override sayGoodbye method of super class, to save board
     */
    // ...
    // {
        // as usual, sayGoodbye...
        
        // ask for save filename
        // String filename = JOptionPane.showInputDialog("Save board filename");
        
        // save board to file
        // ...
    // }
    @Override
    protected void sayGoodbye(){    
        super.sayGoodbye();   
        String filename = JOptionPane.showInputDialog("Save board filename");
        saveBoard(filename);
           
    }
        
    
    // STUDENTS' WORK HERE    
    // main() method, starting point of subclass ReversiOnFile
    public static void main(String[] args) {
        ReversiOnFile game = new ReversiOnFile();
        
        // comment or remove the following statements for real game play
        //game.setupDebugBoardEndGame();
        //game.saveBoard("game4.txt");
        // end of sample/ debugging code
        
        // ask for load filename
        String filename = JOptionPane.showInputDialog("Load board filename");
        // load board from file
        game.loadBoard(filename);
        // ...
    }
}
