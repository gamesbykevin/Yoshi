package com.gamesbykevin.yoshi.resources;

import com.gamesbykevin.framework.resources.*;

/**
 * All audio for game
 * @author GOD
 */
public final class GameAudio extends AudioManager
{
    //description for progress bar
    private static final String DESCRIPTION = "Loading Audio Resources";
    
    /**
     * These are the keys used to access the resources and need to match the id in the xml file
     */
    public enum Keys
    {
        CreateYoshi0, CreateYoshi1, CreateYoshi2, CreateYoshi3,
        CreateYoshi4, CreateYoshi5, CreateYoshi6, CreateYoshi7,
        MatchPieces, MovePlayer, PlacePiece, 
        SwapColumns, TopShellPlaced,
        
        MusicPlayer1, MusicPlayer2, GameOver
    }
    
    public GameAudio() throws Exception
    {
        super(Resources.XML_CONFIG_GAME_AUDIO);
        
        //the description that will be displayed for the progress bar
        super.setProgressDescription(DESCRIPTION);
        
        if (Keys.values().length < 1)
            super.increase();
    }
}