package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;

import net.sf.juoserver.api.Decodable;
import net.sf.juoserver.api.MessageType;

@Decodable(code = UnicodeSpeechRequest.CODE)
public class UnicodeSpeechRequest extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	protected static final int CODE = 0x0AD;
	private static final int ENCODED_BITS = 0xC0;
	private MessageType messageType;
	private int hue;
	private int font;
	private String language;
	private String text;

	public UnicodeSpeechRequest(byte[] contents) {
		super(CODE, MessagesUtils.getLengthFromSecondAndThirdByte(contents));
		ByteBuffer bb = wrapContents(3, contents);

		byte rawType = bb.get();
		boolean encoded = (rawType & ENCODED_BITS) != 0;
		if (encoded) {
			throw new UnsupportedOperationException("Encoded messages not yet supported!");
		}
		
		messageType = EnumUtils.byCode(encoded? rawType ^ ENCODED_BITS : rawType, MessageType.class);
		hue = bb.getShort();
		font = bb.getShort();
		language = MessagesUtils.readString(bb, 4);
		text = MessagesUtils.readNullTerminatedUnicodeString(bb);
	}
	
	public UnicodeSpeechRequest(MessageType messageType, int hue, int font,
			String language, String text) {
		super(CODE, 12 + (text.length() + 1) * 2);
		
		this.messageType = messageType;
		this.hue = hue;
		this.font = font;
		this.language = language;
		this.text = text;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public int getHue() {
		return hue;
	}

	public int getFont() {
		return font;
	}

	public String getLanguage() {
		return language;
	}

	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return "UnicodeSpeechRequest [messageType=" + messageType + ", text="
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
		UnicodeSpeechRequest other = (UnicodeSpeechRequest) obj;
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
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}
}
