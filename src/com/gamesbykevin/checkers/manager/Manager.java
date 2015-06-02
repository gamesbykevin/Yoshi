package com.gamesbykevin.checkers.manager;

import com.gamesbykevin.framework.util.Timers;
import com.gamesbykevin.checkers.engine.Engine;
import com.gamesbykevin.checkers.menu.CustomMenu;
import com.gamesbykevin.checkers.menu.CustomMenu.*;
import com.gamesbykevin.checkers.resources.GameAudio;
import com.gamesbykevin.checkers.resources.GameFont;
import com.gamesbykevin.checkers.resources.GameImages;
import com.gamesbykevin.checkers.shared.Shared;

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
        
    }
    
    /**
     * Draw all of our application elements
     * @param graphics Graphics object used for drawing
     */
    @Override
    public void render(final Graphics graphics) throws Exception
    {
        
    }
}