package angryflappybird;

import java.util.HashMap;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Defines {
    
	// dimension of the GUI application
    final int APP_HEIGHT = 600;
    final int APP_WIDTH = 600;
    final int SCENE_HEIGHT = 570;
    final int SCENE_WIDTH = 400;

    // coefficients related to the blob
    
    //for some reason this changes the background
    final int BLOB_WIDTH = 100;
    final int BLOB_HEIGHT = 100;
    
    final int BLOB_POS_X = 10;
    final int BLOB_POS_Y = 100;
    final int BLOB_DROP_TIME = 300000000;  	// the elapsed time threshold before the blob starts dropping
    final int BLOB_DROP_VEL = 300;    		// the blob drop velocity
    final int BLOB_FLY_VEL = -40;
    final int BLOB_IMG_LEN = 4;
    final int BLOB_IMG_PERIOD = 5;
    
    // coefficients related to the floors
    final int FLOOR_WIDTH = 500;
    final int FLOOR_HEIGHT = 100;
    final int FLOOR_COUNT = 2;
    
    // coefficients related to the pipes\
    //horizontal align
    final int D_PIPE_POS_X = 200;
    //vertical align
    final int D_PIPE_POS_Y = 30;

    //horizontal align
    final int U_PIPE_POS_X = 200;
    //vertical align
    final int U_PIPE_POS_Y = 400; //Adjust the position of the upward facing pipe to increase the gap between downward facing
    
    // coefficients related to the pipes size
    final int PIPE_WIDTH = 70;   // Adjust to your desired width
    final int PIPE_HEIGHT = 200;  // Adjust height 
    final int U_PIPE_HEIGHT = 300;
    final int U_PIPE_WIDTH = 70;
    final int D_PIPE_HEIGHT = 200; 
    final int D_PIPE_WIDTH = 70;
    final int PIPE_COUNT = 2;

    //initial pipe position
    final double PIPE_INITIAL_Y= 250;
    final double PIPE_RANGE= 200;
    final double PIPE_Y_GAP=500;
    final double PIPE_X_GAP=500;
   
    // coefficients related to time
    final int SCENE_SHIFT_TIME = 5;
    final double SCENE_SHIFT_INCR = -0.4;
    final double NANOSEC_TO_SEC = 1.0 / 1000000000.0;
    final double TRANSITION_TIME = 0.1;
    final int TRANSITION_CYCLE = 2;
    final int BACKGROUND_SWITCH_INTERVAL = 5;
    
    
    // coefficients related to media display
    final String STAGE_TITLE = "Angry Flappy Bird";
	private final String IMAGE_DIR = "../final_images/";
    final String[] IMAGE_FILES = {"day_background", "bird1", "bird2", "bird3", "bird4",  "floor1", "bird_with_parachute",
   "dpipe1", "dpipe2", "dpipe3", "dpipe4", "dpipe5", "upipe1", "upipe2", "upipe3", "upipe4", "upipe5", "game_over_sprite", "golden_egg", "monster_thief", "night_background",
      "special_coin", "start_button_sprite", "upipe1", "white_egg"};

    final HashMap<String, ImageView> IMVIEW = new HashMap<String, ImageView>();
    final HashMap<String, Image> IMAGE = new HashMap<String, Image>();
    
    //nodes on the scene graph
    Button startButton;
    Button easyButton;
    Button mediumButton;
    Button hardButton;
    
    // constructor
	Defines() {
		
		// initialize images
		for(int i=0; i<IMAGE_FILES.length; i++) {
			Image img;
			if (i == 5) {
//				System.out.println(IMAGE_FILES[i]);
				img = new Image(pathImage(IMAGE_FILES[i]), FLOOR_WIDTH, FLOOR_HEIGHT, false, false);
			}

			else if (IMAGE_FILES[i] == "dpipe1"){
				System.out.println(IMAGE_FILES[i]);
				img = new Image(pathImage(IMAGE_FILES[i]), D_PIPE_WIDTH, D_PIPE_HEIGHT, false, false);
			} else if (IMAGE_FILES[i] == "upipe1") {
				img = new Image(pathImage(IMAGE_FILES[i]), U_PIPE_WIDTH, U_PIPE_HEIGHT, false, false);
			}
			else if (i == 1 || i == 2 || i == 3 || i == 4){
				img = new Image(pathImage(IMAGE_FILES[i]), BLOB_WIDTH, BLOB_HEIGHT, false, false);
			}
			else {
				img = new Image(pathImage(IMAGE_FILES[i]), SCENE_WIDTH, SCENE_HEIGHT, false, false);
			}
    		IMAGE.put(IMAGE_FILES[i],img);
    	}
		
		// initialize image views
		for(int i=0; i<IMAGE_FILES.length; i++) {
    		ImageView imgView = new ImageView(IMAGE.get(IMAGE_FILES[i]));
    		IMVIEW.put(IMAGE_FILES[i],imgView);
    	}
		
		// initialize scene nodes
		startButton = new Button("Go!");
		easyButton = new Button("Easy");
//		easyButton.setStyle("-fx-background-color: #FFFF00;");
		mediumButton = new Button("Medium");
//		mediumButton.setStyle("-fx-background-color: #808080;");
		hardButton = new Button("Hard");
//		hardButton.setStyle("-fx-background-color: #808080;");
	}
	
//	this returns the path of a specific image
	public String pathImage(String filepath) {
    	String fullpath = getClass().getResource(IMAGE_DIR+filepath+".png").toExternalForm();
    	return fullpath;
    }
	
//	this returns the image once it's been resized
	public Image resizeImage(String filepath, int width, int height) {
    	IMAGE.put(filepath, new Image(pathImage(filepath), width, height, false, false));
    	return IMAGE.get(filepath);
    }
}