package com.gamesbykevin.yoshi.entity;

import com.gamesbykevin.framework.base.Animation;
import com.gamesbykevin.framework.base.Sprite;

/**
 * Every object in the game is an entity
 * @author GOD
 */
public abstract class Entity extends Sprite
{
    protected Entity()
    {
        super.createSpriteSheet();
    }
    
    /**
     * No time delay
     */
    public static final long DELAY_NONE = 0;
    
    /**
     * Reset the current animation
     */
    public void resetAnimation()
    {
        super.getSpriteSheet().reset();
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
     * Add animation to sprite sheet
     * @param key Object used to identify the animation
     * @param x starting x-coordinate of animation
     * @param y starting y-coordinate of animation
     * @param w width of animation
     * @param h height of animation
     * @param count number of animations
     * @param delay time delay for each animation (nanoseconds)
     * @param loop do we loop animation
     */
    protected void addAnimation(final Object key, final int x, final int y, final int w, final int h, final int count, final long delay, final boolean loop)
    {
        //create a new animation
        Animation animation = new Animation();
        
        //set the loop
        animation.setLoop(loop);
        
        //add all the animations
        for (int i=0; i < count; i++)
        {
            animation.add(x + (i * w), y, w, h, delay);
        }
        
        //add animation to spritesheet
        super.getSpriteSheet().add(animation, key);
        
        //if no current animation set, set this as a default
        if (getSpriteSheet().getCurrent() == null)
            setAnimation(key);
    }
}