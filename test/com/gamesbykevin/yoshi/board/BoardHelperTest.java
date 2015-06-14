package com.gamesbykevin.yoshi.board;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.gamesbykevin.yoshi.board.BoardHelper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import com.gamesbykevin.yoshi.board.piece.Piece;


/**
 * Test the methods in BoardHelper
 * @author GOD
 */
public final class BoardHelperTest 
{
    private List<Piece> pieces;
    
    public BoardHelperTest()
    {
        //create a new list
        this.pieces = new ArrayList<>();
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() 
    {
    }
    
    @Before
    public void setUp() 
    {
        Piece piece = new Piece(Piece.TYPE_GOOMBA);
        piece.placePiece();
        
        //add piece to the list
        pieces.add(piece);
        
        assertTrue(BoardHelper.hasYoshi(pieces));
    }
    
    @After
    public void tearDown() 
    {
        this.pieces.clear();
        this.pieces = null;
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}