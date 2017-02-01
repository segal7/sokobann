package model;

import common.Level;
import model.policy.Policy;

public interface Model {
	void movePlayer(String direction) throws Exception;
	Level get_Level(); 
	Policy get_policy();
	void LoadLevel(String path) throws Exception;
	void SaveLevel(String path) throws Exception;
	int get_numOfMoves();
}
