package net.sf.juoserver.api;


/**
 * Definition of an actor interested in receiving
 * notifications about other mobiles from the other
 * connected clients.
 */
public interface IntercomListener {
	void onOtherMobileMovement(Mobile movingMobile);
	void onEnteredRange(Mobile entered, JUoEntity target);
	void onOtherMobileSpeech(Mobile speaker, MessageType type, int hue, int font, String language, String text);
	void onChangedClothes(Mobile wearingMobile);
	void onItemDropped(Mobile droppingMobile, Item item, int targetSerialId, int targetX, int targetY, int targetZ);
	void onChangedWarMode(Mobile mobile);
	void onAttacked(Mobile attacker, Mobile attacked);
	void onAttackFinished(Mobile attacker, Mobile attacked);
	void onDroppedCloth(Mobile mobile, Item droppedCloth);	
}
