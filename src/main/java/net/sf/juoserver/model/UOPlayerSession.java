package net.sf.juoserver.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.juoserver.api.Account;
import net.sf.juoserver.api.CharacterStatus;
import net.sf.juoserver.api.Container;
import net.sf.juoserver.api.Core;
import net.sf.juoserver.api.Direction;
import net.sf.juoserver.api.GameStatus;
import net.sf.juoserver.api.InterClientNetwork;
import net.sf.juoserver.api.Item;
import net.sf.juoserver.api.JUoEntity;
import net.sf.juoserver.api.Layer;
import net.sf.juoserver.api.LightLevels;
import net.sf.juoserver.api.MapTile;
import net.sf.juoserver.api.MessageType;
import net.sf.juoserver.api.Mobile;
import net.sf.juoserver.api.PlayerSession;
import net.sf.juoserver.api.Point3D;
import net.sf.juoserver.api.Season;

public class UOPlayerSession implements PlayerSession {
	private final Core core;
	private final Account account;
	private final Set<Mobile> mobilesInRange = new HashSet<Mobile>();
	private final ModelOutputPort serverResponseListener;
	private final InterClientNetwork network;
	
	private Mobile mobile;
	
	protected Mobile attacking;
	protected final Set<Mobile> attackingMe = new HashSet<Mobile>(); 
	
	public UOPlayerSession(Core core, Account account, ModelOutputPort serverResponseListener,
			InterClientNetwork network) {
		super();
		this.core = core;
		this.account = account;
		this.serverResponseListener = serverResponseListener;
		this.network = network;
	}

	@Override
	public List<String> getCharacterNames() {
		List<String> names = new ArrayList<String>();
		for (int serialId : account.getCharactersSerials()) {
			Mobile mobile = core.findMobileByID(serialId);
			names.add(mobile.getName());
		}
		return names;
	}

	@Override
	public void selectCharacterById(int charPosition) {
		mobile = core.findMobileByID( account.getCharacterSerialIdByPosition(charPosition) );
	}

	@Override
	public Mobile getMobile() {
		return mobile;
	}

	@Override
	public GameStatus startGame() {
		return new UOGameStatus(LightLevels.Day, Season.Spring);
	}

	@Override
	public void move(Direction direction, boolean running) {
		boolean onlyChangingDirection = isOnlyChangingDirection(direction);
		
		mobile.setDirection(direction);
		mobile.setRunning(running);
		
		if (!onlyChangingDirection) {
			mobile.moveForward();
		}
		
		MapTile tile = core.getTile(mobile.getX(), mobile.getY());
		mobile.setZ( tile.getZ() );
		network.notifyOtherMobileMovement(mobile);
	}

	private boolean isOnlyChangingDirection(Direction direction) {
		return direction != mobile.getDirection();
	}

	@Override
	public void onOtherMobileMovement(Mobile moving) {
		if (moving.equals( mobile )) {
			// TODO: ignore also if mobile has not LOS towards otherMobile
			return; // Ignore self-notifying messages
		}
		
		// If the moving mobile is within range, draw it
		if (!mobilesInRange.contains( moving )) {
			// TODO: do not enter here without LOS or if they're too far?
			// TODO: remove mobiles from range too
			onEnteredRange(moving, mobile);
			
			// Instruct the moving mobile's client to do the same
			network.notifyEnteredRange(mobile, moving);
		}
		
		// Always send an update
		serverResponseListener.mobileChanged(moving);
	}

	@Override
	public void onEnteredRange(Mobile entered, JUoEntity target) {
		if (!target.equals( mobile )) {
			return; // Point-to-point semantics
		}
		
		mobilesInRange.add( entered );
		serverResponseListener.mobileApproached(entered);
	}

	@Override
	public void speak(MessageType messageType, int hue, int font, String language, String text) {
		network.notifyMobileSpeech(mobile, messageType, hue, font, language, text);
		// Don't do anything just right now. The onOtherMobileSpeech()
		// method will bring the message to everybody, including myself.
		
		// NOTE on semantics: we could have returned a UnicodeSpeech here,
		// and skip self-notifications on the onOtherMobileSpeech() method,
		// like we do in the onOtherMobileMovement() method.
	}
	
	// This will be called both on the speaker's controller (thus letting it
	// hear what its mobile is saying) and the other listener controllers,
	// thus letting the others hear it.
	@Override
	public void onOtherMobileSpeech(Mobile speaker, MessageType type, int hue,
			int font, String language, String text) {
		serverResponseListener.mobileSpoke(speaker, type, hue, font, language, text);
	}

	@Override
	public void dropItem(int itemSerial, boolean droppedOnTheGround,
			int targetContainerSerial, Point3D targetPosition) {
		Item droppedItem = core.findItemByID(itemSerial);
		
		if (mobile.removeItem(droppedItem)) {
			network.notifyDroppedCloth(mobile, droppedItem);
		}
		
		removeFromSourceContainer(droppedItem);
		
		if (!droppedOnTheGround) { 
			addToTargetContainer(droppedItem, targetContainerSerial, targetPosition);
		}
		
		if (droppedOnTheGround) {
			network.notifyItemDropped(mobile, droppedItem, droppedOnTheGround? 0 : targetContainerSerial,
					targetPosition.getX(), targetPosition.getY(), targetPosition.getZ());
		}
	}
	
	private void removeFromSourceContainer(Item droppedItem) {
		Container sourceContainer = core.findContainerByContainedItem(droppedItem);
		if (sourceContainer != null) {
			sourceContainer.removeItem(droppedItem);
			core.removeItemFromContainer(droppedItem);
			serverResponseListener.containerChangedContents(sourceContainer);
		}
	}

	private void addToTargetContainer(Item droppedItem, int targetContainerSerial, Point3D targetPosition) {
		Container targetContainer = (Container) core.findItemByID(targetContainerSerial);
		targetContainer.addItem(droppedItem, new Position(targetPosition.getX(), targetPosition.getY()));
		core.addItemToContainer(droppedItem, targetContainer);
		serverResponseListener.containerChangedContents(targetContainer);
	}

	@Override
	public void onItemDropped(Mobile droppingMobile, Item item,
			int targetSerialId, int targetX, int targetY, int targetZ) {
		if (!mobile.equals(droppingMobile)) {
			serverResponseListener.itemDragged(item, 1, droppingMobile, targetSerialId, new PointInSpace(targetX, targetY, targetZ));
		}
		serverResponseListener.itemChanged(item, new PointInSpace(targetX, targetY, targetZ));
	}

	@Override
	public void wearItemOnMobile(Layer layer, int itemSerialId) {
		Item item = core.findItemByID(itemSerialId);
		
		mobile.setItemOnLayer(layer, item);
		removeFromSourceContainer(item);
		
		network.notifyChangedClothes(mobile);
	}
	
	@Override
	public void onChangedClothes(Mobile wearingMobile) {
		serverResponseListener.mobileChangedClothes(wearingMobile);
	}

	@Override
	public void onDroppedCloth(Mobile mobile, Item droppedCloth) {
		serverResponseListener.mobileDroppedCloth(mobile, droppedCloth);
	}
	
	@Override
	public void toggleWarMode(boolean isWarOn) {
		if (isWarOn) {
			mobile.setCharacterStatus(CharacterStatus.WarMode);			
		} else {
			if (isAttackingSomeone()) {
				network.notifyAttackFinished(mobile, attacking);
			}
			mobile.setCharacterStatus(CharacterStatus.Normal);
		}
		network.notifyChangedWarMode(mobile);		
	}
	
	private boolean isAttackingSomeone() {
		return attacking!=null;
	}
	
	@Override
	public void onChangedWarMode(Mobile mobile) {
		serverResponseListener.mobileChangedWarMode(mobile);
	}

	@Override
	public void attack(Mobile attacked) {		
		// TODO identify player that started the fight
		network.notifyAttacked(mobile, attacked);
	}
	
	@Override
	public void onAttacked(Mobile attacker, Mobile attacked) {			
		if (isAttacked(attacked)) {
			attackingMe.add(attacker);			
			serverResponseListener.mobileAttacked(attacker);
		} else {
			if (isAttacker(attacker)) {
				attacking = attacked;
			}
		}		
	}	
	
	@Override
	public void onAttackFinished(Mobile attacker, Mobile attacked) {		
		if (isAttacker(attacker)) {
			attacking = null;
			if (imNotUnderAttack(attacked)) {
				serverResponseListener.mobileAttackFinished(attacked);
			}			
		} else {
			if (isAttacked(attacked)) {		
				attackingMe.remove(attacker);
				if (imNotAttacking()) {
					serverResponseListener.mobileAttackFinished(attacker);
				}				
			}
		}
	}
	
	private boolean imNotUnderAttack(Mobile attacked) {
		return !attackingMe.contains(attacked);
	}
	
	private boolean imNotAttacking() {
		return attacking == null;
	}
	
	private boolean isAttacked(Mobile attacked) {
		return mobile.equals(attacked);
	}
	
	private boolean isAttacker(Mobile attacker) {
		return mobile.equals(attacker);
	}
	
}
