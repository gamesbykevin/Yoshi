package com.gamesbykevin.yoshi.board.piece;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the methods in piece
 * @author GOD
 */
public class PieceTest 
{
    //the temp piece
    private Piece piece;
    
    @BeforeClass
    public static void setUpClass() throws Exception
    {
        Piece piece = new Piece(Piece.TYPE_GOOMBA);
        assertTrue(piece.getType() == Piece.TYPE_GOOMBA);
        
        piece = new Piece(Piece.TYPE_SQUID);
        assertTrue(piece.getType() == Piece.TYPE_SQUID);
        
        piece = new Piece(Piece.TYPE_BOO);
        assertTrue(piece.getType() == Piece.TYPE_BOO);
        
        piece = new Piece(Piece.TYPE_PLANT);
        assertTrue(piece.getType() == Piece.TYPE_PLANT);
        
        piece = new Piece(Piece.TYPE_SHELL_TOP);
        assertTrue(piece.getType() == Piece.TYPE_SHELL_TOP);
        
        piece = new Piece(Piece.TYPE_SHELL_BOTTOM);
        assertTrue(piece.getType() == Piece.TYPE_SHELL_BOTTOM);
        
        //assume the number of pieces will remain at 6
        assertTrue(Piece.TYPE_TOTAL == 6);
        
        //assume these sizes
        assertTrue(Piece.YOSHI_SIZE_TINY == 2);
        assertTrue(Piece.YOSHI_SIZE_SMALL == 6);
        assertTrue(Piece.YOSHI_SIZE_MEDIUM == 8);
        assertTrue(Piece.YOSHI_SIZE_LARGE == 9);
        
        //assume value will not change
        assertTrue(Piece.NO_ID == -1);
        
        //assume this will be a nice convenient number
        assertTrue((int)(1 % Piece.SWAP_COLUMN_RATE) == 0);
    }
    
    @AfterClass
    public static void tearDownClass() 
    {
        
    }
    
    @Before
    public void setUp() 
    {
        
    }
    
    @After
    public void tearDown() 
    {
        piece.dispose();
        piece = null;
    }
    
    @Test
    public void placePieceTest() throws Exception
    {
        piece = new Piece(Piece.TYPE_BOO);
        piece.placePiece();
        
        //assume placed
        assertTrue(piece.isPlaced());
        
        //can't be frozen and placed
        assertFalse(piece.isFrozen());
    }
    
    @Test
    public void applyGravityTest() throws Exception
    {
        final double y = 0;
        final double row = 0;
        
        piece = new Piece(Piece.TYPE_BOO);
        piece.setY(y);
        piece.setRow(row);
        
        //apply gravity
        piece.applyGravity();
        
        //assume moving south
        assertTrue(piece.getY() > y);
        assertTrue(piece.getRow() > row);
    }
    
    @Test
    public void yoshiTest() throws Exception
    {
        final int size = 10;
        
        piece = new Piece(Piece.TYPE_BOO);
        piece.markYoshi(size);
        
        //assume yoshi since we marked it
        assertTrue(piece.isYoshi());
        
        //assume the numbers match
        assertTrue(piece.getYoshiSize() == size);
    }
    
    @Test
    public void targetColTest() throws Exception
    {
        final int targetCol = 5;
        
        piece = new Piece(Piece.TYPE_BOO);
        piece.setCol(targetCol);
        piece.setTargetCol(targetCol);
        
        //assume they match
        assertTrue(piece.getTargetCol() == targetCol);
        
        //assume they match
        assertTrue(piece.hasTargetCol());
        
        //change the column
        piece.setCol(targetCol - 1);
        
        //assume they don't match
        assertFalse(piece.hasTargetCol());
        
    }
}