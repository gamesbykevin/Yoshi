package com.gamesbykevin.yoshi.player.stats;

import com.gamesbykevin.framework.util.Timer;
import com.gamesbykevin.framework.util.Timers;

import com.gamesbykevin.yoshi.engine.Engine;
import com.gamesbykevin.yoshi.entity.Entity;
import com.gamesbykevin.yoshi.shared.IElement;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;

/**
 * This object will contain the stats for a given player player
 * @author GOD
 */
public final class Stats implements IElement
{
    //our list of stats
    private HashMap<Key, Stat> stats;
    
    //where our stats will be displayed for single player
    private static final Point SINGLE_PLAYER_LOCATION_OFFSET_TIMER = new Point(-145, -335);
    private static final Point SINGLE_PLAYER_LOCATION_OFFSET_LEVEL = new Point(-115, 25);
    private static final Point SINGLE_PLAYER_LOCATION_OFFSET_YOSHI = new Point(330, -100);
    private static final Point SINGLE_PLAYER_LOCATION_OFFSET_SCORE = new Point(295, -335);
    
    //we want to offset the coordinates based on where the player is
    private static final Point MULTI_PLAYER_LOCATION_OFFSET_TIMER = new Point(165, -401);
    private static final Point MULTI_PLAYER_LOCATION_OFFSET_LEVEL = new Point(80, -401);
    private static final Point MULTI_PLAYER_LOCATION_OFFSET_SCORE = new Point(80, -416);
    
    //default dimensions for this
    private static final int WIDTH = 200, HEIGHT = 40;
    
    //the game timer
    private Timer timer;
    
    //store the previous time of the timer update
    private long previous = 0;
    
    //are we counting the timer down
    private boolean countdown = false;
    
    /**
     * The font size for multi-player
     */
    private static final float FONT_SIZE_MULTI_PLAYER = 12f;
    
    /**
     * The font size for single player
     */
    private static final float FONT_SIZE_SINGLE_PLAYER = 16f;
    
    /**
     * The keys for each stat we are tracking
     */
    private enum Key
    {
        Timer, Level, Yoshi, Score
    }
    
    /**
     * Create new container for the stats in the game
     * @param font The font we want to use
     * @param startX starting x-coordinate
     * @param startY starting y-coordinate
     * @param multiplayer Are we playing multi-player?
     */
    public Stats(Font font, final int startX, final int startY, final boolean multiplayer)
    {
        //create list for all stats
        this.stats = new HashMap<>();
        
        //adjust the font size depending on multiple players
        font = font.deriveFont((multiplayer) ? FONT_SIZE_MULTI_PLAYER : FONT_SIZE_SINGLE_PLAYER);
        
        //create the stats
        createStatScore(font, startX, startY, multiplayer);
        createStatYoshi(font, startX, startY, multiplayer);
        createStatLevel(font, startX, startY, multiplayer);
        createStatTimer(font, startX, startY, multiplayer);
        
        //create a new timer
        this.timer = new Timer();
    }
    
    private void createStatScore(final Font font, final int startX, final int startY, final boolean multiplayer)
    {
        //setup stat
        Stat stat = new Stat(WIDTH, HEIGHT);
        
        //setup the coordinate ccordingly
        if (!multiplayer)
        {
            stat.setX(SINGLE_PLAYER_LOCATION_OFFSET_SCORE.x + startX);
            stat.setY(SINGLE_PLAYER_LOCATION_OFFSET_SCORE.y + startY);
        }
        else
        {
            stat.setX(MULTI_PLAYER_LOCATION_OFFSET_SCORE.x + startX);
            stat.setY(MULTI_PLAYER_LOCATION_OFFSET_SCORE.y + startY);
        }
        
        stat.setValue(0);
        stat.setFont(font);
        stat.render();
        
        //add to list
        add(stat, Key.Score);
    }
    
    private void createStatYoshi(final Font font, final int startX, final int startY, final boolean multiplayer)
    {
        //we will only track this in single player
        if (!multiplayer)
        {
            //setup stat
            Stat stat = new Stat(WIDTH, HEIGHT);

            //only track the yoshi's in single player
            stat.setX(SINGLE_PLAYER_LOCATION_OFFSET_YOSHI.x + startX);
            stat.setY(SINGLE_PLAYER_LOCATION_OFFSET_YOSHI.y + startY);
            stat.setValue(0);
            stat.setFont(font);
            stat.render();

            //add to list
            add(stat, Key.Yoshi);
        }
    }
    
    private void createStatLevel(final Font font, final int startX, final int startY, final boolean multiplayer)
    {
        //setup stat
        Stat stat = new Stat(WIDTH, HEIGHT);
        
        //setup the coordinate ccordingly
        if (!multiplayer)
        {
            stat.setX(SINGLE_PLAYER_LOCATION_OFFSET_LEVEL.x + startX);
            stat.setY(SINGLE_PLAYER_LOCATION_OFFSET_LEVEL.y + startY);
        }
        else
        {
            stat.setX(MULTI_PLAYER_LOCATION_OFFSET_LEVEL.x + startX);
            stat.setY(MULTI_PLAYER_LOCATION_OFFSET_LEVEL.y + startY);
        }
        
        stat.setValue(1);
        stat.setFont(font);
        stat.render();
        
        //add to list
        add(stat, Key.Level);
    }
    
    private void createStatTimer(final Font font, final int startX, final int startY, final boolean multiplayer)
    {
        //setup stat
        Stat stat = new Stat(WIDTH, HEIGHT);
        
        //setup the coordinate ccordingly
        if (!multiplayer)
        {
            stat.setX(SINGLE_PLAYER_LOCATION_OFFSET_TIMER.x + startX);
            stat.setY(SINGLE_PLAYER_LOCATION_OFFSET_TIMER.y + startY);
        }
        else
        {
            stat.setX(MULTI_PLAYER_LOCATION_OFFSET_TIMER.x + startX);
            stat.setY(MULTI_PLAYER_LOCATION_OFFSET_TIMER.y + startY);
            
        }
        
        stat.setDesc("");
        stat.setFont(font);
        stat.render();
        
        //add to list
        add(stat, Key.Timer);
    }
    
    private void add(final Stat stat, final Key key)
    {
        //add to list
        this.stats.put(key, stat);
    }
    
    public Stat getStatScore()
    {
        return this.stats.get(Key.Score);
    }
    
    public Stat getStatTimer()
    {
        return this.stats.get(Key.Timer);
    }
    
    public Stat getStatLevel()
    {
        return this.stats.get(Key.Level);
    }
    
    public Stat getStatYoshi()
    {
        return this.stats.get(Key.Yoshi);
    }
    
    @Override
    public void dispose()
    {
        if (stats != null)
        {
            for (Key key : stats.keySet())
            {
                stats.get(key).dispose();
                stats.put(key, null);
            }
            
            stats.clear();
            stats = null;
        }
        
        timer = null;
    }
    
    /**
     * Are we counting down the game timer?
     * @param countdown true = yes, false = no
     */
    public void flagCountdown(final boolean countdown)
    {
        this.countdown = countdown;
    }
    
    /**
     * Are we counting down the game timer?
     * @return true = yes, false = no
     */
    private boolean hasCountdown()
    {
        return this.countdown;
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        for (Key key : stats.keySet())
        {
            //get the current stat
            final Stat stat = stats.get(key);
        
            //if flagged render a new image
            if (stat.hasFlag())
                stat.render();
        }
        
        //update timer
        getGameTimer().update(engine.getMain().getTime());
        
        //update the data every 1 second
        if (System.nanoTime() - previous >= Timers.NANO_SECONDS_PER_SECOND)
        {
            //store the new time so we will know when to update again
            previous = System.nanoTime();
            
            //update description
            updateGameTimerDesc();
        }
    }
    
    /**
     * Update the game timer description according to the current time
     */
    public void updateGameTimerDesc()
    {
        if (!hasCountdown())
        {
            //update timer description
            this.getStatTimer().setDesc(getGameTimer().getDescPassed(Timers.FORMAT_8));
        }
        else
        {
            //make sure the timer doesn't go below 0
            if (getGameTimer().getRemaining() < Entity.DELAY_NONE)
                getGameTimer().setRemaining(Entity.DELAY_NONE);

            this.getStatTimer().setDesc(getGameTimer().getDescRemaining(Timers.FORMAT_8));
        }
    }
    
    /**
     * Get the game timer.
     * @return The timer used to track the game time
     */
    public Timer getGameTimer()
    {
        return this.timer;
    }
    
    @Override
    public void render(final Graphics graphics) throws Exception
    {
        for (Key key : stats.keySet())
        {
            //get the current stat
            final Stat stat = stats.get(key);
            
            //draw the stat image
            stat.draw(graphics, stat.getBufferedImage());
        }
    }
}
