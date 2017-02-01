package model;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Observable;

import common.Level;
import common.Point;
import model.data.GameObject;
import model.data.LevelLoader;
import model.data.LevelSaver;
import model.data.MyObjLevelLoader;
import model.data.MyObjLevelSaver;
import model.data.MyTextLevelLoader;
import model.data.MyTextLevelSaver;
import model.data.MyXMLLevelLoader;
import model.data.MyXMLLevelSaver;
import model.data.Sokoban;
import model.policy.Policy;

public class MyModel extends Observable implements Model{
	private Level _loadedLevel;
	private Policy _policy;
	private int _numOfMoves = 0;
	/////// TODO the step count of solving the level might be in the model, might be in the level. probably in the model
	
	private HashMap<String,LevelLoader> _loaders;
	private HashMap<String,LevelSaver> _savers;
	
	public MyModel(Policy pol){
		this._policy = pol;
		_numOfMoves = 0;
		//loader dictionary
		_loaders = new HashMap<String,LevelLoader>();
		_loaders.put("txt",new MyTextLevelLoader());
		_loaders.put("xml",new MyXMLLevelLoader());
		_loaders.put("obj", new MyObjLevelLoader());
		//saver dictionary
		_savers = new HashMap<String,LevelSaver>();
		_savers.put("txt", new MyTextLevelSaver());
		_savers.put("obj", new MyObjLevelSaver());
		_savers.put("xml", new MyXMLLevelSaver());
	}
	
	
	
	public int get_numOfMoves() {
		return _numOfMoves;
	}

	public void set_numOfMoves(int _numOfMoves) {
		this._numOfMoves = _numOfMoves;
	}



	public void setPolicy(Policy poc){
		this._policy = poc;
	}
	
	@Override
	public void movePlayer(String direction) throws Exception {

		if(_loadedLevel == null)
			throw new NullPointerException("there is no level to move in");
		Sokoban sokob = _loadedLevel.get_sokobans().get(0);
		if(sokob == null)
			throw new NullPointerException("there are no sokobans in this level to move");
		move(sokob,direction);
		_numOfMoves++;

	}

	@Override
	public Level get_Level() {
		return _loadedLevel;
	}

	@Override
	public void LoadLevel(String path) throws Exception {
		String filetype = "";
		int dotidx = path.length();
		if(path.indexOf(".")==-1)
			throw new FileNotFoundException("this file name is invalid");
		while(path.charAt(--dotidx)!='.');//find the index of the "." indicating the file type
		filetype = path.substring(dotidx+1, path.length());
		_loadedLevel = _loaders.get(filetype).loadLevel(new BufferedInputStream(new FileInputStream(path)));
		_numOfMoves = 0;
	}

	@Override
	public void SaveLevel(String path) throws Exception {
		if(_loadedLevel == null)
			throw new NullPointerException("no lvl to save");
		String filetype = "";
		int dotidx = path.length();
		if(path.indexOf(".")==-1)
			throw new FileNotFoundException("invalid file name");
		while(path.charAt(--dotidx)!='.');//find the index of the "." indicating the file type
		filetype = path.substring(dotidx+1, path.length());
		_savers.get(filetype).saveLevel(_loadedLevel, path.substring(0, dotidx));
		
	}
	
	
	
	private boolean move(GameObject obj,String direction) throws Exception{
		boolean willmove = false;
		Point currentLocation = obj.get_location();
		Point nextLocation = nextPoint(currentLocation,direction);
		GameObject nextObj = _loadedLevel.get_layout().get(nextLocation);
		
		if(_policy.canStepOn(obj, nextObj))
			willmove = true;
		else if(_policy.canPush(obj, nextObj))
			if(move(nextObj,direction))
				willmove = true;
		
		if(willmove)
		{
			GameObject host = obj.get_host();
			_loadedLevel.placeObject(obj,nextLocation);
			_loadedLevel.placeObject(host, currentLocation);
		}
		
		return willmove;
	}
	private Point nextPoint(Point p,String toDirection) throws Exception{
		Point res = new Point(p.x,p.y);
		switch (toDirection.toLowerCase())
		 {
		
		  case ("up") :
			  res.x = p.x; res.y = p.y-1;
			  break;
		  case ("down"):
			  res.x = p.x; res.y = p.y+1;
		       break;
		  case ("left"):
			  res.x = p.x-1; res.y = p.y;
		  	   break;
		  case ("right"):
			  res.x = p.x+1; res.y = p.y;
		  	   break;
		  default:
			  throw new Exception("no such direction");
		 }
		return res;
	}

	@Override
	public Policy get_policy() {
		return _policy;
	}	
	
}
