package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import view.View;
import controller.commands.Command;
import controller.commands.DisplayCommand;
import controller.commands.ExitCommand;
import controller.commands.LoadCommand;
import controller.commands.MoveCommand;
import controller.commands.SaveCommand;
import controller.server.Server;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import model.Model;

public class MyController implements Observer{
	View _view;
	Model _model;
	Server _server;
	private Controller _controller;
	private Map<String, Command> _commands;
	
	private IntegerProperty numofmoves;
	
	public MyController(View v, Model m,Server s) {
		this._view = v;
		this._model = m;
		this._server = s;

		_controller = new Controller();
		//_controller.start();
		
		numofmoves = new SimpleIntegerProperty();
		
		initCommands();
	}

	public void start(){
		_controller.start();
		try {
			if(_server!=null)
				_server.runServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void stop(){
		_controller.stop();
		if(_server!= null)
		{
			System.out.println("shutting server down");
			_server.stop();
		}
	}

	public View getView(){ return _view; }
	public Model getModel() {return _model; }
	
	private void initCommands() {
		_commands = new HashMap<String, Command>();

		_commands.put("Display", new DisplayCommand(_model,_view,_server));
		_commands.put("Exit", new ExitCommand(this,_view,_server));
		_commands.put("Load", new LoadCommand(_model,_view));
		_commands.put("Save", new SaveCommand(_model,_view));
		_commands.put("Move", new MoveCommand(_model,_view));
	}

	@Override
	public void update(Observable o, Object arg) {// ags (obj) and than back in to (linkined list ) in case of an error
		
		if(o==_model)
			numofmoves.set(_model.get_numOfMoves());
		
		LinkedList<String> params = (LinkedList<String>) arg;
		String commandKey = params.removeFirst();
		Command c = _commands.get(commandKey);
		if (c == null) {
			return;
		}
		c.setParams(params);
		_controller.insertCommand(c);
	}
}
