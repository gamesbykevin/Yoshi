package com.gamesbykevin.yoshi.board;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.util.*;

import com.gamesbykevin.yoshi.board.piece.Piece;
import com.gamesbykevin.yoshi.engine.Engine;
import com.gamesbykevin.yoshi.entity.Entity;
import com.gamesbykevin.yoshi.shared.IElement;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

/**
 * The board the player will play on
 * @author GOD
 */
public final class Board extends Sprite implements IElement
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
    protected static final long DELAY_GRAVITY = Timers.toNanoSeconds(250L);
    
    /**
     * The amount of time at which we apply gravity when we have a yoshi
     */
    protected static final long DELAY_GRAVITY_YOSHI = Timers.toNanoSeconds(200L);
    
    //do we have a losing board
    private boolean lose = false;
    
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
     * Get the timer.
     * @return The timer we use to determine when to apply gravity.
     */
    protected Timer getTimer()
    {
        return this.timer;
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
        return (getPiece(piece) != null);
    }
    
    /**
     * Get the piece at the specified piece location.<br>
     * We will not ignore the specified piece to check if another piece also exists at the same location
     * @param piece The piece whose location we want to check
     * @return The piece at this location, if none are found null is returned
     */
    protected Piece getPiece(final Piece piece)
    {
        //check all pieces to see if they are at the location
        for (int i = 0; i < getPieces().size(); i++)
        {
            //get the current piece
            final Piece tmp = getPieces().get(i);
            
            //we don't want to check our own piece
            if (tmp.getId() != piece.getId())
            {
                //if the current piece has this location, return true
                if (tmp.equals(piece))
                    return tmp;
            }
        }
        
        //none were found, return null
        return null;
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
        getTimer().setRemaining(Entity.DELAY_NONE);
    }
    
    /**
     * Did we lose
     * @return true if the board has filled up and the game is over, false otherwise
     */
    public boolean hasLost()
    {
        return this.lose;
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        //no need to continue if we lost
        if (hasLost())
            return;
        
        //if we don't have any starting pieces, we need to spawn them
        if (!BoardHelper.hasStartingPieces(getPieces()))
        {
            //if no pieces at the top, spawn some
            BoardHelper.spawnPieces(engine);
            
            //no need to continue
            return;
        }
        
        //if no pieces are falling, drop the starting pieces at the top
        if (!BoardHelper.hasFallingPieces(getPieces()))
        {
            //drop the starting pieces
            BoardHelper.dropStartingPieces(getPieces());
            
            //no need to continue
            return;
        }
        
        //if there are pieces that are destroyed we need to handle those
        if (BoardHelper.hasDestroyedPiece(getPieces()))
        {
            //if we have a destroyed piece pause everything else
            BoardHelper.manageDestroyedPieces(this, engine.getMain().getTime());
            
            //no need to continue
            return;
        }
        
        //update gravity timer
        getTimer().update(engine.getMain().getTime());

        //do we apply gravity
        boolean applyGravity = (getTimer().hasTimePassed());

        //reset the timer
        if (applyGravity)
            getTimer().reset();
        
        //if we have a yoshi we will manage the pieces differently
        if (BoardHelper.hasYoshi(getPieces()))
        {
            for (int i = 0; i < getPieces().size(); i++)
            {
                //get the current piece
                final Piece piece = getPieces().get(i);
                
                //skip the piece if it is not a yoshi
                if (!piece.isYoshi())
                    continue;

                //we are only worried about updating the top/bottom shells
                if (piece.getType() != Piece.TYPE_SHELL_BOTTOM && piece.getType() != Piece.TYPE_SHELL_TOP)
                    continue;
                
                //manage the creation
                BoardHelper.manageYoshiCreation(piece, this, engine.getMain().getTime(), applyGravity);
            }
        }
        else
        {
            /**
             * If we don't have a yoshi continue business as usual
             */
            for (int i = 0; i < getPieces().size(); i++)
            {
                //if we have a yoshi, don't continue here
                if (BoardHelper.hasYoshi(getPieces()))
                    break;
                
                //get the current piece
                final Piece piece = getPieces().get(i);

                //update the piece
                BoardHelper.updatePiece(piece, this, engine.getMain().getTime(), applyGravity);
            }
        }
        
        //check if we have a losing board
        lose = BoardHelper.hasLosingBoard(getPieces());
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