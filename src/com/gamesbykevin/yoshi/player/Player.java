package com.gamesbykevin.yoshi.player;

import com.gamesbykevin.framework.util.Timers;
import com.gamesbykevin.yoshi.board.Board;
import com.gamesbykevin.yoshi.board.BoardHelper;
import com.gamesbykevin.yoshi.engine.Engine;
import com.gamesbykevin.yoshi.entity.Entity;
import com.gamesbykevin.yoshi.player.stats.Stats;
import com.gamesbykevin.yoshi.shared.IElement;
import com.gamesbykevin.yoshi.player.stats.Stat;

import java.awt.Font;
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
    
    /**
     * The amount of time to deduct from your opponent when matching pieces
     */
    protected static final long DAMAGE_DELAY_MATCH = Timers.toNanoSeconds(5000L);
    
    /**
     * The amount of time to add to your own timer when matching pieces
     */
    protected static final long HEAL_DELAY_MATCH = Timers.toNanoSeconds(1000L);
    
    /**
     * The amount of time to deduct from your opponent when creating a yoshi
     */
    protected static final long DAMAGE_DELAY_YOSHI = Timers.toNanoSeconds(7000L);
    
    /**
     * The amount of time to add to your own timer when creating a yoshi
     */
    protected static final long HEAL_DELAY_YOSHI = Timers.toNanoSeconds(2000L);
    
    //the direction the player is facing
    private boolean front = true;
    
    //starting location for player
    public static final int SINGLE_PLAYER_START_X = 190;
    public static final int SINGLE_PLAYER_START_Y = 400;
    
    //the number of pixels to move
    protected static final int PLAYER_MOVE_PIXELS = 60;
    
    //the starting locations for multiplayer
    public static final int MULTI_PLAYER_1_START_X = 50;
    public static final int MULTI_PLAYER_1_START_Y = 423;
    public static final int MULTI_PLAYER_2_START_X = MULTI_PLAYER_1_START_X + 300;
    public static final int MULTI_PLAYER_2_START_Y = MULTI_PLAYER_1_START_Y;
    
    public static final int INDEX_DIFFICULTY_EASY = 0;
    public static final int INDEX_DIFFICULTY_MEDIUM = 1;
    public static final int INDEX_DIFFICULTY_HARD = 2;
    
    //the players starting column
    private static final int START_COLUMN = 1;
    
    //the starting coordinates
    private int startX;
    private int startY;
    
    //list of columns and the order they currently reside (needed so api will know which columns to switch)
    private List<Integer> columnOrder;
    
    //our object containing the game stats
    private Stats stats;
    
    public Player(final Image image, final boolean multiplayer, final int difficultyIndex) throws Exception
    {
        //set y-coordinate
        super.setY(this.startY);
        
        this.board = new Board(difficultyIndex);
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
        
        //check if we have any destroyed pieces
        final boolean hasDestroyedPieces = BoardHelper.hasDestroyedPieces(getBoard().getPieces());
        
        //do we have a yoshi
        final boolean hasYoshi = BoardHelper.hasYoshi(getBoard().getPieces());
        
        //update the board
        getBoard().update(engine);
        
        //if we now have destroyed pieces
        if (!hasDestroyedPieces && BoardHelper.hasDestroyedPieces(getBoard().getPieces()))
        {
            //get the number of destroyed pieces
            int count = BoardHelper.getDestroyedPieceCount(getBoard().getPieces());
            
            //update the score stats
            Stat stat = getStats().getStatScore();
            stat.setValue(stat.getValue() + (count * Board.SCORE_PIECE_MATCH));
            
            //if attack mode we want to heal ourselves and damage the opponent
            if (engine.getManager().getPlayers().hasModeAttack())
            {
                //get the time remaining
                final long remaining = getStats().getGameTimer().getRemaining();
                
                //now add the heal time
                getStats().getGameTimer().setRemaining(remaining + HEAL_DELAY_MATCH);
                
                //update description
                getStats().updateGameTimerDesc();
                
                //now damage opponent
                engine.getManager().getPlayers().damageOpponent(this, DAMAGE_DELAY_MATCH);
            }
        }
        
        //if we now have a yoshi
        if (!hasYoshi && BoardHelper.hasYoshi(getBoard().getPieces()))
        {
            //how big the yoshi is
            int yoshiSize = BoardHelper.getYoshiSize(getBoard().getPieces());
            
            //update the score stats
            Stat stat = getStats().getStatScore();
            stat.setValue(stat.getValue() + (yoshiSize * Board.SCORE_YOSHI_PIECE));
            
            //only track this stat if it exists
            if (getStats().getStatYoshi() != null)
            {
                //increase the yoshi stat
                stat = getStats().getStatYoshi();
                stat.setValue(stat.getValue() + 1);
            }
            
            //if attack mode we want to heal ourselves and damage the opponent
            if (engine.getManager().getPlayers().hasModeAttack())
            {
                //get the time remaining
                final long remaining = getStats().getGameTimer().getRemaining();
                
                //now add the heal time
                getStats().getGameTimer().setRemaining(remaining + (HEAL_DELAY_YOSHI * yoshiSize));
                
                //update description
                getStats().updateGameTimerDesc();
                
                //now damage opponent
                engine.getManager().getPlayers().damageOpponent(this, (DAMAGE_DELAY_YOSHI * yoshiSize));
            }
        }
        
        //if the game is over, record our result
        if (getBoard().hasGameOver())
            setGameResult(getBoard().hasLost());
        
        if (getStats() != null)
        {
            //update the stats
            getStats().update(engine);
        }
    }
    
    /**
     * Our game is over, set the result.<br>
     * We will also display the win/lost animation.
     * @param lose True if the player controlling this board lost, false if they won
     */
    public void setGameResult(final boolean lose)
    {
        //update the result on the board
        getBoard().setGameResult(lose);
        
        //show the correct animation if we won/lose
        if (getBoard().hasLost())
        {
            //set the lose animation
            super.setAnimation(Player.ANIMATION_KEY_LOSE);
        }
        else
        {
            //set the winning animation
            super.setAnimation(Player.ANIMATION_KEY_VICTORY);
        }

        //set the coordinates in middle of the board
        super.setX(getBoard().getX() - (getWidth() / 2));
        super.setY(getBoard().getY() - (getHeight() / 2));
    }
    
    /**
     * Get our stats container
     * @return The stats container with all displayed stats on the screen
     */
    public Stats getStats()
    {
        return this.stats;
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
    
    /**
     * Create Stats container
     * @param font The font for our Stats
     * @param multiplayer Is this multi-player
     */
    public void createStats(final Font font, final boolean multiplayer)
    {
        this.stats = new Stats(font, getStartX(), getStartY(), multiplayer);
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
        
        if (stats != null)
        {
            stats.dispose();
            stats = null;
        }
    }
    
    @Override
    public void render(final Graphics graphics) throws Exception
    {
        //draw the board
        getBoard().render(graphics);
        
        //draw the player info
        super.draw(graphics);
        
        //display stats on-screen
        getStats().render(graphics);
    }
}