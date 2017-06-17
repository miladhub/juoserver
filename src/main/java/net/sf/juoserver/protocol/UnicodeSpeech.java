package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;

import net.sf.juoserver.api.MessageType;
import net.sf.juoserver.api.Mobile;

public class UnicodeSpeech extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	private static final int CODE = 0xAE;
	private Mobile speaker;
	private MessageType messageType;
	private int hue;
	private int font;
	private String language;
	private String text;
	
	public UnicodeSpeech(Mobile speaker, UnicodeSpeechRequest request) {
		this(speaker, request.getMessageType(), request.getHue(), request.getFont(),
				request.getLanguage(), request.getText());
	}

	public UnicodeSpeech(Mobile speaker, MessageType messageType, int hue,
			int font, String language, String text) {
		super(CODE, computeLength(text));
		this.speaker = speaker;
		this.messageType = messageType;
		this.hue = hue;
		this.font = font;
		this.language = language;
		this.text = text;
	}
	
	@Override
	public ByteBuffer encode() {
		ByteBuffer bb = super.encode();
		bb.putShort((short) getLength());
		bb.putInt(speaker.getSerialId());
		bb.putShort((short) speaker.getModelId());
		bb.put((byte) messageType.getCode());
		bb.putShort((short) hue);
		bb.putShort((short) font);
		bb.put( MessagesUtils.padString(language, 4) );
		bb.put( MessagesUtils.padString(speaker.getName(), 30) );
		MessagesUtils.putUnicodeString(bb, text);
		bb.putShort( (short) 0 );
		return bb;
	}

	private static int computeLength(String text) {
		return 48 + MessagesUtils.getUnicodeNullTerminatedStringLength(text);
	}
	
	@Override
	public String toString() {
		return "UnicodeSpeech [messageType=" + messageType + ", text="
				+ text + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + font;
		result = prime * result + hue;
		result = prime * result
				+ ((language == null) ? 0 : language.hashCode());
		result = prime * result
				+ ((messageType == null) ? 0 : messageType.hashCode());
		result = prime * result + ((speaker == null) ? 0 : speaker.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
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
		UnicodeSpeech other = (UnicodeSpeech) obj;
		if (font != other.font)
			return false;
		if (hue != other.hue)
			return false;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (messageType != other.messageType)
			return false;
		if (speaker == null) {
			if (other.speaker != null)
				return false;
		} else if (!speaker.equals(other.speaker))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}
}
