package com.gamesbykevin.yoshi.player;

import com.gamesbykevin.yoshi.board.Board;
import com.gamesbykevin.yoshi.board.piece.Piece;

import java.util.ArrayList;
import java.util.List;

/**
 * This class will provide custom methods to assist with the cpu
 * @author GOD
 */
public final class CpuHelper 
{
    /**
     * Get the falling pieces.<br>
     * The falling pieces are not frozen and not placed
     * @param pieces List of pieces to check
     * @return List of pieces
     */
    protected static List<Piece> getFallingPieces(final List<Piece> pieces)
    {
        //our result list
        List<Piece> result = new ArrayList<>();
        
        for (int i = 0; i < pieces.size(); i++)
        {
            //get the current piece
            Piece piece = pieces.get(i);
            
            //we don't want these pieces
            if (piece.isPlaced() || piece.isYoshi() || piece.isDestroyed() || piece.isFrozen())
                continue;
            
            //add piece to our list
            result.add(piece);
        }
        
        //return our result
        return result;
    }
    
    /**
     * Get the top piece for the specified column.<br>
     * We will only checked pieces that have been placed.<br>
     * This way we aren't looking at the starting pieces.
     * @param pieces List of pieces to check
     * @param col The column we want to check
     * @return The top piece for the specified column, if none found null is returned
     */
    protected static Piece getTopPiece(final List<Piece> pieces, final int col)
    {
        Piece tmp = null;
        
        //keep track of the highest row
        int row = Board.ROWS;
        
        for (int i = 0; i < pieces.size(); i++)
        {
            //get the current piece
            Piece piece = pieces.get(i);
            
            //we only want to check the placed pieces
            if (!piece.isPlaced())
                continue;
            
            //don't check if the columns don't match
            if (piece.getCol() != col)
                continue;
            
            //if we found a row higher
            if (piece.getRow() < row)
            {
                //save the higher row found
                row = (int)piece.getRow();
                
                //save the winning piece
                tmp = piece;
            }
        }
        
        //return our result
        return tmp;
    }
    
    /**
     * Get the row height.<br>
     * We will count the number of pieces that are placed in the specified column.<br>
     * This will be the height.
     * @param board The board we are playing on
     * @param col The column we want to check
     * @return The number of rows we have pieces placed in the specified column
     */
    protected static int getRowHeight(final Board board, final int col)
    {
        //the default height will be 0
        int height = 0;
        
        //check each row for pieces
        for (int row = 0; row < Board.ROWS; row++)
        {
            //get the piece at this location
            final Piece piece = board.getPiece(col, row, Piece.NO_ID);
            
            //skip if no piece found
            if (piece == null)
                continue;
            
            //we only want to check the placed pieces
            if (!piece.isPlaced() || piece.isDestroyed() || piece.isYoshi())
                continue;
            
            //don't check if the columns don't match
            if (piece.getCol() != col)
                continue;
            
            //increase the height
            height++;
        }
        
        //return the height found
        return height;
    }
    
    /**
     * Get the bottom shell count.<br>
     * Count the number of pieces found above the first bottom shell in the row.<br>
     * We will only check pieces that have already been placed in the specified column.<br>
     * @param board The board we are playing on
     * @param col The column we want to check
     * @return The number of pieces found above the first bottom shell, if the bottom shell does not exist return 0
     */
    protected static int getBottomShellCount(final Board board, final int col)
    {
        //count the number of pieces until we find a shell
        int count = 0;
        
        //check each row until we find the bottom shell
        for (int row = 0; row < Board.ROWS; row++)
        {
            //get the piece at this location
            final Piece piece = board.getPiece(col, row, Piece.NO_ID);
            
            //skip if no piece found
            if (piece == null)
                continue;
            
            //we only want to check the placed pieces
            if (!piece.isPlaced() || piece.isDestroyed() || piece.isYoshi() || piece.isFrozen())
                continue;
            
            //don't check if the columns don't match
            if (piece.getCol() != col)
                continue;
            
            //increase the count
            count++;
            
            //if we found our shell, return the count
            if (piece.getType() == Piece.TYPE_SHELL_BOTTOM)
                return count;
        }
        
        //we didn't find the bottom shell, so return 0
        return 0;
    }
}