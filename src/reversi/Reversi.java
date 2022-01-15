package reversi;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 * CSCI1130 Java Assignment
 * Reversi board game
 * 
 * Students shall complete this class to implement the game.
 * There are debugging, testing and demonstration code for your reference.
 * 
 * I declare that the assignment here submitted is original
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
 * Date        : 2020/11/11
 * 
 * @author based on skeleton code provided by Michael FUNG
 */
public class Reversi {

    // pre-defined class constant fields used throughout this app
    public static final int BLACK = -1;
    public static final int WHITE = +1;
    public static final int EMPTY =  0;
    
    // a convenient constant field that can be used by students
    public final int FLIP  = -1;
    
    // GUI objects representing and displaying the game window and game board
    protected JFrame window;
    protected ReversiPanel gameBoard;
    protected Color boardColor = Color.GREEN;

    
    // a 2D array of pieces, each piece can be:
    //  0: EMPTY/ unoccupied/ out of bound
    // -1: BLACK
    // +1: WHITE
    protected int[][] pieces;
    
    
    // currentPlayer:
    // -1: BLACK
    // +1: WHITE
    protected int currentPlayer;

    
    
    // STUDENTS may declare other fields HERE
    
    
    /**
     * The only constructor for initializing a new board in this app
     */
    public Reversi() {
        window = new JFrame("Reversi");
        gameBoard = new ReversiPanel(this);
        window.add(gameBoard);
        window.setSize(850, 700);
        window.setLocation(100, 50);
        window.setVisible(true);
        
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // use of implicitly extended inner class with overriden method, advanced stuff
        window.addWindowListener(
            new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e)
                {
                    sayGoodbye();
                }
            }
        );


        // a 8x8 board of pieces[1-8][1-8] surrounded by an EMPTY boundary of 10x10 
        pieces = new int[10][10];
        
        pieces[4][4] = WHITE;
        pieces[4][5] = BLACK;
        pieces[5][4] = BLACK;
        pieces[5][5] = WHITE;
        currentPlayer = BLACK;  // black plays first
        gameBoard.updateStatus(pieces, currentPlayer);
    }

    
    
    /**
     * setupDebugBoard for testing END-game condition
     * students can freely make any changes to this method for testing purpose
     * TEMPORARY testing case
     */
    protected void setupDebugBoardEndGame()
    {
        gameBoard.addText("setupDebugBoardEndGame():");

        pieces[4][4] = WHITE;
        pieces[4][5] = BLACK;
        pieces[5][4] = BLACK;
        pieces[5][5] = WHITE;

        currentPlayer = BLACK;  // BLACK plays first
        gameBoard.updateStatus(pieces, currentPlayer);
    }


    /**
     * setupDebugBoard for testing MID-game condition
     * students can freely make any changes to this method for testing purpose
     * TEMPORARY testing case
     */
    protected void setupDebugBoardMidGame()
    {
        gameBoard.addText("setupDebugBoardMidGame():");

        int row, col, distance;
        
        // make all pieces EMPTY
        for (row = 1; row <= 8; row++)
            for (col = 1; col <= 8; col++)
                pieces[row][col] = EMPTY;
        
        // STUDENTS' TEST and EXPERIMENT
        // setup a star pattern as a demonstration, you may try other setups
        // relax, we will NOT encounter array index out of bounds, see below!!
        row = 5;
        col = 3;
        distance = 3;
        
        // beware of hitting the boundary or ArrayIndexOutOfBoundsException
        for (int y_dir = -1; y_dir <= +1; y_dir++)
            for (int x_dir = -1; x_dir <= +1; x_dir++)
            {
                try {
                    int move;
                    // setup some opponents
                    for (move = 1; move <= distance; move++)
                        pieces[row + y_dir * move][col + x_dir * move] = BLACK;

                    // far-end friend piece
                    pieces[row+y_dir * move][col + x_dir*move] = WHITE;
                }
                catch (ArrayIndexOutOfBoundsException e)
                {
                    // intentionally do nothing in this catch block
                    // this is simple and convenient in guarding array OOB
                }
            }
        // leave the center EMPTY for the player's enjoyment
        pieces[row][col] = EMPTY;
        
        // pieces[row][col] = 999;  // try an invalid piece

        
        // restore the fence of 10x10 EMPTY pieces around the 8x8 game board
        for (row = 1; row <= 8; row++)
            pieces[row][0] = pieces[row][9] = EMPTY;
        for (col = 1; col <= 8; col++)
            pieces[0][col] = pieces[9][col] = EMPTY;
      
        
        currentPlayer = WHITE;  // WHITE plays first
        // currentPlayer = 777;    // try an invalid player
        
        gameBoard.updateStatus(pieces, currentPlayer);
    }
    
    
    
    // STUDENTS are encouraged to define other instance methods here
    
    //Note of the parameter 'test' in below methods:
    //If method is true, the mothods of checking only check if there is any chess
    //can be flipped. If test is false, the mothods of checking check the 
    //flipping condition and flip the chess if it's allowed.
    
    
    //This method is used only to aid the method checkValidMove
    //It checks the two sides of the current row from click point 
    //If there is a chance to flip the chess, it will flip them all
    //and return true; 
    //The parameter test: if it's true, then it means only check the place
    //and do not flip any chess. If its false, then it means check and flip
    //NOTE: Though it returns boolean value, it does change the board!
    private boolean checkRow(int row,int col,boolean test){
        //Count the number of chess that can be flipped
        //Make it convenient to locate the flipping area
        int left=0,right=0; 
        //Determine whether there is any chess can be flipped:
        boolean flipLeft=true,flipRight=true;
        //Left side:
        for(int leftCol=col-1;leftCol>=1&&pieces[row][leftCol]!=currentPlayer;leftCol--){
            if(pieces[row][leftCol]==EMPTY){
                flipLeft=false;
                break;
            }
            if(leftCol==1&&pieces[row][leftCol]!=currentPlayer){
                flipLeft=false;
                break;
            }
            left++;
        }
        //Right side:
        for(int rightCol=col+1;rightCol<=8&&pieces[row][rightCol]!=currentPlayer;rightCol++){
            if(pieces[row][rightCol]==EMPTY){
                flipRight=false;
                break;
            }
            if(rightCol==8&&pieces[row][rightCol]!=currentPlayer){
                flipRight=false;
                break;
            }
            right++;
        }
        if(!test){//Flipping process
          if(flipLeft&&left>0){
            for(int move=1;move<=left;move++)
                pieces[row][col-move]=currentPlayer;
        }
          if(flipRight&&right>0){
            for(int move=1;move<=right;move++)
                pieces[row][col+move]=currentPlayer;
        }
        }
        //Can be flipped and the number of latent chess is more than 0:
       return ((flipLeft&&left>0)||(flipRight&&right>0));
    }
    
    //This method is used only to aid the method checkValidMove
    //It checks the two sides of the current column from click point 
    //If there is a chance to flip the chess, it will flip them all
    //and return true; 
    //NOTE: Though it returns boolean value, it does change the board!
    private boolean checkCol(int row,int col,boolean test){
        //Count the number of chess that can be flipped
        //Make it convenient to locate the flipping area
        int up=0,down=0;
        //Determine whether there is any chess can be flipped:
        boolean flipUp=true,flipDown=true;
        //Downward checking:
        for(int downRow=row-1;downRow>=1&&pieces[downRow][col]!=currentPlayer;downRow--){
            if(pieces[downRow][col]==EMPTY){
                flipDown=false;
                break;
            }
            if(downRow==1&&pieces[downRow][col]!=currentPlayer){
                flipDown=false;
                break;
            }
            down++;
        }
        //Upward checking
        for(int upRow=row+1;upRow<=8&&pieces[upRow][col]!=currentPlayer;upRow++){
            if(pieces[upRow][col]==EMPTY){
                flipUp=false;
                break;
            }
            if(upRow==8&&pieces[upRow][col]!=currentPlayer){
                flipUp=false;
                break;
            }
            up++;
        }
        if(!test){//Flipping process
        if(flipUp&&up>0){
            for(int move=1;move<=up;move++)
                pieces[row+move][col]=currentPlayer;
        }
         if(flipDown&&down>0){
             
            for(int move=1;move<=down;move++)
                pieces[row-move][col]=currentPlayer;
        }
        }
        //Can be flipped and the number of latent chess is more than 0:
        return ((flipUp&&up>0)||(flipDown&&down>0));     
     }
        
    //This method is used only to aid the method checkValidMove
    //It checks the right sides of the Hypotenuses from click point 
    //EG:     *
    //      * 
    //     * (Clicked point)
    //      *
    //        *
    //If there is a chance to flip the chess, it will flip them all
    //and return true; 
    //NOTE: Though it returns boolean value, it does change the board!
    private boolean checkRightHypotenuse(int row,int col,boolean test){
         //Count the number of chess that can be flipped
        //Make it convenient to locate the flipping area
        int up=0,down=0;
        //Determine whether there is any chess can be flipped:
        boolean flipUp=true,flipDown=true;
        //Upward checking:
        for(int upRow=row-1,upCol=col+1;upRow>=1&&upCol<=8&&pieces[upRow][upCol]!=currentPlayer;
                upRow--,upCol++){
            if(pieces[upRow][upCol]==EMPTY){
                flipUp=false;
                break;
            }
            if((upRow==1||upCol==8)&&pieces[upRow][upCol]!=currentPlayer){
                flipUp=false;
                break;
            }
            up++ ;           
        }
        //Downward checking:
         for(int downRow=row+1,downCol=col+1;downRow<=8&&downCol<=8&&pieces[downRow][downCol]!=currentPlayer;
                downRow++,downCol++){
            if(pieces[downRow][downCol]==EMPTY){
                flipDown=false;
                break;
            }
            if((downRow==8||downCol==8)&&pieces[downRow][downCol]!=currentPlayer){
                flipDown=false;
                break;
            }
            down++;           
        }
        if(!test){//Flipping process
         if(flipUp&&up>0){
           
            for(int move=1;move<=up;move++)
                pieces[row-move][col+move]=currentPlayer;
        }
         if(flipDown&&down>0){
            
            for(int move=1;move<=down;move++)
                pieces[row+move][col+move]=currentPlayer;
        }
        }
        //Can be flipped and the number of latent chess is more than 0:
       return ((flipUp&&up>0)||(flipDown&&down>0));          
    }   
    
    //This method is used only to aid the method checkValidMove
    //It checks the right sides of the Hypotenuses from click point 
    //EG:     *
    //          *
    //            *
    //          *
    //        *
    //If there is a chance to flip the chess, it will flip them all
    //and return true; 
    //NOTE: Though it returns boolean value, it does change the board!
    private boolean checkLeftHypotenuse(int row,int col,boolean test){
        //Count the number of chess that can be flipped
        //Make it convenient to locate the flipping are:
        int up=0,down=0;
        //Determine whether there is any chess can be flipped:
        boolean flipUp=true,flipDown=true;
        //Upward checking:
        for(int upRow=row-1,upCol=col-1;upRow>=1&&upCol>=1&&pieces[upRow][upCol]!=currentPlayer;
                upRow--,upCol--){
            if(pieces[upRow][upCol]==EMPTY){
                flipUp=false;
                break;
            }
            if((upRow==1||upCol==1)&&pieces[upRow][upCol]!=currentPlayer){
                flipUp=false;
                break;
            }
            up++ ;           
        }
        //Downward checking:
         for(int downRow=row+1,downCol=col-1;downRow<=8&&downCol>=1&&pieces[downRow][downCol]!=currentPlayer;
                downRow++,downCol--){
            if(pieces[downRow][downCol]==EMPTY){
                flipDown=false;
                break;
            }
            if((downRow==8||downCol==1)&&pieces[downRow][downCol]!=currentPlayer){
                flipDown=false;
                break;
            }
            down++;           
        }
         if(!test){//Flipping process
         if(flipUp&&up>0){
           
            for(int move=1;move<=up;move++)
                pieces[row-move][col-move]=currentPlayer;
        }
         if(flipDown&&down>0){
         
            for(int move=1;move<=down;move++)
                pieces[row+move][col-move]=currentPlayer;
        }
         }
       //Can be flipped and the number of latent chess is more than 0:
      return ((flipUp&&up>0)||(flipDown&&down>0));     
    }  
    
    
    //If test==true, then only check the place, DO NOT FLIP.
    //If test==false, check, if valid,flip.
    //This method is used to aid the method userClicked
    //Check if the clicking position is a valid placing point
    private boolean checkValidMove(int row,int col,boolean test){
        //If it is not empty: return false
        if(pieces[row][col]!=EMPTY)
            return false;
        //If there is no chess can be flipped: return false.
        if(checkRow(row,col,test)|checkCol(row,col,test)|
                checkLeftHypotenuse(row,col,test)| checkRightHypotenuse(row,col,test))
            return true;
        return false;
    }
    
    //This method is used to aid the method userClicked       
    //After any click, this method would detect if there will be a 
    //forced move or not.
    private boolean haveValidPlace(){
        //Row|Col
        int aRow,aCol;
        //Check from the first place to the last
        //Only check,do not flip
        //test parameter is set to be ture
        for(aRow=1;aRow<=8;aRow++)
            for(aCol=1;aCol<=8;aCol++){
                if(pieces[aRow][aCol]!=EMPTY)
                    continue;
                if(checkValidMove(aRow,aCol,true))//Only check, don't flip
                {   
                    return true;
                }
                
            }
        return false;
    }
    /**
     * STUDENTS' WORK HERE
     * 
     * As this is a GUI application, the gameBoard object (of class ReversiPanel)
     * actively listens to user's actionPerformed.  On user clicking of a
     * ReversiButton object, this callback method will be invoked to do some
     * game processing.
     * 
     * @param row is the row number of the clicked button
     * @param col is the col number of the clicked button
     */
    public void userClicked(int row, int col)
    {
        // major operation of this method:
        
       
        // check for invalid move, i.e., a cell is occupied or
        // a move that cannot take any opponent pieces
        // gameBoard.addText("Invalid move");
        if(checkValidMove(row,col,false)){
        // make a valid move by placing a piece at [row][col]
        // AND flipping some opponent piece(s) in all available directions
        pieces[row][col] = currentPlayer;
        currentPlayer = FLIP * currentPlayer;
        gameBoard.updateStatus(pieces, currentPlayer);
         // check and handle forced pass
         // gameBoard.addText("Forced Pass");
        if(!haveValidPlace()){
            gameBoard.addText("Forced Pass");
            //Change the player:
            currentPlayer = FLIP * currentPlayer;
            gameBoard.updateStatus(pieces, currentPlayer);
            // check and handle double forced pass
            // gameBoard.addText("Double Forced Pass");
            // gameBoard.addText("End game!");
            if(!haveValidPlace()){
                gameBoard.addText("Double Forced Pass");
                gameBoard.addText("End game!");
                return;
            }
                
          }
        }
       
        //If has latent placing postion and the click is invalid:
        else
            gameBoard.addText("Invalid move");
        
    }
    
    
    /**
     * sayGoodbye on System.out, before program termination
     */
    protected void sayGoodbye()
    {
        System.out.println("Goodbye!");
    }

    
    
    // main() method, starting point of basic Reversi game
    public static void main(String[] args) {
        Reversi game = new Reversi();
        game.setupDebugBoardEndGame();
        // comment or remove the following statements for real game play
//        game.setupDebugBoardEndGame();
//        game.setupDebugBoardMidGame();
        // end of sample/ debugging code

        
        // *** STUDENTS need NOT write anything here ***
        
        // this application still runs in the background!!
        // the gameBoard object (of class ReversiPanel) listens and handles
        // user interactions as well as invoking method userClicked(row, col)
        
        // although this is end of main() method
        // THIS IS NOT THE END OF THE APP!
    }
}
