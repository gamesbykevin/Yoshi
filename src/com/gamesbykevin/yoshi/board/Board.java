package com.gamesbykevin.yoshi.board;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.util.*;

import com.gamesbykevin.yoshi.board.piece.Piece;
import com.gamesbykevin.yoshi.engine.Engine;
import com.gamesbykevin.yoshi.entity.Entity;
import com.gamesbykevin.yoshi.player.Player;
import com.gamesbykevin.yoshi.shared.IElement;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    
    //the scoring values
    public static final int SCORE_NONE = 0;
    public static final int SCORE_YOSHI_PIECE = 50;
    public static final int SCORE_YOSHI_HEIGHT_REWARD = 25;
    public static final int SCORE_PIECE_MATCH = 10;
    public static final int SCORE_YOSHI_HEIGHT_PENALTY = -1;
    public static final int SCORE_PIECE_HEIGHT = -5;
    public static final int SCORE_BOTTOM_SHELL_HEIGHT = -7;
    
    //the time delay for each difficulty in which we will apply gravity
    private static final long DELAY_DIFFICULTY_EASY = Timers.toNanoSeconds(900L);
    private static final long DELAY_DIFFICULTY_MEDIUM = Timers.toNanoSeconds(450L);
    private static final long DELAY_DIFFICULTY_HARD = Timers.toNanoSeconds(125L);
    
    //the pieces in the game
    private List<Piece> pieces;
    
    //the timer to determine how often we apply gravity
    private Timer timer;
    
    /**
     * The amount of time at which we apply gravity when we have a yoshi
     */
    protected static final long DELAY_GRAVITY_YOSHI = Timers.toNanoSeconds(150L);
    
    //do we have a losing board
    private boolean lose = false;
    
    //is the game over
    private boolean gameover = false;
    
    /**
     * The starting coordinates where the pieces spawn on the board
     */
    private int startPieceColumnX, startPieceRowY;
    
    //the default time delay
    private final long delay;
    
    public Board(final int difficultyIndex) throws Exception
    {
        //set the bounds
        super.setBounds(0, COLUMNS - 1, 0, ROWS - 1);
        
        //create a new list for the pieces
        this.pieces = new ArrayList<>();
        
        //create gravity timer with a time delay dependant on the diffuculty
        switch (difficultyIndex)
        {
            case Player.INDEX_DIFFICULTY_EASY:
                this.delay = DELAY_DIFFICULTY_EASY;
                break;
                
            case Player.INDEX_DIFFICULTY_MEDIUM:
                this.delay = DELAY_DIFFICULTY_MEDIUM;
                break;
                
            case Player.INDEX_DIFFICULTY_HARD:
                this.delay = DELAY_DIFFICULTY_HARD;
                break;
            
            default:
                throw new Exception("Difficulty Index not found " + difficultyIndex);
        }
        
        //create timer with assigned delay
        this.timer = new Timer(this.delay);
    }
    
    /**
     * Get the time delay for when to apply gravity to the pieces
     * @return The time delay in nano seconds
     */
    protected long getDelayDefault()
    {
        return this.delay;
    }
    
    /**
     * Get the x-coordinate for the first column on the board.<br>
     * This is used to determine where to spawn pieces
     * @return The x-coordinate
     */
    protected int getStartPieceColumnX()
    {
        return this.startPieceColumnX;
    }
    
    /**
     * Get the y-coordinate for the first row on the board.<br>
     * This is used to determine where to spawn pieces
     * @return The y-coordinate
     */
    protected int getStartPieceRowY()
    {
        return this.startPieceRowY;
    }
    
    /**
     * Set the x-coordinate for the first column on the board.<br>
     * This is used to determine where the pieces are spawned
     * @param startPieceColumnX The x-coordinate
     */
    public void setStartPieceColumnX(final int startPieceColumnX)
    {
        this.startPieceColumnX = startPieceColumnX;
    }
    
    /**
     * Set the y-coordinate for the first row on the board.<br>
     * This is the row where pieces are spawned
     * @param startPieceRowY The y-coordinate
     */
    public void setStartPieceRowY(final int startPieceRowY)
    {
        this.startPieceRowY = startPieceRowY;
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
        timer = null;
        
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
        //set the current column as the target
        piece.setTargetCol((int)piece.getCol());
        
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
     * Get the piece at the specified piece location.<br>
     * We will not ignore the specified piece to check if another piece also exists at the same location
     * @param piece The piece whose location we want to check
     * @return The piece at this location, if none are found null is returned
     */
    protected Piece getPiece(final Piece piece)
    {
        return getPiece(piece.getCol(), piece.getRow(), piece.getId());
    }
    
    /**
     * Get the piece at the location.
     * @param col Column
     * @param row Row
     * @param id The unique key identifying the piece we don't want to check
     * @return The piece at the specified location. If not found null is returned.
     */
    public Piece getPiece(final double col, final double row, final UUID id)
    {
        for (int i = 0; i < getPieces().size(); i++)
        {
            //get the current piece
            final Piece piece = getPieces().get(i);
            
            //make sure we aren't checking our own piece
            if (id == null || piece.getId() != id)
            {
                //if the location matches, get the piece
                if (piece.equals(col, row))
                    return piece;
            }
        }
        
        //a piece was not found return null
        return null;
    }
    
    /**
     * Make the timer expire so gravity can be applied.<br>
     * Note: if the player can't swap the columns, gravity won't be applied
     */
    public void applyGravity()
    {
        //if we can swap columns, we can also apply gravity
        if (BoardHelper.canSwapColumns(getPieces()))
            getTimer().setRemaining(Entity.DELAY_NONE);
    }
    
    
    /**
     * Did we lose?
     * @return true if the board has filled up and the game is over, false otherwise
     */
    public boolean hasLost()
    {
        return this.lose;
    }
    
    /**
     * Now that our game is over, record the result
     * @param lose True if the player controlling this board lost, false if they won
     */
    public void setGameResult(final boolean lose)
    {
        //store our win/lose result
        this.lose = lose;
        
        //flag the game as over
        this.gameover = true;
    }
    
    public boolean hasGameOver()
    {
        return this.gameover;
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        //no need to continue if the game is over
        if (hasGameOver())
            return;
        
        //if we don't have any starting pieces, we need to spawn them
        if (!BoardHelper.hasStartingPieces(getPieces()))
        {
            //if no pieces at the top, spawn some
            BoardHelper.spawnPieces(this, engine.getRandom());
            
            //no need to continue
            return;
        }
        
        //if no pieces are falling, drop the starting pieces at the top
        if (!BoardHelper.hasFallingPieces(getPieces()))
        {
            /**
             * Even though we don't have falling pieces let make sure any existing pieces are finished.
             * Finished meaning that there aren't any yoshi's or destroyed
             */
            if (!BoardHelper.hasExistingPieces(getPieces()))
            {
                //drop the starting pieces
                BoardHelper.dropStartingPieces(getPieces());

                //no need to continue
                return;
            }
        }
        
        //if there are pieces that are destroyed we need to handle those
        if (BoardHelper.hasDestroyedPieces(getPieces()))
        {
            //if we have a destroyed piece pause everything else
            BoardHelper.manageDestroyedPieces(this, engine.getMain().getTime());
            
            //no need to continue
            return;
        }
        
        //if we have pieces needing to be swapped
        if (BoardHelper.isSwappingColumns(getPieces()))
        {
            //swap the pieces that are not yet at the target column
            BoardHelper.swapPieces(getPieces(), getStartPieceColumnX());

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
                //get the current piece
                final Piece piece = getPieces().get(i);

                //update the piece
                BoardHelper.updatePiece(piece, this, engine.getMain().getTime(), applyGravity);
                
                //we only want to create 1 yoshi at a time, so if 1 is found we will exit
                if (BoardHelper.hasYoshi(getPieces()))
                    break;
            }
        }
        
        //check if we have a losing board, and if so set the game result
        if (BoardHelper.hasLosingBoard(getPieces()))
            setGameResult(true);
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