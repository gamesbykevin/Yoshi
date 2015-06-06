package com.gamesbykevin.yoshi.board;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.util.*;

import com.gamesbykevin.yoshi.board.piece.Piece;
import com.gamesbykevin.yoshi.engine.Engine;
import com.gamesbykevin.yoshi.shared.IElement;

import java.awt.Graphics;
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
    
    //the pieces in the game
    private List<Piece> pieces;
    
    //the timer to determine how often we apply gravity
    private Timer timer;
    
    //the amount of time to wait to apply gravity
    private static final long DELAY_GRAVITY = Timers.toNanoSeconds(50L);
    
    //not time delay
    private static final long DELAY_NONE = 0;
    
    public Board()
    {
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
    public void add(final Piece piece)
    {
        //add the piece to our list
        getPieces().add(piece);
    }
    
    /**
     * Remove the piece from the board
     * @param piece The piece we want to remove
     */
    public void remove(final Piece piece)
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
    protected boolean hasPiece(final Piece piece)
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
     * Get the piece at the location.
     * @param col Column
     * @param row Row
     * @return The at the specified location. If not found null is returned.
     */
    protected Piece getPiece(final double col, final double row)
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
     * Make the timer expire so gravity can be applied
     */
    public void applyGravity()
    {
        timer.setRemaining(DELAY_NONE);
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        if (!BoardHelper.hasStartingPieces(getPieces()))
        {
            //if no pieces at the top, spawn some
            BoardHelper.spawnPieces(engine);
        }
        else if (!BoardHelper.hasFallingPieces(getPieces()))
        {
            //drop the starting pieces
            BoardHelper.dropStartingPieces(getPieces());
        }
        else if (BoardHelper.hasDestroyedPiece(getPieces()))
        {
            //if we have a destroyed piece pause everything else
            BoardHelper.manageDestroyedPieces(this, engine.getMain().getTime());
        }
        else if (BoardHelper.hasYoshi(this))
        {
            //if we have yoshi, handle the rest here
            
            
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

                //update the piece
                BoardHelper.updatePiece(piece, this, engine.getMain().getTime(), applyGravity);
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