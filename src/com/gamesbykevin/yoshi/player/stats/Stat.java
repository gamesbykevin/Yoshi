package com.gamesbykevin.yoshi.player.stats;

import com.gamesbykevin.framework.awt.CustomImage;
import java.awt.Color;

/**
 * This is a custom stat that will display on the screen
 * @author GOD
 */
public final class Stat extends CustomImage
{
    //the text description of the stat
    private String desc = "";
    
    //the value of the stat
    private int value;
    
    //flag whenever we need to render a new image
    private boolean flag = false;
    
    //is this stat numeric or not
    private boolean numeric = false;
    
    protected Stat(final int width, final int height)
    {
        super(width, height);
    }
    
    /**
     * Set the text description (if needed)
     * @param desc The description
     */
    protected void setDesc(final String desc)
    {
        this.desc = desc;
        this.flag = true;
        this.numeric = false;
    }
    
    protected String getDesc()
    {
        return this.desc;
    }
    
    /**
     * Set the stat value (if needed)
     * @param value The value
     */
    public void setValue(final int value)
    {
        this.value = value;
        this.flag = true;
        this.numeric = true;
    }
    
    public int getValue()
    {
        return this.value;
    }
    
    /**
     * Has this stat been flagged?<br>
     * If yes a new image will be rendered.
     * @return yes, if a change was made, otherwise false
     */
    protected boolean hasFlag()
    {
        return this.flag;
    }
    
    @Override
    public void render()
    {
        //no longer flag a change
        this.flag = false;
        
        //clear image
        super.clear();
        
        //set the color black
        getGraphics2D().setColor(Color.BLACK);
        
        //draw stat to new render image
        getGraphics2D().drawString((!numeric) ? getDesc() : getValue() + "", 0, 15);
    }
}
