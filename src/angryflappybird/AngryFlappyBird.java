//Version Dec 8: Anh pushed
//Please do not edit font, and position of text/box randomly, let's discuss if changes are needed
package angryflappybird;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
	private Defines DEF = new Defines();
	//text display
	private int currentLives = 3;
	Text livesText =new Text("Lives Left: 3");
	private Text gameOverText;
	private int currentScores=0;
	Text scoreText= new Text("Score: 0");
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
		gameLevel = new VBox(5);
		gameLevel.getChildren().addAll(DEF.easyButton, DEF.mediumButton, DEF.hardButton);
		gameControl.getChildren().addAll(DEF.startButton, gameLevel, gameDesc);
		}
	private void mouseClickHandler(MouseEvent e) {
		if (GAME_OVER) {
			resetGameScene(false);
	}
		else if (GAME_START){
			clickTime = System.nanoTime();
	}
		GAME_START = true;
		CLICKED = true;
	}
	private void resetGameScene(boolean firstEntry) {
		// Create a Text object to display the score
		//Text scoreText = new Text("Score: 0");
		
		scoreText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 19));
		scoreText.setFill(Color.WHITE);
		scoreText.setX(8);
		scoreText.setY(20);
		
		//livesText = new Text("Lives Left: 3");
		livesText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 19));
		livesText.setFill(Color.BROWN);
		livesText.setX(245);
		livesText.setY(560);
		
		gameOverText = new Text("Game Over, click to play again");
		gameOverText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));
		gameOverText.setFill(Color.RED);
		gameOverText.setX(DEF.SCENE_WIDTH / 2 - 150);
		gameOverText.setY(DEF.SCENE_HEIGHT / 2);
		// Set the initial visibility of gameOverText to false
		gameOverText.setVisible(false);
		
		// reset variables
		CLICKED = false;
		GAME_OVER = false;
		GAME_START = false;
		floors = new ArrayList<>();
		uPipes = new ArrayList<>();
		dPipes = new ArrayList<>();
		
		if(firstEntry) {
			// create two canvases
			Canvas canvas = new Canvas(DEF.SCENE_WIDTH, DEF.SCENE_HEIGHT);
			gc = canvas.getGraphicsContext2D();
			//Make background defined globally as an attribute
			// create a background
			background = DEF.IMVIEW.get("day_background");
			// System.out.println(background);
			// create the game scene
			gameScene = new Group();
			gameScene.getChildren().addAll(background, canvas, scoreText, livesText, gameOverText);
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
		//initialize Pipes
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
			
			// Calculate the initial Y forU uPipe based on the corresponding dPipe
			uPipeInitialY = initialY + DEF.PIPE_Y_GAP;
			uPipe = new Pipe(initialX, uPipeInitialY, DEF.IMAGE.get("upipe1"));
			uPipe.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
			uPipe.render(gc);
			uPipes.add(uPipe);
			
		}
		
		//for rendering the eggs on the pipe
		
		whiteEgg.render(gc);
		// initialize blob
		blob = new Blob(DEF.BLOB_POS_X, DEF.BLOB_POS_Y, DEF.IMAGE.get("bird1"));
		blob.render(gc);
		
		double pigStart = initialY+DEF.PIG_POS_START;
		pig = new Sprite(DEF.PIG_POS_X, initialY + DEF.D_PIPE_HEIGHT, DEF.IMAGE.get("monster_thief"));
		System.out.println("tHE PIG: " + pig);
		pig.setVelocity(0, DEF.BLOB_DROP_VEL);
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
	 	 movePig();
	 	
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
	 /**
	 * @params None
	 * movePipe is responsible for animating the upward and downward pipes
	 */
	 Random rand1 = new Random();
	 public void movePipe() {

	 	for (int i = 0; i < DEF.PIPE_COUNT; i++) {
	 		 System.out.println("Pipe " + i + " X: " + dPipes.get(i).getPositionX());
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
		 		 
		 		
		 		 }
				 dPipes.get(i).render(gc);
				 dPipes.get(i).update(DEF.EASY_SCENE_SHIFT_TIME);
				
				 uPipes.get(i).render(gc);
				 uPipes.get(i).update(DEF.EASY_SCENE_SHIFT_TIME);
	 }
	 	System.out.println("pairNumber" + pairNumber);
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
		 		 System.out.println("Pig is moving " + nextY_down);
		 		 
		 		//nextX_up = uPipes.get((i+1)%DEF.PIPE_COUNT).getPositionX();
		 		 nextY_up = nextY_down + DEF.PIPE_Y_GAP;
		 		 uPipes.get(i).setPositionXY(nextX_down, nextY_up);
		 		
				 double curr_dpipe_pos_x = dPipes.get(i).getPositionX();
//				 System.out.println("curr pig x position: "+curr_dpipe_pos_x);
				 double curr_dpipe_pos_y = dPipes.get(i).getPositionY();
				 System.out.println("curr pig y position: "+curr_dpipe_pos_y);
//				 UTILIZE THE HEIGHT OF THE PIG TO SET IT DOWN
				 pig.setPositionXY(nextX_down, nextY_down+DEF.D_PIPE_HEIGHT);

			 }

		 }
		
			 pig.setVelocity(-0.4, whiteEgg.getPositionY());
			 pig.render(gc);
			 pig.update(DEF.EASY_SCENE_SHIFT_TIME);
			//pig.render(gc);
		 
	 }
	 
	 public void moveEgg() {
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
		 		 
		 		 
		 		 //Randomize whether eggs show up
		 		 if (rand.nextDouble() < 0.5) {
		 		 whiteEgg.setPositionXY(nextX_down-8, nextY_up-60);
		 		 }
				 
			 }
			 whiteEgg.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
			 whiteEgg.render(gc);
			 whiteEgg.update(DEF.EASY_SCENE_SHIFT_TIME);
		 }
		 	
	 }
	 
	 
	 // step2: update blob
	 private void moveBlob() {
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
	 
	 
	 
	 /**
	  * Create a helper method
	  * for loop to check the size of the pipe
	  * check if the pipe is offscreen
	  * 	calc the nextx and the nextY from the pipe similar to 324
	  *     nextY set from the uPipe gotten from the height of the pipe on the top to have a range of how high 
	  *     the egg should be from pipe. more calc done on the x var
	  *     then set position for the egg
	  *     randomize where the egg should show up
	  */
	
	 
	 
	// Helper method to reset the position of the bird
	 //should be fixed, the bird should be where?
	 private void resetBirdPosition() {
	     blob.setPositionXY(DEF.BLOB_POS_X, DEF.BLOB_POS_Y);
	     blob.setVelocity(0, 0);
	     //bird appear again
	     //blob.render(gc);
	 }
	 
	 public void checkCollision() {
		 boolean hitAUpipe = false;
		 // check collision with the floor
		 for (Sprite floor: floors) {
		 GAME_OVER = GAME_OVER || blob.intersectsSprite(floor);
		 }
		
		 /**handling the blob intersecting with a upipe
		 * the life of the blob should go to life - 1
		 * the UI should be updated
		 * if the life reaches 0, the game ends
		 **/
		
		 for (Pipe uPipe: uPipes) {
			 if (blob.intersectsPipe(uPipe)) {
				 //show the hit effect
				 //showHitEffect();
				 //re-render the current_lives
				 System.out.println("Hit uPipe");
				System.out.println("lives BEFORE uPIPE:" + currentLives);
			 	currentLives--;
			 	 System.out.println("lives AFTER uPIPE:" + currentLives);
			 	// if (currentLives>=0){
			 	updateLivesText();
			 	 //}
			 	GAME_OVER = GAME_OVER || blob.intersectsPipe(uPipe);
			 // Reset the position of the bird after collision with pipes
	            resetBirdPosition();
			 }
		 }
		
		 /**handling the blob intersecting with a dpipe
		 *the life of the blob should go to life - 1
		 *the UI should be updated
		 *if the life reaches 0, the game ends
		 **/
		 for (Pipe dPipe: dPipes) {
			 if (blob.intersectsPipe(dPipe)) {
				//show the hit effect
				//showHitEffect();
				//re-render the current_lives
				 System.out.println("Hit dPipe");
				 System.out.println("lives BEFORE dPIPE:" + currentLives);
			 	 currentLives--;
			 	 System.out.println("lives AFTER dPIPE:" + currentLives);
		// 	 	 if (currentLives>0){
			 	 updateLivesText();
			 		 
			 	 //}
			 GAME_OVER = GAME_OVER || blob.intersectsPipe(dPipe);
			// Reset the position of the bird after collision with pipes
	            resetBirdPosition();
			 }
		 }
		 
		 /**
		 * This handles the logic for letting the white eggs increment the total coins
		 * available
		 */
		// handling the logic for letting the white eggs increment the total coins available
		    for (Pipe dPipe : dPipes) {
		        if (blob.intersectsSprite(whiteEgg)) {
		            currentScores += 5;
		            // show the additional score gained
		            //showHitEffect();
		            // re-render the current_score on the screen
		            updateScoreText();

		            // Reset the position of the bird after collecting a white egg
		            //resetBirdPosition();
		        }
		    }
		 // check if the bird goes through a pair of pipes without collision
		    if (!GAME_OVER && blob.getPositionX() > uPipes.get(0).getPositionX() + DEF.D_PIPE_WIDTH) {
		        int currentPassedPipeIndex = (int) (blob.getPositionX() / (DEF.D_PIPE_WIDTH + DEF.PIPE_X_GAP));
		        System.out.println("currentPassedPipeIndex:"+currentPassedPipeIndex);
		        if (currentPassedPipeIndex > lastPassedPipeIndex) {
		            // The bird has passed through a new set of pipes
		            currentScores = pairNumber; // Set the score equal to the pair number
		            lastPassedPipeIndex = currentPassedPipeIndex;

		            // Update the score text on the screen
		            updateScoreText();
		        }
		    }

		 // end the game when blob hit stuff
		 if (GAME_OVER) {
			 showHitEffect();
			 for (Sprite floor: floors) {
			 floor.setVelocity(0, 0);
			 timer.stop();
			 }
			 
		 }
		 
		// Set the game_over to true if no lives remaining
		 if (currentLives == 0) {
		         GAME_OVER = true;
		         showHitEffect();
				 for (Sprite floor: floors) {
				 floor.setVelocity(0, 0);
				 timer.stop();
				 }
		        System.out.println("Game Over! Scores: " + currentScores + ", Lives: " + currentLives);
		        System.out.println("Game Restarted!");
//		        timer.stop();
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
	
	 //when bird loses a life, update remaining lives
		 //when bird loses a life, update remaining lives
	 //havent tested this function
	 private void updateLivesText() {
	 	 if (currentLives >= 0) {
	 		 livesText.setText("Lives Left: " + currentLives);
	 		System.out.println("You have " + currentLives + " lives left");
	 	 } else {
	 		 livesText.setText("Lives Left: 0");
	 	 }
	 	}
		
		 private void updateScoreText() {
//		 if (blob.intersectsSprite(whiteEgg)) {
//		 currentScore += 5;
		 // Update the score text on the screen
			 scoreText.setText("Score: " + currentScores);
			 //System.out.println("Update Score " + currentScores);
		 }
		
		
	} // End of MyTimer class
	} // End of AngryFlappyBird Class
	//Draft (do not delete)
	//if (currentLives==0) {
//		GAME_OVER = true;
//	 	gameOverText.setVisible(true);
//	 	gameScene.setOnMouseClicked(e -> {
	// gameOverText.setVisible(false); // Hide the game over message when restarting
	// });
		// Allow the player to play again by clicking anywhere on the scene
	// gameScene.setOnMouseClicked(e -> {
	// gameOverText.setVisible(false); // Hide the game over message when restarting
	// });
//		}

