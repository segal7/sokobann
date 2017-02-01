package controller.commands;

import view.View;
import controller.Controller;
import controller.MyController;
import controller.server.Server;
import model.Model;

public class ExitCommand extends Command{

	MyController _controller;
	View _view;
	Server _server;
	
	public ExitCommand(MyController c, View v,Server s) {
		this._controller = c;
		this._view = v;
		this._server = s;
	}
	
	@Override
	public void execute() throws Exception {
		if(_server != null)
			_server.sendMsgToClient("Exit");
		
	}

}
