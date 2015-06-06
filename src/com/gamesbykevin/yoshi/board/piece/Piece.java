package com.gamesbykevin.yoshi.board.piece;

import com.gamesbykevin.framework.base.Animation;
import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.util.Timers;

import com.gamesbykevin.yoshi.board.Board;
import com.gamesbykevin.yoshi.board.BoardHelper;

import java.awt.Graphics;
import java.awt.Image;

/**
 * Each cell on the board is a piece
 * @author GOD
 */
public final class Piece extends Sprite
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
    
    //default animation delay
    private static final long DELAY_DEFAULT = Timers.toNanoSeconds(600L);
    
    //how long the destroy animation lasts
    private static final long DELAY_DESTROY = Timers.toNanoSeconds(300L);
    
    //no delay
    private static final long DELAY_NONE = 0;
    
    //the type of piece (goomba, plant, shell top, etc...)
    private final int type;
    
    //should this piece be falling?
    private boolean frozen = false;
    
    //is the piece destroyed
    private boolean destroyed = false;
    
    public Piece(final int type)
    {
        this.type = type;
        
        //create the sprite sheet
        super.createSpriteSheet();
        
        //do not freeze the new piece
        setFrozen(false);
        
        switch(type)
        {
            case TYPE_GOOMBA:
                addAnimation(ANIMATION_KEY_FALLING, 0, 730, 32, 32, 2, DELAY_DEFAULT, true);
                addAnimation(ANIMATION_KEY_PLACED, 0, 768, 44, 32, 1, DELAY_NONE, false);
                addAnimation(ANIMATION_KEY_DESTROYED, 176, 768, 44, 32, 1, DELAY_DESTROY, false);
                break;
                
            case TYPE_SQUID:
                addAnimation(ANIMATION_KEY_FALLING, 64, 730, 32, 30, 2, DELAY_DEFAULT, true);
                addAnimation(ANIMATION_KEY_PLACED, 44, 768, 44, 32, 1, DELAY_NONE, false);
                addAnimation(ANIMATION_KEY_DESTROYED, 176, 768, 44, 32, 1, DELAY_DESTROY, false);
                break;
                
            case TYPE_BOO:
                addAnimation(ANIMATION_KEY_FALLING, 128, 730, 32, 30, 2, DELAY_DEFAULT, true);
                addAnimation(ANIMATION_KEY_PLACED, 88, 768, 44, 32, 1, DELAY_NONE, false);
                addAnimation(ANIMATION_KEY_DESTROYED, 176, 768, 44, 32, 1, DELAY_DESTROY, false);
                break;
                
            case TYPE_PLANT:
                addAnimation(ANIMATION_KEY_FALLING, 192, 730, 32, 32, 2, DELAY_DEFAULT, true);
                addAnimation(ANIMATION_KEY_PLACED, 132, 768, 44, 32, 1, DELAY_NONE, false);
                addAnimation(ANIMATION_KEY_DESTROYED, 176, 768, 44, 32, 1, DELAY_DESTROY, false);
                break;
                
            case TYPE_SHELL_TOP:
                addAnimation(ANIMATION_KEY_FALLING, 256, 730, 32, 32, 1, DELAY_NONE, false);
                addAnimation(ANIMATION_KEY_PLACED,  256, 730, 32, 32, 1, DELAY_NONE, false);
                addAnimation(ANIMATION_KEY_DESTROYED, 176, 768, 44, 32, 1, DELAY_DESTROY, false);
                break;
                
            case TYPE_SHELL_BOTTOM:                
                addAnimation(ANIMATION_KEY_FALLING, 288, 730, 32, 32, 1, DELAY_NONE, false);
                addAnimation(ANIMATION_KEY_PLACED,  288, 730, 32, 32, 1, DELAY_NONE, false);
                addAnimation(ANIMATION_KEY_DESTROYED, 176, 768, 44, 32, 1, DELAY_DESTROY, false);
                break;
                
        }
        
        //assign the default animation
        setAnimation(ANIMATION_KEY_FALLING);
    }
    
    /**
     * Place the piece at the current location.<br>
     * We will freeze the piece as well as change the animation
     */
    public void placePiece()
    {
        //stop dropping piece
        setFrozen(true);

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
     * Assign the current animation.<br>
     * Also make sure we set the appropriate dimension
     * @param key The key of the animation
     */
    public void setAnimation(final Object key)
    {
        super.getSpriteSheet().setCurrent(key);
        
        //make sure we adjust the dimensions to a default
        adjustDimensions();
    }
    
    /**
     * Has the current animation finished
     * @return true=yes, false=no
     */
    public boolean hasAnimationFinished()
    {
        return super.getSpriteSheet().hasFinished();
    }
    
    /**
     * Update the animation.
     * @param time Time per update (nano-seconds)
     */
    public void updateAnimation(final long time) throws Exception
    {
        super.getSpriteSheet().update(time);
    }
    
    /**
     * Set the width/height based on the current animation frame
     */
    public void adjustDimensions()
    {
        //set the width/height
        super.setDimensions(super.getSpriteSheet().getLocation());
    }
    
    /**
     * Mark the piece destroyed.<br>
     * We will also set the destroyed animation.
     */
    public void markDestroyed()
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
     * Freeze the piece
     * @param frozen Assign true if we don't want the piece to fall, false otherwise
     */
    public void setFrozen(final boolean frozen)
    {
        this.frozen = frozen;
    }
    
    /**
     * If this piece frozen?
     * @return true if the piece is to not fall, false otherwise
     */
    public boolean isFrozen()
    {
        return this.frozen;
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
     * Add animation to sprite sheet
     * @param key Object used to identify the animation
     * @param x starting x-coordinate of animation
     * @param y starting y-coordinate of animation
     * @param w width of animation
     * @param h height of animation
     * @param count number of animations
     * @param delay time delay for each animation (nano-seconds)
     * @param loop do we loop animation
     */
    private void addAnimation(final Object key, final int x, final int y, final int w, final int h, final int count, final long delay, final boolean loop)
    {
        Animation animation = new Animation();
        animation.setLoop(loop);
        
        for (int i=0; i < count; i++)
        {
            animation.add(x + (i * w), y, w, h, delay);
        }
        
        //add to spritesheet
        super.getSpriteSheet().add(animation, key);
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