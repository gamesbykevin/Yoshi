package com.gamesbykevin.yoshi.main;


import com.gamesbykevin.framework.util.Timers;
import javax.swing.JApplet;
import javax.swing.JPanel;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test Main class
 * @author GOD
 */
public class MainTest 
{
    private Main main;
    
    private static final JPanel panel = new JPanel();
    private static final JApplet applet = new JApplet();
    
    
    @BeforeClass
    public static void setUpClass() 
    {
        Main main = new Main(60, panel);
        
        main = new Main(-1, applet);
        
        assertTrue(Main.UPS_MINIMUM == 1);
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
        main.dispose();
        
        assertNull(main.getApplet());
        assertNull(main.getPanel());
        assertNull(main.getScreen());
        
        main = null;
    }
    
    @Test
    public void createTest() throws Exception
    {
        main = new Main(10, panel);
        main.create();
    }
    
    @Test
    public void hasFocusTest() 
    {
        main = new Main(10, panel);
        
        //assume true
        assertTrue(main.hasFocus());
    }
    
    @Test
    public void setActiveTest()
    {
        main = new Main(30, panel);
        main.setActive(true);
        main.setActive(false);
    }
    
    @Test
    public void getAppletTest()
    {
        main = new Main(30, panel);
        assertNull(main.getApplet());
        
        main = new Main(30, applet);
        assertNotNull(main.getApplet());
    }
    
    @Test
    public void getPanelTest()
    {
        main = new Main(30, panel);
        assertNotNull(main.getPanel());
        
        main = new Main(30, applet);
        assertNull(main.getPanel());
    }
    
    @Test
    public void getTimeTest()
    {
        main = new Main(1, panel);
        assertTrue(main.getTime() == Timers.NANO_SECONDS_PER_SECOND);
        
        main = new Main(-1, panel);
        assertTrue(main.getTime() == Timers.NANO_SECONDS_PER_SECOND);
        
        final int ups = 60;
        main = new Main(ups, panel);
        assertTrue(main.getTime() == Timers.NANO_SECONDS_PER_SECOND / ups);
    }
    
    @Test
    public void getContainerClassTest()
    {
        main = new Main(1, panel);
        assertNotNull(main.getContainerClass());
        
        main = new Main(1, applet);
        assertNotNull(main.getContainerClass());
        
        JPanel jpanel = null;
        main = new Main(1, jpanel);
        assertNull(main.getContainerClass());
        
        JApplet japplet = null;
        main = new Main(1, japplet);
        assertNull(main.getContainerClass());
    }
    
    @Test
    public void getScreenTest()
    {
        main = new Main(1, applet);
        assertNotNull(main.getScreen());
    }
    
    @Test
    public void setFullScreenTest()
    {
        main = new Main(1, applet);
        main.setFullScreen();
        
        main = new Main(1, panel);
        main.setFullScreen();
    }
}