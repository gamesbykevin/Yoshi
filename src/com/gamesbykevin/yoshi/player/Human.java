package com.gamesbykevin.yoshi.player;

import com.gamesbykevin.framework.input.Keyboard;
import com.gamesbykevin.yoshi.board.BoardHelper;

import com.gamesbykevin.yoshi.engine.Engine;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;

/**
 * The human controlled player
 * @author GOD
 */
public final class Human extends Player
{
    public Human(final Image image, final int startX, final int startY)
    {
        super(image, startX, startY);
        
        //set animations
        setupAnimations();
    }
    
    @Override
    protected void setupAnimations()
    {
        super.addAnimation(Player.ANIMATION_KEY_ROTATE_COLUMNS_BACK, 0,   0,   120, 40,  5, DELAY_DEFAULT, false);
        super.addAnimation(Player.ANIMATION_KEY_ROTATE_COLUMNS_FRONT, 0,   40,  120, 40,  5, DELAY_DEFAULT, false);
        super.addAnimation(Player.ANIMATION_KEY_VICTORY,          0,   180, 220, 140, 1, DELAY_NONE,    false);
        super.addAnimation(Player.ANIMATION_KEY_LOSE,             220, 180, 220, 140, 1, DELAY_NONE,    false);
        
        //set default
        super.setAnimation(Player.ANIMATION_KEY_ROTATE_COLUMNS_BACK);
        
        //flag this animation as finished
        super.getSpriteSheet().getSpriteSheetAnimation().setFinished(true);
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        //update the board
        super.getBoard().update(engine);
        
        //update animation
        super.updateAnimation(engine.getMain().getTime());
        
        //get the keyboard input
        final Keyboard keyboard = engine.getKeyboard();
        
        //check  if we have a yoshi
        final boolean hasYoshi = BoardHelper.hasYoshi(getBoard().getPieces());
        
        if (keyboard.hasKeyReleased(KeyEvent.VK_LEFT))
        {
            //remove key released
            keyboard.removeKeyReleased(KeyEvent.VK_LEFT);
            
            if (hasAnimationFinished())
            {
                //update location
                super.setCol(super.getCol() - 1);
            }
        }
        else if (keyboard.hasKeyReleased(KeyEvent.VK_RIGHT))
        {
            //remove key released
            keyboard.removeKeyReleased(KeyEvent.VK_RIGHT);
            
            if (hasAnimationFinished())
            {
                //update location
                super.setCol(super.getCol() + 1);
            }
        }
        else if (keyboard.hasKeyReleased(KeyEvent.VK_DOWN))
        {
            //remove key released
            keyboard.removeKeyReleased(KeyEvent.VK_DOWN);
            
            if (hasAnimationFinished() && !hasYoshi)
            {
                //force gravity to be applied
                getBoard().applyGravity();
            }
        }
        else if (keyboard.hasKeyReleased(KeyEvent.VK_SPACE))
        {
            //remove key released
            keyboard.removeKeyReleased(KeyEvent.VK_SPACE);
            
            if (hasAnimationFinished() && !hasYoshi)
            {
                //flip the front
                setFront(!hasFront());

                if (hasFront())
                {
                    super.setAnimation(Player.ANIMATION_KEY_ROTATE_COLUMNS_BACK);
                    super.resetAnimation();
                }
                else
                {
                    super.setAnimation(Player.ANIMATION_KEY_ROTATE_COLUMNS_FRONT);
                    super.resetAnimation();
                }
            }
        }
    }
    
    @Override
    public void render(final Graphics graphics) throws Exception
    {
        //render board
        super.render(graphics);
    }
}