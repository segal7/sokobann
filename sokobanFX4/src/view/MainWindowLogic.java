package view;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import common.Level;
import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;

public class MainWindowLogic extends Observable implements Initializable,View {
	
	Level l = null;
	/*@FXML
	LevelDisplayer displayer;*/
	
	@FXML
	Label time;
	@FXML
	Label steps;
	@FXML
	Label msg;
	@FXML
	LevelDisplayer displayer;
	
	
	private boolean up;
	private	boolean down;
	private boolean left;
	private boolean right;
	
	private int seconds = 0;
	private boolean B; 
	MediaPlayer mpplayer;
	private Timer timer = null;
	
	public MainWindowLogic(){
		up=false;
		down=false;
		left=false;
		right=false;
		//timer = new Timer();
		displayer = new LevelDisplayer();
		String path; 
		path = "./resources/music/The Simpsons 8 bit theme.mp3";
		Media media = new Media(new File(path).toURI().toString());
		mpplayer = new MediaPlayer(media);
		msg = new Label();
		B=false;
	}
	
	public void startTimer(){
		seconds = 0;
		if(timer == null){
			Timer timer = new Timer();
		    timer.scheduleAtFixedRate(new TimerTask() {
		        @Override
		        public void run() {
		            Platform.runLater(new Runnable() {
		                @Override
		                public void run() {
		                    time.setText("time: " + seconds + "sec");
		                    seconds++;
		                }
		            });
		        }
		    }, 0, 1000);
		}
	}
	
	@Override
	public void start(){
		System.out.println("start");
	}
	@Override
	public void Load()
	{
		LinkedList<String> args = new LinkedList<String>();
		FileChooser f= new FileChooser();
		f.setTitle("Load");
		f.setInitialDirectory(new File("./resources/levels"));
		File chosen= f.showOpenDialog(null);
		if(chosen!=null)
		{
			String path;
			path=chosen.getPath();
			args.add("Load");
			args.add(path);
			setChanged();
			notifyObservers(args);
		}
		startTimer();
	}
	public void UpChanger ()
	{
		up=true;
	}
	public void DownChanger ()
	{
		down=true;
	}
	public void LeftChanger ()
	{
		left=true;
	}
	public void RightChanger ()
	{
		right=true;
	}
	@Override
	public void Save()
	{
		LinkedList<String> args = new LinkedList<String>();
		FileChooser f= new FileChooser();
		f.setTitle("save");
		f.setInitialDirectory(new File("./resources/levels/"));
		File chosen= f.showSaveDialog(null);
		if(chosen!=null)
		{
			args.add("Save");
			args.add(chosen.getPath());
			setChanged();
			notifyObservers(args);
		}
	}
	 @Override
	 public void close()
	 {
		LinkedList<String> args = new LinkedList<String>();
		args.add("Exit");
		setChanged();
		notifyObservers(args); 
	 }
	 // music
	 public void Start_music()
	 {
		 B  = !B;
		 if(B)
		 {
			 String path; 
			 path = "./resources/music/The Simpsons 8 bit theme.mp3";
			 Media media = new Media(new File(path).toURI().toString());
			 mpplayer = new MediaPlayer(media);
			 mpplayer.setAutoPlay(true);
			 MediaView mv = new MediaView(mpplayer);
			 mpplayer.setCycleCount(Animation.INDEFINITE);
		 }
		 else
			 mpplayer.stop();
	 }
	 public void stop_music()
	 {
		 Platform.runLater(new Runnable() {
			public void run() 
			{
				Alert alert;
				alert= new Alert(AlertType.INFORMATION);
				alert.setContentText("You wish . Just kidding, there is a way to stop it >;)");
				alert.setTitle("MMMMMWWWUUUHAHAHAHAHAHAHAHAHAHA");
				alert.setHeaderText(null);
				alert.showAndWait();
				
			}
		});
	
	 }
	 
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		displayer.setLevel(this.l);
		displayer.addEventFilter(MouseEvent.MOUSE_CLICKED, (e)->displayer.requestFocus());
		displayer.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				LinkedList<String> args = new LinkedList<String>();
				args.add("Move");
					
				if(event.getCode().toString().toLowerCase().equals(displayer.getUp_Key().toLowerCase())){
					args.add("Up");
					setChanged();
					notifyObservers(args);
					
				}
				else if(event.getCode().toString().toLowerCase().equals(displayer.getDown_Key().toLowerCase())){
					args.add("Down");
					setChanged();
					notifyObservers(args);
					
				}
				else if(event.getCode().toString().toLowerCase().equals(displayer.getRight_Key().toLowerCase())){
					args.add("Right");
					setChanged();
					notifyObservers(args);
					
				}
				else if(event.getCode().toString().toLowerCase().equals(displayer.getLeft_Key().toLowerCase())){
					args.add("Left");
					setChanged();
					notifyObservers(args);
					
				}
				
				
				if(up)
				{
					displayer.setUp_Key(event.getCode().toString());
					up = false;
				}
				if(down){
					displayer.setDown_Key(event.getCode().toString());
					down = false;
				}
				if(left){
					displayer.setLeft_Key(event.getCode().toString());
					left = false;
				}
				if(right){
					displayer.setRight_Key(event.getCode().toString());
					right = false;
				}
			}
		});
		
	}

	public void setMoves(int moves) {
		Platform.runLater(new Runnable() { @Override
			public void run() { steps.setText("steps: " + moves); } });
	}
	
	@Override
	public void setLevel(Level l) {
		this.l = l;
		displayer.setLevel(this.l);
		displayer.redraw();
	}
}
