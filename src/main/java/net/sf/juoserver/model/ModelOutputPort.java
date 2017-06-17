package net.sf.juoserver.model;

import net.sf.juoserver.api.Container;
import net.sf.juoserver.api.Item;
import net.sf.juoserver.api.MessageType;
import net.sf.juoserver.api.Mobile;
import net.sf.juoserver.api.Point3D;

/**
 * Model output boundary port. This interface is used by the model
 * to update the client about changes in the game.
 */
public interface ModelOutputPort {
	void mobileChanged(Mobile mobile);
	void mobileApproached(Mobile entered);
	void mobileSpoke(Mobile speaker, MessageType type, int hue, int font, String language, String text);
	void containerChangedContents(Container updatedContainer);
	void itemDragged(Item item, int amount, Mobile droppingMobile, int targetSerialId, Point3D targetPosition);
	void itemChanged(Item item, Point3D where);
	void mobileChangedClothes(Mobile mobile);
	void mobileChangedWarMode(Mobile mobile);
	void mobileAttacked(Mobile attacker);
	void mobileAttackFinished(Mobile enemy);
	void mobileDroppedCloth(Mobile mobile, Item droppedCloth);
}
