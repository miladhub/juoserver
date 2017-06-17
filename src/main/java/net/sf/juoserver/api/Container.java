package net.sf.juoserver.api;

import java.util.List;
import java.util.Map;

public interface Container extends Item {
	List<? extends Item> getItems();

	int getGumpId();
	
	Point2D getPositionWithinContainer(Item item);

	void removeItem(Item item);

	void addItem(Item item, Point2D positionInContainer);

	Map<Item, Point2D> getItemsPositions();
}