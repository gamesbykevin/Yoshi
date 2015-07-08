package com.gamesbykevin.yoshi.board.piece;

import com.gamesbykevin.framework.util.Timers;

import com.gamesbykevin.yoshi.board.BoardHelper;
import com.gamesbykevin.yoshi.entity.Entity;

import java.awt.Graphics;
import java.awt.Image;
import java.util.UUID;

/**
 * Each cell on the board is a piece
 * @author GOD
 */
public final class Piece extends Entity
{
    /**
     * The different types of pieces
     */
    public static final int TYPE_GOOMBA = 0;
    public static final int TYPE_SQUID = 1;
    public static final int TYPE_BOO = 2;
    public static final int TYPE_PLANT = 3;
    public static final int TYPE_SHELL_TOP = 4;
    public static final int TYPE_SHELL_BOTTOM = 5;
    
    /**
     * The total number of different piece types
     */
    public static final int TYPE_TOTAL = 6;
    
    public static final String ANIMATION_KEY_FALLING = "Falling";
    public static final String ANIMATION_KEY_PLACED = "Placed";
    public static final String ANIMATION_KEY_DESTROYED = "Destroyed";
    
    //the different animations for the different yoshi sizes
    public static final String ANIMATION_KEY_CREATE_YOSHI_TINY = "YoshiTiny";
    public static final String ANIMATION_KEY_CREATE_YOSHI_SMALL = "YoshiSmall";
    public static final String ANIMATION_KEY_CREATE_YOSHI_MEDIUM = "YoshiMedium";
    public static final String ANIMATION_KEY_CREATE_YOSHI_LARGE = "YoshiLarge";
    
    //the different yoshi sizes for each animation
    public static final int YOSHI_SIZE_TINY = 2;
    public static final int YOSHI_SIZE_SMALL = 6;
    public static final int YOSHI_SIZE_MEDIUM = 8;
    public static final int YOSHI_SIZE_LARGE = 9;
    
    //default animation delay
    private static final long DELAY_DEFAULT = Timers.toNanoSeconds(600L);
    
    //how long the destroy animation lasts
    private static final long DELAY_DESTROY = Timers.toNanoSeconds(300L);
    
    //how long the yoshi create animation lasts
    private static final long DELAY_YOSHI_CREATE = Timers.toNanoSeconds(200L);
    
    //how long the final animation will be
    private static final long DELAY_YOSHI_CREATE_LAST = Timers.toNanoSeconds(500L);
    
    /**
     * The speed at which we switch columns
     */
    public static final double SWAP_COLUMN_RATE = 0.1;
    
    /**
     * For when we aren't looking for a piece by the id
     */
    public static final UUID NO_ID = null;
    
    //the type of piece (goomba, plant, shell top, etc...)
    private final int type;
    
    //has the piece been placed
    private boolean placed = false;
    
    //is the piece frozen?
    private boolean frozen = false;
    
    //is the piece destroyed
    private boolean destroyed = false;
    
    //if this piece part of a yoshi
    private boolean yoshi = false;
    
    //the yoshi size
    private int size = 0;
    
    //the target column, used when switching columns
    private double targetCol = 0;
    
    public Piece(final int type) throws Exception
    {
        super();
        
        this.type = type;
        
        //do not place the new piece
        setPlaced(false);
        
        //freeze the piece at first
        setFrozen(true);
        
        switch(type)
        {
            case TYPE_GOOMBA:
                addAnimation(ANIMATION_KEY_FALLING,   0,   730, 32, 32, 2, DELAY_DEFAULT, true);
                addAnimation(ANIMATION_KEY_PLACED,    0,   768, 44, 32, 1, DELAY_NONE, false);
                addAnimation(ANIMATION_KEY_DESTROYED, 176, 768, 44, 32, 1, DELAY_DESTROY, false);
                break;
                
            case TYPE_SQUID:
                addAnimation(ANIMATION_KEY_FALLING,   64,  730, 32, 30, 2, DELAY_DEFAULT, true);
                addAnimation(ANIMATION_KEY_PLACED,    44,  768, 44, 32, 1, DELAY_NONE, false);
                addAnimation(ANIMATION_KEY_DESTROYED, 176, 768, 44, 32, 1, DELAY_DESTROY, false);
                break;
                
            case TYPE_BOO:
                addAnimation(ANIMATION_KEY_FALLING,   128, 730, 32, 30, 2, DELAY_DEFAULT, true);
                addAnimation(ANIMATION_KEY_PLACED,    88,  768, 44, 32, 1, DELAY_NONE, false);
                addAnimation(ANIMATION_KEY_DESTROYED, 176, 768, 44, 32, 1, DELAY_DESTROY, false);
                break;
                
            case TYPE_PLANT:
                addAnimation(ANIMATION_KEY_FALLING,   192, 730, 32, 32, 2, DELAY_DEFAULT, true);
                addAnimation(ANIMATION_KEY_PLACED,    132, 768, 44, 32, 1, DELAY_NONE, false);
                addAnimation(ANIMATION_KEY_DESTROYED, 176, 768, 44, 32, 1, DELAY_DESTROY, false);
                break;
                
            case TYPE_SHELL_TOP:
                addAnimation(ANIMATION_KEY_FALLING,   256, 730, 32, 32, 1, DELAY_NONE, false);
                addAnimation(ANIMATION_KEY_PLACED,    256, 730, 32, 32, 1, DELAY_NONE, false);
                addAnimation(ANIMATION_KEY_DESTROYED, 176, 768, 44, 32, 1, DELAY_DESTROY, false);
                break;
                
            case TYPE_SHELL_BOTTOM:                
                addAnimation(ANIMATION_KEY_FALLING,   288, 730, 32, 32, 1, DELAY_NONE, false);
                addAnimation(ANIMATION_KEY_PLACED,    288, 730, 32, 32, 1, DELAY_NONE, false);
                addAnimation(ANIMATION_KEY_DESTROYED, 176, 768, 44, 32, 1, DELAY_DESTROY, false);
                
                //the animations for each differnt yoshi 
                addAnimation(ANIMATION_KEY_CREATE_YOSHI_TINY,   0, 470, 44, 42, 6, DELAY_YOSHI_CREATE, DELAY_YOSHI_CREATE_LAST, false);
                addAnimation(ANIMATION_KEY_CREATE_YOSHI_SMALL,  0, 512, 44, 55, 6, DELAY_YOSHI_CREATE, DELAY_YOSHI_CREATE_LAST, false);
                addAnimation(ANIMATION_KEY_CREATE_YOSHI_MEDIUM, 0, 567, 55, 61, 6, DELAY_YOSHI_CREATE, DELAY_YOSHI_CREATE_LAST, false);
                addAnimation(ANIMATION_KEY_CREATE_YOSHI_LARGE,  0, 628, 63, 81, 6, DELAY_YOSHI_CREATE, DELAY_YOSHI_CREATE_LAST, false);
                break;
                
            default:
                throw new Exception("Type not found here " + type);
        }
        
        //assign the default animation
        setAnimation(ANIMATION_KEY_FALLING);
    }
    
    /**
     * Set the target column
     * @param targetCol The column we want the piece to head towards (when switching columns
     */
    public void setTargetCol(final int targetCol)
    {
        this.targetCol = targetCol;
    }
    
    /**
     * Do we have the target column?
     * @return true if the current column equals the target column
     */
    public boolean hasTargetCol()
    {
        return (getCol() == getTargetCol());
    }
    
    /**
     * Get the target column
     * @return The column we want the piece to be at
     */
    public double getTargetCol()
    {
        return this.targetCol;
    }
    
    /**
     * Mark this piece as part of a yoshi
     */
    /**
     * Mark this piece as part of a yoshi.
     * @param size The size of the yoshi, the number of pieces used to create the yoshi
     */
    public void markYoshi(final int size)
    {
        //mark as part of yoshi
        this.yoshi = true;
        
        //store the size so we will know which animation to use
        this.size = size;
    }
    
    /**
     * Get the yoshi size.
     * @return The total number of pieces used to create the yoshi
     */
    public int getYoshiSize()
    {
        return this.size;
    }
    
    /**
     * Is this piece part of a yoshi?
     * @return true if the piece is part of a yoshi, false otherwise
     */
    public boolean isYoshi()
    {
        return this.yoshi;
    }
    
    /**
     * Place the piece at the current location.<br>
     * We will freeze the piece as well as change the animation
     */
    public void placePiece() throws Exception
    {
        //stop dropping piece
        setPlaced(true);

        //also change the animation
        setAnimation(Piece.ANIMATION_KEY_PLACED);
    }
    
    /**
     * Drop the piece down and update y-coordinate
     */
    public void applyGravity()
    {
        //set row
        setRow(getRow() + BoardHelper.ROW_DROP);
        
        //update y-coordinate
        setY(getY() + BoardHelper.DROP_PIXEL_DISTANCE);
    }
    
    /**
     * Mark the piece destroyed.<br>
     * We will also set the destroyed animation.
     */
    public void markDestroyed() throws Exception
    {
        //flag destroyed
        this.destroyed = true;
        
        //set the destroy animation
        this.setAnimation(ANIMATION_KEY_DESTROYED);
        
        //reset animation
        super.getSpriteSheet().reset();
    }
    
    /**
     * If this piece destroyed?
     * @return true if the piece is to be removed, false otherwise
     */
    public boolean isDestroyed()
    {
        return this.destroyed;
    }
    
    /**
     * Flag the piece as frozen
     * @param frozen true If the piece is not placed but we want to not move the piece, false otherwise
     */
    public void setFrozen(final boolean frozen)
    {
        this.frozen = frozen;
    }
    
    /**
     * If the piece frozen?
     * @return true If the piece is not placed but we want to not move the piece, false otherwise
     */
    public boolean isFrozen()
    {
        return this.frozen;
    }
    
    /**
     * Flag the piece as placed
     * @param placed true if the piece is being placed on the board permanent, false otherwise
     */
    public void setPlaced(final boolean placed)
    {
        this.placed = placed;
        
        //if placed, we also can't be frozen
        if (isPlaced())
            setFrozen(!isPlaced());
    }
    
    /**
     * If the piece place?
     * @return true If the piece was placed permanently, false otherwise
     */
    public boolean isPlaced()
    {
        return this.placed;
    }
    
    /**
     * Get the type of piece (goomba, plant, shell top, etc...)
     * @return The type of piece
     */
    public int getType()
    {
        return this.type;
    }
    
    /**
     * Render the piece.
     * @param graphics Object used to write image for display
     * @param image Image containing sprite sheet animation
     * @throws Exception 
     */
    public void render(final Graphics graphics, final Image image) throws Exception
    {
        //store original location
        final double x = getX();
        final double y = getY();
        
        //offset location
        setX(x - (getWidth() / 2));
        setY(y - (getHeight() / 2));
        
        super.draw(graphics, image);
        
        //set location back
        setX(x);
        setY(y);
    }
}