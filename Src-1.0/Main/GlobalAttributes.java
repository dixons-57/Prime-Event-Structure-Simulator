package Main;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.Random;

/* Class for storing general program-wide attributes that don't fit
 * into any particular class - these are all statically accessed */
public class GlobalAttributes {

	/* The title of the software */
    public static final String title="Prime Event Structure Simulator V1.0 by Daniel Morrison";
    
    /* The initial window size */
    public static Dimension initialSize=new Dimension(800,400);
    
    /* The program background colours for the various
     * panels/windows */
    public static Color background = Color.LIGHT_GRAY;
    public static Color outputBackground=Color.black;
    public static Color controlsBackground=Color.white;
    public static Color graphicsBackground=Color.white;
    
    /* Random number generator */
    public static Random random=new Random();
    
    /* Seed for the generator */
	public static int seed=11111111;
    
    /* The maximum wait period for an event thread */
    public static int maxWait=30000;
    
    /* The drawing mode - 1 means causality mode, 2 means transition
     * graph mode, 3 means circuit mode */
    public static int renderMode=1;
    
    /* Flag which remembers whether the current event structure
     * has had a transition graph generated for it
     */
    public static boolean transitions=false;
    
    /* The icon data for the program - loaded immediately */
    public static BufferedImage icon;
    
    /* Indictes which circuit has been loaded (an index from
     * the list)*/
    public static int circuitSelected=1;
    
    /* Synchronised call to the random number generator */
    public static synchronized float random(){
    	return GlobalAttributes.random.nextFloat();
    }
    
    /* The colour which indicates that a circuit element is
     * in the middle of processing
     */
	public static Color processingColor=new Color(255,128,128);	

	/* The colour which represents when an event is completed */
	public static Color completedColour=new Color(255,128,128);
	
	/* Line thicknesses for circuit rendering */
	public static Stroke thickStroke=new BasicStroke(4.0f);
	public static Stroke basicStroke=new BasicStroke(1.0f);
	
	/* Fonts used for circuit rendering */
	public static Font middleFont;
	public static Font smallFont;
}