package controller;

import logic.User;

public interface IClientServerCommunication {

	public void sendToServer(Object msg);
	public Object getServerMsg();
}
