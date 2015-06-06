package com.gamesbykevin.yoshi.manager;

import com.gamesbykevin.framework.util.Timers;
import com.gamesbykevin.yoshi.player.*;
import com.gamesbykevin.yoshi.engine.Engine;
import com.gamesbykevin.yoshi.menu.CustomMenu;
import com.gamesbykevin.yoshi.menu.CustomMenu.*;
import com.gamesbykevin.yoshi.resources.GameAudio;
import com.gamesbykevin.yoshi.resources.GameFont;
import com.gamesbykevin.yoshi.resources.GameImages;
import com.gamesbykevin.yoshi.shared.Shared;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * The class that contains all of the game elements
 * @author GOD
 */
public final class Manager implements IManager
{
    //background image to display
    private Image image;
    
    //the player playing the game
    private Player player;
    
    /**
     * Constructor for Manager, this is the point where we load any menu option configurations
     * @param engine Engine for our game that contains all objects needed
     * @throws Exception 
     */
    public Manager(final Engine engine) throws Exception
    {
        //set the audio depending on menu setting
        engine.getResources().setAudioEnabled(engine.getMenu().getOptionSelectionIndex(LayerKey.Options, OptionKey.Sound) == CustomMenu.SOUND_ENABLED);
    }
    
    @Override
    public void reset(final Engine engine) throws Exception
    {
        this.image = engine.getResources().getGameImage(GameImages.Keys.Background1Player);
        
        if (this.player == null)
        {
            this.player = new Human(engine.getResources().getGameImage(GameImages.Keys.Spritesheet));
        }
    }
    
    public Player getPlayer()
    {
        return this.player;
    }
    
    /**
     * Free up resources
     */
    @Override
    public void dispose()
    {
        try
        {
            //recycle objects
            super.finalize();
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Update all elements
     * @param engine Our game engine
     * @throws Exception 
     */
    @Override
    public void update(final Engine engine) throws Exception
    {
        if (player != null)
        {
            player.update(engine);
        }
    }
    
    /**
     * Draw all of our application elements
     * @param graphics Graphics object used for drawing
     */
    @Override
    public void render(final Graphics graphics) throws Exception
    {
        graphics.drawImage(image, 0, 0, null);
        
        if (player != null)
        {
            player.render(graphics);
        }
    }
}