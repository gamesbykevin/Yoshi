package com.gamesbykevin.yoshi.board;

import com.gamesbykevin.yoshi.board.piece.Piece;
import com.gamesbykevin.yoshi.engine.Engine;

import java.util.ArrayList;

import java.util.List;

/**
 * This class will contain custom methods to help the board
 * @author GOD
 */
public final class BoardHelper 
{
    /**
     * How many pieces to spawn
     */
    public static final int SPAWN_TOTAL = 2;
    
    public static final int COLUMN_1 = 0;
    public static final int COLUMN_2 = 1;
    public static final int COLUMN_3 = 2;
    public static final int COLUMN_4 = 3;
    
    /*
     * The x-coordinates for each column single player
     */
    public static final int SINGLE_PLAYER_COLUMN_1_X = 220;
    public static final int SINGLE_PLAYER_COLUMN_2_X = 280;
    public static final int SINGLE_PLAYER_COLUMN_3_X = 340;
    public static final int SINGLE_PLAYER_COLUMN_4_X = 400;
    
    //the starting y-coordinate
    public static final int SINGLE_PLAYER_ROW_1_Y = 55;
    
    /**
     * The x-coordinates for each column for player 1 in multi-player
     */
    public static final int MULTI_PLAYER_1_COLUMN_1_X = 80;
    public static final int MULTI_PLAYER_1_COLUMN_2_X = 140;
    public static final int MULTI_PLAYER_1_COLUMN_3_X = 200;
    public static final int MULTI_PLAYER_1_COLUMN_4_X = 260;
    
    //the starting y-coordinate
    public static final int MULTI_PLAYER_1_ROW_1_Y = 75;
    
    /**
     * The x-coordinates for each column for player 2 in multi-player
     */
    public static final int MULTI_PLAYER_2_COLUMN_1_X = 380;
    public static final int MULTI_PLAYER_2_COLUMN_2_X = 440;
    public static final int MULTI_PLAYER_2_COLUMN_3_X = 500;
    public static final int MULTI_PLAYER_2_COLUMN_4_X = 560;
    
    //the starting y-coordinate
    public static final int MULTI_PLAYER_2_ROW_1_Y = 75;
    
    //the amount of pixels to drop for everytime we apply gravity to a piece
    public static final int DROP_PIXEL_DISTANCE = 20;
    
    /**
     * Every time we apply gravity we increase the row by this amount
     */
    public static final double ROW_DROP = 0.5;
    
    /**
     * Do we have at least 1 destroyed piece?
     * @param pieces List of pieces to check
     * @return true = yes, false = no
     */
    public static boolean hasDestroyedPiece(final List<Piece> pieces)
    {
        for (int i = 0; i < pieces.size(); i++)
        {
            //if this piece is destroyed return true
            if (pieces.get(i).isDestroyed())
                return true;
        }
        
        //we don't have any destroyed pieces
        return false;
    }
    
    /**
     * Do we currently have any falling pieces?
     * @param pieces List of pieces to check
     * @return true if at least 1 piece is falling, false otherwise
     */
    public static boolean hasFallingPieces(final List<Piece> pieces)
    {
        for (int i = 0; i < pieces.size(); i++)
        {
            //if not frozen we have at least 1 falling piece
            if (!pieces.get(i).isFrozen())
                return true;
        }
        
        //we did not find any falling pieces
        return false;
    }
    
    /**
     * Start dropping pieces at start location
     * @param pieces List of pieces to check.
     */
    public static void dropStartingPieces(final List<Piece> pieces)
    {
        for (int i = 0; i < pieces.size(); i++)
        {
            //get the current piece
            final Piece piece = pieces.get(i);
            
            //if at first row, set frozen false
            if (piece.getRow() == 0)
                piece.setFrozen(false);
        }
    }
    
    /**
     * Check if we have any pieces at the starting position
     * @param pieces List of pieces to check.
     * @return true if we have at least 1 piece in row 0, false otherwise
     */
    public static boolean hasStartingPieces(final List<Piece> pieces)
    {
        for (int i = 0; i < pieces.size(); i++)
        {
            if ((int)pieces.get(i).getRow() == 0)
                return true;
        }
        
        //we didn't find any pieces, return false
        return false;
    }
    
    /**
     * Spawn pieces on the board
     * @param engine Object containing all needed game elements
     * @throws Exception if an un-expected column is chosen
     */
    public static void spawnPieces(final Engine engine) throws Exception
    {
        final Board board = engine.getManager().getPlayer().getBoard();
        
        //the optional columns to choose from
        final List<Integer> options = new ArrayList<>();
        
        //add columns to list
        for (int i = 0 ; i < Board.COLUMNS; i++)
        {
            options.add(i);
        }
        
        for (int i = 0 ; i < SPAWN_TOTAL; i++)
        {
            //create a new random piece
            Piece piece = new Piece(engine.getRandom().nextInt(Piece.TYPE_TOTAL));

            //freeze for now
            piece.setFrozen(true);

            //set starting y-coordinate
            piece.setY(BoardHelper.SINGLE_PLAYER_ROW_1_Y);

            //pick random index from optional column list
            final int index = engine.getRandom().nextInt(options.size());
            
            //get random column
            final int col = options.get(index);

            //now remove from index
            options.remove(index);
            
            //set the column
            piece.setCol(col);

            //setup accordingly
            switch (col)
            {
                case BoardHelper.COLUMN_1:
                    piece.setX(BoardHelper.SINGLE_PLAYER_COLUMN_1_X);
                    break;

                case BoardHelper.COLUMN_2:
                    piece.setX(BoardHelper.SINGLE_PLAYER_COLUMN_2_X);
                    break;

                case BoardHelper.COLUMN_3:
                    piece.setX(BoardHelper.SINGLE_PLAYER_COLUMN_3_X);
                    break;

                case BoardHelper.COLUMN_4:
                    piece.setX(BoardHelper.SINGLE_PLAYER_COLUMN_4_X);
                    break;
                    
                default:
                    throw new Exception("Unexpected column chosen " + col);
            }

            //add piece to list
            board.add(piece);
        }
    }
    
    /**
     * Manage the destroyed pieces.
     * @param board The containing the pieces List of pieces to check
     * @param time Time per update (in nanoseconds)
     * @throws Exception if a problem with updating the animation occurs
     */
    public static void manageDestroyedPieces(final Board board, final long time) throws Exception
    {
        for (int i = 0; i < board.getPieces().size(); i++)
        {
            //get the current piece
            final Piece piece = board.getPieces().get(i);

            //update animation if destroyed
            if (piece.isDestroyed())
            {
                //if the piece is destroyed check if the animation is finished before we remove
                if (piece.hasAnimationFinished())
                {
                    //remove piece
                    board.remove(piece);

                    //move index back
                    i--;
                }
                else
                {
                    //update animation
                    piece.updateAnimation(time);
                }
            }
        }
    }
    
    /**
     * Do we have a yoshi?<br>
     * A yoshi means we have a top shell on a bottom shell.
     * @param board The board we are playing on
     * @return true=yes, false=no
     */
    public static boolean hasYoshi(final Board board)
    {
        //check each column
        for (int col = 0; col < Board.COLUMNS; col++)
        {
            boolean top = false;
            
            int startRow = 0;
            
            //did we find the first piece in this column
            boolean start = false;
            
            //check all rows in this column
            for (int row = 0; row < Board.ROWS; row++)
            {
                //get the piece at this location
                final Piece piece = board.getPiece(col, row);
                
                //if piece doesn't exist
                if (piece == null)
                {
                    if (start)
                    {
                        //we already found a piece for the column, so exit
                        break;
                    }
                    else
                    {
                        //skip to the next row
                        continue;
                    }
                }
                
                //we found our start piece
                start = true;
                
                //flag we found the shell top, and the piece has already been placed
                if (piece.getType() == Piece.TYPE_SHELL_TOP && piece.isFrozen())
                {
                    //flag we found top
                    top = true;
                    
                    //store the start row
                    startRow = row;
                }
                
                //if we found the bottom, and the piece has already been placed
                if (piece.getType() == Piece.TYPE_SHELL_BOTTOM && piece.isFrozen())
                {
                    //make sure we already found the top
                    if (top)
                    {
                        //calculate the number of enemies captured
                        System.out.println("Yoshis = " + (row - (startRow + 1)));
                        
                        //we have a yoshi
                        return true;
                    }
                    else
                    {
                        //skip to the next column, since we can't have a yoshi here
                        break;
                    }
                }
            }
        }
        
        //we didn't find a yoshi
        return false;
    }
    
    /**
     * Update the piece
     * @param piece The piece we are updating
     * @param board Board we are playing on
     * @param time Time per each game update (nanoseconds)
     * @param applyGravity Do we apply gravity to the piece
     */
    public static void updatePiece(final Piece piece, final Board board, final long time, final boolean applyGravity) throws Exception
    {
        //update the animation of the piece
        piece.updateAnimation(time);

        //are we applying gravity to the pieces
        if (applyGravity)
        {
            //if the piece is not frozen, we can apply gravity
            if (!piece.isFrozen())
            {
                //store original row
                final double row = piece.getRow();

                //move the row down 1
                piece.setRow(row + 1);

                //if we are out of bounds or hit another piece, we will place and freeze
                if (!board.hasBounds(piece.getCol(), row + BoardHelper.ROW_DROP) || board.hasPiece(piece))
                {
                    //reset previous location
                    piece.setRow(row);

                    //place the piece
                    piece.placePiece();

                    //check the piece below
                    Piece tmp = board.getPiece(piece.getCol(), piece.getRow() + 1);

                    //if there is a piece below
                    if (tmp != null)
                    {
                        //check if we placed a top shell piece
                        if (piece.getType() == Piece.TYPE_SHELL_TOP)
                        {
                            //if we don't have a yoshi, we need to remove this piece
                            if (!BoardHelper.hasYoshi(board))
                                piece.markDestroyed();
                        }
                        else if (piece.getType() == tmp.getType())
                        {
                            /**
                             * If the types are the same, remove them
                             */
                            piece.markDestroyed();
                            tmp.markDestroyed();
                        }
                    }
                    else
                    {
                        //if placing at bottom we can't do anything, remove it
                        if (piece.getType() == Piece.TYPE_SHELL_TOP)
                            piece.markDestroyed();
                    }
                }
                else
                {
                    //reset previous location
                    piece.setRow(row);

                    //drop piece
                    piece.applyGravity();
                }
            }
        }
    }
}