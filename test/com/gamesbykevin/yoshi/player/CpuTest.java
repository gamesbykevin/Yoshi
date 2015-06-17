package com.gamesbykevin.yoshi.player;

import com.gamesbykevin.yoshi.engine.Engine;
import com.gamesbykevin.yoshi.main.Main;
import javax.swing.JPanel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author GOD
 */
public class CpuTest
{
    private Cpu cpu;
    
    private static final Main main = new Main(1, new JPanel());
    private static Engine engine;
    
    @BeforeClass
    public static void setUpClass() throws Exception
    {
        boolean multiplayer = false;
        Cpu cpu;
        cpu = new Cpu(null, multiplayer, Player.INDEX_DIFFICULTY_EASY);
        cpu = new Cpu(null, multiplayer, Player.INDEX_DIFFICULTY_MEDIUM);
        cpu = new Cpu(null, multiplayer, Player.INDEX_DIFFICULTY_HARD);
        
        multiplayer = true;
        cpu = new Cpu(null, multiplayer, Player.INDEX_DIFFICULTY_EASY);
        cpu = new Cpu(null, multiplayer, Player.INDEX_DIFFICULTY_MEDIUM);
        cpu = new Cpu(null, multiplayer, Player.INDEX_DIFFICULTY_HARD);
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
        cpu.dispose();
        cpu = null;
    }
    
    @Test
    public void setCoordinatesTest() throws Exception
    {
        boolean multiplayer = false;
        cpu = new Cpu(null, multiplayer, Player.INDEX_DIFFICULTY_EASY);
        cpu.setCoordinates(multiplayer);
        
        multiplayer = true;
        cpu = new Cpu(null, multiplayer, Player.INDEX_DIFFICULTY_EASY);
        cpu.setCoordinates(multiplayer);
    }
    
    @Test
    public void setupAnimationsTest() throws Exception
    {
        boolean multiplayer = false;
        cpu = new Cpu(null, multiplayer, Player.INDEX_DIFFICULTY_EASY);
        cpu.setupAnimations();
    }
    
    @Test
    public void updateTest() throws Exception
    {
        engine = new Engine(main);
        cpu = new Cpu(null, false, Player.INDEX_DIFFICULTY_EASY);
        cpu.update(engine);
    }
}