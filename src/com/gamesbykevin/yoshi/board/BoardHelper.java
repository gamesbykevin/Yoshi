package com.gamesbykevin.yoshi.board;

import com.gamesbykevin.yoshi.board.piece.Piece;
import com.gamesbykevin.yoshi.entity.Entity;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class will contain custom methods to help with the board
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
    
    /**
     * The amount of pixels between each column
     */
    public static final int COLUMN_PIXEL_WIDTH = 60;
    
    /*
     * The x-coordinates for each column single player
     */
    public static final int SINGLE_PLAYER_COLUMN_1_X = 220;
    
    //the starting y-coordinate
    public static final int SINGLE_PLAYER_ROW_1_Y = 55;
    
    /**
     * The location of the center of the playable board in single player
     */
    public static final Point SINLGE_PLAYER_BOARD_CENTER = new Point(310,237);
    
    /**
     * The x-coordinates for each column for player 1 in multi-player
     */
    public static final int MULTI_PLAYER_1_COLUMN_1_X = 80;
    
    //the starting y-coordinate
    public static final int MULTI_PLAYER_1_ROW_1_Y = 75;
    
    //the center of the board for player 1
    public static final Point MULTI_PLAYER_1_BOARD_CENTER = new Point(170,260);
    
    /**
     * The x-coordinates for each column for player 2 in multi-player
     */
    public static final int MULTI_PLAYER_2_COLUMN_1_X = MULTI_PLAYER_1_COLUMN_1_X + 300;
    
    //the starting y-coordinate
    public static final int MULTI_PLAYER_2_ROW_1_Y = MULTI_PLAYER_1_ROW_1_Y;
    
    //the center of the board for player 2
    public static final Point MULTI_PLAYER_2_BOARD_CENTER = new Point(470,260);
    
    //the amount of pixels to drop for everytime we apply gravity to a piece
    public static final int DROP_PIXEL_DISTANCE = 20;
    
    /**
     * Every time we apply gravity we increase the row by this amount
     */
    public static final double ROW_DROP = 0.5;
    
    /**
     * Does this board have any placed pieces
     * @param pieces List of pieces to check
     * @return true if at least 1 piece is placed, false otherwise
     */
    public static boolean hasPlacedPieces(final List<Piece> pieces)
    {
        for (int i = 0; i < pieces.size(); i++)
        {
            //get the current piece
            final Piece piece = pieces.get(i);
            
            //if any piece is placed, yoshi, or destoryed, there are placed pieces
            if (piece.isPlaced() || piece.isYoshi() || piece.isDestroyed())
                return true;
        }
        
        //we don't have any placed pieces
        return false;
    }
    
    /**
     * Do we have at least 1 destroyed piece?
     * @param pieces List of pieces to check
     * @return true = yes, false = no
     */
    public static boolean hasDestroyedPieces(final List<Piece> pieces)
    {
        return (getDestroyedPieceCount(pieces) > Entity.NO_COUNT);
    }
    
    /**
     * Is there a top shell marked destroyed?
     * @param pieces List of pieces to check
     * @return true if a top shell is flagged destroyed, false otherwise
     */
    public static boolean hasDestroyedTopShell(final List<Piece> pieces)
    {
        //check each piece
        for (int i = 0; i < pieces.size(); i++)
        {
            final Piece piece = pieces.get(i);
            
            //don't check if not destroyed
            if (!piece.isDestroyed())
                continue;
            
            //if we found our shell
            if (piece.getType() == Piece.TYPE_SHELL_TOP)
                return true;
        }
        
        //no destroyed top shells were found
        return false;
    }
    
    /**
     * Get the count of destroyed pieces
     * @param pieces List of pieces to check
     * @return The total number of pieces flagged as destroyed
     */
    public static int getDestroyedPieceCount(final List<Piece> pieces)
    {
        //keep track of the count
        int count = 0;
        
        //check each piece
        for (int i = 0; i < pieces.size(); i++)
        {
            //if this piece is destroyed increase count
            if (pieces.get(i).isDestroyed())
                count++;
        }
        
        //return result
        return count;
    }
    
    /**
     * Populate the board with random pieces
     * @param board The board we are adding pieces to
     * @param random Object used to make random decisions
     */
    public static void populateBoard(final Board board, final Random random) throws Exception
    {
        //create new list of options
        List<Integer> options = new ArrayList<>();
        
        for (int row = Board.ROWS - 3; row < Board.ROWS; row++)
        {
            for (int col = 0; col < Board.COLUMNS; col++)
            {
                //clear the options
                options.clear();
                
                //get the pieces to the north an south
                Piece north = board.getPiece(col, row - 1, Piece.NO_ID);
                Piece south = board.getPiece(col, row + 1, Piece.NO_ID);
                
                //check if we can use this piece
                if ((north == null || north.getType() != Piece.TYPE_BOO) && (south == null || south.getType() != Piece.TYPE_BOO))
                    options.add(Piece.TYPE_BOO);
                if ((north == null || north.getType() != Piece.TYPE_GOOMBA) && (south == null || south.getType() != Piece.TYPE_GOOMBA))
                    options.add(Piece.TYPE_GOOMBA);
                if ((north == null || north.getType() != Piece.TYPE_PLANT) && (south == null || south.getType() != Piece.TYPE_PLANT))
                    options.add(Piece.TYPE_PLANT);
                if ((north == null || north.getType() != Piece.TYPE_SQUID) && (south == null || south.getType() != Piece.TYPE_SQUID))
                    options.add(Piece.TYPE_SQUID);
                
                //create a random piece of type
                Piece piece = new Piece(options.get(random.nextInt(options.size())));

                //set the location
                piece.setCol(col);
                piece.setRow(row);

                //mark the piece as placed
                piece.placePiece();

                //set the y-coordinate
                piece.setY(board.getStartPieceRowY() + (row * (DROP_PIXEL_DISTANCE * 2)));

                //set the x-coordinate
                piece.setX(board.getStartPieceColumnX() + (col * BoardHelper.COLUMN_PIXEL_WIDTH));

                //add piece to the board
                board.add(piece);
            }
        }
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
            
            //we don't want these pieces because they can't fall
            if (piece.isPlaced() || piece.isDestroyed() || piece.isFrozen())
                continue;
            
            //if none of the above is true, we have a falling piece
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
            
            //only worried about frozen pieces
            if (!piece.isFrozen())
                continue;
            
            //only worried about pieces in the very first row
            if (piece.getRow() != 0)
                continue;
            
            //unfreeze
            piece.setFrozen(false);
        }
    }
    
    /**
     * Check if we have any existing pieces that need to finish
     * @param pieces List of pieces to check
     * @return true if we have at least 1 piece that is a yoshi or destroyed, false otherwise
     */
    public static boolean hasExistingPieces(final List<Piece> pieces)
    {
        for (int i = 0; i < pieces.size(); i++)
        {
            //get the current piece
            final Piece piece = pieces.get(i);
            
            //if yoshi or destroyed, we have existing pieces
            if (piece.isYoshi() || piece.isDestroyed())
                return true;
        }
        
        //none were found
        return false;
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
            //get the current piece
            final Piece piece = pieces.get(i);
            
            //if we are at the first row and the piece is frozen, we have a starting piece
            if ((int)piece.getRow() == 0)
                return true;
        }
        
        //we didn't find any pieces, return false
        return false;
    }
    
    /**
     * Spawn pieces on the board.<br>
     * @param board The board we want to add the pieces to
     * @param random Object used to make random decisions
     */
    public static void spawnPieces(final Board board, final Random random) throws Exception
    {
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
            Piece piece = new Piece(random.nextInt(Piece.TYPE_TOTAL));

            //freeze for now
            piece.setFrozen(true);

            //set starting y-coordinate
            piece.setY(board.getStartPieceRowY());

            //pick random index from optional column list
            final int index = random.nextInt(options.size());
            
            //get random column
            final int col = options.get(index);

            //now remove from index
            options.remove(index);
            
            //set the column
            piece.setCol(col);

            //set the x-coordinate
            piece.setX(board.getStartPieceColumnX() + (col * BoardHelper.COLUMN_PIXEL_WIDTH));

            //add piece to list
            board.add(piece);
        }
    }
    
    /**
     * Can we swap columns?<br>
     * @param pieces The list of pieces to check
     * @return true if there are: no yoshi's, no destroyed pieces, and we aren't already swapping columns, otherwise false is returned
     */
    public static boolean canSwapColumns(final List<Piece> pieces)
    {
        //if we are currently swapping columns, we can't do it again
        if (isSwappingColumns(pieces))
            return false;
        
        //if we currently have destroyed pieces
        if (hasDestroyedPieces(pieces))
            return false;
        
        //if we have yoshi we can't swap columns
        if (hasYoshi(pieces))
            return false;
        
        //if none of the above are an issue we can swap columns
        return true;
    }
    
    /**
     * Are we swapping columns?
     * @param pieces List of pieces we want to check
     * @return true if at least 1 piece is not at the target column, false otherwise
     */
    public static boolean isSwappingColumns(final List<Piece> pieces)
    {
        for (int i = 0; i < pieces.size(); i++)
        {
            //get the current piece
            final Piece piece = pieces.get(i);
            
            //if we are not at our target column, we are swapping columns
            if (!piece.hasTargetCol())
                return true;
        }
        
        return false;
    }
    
    /**
     * Start swapping the pieces in the specified columns.<br>
     * We won't swap them yet, but will set the target column
     * @param board The board where the pieces are
     * @param leftCol The left column we want to swap with the right
     * @param rightCol The right column we want to swap with the left
     */
    public static void startSwap(final Board board, final int leftCol, final int rightCol)
    {
        for (int i = 0; i < board.getPieces().size(); i++)
        {
            //get the current piece
            final Piece piece = board.getPieces().get(i);
            
            //don't check frozen pieces
            if (piece.isFrozen())
                continue;
            
            //if the piece is falling, check if we should swap it
            if (!piece.isFrozen() && !piece.isPlaced())
            {
                //temp piece
                Piece tmp;
                
                //depending on the side we need to check if the falling piece should be swapped
                if (piece.getCol() == rightCol)
                {
                    //get the neighbor piece
                    tmp = board.getPiece(leftCol, (int)piece.getRow(), piece.getId());
                    
                    //if a piece exists next to this piece and is placed, we need to swap
                    if (tmp != null && tmp.isPlaced())
                    {
                        piece.setTargetCol(leftCol);
                    }
                    else
                    {
                        //also check just below so pieces don't get stuck
                        tmp = board.getPiece(leftCol, piece.getRow() + BoardHelper.ROW_DROP, piece.getId());

                        //if a piece exists next to this piece and is placed, we need to swap
                        if (tmp != null && tmp.isPlaced())
                            piece.setTargetCol(leftCol);
                    }
                }
                else if (piece.getCol() == leftCol)
                {
                    //get the neighbor piece
                    tmp = board.getPiece(rightCol, (int)piece.getRow(), piece.getId());
                    
                    //if a piece exists next to this piece and is placed, we need to swap
                    if (tmp != null && tmp.isPlaced())
                    {
                        piece.setTargetCol(rightCol);
                    }
                    else
                    {
                        //also check just below so pieces don't get stuck
                        tmp = board.getPiece(rightCol, piece.getRow() + BoardHelper.ROW_DROP, piece.getId());

                        //if a piece exists next to this piece and is placed, we need to swap
                        if (tmp != null && tmp.isPlaced())
                            piece.setTargetCol(rightCol);
                    }
                }
            }
            else
            {
                //set target column depending on our current location
                if (piece.getCol() == rightCol)
                {
                    piece.setTargetCol(leftCol);
                }
                else if (piece.getCol() == leftCol)
                {
                    piece.setTargetCol(rightCol);
                }
            }
        }
    }
    
    /**
     * Swap the pieces.<br>
     * Move the pieces column towards the target column
     * @param pieces The pieces we want to swap
     * @param startColumnX The x-coordinate where the first column is. We need to calculate the x-coordinate.
     */
    public static void swapPieces(final List<Piece> pieces, final double startColumnX)
    {
        for (int i = 0; i < pieces.size(); i++)
        {
            //get the current piece
            final Piece piece = pieces.get(i);
            
            //only check pieces not at the target col
            if (piece.hasTargetCol())
                continue;
            
            //swap the piece
            swapPiece(piece, startColumnX);
        }
    }
    
    /**
     * Swap the piece.<br>
     * Move the piece's column towards the target column
     * @param piece The piece we want to swap
     * @param startColumnX The x-coordinate where the first column is. We need to calculate the x-coordinate.
     */
    public static void swapPiece(final Piece piece, final double startColumnX)
    {
        //get the difference
        final double difference = (piece.getCol() > piece.getTargetCol()) ? piece.getCol() - piece.getTargetCol() : piece.getTargetCol() - piece.getCol();
        
        //if we are close enough to the target, place on target column
        if (difference <= Piece.SWAP_COLUMN_RATE)
            piece.setCol(piece.getTargetCol());
        
        if (piece.getCol() < piece.getTargetCol())
        {
            piece.setCol(piece.getCol() + Piece.SWAP_COLUMN_RATE);
        }
        else if (piece.getCol() > piece.getTargetCol())
        {
            piece.setCol(piece.getCol() - Piece.SWAP_COLUMN_RATE);
        }
        
        //update x-coordinate accordingly
        piece.setX(startColumnX + (piece.getCol() * COLUMN_PIXEL_WIDTH));
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
            final Piece piece = board.getPiece(placedPiece.getCol(), row, Piece.NO_ID);

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
                        board.getPiece(placedPiece.getCol(), tmpRow, Piece.NO_ID).markYoshi((row - startRow) + 1);
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
                board.getTimer().setReset(board.getDelayDefault());
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
            //temporary piece used to check the piece below
            Piece tmp;
            
            /**
             * If we went out of bounds or landed on another piece, handle accordingly
             */
            if (!board.hasBounds(piece.getCol(), piece.getRow() + BoardHelper.ROW_DROP) || 
                board.getPiece(piece.getCol(), (int)piece.getRow() + 1, piece.getId()) != null)
            {
                //place the piece
                piece.placePiece();

                //check the piece below (if exists)
                tmp = board.getPiece(piece.getCol(), (int)piece.getRow() + 1, piece.getId());

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