package com.gamesbykevin.yoshi.entity;

import com.gamesbykevin.framework.base.Animation;
import com.gamesbykevin.framework.base.Sprite;

/**
 * Every object in the game is an entity
 * @author GOD
 */
public abstract class Entity extends Sprite
{
    /**
     * No time delay
     */
    public static final long DELAY_NONE = 0;
    
    /**
     * No count
     */
    public static final int NO_COUNT = 0;
    
    protected Entity()
    {
        super.createSpriteSheet();
    }
    
    
    /**
     * Reset the current animation
     */
    public void resetAnimation() throws Exception
    {
        super.getSpriteSheet().reset();
    }
    
    /**
     * Assign the current animation.<br>
     * Also make sure we set the appropriate dimension
     * @param key The key of the animation
     */
    public void setAnimation(final Object key) throws Exception
    {
        super.getSpriteSheet().setCurrent(key);
        
        //make sure we adjust the dimensions to a default
        adjustDimensions();
    }
    
    /**
     * Has the current animation finished
     * @return true=yes, false=no
     */
    public boolean hasAnimationFinished() throws Exception
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
    public void adjustDimensions() throws Exception
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
    protected void addAnimation(final Object key, final int x, final int y, final int w, final int h, final int count, final long delay, final boolean loop) throws Exception
    {
        addAnimation(key, x, y, w, h, count, delay, delay, loop);
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
     * @param lastDelay time delay for last animation frame (nanoseconds)
     * @param loop do we loop animation
     */
    protected void addAnimation(final Object key, final int x, final int y, final int w, final int h, final int count, final long delay, final long lastDelay, final boolean loop) throws Exception
    {
        //single animation
        Animation animation = null;
        
        //add all the animations
        for (int i=0; i < count; i++)
        {
            if (animation == null)
            {
                animation = new Animation(x + (i * w), y, w, h, (i < count - 1) ? delay : lastDelay);
            }
            else
            {
                //set correct delay depending on the index
                animation.add(x + (i * w), y, w, h, (i < count - 1) ? delay : lastDelay);
            }
        }
        
        //set the loop
        animation.setLoop(loop);
        
        //add animation to spritesheet
        super.getSpriteSheet().add(animation, key);
        
        //if no current animation set, set this as a default
        if (getSpriteSheet().getCurrent() == null)
            setAnimation(key);
    }
}