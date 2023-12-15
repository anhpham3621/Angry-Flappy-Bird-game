/**
 * @author: Emmanuella Umoye, Anh Pham, Keisha Modi
 * @date: 12/17/2023
 */

package angryflappybird;

import java.io.File;
import java.util.HashMap;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Defines class contains all the constants and variables used in the angry flappy bird game.
 */
public class Defines {

    // dimension of the GUI application
    final int APP_HEIGHT = 600;
    final int APP_WIDTH = 600;
    final int SCENE_HEIGHT = 570;
    final int SCENE_WIDTH = 400;

   //coefficients related to the blob
    final int MAX_LIFE=3;
    final int BLOB_WIDTH = 60;
    final int BLOB_HEIGHT = 60;
    final int BLOB_WITH_PARACHUTE_WIDTH = 70;
    final int BLOB_WITH_PARACHUTE_HEIGHT = 70;
    
    //blob drop time based on game mode
    final int BLOB_POS_X = 10;
    final int BLOB_POS_Y = 100;
    final int BLOB_DROP_TIME = 300000000;  	
    final int BLOB_MED_DROP_TIME = 290000000;
    final int BLOB_HARD_DROP_TIME = 280000000;
    final int BLOB_EASY_DROP_TIME = 300000000;
    
    //blob drop velocity based on game mode
    int BLOB_EASY_DROP_VEL = 300;    		
    final int BLOB_MED_DROP_VEL = 360;
    final int BLOB_HARD_DROP_VEL = 450;
    final int BLOB_FLY_VEL = -40;
    final int EASY_BLOB_FLY_VEL = -40;
    final int MED_BLOB_FLY_VEL = -48;
    final int HARD_BLOB_FLY_VEL = -60;
    final int BLOB_IMG_LEN = 4;
    final int BLOB_IMG_PERIOD = 5;
    
    // coefficients related to the floors
    final int FLOOR_WIDTH = 500;
    final int FLOOR_HEIGHT = 100;
    final int FLOOR_COUNT = 2;
    
    // coefficients related to the pipes position
    final int D_PIPE_POS_X = 400;
    final int D_PIPE_POS_Y = -240;
    final int U_PIPE_POS_X = 200;
    final int U_PIPE_POS_Y = 400; 

    // coefficients related to the pipes size
    final int PIPE_WIDTH = 60;   
    final int PIPE_HEIGHT = 200;  
    final int U_PIPE_HEIGHT = 400;
    final int U_PIPE_WIDTH = 55;
    final int D_PIPE_HEIGHT = 500; 
    final int D_PIPE_WIDTH = 55;
    final int PIPE_COUNT = 2;
    
    // coefficients related to the pigs
    final int PIG_HEIGHT = 80;
    final int PIG_WIDTH = 80;
    final int PIG_POS_X = 400;
    final int PIG_POS_Y = 300;
    double PIG_DROP_Y_VEL = 0.09;
    final int PIG_POS_START = 300;

    //initial pipe position
    final double PIPE_RANGE= 150;
    final double PIPE_Y_GAP=750;
    final double PIPE_X_GAP=300;
    
    //coefficients related to WHITE egg
    final double WHITE_EGG_HEIGHT = 80;
    final double WHITE_EGG_WIDTH = 80;
    final double WHITE_EGG_POS_Y = 300;
    final double WHITE_EGG_POS_X = 200;
    
    //coefficients related to GOLD egg
    final double GOLD_EGG_HEIGHT = 80;
    final double GOLD_EGG_WIDTH = 80;
    final double GOLD_EGG_POS_Y = 300;
    final double GOLD_EGG_POS_X = 200;
    
    // coefficients related to time
    final int EASY_SCENE_SHIFT_TIME = 10;
    final int MED_SCENE_SHIFT_TIME = 20;
    final int HARD_SCENE_SHIFT_TIME = 30;
    //final double SCENE_SHIFT_INCR = -0.2;
    final double EASY_SCENE_SHIFT_INCR = -0.2;
    final double MED_SCENE_SHIFT_INCR = -0.4;
    final double HARD_SCENE_SHIFT_INCR = -0.6;
    
    // coefficients related to the time
    final double NANOSEC_TO_SEC = 1.0 / 1000000000.0;
    final double TRANSITION_TIME = 0.1;
    final int TRANSITION_CYCLE = 2;
    final int BACKGROUND_SWITCH_INTERVAL = 5;
    
    final String STAGE_TITLE = "Angry Flappy Bird";

    //IMAGE SRC FOLDER
    private final String IMAGE_DIR = "../final_images/";
    final String[] IMAGE_FILES = {"day_background", "bird1", "bird2", "bird3", "bird4",  "floor1", "bird_with_parachute",
    "dpipe2", "dpipe2", "dpipe3", "dpipe4", "dpipe5", "upipe1", "upipe2", "upipe3", "upipe4", "upipe5", "game_over_sprite", "golden_egg", "monster_thief", "night_background",
        "special_coin", "start_button_sprite", "upipe1", "white_egg", "upipe1", "golden_egg","game_over_sprite"};

    private final String AUDIO_DIR = "../final_audio/";
    final String[] AUDIO_FILES = {"collect_coin_1", "bird_flapping_1", "obstacle_hit_1", "obstacle_hit_2"};

    //hashmaps to store images, imageviews, and audio
    final HashMap<String, ImageView> IMVIEW = new HashMap<String, ImageView>();
    final HashMap<String, Image> IMAGE = new HashMap<String, Image>();
    final HashMap<String, MediaPlayer> AUDIO = new HashMap<String, MediaPlayer>();
    
    //nodes on the scene graph
    Button startButton;
    ComboBox<String> mode;

    Defines() {
    	//initialize audio and save them in a hashmap
    	for(int i = 0; i < AUDIO_FILES.length; i++) {
    		Media audio;
    		audio = new Media(pathAudio(AUDIO_FILES[i]));
    		MediaPlayer media = new MediaPlayer(audio);
    		AUDIO.put(AUDIO_FILES[i], media);
    	}
        
        // initialize images and save them in a hashmap
        for(int i=0; i<IMAGE_FILES.length; i++) {
            Image img;
            if (i == 5) {
                img = new Image(pathImage(IMAGE_FILES[i]), FLOOR_WIDTH, FLOOR_HEIGHT, false, false);
            }

            else if (IMAGE_FILES[i] == "dpipe2"){
                img = new Image(pathImage(IMAGE_FILES[i]), D_PIPE_WIDTH, D_PIPE_HEIGHT, false, false);
            } else if (IMAGE_FILES[i] == "upipe1") {
                img = new Image(pathImage(IMAGE_FILES[i]), U_PIPE_WIDTH, U_PIPE_HEIGHT, false, false);
            } else if(IMAGE_FILES[i] == "white_egg") {
            	System.out.println("the white egg is visible");
                img = new Image(pathImage("white_egg"), WHITE_EGG_WIDTH, WHITE_EGG_HEIGHT, false, false);
            } else if(IMAGE_FILES[i] == "golden_egg") {
            	System.out.println("the golden egg is visible");
                img = new Image(pathImage("golden_egg"), GOLD_EGG_WIDTH, GOLD_EGG_HEIGHT, false, false);
            }
            else if(IMAGE_FILES[i] == "monster_thief") {
            	img = new Image(pathImage("monster_thief"), PIG_WIDTH, PIG_HEIGHT, false, false);
            }
            else if (i == 1 || i == 2 || i == 3 || i == 4){
                img = new Image(pathImage(IMAGE_FILES[i]), BLOB_WIDTH, BLOB_HEIGHT, false, false);
            } 
            else if(IMAGE_FILES[i] == "bird_with_parachute") {
            	img = new Image(pathImage(IMAGE_FILES[i]), BLOB_WITH_PARACHUTE_WIDTH, BLOB_WITH_PARACHUTE_WIDTH, false, false);
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
        
        mode = new ComboBox<String>();
        mode.getItems().addAll("Easy", "Medium", "Hard");
        mode.setValue("Easy");
    }
    
    /**
     * pathImage method returns the full path of the image file
     * @param filepath
     * @return
     */
    public String pathImage(String filepath) {
        String fullpath = getClass().getResource(IMAGE_DIR+filepath+".png").toExternalForm();
        return fullpath;
    }
    
    /**
     * resizeImage method resizes the image
     * @param filepath
     * @param width
     * @param height
     * @return
     */
    public Image resizeImage(String filepath, int width, int height) {
        IMAGE.put(filepath, new Image(pathImage(filepath), width, height, false, false));
        return IMAGE.get(filepath);
    }
    
    /**
     * pathAudio method returns the full path of the audio file
     * @param filepath
     * @return
     */
    public String pathAudio(String filepath) {
    	String fullpath = getClass().getResource(AUDIO_DIR+filepath+".mp3").toExternalForm();
    	return fullpath;
    }
}