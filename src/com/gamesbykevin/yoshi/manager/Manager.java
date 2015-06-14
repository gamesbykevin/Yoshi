package com.gamesbykevin.yoshi.manager;

import com.gamesbykevin.yoshi.player.Players;
import com.gamesbykevin.yoshi.engine.Engine;
import com.gamesbykevin.yoshi.menu.CustomMenu;
import com.gamesbykevin.yoshi.menu.CustomMenu.*;
import com.gamesbykevin.yoshi.resources.GameAudio;
import com.gamesbykevin.yoshi.resources.GameFont;
import com.gamesbykevin.yoshi.resources.GameImages;

import java.awt.Graphics;
import java.awt.Image;
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
    
    //the players in the game
    private Players players;
    
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
        //get the difficulty
        final int difficultyIndex = engine.getMenu().getOptionSelectionIndex(LayerKey.Options, OptionKey.Difficulty);
        
        //get the game mode
        final int modeIndex = engine.getMenu().getOptionSelectionIndex(LayerKey.Options, OptionKey.Mode);
        
        //are we playing multi-player
        final boolean multiplayer = Players.isMultiPlayer(modeIndex);

        //the background image will be determined by multi-player
        this.image = engine.getResources().getGameImage((!multiplayer) ? GameImages.Keys.Background1Player : GameImages.Keys.Background2Player);
        
        if (players == null)
        {
            players = new Players(
                modeIndex, 
                difficultyIndex, 
                engine.getResources().getGameImage(GameImages.Keys.Spritesheet),
                engine.getResources().getGameFont(GameFont.Keys.Default),
                engine.getRandom()
            );
        }
    }
    
    public Players getPlayers()
    {
        return this.players;
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
            
            if (players != null)
            {
                players.dispose();
                players = null;
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
            players.update(engine);
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
            players.render(graphics);
        }
    }
}