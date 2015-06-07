package com.gamesbykevin.yoshi.player;

import com.gamesbykevin.framework.util.Timers;
import com.gamesbykevin.yoshi.board.Board;
import com.gamesbykevin.yoshi.board.BoardHelper;
import com.gamesbykevin.yoshi.engine.Engine;
import com.gamesbykevin.yoshi.entity.Entity;
import com.gamesbykevin.yoshi.shared.IElement;

import java.awt.Graphics;
import java.awt.Image;

/**
 * The player that plays the board
 * @author GOD
 */
public abstract class Player extends Entity implements IElement, IPlayer
{
    //the game board
    private Board board;
    
    //different animations for the player
    protected static final String ANIMATION_KEY_ROTATE_BACK = "Rotate1";
    protected static final String ANIMATION_KEY_ROTATE_FRONT = "Rotate2";
    protected static final String ANIMATION_KEY_VICTORY = "Victory";
    protected static final String ANIMATION_KEY_LOSE = "Lose";
    
    //default animation delay
    protected static final long DELAY_DEFAULT = Timers.toNanoSeconds(25L);
    
    //the direction the player is facing
    private boolean front = true;
    
    //starting location for player
    public static final int SINGLE_PLAYER_START_X = 190;
    public static final int SINGLE_PLAYER_START_Y = 400;
    
    //the number of pixels to move
    protected static final int PLAYER_MOVE_PIXELS = 60;
    
    //the starting locations for multiplayer
    public static final int MULTI_PLAYER_1_START_X = 60;
    public static final int MULTI_PLAYER_1_START_Y = 425;
    public static final int MULTI_PLAYER_2_START_X = 360;
    public static final int MULTI_PLAYER_2_START_Y = 425;
    
    //the players starting column
    private static final int START_COLUMN = 1;
    
    //the starting coordinates
    private int startX;
    private int startY;
    
    public Player(final Image image, final boolean multiplayer)
    {
        //set y-coordinate
        super.setY(this.startY);
        
        this.board = new Board();
        this.board.setImage(image);
        
        //store the spritesheet image
        super.setImage(image);
        
        //set animations
        setupAnimations();
        
        //setup the coordinates
        setCoordinates(multiplayer);
    }
    
    /**
     * Start to switch the columns.
     * @return true if we were successful, false otherwise
     */
    protected boolean switchColumns()
    {
        //if we can swap columns, initialize it then
        if (BoardHelper.canSwapColumns(getBoard().getPieces()) && hasAnimationFinished())
        {
            //initialize the column swap
            BoardHelper.startSwap(getBoard(), (int)getCol(), (int)getCol() + 1);
            
            //we were successful
            return true;
        }
        else
        {
            //we can't switch columns at the moment
            return false;
        }
    }
    
    /**
     * Set the start coordinates for the player as if they are in the first column.<br>
     * Also we will set the starting coordinates for the player
     * @param startX x-coordinate
     * @param startY y-coordinate
     */
    protected void setStartCoordinates(final int startX, final int startY)
    {
        this.startX = startX;
        this.startY = startY;
        
        //set the starting position
        setCol(START_COLUMN);
        setY(getStartY());
    }
    
    /**
     * The children will need logic to update the player
     * @param engine Object containing all game elements
     * @throws Exception 
     */
    @Override
    public abstract void update(final Engine engine) throws Exception;
    
    /**
     * Get the starting x-coordinate
     * @return The starting x-coordinate
     */
    private int getStartX()
    {
        return this.startX;
    }
    
    /**
     * Get the starting x-coordinate
     * @return The starting x-coordinate
     */
    private int getStartY()
    {
        return this.startY;
    }
    
    /**
     * Assign the column for the player.<br>
     * We will also update the x-coordinate.<br>
     * Will also keep the column in range
     * @param col The column
     */
    @Override
    public final void setCol(final double col)
    {
        //set the column
        super.setCol(col);
        
        //keep column within range
        if (super.getCol() < 0)
            super.setCol(0);
        if (super.getCol() > Board.COLUMNS - 2)
            super.setCol(Board.COLUMNS - 2);
        
        //assign the x coordinate
        setX(getStartX() + (PLAYER_MOVE_PIXELS * getCol()));
    }
    
    /**
     * Set the direction facing.
     * @param front true if the player is facing front, false if the player is facing back
     */
    protected void setFront(final boolean front)
    {
        this.front = front;
    }
    
    /**
     * Is the player facing the front
     * @return true if the player is facing front, false if the player is facing back
     */
    protected boolean hasFront()
    {
        return this.front;
    }
    
    /**
     * Get the player's game board
     * @return 
     */
    public Board getBoard()
    {
        return this.board;
    }
    
    @Override
    public void dispose()
    {
        if (board != null)
        {
            board.dispose();
            board = null;
        }
    }
    
    @Override
    public void render(final Graphics graphics) throws Exception
    {
        //draw the board
        getBoard().render(graphics);
        
        //draw the player info
        super.draw(graphics);
    }
}