package controller.commands;

import controller.server.Server;
import model.Model;
import model.data.TextLevelDisplayer;
import view.View;

public class DisplayCommand extends Command{
	Model model;
	View view;
	Server server;
	
	public DisplayCommand(Model M,View V,Server S) {

		this.model=M;
		this.view=V;
		this.server = S;
	}


	@Override
	public void execute()throws Exception {
		view.setLevel(model.get_Level());
		view.setMoves(model.get_numOfMoves());
		TextLevelDisplayer tld = new TextLevelDisplayer();
		server.sendMsgToClient(tld.display(model.get_Level()));
	}

}
