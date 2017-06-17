package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;

import net.sf.juoserver.api.MessageType;
import net.sf.juoserver.api.Item;
import net.sf.juoserver.api.Mobile;
import net.sf.juoserver.model.Clilocs;

public class ClilocMessage extends AbstractMessage {

	private static final int CODE = 0xC1;

	private static final long serialVersionUID = 1L;
	
	private int serialID;
	private int modelID;
	private int clilocId;
	private String displayName;
	private String parameters;
	private MessageType messageType;
	
	public ClilocMessage(Mobile mobile) {
		this(mobile.getSerialId(), mobile.getModelId(), Clilocs.PREFIX_NAME_SUFFIX.getCode(),
				mobile.getName(), mobile.getPrefixNameSuffix(), MessageType.Mobile);
	}
	
	public ClilocMessage(Item item) {
		this(item.getSerialId(), item.getModelId(), RevisionUtils.getItemClilocID(item), "", "", MessageType.Label);
	}
	
	public ClilocMessage(int serialID, int modelID, int clilocId, String displayName, String parameters, MessageType messageType) {
		super(CODE, computeLength(parameters));
		this.serialID = serialID;
		this.modelID = modelID;
		this.clilocId = clilocId;
		this.displayName = displayName;
		this.parameters = parameters;
		this.messageType = messageType;
	}
	
	@Override
	public ByteBuffer encode() {
		ByteBuffer bb = super.encode();
		bb.putShort((short) getLength());
		bb.putInt(serialID);
		bb.putShort((short) modelID);
		bb.put((byte) messageType.getCode());
		bb.putShort((short) 0x0B); // Hue (TODO: consider 0x3B2 instead)
		bb.putShort((short) 3); // Font
		bb.putInt(clilocId);
		bb.put( MessagesUtils.padString(displayName, 30) );
		MessagesUtils.putReverseUnicodeString(bb, parameters);
		bb.putShort((short) 0);
		return bb;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + clilocId;
		result = prime * result
				+ ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result
				+ ((messageType == null) ? 0 : messageType.hashCode());
		result = prime * result + modelID;
		result = prime * result
				+ ((parameters == null) ? 0 : parameters.hashCode());
		result = prime * result + serialID;
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
		ClilocMessage other = (ClilocMessage) obj;
		if (clilocId != other.clilocId)
			return false;
		if (displayName == null) {
			if (other.displayName != null)
				return false;
		} else if (!displayName.equals(other.displayName))
			return false;
		if (messageType != other.messageType)
			return false;
		if (modelID != other.modelID)
			return false;
		if (parameters == null) {
			if (other.parameters != null)
				return false;
		} else if (!parameters.equals(other.parameters))
			return false;
		if (serialID != other.serialID)
			return false;
		return true;
	}

	private static int computeLength(String parameters) {
		return 1 + 2 + 4 + 2 + 1 + 2 + 2 + 4 + 30 + parameters.length() * 2 + 2;
	}
	
}
