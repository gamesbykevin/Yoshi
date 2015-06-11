/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gamesbykevin.yoshi.player;

/**
 * This class will contain information about a target column.<br> 
 * This is used by the AI
 * @author GOD
 */
public final class Target 
{
    //the score of this target
    private final int score;

    //the column we want to move to
    private final int destinationColumn;
    
    //column we want to place under the falling piece
    private final int sourceColumn;
    
    //the column we want to move to
    private final int destinationColumnIndex;
    
    //column we want to place under the falling piece (this may need to change if the source does)
    private int sourceColumnIndex;
    
    /**
     * Create the target
     * @param destinationColumn The column we want to move to where the falling piece is
     * @param destinationColumnIndex
     * @param sourceColumn The column we want to move to the destination column
     * @param sourceColumnIndex
     * @param score The point score for this target
     */
    protected Target(final int destinationColumn, final int destinationColumnIndex, final int sourceColumn, final int sourceColumnIndex, final int score)
    {
        this.destinationColumn = destinationColumn;
        this.sourceColumn = sourceColumn;
        this.destinationColumnIndex = destinationColumnIndex;
        this.sourceColumnIndex = sourceColumnIndex;
        this.score = score;
    }

    /**
     * Check if the "target column" and "source column" match.<br>>
     * @param target Target we are comparing
     * @return true = yes, false = no
     */
    protected boolean hasTargetStats(final Target target)
    {
        return (hasDestinationColumn(target.getDestinationColumn()) || hasSourceColumn(target.getSourceColumn()));
    }

    /**
     * Do we already have this column targeted?
     * @param destinationColumn The column we want to check
     * @return true if the column matches, false otherwise
     */
    private boolean hasDestinationColumn(final int destinationColumn)
    {
        return (this.destinationColumn == destinationColumn);
    }

    /**
     * Do we already have this source column?
     * @param sourceColumn The column we want to check
     * @return true if the column matches, false otherwise
     */
    private boolean hasSourceColumn(final int sourceColumn)
    {
        return (getSourceColumn() == sourceColumn);
    }
    
    /**
     * Get the column we want to move to
     * @return The column we are looking to move to where the falling piece is
     */
    protected int getDestinationColumn()
    {
        return this.destinationColumn;
    }
    
    /**
     * Get the source column.
     * @return The column we want to move under the falling piece.
     */
    protected int getSourceColumn()
    {
        return this.sourceColumn;
    }
    
    /**
     * Get the index of the column we want to move to
     * @return The index we are looking to move to where the falling piece is
     */
    protected int getDestinationColumnIndex()
    {
        return this.destinationColumnIndex;
    }
    
    /**
     * Set the source column index
     * @param sourceColumnIndex The index of the column we want to move under the falling piece
     */
    protected void setSourceColumnIndex(final int sourceColumnIndex)
    {
        this.sourceColumnIndex = sourceColumnIndex;
    }
    
    /**
     * Get the source index.
     * @return The index we want to move under the falling piece.
     */
    protected int getSourceColumnIndex()
    {
        return this.sourceColumnIndex;
    }
    
    /**
     * Get the score.
     * @return The score for this target
     */
    protected int getScore()
    {
        return this.score;
    }
}