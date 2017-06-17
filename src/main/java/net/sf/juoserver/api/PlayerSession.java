package net.sf.juoserver.api;

import java.util.List;

public interface PlayerSession extends IntercomListener {
	List<String> getCharacterNames();
	void selectCharacterById(int charId);
	GameStatus startGame();
	Mobile getMobile();	
	void move(Direction direction, boolean running);
	void speak(MessageType messageType, int hue, int font, String language, String text);
	void dropItem(int itemSerial, boolean droppedOnTheGround, int targetContainerSerial, Point3D targetPosition);
	void wearItemOnMobile(Layer layer, int itemSerialId);
	void toggleWarMode(boolean war);
	void attack(Mobile enemy);
}
