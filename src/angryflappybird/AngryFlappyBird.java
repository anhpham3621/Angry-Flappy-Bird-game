package angryflappybird;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Random;

/**
 * AngryFlappyBird is a game inspired by Flappy Bird. It takes you through the journey of "Blob"
 * as it attempts to navigate various obstacles and collect rewards along the way. Enjoy!
 */
public class AngryFlappyBird extends Application {
	
	//bounce method
	private boolean bounced = false;
	private boolean pipeCollision = false;
	private boolean pigCollision = false;
	private ArrayList<Sprite> pigs;
	private ArrayList<Sprite> whiteEggs;
	private ArrayList<Sprite> goldenEggs;

	private boolean collisionDetected = false;
	private double SCENE_SHIFT_TIME;
	private double SCENE_SHIFT_INCR;
	private int BLOB_FLY_VEL;
	private int BLOB_DROP_VEL;
	private int BLOB_DROP_TIME;
	private double PIG_DROP_Y_VEL;
	
	//Game Over Animation
	private ImageView gameOverAnimation;
	private MediaPlayer mediaPlayer;

	private int background_counter = 0;
	private int gold_egg_counter = 0;
	private int counter = 0;
	private boolean isInitGame = false;
	private Defines DEF = new Defines();
	private boolean isCollided = false;
	
	//text display
	private int currentLives;
	private Text scoreText;
	private Text livesText;
	public int currentScores;

	// time related attributes
	private long clickTime, startTime, elapsedTime, bounceTime;

	// game components
	private Blob blob;
	private ArrayList<Sprite> floors;
	private ArrayList<Sprite> uPipes;
	private ArrayList<Sprite> dPipes;
	private Sprite dPipe;
	private Sprite uPipe;
	private Sprite whiteEgg;
	private Sprite goldEgg;
	private Sprite pig;
	private AnimationTimer timer;

	
	// game flags
	private boolean CLICKED, GAME_START, GAME_OVER;
	private boolean isAutoPilot = false;
	private boolean isNightBackground = true;

	// scene graphs
	private Group gameScene;	
	private VBox gameControl;	 
	private VBox gameLevel;
	private VBox gameDesc;
	private GraphicsContext gc;		

	double nextX_down = 0;
	double nextY_down = 0;
	double nextX_up = 0;
	double nextY_up = 0;
	double initialY = 0;
	private double uPipeInitialY;

	private ImageView background;
	private ComboBox<String> comboBox;

	/**
	 * The main method is required in order to launch the JavaFX application
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * The start method is the entry point into angryflappybird game
	 * @param primaryStage
	 * @throws Exception
	 */
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
	
	/**
	 * resetGameControl is responsible for resetting the game control
	 * @param None
	 */
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
		ImageView pipeImage = new ImageView(DEF.IMAGE.get("upward_pipe"));
		pipeImage.setFitHeight(50);
		pipeImage.setFitWidth(150);
		HBox pHBox = new HBox(5);
		pHBox.getChildren().addAll(pigImage, pigTextLabel);
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
	
	/**
	 * GameModeHandler is responsible for handling the game mode
	 * It switches between easy, medium or hard mode based on the users' selection
	 * Easy mode is the default mode and much slower.
	 * @param mode
	 */
	private void GameModeHandler(String mode) {
	    	switch (mode) {
				case "Easy":
					SCENE_SHIFT_INCR = DEF.SCENE_SHIFT_INCR;
					SCENE_SHIFT_TIME = DEF.SCENE_SHIFT_TIME;
					BLOB_DROP_TIME = DEF.BLOB_EASY_DROP_TIME;
					BLOB_DROP_VEL = DEF.BLOB_EASY_DROP_VEL;
					BLOB_FLY_VEL = DEF.EASY_BLOB_FLY_VEL;
					PIG_DROP_Y_VEL=DEF.EASY_PIG_DROP_Y_VEL;
					break;
	        	case "Medium":
	        		SCENE_SHIFT_INCR = DEF.SCENE_SHIFT_INCR;
					SCENE_SHIFT_TIME = DEF.SCENE_SHIFT_TIME;
					BLOB_DROP_TIME = DEF.BLOB_MED_DROP_TIME;
					BLOB_DROP_VEL = DEF.BLOB_MED_DROP_VEL;
					BLOB_FLY_VEL = DEF.MED_BLOB_FLY_VEL;
					PIG_DROP_Y_VEL=DEF.MED_PIG_DROP_Y_VEL;
					break;
	        	case "Hard":
	        		SCENE_SHIFT_INCR = DEF.SCENE_SHIFT_INCR;
					SCENE_SHIFT_TIME = DEF.SCENE_SHIFT_TIME;
					BLOB_DROP_TIME = DEF.BLOB_HARD_DROP_TIME;
					BLOB_DROP_VEL = DEF.BLOB_HARD_DROP_VEL;
					BLOB_FLY_VEL = DEF.HARD_BLOB_FLY_VEL;
					PIG_DROP_Y_VEL=DEF.HARD_PIG_DROP_Y_VEL;
					break;
				case "":
					SCENE_SHIFT_INCR = DEF.SCENE_SHIFT_INCR;
					SCENE_SHIFT_TIME = DEF.SCENE_SHIFT_TIME;
					BLOB_DROP_TIME = DEF.BLOB_EASY_DROP_TIME;
					BLOB_DROP_VEL = DEF.BLOB_EASY_DROP_VEL;
					BLOB_FLY_VEL = DEF.EASY_BLOB_FLY_VEL;
					PIG_DROP_Y_VEL=DEF.EASY_PIG_DROP_Y_VEL;
					break;
	       }
	}

	/**
	 * mouseClickHandler is responsible for handling the start button when clicked
	 * if the game is over, it resets the scene to a neutral screen
	 * if the game is not over, it starts the game using a timer
	 * @param e
	 */
	private void mouseClickHandler(MouseEvent e) {
		if (GAME_OVER) {
			resetGameScene(false);
		}
		else if (GAME_START){
			clickTime = System.nanoTime();
			gameOverAnimation.setVisible(false);
	        gameScene.getChildren().remove(gameOverAnimation);
		}
		GAME_START = true;
		CLICKED = true;
	}

	/**
	 * resetGameScene is responsible for resetting the game scene
	 * of its argument is true, it resets the game scene 
	 * and creates new objects to be rendered on the screen
	 * @param firstEntry
	 */
	private void resetGameScene(boolean firstEntry) {
		CLICKED = false;
		GAME_OVER = false;
		GAME_START = false;

		//initialize game components. To create the illusion of multiple pipes on screen
		//we utilize an arraylist to store the pipes and floor
		floors = new ArrayList<>();
		uPipes = new ArrayList<>();
		dPipes = new ArrayList<>();
		pigs = new ArrayList<>();
	    whiteEggs = new ArrayList<>();
	    goldenEggs = new ArrayList<>();
		
		currentLives = DEF.MAX_LIFE;
		currentScores = 0;

		// initialize time related attributes
        startTime = System.nanoTime();
        timer = new MyTimer();
        timer.start();

		if(firstEntry) {
			isInitGame = true;
			// create two canvases
			Canvas canvas = new Canvas(DEF.SCENE_WIDTH, DEF.SCENE_HEIGHT);
			gc = canvas.getGraphicsContext2D();
			// create a background
			background = DEF.IMVIEW.get("day_background");
			// create the game scene
			gameScene = new Group();
			gameScene.getChildren().add(background);
			gameScene.getChildren().add(canvas);
		}

		// initialize other components to be displayed on the screen 
		//through helper methods
		initializegameOverAnimation();
		initializeFloors();
		initializePipesEgg();
        initializePigs();
        initializeBlob();
       
		//Only make the gameOver animation visible when the game is over
        if (gameOverAnimation != null) {
            gameOverAnimation.setVisible(false);
            gameScene.getChildren().remove(gameOverAnimation);
        }
        
		if (livesText != null) {
            // Remove the previous Text node
            gameScene.getChildren().remove(livesText);
        }

		// Create a Text object to display the blob's current life
		livesText = new Text("Lives Left: 3");
		livesText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 19));
		livesText.setFill(Color.BROWN);
		livesText.setX(245);
		livesText.setY(560);
		gameScene.getChildren().add(livesText);
		
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
	
	/**
	 * initializeFloors is responsible for initializing the floors to be animated in the
	 * timer class
	 * @param None
	 */
	private void initializeFloors() {

			// initialize floor by assigning them a position and velocity 
			//the velocity depends on the game mode
			for(int i=0; i<DEF.FLOOR_COUNT; i++) {
				int posX = i * DEF.FLOOR_WIDTH;
				int posY = DEF.SCENE_HEIGHT - DEF.FLOOR_HEIGHT;
				Sprite floor = new Sprite(posX, posY, DEF.IMAGE.get("floor1"));
				floor.setVelocity(SCENE_SHIFT_INCR, 0);
				floor.render(gc);
				floors.add(floor);
			}
	}
	
	/**
	 * @params None
	 * initializePipesEgg is responsible for initializing the pipes and 
	 * eggs to be animated in the
	 */
	private void  initializePipesEgg() {
			//initialize Pipes n eggs
			Random rand = new Random();
			for (int i = 0; i < DEF.PIPE_COUNT; i++) {
				//unfinished x-calculation
			    double initialX = i*DEF.PIPE_X_GAP + DEF.SCENE_WIDTH;
				double randomOffset = rand.nextDouble() * DEF.PIPE_RANGE;
				// Limit the randomOffset so that D_PIPE_POS_Y never goes higher than 0
				randomOffset = Math.min(randomOffset, Math.abs(DEF.D_PIPE_POS_Y));
				initialY = DEF.D_PIPE_POS_Y - randomOffset;
				dPipe = new Sprite(initialX, initialY, DEF.IMAGE.get("dpipe2"));
				dPipe.setVelocity(SCENE_SHIFT_INCR, 0);
				dPipe.render(gc);
				dPipes.add(dPipe);
				//somehow bringing white egg up here work, but down doesnt
				whiteEgg = new Sprite(DEF.D_PIPE_POS_X-13, uPipeInitialY-60, DEF.IMAGE.get("white_egg"));
				goldEgg = new Sprite(-100, -100, DEF.IMAGE.get("golden_egg"));
				whiteEggs.add(whiteEgg);
				goldenEggs.add(goldEgg);
				
				uPipeInitialY = initialY + DEF.PIPE_Y_GAP;
				uPipe = new Sprite(initialX, uPipeInitialY, DEF.IMAGE.get("upipe1"));
				uPipe.setVelocity(SCENE_SHIFT_INCR, 0);
				uPipe.render(gc);
				uPipes.add(uPipe);
			}
	}
	
	/**
	 * @params None
	 * initializeBird is responsible for initializing the bird 
	 * to be animated in the timer class
	 * An image name is set to identify the blob later on
	 */
	private void initializeBlob() {
		// initialize blob
		blob = new Blob(DEF.BLOB_POS_X, DEF.BLOB_POS_Y, DEF.IMAGE.get("bird1"));
		blob.setImageName("bird1");
		blob.render(gc);
	}	
	
	/**
	 * @params None
	 * initializePigs is responsible for initializing the pigs to be animated in the
	 * timer class
	 */
	private void initializePigs() {
		double pigStart = initialY+DEF.PIG_POS_START;
		pig = new Sprite(570, 570, DEF.IMAGE.get("monster_thief"));
		pig.setVelocity(0, 0);
		pig.render(gc);
		pigs.add(pig);
	}		
	
	/**
	 * @params None
	 * initializegameOverAnimation is responsible for initializing the game over animation
	 * It is triggered once the blob loses a game
	 */
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

/**
 * MyTimer extends the AnimationTimer class and is responsible for animating the game
 * by creating a continuous loop that updates the game components as needed
 */
class MyTimer extends AnimationTimer {

	//this counter is used to set the background switch time
	public void handle(long now) { 	
		background_counter++;

		//the counter is used for the bird's autopilot mode
		gold_egg_counter++;

		//time keeper
	 	elapsedTime = now - startTime;
	 	startTime = now;
	
	 	//clears the current_scene
	 	gc.clearRect(0, 0, DEF.SCENE_WIDTH, DEF.SCENE_HEIGHT);
	 	
		//
		if (GAME_START) {
	 	
	 		//step1: update background
			if (background_counter>450) {
				background_counter=0;
				isNightBackground= !isNightBackground;
			}
	 		displayBackground();
			
			//step2: update the autopilot mode of the blob if needed
			if (isAutoPilot && gold_egg_counter % 1000 == 0) {
				isAutoPilot = false;
				gold_egg_counter = 0;
			}
			
			//update the blob's score
	 		updateScoreText();

			//update the blob's lives
	 		updateLivesText();

	 	 	//step2: update floor
	 	 	moveFloor();
	 	
			//step3: movePipe
			movePipe();
			
			//step4: check for collision only when we are not in
			//autopilot mode
			if(!isAutoPilot) {
				updatePipesScore();
				checkCollision();
			}
	 	
			// step5: update blob
			moveBlob();
	 	
			//step6: update egg and pig
			moveEggAndPig();
	 	}
	}
	
	/**
	 * displayBackground handles switching the background from night mode to day periodically
	 * it utilizes a boolean value that checks what the background is set to
	 * if the background is set to night, it switches to day and vice versa
	 * if the background is set to day, it switches to night and vice versa
	 * @param None
	 */
	private void displayBackground() {
		 if(isNightBackground) {
			 gc.drawImage (DEF.IMAGE.get("night_background"), 0, 0);
		 }
		 else if(!isNightBackground) {
			 gc.drawImage (DEF.IMAGE.get("day_background"), 0, 0);
		 }
	}
	
	/**
	 * moveFloor handles the animation of the floor by moving it in and out of the screen
	 * @params None
	 */
	private void moveFloor() {
		for(int i=0; i<DEF.FLOOR_COUNT; i++) {
			if (floors.get(i).getPositionX() <= -DEF.FLOOR_WIDTH) {
				double nextX = floors.get((i+1)%DEF.FLOOR_COUNT).getPositionX() + DEF.FLOOR_WIDTH;
			 	double nextY = DEF.SCENE_HEIGHT - DEF.FLOOR_HEIGHT;
			 	floors.get(i).setPositionXY(nextX, nextY);
			}
			floors.get(i).render(gc);
			floors.get(i).update(SCENE_SHIFT_TIME);
		}
	}
	 
	/**
	 * @params None
	 * movePipe is responsible for animating the upward and downward pipes
	 * It has to main functions: 1. randomize the height and gaps between the pipes
	 * 2. animate the pipes and create a new set of pipes when the old ones are going out of screen.
	*/
	Random rand1 = new Random();
	public void movePipe() {
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
			uPipes.get(i).render(gc);
			uPipes.get(i).update(SCENE_SHIFT_TIME);
	 	}
	}

	/**
	 * moveBlob is responsible for animating the blob
	 * it has two main functions: 1. animate the blob when the start button is clicked
	 * 2. animate the blob when it is in autopilot mode
	 * @params None
	 */
	private void moveBlob() {
		//set a sound for the blob's movement.
		blob.setSound(DEF.AUDIO.get("bird_flapping_1"));
		blob.playSound();
		long diffTime = System.nanoTime() - clickTime;
		
		// this switches the blob into different images to create the illusion of flying
		if (CLICKED && diffTime <= BLOB_DROP_TIME) {
			int imageIndex = Math.floorDiv(counter++, DEF.BLOB_IMG_PERIOD);
			imageIndex = Math.floorMod(imageIndex, DEF.BLOB_IMG_LEN);
			blob.setImage(DEF.IMAGE.get("bird"+String.valueOf(imageIndex+1)));
			blob.setImageName("bird"+String.valueOf(imageIndex+1));
			blob.setVelocity(0, BLOB_FLY_VEL);
		} 
		
		//check if we are on autoPilot mode
		else if (isAutoPilot){
			//on autopilot mode, the bird should stop flapping and avoid collisions
			//without hitting the start button
			blob.setPositionXY(200, 250);
			blob.setVelocity(0, 0);
			blob.setImage(DEF.IMAGE.get("bird_with_parachute"));
			blob.setImageName("bird_with_parachute");
		}
			
		//check if the previous state of the bird was autopilot so we can drop the blob and switch 
		//the image back to the original one
			else if (!isAutoPilot && blob.blobGetName().equals("bird_with_parachute")&&!bounced) {
				blob.setImage(DEF.IMAGE.get("bird1"));
				blob.setVelocity(0, 100);
				CLICKED = false;
				pig.setVelocity(-0.6, 0.1);
				//this is to prevent immediate collision when we get out of autopilot mode.
				pig.setPositionXY(600, 600);
			}
			//drop the blob if the start button is not clicked
			else {
				blob.setVelocity(0, BLOB_DROP_VEL);
				CLICKED = false;
				if (bounced) {
	                isBounced();
	            }
				else if(pigCollision) {
					GAME_OVER=true;
					gameOverAnimation.setVisible(true);
				}
			}
			// render blob on GUI
			blob.update(elapsedTime * DEF.NANOSEC_TO_SEC);
			blob.render(gc);
	}
    
	/**
	 * moveEggAndPig is responsible for animating the eggs and pigs
	 * The eggs appear randomly on downard pipes through our randomizer algorithm
	 * The pig appears randomly on the screen dropping from a top pipe to a low pipe
	 */
	public void moveEggAndPig() {
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
				
			if (randWhite < 0.8 & randGold>=0.4) {
				showWhite=true;
				whiteEgg.setPositionXY(nextX_down-10, nextY_up-60);
			}
			if (randGold < 0.4) {
				showGold=true;
				goldEgg.setPositionXY(nextX_down-10, nextY_up-60);
			}
			if(randPig>=.5 && randPig<.99){
				showPig=true;
			pig.setPositionXY(nextX_down-9, nextY_down+350);
			}
		}

		// Render and update the the eggs only if they are positioned
		 if(!showGold) {
			 if (!bounced) {
				 goldEgg.setVelocity(SCENE_SHIFT_INCR, 0);
			 }
			goldEgg.render(gc);
			goldEgg.update(SCENE_SHIFT_TIME);
		 }
		 if(!showWhite) {
			 if (!bounced) {
				 whiteEgg.setVelocity(SCENE_SHIFT_INCR, 0);
			 }
			whiteEgg.render(gc);
			whiteEgg.update(SCENE_SHIFT_TIME);
		}
		 if(!showPig) {
			 if (!bounced) {
			     pig.setVelocity(SCENE_SHIFT_INCR, DEF.PIG_DROP_Y_VEL);
			 }
			pig.render(gc);
			pig.update(SCENE_SHIFT_TIME);
        }
		}
	}

	/**
	  * @param None
	  * updatePipesScore updates the score when we pass through 
	  */
	 public void updatePipesScore() {
	     for (int i = 0; i < DEF.PIPE_COUNT; i++) {
	         if (dPipes.get(i).getPositionX()== -(dPipes.get(i).getPositionX())){
              currentScores++;
              updateScoreText();
	         }
	     }
	 }
	/**
	 * @params None
	 * resetBirdPosition is responsible for resetting the bird's position by creating a bounce
	 * back effect. It is triggered when the bird hits an obstacle
	 */
	private void resetBirdPosition() {
	     blob.setPositionXY(DEF.BLOB_POS_X, DEF.BLOB_POS_Y);
	     blob.setVelocity(0, 0);
	}
	 
	/**
	 * @params None
	 * checkCollision is a suite of helper methods that checks for various collisions
	 * during the game. It checks for collision with the floor, pipes, eggs and pigs
	 */
	public void checkCollision() {
		
		// check collision with the floor
		checkCollision_floor();

		//check for pig intersecting the egg
		checkCollision_blob_white_egg();
		
		//check collision blob gold egg
		checkCollision_blob_gold_egg();
		
		//check blob collision with the upipes
		checkCollision_blob_upipes();
		    
		//check for blob intersection with pig
		checkCollision_blob_pig();
		 
		//check for blob intersection with dpipes
		checkCollision_blob_dpipes();
		
		//check collision pig with upipe(we want it to disappear)
		checkCollision_pig_pipe();

		//check collision pig with egg
		checkCollision_pig_white_egg();	
		
		//check for collision pig with gold egg
		checkCollision_pig_gold_egg();
		
		// Set the game_over to true if no lives are remaining
		if (currentLives <= 0) {
		    GAME_OVER = true;
		    bounced = false;
            pipeCollision = false;
            pigCollision = false;
		}
		    
		// if GAME_OVER == true, we stop the timer and display the game over animation
		 if (GAME_OVER) {
			bounced = false;
            pipeCollision = false;
            pigCollision = false;
			showHitEffect();
			blob.stopSound();
			for (Sprite floor: floors) {
				//stop the floors animation
				floor.setVelocity(0, 0);
				timer.stop();
			}
			//add the gameover animation to the game scene 
            gameScene.getChildren().add(gameOverAnimation);
            initializegameOverAnimation();
		 }
	}
	
	/**
	 * @params None
	 * checkCollision blob with floor checks if the blob intersects with the floor
	 * if it does, the game_over flag is set to true and the game ends.
	 */
	private void checkCollision_floor() {
		for (Sprite floor: floors) {
			GAME_OVER = GAME_OVER || blob.intersectsSprite(floor);
				if (GAME_OVER) {
					gameOverAnimation.setVisible(true);
				}
		}
	 }
	 
	
	/**
	 * @params None
	 * checkCollision blob with white egg increases the score by 2 points if the 
	 * blob intersects with a white egg. The white egg disappears after the collision
	 */
	private void checkCollision_blob_white_egg() {
		if (blob.intersectsSprite(whiteEgg)) {
			blob.setSound(DEF.AUDIO.get("collect_coin_1"));
			blob.playSound();
			currentScores += 2;
			updateScoreText();
			whiteEgg.setPositionXY(-100, -100);
	    }
	}
	/**
	 * checkCollision blob with gold egg sets the blob on autopilot mode if it intersects with a gold egg
	 * @param None
	 */
	private void checkCollision_blob_gold_egg() {
		if (blob.intersectsSprite(goldEgg)) {
			blob.setSound(DEF.AUDIO.get("collect_coin_1"));
			blob.playSound();
		    goldEgg.setPositionXY(-100, -100);
			blob.setImage(DEF.IMAGE.get("bird_with_parachute"));
			blob.setVelocity(0, 0);
			isAutoPilot = true;
			CLICKED = false;
			DEF.startButton.setDisable(CLICKED);
			
		}
	}

	/**
	 * @params None
	 * checkCollision blob with dpipes checks if the blob intersects with the downward pipes
	 * if it does, a live is taken and the total lives the blob has is updated.
	 */
	private void checkCollision_blob_dpipes() {
		for (Sprite dPipe: dPipes) {
			if((dPipe.getPositionX()+10 == blob.getPositionX() || dPipe.getPositionY()-10 == blob.getPositionY()) && blob.intersectsPipe(dPipe)) {
				bounceTime = System.nanoTime();
				bounced = true;
	            pipeCollision=true;
				blob.setSound(DEF.AUDIO.get("obstacle_hit_1"));
				blob.playSound();
				currentLives--;
				updateLivesText();
			}
		} 
	}
	
	/**
	 * @params None
	 * checkCollision blob with dpipes checks if the blob intersects with the downward pipes
	 * if it does, a live is taken and the total lives the blob has is updated.
	 */
	private void checkCollision_blob_upipes() {
		for (Sprite uPipe: uPipes) {
			if((uPipe.getPositionX()+10 == blob.getPositionX()|| uPipe.getPositionY()-10 == blob.getPositionY()) && blob.intersectsPipe(uPipe)) {
				bounceTime = System.nanoTime();
				bounced = true;
	            pipeCollision=true;
	            blob.setSound(DEF.AUDIO.get("obstacle_hit_1"));
				blob.playSound();
				currentLives--;
				updateLivesText();
			}
		}
	}
	 
	/**
	 * @params None
	 * checkCollision blob with pig checks if the blob intersects with the pig
	 * if it does, the game_over flag is set to true and the game ends.
	*/
	 private void checkCollision_blob_pig() {
		if (blob.intersectsSprite(pig)) {
			pigCollision = true;
			bounced = true;
	    }
	}
	 
	/**
	* @params None
	* CheckCollision pig with upipe to set it off screen when it interacts with a downard pipe
	*/
	 private void checkCollision_pig_pipe() {
		for (Sprite uPipe: uPipes) {
			if(pig.intersectsPipe(uPipe)) {
				 pig.setPositionXY(-100, -100);
			}
		}
	 }
	 
	/**
	 * @params None
	 * checkCollision with white egg decreases the blob's score if it intersects 
	 * with a white egg. The white egg and pig disappears after the collision
	*/
	private void checkCollision_pig_white_egg() {
		if (pig.intersectsSprite(whiteEgg)) {
			pig.setPositionXY(-100, -100);
			whiteEgg.setPositionXY(600, 600);
		} 
	 }
	
	/**
	 * @params None
	 * checkCollision pig with gold egg decreases the score by 5 points if the
	 */
	private void checkCollision_pig_gold_egg() {
		if(pig.intersectsSprite(goldEgg)) {
			currentScores -= 5;
			pig.setPositionXY(-100, -100);
			goldEgg.setPositionXY(-100, -100);
			updateScoreText();
		}
	}

	/**
	 * showHitEffect is responsible for showing the hit effect when the blob hits an obstacle
	 * It utilizes the parallel transition to create a fade effect
	 * @param None
	 */
	private void showHitEffect() {
		ParallelTransition parallelTransition = new ParallelTransition();
		FadeTransition fadeTransition = new FadeTransition(Duration.seconds(DEF.TRANSITION_TIME), gameScene);
		fadeTransition.setToValue(0);
		fadeTransition.setCycleCount(DEF.TRANSITION_CYCLE);
		fadeTransition.setAutoReverse(true);
		parallelTransition.getChildren().add(fadeTransition);
		parallelTransition.play();
	 }
	
	/**
	 * @param None
	 * updateLivesText is responsible for updating the lives text
	*/
	private void updateLivesText() {
		if (currentLives > 0) {
			livesText.setText("Lives Left: " + currentLives);
		} 
		else {
			livesText.setText("Lives Left: 0");
			gameOverAnimation.setVisible(true);
		}
	 }
	
	/**
	 * @param None
	 * updateScoreText is responsible for updating the score text
	 */
	private void updateScoreText() {
		scoreText.setText("Score: " + currentScores);
	}
	
	private void isBounced() {
	    long bouncedDifference = System.nanoTime() - bounceTime;
	    blob.stopSound();
	    if (bouncedDifference <= 2000000000L) {
	    	 if (bouncedDifference <= 1000000000L) {
	    		 // Move the bird down and to left during the first second of bouncing
	    	     //blob.setVelocity(-50, -100);
	    		 blob.setVelocity(-50, 100);
	    	     
	    	 } 
	    	 haltOperation();
	    }        
	    else {
	        // Reset the bouncing-related flags and bird's position after 2 seconds
	        bounced = false;
	        pipeCollision = false;
	        pigCollision = false;
	        resumeOperation();
	        blob.setVelocity(0, DEF.BLOB_FLY_VEL);
	        double newYPosition = Math.min(DEF.SCENE_HEIGHT - DEF.BLOB_HEIGHT, Math.max(0, DEF.SCENE_HEIGHT / 2));
	        blob.setPositionXY(0, newYPosition-DEF.BLOB_HEIGHT);
	        clickTime = System.nanoTime();
	    }
	}
	private void haltOperation() {
        for (Sprite floor : floors) {
            floor.setVelocity(0, 0);        
        }
        for (Sprite dPipe : dPipes) {
            dPipe.setVelocity(0, 0);        
        }
        for (Sprite uPipe : uPipes) {
            uPipe.setVelocity(0, 0);        
        }
        for (Sprite whiteEgg : whiteEggs) {
            whiteEgg.setVelocity(0, 0);        
        }
         for (Sprite goldenEgg : goldenEggs) {
            goldenEgg.setVelocity(0, 0);        
        }
         for (Sprite pig : pigs) {
             pig.setVelocity(0, 0);        
         }
    }
	private void resumeOperation() {
     for (Sprite floor : floors) {
         floor.setVelocity(SCENE_SHIFT_INCR, 0);       
     }
     for (Sprite downPipe : dPipes) {
         downPipe.setVelocity(SCENE_SHIFT_INCR, 0);        
     }
     for (Sprite upPipe : uPipes) {
         upPipe.setVelocity(SCENE_SHIFT_INCR, 0);         
     }
     for (Sprite whiteEgg : whiteEggs) {
         whiteEgg.setVelocity(SCENE_SHIFT_INCR, 0);        
     }
     for (Sprite goldenEgg : goldenEggs) {
         goldenEgg.setVelocity(SCENE_SHIFT_INCR, 0);        
     }
     for (Sprite pig : pigs) {
         pig.setVelocity(SCENE_SHIFT_INCR, 0);        
     }
 }
 // End of MyTimer class
}}
// End of AngryFlappyBird Class
