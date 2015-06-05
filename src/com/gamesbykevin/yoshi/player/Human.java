package com.gamesbykevin.yoshi.player;

import com.gamesbykevin.yoshi.engine.Engine;

import java.awt.Graphics;
import java.awt.Image;

/**
 * The human controlled player
 * @author GOD
 */
public final class Human extends Player
{
    public Human(final Image image)
    {
        super(image);
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        //update the board
        super.getBoard().update(engine);
    }
    
    @Override
    public void render(final Graphics graphics) throws Exception
    {
        //render the board
        super.getBoard().render(graphics);
    }
}