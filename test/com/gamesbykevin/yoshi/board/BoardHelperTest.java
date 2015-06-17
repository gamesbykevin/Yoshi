package com.gamesbykevin.yoshi.board;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.gamesbykevin.yoshi.board.piece.Piece;

/**
 * Test the methods in BoardHelper
 * @author GOD
 */
public class BoardHelperTest 
{
    private List<Piece> pieces;
    
    //the game board
    private Board board;
    
    //object used to make random decisions
    private Random random;
    
    //temp piece
    private Piece piece;
    
    @BeforeClass
    public static void setUpClass() 
    {
        //assume these static variables do not change
        assertTrue(BoardHelper.COLUMN_1 == 0);
        assertTrue(BoardHelper.COLUMN_2 == 1);
        assertTrue(BoardHelper.COLUMN_3 == 2);
        assertTrue(BoardHelper.COLUMN_4 == 3);
        assertTrue(BoardHelper.ROW_DROP == 0.5);
        assertTrue(BoardHelper.SPAWN_TOTAL == 2);
        
        //assume this variable will be a nice convenient number
        assertTrue(1 % BoardHelper.ROW_DROP == 0);
    }
    
    @AfterClass
    public static void tearDownClass() 
    {
        
    }
    
    @Before
    public void setUp() throws Exception
    {
        //create a new list
        this.pieces = new ArrayList<>();
        
        //create new board
        this.board = new Board(0);
        
        //create new random object
        this.random = new Random();
    }
    
    @After
    public void tearDown() 
    {
        this.pieces.clear();
        this.pieces = null;
    }
    
    @Test
    public void populateBoardTest() throws Exception
    {
        //populate the board
        BoardHelper.populateBoard(board, random);
        
        //assume that the number of pieces created is equal to 3 rows times the number of columns
        assertTrue(board.getPieces().size() == (3 * Board.COLUMNS));
    }
    
    @Test 
    public void fallingPiecesTest() throws Exception
    {
        piece = new Piece(Piece.TYPE_SQUID);
        piece.placePiece();
        pieces.clear();
        pieces.add(piece);
        assertFalse(BoardHelper.hasFallingPieces(pieces));
        
        piece = new Piece(Piece.TYPE_SQUID);
        pieces.clear();
        pieces.add(piece);
        assertFalse(BoardHelper.hasFallingPieces(pieces));
        
        piece = new Piece(Piece.TYPE_BOO);
        piece.setFrozen(false);
        pieces.clear();
        pieces.add(piece);
        assertTrue(BoardHelper.hasFallingPieces(pieces));
    }

    @Test
    public void dropPiecesTest() throws Exception
    {
        piece = new Piece(Piece.TYPE_SQUID);
        pieces.clear();
        pieces.add(piece);
        
        //start dropping pieces
        BoardHelper.dropStartingPieces(pieces);
        
        //now assume we now have falling pieces
        assertTrue(BoardHelper.hasFallingPieces(pieces));
    }
    
    @Test
    public void existingPiecesTest() throws Exception
    {
        piece = new Piece(Piece.TYPE_SHELL_TOP);
        piece.placePiece();
        pieces.clear();
        pieces.add(piece);
        assertFalse(BoardHelper.hasExistingPieces(pieces));
        
        piece = new Piece(Piece.TYPE_SHELL_TOP);
        piece.markYoshi(0);
        pieces.clear();
        pieces.add(piece);
        assertTrue(BoardHelper.hasExistingPieces(pieces));
        
        piece = new Piece(Piece.TYPE_SHELL_TOP);
        piece.markDestroyed();
        pieces.clear();
        pieces.add(piece);
        assertTrue(BoardHelper.hasExistingPieces(pieces));
    }
    
    @Test
    public void startingPiecesTest() throws Exception
    {
        piece = new Piece(Piece.TYPE_BOO);
        pieces.clear();
        pieces.add(piece);
        assertTrue(BoardHelper.hasStartingPieces(pieces));
        
        piece = new Piece(Piece.TYPE_BOO);
        piece.setRow(0);
        pieces.clear();
        pieces.add(piece);
        assertTrue(BoardHelper.hasStartingPieces(pieces));
        
        piece = new Piece(Piece.TYPE_BOO);
        piece.setRow(0.5);
        pieces.clear();
        pieces.add(piece);
        assertTrue(BoardHelper.hasStartingPieces(pieces));
        
        piece = new Piece(Piece.TYPE_BOO);
        piece.setRow(1.5);
        pieces.clear();
        pieces.add(piece);
        assertFalse(BoardHelper.hasStartingPieces(pieces));
        
        piece = new Piece(Piece.TYPE_BOO);
        piece.setRow(2);
        pieces.clear();
        pieces.add(piece);
        assertFalse(BoardHelper.hasStartingPieces(pieces));
    }
    
    @Test
    public void spawnPiecesTest() throws Exception
    {
        //spawn pieces
        BoardHelper.spawnPieces(board, random);
        
        //assume the number of pieces created
        assertTrue(board.getPieces().size() == BoardHelper.SPAWN_TOTAL);
        
        //check each piece
        for (int i = 0; i < board.getPieces().size(); i++)
        {
            //check the current piece
            piece = board.getPieces().get(i);
            
            //assume piece will be in the first row
            assertTrue(piece.getRow() == 0);
            
            //assume the new piece will be frozen
            assertTrue(piece.isFrozen());
        }
        
        //make sure that the new pieces are not in the same column
        //check each piece
        for (int i = 0; i < board.getPieces().size(); i++)
        {
            //get the current piece
            final Piece piece1 = board.getPieces().get(i);
            
            for (int j = 0; j < board.getPieces().size(); j++)
            {
                //don't check the same piece
                if (i == j)
                    continue;
                
                //get the current piece
                final Piece piece2 = board.getPieces().get(j);
                
                //assume they will be placed in the same starting row
                assertTrue(piece1.getRow() == piece2.getRow());
                
                //assume they will not be placed in the same column
                assertTrue(piece1.getCol() != piece2.getCol());
            }
        }
    }
    
    @Test
    public void canSwapColumnsTest() throws Exception
    {
        piece = new Piece(Piece.TYPE_SQUID);
        piece.setCol(0);
        piece.setTargetCol(1);
        piece.markYoshi(0);
        pieces.clear();
        pieces.add(piece);
        assertFalse(BoardHelper.canSwapColumns(pieces));
        
        piece = new Piece(Piece.TYPE_SQUID);
        piece.setCol(1);
        piece.setTargetCol(1);
        pieces.clear();
        pieces.add(piece);
        assertTrue(BoardHelper.canSwapColumns(pieces));
        
        piece = new Piece(Piece.TYPE_SQUID);
        piece.setCol(1);
        piece.setTargetCol(1);
        piece.markYoshi(0);
        pieces.clear();
        pieces.add(piece);
        assertFalse(BoardHelper.canSwapColumns(pieces));
        
        piece = new Piece(Piece.TYPE_SQUID);
        piece.setCol(1);
        piece.setTargetCol(1);
        piece.markDestroyed();
        pieces.clear();
        pieces.add(piece);
        assertFalse(BoardHelper.canSwapColumns(pieces));
        
        piece = new Piece(Piece.TYPE_SQUID);
        piece.setCol(2);
        piece.setTargetCol(1);
        pieces.clear();
        pieces.add(piece);
        assertFalse(BoardHelper.canSwapColumns(pieces));
    }
    
    @Test
    public void isSwappingColumnsTest() throws Exception
    {
        piece = new Piece(Piece.TYPE_SQUID);
        piece.setCol(1);
        piece.setTargetCol(1);
        pieces.clear();
        pieces.add(piece);
        assertFalse(BoardHelper.isSwappingColumns(pieces));
        
        piece = new Piece(Piece.TYPE_SQUID);
        piece.setCol(4);
        piece.setTargetCol(1);
        pieces.clear();
        pieces.add(piece);
        assertTrue(BoardHelper.isSwappingColumns(pieces));
    }
    
    @Test
    public void startSwapTest() throws Exception
    {
        //clear
        board.getPieces().clear();
        
        piece = new Piece(Piece.TYPE_SQUID);
        piece.setFrozen(false);
        piece.setPlaced(true);
        piece.setRow(0);
        piece.setCol(1);
        board.getPieces().add(piece);
        
        piece = new Piece(Piece.TYPE_SQUID);
        piece.setFrozen(false);
        piece.setPlaced(true);
        piece.setRow(0);
        piece.setCol(0);
        board.getPieces().add(piece);
        
        BoardHelper.startSwap(board, 0, 1);
        
        for (int i = 0; i < board.getPieces().size(); i++)
        {
            //get current piece
            piece = board.getPieces().get(i);
            
            //assume we don't have the target column as we just swapped pieces
            assertTrue(!piece.hasTargetCol());
        }
    }
    
    @Test
    public void manageDestroyedPiecesTest() throws Exception
    {
        //clear
        board.getPieces().clear();
        
        piece = new Piece(Piece.TYPE_SQUID);
        piece.markDestroyed();
        piece.getSpriteSheet().getSpriteSheetAnimation().setFinished(true);
        board.getPieces().add(piece);
        
        BoardHelper.manageDestroyedPieces(board, 0);
        
        //assume it will be removed
        assertTrue(board.getPieces().isEmpty());
    }
    
    @Test
    public void swapPieceTest() throws Exception
    {
        int startCol = 1;
        int targetCol = 2;
        
        piece = new Piece(Piece.TYPE_SQUID);
        piece.setCol(startCol);
        piece.setTargetCol(targetCol);
        
        //swap the piece
        BoardHelper.swapPiece(piece, 0);
        
        //the destination is greater so the current column location should be greater that the start
        assertTrue(piece.getCol() > startCol);
        
        
        startCol = 2;
        targetCol = 1;
        
        piece = new Piece(Piece.TYPE_SQUID);
        piece.setCol(startCol);
        piece.setTargetCol(targetCol);
        
        //swap the piece
        BoardHelper.swapPiece(piece, 0);
        
        //the destination is small so the current column location should be less that the start
        assertTrue(piece.getCol() < startCol);
    }
    
    @Test
    public void destroyedPiecesTest() throws Exception
    {
        piece = new Piece(Piece.TYPE_SHELL_TOP);
        piece.placePiece();
        pieces.clear();
        pieces.add(piece);
        assertFalse(BoardHelper.hasDestroyedPieces(pieces));
        assertFalse(BoardHelper.hasDestroyedTopShell(pieces));
        assertFalse(BoardHelper.getDestroyedPieceCount(pieces) > 0);
        
        piece = new Piece(Piece.TYPE_SHELL_TOP);
        piece.markDestroyed();
        pieces.clear();
        pieces.add(piece);
        assertTrue(BoardHelper.hasDestroyedPieces(pieces));
        assertTrue(BoardHelper.hasDestroyedTopShell(pieces));
        assertTrue(BoardHelper.getDestroyedPieceCount(pieces) > 0);
        
        piece = new Piece(Piece.TYPE_PLANT);
        piece.markDestroyed();
        pieces.clear();
        pieces.add(piece);
        assertTrue(BoardHelper.hasDestroyedPieces(pieces));
        assertFalse(BoardHelper.hasDestroyedTopShell(pieces));
        assertTrue(BoardHelper.getDestroyedPieceCount(pieces) > 0);
    }
    
    @Test
    public void placedPiecesTest() throws Exception
    {
        piece = new Piece(Piece.TYPE_GOOMBA);
        piece.placePiece();
        pieces.clear();
        pieces.add(piece);
        assertTrue(BoardHelper.hasPlacedPieces(pieces));
        
        piece = new Piece(Piece.TYPE_GOOMBA);
        piece.markYoshi(0);
        pieces.clear();
        pieces.add(piece);
        assertTrue(BoardHelper.hasPlacedPieces(pieces));
        
        piece = new Piece(Piece.TYPE_GOOMBA);
        piece.markDestroyed();
        pieces.clear();
        pieces.add(piece);
        assertTrue(BoardHelper.hasPlacedPieces(pieces));
        
        piece = new Piece(Piece.TYPE_GOOMBA);
        pieces.clear();
        pieces.add(piece);
        assertFalse(BoardHelper.hasPlacedPieces(pieces));
    }
    
    /**
     * Check the user friendly methods for checking yoshi
     */
    @Test
    public void yoshiTest() throws Exception
    {
        piece = new Piece(Piece.TYPE_GOOMBA);
        piece.markYoshi(0);
        piece.placePiece();
        pieces.clear();
        pieces.add(piece);
        
        //assume the pieces list has a yoshi
        assertTrue(BoardHelper.hasYoshi(pieces));
        
        //assume the yoshi size count is greater than 0
        assertTrue(BoardHelper.getYoshiSize(pieces) > 0);
        
        
        piece = new Piece(Piece.TYPE_GOOMBA);
        piece.placePiece();
        pieces.clear();
        pieces.add(piece);
        
        //assume this piece is not a yoshi
        assertFalse(BoardHelper.hasYoshi(pieces));
        
        //assume the yoshi size count is not greater than 0
        assertFalse(BoardHelper.getYoshiSize(pieces) > 0);
    }
    
    @Test
    public void hasLosingBoardTest() throws Exception
    {
        piece = new Piece(Piece.TYPE_GOOMBA);
        piece.setRow(0);
        piece.placePiece();
        pieces.clear();
        pieces.add(piece);
        
        //assume this is a losing board
        assertTrue(BoardHelper.hasLosingBoard(pieces));
        
        
        piece = new Piece(Piece.TYPE_GOOMBA);
        piece.setRow(0);
        piece.setFrozen(true);
        pieces.clear();
        pieces.add(piece);
        
        //assume this isn't a losing board
        assertFalse(BoardHelper.hasLosingBoard(pieces));
        
        
        piece = new Piece(Piece.TYPE_GOOMBA);
        piece.setRow(0);
        piece.setPlaced(true);
        pieces.clear();
        pieces.add(piece);
        
        //assume this isn't a losing board
        assertTrue(BoardHelper.hasLosingBoard(pieces));
        
        
        piece = new Piece(Piece.TYPE_GOOMBA);
        piece.setPlaced(true);
        piece.setRow(1);
        pieces.clear();
        pieces.add(piece);
        
        //assume this isn't a losing board
        assertFalse(BoardHelper.hasLosingBoard(pieces));
    }
}