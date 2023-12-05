package angryflappybird;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
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
    
    // time related attributes
    private long clickTime, startTime, elapsedTime;   
    private AnimationTimer timer;
    
    // game components
    private Sprite blob;
    private ArrayList<Sprite> floors;
    
    // game flags
    private boolean CLICKED, GAME_START, GAME_OVER;
    
    // scene graphs
    private Group gameScene;	 // the left half of the scene
    private VBox gameControl;	 // the right half of the GUI (control)
    private VBox gameLevel;
    private VBox gameDesc;
    private GraphicsContext gc;		
    private int currentScore;
    
    private ImageView background;
    
    private Sprite uPipe;
    private Sprite dPipe;
    
	// the mandatory main method 
    public static void main(String[] args) {
        launch(args);
    }
       
    // the start method sets the Stage layer
    @Override
    public void start(Stage primaryStage) throws Exception {
    	

    	
    	// initialize scene graphs and UIs
        resetGameControl();    // resets the gameControl
    	resetGameScene(true);  // resets the gameScene
    	
        HBox root = new HBox();
		HBox.setMargin(gameScene, new Insets(0,0,0,15));
		root.getChildren().add(gameScene);
		root.getChildren().add(gameControl);
//		root.getChildren().add(scoreText);                                                                     
		
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
        
//        this is appearing by the pig for some reason
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
//        adding the remainder whi
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
        Text scoreText = new Text("Score: 0");

        // You can customize the font size and style if needed
        scoreText.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-fill: black");
        
    	// Create a Text object to display the][i[ score
        Text livesText = new Text("Score: 3");

        // You can customize the font size and style if needed
        livesText.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-fill: black");
    	// reset variables
        CLICKED = false;
        GAME_OVER = false;
        GAME_START = false;
        floors = new ArrayList<>();
        
    	if(firstEntry) {
    		// create two canvases
            Canvas canvas = new Canvas(DEF.SCENE_WIDTH, DEF.SCENE_HEIGHT);
            gc = canvas.getGraphicsContext2D();

            //Make background defined globally as an attribute
            // create a background
            background = DEF.IMVIEW.get("day_background");
            System.out.println(background);
            // create the game scene
            gameScene = new Group();
            gameScene.getChildren().addAll(background, canvas, scoreText);
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
    	//initialize pipe
    	/*
    	for(int i=0; i<DEF.PIPE_COUNT; i++) {
    		//have only i xPos
    		// but 2 y pos for up and down pipe
    		//
    		double dPipePos= DEF.PIPE_INITIAL_Y + PIPE_RANGE*Math.random();
    		double upPipePos= dPipePos - PIPE_Y_GAP;
    		
    		//int posX = i * DEF.FLOOR_WIDTH;
    		//int posY = DEF.SCENE_HEIGHT - DEF.FLOOR_HEIGHT;
    		
    		Sprite upPipe1 = new Sprite(D_PIPE_POS_X, posY, DEF.IMAGE.get("upipe1"));
    		Sprite upPipe2 = new Sprite(D_PIPE_POS_X, posY, DEF.IMAGE.get("upipe2"));
    		Sprite upPipe3 = new Sprite(D_PIPE_POS_X, posY, DEF.IMAGE.get("upipe3"));
    		Sprite upPipe4 = new Sprite(D_PIPE_POS_X, posY, DEF.IMAGE.get("upipe4"));
    		Sprite upPipe5 = new Sprite(D_PIPE_POS_X, posY, DEF.IMAGE.get("upipe5"));
    		
    		Sprite dpipe1 = new Sprite(D_PIPE_POS_X, posY, DEF.IMAGE.get("dpipe1"));
    		Sprite dpipe2 = new Sprite(D_PIPE_POS_X, posY, DEF.IMAGE.get("dpipe2"));
    		Sprite dpipe3 = new Sprite(D_PIPE_POS_X, posY, DEF.IMAGE.get("dpipe3"));
    		Sprite dpipe4 = new Sprite(D_PIPE_POS_X, posY, DEF.IMAGE.get("dpipe4"));
    		Sprite dpipe5 = new Sprite(D_PIPE_POS_X, posY, DEF.IMAGE.get("dpipe5"));
    		//Sprite floor = new Sprite(D_PIPE_POS_X, posY, DEF.IMAGE.get("floor1"));

    		upPipe1.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
    		upPipe2.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
    		upPipe3.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
    		upPipe4.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
    		upPipe5.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
    		dpipe1.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
    		dpipe2.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
    		dpipe3.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
    		dpipe4.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
    		dpipe5.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
    		
    		upPipe1.render(gc);
    		upPipe2.render(gc);
    		upPipe3.render(gc);
    		upPipe4.render(gc);
    		upPipe5.render(gc);
    		dpipe1.render(gc);
    		dpipe2.render(gc);
    		dpipe3.render(gc);
    		dpipe4.render(gc);
    		dpipe5.render(gc);

    		
    		//floors.add(floor);
    	}
    	*/
        
        // initialize blob
        blob = new Sprite(DEF.BLOB_POS_X, DEF.BLOB_POS_Y, DEF.IMAGE.get("bird1"));
        blob.render(gc);
        
        // initialize pipes
        uPipe = new Sprite(DEF.D_PIPE_POS_X, DEF.U_PIPE_POS_Y, DEF.IMAGE.get("upipe1"));
        uPipe.render(gc);
        
        dPipe = new Sprite(DEF.D_PIPE_POS_X, DEF.D_PIPE_POS_Y, DEF.IMAGE.get("dpipe1"));
        dPipe.render(gc);
        
        // initialize timer
        startTime = System.nanoTime();
        timer = new MyTimer();
        timer.start();
    }

    //timer stuff
    class MyTimer extends AnimationTimer {
    	
    	
    	int counter = 0;
    	
    	 @Override
    	 public void handle(long now) {   	
    	    	

    		 // time keeping
    	     elapsedTime = now - startTime;
    	     startTime = now;
    	     
    	     // clear current scene
    	     gc.clearRect(0, 0, DEF.SCENE_WIDTH, DEF.SCENE_HEIGHT);

    	     if (GAME_START) {
    	    	 switchBackground();

    	    	 // step1: update floor
    	    	 moveFloor();
    	    	 movePipe();
    	    	 System.out.println("In the animation");
    	    	 
    	    	 // step2: update blob
    	    	 moveBlob();
    	    	 checkCollision();
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
    			floors.get(i).update(DEF.SCENE_SHIFT_TIME);
    		}
    	 }
    	 
    	 //private void movePipe() { 
    	 //}
    	 
    	 
    	 // step2: update blob
    	 private void moveBlob() {
    		 System.out.println("Move blob");
    		 
			long diffTime = System.nanoTime() - clickTime;
			System.out.println(diffTime);
			System.out.println(DEF.BLOB_DROP_TIME);
			
			// blob flies upward with animation
			if (CLICKED && diffTime <= DEF.BLOB_DROP_TIME) {
				
				int imageIndex = Math.floorDiv(counter++, DEF.BLOB_IMG_PERIOD);
				imageIndex = Math.floorMod(imageIndex, DEF.BLOB_IMG_LEN);
				System.out.println("The image index: " + imageIndex);
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
    	 
    	 public void movePipe() {
    		uPipe.update(elapsedTime * DEF.NANOSEC_TO_SEC);
 			uPipe.render(gc);
 			
 			dPipe.update(elapsedTime * DEF.NANOSEC_TO_SEC);
			dPipe.render(gc);
    	 }
    	 
    	 public void checkCollision() {
    		 
    		// check collision  
			for (Sprite floor: floors) {
				GAME_OVER = GAME_OVER || blob.intersectsSprite(floor);
			}
			
			// end the game when blob hit stuff
			if (GAME_OVER) {
				showHitEffect(); 
				for (Sprite floor: floors) {
					floor.setVelocity(0, 0);
				}
				timer.stop();
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
	     
	     /**
	      *     	else if (!firstEntry && firstBackground % 2 == 1) {
    		Canvas canvas = new Canvas(DEF.SCENE_WIDTH, DEF.SCENE_HEIGHT);
            gc = canvas.getGraphicsContext2D();

            // create a background
            ImageView background = DEF.IMVIEW.get("night_background");
            System.out.println(background);
            // create the game scene
            gameScene = new Group();
            gameScene.getChildren().addAll(background, canvas, scoreText);
            firstBackground ++;
    	}
	      */
	     private void switchBackground() {
	    	 
//	    	 Text scoreText = new Text("Score: 0");
//			 Canvas canvas = new Canvas(DEF.SCENE_WIDTH, DEF.SCENE_HEIGHT);
//			 gc = canvas.getGraphicsContext2D();
	    	 
	    	 gc.drawImage(DEF.IMAGE.get("night_background"), 0, 0);
//			
			    // create a background
			background = DEF.IMVIEW.get("night_background");
			System.out.println("The background is working: ");
//			// create the game scene
//			gameScene = new Group();
//			gameScene.getChildren().addAll(background, canvas, scoreText); 
	     }
    	 
    } // End of MyTimer class

} // End of AngryFlappyBird Class

