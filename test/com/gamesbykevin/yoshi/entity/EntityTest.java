package com.gamesbykevin.yoshi.entity;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Entity Test
 * @author GOD
 */
public class EntityTest 
{
    private MyEntity entity;
    
    private static class MyEntity extends Entity
    {
        private MyEntity()
        {
            super();
        }
        
        //inherit class to test
    }
    
    @BeforeClass
    public static void setUpClass() 
    {
        assertTrue(Entity.DELAY_NONE == 0);
        assertTrue(Entity.NO_COUNT == 0);
        
        MyEntity entity = new MyEntity();
        
        assertNotNull(entity.getSpriteSheet());
    }
    
    @AfterClass
    public static void tearDownClass() 
    {
        
    }
    
    @Before
    public void setUp() 
    {
        
    }
    
    @After
    public void tearDown() 
    {
        entity.dispose();
        entity = null;
    }
    
    @Test
    public void addAnimationTest() 
    {
        final String key = "key";
        
        entity = new MyEntity();
        entity.addAnimation(key, 0, 0, 25, 25, 3, 0, true);
        
        assertTrue(entity.getSpriteSheet().hasAnimations());
        assertTrue(entity.getSpriteSheet().hasLoop());
    }
    
    @Test
    public void adjustDimensionsTest() 
    {
        entity = new MyEntity();
        entity.addAnimation("key", 0, 0, 25, 35, 3, 0, true);
        
        entity.adjustDimensions();
        
        assertTrue(entity.getWidth() == entity.getSpriteSheet().getLocation().width);
        assertTrue(entity.getHeight()== entity.getSpriteSheet().getLocation().height);
    }
    
    @Test
    public void updateAnimationTest() throws Exception 
    {
        entity = new MyEntity();
        entity.addAnimation("key", 0, 0, 25, 35, 3, 0, true);
        entity.updateAnimation(0);
    }
    
    @Test
    public void hasAnimationFinishedTest()
    {
        entity = new MyEntity();
        entity.addAnimation("key", 0, 0, 25, 35, 3, 0, true);
        entity.getSpriteSheet().getSpriteSheetAnimation().setFinished(true);
        
        assertTrue(entity.hasAnimationFinished());
        
        entity.getSpriteSheet().getSpriteSheetAnimation().setFinished(false);
        
        assertFalse(entity.hasAnimationFinished());
    }
    
    @Test
    public void setAnimationTest()
    {
        final String key1 = "key1";
        final String key2 = "key2";
        
        entity = new MyEntity();
        entity.addAnimation(key1, 0, 0, 25, 35, 3, 0, true);
        entity.addAnimation(key2, 0, 0, 15, 5, 3, 0, true);
        
        entity.setAnimation(key1);
        assertTrue(entity.getWidth() == 25);
        assertTrue(entity.getHeight() == 35);
        assertTrue(entity.getSpriteSheet().getCurrent() == key1);
        
        entity.setAnimation(key2);
        assertTrue(entity.getWidth() == 15);
        assertTrue(entity.getHeight() == 5);
        assertTrue(entity.getSpriteSheet().getCurrent() == key2);
    }
}