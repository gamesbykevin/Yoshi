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
    public Human(final Image image, final boolean multiplayer)
    {
        super(image, multiplayer);
    }
    
    @Override
    public void setCoordinates(final boolean multiplayer)
    {
        if (multiplayer)
        {
            //set the start coordinates where the player will be placed
            setStartCoordinates(Player.MULTI_PLAYER_1_START_X, Player.MULTI_PLAYER_1_START_Y);
            
            //set the center of the board
            getBoard().setLocation(BoardHelper.MULTI_PLAYER_1_BOARD_CENTER);
            
            //set the location where pieces can be spawned
            getBoard().setStartPieceColumnX(BoardHelper.MULTI_PLAYER_1_COLUMN_1_X);
            getBoard().setStartPieceRowY(BoardHelper.MULTI_PLAYER_1_ROW_1_Y);
        }
        else
        {
            //set the start coordinates where the player will be placed
            setStartCoordinates(Player.SINGLE_PLAYER_START_X, Player.SINGLE_PLAYER_START_Y);
            
            //set the center of the board
            getBoard().setLocation(BoardHelper.SINLGE_PLAYER_BOARD_CENTER);
            
            //set the location where pieces can be spawned
            getBoard().setStartPieceColumnX(BoardHelper.SINGLE_PLAYER_COLUMN_1_X);
            getBoard().setStartPieceRowY(BoardHelper.SINGLE_PLAYER_ROW_1_Y);
        }
    }
    
    @Override
    public void setupAnimations()
    {
        super.addAnimation(Player.ANIMATION_KEY_ROTATE_BACK,  0,   0,   120, 40,  5, DELAY_DEFAULT, false);
        super.addAnimation(Player.ANIMATION_KEY_ROTATE_FRONT, 0,   40,  120, 40,  5, DELAY_DEFAULT, false);
        super.addAnimation(Player.ANIMATION_KEY_VICTORY,          0,   180, 220, 140, 1, DELAY_NONE,    false);
        super.addAnimation(Player.ANIMATION_KEY_LOSE,             220, 180, 220, 140, 1, DELAY_NONE,    false);
        
        //set default
        super.setAnimation(Player.ANIMATION_KEY_ROTATE_BACK);
        
        //flag this animation as finished
        super.getSpriteSheet().getSpriteSheetAnimation().setFinished(true);
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        if (getBoard().hasLost())
            return;
        
        //update the board
        super.getBoard().update(engine);
        
        if (getBoard().hasLost())
        {
            //set the lose animation
            super.setAnimation(Player.ANIMATION_KEY_LOSE);
            
            //set the coordinates in middle of the board
            super.setX(getBoard().getX() - (getWidth() / 2));
            super.setY(getBoard().getY() - (getHeight() / 2));
            
            //no need to continue
            return;
        }
        
        //update animation
        super.updateAnimation(engine.getMain().getTime());
        
        //get the keyboard input
        final Keyboard keyboard = engine.getKeyboard();
        
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
            
            //force gravity to be applied
            getBoard().applyGravity();
        }
        else if (keyboard.hasKeyReleased(KeyEvent.VK_SPACE))
        {
            //remove key released
            keyboard.removeKeyReleased(KeyEvent.VK_SPACE);
            
            //if switching the columns was successful
            if (switchColumns())
            {
                //flip the direction facing
                setFront(!hasFront());

                //change the animation
                if (hasFront())
                {
                    super.setAnimation(Player.ANIMATION_KEY_ROTATE_BACK);
                    super.resetAnimation();
                }
                else
                {
                    super.setAnimation(Player.ANIMATION_KEY_ROTATE_FRONT);
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