package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;

import net.sf.juoserver.api.Decodable;
import net.sf.juoserver.api.Point3D;
import net.sf.juoserver.model.PointInSpace;

@Decodable(code = DropItem.CODE)
public class DropItem extends AbstractMessage {
	private static final long serialVersionUID = 1L;

	protected static final int CODE = 0x08;
	
	/**
	 * Serial ID of the item being dropped.
	 */
	private int itemSerial;
	
	/**
	 * X coordinate where the item is being dropped to.
	 */
	private int targetX;
	
	/**
	 * Y coordinate where the item is being dropped to.
	 */
	private int targetY;
	
	/**
	 * Z coordinate where the item is being dropped to.
	 */
	private int targetZ;
	
	/**
	 * Container where the item it being dropped to.
	 * <p/>
	 * Equals <tt>0xFFFFFFFF</tt> in case of dropping
	 * onto the ground.
	 * 
	 * @see #isDroppedOnTheGround()
	 */
	private int targetContainerSerial;
	
	private DropItem() {
		super(CODE, 14);
	}
	
	public DropItem(int itemSerial, int targetX, int targetY, int targetZ,
			int targetContainerSerial) {
		this();
		this.itemSerial = itemSerial;
		this.targetX = targetX;
		this.targetY = targetY;
		this.targetZ = targetZ;
		this.targetContainerSerial = targetContainerSerial;
	}

	public DropItem(byte[] contents) {
		this();
		ByteBuffer bb = wrapContents(1, contents);
		itemSerial = bb.getInt();
		targetX = bb.getShort();
		targetY = bb.getShort();
		targetZ = bb.get();
		targetContainerSerial = bb.getInt();
	}

	public int getItemSerial() {
		return itemSerial;
	}

	public int getTargetX() {
		return targetX;
	}

	public int getTargetY() {
		return targetY;
	}

	public int getTargetZ() {
		return targetZ;
	}

	public int getTargetContainerSerial() {
		return targetContainerSerial;
	}

	public boolean isDroppedOnTheGround() {
		return targetContainerSerial == 0xFFFFFFFF;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + itemSerial;
		result = prime * result + targetContainerSerial;
		result = prime * result + targetX;
		result = prime * result + targetY;
		result = prime * result + targetZ;
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
		DropItem other = (DropItem) obj;
		if (itemSerial != other.itemSerial)
			return false;
		if (targetContainerSerial != other.targetContainerSerial)
			return false;
		if (targetX != other.targetX)
			return false;
		if (targetY != other.targetY)
			return false;
		if (targetZ != other.targetZ)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DropItem [itemSerial=" + itemSerial + ", x=" + targetX + ", y=" + targetY
				+ ", z=" + targetZ + ", containerSerial=" + targetContainerSerial + "]";
	}

	public Point3D getTargetPosition() {
		return new PointInSpace(getTargetX(), getTargetY(), getTargetZ());
	}
}
