package com.gamesbykevin.yoshi.player;

import com.gamesbykevin.yoshi.board.Board;
import com.gamesbykevin.yoshi.shared.IElement;

import java.awt.Image;

/**
 * The player that plays the board
 * @author GOD
 */
public abstract class Player implements IElement
{
    //the game board
    private Board board;
    
    public Player(final Image image)
    {
        this.board = new Board(image);
    }
    
    /**
     * Get the player's game board
     * @return 
     */
    protected Board getBoard()
    {
        return this.board;
    }
    
    @Override
    public void dispose()
    {
        if (board != null)
        {
            board.dispose();
            board = null;
        }
    }
}