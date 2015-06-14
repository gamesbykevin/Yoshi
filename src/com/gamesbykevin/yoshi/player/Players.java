package com.gamesbykevin.yoshi.player;

import com.gamesbykevin.framework.util.Timers;
import com.gamesbykevin.yoshi.board.BoardHelper;
import com.gamesbykevin.yoshi.engine.Engine;
import com.gamesbykevin.yoshi.entity.Entity;
import com.gamesbykevin.yoshi.shared.IElement;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This will contain the players in the game
 * @author GOD
 */
public final class Players implements IElement
{
    //the game mode we are playing
    private final int modeIndex;
    
    public static final int MODE_SINGLE_PLAYER_HUMAN = 0;
    public static final int MODE_VS_HIGH_SCORE = 1;
    public static final int MODE_VS_CLEAR_BOARD = 2;
    public static final int MODE_VS_ATTACK = 3;
    public static final int MODE_SINGLE_PLAYER_CPU = 4;
    
    //the delay for high score mode (3 minutes)
    private static final long MODE_HIGH_SCORE_DELAY = Timers.toNanoSeconds(3);
    
    //the remaining time for attack mode (5 minutes)
    private static final long MODE_ATTACK_DELAY = Timers.toNanoSeconds(5);
    
    //the players playing the game
    private List<Player> players;
    
    //is the game over
    private boolean gameover = false;
    
    /**
     * Create our players container
     * @param modeIndex The game mode we are playing
     * @param difficultyIndex The difficulty
     * @param image Sprite Sheet for players
     * @param font Font to use for the players stats
     * @param random Object used to make random decisions
     * @throws Exception 
     */
    public Players(final int modeIndex, final int difficultyIndex, final Image image, final Font font, final Random random) throws Exception
    {
        //store the mode index
        this.modeIndex = modeIndex;
        
        //create the list container of players
        this.players = new ArrayList<>();
        
        //is this multiplayer
        final boolean multiplayer = isMultiPlayer(this.modeIndex);
        
        switch (getModeIndex())
        {
            case MODE_SINGLE_PLAYER_HUMAN:
                players.add(new Human(image, multiplayer, difficultyIndex));
                break;
            
            case MODE_SINGLE_PLAYER_CPU:
                players.add(new Cpu(image, multiplayer, difficultyIndex));
                break;
                
            case MODE_VS_HIGH_SCORE:
            case MODE_VS_CLEAR_BOARD:
            case MODE_VS_ATTACK:
                players.add(new Human(image, multiplayer, difficultyIndex));
                players.add(new Cpu(image, multiplayer, difficultyIndex));
                break;
                
            default:
                throw new Exception("Mode not found " + modeIndex);
        }
        
        for (int i = 0; i < players.size(); i++)
        {
            //get the current player
            final Player player = players.get(i);
            
            //create our stats object
            player.createStats(font, multiplayer);
            
            //modify according to mode
            if (hasModeHighScore())
            {
                //set the timer for high score mode
                player.getStats().getGameTimer().setReset(MODE_HIGH_SCORE_DELAY);
                player.getStats().getGameTimer().reset();
                
                //notify that we are counting down the time
                player.getStats().flagCountdown(true);
            }
            else if (hasModeClearBoard())
            {
                //populate the board with pieces
                BoardHelper.populateBoard(player.getBoard(), random);
            }
            else if (hasModeAttack())
            {
                //set the timer for attack mode
                player.getStats().getGameTimer().setReset(MODE_ATTACK_DELAY);
                player.getStats().getGameTimer().reset();
                
                //notify that we are counting down the time
                player.getStats().flagCountdown(true);
            }
        }
    }
    
    private int getModeIndex()
    {
        return this.modeIndex;
    }
    
    /**
     * Is the current mode "Vs. High Score"
     * @return true = yes, false = no
     */
    public boolean hasModeHighScore()
    {
        return (getModeIndex() == MODE_VS_HIGH_SCORE);
    }
    
    /**
     * Is the current mode "Vs. Clear Board"
     * @return true = yes, false = no
     */
    public boolean hasModeClearBoard()
    {
        return (getModeIndex() == MODE_VS_CLEAR_BOARD);
    }
    
    /**
     * Is the current mode "Attack"
     * @return true = yes, false = no
     */
    public boolean hasModeAttack()
    {
        return (getModeIndex() == MODE_VS_ATTACK);
    }
    
    /**
     * Is the current mode multi-player?
     * @return true if the mode is multi-player, false otherwise
     */
    public boolean isMultiPlayer()
    {
        return isMultiPlayer(getModeIndex());
    }
    
    /**
     * Is this mode multi-player?
     * @param modeIndex The mode we want to check
     * @return true if the mode is multi-player, false otherwise
     */
    public static boolean isMultiPlayer(final int modeIndex)
    {
        return (modeIndex != MODE_SINGLE_PLAYER_HUMAN && modeIndex != MODE_SINGLE_PLAYER_CPU);
    }
    
    @Override
    public void dispose()
    {
        for (int i = 0; i < players.size(); i++)
        {
            players.get(i).dispose();
            players.set(i, null);
        }
    }
    
    private boolean hasGameOver()
    {
        return this.gameover;
    }
    
    /**
     * Damage opponent.<br>
     * We will damage our opponent by deducting time from the opponents timer.
     * @param player Our player that we do not want to damage
     * @param damage The amount of time to deduct from the opponents timer
     */
    public void damageOpponent(final Player player, final long damage)
    {
        for (int i = 0; i < players.size(); i++)
        {
            //get the current player
            final Player tmp = players.get(i);
            
            //don't damage ourselves
            if (tmp.getId() == player.getId())
                continue;
            
            //get the time remaining
            final long remaining = tmp.getStats().getGameTimer().getRemaining();
            
            if (remaining - damage < Entity.DELAY_NONE)
            {
                tmp.getStats().getGameTimer().setRemaining(Entity.DELAY_NONE);
                tmp.getStats().updateGameTimerDesc();
            }
            else
            {
                //now subtract damage
                tmp.getStats().getGameTimer().setRemaining(remaining - damage);
                tmp.getStats().updateGameTimerDesc();
            }
        }
    }
    
    private void checkGameOver()
    {
        for (int i = 0; i < players.size(); i++)
        {
            //get the current player
            final Player player = players.get(i);
            
            //if the game is over here return true
            if (player.getBoard().hasGameOver())
            {
                //flag other games over as well
                if (player.getBoard().hasLost())
                {
                    flagGameOverLose(player);
                }
                else
                {
                    flagGameOverWin(player);
                }
                
                this.gameover = true;
            }
        }
        
        //none of the games are over
        this.gameover = false;
    }
    
    /**
     * Flag the game over for all players.<br>
     * We will set the result for all players.<br>
     * The specified player will be the player who won, the other player(s) will lose
     * @param player The player who won
     */
    private void flagGameOverWin(final Player player)
    {
        for (int i = 0; i < players.size(); i++)
        {
            //get the current player
            final Player tmp = players.get(i);
            
            //record the player result
            tmp.setGameResult((tmp.getId() == player.getId()) ? false : true);
        }
    }
    
    /**
     * Flag the game over for all players.<br>
     * We will set the result for all players.<br>
     * The specified player will be the player who lost, the other player(s) will win
     * @param player The player who lost
     */
    private void flagGameOverLose(final Player player)
    {
        for (int i = 0; i < players.size(); i++)
        {
            //get the current player
            final Player tmp = players.get(i);
            
            //record the player result
            tmp.setGameResult((tmp.getId() == player.getId()) ? true : false);
        }
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        //no need to continue if the game has ended
        if (hasGameOver())
            return;
        
        for (int i = 0; i < players.size(); i++)
        {
            //get the current player
            final Player player = players.get(i);
            
            //skip this player if they already have a game over
            if (player.getBoard().hasGameOver())
                continue;
            
            //update the player
            player.update(engine);
            
            //check the players according to the game mode
            switch (getModeIndex())
            {
                //if the player runs out of time, they lose
                case MODE_VS_ATTACK:
                    
                    //if time ran out, this player lost
                    if (player.getStats().getGameTimer().hasTimePassed())
                        flagGameOverLose(player);
                    break;
                    
                //if the player clears the board they win
                case MODE_VS_CLEAR_BOARD:
                    
                    //if the board is cleared, flag winner
                    if (!BoardHelper.hasPlacedPieces(player.getBoard()))
                        this.flagGameOverWin(player);
                    break;
                    
                    
                //the player with the highest score wins
                case MODE_VS_HIGH_SCORE:
                    
                    //check for the winner when time runs out
                    if (player.getStats().getGameTimer().hasTimePassed())
                    {
                        //the score to beat
                        int score = -1;

                        //the winner
                        Player winner = null;

                        //check each player
                        for (int x = 0; x < players.size(); x++)
                        {
                            //get the current player
                            final Player tmp = players.get(x);

                            //check if there is a better score
                            if (tmp.getStats().getStatScore().getValue() > score)
                            {
                                //store the score to beat
                                score = tmp.getStats().getStatScore().getValue();

                                //this is the current winner
                                winner = tmp;
                            }
                        }

                        //flag the winner
                        flagGameOverWin(winner);
                    }
                    break;
            }
        }
        
        //only check when playing an opponent
        if (isMultiPlayer())
        {
            //check if the game is over
            checkGameOver();
        }
    }
    
    @Override
    public void render(final Graphics graphics) throws Exception
    {
        for (int i = 0; i < players.size(); i++)
        {
            players.get(i).render(graphics);
        }
    }
}