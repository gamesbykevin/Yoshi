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
    
    /**
     * The amount of time to wait between each move
     */
    private static final long MOVE_DELAY = Timers.toNanoSeconds(25L);
    
    /*
     * The expected number of falling pieces, while locating our target(S)
     */
    private static final int FALLING_PIECE_COUNT = 2;
    
    public Cpu(final Image image, final boolean multiplayer)
    {
        super(image, multiplayer);
        
        //create timer
        this.timer = new Timer(MOVE_DELAY);
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
        if (getBoard().hasLost())
            return;
        
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
                //if our targets are not yet at their destinations
                if (!hasTargetsSet())
                {
                    //update movement timer
                    timer.update(engine.getMain().getTime());
                    
                    //don't continue until timer is finished
                    if (!timer.hasTimePassed())
                        return;
                    
                    //make sure we aren't currently swapping the board
                    if (!BoardHelper.isSwappingColumns(getBoard().getPieces()))
                    {
                        //reset timer
                        timer.reset();

                        //move targets to their assigned destinations
                        placeTargets();
                    }
                }
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
     * Locate the targets
     * @throws Exception If the number of falling pieces is not 2
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
                throw new Exception("Number of falling pieces is not 2 (" + pieces.size() + ")");
            
            //now check each column for the current piece
            for (int i = 0; i < pieces.size(); i++)
            {
                //get the current piece
                final Piece piece = pieces.get(i);

                //check each column
                for (int col = 0; col < Board.COLUMNS; col++)
                {
                    //the score of this column
                    int score;
                    
                    //if the top piece is a top shell, let's see if we can create a yoshi
                    if (piece.getType() == Piece.TYPE_SHELL_TOP)
                    {
                        //get the total # of pieces that are above the bottom shell
                        final int count = CpuHelper.getBottomShellCount(getBoard(), col);

                        //calculate score
                        score = Board.SCORE_YOSHI_PIECE * count;
                    }
                    else if (piece.getType() == Piece.TYPE_SHELL_BOTTOM)
                    {
                        //get the row height
                        final int height = CpuHelper.getRowHeight(getBoard(), col);

                        //calculate score
                        score = Board.SCORE_PIECE_HEIGHT * height;
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
                                //calculate score
                                score = Board.SCORE_PIECE_MATCH;
                            }
                            else
                            {
                                //if no match score by the height
                                final int height = CpuHelper.getRowHeight(getBoard(), col);

                                //calculate score
                                score = Board.SCORE_PIECE_HEIGHT * height;
                            }
                        }
                        else
                        {
                            //calculate score
                            score = Board.SCORE_PIECE_HEIGHT * 0;
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
            
            //the first target will be the highest scoring
            target1 = targets.get(0);
            
            /**
             * Now the targets are sorted in descending order.<br>
             * We can start checking one by one to set the targets
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