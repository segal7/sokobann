package view;

import common.Level;

public interface View {
	public void setLevel(Level l);
	public void setMoves(int moves);
	public void startTimer();
	public void start();
	public void close();
	public void Load();
	public void Save();
	
}
