/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gamesbykevin.yoshi.player;

/**
 *
 * @author GOD
 */
public interface IPlayer 
{
    /**
     * Each player will need to setup their coordinates
     * @param multiplayer Are we playing multiplayer? true=yes false=no
     */
    public void setCoordinates(final boolean multiplayer);
    
    /**
     * Each player will need to setup their animations
     */
    public void setupAnimations();
}
