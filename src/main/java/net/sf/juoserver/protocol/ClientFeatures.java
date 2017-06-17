package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;
import java.util.Arrays;

import net.sf.juoserver.model.ClientFeature;

public class ClientFeatures extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	private static final int CODE = 0xB9;
	
	private ClientFeature[] features;
	public ClientFeatures(ClientFeature... features) {
		super(CODE, 3);
		this.features = features;
	}
	public ClientFeature[] getFeatures() {
		return features;
	}
	@Override
	public ByteBuffer encode() {
		ByteBuffer bb = super.encode();
		short featurez = 0;
		for (ClientFeature f : features) {
			featurez |= f.getValue();
		}
		bb.putShort(featurez);
		return bb;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(features);
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
		ClientFeatures other = (ClientFeatures) obj;
		if (!Arrays.equals(features, other.features))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return getName() + " [features=" + Arrays.toString(features) + "]";
	}
}
