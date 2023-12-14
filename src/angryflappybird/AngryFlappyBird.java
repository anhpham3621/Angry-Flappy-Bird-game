//Version Dec 8: Anh pushed
//Please do not edit font, and position of text/box randomly, let's discuss if changes are needed
package angryflappybird;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.awt.Label;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//The Application layer
public class AngryFlappyBird extends Application {
	private boolean isAutoPilot = false;
	private double movex = 0;
    private double movey = 0;
    private double xvariation = 0;
    private double yvariation = 0;
    private double rate = 1;
    
    private boolean right = true;
    private boolean up = false;
	private boolean isAutoPilotMode = false;
	private boolean collisionDetected = false;
	private double SCENE_SHIFT_TIME=10;
	private double SCENE_SHIFT_INCR=-0.2;
	private int BLOB_FLY_VEL;
	private int BLOB_DROP_VEL;
	private int BLOB_DROP_TIME;
	
	//Game Over Animation
	private ImageView gameOverAnimation;

	private int gold_egg_counter = 0;
	private boolean isInitGame = false;
    private MediaPlayer mediaPlayer;
	private Defines DEF = new Defines();
	private boolean isCollided = false;
	
	//text display
	private int currentLives;
	private Text livesText;
	public int currentScores;
	private Text scoreText;
	private int pairNumber = 0;
	private int lastPassedPipeIndex = -1;
	// time related attributes
	private long clickTime, startTime, elapsedTime;
	private AnimationTimer timer;
	// game components
	private Blob blob;
	private ArrayList<Sprite> floors;
	private ArrayList<Pipe> uPipes;
	private ArrayList<Pipe> dPipes;
	private Sprite whiteEgg;
	private Sprite goldEgg;
	private Sprite pig;
	private Blob blob2;
	private double uPipeInitialY;
	// game flags
	private boolean CLICKED, GAME_START, GAME_OVER;
	// scene graphs
	private Group gameScene;	 // the left half of the scene
	private VBox gameControl;	 // the right half of the GUI (control)
	private VBox gameLevel;
	private VBox gameDesc;
	private GraphicsContext gc;		
//	private int currentScore;
	private int bg_counter = 0;
	double nextX_down = 0;
	double nextY_down = 0;
	double nextX_up = 0;
	double nextY_up = 0;
	double initialY = 0;
	private ImageView background;
	private Pipe uPipe;
	private Pipe dPipe;
	private boolean isNightBackground = true;
	private boolean pigHitsEgg = false;
	private boolean pigHitsGoldEgg = false;
	private boolean blobHitsEgg = false;
	private boolean blobHitsGoldEgg = false;
	private boolean showEgg = true;
	private boolean isGoldEggIntersected = false;
	private ComboBox<String> comboBox;
	private String gameMode = "";
		// the mandatory main method
	public static void main(String[] args) {
		launch(args);
		}
		// the start method sets the Stage layer
		@Override
	public void start(Stage primaryStage) throws Exception {
		// initialize scene graphs and UIs
		GameModeHandler(DEF.mode.getValue());
		resetGameControl(); // resets the gameControl
		resetGameScene(true); // resets the gameScene
			
		HBox root = new HBox();
		HBox.setMargin(gameScene, new Insets(0,0,0,15));
		root.getChildren().add(gameScene);
		root.getChildren().add(gameControl);
		
		// add scene graphs to scene
		Scene scene = new Scene(root, DEF.APP_WIDTH, DEF.APP_HEIGHT);
		// finalize and show the stage
		primaryStage.setScene(scene);
		primaryStage.setTitle(DEF.STAGE_TITLE);
		primaryStage.setResizable(false);
		primaryStage.show();
		}
	// the getContent method sets the Scene layer
	private void resetGameControl() {
		DEF.startButton.setOnMouseClicked(this::mouseClickHandler);
		//GameModeHandler(DEF.mode.getValue());
		gameControl = new VBox(30);
		gameDesc = new VBox(15);
		Text pigTextLabel = new Text("Avoid the pigs");
		Text goldEggLabel = new Text("Collect the golden eggs");
		Text whiteEggLabel = new Text("Collect the white eggs");
		Text pipeLabel = new Text("Avoid the pipes");
		ImageView pigImage = new ImageView(DEF.IMAGE.get("monster_thief"));
		pigImage.setFitHeight(50);
		pigImage.setFitWidth(50);
		ImageView goldEggImage = new ImageView(DEF.IMAGE.get("golden_egg"));
		goldEggImage.setFitHeight(50);
		goldEggImage.setFitWidth(50);
		ImageView whiteEggImage = new ImageView(DEF.IMAGE.get("white_egg"));
		whiteEggImage.setFitHeight(50);
		whiteEggImage.setFitWidth(50);
		// this is appearing by the pig for some reason
		ImageView pipeImage = new ImageView(DEF.IMAGE.get("upward_pipe"));
		pipeImage.setFitHeight(50);
		pipeImage.setFitWidth(150);
		HBox pHBox = new HBox(5);
		pHBox.getChildren().addAll(pigImage, pigTextLabel);
		//
		HBox wHBox = new HBox(5);
		wHBox.getChildren().addAll(whiteEggImage, whiteEggLabel);
		HBox piHBox = new HBox(5);
		pHBox.getChildren().addAll(pipeImage, pipeLabel);
		HBox gHBox = new HBox(5);
		gHBox.getChildren().addAll(goldEggImage, goldEggLabel);
		gameDesc.getChildren().addAll(pHBox, gHBox, wHBox, piHBox);
		DEF.mode.setPromptText("Change Mode");

		// Add an event listener to the combo box to detect when an item is selected
		DEF.mode.setOnAction(event -> GameModeHandler(DEF.mode.getValue()));
		gameLevel = new VBox(5);
		gameLevel.getChildren().addAll( DEF.mode);
		gameControl.getChildren().addAll(DEF.startButton, gameLevel, gameDesc);
		}

	private void GameModeHandler(String mode) {
	       switch (mode) {
	           case "Easy":
	        	System.out.println("EASY");
	            SCENE_SHIFT_INCR = DEF.EASY_SCENE_SHIFT_INCR;
	   			SCENE_SHIFT_TIME = DEF.EASY_SCENE_SHIFT_TIME;
	   			BLOB_DROP_TIME = DEF.BLOB_EASY_DROP_TIME;
	   			BLOB_DROP_VEL = DEF.BLOB_EASY_DROP_VEL;
	   			BLOB_FLY_VEL = DEF.EASY_BLOB_FLY_VEL;
	               break;
	           case "Medium":
	               //DEF.BLOB_EASY_DROP_VEL = 250; //increase bird drop velocity
	        	System.out.println("MEDIUM");
	   			SCENE_SHIFT_INCR = DEF.MED_SCENE_SHIFT_INCR;
	   			SCENE_SHIFT_TIME = DEF.MED_SCENE_SHIFT_TIME;
	   			BLOB_DROP_TIME = DEF.BLOB_MED_DROP_TIME;
	   			BLOB_DROP_VEL = DEF.BLOB_MED_DROP_VEL;
	   			BLOB_FLY_VEL = DEF.MED_BLOB_FLY_VEL;
	               break;
	           case "Hard":
//	               DEF.PIG_DROP_Y_VEL=0.5; //increase speed of pigs falling down the pipes
//	               DEF.BLOB_EASY_DROP_VEL = 250; //increase bird drop velocity
	        	System.out.println("HARD");
	        	SCENE_SHIFT_INCR = DEF.HARD_SCENE_SHIFT_INCR;
	   			SCENE_SHIFT_TIME = DEF.HARD_SCENE_SHIFT_TIME;
	   			BLOB_DROP_TIME = DEF.BLOB_HARD_DROP_TIME;
	   			BLOB_DROP_VEL = DEF.BLOB_HARD_DROP_VEL;
	   			BLOB_FLY_VEL = DEF.HARD_BLOB_FLY_VEL;
	               break;
	       }
	 }
	private void mouseClickHandler(MouseEvent e) {
		if (GAME_OVER) {
			resetGameScene(false);
			//(false);
	}
		else if (GAME_START){
			clickTime = System.nanoTime();
			gameOverAnimation.setVisible(false);
	        gameScene.getChildren().remove(gameOverAnimation);
	}
		GAME_START = true;
		CLICKED = true;
	}
	private void resetGameScene(boolean firstEntry) {
		// reset variables
		CLICKED = false;
		GAME_OVER = false;
		GAME_START = false;
		floors = new ArrayList<>();
		uPipes = new ArrayList<>();
		dPipes = new ArrayList<>();
		
		if(firstEntry) {
			isInitGame = true;
			// create two canvases
			Canvas canvas = new Canvas(DEF.SCENE_WIDTH, DEF.SCENE_HEIGHT);
			gc = canvas.getGraphicsContext2D();
			//Make background defined globally as an attribute
			// create a background
			background = DEF.IMVIEW.get("day_background");
			// System.out.println(background);
			// create the game scene
			gameScene = new Group();
			gameScene.getChildren().add(background);
			gameScene.getChildren().add(canvas);
		}
		initializegameOverAnimation();
		initializeFloors();
		initializePipesEgg();
        initializePigs();
        initializeBird();
        
        startTime = System.nanoTime();
        timer = new MyTimer();
        timer.start();
       
        if (gameOverAnimation != null) {
            gameOverAnimation.setVisible(false);
            gameScene.getChildren().remove(gameOverAnimation);
        }
        
		currentLives = DEF.MAX_LIFE;
		if (livesText != null) {
            // Remove the previous Text node
            gameScene.getChildren().remove(livesText);
        }
		livesText = new Text("Lives Left: 3");
		livesText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 19));
		livesText.setFill(Color.BROWN);
		livesText.setX(245);
		livesText.setY(560);
		gameScene.getChildren().add(livesText);
		
        currentScores = 0;
        if (livesText != null) {
            // Remove the previous Text node
            gameScene.getChildren().remove(scoreText);
        }
     // Create a Text object to display the score
        scoreText = new Text("Score: 0");
 		scoreText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 19));
 		scoreText.setFill(Color.WHITE);
 		scoreText.setX(8);
 		scoreText.setY(20);
       
        // Add Text node to the gameScenex
        gameScene.getChildren().add(scoreText);
	} 
		
		private void initializeFloors() {
			// initialize floor
			for(int i=0; i<DEF.FLOOR_COUNT; i++) {
				int posX = i * DEF.FLOOR_WIDTH;
				int posY = DEF.SCENE_HEIGHT - DEF.FLOOR_HEIGHT;
				Sprite floor = new Sprite(posX, posY, DEF.IMAGE.get("floor1"));
				floor.setVelocity(SCENE_SHIFT_INCR, 0);
				floor.render(gc);
				floors.add(floor);
			}
		}
		
		private void  initializePipesEgg() {
			//initialize Pipes n eggs
			Random rand = new Random();
			for (int i = 0; i < DEF.PIPE_COUNT; i++) {
				pairNumber=1;
				//unfinished x-calculation
			    double initialX = i*DEF.PIPE_X_GAP + DEF.SCENE_WIDTH;
				double randomOffset = rand.nextDouble() * DEF.PIPE_RANGE;
				// Limit the randomOffset so that D_PIPE_POS_Y never goes higher than 0
				randomOffset = Math.min(randomOffset, Math.abs(DEF.D_PIPE_POS_Y));
				initialY = DEF.D_PIPE_POS_Y - randomOffset;
				dPipe = new Pipe(initialX, initialY, DEF.IMAGE.get("dpipe2"));
				dPipe.setVelocity(SCENE_SHIFT_INCR, 0);
				System.out.println("SS dPipe incr: "+SCENE_SHIFT_INCR);
				dPipe.render(gc);
				dPipes.add(dPipe);
				//somehow bringing white egg up here work, but down doesnt
				whiteEgg = new Sprite(DEF.D_PIPE_POS_X-13, uPipeInitialY-60, DEF.IMAGE.get("white_egg"));
				goldEgg = new Sprite(-100, -100, DEF.IMAGE.get("golden_egg"));
				
				uPipeInitialY = initialY + DEF.PIPE_Y_GAP;
				uPipe = new Pipe(initialX, uPipeInitialY, DEF.IMAGE.get("upipe1"));
				uPipe.setVelocity(SCENE_SHIFT_INCR, 0);
				System.out.println("SS uPipe incr: "+SCENE_SHIFT_INCR);
				uPipe.render(gc);
				uPipes.add(uPipe);
			}
		}
		
		//initialize Bird
		private void initializeBird() {
			// initialize blob
			blob = new Blob(DEF.BLOB_POS_X, DEF.BLOB_POS_Y, DEF.IMAGE.get("bird1"));
			blob.setImageName("bird1");
			blob.render(gc);
		}	
		
		//initialize Pigs
		private void initializePigs() {
			double pigStart = initialY+DEF.PIG_POS_START;
			pig = new Sprite(570, 570, DEF.IMAGE.get("monster_thief"));
			//System.out.println("tHE PIG: " + pig);
			pig.setVelocity(0, 0);
			pig.render(gc);
		}		
		
		private void initializegameOverAnimation() {
	        if (gameOverAnimation == null) {
	            gameOverAnimation = new ImageView(DEF.IMAGE.get("game_over_sprite"));
	            gameOverAnimation.setFitWidth(200);
	            gameOverAnimation.setFitHeight(100);

	            // Set the position of the image
	            gameOverAnimation.setTranslateX(DEF.SCENE_WIDTH / 2 - 100);
	            gameOverAnimation.setTranslateY(DEF.SCENE_HEIGHT / 2);

	            // Make the image invisible initially
	            gameOverAnimation.setVisible(false);
	            gameScene.getChildren().add(gameOverAnimation);
	        }
	    }

//timer stuff
class MyTimer extends AnimationTimer {
	private Timeline backgroundSwitchTimeline;
	 
	int counter = 0;
	int bgr_counter = 0;
	public void handle(long now) { 	

		 bgr_counter++;
		 gold_egg_counter++;
		 // time keeping
	 elapsedTime = now - startTime;
	 startTime = now;
	
	 // clear current scene
	 gc.clearRect(0, 0, DEF.SCENE_WIDTH, DEF.SCENE_HEIGHT);
	 if (GAME_START) {
	 	
	 	 //step1: update background
	 	 if (bgr_counter>450) {
// 	 		 System.out.println("The bgr counter is : " + bgr_counter);
	 		 bgr_counter=0;
	 		 isNightBackground= !isNightBackground;
	 	 }
	 		 displayBackground();
	 	
	 	if (isAutoPilot && gold_egg_counter % 1000 == 0) {
	 		isAutoPilot = false;
	 		System.out.println("not autopilot");
	 		gold_egg_counter = 0;
	 	}
	 	
	 	updateScore();
	 	//updateLives();
	 	 //step2: update floor
	 	 moveFloor();
	 	
	 	 //step3: movePipe
	 	 movePipe();
	 	 
	 	//step5: check for collision
         if(!isAutoPilot) {
         checkCollision();
         }
	 	
	 	 // step4: update blob
	 	 moveBlob();
	 	
	 	 //setp5: update egg and pig
	 	 moveEgg();
	 	}
	 }
	
	 private void displayBackground() {
		 if(isNightBackground) {
			 gc.drawImage (DEF.IMAGE.get("night_background"), 0, 0);
		 }
		 else if(!isNightBackground) {
			 gc.drawImage (DEF.IMAGE.get("day_background"), 0, 0);
		 }
	 }
	 
	 // step1: update floor
	 private void moveFloor() {
		for(int i=0; i<DEF.FLOOR_COUNT; i++) {
			if (floors.get(i).getPositionX() <= -DEF.FLOOR_WIDTH) {
				double nextX = floors.get((i+1)%DEF.FLOOR_COUNT).getPositionX() + DEF.FLOOR_WIDTH;
			 	double nextY = DEF.SCENE_HEIGHT - DEF.FLOOR_HEIGHT;
			 	floors.get(i).setPositionXY(nextX, nextY);
			}
			floors.get(i).render(gc);
			//System.out.println(SCENE_SHIFT_TIME);
			floors.get(i).update(SCENE_SHIFT_TIME);
		}
	 }
	 
	 //function to check when the blob is between the pipes
	 /**
	 * @params None
	 * movePipe is responsible for animating the upward and downward pipes
	 */
	 Random rand1 = new Random();
	 public void movePipe() {
//	     currentScores=0;
	 	for (int i = 0; i < DEF.PIPE_COUNT; i++) {
	 		 if (dPipes.get(i).getPositionX() <= -DEF.D_PIPE_WIDTH) {
	 			 
		 		 nextX_down = dPipes.get((i+1)%DEF.PIPE_COUNT).getPositionX()+DEF.PIPE_X_GAP;
		 		 double randomOffset = rand1.nextDouble() * DEF.PIPE_RANGE;
		 		 // Limit the randomOffset so that D_PIPE_POS_Y never goes higher than 0
		 		 randomOffset = Math.min(randomOffset, Math.abs(DEF.D_PIPE_POS_Y));
		 		 nextY_down = DEF.D_PIPE_POS_Y - randomOffset;
		 		 dPipes.get(i).setPositionXY(nextX_down, nextY_down);
		 		 nextY_up = nextY_down + DEF.PIPE_Y_GAP;
		 		 uPipes.get(i).setPositionXY(nextX_down, nextY_up);
	 		 }
				 dPipes.get(i).render(gc);
				 dPipes.get(i).update(SCENE_SHIFT_TIME);
				 System.out.println("movePipe SST dPipe: "+ SCENE_SHIFT_TIME);
				 uPipes.get(i).render(gc);
				 System.out.println("movePipe SST uPipe: "+ SCENE_SHIFT_TIME);
				 uPipes.get(i).update(SCENE_SHIFT_TIME);
	 	}
	 }
	 /**
	  * for updating score
	  */
	 public void updateScore() {
	     for (int i = 0; i < DEF.PIPE_COUNT; i++) {
	         if (dPipes.get(i).getPositionX()== -(dPipes.get(i).getPositionX())){
                 currentScores++;
                 updateScoreText();
                    System.out.println("currentscore:" + currentScores);
	         }
	     }
	         if(pig.getPositionY()== goldEgg.getPositionY()-80) {
               currentScores -= 5;
               updateScoreText();
               if (currentScores<0) {
                   currentScores=0;
                   updateScoreText();
               }
               System.out.println("-goldegg:" + currentScores);
               }
	         else if (pig.getPositionY()== whiteEgg.getPositionY()-80) {
             System.out.println("the pig stole the white egg");
               currentScores -= 5;
               updateScoreText();
               if (currentScores<0) {
                   currentScores=0;
                   updateScoreText();
                
               }
               System.out.println("-whiteEgg:" + currentScores);
               
           }
	         else if ((blob.intersectsSprite(whiteEgg))) {
               blob.setCollisionSound(DEF.AUDIO.get("collect_coin_1"));
               blob.playCollisionSound();
               currentScores += 5;
               updateScoreText();
               System.out.println("+whiteEgg:" + currentScores);
           }
	 }
	 
	 public void updateLives() {
	     for (int i = 0; i < DEF.PIPE_COUNT; i++) {
             if (dPipes.get(i).getPositionX()== blob.getPositionX()){
                if( blob.intersectsPipe(dPipe)){
                 currentLives--;
                 updateLivesText();
                 System.out.println("lives AFTER uPIPE:" + currentLives);
                }
             }
	     }
	 }
             
	 public void moveEgg() {
		 boolean showWhite=false;
		 boolean showGold=false;
		 boolean showPig=false;
		 Random rand = new Random();
		 for (int i = 0; i < DEF.PIPE_COUNT-1; i++) {
			 if (dPipes.get(i).getPositionX() <= -DEF.D_PIPE_WIDTH) {
		 		 nextX_down = dPipes.get((i+1)%DEF.PIPE_COUNT).getPositionX()+DEF.PIPE_X_GAP;
		 		 double randomOffset = rand1.nextDouble() * DEF.PIPE_RANGE;
		 		 // Limit the randomOffset so that D_PIPE_POS_Y never goes higher than 0
		 		 randomOffset = Math.min(randomOffset, Math.abs(DEF.D_PIPE_POS_Y));
		 		 nextY_down = DEF.D_PIPE_POS_Y - randomOffset;
		 		 dPipes.get(i).setPositionXY(nextX_down, nextY_down);
		 		
		 		 nextY_up = nextY_down + DEF.PIPE_Y_GAP;
		 		 uPipes.get(i).setPositionXY(nextX_down, nextY_up);
		 		 
		 		 double randWhite=rand.nextDouble();
		 		 double randGold=rand.nextDouble();
		 		 double randPig =rand.nextDouble();
		 		 
		 		if (randWhite > 0.5 & randGold>=0.3) {
		 			showWhite=true;
		 		    whiteEgg.setPositionXY(nextX_down-10, nextY_up-60);
		 		}
		 		if (randGold < 0.3) {
		 			showGold=true;
		 		    goldEgg.setPositionXY(nextX_down-10, nextY_up-60);
		 		}
		 		if(randPig>.5 && randPig<.8)
		 		    showPig=true;
		 		pig.setPositionXY(nextX_down-9, nextY_down+320);

			 }
 
		     }
		// Render and update the gold egg only if it's positioned
		 if(!showGold) {
			 goldEgg.setVelocity(SCENE_SHIFT_INCR, 0);
	         goldEgg.render(gc);
	         goldEgg.update(SCENE_SHIFT_TIME);
	         System.out.println("moveEgg SST goldEgg: "+ SCENE_SHIFT_TIME);
		 }
		 if(!showWhite) {
			 whiteEgg.setVelocity(SCENE_SHIFT_INCR, 0);
	         whiteEgg.render(gc);
	         whiteEgg.update(SCENE_SHIFT_TIME);
	         System.out.println("moveEgg SST goldEgg: "+ SCENE_SHIFT_TIME);
		 }
		 if(!showPig) {
		     pig.setVelocity(SCENE_SHIFT_INCR, DEF.PIG_DROP_Y_VEL);
	         pig.render(gc);
	         pig.update(SCENE_SHIFT_TIME);
	         System.out.println("moveEgg SST pig: "+ SCENE_SHIFT_TIME);
         }
	 }
	 
	 // step2: update blob
	 private void moveBlob() {
		 	blob.setCollisionSound(DEF.AUDIO.get("bird_flapping_1"));
		 	blob.playCollisionSound();
			long diffTime = System.nanoTime() - clickTime;
			
			// blob flies upward with animation
			if (CLICKED && diffTime <= BLOB_DROP_TIME) {
				
				int imageIndex = Math.floorDiv(counter++, DEF.BLOB_IMG_PERIOD);
				imageIndex = Math.floorMod(imageIndex, DEF.BLOB_IMG_LEN);
				blob.setImage(DEF.IMAGE.get("bird"+String.valueOf(imageIndex+1)));
				blob.setImageName("bird"+String.valueOf(imageIndex+1));
				blob.setVelocity(0, BLOB_FLY_VEL);
			} 
			
			//check if we are on autoPilot mode
			else if (isAutoPilot){
				 //Will set the vals as variables once fully tested that it works
			blob.setPositionXY(200, 250);
			 blob.setVelocity(0, 0);
			 blob.setImage(DEF.IMAGE.get("bird_with_parachute"));
			 blob.setImageName("bird_with_parachute");
			}
			
			//check if the previous state of the bird was autopilot so we can drop the blob
			else if (!isAutoPilot && blob.blobGetName().equals("bird_with_parachute")) {
				 blob.setImage(DEF.IMAGE.get("bird1"));
				 //System.out.println("The bird is in position y: "+blob.getPositionY());
				 
				 //Will set the vals as variables once fully tested that it works
				 blob.setVelocity(0, 100);
				 CLICKED = false;
				 pig.setVelocity(-0.6, 0.1);
				 //this is to prevent unnecessary collision when we get out of autopilot mode.
				 pig.setPositionXY(600, 600);
			}
			else {
			 blob.setVelocity(0, BLOB_DROP_VEL);
			 CLICKED = false;
			}
			// render blob on GUI
			blob.update(elapsedTime * DEF.NANOSEC_TO_SEC);
			blob.render(gc);
	 }

	// Helper method to reset the position of the bird
	 //should be fixed, the bird should be where?
	 private void resetBirdPosition() {
	     blob.setPositionXY(DEF.BLOB_POS_X, DEF.BLOB_POS_Y);
	     blob.setVelocity(0, 0);
	 }
	 
	 public void checkCollision() {
		
		 boolean hitAPig = false;
		 boolean hitAUpipe = false;
		 
		 // check collision with the floor
		checkCollision_floor();
		
		 //check blob collision with the upipes
		// checkCollision_blob_upipes();
		    
		 //check for blob intersection with pig
		// checkCollision_blob_pig();
		 
		 //check for blob intersection with dpipes
		 
		checkCollision_blob_dpipes();
		 
		 //check collision pig with pipe
		 checkCollision_pig_pipe();

		 //check collision pig with egg
		 checkCollision_pig_white_egg();	
		 
		 //check for collision with gold egg
		 checkCollision_pig_gold_egg();
		 
		 //check for pig intersecting the egg
		 checkCollision_blob_white_egg();
		 
		 //check collision blob gold egg
		 // checkCollision_blob_gold_egg();
		 
		// Set the game_over to true if no lives remaining
		 if (currentLives <= 0) {
		     GAME_OVER = true;
		 }
		    
		 // end the game when blob hit stuff
		 if (GAME_OVER) {
			 System.out.println("the game is over");
			 //showHitEffect();
			 blob.stopCollisionSound();
			 for (Sprite floor: floors) {
			 floor.setVelocity(0, 0);
			 timer.stop();
			 //DO NOT DELETE COMMENT BELOW, IT HAS A FUNCTION WE MIGHT WANT LATER
			 //resetGameScene(false);
			 } 
			// Add the image to the game scene
             gameScene.getChildren().add(gameOverAnimation);
             initializegameOverAnimation();
		 }
	 }
	 
	 private void checkCollision_pig_gold_egg() {
		 if(pig.intersectsSprite(goldEgg)) {
//			 currentScores -= 5;
			 System.out.println("goldEgg:" + currentScores);
			 pig.setPositionXY(-100, -100);
			 goldEgg.setPositionXY(-100, -100);
//			 updateScoreText();
		 }
	}
	private void checkCollision_floor() {
		 for (Sprite floor: floors) {
		 GAME_OVER = GAME_OVER || blob.intersectsSprite(floor);
			 if (GAME_OVER) {
				 gameOverAnimation.setVisible(true);
			 }
		 }
	 }
	 
	 private void checkCollision_blob_dpipes() {
		 
//		 if(!collisionDetected) {
		 for (Pipe dPipe: dPipes) {

			 if (blob.intersectsPipe(dPipe)) {
				 blob.setCollisionSound(DEF.AUDIO.get("obstacle_hit_1"));
				 blob.playCollisionSound();
//				System.out.println("Hit uPipe");
				System.out.println("lives BEFORE dPIPE:" + currentLives);
//			 	currentLives--;
//			 	 
//			 	updateLivesText();
//			 	System.out.println("lives AFTER uPIPE:" + currentLives);
	            //resetBirdPosition();
//	            collisionDetected = true;
//	            break;
			 }
			 
		 }
//		 } else {
//			 collisionDetected = false;
//		 }
		
	 }
	 
	/**
	 * checkCollision blob with up pipes
	 */
	private void checkCollision_blob_upipes() {
	
			 if(!collisionDetected) {
			 for (Pipe dPipe: dPipes) {
				 if (blob.intersectsPipe(uPipe)) {
					 blob.setCollisionSound(DEF.AUDIO.get("obstacle_hit_1"));
					 blob.playCollisionSound();
					System.out.println("lives BEFORE dPIPE:" + currentLives);
				 	currentLives--;
				 	updateLivesText();
		           // resetBirdPosition();
		            collisionDetected = true;
		            break;
				 } 
			 }
			} else {
				 collisionDetected = false;
			 }
		 }
	 
	/**
	 * checkCollision blob with pig
	 */
	 private void checkCollision_blob_pig() {
		    if (blob.intersectsSprite(pig)) {
	    	System.out.println("the blob has intersected the pig");
	    	GAME_OVER = true;
	    	showHitEffect();
	    }
	 }
	 
	 /**
	  * CheckCollision pig with pipe to set it off screen
	  */
	 private void checkCollision_pig_pipe() {
		 for (Pipe uPipe: uPipes) {
			 if(pig.intersectsPipe(uPipe)) {
				 pig.setPositionXY(-100, -100);
			 }
		 }
	 }
	 
	 /**
	  * checkCollision with white egg to decrease the score
	  */
	 private void checkCollision_pig_white_egg() {
	    	if (pig.intersectsSprite(whiteEgg)) {
				pig.setPositionXY(-100, -100);
				whiteEgg.setPositionXY(-100, -100);
				pigHitsEgg = true;
		    } 
	    	else  {
		    	pigHitsEgg = false;
		    }
	 }
	 
	private void checkCollision_blob_white_egg() {
		if (blob.intersectsSprite(whiteEgg)) {
			blob.setCollisionSound(DEF.AUDIO.get("collect_coin_1"));
			blob.playCollisionSound();
			whiteEgg.setPositionXY(-100, -100);
			blobHitsEgg = true;
	    } else  {
	    	blobHitsEgg = false;
	    }
	}
	
	private void checkCollision_blob_gold_egg() {
		if (blob.intersectsSprite(goldEgg)) {
		    goldEgg.setPositionXY(-100, -100);
			blob.setImage(DEF.IMAGE.get("bird_with_parachute"));
			blob.setVelocity(0, 0);
			isAutoPilot = true;
			CLICKED= false;
			DEF.startButton.setDisable(CLICKED);
			
		}
	}

	private void showHitEffect() {
		 ParallelTransition parallelTransition = new ParallelTransition();
		 FadeTransition fadeTransition = new FadeTransition(Duration.seconds(DEF.TRANSITION_TIME), gameScene);
		 fadeTransition.setToValue(0);
		 fadeTransition.setCycleCount(DEF.TRANSITION_CYCLE);
		 fadeTransition.setAutoReverse(true);
		 parallelTransition.getChildren().add(fadeTransition);
		 parallelTransition.play();
	 }
	
	 private void updateLivesText() {
	 	 if (currentLives > 0) {
	 		 livesText.setText("Lives Left: " + currentLives);
	 		System.out.println("You have " + currentLives + " lives left");
	 	 } else {
	 		 livesText.setText("Lives Left: 0");
	 		gameOverAnimation.setVisible(true);
	 	 }
	 }
		
	 private void updateScoreText() {
		
			scoreText.setText("Score: " + currentScores);
	
	 }
	 
	    public void randomBounceAngle(){
	        double ran = Math.random();
	        if(ran >= .50){
	            //shallow bounce angle
	            xvariation = 3;
	            yvariation = 2;

	        }else{
	            //sharp bounce angle
	            xvariation = 2;
	            yvariation = 3;
	        }
	    }
	    
	    public double getRate(){
	        return rate;
	    }
	  
 // End of MyTimer class
}}// End of AngryFlappyBird Class

