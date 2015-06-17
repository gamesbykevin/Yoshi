package com.gamesbykevin.yoshi.player;

import com.gamesbykevin.yoshi.board.Board;
import com.gamesbykevin.yoshi.board.BoardHelper;
import com.gamesbykevin.yoshi.board.piece.Piece;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Random;

/**
 * CpuHelper unit test
 * @author GOD
 */
public class CpuHelperTest 
{
    private Cpu cpu;
    
    @BeforeClass
    public static void setUpClass() throws Exception
    {
        boolean multiplayer = false;
        Cpu cpu;
        cpu = new Cpu(null, multiplayer, Player.INDEX_DIFFICULTY_EASY);
        cpu = new Cpu(null, multiplayer, Player.INDEX_DIFFICULTY_MEDIUM);
        cpu = new Cpu(null, multiplayer, Player.INDEX_DIFFICULTY_HARD);
        
        multiplayer = true;
        cpu = new Cpu(null, multiplayer, Player.INDEX_DIFFICULTY_EASY);
        cpu = new Cpu(null, multiplayer, Player.INDEX_DIFFICULTY_MEDIUM);
        cpu = new Cpu(null, multiplayer, Player.INDEX_DIFFICULTY_HARD);
    }
    
    @Test
    public void getFallingPiecesTest() throws Exception
    {
        cpu = new Cpu(null, false, Player.INDEX_DIFFICULTY_EASY);
        
        assertTrue(CpuHelper.getFallingPieces(cpu.getBoard().getPieces()).isEmpty());
        
        BoardHelper.spawnPieces(cpu.getBoard(), new Random());
        BoardHelper.dropStartingPieces(cpu.getBoard().getPieces());
        
        assertFalse(CpuHelper.getFallingPieces(cpu.getBoard().getPieces()).isEmpty());
        assertTrue(CpuHelper.getFallingPieces(cpu.getBoard().getPieces()).size() == BoardHelper.SPAWN_TOTAL);
    }
    
    @Test
    public void getTopPieceTest() throws Exception
    {
        cpu = new Cpu(null, false, Player.INDEX_DIFFICULTY_EASY);
        
        final int col = 2;
        
        assertNull(CpuHelper.getTopPiece(cpu.getBoard().getPieces(), col));
        
        Piece piece = new Piece(Piece.TYPE_BOO);
        piece.setCol(col);
        piece.placePiece();
        
        cpu.getBoard().add(piece);
        
        assertTrue(CpuHelper.getTopPiece(cpu.getBoard().getPieces(), col).getId() == piece.getId());
    }
    
    @Test
    public void getRowHeightTest() throws Exception
    {
        cpu = new Cpu(null, false, Player.INDEX_DIFFICULTY_EASY);
        
        final int col = 2;
        
        assertTrue(CpuHelper.getRowHeight(cpu.getBoard(), col) == 0);
        
        Piece piece = new Piece(Piece.TYPE_BOO);
        piece.setCol(col);
        piece.setRow(Board.ROWS - 1);
        piece.placePiece();
        cpu.getBoard().add(piece);
        
        piece = new Piece(Piece.TYPE_BOO);
        piece.setCol(col);
        piece.setRow(Board.ROWS - 2);
        piece.placePiece();
        cpu.getBoard().add(piece);
        
        assertTrue(CpuHelper.getRowHeight(cpu.getBoard(), col) == 2);
    }
    
    @Test
    public void getBottomShellCountTest() throws Exception
    {
        cpu = new Cpu(null, false, Player.INDEX_DIFFICULTY_EASY);
        
        final int col = 2;
        
        assertTrue(CpuHelper.getBottomShellCount(cpu.getBoard(), col) == 0);
        
        Piece piece = new Piece(Piece.TYPE_BOO);
        piece.setCol(col);
        piece.placePiece();
        cpu.getBoard().add(piece);
        
        assertTrue(CpuHelper.getBottomShellCount(cpu.getBoard(), col) == 0);
        
        piece = new Piece(Piece.TYPE_SHELL_BOTTOM);
        piece.setCol(col);
        piece.placePiece();
        cpu.getBoard().add(piece);
        
        assertTrue(CpuHelper.getBottomShellCount(cpu.getBoard(), col) == 0);
        
        cpu = new Cpu(null, false, Player.INDEX_DIFFICULTY_EASY);
        
        piece = new Piece(Piece.TYPE_SHELL_BOTTOM);
        piece.setCol(col);
        piece.setRow(Board.ROWS);
        piece.placePiece();
        cpu.getBoard().add(piece);
        
        piece = new Piece(Piece.TYPE_SHELL_BOTTOM);
        piece.setCol(col);
        piece.setRow(Board.ROWS - 1);
        piece.placePiece();
        cpu.getBoard().add(piece);
        
        assertTrue(CpuHelper.getBottomShellCount(cpu.getBoard(), col) == 1);
    }
}