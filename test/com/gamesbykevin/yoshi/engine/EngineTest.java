package com.gamesbykevin.yoshi.engine;

import com.gamesbykevin.yoshi.main.Main;
import java.awt.Font;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.JApplet;
import javax.swing.JPanel;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * unit test Engine class
 * @author GOD
 */
public class EngineTest 
{
    private Engine engine;
    
    private static final Main main = new Main(1, new JPanel());
    
    @BeforeClass
    public static void setUpClass() throws Exception
    {
        Engine engine = new Engine(main);
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
        engine.dispose();
        
        assertNull(engine.getResources());
        assertNull(engine.getKeyboard());
        assertNull(engine.getMouse());
        assertNull(engine.getManager());
        assertNull(engine.getMenu());
        assertNull(engine.getRandom());
        
        engine = null;
    }
    
    @Test
    public void getSeedTest() throws Exception
    {
        engine = new Engine(main);
        assertTrue(engine.getSeed() > 0);
    }
    
    @Test
    public void resetTest() throws Exception
    {
        engine = new Engine(main);
        engine.reset();
        
        assertNull(engine.getManager());
    }
    
    @Test
    public void getMainTest() throws Exception
    {
        engine = new Engine(main);
        assertNotNull(engine.getMain());
    }
    
    @Test
    public void getRandomTest() throws Exception
    {
        engine = new Engine(main);
        assertNotNull(engine.getRandom());
    }
    
    @Test
    public void getManagerTest() throws Exception
    {
        engine = new Engine(main);
        assertNull(engine.getManager());
    }
    
    @Test
    public void getMenuTest() throws Exception
    {
        engine = new Engine(main);
        assertNull(engine.getMenu());
    }
    
    @Test
    public void getResourcesTest() throws Exception
    {
        engine = new Engine(main);
        assertNull(engine.getResources());
    }
    
    @Test
    public void keyReleasedTest() throws Exception
    {
        engine = new Engine(main);
        
        assertFalse(engine.getKeyboard().isKeyReleased());
        
        KeyEvent key = new KeyEvent(main.getPanel(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_UP,'Z');
        
        engine.keyReleased(key);
        
        assertTrue(engine.getKeyboard().isKeyReleased());
    }
    
    @Test
    public void keyPressedTest() throws Exception
    {
        engine = new Engine(main);
        
        assertFalse(engine.getKeyboard().isKeyPressed());
        
        KeyEvent key = new KeyEvent(main.getPanel(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_UP,'Z');
        
        engine.keyPressed(key);
        
        assertTrue(engine.getKeyboard().isKeyPressed());
    }
    
    @Test
    public void mouseClickedTest() throws Exception
    {
        engine = new Engine(main);
        
        assertFalse(engine.getMouse().isMouseClicked());
        
        MouseEvent event = new MouseEvent(new JPanel(), 1, System.currentTimeMillis(), InputEvent.CTRL_DOWN_MASK, 1, 1, 0, false);
        
        engine.mouseClicked(event);
        
        assertTrue(engine.getMouse().isMouseClicked());
    }
    
    @Test
    public void mousePressedTest() throws Exception
    {
        engine = new Engine(main);
        
        assertFalse(engine.getMouse().isMousePressed());
        
        MouseEvent event = new MouseEvent(new JPanel(), 1, System.currentTimeMillis(), InputEvent.CTRL_DOWN_MASK, 1, 1, 0, false);
        
        engine.mousePressed(event);
        
        assertTrue(engine.getMouse().isMousePressed());
    }
    
    @Test
    public void mouseReleasedTest() throws Exception
    {
        engine = new Engine(main);
        
        assertFalse(engine.getMouse().isMouseReleased());
        
        MouseEvent event = new MouseEvent(new JPanel(), 1, System.currentTimeMillis(), InputEvent.CTRL_DOWN_MASK, 1, 1, 0, false);
        
        engine.mouseReleased(event);
        
        assertTrue(engine.getMouse().isMouseReleased());
    }
    
    @Test
    public void mouseEnteredTest() throws Exception
    {
        engine = new Engine(main);
        
        assertFalse(engine.getMouse().hasMouseEntered());
        
        MouseEvent event = new MouseEvent(new JPanel(), 1, System.currentTimeMillis(), InputEvent.CTRL_DOWN_MASK, 1, 1, 0, false);
        
        engine.mouseEntered(event);
        
        assertTrue(engine.getMouse().hasMouseEntered());
    }
    
    @Test
    public void mouseExitedTest() throws Exception
    {
        engine = new Engine(main);
        
        assertFalse(engine.getMouse().hasMouseExited());
        
        MouseEvent event = new MouseEvent(new JPanel(), 1, System.currentTimeMillis(), InputEvent.CTRL_DOWN_MASK, 1, 1, 0, false);
        
        engine.mouseExited(event);
        
        assertTrue(engine.getMouse().hasMouseExited());
    }
    
    @Test
    public void mouseMovedTest() throws Exception
    {
        engine = new Engine(main);
        
        assertFalse(engine.getMouse().hasMouseMoved());
        
        MouseEvent event = new MouseEvent(new JPanel(), 1, System.currentTimeMillis(), InputEvent.CTRL_DOWN_MASK, 1, 1, 0, false);
        
        engine.mouseMoved(event);
        
        assertTrue(engine.getMouse().hasMouseMoved());
    }
    
    @Test
    public void mouseDraggedTest() throws Exception
    {
        engine = new Engine(main);
        
        assertFalse(engine.getMouse().isMouseDragged());
        
        MouseEvent event = new MouseEvent(new JPanel(), 1, System.currentTimeMillis(), InputEvent.CTRL_DOWN_MASK, 1, 1, 0, false);
        
        engine.mouseDragged(event);
        
        assertTrue(engine.getMouse().isMouseDragged());
    }
    
    @Test
    public void getMouseTest() throws Exception
    {
        engine = new Engine(main);
        assertNotNull(engine.getMouse());
    }
    
    @Test
    public void getKeyboardTest() throws Exception
    {
        engine = new Engine(main);
        assertNotNull(engine.getKeyboard());
    }
}