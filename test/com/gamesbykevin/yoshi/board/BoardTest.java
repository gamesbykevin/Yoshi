package com.gamesbykevin.yoshi.board;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import com.gamesbykevin.yoshi.player.Player;
import com.gamesbykevin.yoshi.board.piece.Piece;
import junit.framework.Assert;

/**
 * Test the methods in Board
 * @author GOD
 */
public class BoardTest 
{
    //the game board
    private Board board;
    
    //a temp piece
    private Piece piece;
    
    @BeforeClass
    public static void setUpClass() 
    {
        //assume these variables will not change
        assertTrue(Board.COLUMNS == 4);
        assertTrue(Board.ROWS == 9);
    }
    
    @AfterClass
    public static void tearDownClass() 
    {
        
    }
    
    @Before
    public void setUp() throws Exception
    {
        //create a board with each difficulty
        board = new Board(Player.INDEX_DIFFICULTY_EASY);
        board = new Board(Player.INDEX_DIFFICULTY_MEDIUM);
        board = new Board(Player.INDEX_DIFFICULTY_HARD);
    }
    
    @After
    public void tearDown() 
    {
        board.dispose();
        board = null;
    }
    
    @Test
    public void addTest() throws Exception
    {
        piece = new Piece(Piece.TYPE_BOO);
        piece.setCol(4);
        
        //add piece
        board.add(piece);
        
        //assume not empty
        assertTrue(!board.getPieces().isEmpty());
        
        //assume the piece has the current column is the target
        assertTrue(piece.hasTargetCol());
    }
    
    @Test
    public void removeTest() throws Exception
    {
        //create piece
        piece = new Piece(Piece.TYPE_BOO);
        
        //add piece
        board.add(piece);
        
        //remove the piece
        board.remove(piece);
        
        //assume empty
        assertTrue(board.getPieces().isEmpty());
    }
    
    @Test
    public void getPieceTest() throws Exception
    {
        //create piece
        piece = new Piece(Piece.TYPE_BOO);
        
        //add piece
        board.add(piece);
        
        //assume a piece is not found since we will avoid checking this piece
        assertNull(board.getPiece(piece));
        
        //assume null as the piece is not in this location here
        assertNull(board.getPiece(0, 1, Piece.NO_ID));
        
        //assume not null as the piece is in this location and we aren't avoiding this piece
        assertNotNull(board.getPiece(0, 0, Piece.NO_ID));
        
        //assume null because even thought the piece is here, we are avoiding the piece by providing the id
        assertNull(board.getPiece(0, 0, piece.getId()));
    }
    
    @Test
    public void setGameResultTest() throws Exception
    {
        board = new Board(Player.INDEX_DIFFICULTY_EASY);
        board.setGameResult(false);
        
        //assume the game is flagged as over
        assertTrue(board.hasGameOver());
        
        board = new Board(Player.INDEX_DIFFICULTY_EASY);
        board.setGameResult(true);
        
        //assume the game is flagged as over
        assertTrue(board.hasGameOver());
    }
}