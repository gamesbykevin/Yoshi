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
            //get the current piece
            final Piece piece = pieces.get(i);
            
            //if not placed or frozen, we have falling pieces
            if (!piece.isFrozen() && !piece.isPlaced())
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
     * Do we have a yoshi
     * @param pieces List of pieces to check
     * @return true if at least 1 piece is part of a yoshi, false otherwise
     */
    public static boolean hasYoshi(final List<Piece> pieces)
    {
        return (getYoshiSize(pieces) > 0);
    }
    
    /**
     * Get the yoshi size.
     * @param pieces List of pieces to check
     * @return The number of pieces that is part of the yoshi
     */
    public static int getYoshiSize(final List<Piece> pieces)
    {
        int count = 0;
        
        for (int i = 0; i < pieces.size(); i++)
        {
            //if this is a yoshi, return true
            if (pieces.get(i).isYoshi())
                count++;
        }
        
        //return the size of the yoshi found
        return count;
    }
    
    /**
     * Check if we have a yoshi in the column where we just placed a piece.<br>
     * If we find it we will flag all relative pieces as part of a yoshi.
     * A yoshi means we have a top shell on top of a bottom shell with x amount of pieces between.
     * @param board The board we are playing on
     * @param placedPiece The piece we just placed.
     */
    public static void checkYoshi(final Board board, final Piece placedPiece)
    {
        boolean top = false;

        int startRow = 0;

        //did we find the first piece in this column
        boolean start = false;

        //check all rows in this column
        for (int row = (int)placedPiece.getRow(); row < Board.ROWS; row++)
        {
            //get the piece at this location
            final Piece piece = board.getPiece(placedPiece.getCol(), row);

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

            //flag we found the shell top
            if (piece.getType() == Piece.TYPE_SHELL_TOP)
            {
                //flag we found top
                top = true;

                //store the start row
                startRow = row;
            }

            //if we found the bottom
            if (piece.getType() == Piece.TYPE_SHELL_BOTTOM)
            {
                //if we also have a top, flag the pieces
                if (top)
                {
                    //get all pieces to flag as part of the yoshi
                    for (int tmpRow = startRow; tmpRow <= row; tmpRow++)
                    {
                        //mark as yoshi and set the yoshi size
                        board.getPiece(placedPiece.getCol(), tmpRow).markYoshi((row - startRow) + 1);
                    }

                    //now skip to the next column, since any further rows won't be part
                    break;
                }
                else
                {
                    //skip to the next column, since we can't have a yoshi here
                    break;
                }
            }
        }
    }
    
    /**
     * Manage the yoshi creation
     * @param piece The piece that will play a role in yoshi creation
     * @param board The board containing all pieces
     */
    public static void manageYoshiCreation(final Piece piece, final Board board, final long time, final boolean applyGravity) throws Exception
    {
        //if the yoshi count is greater than 1 continue updating the top shell drop
        if (BoardHelper.getYoshiSize(board.getPieces()) > 1)
        {
            //don't continue if not the top shell
            if (piece.getType() != Piece.TYPE_SHELL_TOP)
                return;
            
            //update if we apply gravity
            if (applyGravity)
            {
                //get the piece at the current location, that isn't the current piece
                final Piece tmp = board.getPiece(piece);

                //make sure the bottom piece exists
                if (tmp != null)
                {
                    //if the piece below is a bottom shell, we need to start the yoshi animation
                    if (tmp.getType() == Piece.TYPE_SHELL_BOTTOM)
                    {
                        //remove the top piece
                        board.remove(piece);

                        //set the correct animatiion/location
                        if (piece.getYoshiSize() <= Piece.YOSHI_SIZE_TINY)
                        {
                            tmp.setAnimation(Piece.ANIMATION_KEY_CREATE_YOSHI_TINY);
                        }
                        else if (piece.getYoshiSize() <= Piece.YOSHI_SIZE_SMALL)
                        {
                            tmp.setAnimation(Piece.ANIMATION_KEY_CREATE_YOSHI_SMALL);
                        }
                        else if (piece.getYoshiSize() <= Piece.YOSHI_SIZE_MEDIUM)
                        {
                            tmp.setAnimation(Piece.ANIMATION_KEY_CREATE_YOSHI_MEDIUM);
                        }
                        else
                        {
                            tmp.setAnimation(Piece.ANIMATION_KEY_CREATE_YOSHI_LARGE);
                        }

                        //move down a little
                        tmp.setY(tmp.getY() + (tmp.getHeight() / 2));
                        
                        //reset animation
                        tmp.resetAnimation();
                        
                        //adjust location with the new dimension
                        tmp.setY(tmp.getY() - tmp.getHeight());
                    }
                    else
                    {
                        //remove the piece below
                        board.remove(tmp);

                        //move the top shell piece down
                        piece.applyGravity();
                    }
                }
                else
                {
                    //move the top shell piece down
                    piece.applyGravity();
                }
            }
        }
        else
        {
            /**
             * Since there is only 1 yoshi piece left continue updating the animation<br>
             * Once the animation is finished remove the piece.
             */
            if (piece.hasAnimationFinished())
            {
                //remove the last piece
                board.remove(piece);
                
                //now that we have created the yoshi and no more pieces left, adjust the time delay
                board.getTimer().setReset(Board.DELAY_GRAVITY);
                board.getTimer().reset();
            }
            else
            {
                //update animation
                piece.updateAnimation(time);
            }
        }
        
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
        //if the piece is placed or frozen, we can't do anything here
        if (piece.isPlaced() || piece.isFrozen())
            return;
        
        //update the animation of the piece
        piece.updateAnimation(time);

        //do we apply gravity
        if (applyGravity)
        {
            //store original row
            final double row = piece.getRow();

            //move the row down 1
            piece.setRow(row + 1);

            //temporary piece used to check the piece below
            Piece tmp;
            
            //if we are out of bounds or hit another piece, we will handle accordingly
            if (!board.hasBounds(piece.getCol(), row + BoardHelper.ROW_DROP) || board.hasPiece(piece))
            {
                //reset previous location
                piece.setRow(row);

                //place the piece
                piece.placePiece();

                //check the piece below (if exists)
                tmp = board.getPiece(piece.getCol(), piece.getRow() + 1);

                //if there is a piece below
                if (tmp != null)
                {
                    //check if we placed a top shell piece
                    if (piece.getType() == Piece.TYPE_SHELL_TOP)
                    {
                        //check for any yoshi's
                        BoardHelper.checkYoshi(board, piece);

                        //if we don't have a yoshi, we need to remove this piece
                        if (!BoardHelper.hasYoshi(board.getPieces()))
                        {
                            piece.markDestroyed();
                        }
                        else
                        {
                            //if we do have yoshi adjust the gravity timer
                            board.getTimer().setReset(Board.DELAY_GRAVITY_YOSHI);
                            board.getTimer().reset();
                        }
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
    
    /**
     * Check if we have lost.<br>
     * Here we will only check pieces in the first row, that aren't destroyed
     * @param pieces List of pieces to check
     * @return true if there is at least 1 piece placed at the top that isn't the shell top
     */
    public static boolean hasLosingBoard(final List<Piece> pieces)
    {
        //check each piece
        for (int i = 0; i < pieces.size(); i++)
        {
            //get the current piece
            final Piece piece = pieces.get(i);
            
            //check any other piece
            if (piece.getType() != Piece.TYPE_SHELL_TOP)
            {
                //we are only worried about pieces in the first row
                if (piece.getRow() > 0)
                    continue;
                
                //if the piece is not destoryed and placed, we have lost
                if (!piece.isDestroyed() && piece.isPlaced())
                    return true;
            }
        }
        
        //we are safe and have not lost
        return false;
    }
}