package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import net.sf.juoserver.api.Decodable;
import net.sf.juoserver.api.Mobile;

@Decodable(code=MegaClilocRequest.CODE)
public class MegaClilocRequest extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	protected static final int CODE = 0xD6;
	private List<Integer> querySerials = new ArrayList<Integer>();

	public MegaClilocRequest(byte[] contents) {
		super(CODE, MessagesUtils.getLengthFromSecondAndThirdByte(contents));
		ByteBuffer bb = wrapContents(3, contents);
		if ((getLength() - 3) % 4 != 0) {
			throw new IllegalStateException("Bad MegaCliloc message: " +
					MessagesUtils.getHexString(contents));
		}
		int nQueries = (getLength() - 3) / 4;
		for (int i = 0; i < nQueries; i++) {
			querySerials.add( bb.getInt() );
		}
	}
	
	public MegaClilocRequest(Mobile mobile) {
		super(CODE, 7);
		querySerials.add( mobile.getSerialId() );
	}

	public List<Integer> getQuerySerials() {
		return querySerials;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((querySerials == null) ? 0 : querySerials.hashCode());
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
		MegaClilocRequest other = (MegaClilocRequest) obj;
		if (querySerials == null) {
			if (other.querySerials != null)
				return false;
		} else if (!querySerials.equals(other.querySerials))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MegaClilocRequest [querySerials=" + querySerials + "]";
	}
}
