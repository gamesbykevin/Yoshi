package com.gamesbykevin.yoshi.player;

import com.gamesbykevin.framework.util.*;

import com.gamesbykevin.yoshi.board.Board;
import com.gamesbykevin.yoshi.board.BoardHelper;
import com.gamesbykevin.yoshi.engine.Engine;
import com.gamesbykevin.yoshi.board.piece.Piece;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

/**
 * Computer controlled player
 * @author GOD
 */
public final class Cpu extends Player
{
    //the targets for the 2 falling pieces
    private Target target1, target2;
    
    //the move timer
    private Timer timer;
    
    //the time delay for each difficulty
    private static final long DELAY_DIFFICULTY_EASY   = Timers.toNanoSeconds(500L);
    private static final long DELAY_DIFFICULTY_MEDIUM = Timers.toNanoSeconds(250L);
    private static final long DELAY_DIFFICULTY_HARD   = Timers.toNanoSeconds(125L);
    
    /*
     * The expected number of falling pieces, while locating our target(S)
     */
    private static final int FALLING_PIECE_COUNT = 2;
    
    /**
     * 0 Count
     */
    private static final int COUNT_NONE = 0;
    
    public Cpu(final Image image, final boolean multiplayer, final int difficultyIndex) throws Exception
    {
        super(image, multiplayer, difficultyIndex);
        
        //create timer with a time delay dependant on the diffuculty
        switch (difficultyIndex)
        {
            case Player.INDEX_DIFFICULTY_EASY:
                this.timer = new Timer(DELAY_DIFFICULTY_EASY);
                break;
                
            case Player.INDEX_DIFFICULTY_MEDIUM:
                this.timer = new Timer(DELAY_DIFFICULTY_MEDIUM);
                break;
                
            case Player.INDEX_DIFFICULTY_HARD:
                this.timer = new Timer(DELAY_DIFFICULTY_HARD);
                break;
            
            default:
                throw new Exception("Difficulty Index not found " + difficultyIndex);
        }
    }
    
    @Override
    public void setCoordinates(final boolean multiplayer)
    {
        if (multiplayer)
        {
            //set the start coordinates where the player will be placed
            setStartCoordinates(Player.MULTI_PLAYER_2_START_X, Player.MULTI_PLAYER_2_START_Y);
            
            //set the center of the board
            getBoard().setLocation(BoardHelper.MULTI_PLAYER_2_BOARD_CENTER);
            
            //set the location where pieces can be spawned
            getBoard().setStartPieceColumnX(BoardHelper.MULTI_PLAYER_2_COLUMN_1_X);
            getBoard().setStartPieceRowY(BoardHelper.MULTI_PLAYER_2_ROW_1_Y);
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
        super.addAnimation(Player.ANIMATION_KEY_ROTATE_BACK,  0,   80,   120, 40,  5, DELAY_DEFAULT, false);
        super.addAnimation(Player.ANIMATION_KEY_ROTATE_FRONT, 0,   120,  120, 40,  5, DELAY_DEFAULT, false);
        super.addAnimation(Player.ANIMATION_KEY_VICTORY,          0,   320, 220, 140, 1, DELAY_NONE,    false);
        super.addAnimation(Player.ANIMATION_KEY_LOSE,             220, 320, 220, 140, 1, DELAY_NONE,    false);
        
        //set default
        super.setAnimation(Player.ANIMATION_KEY_ROTATE_BACK);
        
        //flag this animation as finished
        super.getSpriteSheet().getSpriteSheetAnimation().setFinished(true);
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        //do we have falling pieces before updating
        final boolean hasFallingPiecesBefore = BoardHelper.hasFallingPieces(getBoard().getPieces());
        
        //update the extra things
        super.updateMisc(engine);
        
        //do we have falling pieces after updating
        final boolean hasFallingPieceAfter = BoardHelper.hasFallingPieces(getBoard().getPieces());
        
        //if we haven't picked our targets yet
        if (!hasSelectedTargets())
        {
            //locate our targets
            locateTargets();
        }
        else
        {
            //if our targeted pieces are still falling
            if (hasFallingPiecesBefore && hasFallingPieceAfter)
            {
                //update movement timer
                timer.update(engine.getMain().getTime());

                //don't continue until timer is finished
                if (!timer.hasTimePassed())
                    return;
                
                //if our targets are not yet at their destinations
                if (!hasTargetsSet())
                {
                    
                    //make sure we aren't currently swapping the board
                    if (!BoardHelper.isSwappingColumns(getBoard().getPieces()))
                    {

                        //move targets to their assigned destinations
                        placeTargets();
                    }
                }
                else
                {
                    //if the pieces are in place, apply gravity
                    getBoard().applyGravity();
                }

                //reset timer
                timer.reset();
            }
            else
            {
                //reset
                resetTargets();
            }
        }
    }
    
    protected Target getTarget1()
    {
        return this.target1;
    }
    
    protected Target getTarget2()
    {
        return this.target2;
    }
    
    /**
     * Place the targets at their destinations
     * @throws Exception if the column order index is not found, or if no valid moves were found
     */
    private void placeTargets() throws Exception
    {
        //if the source column has changed, update the index
        if (getColumnOrderIndex(getTarget1().getSourceColumn()) != getTarget1().getSourceColumnIndex())
            getTarget1().setSourceColumnIndex(getColumnOrderIndex(getTarget1().getSourceColumn()));
        if (getColumnOrderIndex(getTarget2().getSourceColumn()) != getTarget2().getSourceColumnIndex())
            getTarget2().setSourceColumnIndex(getColumnOrderIndex(getTarget2().getSourceColumn()));
        
        //the current index for our source columns
        final int currentTarget1Index = getColumnOrderIndex(getTarget1().getSourceColumn());
        final int currentTarget2Index = getColumnOrderIndex(getTarget2().getSourceColumn());

        /**
         * First we want the correct target columns to be on the correct side.<br>
         * This means we want the greater index to be on the right side of the lesser etc...
         */
        if (getTarget2().getDestinationColumnIndex() > getTarget1().getDestinationColumnIndex() && currentTarget2Index < currentTarget1Index)
        {
            performMove(currentTarget2Index);
        }
        else if (getTarget2().getDestinationColumnIndex() < getTarget1().getDestinationColumnIndex() && currentTarget2Index > currentTarget1Index)
        {
            performMove(currentTarget2Index - 1);
        }
        else if (getTarget1().getDestinationColumnIndex() > getTarget2().getDestinationColumnIndex() && currentTarget1Index < currentTarget2Index)
        {
            performMove(currentTarget1Index);
        }
        else if (getTarget1().getDestinationColumnIndex() < getTarget2().getDestinationColumnIndex() && currentTarget1Index > currentTarget2Index)
        {
            performMove(currentTarget1Index - 1);
        }
        else if (getTarget2().getDestinationColumnIndex() > getTarget1().getDestinationColumnIndex() && currentTarget2Index > currentTarget1Index && currentTarget1Index >= getTarget2().getDestinationColumnIndex())
        {
            performMove(currentTarget1Index - 1);
        }
        else if (getTarget2().getDestinationColumnIndex() < getTarget1().getDestinationColumnIndex() && currentTarget2Index < currentTarget1Index && currentTarget1Index <= getTarget2().getDestinationColumnIndex())
        {
            performMove(currentTarget1Index);
        }
        else if (getTarget1().getDestinationColumnIndex() > getTarget2().getDestinationColumnIndex() && currentTarget1Index > currentTarget2Index && currentTarget2Index >= getTarget1().getDestinationColumnIndex())
        {
            performMove(currentTarget2Index - 1);
        }
        else if (getTarget1().getDestinationColumnIndex() < getTarget2().getDestinationColumnIndex() && currentTarget1Index < currentTarget2Index && currentTarget2Index <= getTarget1().getDestinationColumnIndex())
        {
            performMove(currentTarget2Index);
        }
        else
        {
            /**
             * If the current index locations are on their correct side<br>
             * We can now move each column to the correct location.
             */
            if (currentTarget2Index < getTarget2().getDestinationColumnIndex())
            {
                performMove(currentTarget2Index);
            }
            else if (currentTarget2Index > getTarget2().getDestinationColumnIndex())
            {
                performMove(currentTarget2Index - 1);
            }
            else if (currentTarget1Index < getTarget1().getDestinationColumnIndex())
            {
                performMove(currentTarget1Index);
            }
            else if (currentTarget1Index > getTarget1().getDestinationColumnIndex())
            {
                performMove(currentTarget1Index - 1);
            }
            else
            {
                //this should not happen
                throw new Exception("Valid move not found");
            }
        }
    }
    
    /**
     * Here we will move the location towards the specified index
     * @param targetIndex The index we want to move to
     */
    private void performMove(final int targetIndex) throws Exception
    {
        //move to the location
        if (getCol() < targetIndex)
        {
            setCol(getCol() + 1);
        }
        else if (getCol() > targetIndex)
        {
            setCol(getCol() - 1);
        }
        else
        {
            //if we are at the specified location, swap columns
            switchColumns();
        }
    }
    
    /**
     * Here we will take the falling pieces and score each column to determine the best move
     * @throws Exception If the number of falling pieces is not 2, of if no targets are found
     */
    private void locateTargets() throws Exception
    {
        //get the starting pieces (there should only be 2)
        List<Piece> pieces = CpuHelper.getFallingPieces(getBoard().getPieces());
        
        //list of optional targets
        List<Target> targets = new ArrayList<>();
        
        /**
         * Make sure there are falling pieces, because they may not have been created yet.<br>
         * Also make sure we aren't swapping columns
         */
        if (!pieces.isEmpty() && !BoardHelper.isSwappingColumns(getBoard().getPieces()))
        {
            //throw exception if we don't get the expected count (should not happen)
            if (pieces.size() != FALLING_PIECE_COUNT)
                throw new Exception("Expected # of falling pieces is not 2 (" + pieces.size() + ")");
            
            //now check each column for the current piece
            for (int i = 0; i < pieces.size(); i++)
            {
                //get the current piece
                final Piece piece = pieces.get(i);

                //check each column
                for (int col = 0; col < Board.COLUMNS; col++)
                {
                    //the score of this column
                    int score = 0;
                    
                    //if the top piece is a top shell, let's see if we can create a yoshi
                    if (piece.getType() == Piece.TYPE_SHELL_TOP)
                    {
                        //get the total # of pieces that are above the bottom shell so we can check the size of the yoshi
                        final int count = CpuHelper.getBottomShellCount(getBoard(), col);

                        //get the row height so we can reward more for yoshi's created at a higher height, which will keep the height of the board lower
                        final int height = CpuHelper.getRowHeight(getBoard(), col);
                        
                        //calculate score
                        score = (Board.SCORE_YOSHI_PIECE * count) + (Board.SCORE_YOSHI_HEIGHT_REWARD * height);
                    }
                    else if (piece.getType() == Piece.TYPE_SHELL_BOTTOM)
                    {
                        //get the row height, we will penalize more, because we want to place this at the lowest point
                        final int height = CpuHelper.getRowHeight(getBoard(), col);

                        //calculate score
                        score = Board.SCORE_BOTTOM_SHELL_HEIGHT * height;
                    }
                    else
                    {
                        //get the top piece for this column
                        final Piece top = CpuHelper.getTopPiece(getBoard().getPieces(), col);

                        //if a top piece exists
                        if (top != null)
                        {
                            //if the pieces match score it
                            if (top.getType() == piece.getType())
                            {
                                //calculate score as we want to match pieces
                                score = Board.SCORE_PIECE_MATCH;
                            }
                            else
                            {
                                //the number of pieces above a bottom shell
                                final int count = CpuHelper.getBottomShellCount(getBoard(), col);
                                
                                //get the height of the column
                                final int height = CpuHelper.getRowHeight(getBoard(), col);
                                
                                //if there is no bottom shell in this column
                                if (count == COUNT_NONE)
                                {
                                    //calculate score, if no match and no bottom shell, penalize by the height
                                    score = Board.SCORE_PIECE_HEIGHT * height;
                                }
                                else
                                {
                                    //penalize the height, but add a different penalty for the pieces that can create a yoshi
                                    score = (Board.SCORE_PIECE_HEIGHT * (height - count)) + (Board.SCORE_YOSHI_HEIGHT_PENALTY * count);
                                }
                            }
                        }
                        else
                        {
                            //there is no top piece so no height etc... the score will be 0
                            score = Board.SCORE_NONE;
                        }
                    }

                    //store information for our target
                    final int destinationColumn = getColumnOrderValue((int)piece.getCol());
                    final int sourceColumn = getColumnOrderValue(col);
                    final int destinationColumnIndex = (int)piece.getCol();
                    final int sourceColumnIndex = col;
                    
                    //add target to list
                    targets.add(new Target(destinationColumn, destinationColumnIndex, sourceColumn, sourceColumnIndex, score));
                }
            }
            
            //sort the targets by score in descending order
            for (int i = 0; i < targets.size(); i++)
            {
                for (int x = 1; x < targets.size(); x++)
                {
                    //if the score is less we will switch
                    if (targets.get(x - 1).getScore() < targets.get(x).getScore())
                    {
                        //get the target
                        Target tmp = targets.get(x - 1);
                        
                        //swap the objects
                        targets.set(x - 1, targets.get(x));
                        targets.set(x, tmp);
                    }
                }
            }
            
            //this should not happen
            if (targets.isEmpty())
                throw new Exception("No targets are found");
            
            //the first target will be the highest scoring
            target1 = targets.get(0);
            
            /**
             * Now the targets are sorted in descending order.<br>
             * We can start checking for the next one that isn't already targeted
             */
            for (int i = 1; i < targets.size(); i++)
            {
                //get the current target
                Target tmp = targets.get(i);
                
                /**
                 * We don't want to target the same column or destination
                 */
                if (!getTarget1().hasTargetStats(tmp))
                {
                    //store the target
                    target2 = tmp;
                    
                    //exit loop, since we found 2nd highest scoring target
                    break;
                }
            }
        }
    }
    
    /**
     * Reset the targets
     */
    private void resetTargets()
    {
        target1 = null;
        target2 = null;
    }
    
    /**
     * Do we have the targets set?
     * @return true if the targeted columns are placed in the correct place, false otherwise
     */
    private boolean hasTargetsSet() throws Exception
    {
        //if we don't have targets return false
        if (!hasSelectedTargets())
            return false;
        
        //if the target column index is not where we want, return false
        if (getColumnOrderIndex(getTarget1().getSourceColumn()) != getTarget1().getDestinationColumnIndex())
            return false;
        
        //if the target column index is not where we want, return false
        if (getColumnOrderIndex(getTarget2().getSourceColumn()) != getTarget2().getDestinationColumnIndex())
            return false;
        
        //we have set the targets
        return true;
    }
    
    /**
     * Have we selected a target for both falling pieces?
     * @return true=yes, false=no
     */
    private boolean hasSelectedTargets()
    {
        return (getTarget1() != null && getTarget2() != null);
    }
}