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
    
    //the players playing the game
    private List<Player> players;
    
    /**
     * Constructor for Manager, this is the point where we load any menu option configurations
     * @param engine Engine for our game that contains all objects needed
     * @throws Exception 
     */
    public Manager(final Engine engine) throws Exception
    {
        //set the audio depending on menu setting
        engine.getResources().setAudioEnabled(engine.getMenu().getOptionSelectionIndex(LayerKey.Options, OptionKey.Sound) == CustomMenu.SOUND_ENABLED);
        
        //create the list container of players
        this.players = new ArrayList<>();
    }
    
    @Override
    public void reset(final Engine engine) throws Exception
    {
        //are we playing multi-player
        final boolean multiplayer = true;
        
        //remove any existing players
        players.clear();
        
        if (!multiplayer)
        {
            //the background image
            this.image = engine.getResources().getGameImage(GameImages.Keys.Background1Player);
            
            //create a new player and add to list
            players.add(new Cpu(engine.getResources().getGameImage(GameImages.Keys.Spritesheet), multiplayer));
        }
        else
        {
            //the background image
            this.image = engine.getResources().getGameImage(GameImages.Keys.Background2Player);
            
            //create players and add to list
            players.add(new Human(engine.getResources().getGameImage(GameImages.Keys.Spritesheet), multiplayer));
            players.add(new Cpu(engine.getResources().getGameImage(GameImages.Keys.Spritesheet), multiplayer));
        }
    }
    
    /**
     * Free up resources
     */
    @Override
    public void dispose()
    {
        try
        {
            if (image != null)
            {
                image.flush();
                image = null;
            }
            
            for (int i = 0; i < players.size(); i++)
            {
                players.get(i).dispose();
                players.set(i, null);
            }
            
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
        if (players != null)
        {
            for (int i = 0; i < players.size(); i++)
            {
                players.get(i).update(engine);
            }
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
        
        if (players != null)
        {
            for (int i = 0; i < players.size(); i++)
            {
                players.get(i).render(graphics);
            }
        }
    }
}