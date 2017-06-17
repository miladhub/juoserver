package net.sf.juoserver.protocol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.sf.juoserver.api.Account;
import net.sf.juoserver.api.ClientMovementTracker;
import net.sf.juoserver.api.Container;
import net.sf.juoserver.api.Core;
import net.sf.juoserver.api.GameStatus;
import net.sf.juoserver.api.InterClientNetwork;
import net.sf.juoserver.api.IntercomException;
import net.sf.juoserver.api.Item;
import net.sf.juoserver.api.LightLevels;
import net.sf.juoserver.api.LoginManager;
import net.sf.juoserver.api.Message;
import net.sf.juoserver.api.MessageType;
import net.sf.juoserver.api.Mobile;
import net.sf.juoserver.api.PlayerSession;
import net.sf.juoserver.api.Point3D;
import net.sf.juoserver.api.Season;
import net.sf.juoserver.api.Skill;
import net.sf.juoserver.model.City;
import net.sf.juoserver.model.ClientFeature;
import net.sf.juoserver.model.Flag;
import net.sf.juoserver.model.ModelOutputPort;
import net.sf.juoserver.model.PlayingCharacter;
import net.sf.juoserver.model.UOPlayerSession;
import net.sf.juoserver.protocol.GeneralInformation.SubcommandType;
import net.sf.juoserver.protocol.SkillUpdate.SkillUpdateType;

import org.apache.log4j.Logger;

/**
 * Game controller. A different instance of this class will be associated
 * to each client's session.
 */
public class GameController extends AbstractProtocolController implements ModelOutputPort {
	private static final Logger logger = Logger.getLogger(GameController.class);
	
	private static final String CONTROLLER_ID_POSTFIX = "-controller";
	
	private final String controllerId;
	private final Core core;
	private final ProtocolIoPort clientHandler;
	private final ClientMovementTracker movementTracker;
	private final ItemManager itemManager = new ItemManager();
	private final LoginManager loginManager;
	private final InterClientNetwork network;
	
	private ClientVersion clientVersion;
	private PlayerSession session;

	public GameController(String clientName, ProtocolIoPort clientHandler, Core core,
			ClientMovementTracker movementTracker, LoginManager loginManager, InterClientNetwork network) {
		super();
		this.controllerId = clientName + CONTROLLER_ID_POSTFIX;
		this.clientHandler = clientHandler;
		this.core = core;
		this.movementTracker = movementTracker;
		this.loginManager = loginManager;
		this.network = network;
	}
	
	// This message is sent in the second connection right after the new seed
	public CharacterList handle(ServerLoginRequest request) throws IOException {
		Account account = loginManager.getAuthorizedAccount(request.getAuthenticationKey());
		if (account == null) {
			clientHandler.deactivate();
			return null;
		}
		
		session = new UOPlayerSession(core, account, this, network);
		network.addIntercomListener(session);
		
		List<String> names = session.getCharacterNames();
		List<PlayingCharacter> chars = new ArrayList<PlayingCharacter>();
		for (String name : names) {
			chars.add(new PlayingCharacter(name, account.getPassword()));
		}
		return new CharacterList(chars, new ArrayList<City>(),
				// TODO: create constants/enum for the following two flags
				new Flag(0x14),   // 1-char only 
				new Flag(0x1A8)); // Mondain's Legacy
	}
	
	public Message handle(CharacterSelect request) {
		session.selectCharacterById(request.getCharId());
		return new ClientVersion();
	}
	
	// TODO: complete this handler - see messages SERVER_74,75 in sample_login
	// see PacketHandlers#DoLogin() [@ RunUo source]
	// may require reading the map files
	public List<Message> handle(ClientVersion clientVersion) {
		if (this.clientVersion != null) {
			return null;
		}
		this.clientVersion = clientVersion;
		logger.info("Client version: " + clientVersion.getClientVersion());
		GameStatus status = session.startGame();
		return sendGameStatus(session.getMobile(), status);
	}

	private List<Message> sendGameStatus(Mobile mobile, GameStatus status) {
		LightLevels lightLevel = status.getLightLevel();
		Season season = status.getSeason();
		
		List<Message> response = new ArrayList<Message>( asList(
			new LoginConfirm(mobile.getSerialId(), (short) mobile.getModelId(),
					(short) mobile.getX(), (short) mobile.getY(), (byte) mobile.getZ(),
					(byte) mobile.getDirection().getCode(), (byte) mobile.getNotoriety().getCode(),
					(short) 7168, (short) 4096),
			// TODO: don't hard-code the map size (7168 x 4096) and index (0), see Core#init() [@ RunUo source]
			new GeneralInformation(new GeneralInformation.SetCursorHueSetMap((byte) 0)),
			new SeasonalInformation(season, true),
			new DrawGamePlayer(mobile),
			new CharacterDraw(mobile),
			new OverallLightLevel(new UOProtocolLightLevel(lightLevel)),
			new PersonalLightLevel(mobile.getSerialId(), new UOProtocolLightLevel(lightLevel)),
			new ClientFeatures(ClientFeature.T2A, ClientFeature.UOR),
			new CharacterWarmode((byte) 0),
			new LoginComplete()
		) );
		response.addAll( mobileObjectsRevisions( mobile ) );
		return response;
	}
	
	private Collection<? extends Message> mobileObjectsRevisions(Mobile mobile) {
		List<Message> revisions = new ArrayList<Message>();
		for (Item item : mobile.getItems().values()) {
			revisions.add( new ObjectRevision(item) );
		}
		return revisions;
	}
	
	/**
	 * Answers with the same sequence, increments the internal sequence (0 -->
	 * 256 and then always restart from 1).
	 * 
	 * @param request movement request
	 * @return movement response
	 * @throws IntercomException in case an inter-client error occurs 
	 */
	public List<Message> handle(MoveRequest request) {
		if (movementTracker.getExpectedSequence() == request.getSequence()) {
			session.move(request.getDirection(), request.isRunning());
			
			movementTracker.incrementExpectedSequence();
			
			return asList( new MovementAck(request.getSequence(), session.getMobile().getNotoriety()) );
		} else {
			logger.warn("Movement request rejected - expected sequence: "
					+ movementTracker.getExpectedSequence() + ", actual sequence: "
					+ request.getSequence());
			return asList( new MovementReject(request.getSequence(), session.getMobile().getX(), session.getMobile().getY(),
					session.getMobile().getZ(), session.getMobile().getDirectionWithRunningInfo()) );
		}
	}
	
	/**
	 * Handles possible client's position synchronization requests.
	 * 
	 * @param synchRequest synchronization request
	 * @return messages suitable for synchronizing the client's position
	 */
	public List<Message> handle(MovementAck synchRequest) {
		// TODO: this handler is not actually needed (i.e., called) yet
		return asList(new DrawGamePlayer(session.getMobile()), new CharacterDraw(session.getMobile()));
	}
	
	public void handle(UnicodeSpeechRequest request) {
		session.speak(request.getMessageType(), request.getHue(),
				request.getFont(), request.getLanguage(), request.getText());
	}
	
	@Override
	public void mobileSpoke(Mobile speaker, MessageType type, int hue, int font,
			String language, String text) {
		try {
			clientHandler.sendToClient( new UnicodeSpeech(speaker, type, hue,
					font, language, text) );
		} catch (IOException e) {
			throw new IntercomException(e);
		}
	}
	
	/**
	 * Handles tooltips requests.
	 * 
	 * @param mcr request
	 * @return tooltip information
	 */
	public List<Message> handle(MegaClilocRequest mcr) {
		List<Message> msgs = new ArrayList<Message>();
		for (int querySerial : mcr.getQuerySerials()) {
			// TODO: distinguish between items and mobiles as in handle(LookRequest)
			Mobile mobile = core.findMobileByID( querySerial );
			if (mobile != null) {
				msgs.add(MegaClilocResponse.createMobileMegaClilocResponse(mobile));
			} else {
				Item item = core.findItemByID( querySerial );
				msgs.add(MegaClilocResponse.createItemMegaClilocResponse(item));
			}
		}
		return msgs;
	}

	public Message handle(PingPong ping) {
		return new PingPong(ping.getSequenceNumber());
	}
	
	//TODO: complete this handler - see CLIENT_77 in sample_login
	public Message handle(GetPlayerStatus gps) {
		switch (gps.getRequest()) {
		case GodClient:
			//TODO: check if clients has the right privileges, kick otherwise
			return null;
		case Stats:
			return new StatusBarInfo(core.findMobileByID(gps.getSerial()));
		case Skills:
			return new SkillUpdate(SkillUpdateType.FullListWithCap,
					core.findMobileByID(gps.getSerial()).getSkills().toArray(new Skill[0]));
		default:
			return null;
		}
	}
	
	public List<? extends Message> handle(DoubleClick doubleClick) {
		if (doubleClick.isPaperdollRequest()) {
			Mobile mob = core.findMobileByID(doubleClick.getObjectSerialId());
			return Arrays.asList(new Paperdoll(doubleClick.getObjectSerialId(),
					mob.getName() + ", " + mob.getTitle(),
					false, false));
		} else {
			Item item = core.findItemByID(doubleClick.getObjectSerialId());
			return itemManager.use(item);
		}
	}
	
	public void handle(GeneralInformation info) {
		Subcommand<GeneralInformation, SubcommandType> sc = info.getSubCommand();
		if (sc != null) {
			logger.debug(sc);
		}
	}
	
	public void handle(SpyOnClient spyOnClient) {} // Ignore this message
	
	// ======================== items ========================
	
	public Message handle(LookRequest lookRequest) {
		Mobile mobile = core.findMobileByID( lookRequest.getSerialId() );
		if (mobile != null) {
			return new ClilocMessage(mobile);
		} else {
			Item item = core.findItemByID( lookRequest.getSerialId() );
			return new ClilocMessage(item);
			// TODO: handle items' stacks too
		}
	}
	
	public void handle(final DropItem dropItem) throws IOException {
		session.dropItem(dropItem.getItemSerial(), dropItem.isDroppedOnTheGround(),
				dropItem.getTargetContainerSerial(), dropItem.getTargetPosition());
	}
	
	@Override
	public void containerChangedContents(Container updatedContainer) {
		try {
			clientHandler.sendToClient(new ContainerItems(updatedContainer));
		} catch (IOException e) {
			throw new ProtocolException(e);
		}
	}

	@Override
	public void itemDragged(Item item, int amount, Mobile droppingMobile,
			int targetSerialId, Point3D targetPosition) {
		try {
			clientHandler.sendToClient(new DragItem(item, amount, droppingMobile, targetSerialId, targetPosition.getX(),
					targetPosition.getY(), targetPosition.getZ()));
		} catch (IOException e) {
			throw new ProtocolException(e);
		}
	}

	@Override
	public void itemChanged(Item item, Point3D where) {
		try {
			clientHandler.sendToClient(new ObjectInfo(item, where.getX(), where.getY(), where.getZ()), new ObjectRevision(item));
		} catch (IOException e) {
			throw new ProtocolException(e);
		}
	}

	public void handle(PickUpItem pickUpItem) {
	}
	
	public void handle(WearItem wearItem) {
		session.wearItemOnMobile(wearItem.getLayer(), wearItem.getItemSerialId());
	}
	
	@Override
	public void mobileChangedClothes(Mobile wearingMobile) {
		try {
			clientHandler.sendToClient(new CharacterDraw(wearingMobile));
		} catch (IOException e) {
			throw new IntercomException(e);
		}
	}
	
	// ====================== COMBAT =========================
	public List<Message> handle(WarMode warMode) {
		session.toggleWarMode(warMode.isWar());
		return asList(warMode, new CharacterDraw(session.getMobile()));
	}
	
	@Override
	public void mobileChangedWarMode(Mobile mobile) {
		try {								
			clientHandler.sendToClient(new CharacterDraw(mobile));
		} catch (IOException e) {
			throw new IntercomException(e);
		}		
	}
	
	public List<Message> handle(AttackRequest attackRequest) {
		Mobile attacked = core.findMobileByID(attackRequest.getMobileID());		
		session.attack(attacked);				
		return asList(new AttackOK(attacked), 
				new FightOccurring(session.getMobile(), attacked),
				new AttackSucceed(attacked));
	}
	
	@Override
	public void mobileAttacked(Mobile attacker) {
		try {
			clientHandler.sendToClient(new AttackOK(attacker.getSerialId()), 
					new AttackSucceed(attacker),
					new FightOccurring(session.getMobile(), attacker));
		} catch (IOException e) {
			throw new IntercomException(e);
		}		
	}
	
	@Override
	public void mobileAttackFinished(Mobile attacker) {		
		try {								
			clientHandler.sendToClient(new AttackSucceed(0));
		} catch (IOException e) {
			throw new IntercomException(e);
		}
	}
	
	// ====================== END COMBAT ======================
	
	public void handle(GenericAOSCommands commands) {
		// TODO handle GenericAOSCommands
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
		+ ((controllerId == null) ? 0 : controllerId.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameController other = (GameController) obj;
		if (controllerId == null) {
			if (other.controllerId != null)
				return false;
		} else if (!controllerId.equals(other.controllerId))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return controllerId;
	}

	public void setSession(PlayerSession session) {
		this.session = session;
	}

	@Override
	public void mobileChanged(Mobile mobile) {
		try {
			clientHandler.sendToClient(new UpdatePlayer(mobile));
		} catch (IOException e) {
			throw new ProtocolException(e);
		}
	}

	@Override
	public void mobileApproached(Mobile mobile) {
		try {
			clientHandler.sendToClient(new CharacterDraw(mobile), new ObjectRevision(mobile));
		} catch (IOException e) {
			throw new ProtocolException(e);
		}
	}

	@Override
	public void mobileDroppedCloth(Mobile mobile, Item droppedCloth) {
		try {
			clientHandler.sendToClient(new DeleteItem(droppedCloth.getSerialId()));
			// TODO: send sound (0x54) too - e.g., 54 01 00 57 00 00 0F 41  01 C8 00 00
		} catch (IOException e) {
			throw new ProtocolException(e);
		}
	}
}
