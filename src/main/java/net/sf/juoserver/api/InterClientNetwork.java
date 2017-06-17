package net.sf.juoserver.api;

public interface InterClientNetwork {
	void notifyOtherMobileMovement(Mobile movingMobile);
	void notifyEnteredRange(Mobile enteredMobile, JUoEntity targetMobile);
	void notifyMobileSpeech(Mobile speaker, MessageType type, int hue, int font, String language, String text);
	void notifyChangedClothes(Mobile wearingMobile);
	void notifyItemDropped(Mobile droppingMobile, Item item, int targetSerialId, int targetX, int targetY, int targetZ);
	void notifyChangedWarMode(Mobile mobile);
	void notifyDroppedCloth(Mobile mobile, Item droppedCloth);
	void notifyAttacked(Mobile attacker, Mobile attacked);
	void notifyAttackFinished(Mobile attacker, Mobile attacked);
	void removeIntercomListener(IntercomListener listener);
	void addIntercomListener(IntercomListener listener);
}
