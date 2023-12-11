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

//The Application layer
public class AngryFlappyBird extends Application {
	private double movex = 0;
    private double movey = 0;
    private double xvariation = 0;
    private double yvariation = 0;
    private double rate = 1;

    private boolean right = true;
    private boolean up = false;
	private boolean isAutoPilotMode = false;
	private boolean collisionDetected = false;
	private int SCENE_SHIFT_TIME = 0;
	private int SCENE_SHIFT = 0;

	private int gold_egg_counter = 0;
	private boolean isInitGame = false;
    private MediaPlayer mediaPlayer;
	private Defines DEF = new Defines();
	private boolean isCollided = false;
	//text display
	private int currentLives;
	private Text livesText;
	private Text gameOverText;
	private int currentScores;
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
	private int currentScore;
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
		// the mandatory main method
	public static void main(String[] args) {
		launch(args);
		}
		// the start method sets the Stage layer
		@Override
	public void start(Stage primaryStage) throws Exception {
			

			
	
		// initialize scene graphs and UIs
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
		
		//adding a control panel
		
		ObservableList<String> options = 
			    FXCollections.observableArrayList(
			        "Easy",
			        "Medium",
			        "Hard"
			    );
		 comboBox = new ComboBox<String>(options);

//		comboBox.getItems().addAll(
//			    "Easy",
//			    "Medium",
//			    "Hard"
//			);
		gameLevel = new VBox(5);
		gameLevel.getChildren().addAll(DEF.easyButton, DEF.mediumButton, DEF.hardButton, comboBox);
		gameControl.getChildren().addAll(DEF.startButton, gameLevel, gameDesc);
		

		}
	private void mouseClickHandler(MouseEvent e) {
		if (GAME_OVER) {
			resetGameScene(false);
	}
		else if (GAME_START){
			clickTime = System.nanoTime();
			//resetGameScene(true);
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
		
        currentScores = 1;
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
        
        
        
        gameOverText = new Text("Game Over, click to play again");
		gameOverText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));
		gameOverText.setFill(Color.RED);
		gameOverText.setX(DEF.SCENE_WIDTH / 2 - 150);
		gameOverText.setY(DEF.SCENE_HEIGHT / 2);
		// Set the initial visibility of gameOverText to false
		gameScene.getChildren().add(gameOverText);
		gameOverText.setVisible(false);
		
		if (gameOverText.isVisible()) {
            // Remove the previous Text node
			//gameOverText.setVisible(false);
            gameScene.getChildren().remove(gameOverText);
            //gameOverText.setVisible(false);
            
        }
		
		
		// initialize floor
		for(int i=0; i<DEF.FLOOR_COUNT; i++) {
			int posX = i * DEF.FLOOR_WIDTH;
			int posY = DEF.SCENE_HEIGHT - DEF.FLOOR_HEIGHT;
			Sprite floor = new Sprite(posX, posY, DEF.IMAGE.get("floor1"));
			floor.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
			floor.render(gc);
			floors.add(floor);
		}
		//initialize Pipes n eggs
		Random rand = new Random();
		for (int i = 0; i < DEF.PIPE_COUNT; i++) {
			pairNumber=1;
			//unfinished x-calculation
		    double initialX = i*DEF.PIPE_X_GAP + DEF.SCENE_WIDTH;
			//temporary x-value
			//double initialX =DEF.D_PIPE_POS_X;
			//Random value in the range [-PIPE_GAP, PIPE_GAP]
			double randomOffset = rand.nextDouble() * DEF.PIPE_RANGE;
			// Limit the randomOffset so that D_PIPE_POS_Y never goes higher than 0
			randomOffset = Math.min(randomOffset, Math.abs(DEF.D_PIPE_POS_Y));
			initialY = DEF.D_PIPE_POS_Y - randomOffset;
			dPipe = new Pipe(initialX, initialY, DEF.IMAGE.get("dpipe2"));
			dPipe.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
			dPipe.render(gc);
			dPipes.add(dPipe);
			//somehow bringing white egg up here work, but down doesnt
			whiteEgg = new Sprite(DEF.D_PIPE_POS_X-13, uPipeInitialY-60, DEF.IMAGE.get("white_egg"));
			goldEgg = new Sprite(DEF.D_PIPE_POS_X+DEF.PIPE_X_GAP-8, initialY + DEF.PIPE_Y_GAP-DEF.GOLD_EGG_HEIGHT+15, DEF.IMAGE.get("golden_egg"));
			
//			whiteEgg = new Sprite(0, 0, DEF.IMAGE.get("white_egg"));
//			goldEgg = new Sprite(0, 0, DEF.IMAGE.get("golden_egg"));
			
			// Calculate the initial Y forU uPipe based on the corresponding dPipe
			uPipeInitialY = initialY + DEF.PIPE_Y_GAP;
			
			uPipe = new Pipe(initialX, uPipeInitialY, DEF.IMAGE.get("upipe1"));
			
			uPipe.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
			uPipe.render(gc);
			uPipes.add(uPipe);
			
		}
		
		//for rendering the eggs on the pipe
		
		//whiteEgg.render(gc);
		// initialize blob
		blob = new Blob(DEF.BLOB_POS_X, DEF.BLOB_POS_Y, DEF.IMAGE.get("bird1"));
//		blob.setCollisionSound(DEF.AUDIO.get("obstacle_hit_1"));
		blob.render(gc);
		
		double pigStart = initialY+DEF.PIG_POS_START;
		pig = new Sprite(570, 570, DEF.IMAGE.get("monster_thief"));
		//System.out.println("tHE PIG: " + pig);
		pig.setVelocity(0, 0);
		pig.render(gc);
		
		// initialize timer
		startTime = System.nanoTime();
		timer = new MyTimer();
		timer.start();
	}
	
//timer stuff
class MyTimer extends AnimationTimer {
	//do not delete the lines commented out below, it might be helpful
//	private long randomSeed = System.currentTimeMillis();
//	private Random rand = new Random(randomSeed);
//	 rand.setSeed(randomSeed);
	private Timeline backgroundSwitchTimeline;
	 
	int counter = 0;
	int bgr_counter = 0;
	public void handle(long now) { 	
//		System.out.println("the timer is invoked");

		 bgr_counter++;
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
	 	
	 	 //step2: update floor
	 	 moveFloor();
	 	
	 	 //step3: movePipe
	 	 movePipe();
	 	
	 	 // step4: update blob
	 	 moveBlob();
	 	
	 	 //setp5: update egg
	 	 moveEgg();
	 	 
	 	 //step5: update pig
//	 	 movePig();
	 	
//	 	 moveEggandPig();
	 	 //step5: check for collision
	 	 checkCollision();
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
			floors.get(i).update(DEF.EASY_SCENE_SHIFT_TIME);
		}
	 }
	 
	 //function to check when the blob is between the pipes
	 /**
	 * @params None
	 * movePipe is responsible for animating the upward and downward pipes
	 */
	 Random rand1 = new Random();
	 public void movePipe() {

	 	for (int i = 0; i < DEF.PIPE_COUNT; i++) {
	 		 if (dPipes.get(i).getPositionX() <= -DEF.D_PIPE_WIDTH) {
	 			 //currentScores++;
	 			 //updateScoreText();
	 			 
		 		 nextX_down = dPipes.get((i+1)%DEF.PIPE_COUNT).getPositionX()+DEF.PIPE_X_GAP;
		 		 double randomOffset = rand1.nextDouble() * DEF.PIPE_RANGE;
		 		 // Limit the randomOffset so that D_PIPE_POS_Y never goes higher than 0
		 		 randomOffset = Math.min(randomOffset, Math.abs(DEF.D_PIPE_POS_Y));
		 		 nextY_down = DEF.D_PIPE_POS_Y - randomOffset;
		 		 dPipes.get(i).setPositionXY(nextX_down, nextY_down);
		 		
		 		 //nextX_up = uPipes.get((i+1)%DEF.PIPE_COUNT).getPositionX();
		 		 nextY_up = nextY_down + DEF.PIPE_Y_GAP;
		 		 uPipes.get(i).setPositionXY(nextX_down, nextY_up);
		 		 
		 		 //Check if bird passes thru
		 		 //boolean isScored=false;
		 		 if (!blob.intersectsPipe(dPipes.get(i))){
		 			 currentScores++;
		 			 updateScoreText();
		 			 //isScored=true;
		 			System.out.println("index "+ i);
		 			 System.out.println("Gain 1 score for this "+ i + "dPipe");
		 		 }
		 		
		 		 
	 		 }
	 		 
				 dPipes.get(i).render(gc);
				 dPipes.get(i).update(DEF.EASY_SCENE_SHIFT_TIME);
				
				 uPipes.get(i).render(gc);
				 uPipes.get(i).update(DEF.EASY_SCENE_SHIFT_TIME);
	 	}
	 }
	 
	 /**
	  * This method is responsible for adding animation to the pig
	  */
	 public void movePig() {
		 for (int i = 0; i < DEF.PIPE_COUNT; i++) {
			 if (dPipes.get(i).getPositionX() <= -DEF.D_PIPE_WIDTH) {
				 nextX_down = dPipes.get((i+1)%DEF.PIPE_COUNT).getPositionX()+DEF.PIPE_X_GAP;
				 double randomOffset = rand1.nextDouble() * DEF.PIPE_RANGE;
				 // Limit the randomOffset so that D_PIPE_POS_Y never goes higher than 0
				 randomOffset = Math.min(randomOffset, Math.abs(DEF.D_PIPE_POS_Y));
				 nextY_down = DEF.D_PIPE_POS_Y - randomOffset;
				 dPipes.get(i).setPositionXY(nextX_down, nextY_down);
		 		 //System.out.println("Pig is moving " + nextY_down);
		 		 
		 		//nextX_up = uPipes.get((i+1)%DEF.PIPE_COUNT).getPositionX();
		 		 nextY_up = nextY_down + DEF.PIPE_Y_GAP;
		 		 uPipes.get(i).setPositionXY(nextX_down, nextY_up);
		 		
//				 UTILIZE THE HEIGHT OF THE PIG TO SET IT DOWN
				 pig.setPositionXY(nextX_down-9, nextY_down+DEF.D_PIPE_HEIGHT);
				 showEgg = true;

			 }

		 }
		
			 pig.setVelocity(DEF.SCENE_SHIFT_INCR, 0.1);
			 pig.render(gc);
			 pig.update(DEF.EASY_SCENE_SHIFT_TIME);
			//pig.render(gc);
		 
	 }
	 
	 public void moveEgg() {
		 boolean showWhite=false;
		 boolean showGold=false;
		 Random rand = new Random();
		 for (int i = 0; i < DEF.PIPE_COUNT-1; i++) {
			 if (dPipes.get(i).getPositionX() <= -DEF.D_PIPE_WIDTH) {
	 			 //currentScores++;
	 			 //updateScoreText();
	 			 
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

		 		 //Randomize whether eggs show up
		 		//if white show up and gold wont showup
		 		if (randWhite > 0.3 & randGold>=0.2) {
		 			//System.out.println("WHITE SHOW " );
		 			showWhite=true;
		 		    whiteEgg.setPositionXY(nextX_down-10, nextY_up-60);
		 		}
		 		if (randGold < 0.2) {
		 			//System.out.println("GOLD SHOW " );
		 			showGold=true;
		 		    goldEgg.setPositionXY(nextX_down-10, nextY_up-60);
		 		}
		 		
		 		pig.setPositionXY(nextX_down-9, nextY_down+385);

			 }

		     }
		// Render and update the gold egg only if it's positioned
		 if(!showGold) {
			 goldEgg.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	         goldEgg.render(gc);
	         goldEgg.update(DEF.EASY_SCENE_SHIFT_TIME);
		 }
		 
		 if(!showWhite) {
			 whiteEgg.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	         whiteEgg.render(gc);
	         whiteEgg.update(DEF.EASY_SCENE_SHIFT_TIME);
		 }
		 
		 pig.setVelocity(DEF.SCENE_SHIFT_INCR, 0.1);
		 pig.render(gc);
		 pig.update(DEF.EASY_SCENE_SHIFT_TIME);
	 }
	 
	 
	 // step2: update blob
	 private void moveBlob() {
		 	blob.setCollisionSound(DEF.AUDIO.get("bird_flapping_1"));
		 	blob.playCollisionSound();
			long diffTime = System.nanoTime() - clickTime;
			
			// blob flies upward with animation
			if (CLICKED && diffTime <= DEF.BLOB_DROP_TIME) {
				
				int imageIndex = Math.floorDiv(counter++, DEF.BLOB_IMG_PERIOD);
				imageIndex = Math.floorMod(imageIndex, DEF.BLOB_IMG_LEN);
				blob.setImage(DEF.IMAGE.get("bird"+String.valueOf(imageIndex+1)));
				blob.setVelocity(0, DEF.BLOB_FLY_VEL);
			}
			// blob drops after a period of time without button click
			else {
				
				//check the image when it's landing
				//if the wing is upward, flip it to a downward flapping wing
			 blob.setVelocity(0, DEF.BLOB_DROP_VEL);
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
//		 if (isAutoPilotMode == false) {
			 
		 
		 boolean hitAPig = false;
		 boolean hitAUpipe = false;
		 
		 // check collision with the floor
		 checkCollision_floor();
		
		 //check blob collision with the pipes
		 checkCollision_pipes();
		    
		 //check for blob intersection with pig
//		 checkCollision_blob_pig();
		 
		 //check for blob intersection with dpipes
		 checkCollision_blob_dpipes();
		 
		 //check collision pig with pipe
		 checkCollision_pig_pipe();

		 //check collision pig with egg
//		 checkCollision_pig_white_egg();	
		 
		 //check for collision with gold egg
//		 checkCollision_pig_gold_egg();
		 
		 //check for pig intersecting the egg
		 checkCollision_blob_white_egg();
		 
		 //check collision blob gold egg
		 checkCollision_blob_gold_egg();
		 
		// Set the game_over to true if no lives remaining
		 if (currentLives <= 0) {
			 
		     GAME_OVER = true;
		 }
		    
		 // end the game when blob hit stuff
		 if (GAME_OVER) {
			 System.out.println("the game is over");
			 gameOverText.setVisible(true);
			 showHitEffect();
			 blob.stopCollisionSound();
			 for (Sprite floor: floors) {
			 floor.setVelocity(0, 0);
			 timer.stop();
			 //DO NOT DELETE COMMENT BELOW, IT HAS A FUNCTION WE MIGHT WANT LATER
			 resetGameScene(false);
			 
			 } 
		 	}
//		 } 

	 }
	 
	 private void checkCollision_floor() {
		 for (Sprite floor: floors) {
		 GAME_OVER = GAME_OVER || blob.intersectsSprite(floor);
		 }
	 }
	 
	 private void checkCollision_pipes() {

		 for (Pipe uPipe: uPipes) {
				blob.applyBounceAnimation();

			 if (blob.intersectsPipe(uPipe)) {
				 blob.setCollisionSound(DEF.AUDIO.get("obstacle_hit_2"));
				 blob.playCollisionSound();
//				System.out.println("Hit uPipe");
				System.out.println("lives BEFORE uPIPE:" + currentLives);
			 	currentLives--;
//			 	 System.out.println("lives AFTER uPIPE:" + currentLives);
			 	// if (currentLives>=0){
			 	updateLivesText();
			 	 //}
//			 	GAME_OVER = GAME_OVER || blob.intersectsPipe(uPipe);
			 // Reset the position of the bird after collision with pipes
	            resetBirdPosition();
			 }
			 
		 }
		 
		 for (Pipe uPipe: dPipes) {
				blob.applyBounceAnimation();

			 if (blob.intersectsPipe(dPipe)) {
				 blob.setCollisionSound(DEF.AUDIO.get("obstacle_hit_2"));
				 blob.playCollisionSound();
//				System.out.println("Hit uPipe");
				System.out.println("lives BEFORE uPIPE:" + currentLives);
			 	currentLives--;
//			 	 System.out.println("lives AFTER uPIPE:" + currentLives);
			 	// if (currentLives>=0){
			 	updateLivesText();
			 	 //}
//			 	GAME_OVER = GAME_OVER || blob.intersectsPipe(uPipe);
			 // Reset the position of the bird after collision with pipes
	            resetBirdPosition();
			 }
			 
		 }
		 
		 
		 //_JUSTCOMMENT OUT TO TRY NEW TRICK
			 // check if the bird goes through a pair of pipes without collision
//		    if (!GAME_OVER && blob.getPositionX() > uPipes.get(0).getPositionX() + DEF.D_PIPE_WIDTH) {
//		        int currentPassedPipeIndex = (int) (blob.getPositionX() / (DEF.D_PIPE_WIDTH + DEF.PIPE_X_GAP));
//		        //System.out.println("currentPassedPipeIndex:"+currentPassedPipeIndex);
//		        if (currentPassedPipeIndex > lastPassedPipeIndex) {
//		            // The bird has passed through a new set of pipes
//		            currentScores += pairNumber; // Set the score equal to the pair number
//		            lastPassedPipeIndex = currentPassedPipeIndex;
//
//		            // Update the score text on the screen
//		            updateScoreText();
//		        }
//		 }
//		    
		    
		    

	 }
	 
	 private void checkCollision_blob_dpipes() {
		 

		 if(!collisionDetected) {
		 for (Pipe dPipe: dPipes) {

			 if (blob.intersectsPipe(dPipe)) {
				 blob.setCollisionSound(DEF.AUDIO.get("obstacle_hit_1"));
				 blob.playCollisionSound();
//				System.out.println("Hit uPipe");
				System.out.println("lives BEFORE dPIPE:" + currentLives);
			 	currentLives--;
//			 	 System.out.println("lives AFTER uPIPE:" + currentLives);
			 	// if (currentLives>=0){
			 	updateLivesText();
			 	 //}
//			 	GAME_OVER = GAME_OVER || blob.intersectsPipe(uPipe);
			 // Reset the position of the bird after collision with pipes
	            resetBirdPosition();
	            collisionDetected = true;
	            break;
			 }
			 
		 }
		 } else {
			 collisionDetected = false;
		 }
	 }
//private void checkCollision_blob_upipes() {
//		 
//
//		 if(!collisionDetected) {
//		 for (Pipe dPipe: dPipes) {
//
//			 if (blob.intersectsPipe(uPipe)) {
//				 blob.setCollisionSound(DEF.AUDIO.get("obstacle_hit_1"));
//				 blob.playCollisionSound();
////				System.out.println("Hit uPipe");
//				System.out.println("lives BEFORE dPIPE:" + currentLives);
//			 	currentLives--;
////			 	 System.out.println("lives AFTER uPIPE:" + currentLives);
//			 	// if (currentLives>=0){
//			 	updateLivesText();
//			 	 //}
////			 	GAME_OVER = GAME_OVER || blob.intersectsPipe(uPipe);
//			 // Reset the position of the bird after collision with pipes
//	            resetBirdPosition();
//	            collisionDetected = true;
//	            break;
//			 }
//			 
//		 }
//		 } else {
//			 collisionDetected = false;
//		 }
//	 }
	 
	 private void checkCollision_blob_pig() {
		    if (blob.intersectsPig(pig)) {
	    	System.out.println("the blob has intersected the pig");
	    	GAME_OVER = true;
	    	showHitEffect();
	    }
	 }
	 
	 private void checkCollision_pig_pipe() {
		 for (Pipe uPipe: uPipes) {
			 if(pig.intersectsPipe(uPipe)) {
				 pig.setPositionXY(-100, -100);
			 }

		 }
	 }
	 
	 private void checkCollision_pig_white_egg() {
	    	if (pig.intersectsSprite(whiteEgg) && !pigHitsEgg) {
//		    	System.out.println("the pig stole the white egg");
		    	currentScores -= 5;
				updateScoreText();
				pig.setPositionXY(-100, -100);
				whiteEgg.setPositionXY(-100, -100);
				pigHitsEgg = true;
		    } else if (!pig.intersectsSprite(whiteEgg)) {
		    	pigHitsEgg = false;
		    }
	 }
	
	 /**
	  * Not working , the pig collides wuth the egg somewhere else
	  */
//	 private void checkCollision_pig_gold_egg() {
//		 if (pig.intersectsSprite(goldEgg) && !pigHitsGoldEgg) {
////		    	System.out.println("the pig stole the gold egg");
//				System.out.println("the gold egg obj: " + goldEgg);
//
//				System.out.println("The gold eggx pig: " +goldEgg.getPositionX());
//		    	currentScores -= 5;
//				updateScoreText();
//				pig.setPositionXY(-100, -100);
//				goldEgg.setPositionXY(500, 500);
//				pigHitsEgg = true;
//				timer.stop();
//		    } else if (!pig.intersectsSprite(goldEgg)) {
//		    	pigHitsGoldEgg = false;
//		    }
//	 	}
//	 }
	 
	private void checkCollision_blob_white_egg() {
		if (blob.intersectsSprite(whiteEgg) && !blobHitsEgg) {
			blob.setCollisionSound(DEF.AUDIO.get("collect_coin_1"));
			blob.playCollisionSound();
//	    	System.out.println("the blob eats the white egg");
	    	currentScores += 5;
			updateScoreText();
//			whiteEgg.setPositionXY(-100, -100);
			blobHitsEgg = true;
	    } else if (!blob.intersectsSprite(whiteEgg)) {
	    	blobHitsEgg = false;
	    }
	}
	
	private void checkCollision_blob_gold_egg() {
//		System.out.println("The gold eggx blob: " +goldEgg.getPositionX());

		if (blob.intersectsSprite(goldEgg) && !blobHitsGoldEgg) {
			System.out.println("I hit the gold egg");
			blob.applyBounceAnimation();
//			System.out.println("the gold egg obj: " + goldEgg);
//			System.out.println("The gold eggx: " +goldEgg.getPositionX());
//	    	System.out.println("the blob eats the gold egg");
			int counter = 6000;
			isAutoPilotMode = true;
			while (counter > 0) {
				blob.setImage(DEF.IMAGE.get("bird_with_parachute"));
				counter --;
			}
	    } else if (!blob.intersectsSprite(whiteEgg)) {
	    	blobHitsGoldEgg = false;
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
	 	 }
	 }
		
	 private void updateScoreText() {
		if (currentScores <= 0 ) {
			scoreText.setText("Score: " + 0);
		} else {
			scoreText.setText("Score: " + currentScores);
		}
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

//	    private void calculateMotion(Line touchedWall){
//	        if(touchedWall.equals(rightWall)){
//	            right = false;
//	            up = false;
//
//	        }
//	        if(touchedWall.equals(leftWall)){
//	            right = true;
//	            up = true;
//	        }
//	        if(touchedWall.equals(ceiling)){
//	            right = true;
//	            up = false;
//	        }
//	        if(touchedWall.equals(floor)){
//	            right = false;
//	            up = true;
//	        }
//
//
//
//	    }
//	    public void move(boolean right, boolean up){
//	        if(right && !up){
//	            blob.setTranslateX((movex += (getRate() + xvariation)));
//	            blob.setTranslateY((movey += (getRate() + yvariation)));  
//	        }
//	        if(right && up){
//	            blob.setTranslateX((movex += (getRate() + xvariation)));
//	            blob.setTranslateY((movey -= (getRate() + yvariation)));
//	        }
////	        if(!right && up){
////	            ball.setTranslateX((movex -= (getRate() + xvariation)));
////	            ball.setTranslateY((movey -= (getRate() + yvariation)));
////	        }
////	        if(!right && !up){
////	            ball.setTranslateX((movex -= (getRate() + xvariation)));
////	            ball.setTranslateY((movey += (getRate() + yvariation)));
////	        }
//	        System.out.println("("+movex+", "+movey+")");
//
//	    }
	    
	    public double getRate(){
	        return rate;
	    }

//	    public void setRate(double rate){
//	        this.rate = rate; 
//	    }
	 Random rand100 = new Random();
	 public void moveEggandPig() {
		 for (int i = 0; i < DEF.PIPE_COUNT; i++) {
			 if (dPipes.get(i).getPositionX() <= -DEF.D_PIPE_WIDTH) {
				 nextX_down = dPipes.get((i+1)%DEF.PIPE_COUNT).getPositionX()+DEF.PIPE_X_GAP;
				 double randomOffset = rand1.nextDouble() * DEF.PIPE_RANGE;
				 // Limit the randomOffset so that D_PIPE_POS_Y never goes higher than 0
				 randomOffset = Math.min(randomOffset, Math.abs(DEF.D_PIPE_POS_Y));
				 nextY_down = DEF.D_PIPE_POS_Y - randomOffset;
				 dPipes.get(i).setPositionXY(nextX_down, nextY_down);
		 		 //System.out.println("Pig is moving " + nextY_down);
		 		 
		 		//nextX_up = uPipes.get((i+1)%DEF.PIPE_COUNT).getPositionX();
		 		 nextY_up = nextY_down + DEF.PIPE_Y_GAP;
		 		 uPipes.get(i).setPositionXY(nextX_down, nextY_up);
		 		
//				 UTILIZE THE HEIGHT OF THE PIG TO SET IT DOWN
				 pig.setPositionXY(nextX_down-9, nextY_down+DEF.D_PIPE_HEIGHT);
				 showEgg = true;

			 }

		 }
		
		 pig.setVelocity(DEF.SCENE_SHIFT_INCR, 0.1);
		 pig.render(gc);
		 pig.update(DEF.EASY_SCENE_SHIFT_TIME);
		 
		 boolean showWhite=false;
		 boolean showGold=false;
		 Random rand = new Random();
		 for (int i = 0; i < DEF.PIPE_COUNT-1; i++) {
			 if (dPipes.get(i).getPositionX() <= -DEF.D_PIPE_WIDTH) {
	 			 pairNumber++;
	 			 
		 		 nextX_down = dPipes.get((i+1)%DEF.PIPE_COUNT).getPositionX()+DEF.PIPE_X_GAP;
		 		 double randomOffset = rand1.nextDouble() * DEF.PIPE_RANGE;
		 		 // Limit the randomOffset so that D_PIPE_POS_Y never goes higher than 0
		 		 randomOffset = Math.min(randomOffset, Math.abs(DEF.D_PIPE_POS_Y));
		 		 nextY_down = DEF.D_PIPE_POS_Y - randomOffset;
		 		 dPipes.get(i).setPositionXY(nextX_down, nextY_down);
		 		
		 		 //nextX_up = uPipes.get((i+1)%DEF.PIPE_COUNT).getPositionX();
		 		 nextY_up = nextY_down + DEF.PIPE_Y_GAP;
		 		 uPipes.get(i).setPositionXY(nextX_down, nextY_up);
		 		 
		 		 double randWhite=rand.nextDouble();
		 		//System.out.println("rand white " +randWhite );
		 		 
		 		double randGold=rand.nextDouble();
		 		//System.out.println("rand gold " +randGold );
		 		
		 		 //Randomize whether eggs show up
		 		//if white show up and gold wont showup
		 		if (randWhite < 0.5 & randGold>=0.3) {
		 			//System.out.println("WHITE SHOW " );
		 			showWhite=true;
		 		    whiteEgg.setPositionXY(nextX_down-10, nextY_up-60);
		 		}
		 		if (randGold < 0.3) {
		 			//System.out.println("GOLD SHOW " );
		 			showGold=true;
		 		    goldEgg.setPositionXY(nextX_down-10, nextY_up-60);
		 		}
				 
			 }
			 
			// Render and update the gold egg only if it's positioned
		     }
		 
		    if (!showWhite && pig.getPositionX() <= whiteEgg.getPositionX()) {
		    	//System.out.println("the pig position is here");
		        whiteEgg.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
		        whiteEgg.render(gc);
		        System.out.println(whiteEgg);
		        whiteEgg.update(DEF.EASY_SCENE_SHIFT_TIME);
		    }

		    // Render and update the gold egg only if it's positioned and the pig is near
		    if (!showGold && pig.getPositionX() <= goldEgg.getPositionX()) {
		    	System.out.println("the gold pig position is here");
		        goldEgg.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
		        goldEgg.render(gc);
		        goldEgg.update(DEF.EASY_SCENE_SHIFT_TIME);
		    }
			//pig.render(gc);
			 
			 
		 
	 
	 }
 // End of MyTimer class
}}// End of AngryFlappyBird Class

