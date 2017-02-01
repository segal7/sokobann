package common;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import model.data.Box;
import model.data.GameObject;
import model.data.Sokoban;
import model.data.Target;
import model.data.Wall;


public class Level implements Serializable {
	//data members:
	private int _score;
	private HashMap<Point,GameObject> _layout;
	private int XEdge = 0 , YEdge = 0;
	
	private ArrayList<Box> _boxes;
	private ArrayList<Target> _targets;
	private ArrayList<Sokoban> _sokobans;
	private ArrayList<Wall> _walls;
	//Default contractor
	public Level()
	{
		_layout = new HashMap<Point,GameObject>();
		_boxes = new ArrayList<Box>();
		_targets = new ArrayList<Target>();
		_sokobans = new ArrayList<Sokoban>();
		_walls = new ArrayList<Wall>();
	}
	
	//methods:
	//setters and getters

	public int get_score() {
		return _score;
	}
	public int getXEdge() {
		return XEdge;
	}
	public int getYEdge() {
		return YEdge;
	}
	public void setXEdge(int xEdge) {
		XEdge = xEdge;
	}
	public void setYEdge(int yEdge) {
		YEdge = yEdge;
	}

	public void set_score(int score) {
		this._score = score;
	}
	public HashMap<Point, GameObject> get_layout() {
		return _layout;
	}
	public void set_layout(HashMap<Point, GameObject> _layout) {
		this._layout = _layout;
	}
	public ArrayList<Box> get_boxes() {
		return _boxes;
	}
	public void set_boxes(ArrayList<Box> _boxes) {
		this._boxes = _boxes;
	}
	public ArrayList<Target> get_targets() {
		return _targets;
	}
	public void set_targets(ArrayList<Target> _targets) {
		this._targets = _targets;
	}
	public ArrayList<Sokoban> get_sokobans() {
		return _sokobans;
	}
	public void set_sokobans(ArrayList<Sokoban> _sokobans) {
		this._sokobans = _sokobans;
	}
	public ArrayList<Wall> get_walls() {
		return _walls;
	}
	public void set_walls(ArrayList<Wall> _walls) {
		this._walls = _walls;
	}
	
	public void updateMaxEdges(){
		int maxX = 0,maxY = 0;
		for(Point p: _layout.keySet())
		{
			if(_layout.get(p) != null)
			{
				if(maxX < p.x)
					maxX = p.x;
				if(maxY < p.y)
					maxY = p.y;
			}
		}
		XEdge = maxX+1;
		YEdge = maxY+1;
	}
	
	public void placeObject(GameObject obj,Point point){
			
		if(obj == null)
		{
			_layout.put(point, null);
			return;
		}
		obj.set_host(_layout.get(point));
		_layout.put(point, obj);
		obj.set_location(point);
		
		//////////add to appropriate list/////////////
		switch(obj.getClass().getSimpleName()){
		case "Box":
			if(!_boxes.contains(obj))
				_boxes.add((Box) obj); 
			break;
		case "Target": 
			if(!_targets.contains(obj))
				_targets.add((Target)obj); 
			break;
		case "Sokoban": 
			if(!_sokobans.contains(obj))
				_sokobans.add((Sokoban)obj); 
			break;
		case "Wall": 
			if(!_walls.contains(obj))
				_walls.add((Wall)obj); 
			break;
		}
		if(point.x >= XEdge)
			XEdge = point.x + 1;
		if(point.y >= YEdge)
			YEdge = point.y + 1;
	}
	public void removeObject(Point position){
		_layout.remove(position);
	}
	
	public int boxesInPlace(){
		int count = 0;
		for(Box b:_boxes){
			for(Target t:_targets)
				if(t.get_location().equals(b.get_location()))
					count++;
		}
		
		return count;
	}
	public int boxesNotInPlace(){
		return _boxes.size() - boxesInPlace();
	}
	
}
