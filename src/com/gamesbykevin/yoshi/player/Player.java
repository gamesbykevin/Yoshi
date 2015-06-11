package com.gamesbykevin.yoshi.player;

import com.gamesbykevin.framework.util.Timers;
import com.gamesbykevin.yoshi.board.Board;
import com.gamesbykevin.yoshi.board.BoardHelper;
import com.gamesbykevin.yoshi.engine.Engine;
import com.gamesbykevin.yoshi.entity.Entity;
import com.gamesbykevin.yoshi.shared.IElement;
import java.awt.Color;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

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
    
    //list of columns and the order they currently reside (needed so api will know which columns to switch)
    private List<Integer> columnOrder;
    
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
        
        //create the list
        this.columnOrder = new ArrayList<>();
        
        //add the columns in their appropriate starting order
        for (int i = 0; i < Board.COLUMNS; i++)
        {
            this.columnOrder.add(i);
        }
    }
    
    /**
     * Start to switch the columns.<br>
     * We will also set the appropriate animation as well.
     * @return true if we were successful, false otherwise
     * @throws Exception If the column(s) are not found in the columnOrder list
     */
    protected boolean switchColumns() throws Exception
    {
        //if we can swap columns, initialize it then
        if (BoardHelper.canSwapColumns(getBoard().getPieces()) && hasAnimationFinished())
        {
            final int leftColumnIndex = (int)getCol();
            final int rightColumnIndex = (int)getCol() + 1;
            
            //initialize the column swap
            BoardHelper.startSwap(getBoard(), leftColumnIndex, rightColumnIndex);
            
            //find which columns are at the index
            final int leftColumnValue = getColumnOrderValue(leftColumnIndex);
            final int rightColumnValue = getColumnOrderValue(rightColumnIndex);
            
            //now swap the values
            columnOrder.set(rightColumnIndex, leftColumnValue);
            columnOrder.set(leftColumnIndex, rightColumnValue);
            
            //flip the direction facing
            setFront(!hasFront());

            //change the animation
            if (hasFront())
            {
                super.setAnimation(Player.ANIMATION_KEY_ROTATE_BACK);
                super.resetAnimation();
            }
            else
            {
                super.setAnimation(Player.ANIMATION_KEY_ROTATE_FRONT);
                super.resetAnimation();
            }
            
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
     * Get the column value of the specified index
     * @param index Index where we want the value
     * @return The column value of the specified index
     */
    protected int getColumnOrderValue(final int index)
    {
        return columnOrder.get(index);
    }
    
    /**
     * Get the index of the columns order list to find where a column is.
     * @param column Column we are searching for
     * @return The index where the column is found
     * @throws Exception If the index is not found an exception will be throws
     */
    protected int getColumnOrderIndex(final int column) throws Exception
    {
        for (int index = 0; index < columnOrder.size(); index++)
        {
            //if this value is the column, we found the index
            if (columnOrder.get(index) == column)
                return index;
        }
        
        //throw exception if the column was not found
        throw new Exception("Column was not found " + column);
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
     * Update the board and check if we lost.<br>
     * Also update the player animation
     * @param engine Object containing all game elements
     * @throws Exception 
     */
    protected void updateMisc(final Engine engine) throws Exception
    {
        //update player animation
        updateAnimation(engine.getMain().getTime());
        
        //update the board
        getBoard().update(engine);
        
        if (getBoard().hasLost())
        {
            //set the lose animation
            super.setAnimation(Player.ANIMATION_KEY_LOSE);
            
            //set the coordinates in middle of the board
            super.setX(getBoard().getX() - (getWidth() / 2));
            super.setY(getBoard().getY() - (getHeight() / 2));
        }
    }
    
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
        if (columnOrder != null)
        {
            columnOrder.clear();
            columnOrder = null;
        }
        
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