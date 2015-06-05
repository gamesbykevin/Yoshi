package com.gamesbykevin.yoshi.board;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.util.*;

import com.gamesbykevin.yoshi.board.piece.Piece;
import com.gamesbykevin.yoshi.engine.Engine;
import com.gamesbykevin.yoshi.shared.IElement;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

/**
 * The board the player will play on
 * @author GOD
 */
public class Board extends Sprite implements IElement
{
    /**
     * The dimensions of our board
     */
    public static final int ROWS = 9;
    public static final int COLUMNS = 4;
    
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
    
    //drop 1/2 a row each time we apply gravity
    /**
     * Every time we apply gravity we increase the row
     */
    public static final double ROW_DROP = 0.5;
    
    //the pieces in the game
    private List<Piece> pieces;
    
    //the timer to determine how often we apply gravity
    private Timer timer;
    
    //the amount of time to wait to apply gravity
    private static final long DELAY_GRAVITY = Timers.toNanoSeconds(200L);
    
    /**
     * How many pieces to spawn
     */
    public static final int SPAWN_TOTAL = 2;
    
    public Board(final Image image)
    {
        super.setImage(image);
        
        //set the bounds
        super.setBounds(0, COLUMNS - 1, 0, ROWS - 1);
        
        //create a new list for the pieces
        this.pieces = new ArrayList<>();
        
        //create timer 
        this.timer = new Timer(DELAY_GRAVITY);
    }
    
    /**
     * Get the pieces on the board.<br>
     * Does not count the falling pieces that have not been yet placed.
     * @return The list of pieces placed on the board.
     */
    public List<Piece> getPieces()
    {
        return this.pieces;
    }
    
    @Override
    public void dispose()
    {
        if (timer != null)
        {
            timer.dispose();
            timer = null;
        }
        
        if (pieces != null)
        {
            for (int i = 0; i < pieces.size(); i++)
            {
                pieces.get(i).dispose();
                pieces.set(i, null);
            }

            pieces.clear();
            pieces = null;
        }
    }
    
    /**
     * Add the piece to the collection
     * @param piece The piece we want to add
     */
    private void add(final Piece piece)
    {
        //add the piece to our list
        getPieces().add(piece);
    }
    
    /**
     * Remove the piece from the board
     * @param piece The piece we want to remove
     */
    private void remove(final Piece piece)
    {
        for (int i = 0; i < getPieces().size(); i++)
        {
            //if this piece has the location, return true
            if (getPieces().get(i).getId() == piece.getId())
                getPieces().remove(i);
        }
    }
    
    /**
     * Do we have a piece at this location?
     * @param piece The piece that has the location we want to check
     * @return true if a piece exists on the board at this location, false otherwise
     */
    private boolean hasPiece(final Piece piece)
    {
        //check all pieces to see if they are at the location
        for (int i = 0; i < getPieces().size(); i++)
        {
            //we don't want to check our own piece
            if (getPieces().get(i).getId() != piece.getId())
            {
                //if the current piece has this location, return true
                if (getPieces().get(i).equals(piece))
                    return true;
            }
        }
        
        //we could not find a piece here
        return false;
    }
    
    /**
     * Do we currently have any falling pieces?
     * @return true if at least 1 piece is falling, false otherwise
     */
    private boolean hasFallingPiece()
    {
        for (int i = 0; i < getPieces().size(); i++)
        {
            //if not frozen we have at least 1 falling piece
            if (!getPieces().get(i).isFrozen())
                return true;
        }
        
        //we did not find any falling pieces
        return false;
    }
    
    /**
     * Do we have at least 1 destroyed piece?
     * @return true = yes, false = no
     */
    private boolean hasDestroyedPiece()
    {
        for (int i = 0; i < getPieces().size(); i++)
        {
            //if this piece is destroyed return true
            if (getPieces().get(i).isDestroyed())
                return true;
        }
        
        //we don't have any destroyed pieces
        return false;
    }
    
    /**
     * Get the piece at the location.
     * @param col Column
     * @param row Row
     * @return The at the specified location. If not found null is returned.
     */
    private Piece getPiece(final double col, final double row)
    {
        for (int i = 0; i < getPieces().size(); i++)
        {
            //if the location matches, get the piece
            if (getPieces().get(i).equals(col, row))
                return getPieces().get(i);
        }
        
        //a piece was not found return null
        return null;
    }
    
    /**
     * Spawn pieces on the board
     * @param engine Object containing all needed game elements
     * @throws Exception if an un-expected column is chosen
     */
    private void spawnPieces(final Engine engine) throws Exception
    {
        //the optional columns to choose from
        final List<Integer> options = new ArrayList<>();
        
        //add columns to list
        for (int i = 0 ; i < COLUMNS; i++)
        {
            options.add(i);
        }
        
        for (int i = 0 ; i < SPAWN_TOTAL; i++)
        {
            //create a new random piece
            Piece piece = new Piece(engine.getRandom().nextInt(Piece.TYPE_TOTAL));

            //make sure not frozen
            piece.setFrozen(false);

            //set starting y-coordinate
            piece.setY(SINGLE_PLAYER_ROW_1_Y);

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
                case COLUMN_1:
                    piece.setX(SINGLE_PLAYER_COLUMN_1_X);
                    break;

                case COLUMN_2:
                    piece.setX(SINGLE_PLAYER_COLUMN_2_X);
                    break;

                case COLUMN_3:
                    piece.setX(SINGLE_PLAYER_COLUMN_3_X);
                    break;

                case COLUMN_4:
                    piece.setX(SINGLE_PLAYER_COLUMN_4_X);
                    break;
                    
                default:
                    throw new Exception("Unexpected column chosen " + col);
            }

            //add piece to list
            add(piece);
        }

        //reset timer just in case
        timer.reset();
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        //if we don't have any falling pieces spawn them
        if (!hasFallingPiece())
        {
            spawnPieces(engine);
        }
        else
        {
            //if we have a destroyed piece pause everything else
            if (hasDestroyedPiece())
            {
                for (int i = 0; i < getPieces().size(); i++)
                {
                    //get the current piece
                    final Piece piece = getPieces().get(i);
                
                    //update animation if destroyed
                    if (piece.isDestroyed())
                    {
                        //if the piece is destroyed check if the animation is finished before we remove
                        if (piece.hasAnimationFinished())
                        {
                            //remove piece
                            remove(piece);

                            //move index back
                            i--;
                        }
                        else
                        {
                            piece.updateAnimation(engine.getMain().getTime());
                        }
                    }
                }
            }
            else
            {
                //update gravity timer
                this.timer.update(engine.getMain().getTime());

                //do we apply gravity
                boolean applyGravity = (this.timer.hasTimePassed());

                //reset the timer
                if (applyGravity)
                    timer.reset();

                for (int i = 0; i < getPieces().size(); i++)
                {
                    //get the current piece
                    final Piece piece = getPieces().get(i);

                    //update the animation of the piece
                    piece.updateAnimation(engine.getMain().getTime());

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
                            if (!super.hasBounds(piece.getCol(), row + ROW_DROP) || hasPiece(piece))
                            {
                                //reset previous location
                                piece.setRow(row);

                                //place the piece
                                piece.placePiece();

                                //check the piece below
                                Piece tmp = getPiece(piece.getCol(), piece.getRow() + 1);

                                //if there is a piece below
                                if (tmp != null)
                                {
                                    //if the types are the same, remove them
                                    if (piece.getType() == tmp.getType())
                                    {
                                        //mark both pieces destroyed
                                        piece.markDestroyed();
                                        tmp.markDestroyed();
                                    }
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
        }
    }
    
    @Override
    public void render(final Graphics graphics) throws Exception
    {
        //draw the pieces on the board
        for (int i = 0; i < getPieces().size(); i++)
        {
            getPieces().get(i).render(graphics, getImage());
        }
    }
}