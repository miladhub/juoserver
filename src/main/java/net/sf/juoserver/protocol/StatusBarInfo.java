package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;

import net.sf.juoserver.api.StatusFlag;
import net.sf.juoserver.api.Mobile;

public class StatusBarInfo extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	private static final int CODE = 0x11;
	private Mobile mobile;
	public StatusBarInfo(Mobile mobile) {
		super(CODE, getLength(mobile));
		this.mobile = mobile;
	}
	private static int getLength(Mobile mobile) {
		int l = 66;
		if (mobile.getStatusFlag().compareTo(StatusFlag.UOML) >= 0) {
			l += 3;
		}
		if (mobile.getStatusFlag().compareTo(StatusFlag.UOR) >= 0) {
			l += 4;
		}
		if (mobile.getStatusFlag().compareTo(StatusFlag.AOS) >= 0) {
			l += 18;
		}
		return l;
	}
	@Override
	public ByteBuffer encode() {
		ByteBuffer bb = super.encode();
		bb.putShort((short) getLength());
		bb.putInt(mobile.getSerialId());
		bb.put(MessagesUtils.padString(mobile.getName(), 30));
		bb.putShort((short) mobile.getCurrentHitPoints());
		bb.putShort((short) mobile.getMaxHitPoints());
		bb.put((byte) (mobile.isNameChangeFlag()? 1 : 0));
		bb.put((byte) mobile.getStatusFlag().getCode());
		bb.put((byte) mobile.getSexRace().getCode());
		bb.putShort((short) mobile.getStrength());
		bb.putShort((short) mobile.getDexterity());
		bb.putShort((short) mobile.getIntelligence());
		bb.putShort((short) mobile.getCurrentStamina());
		bb.putShort((short) mobile.getMaxStamina());
		bb.putShort((short) mobile.getCurrentMana());
		bb.putShort((short) mobile.getMaxMana());
		bb.putInt(mobile.getGoldInPack());
		bb.putShort((short) mobile.getArmorRating());
		bb.putShort((short) mobile.getWeight());
		if (mobile.getStatusFlag().compareTo(StatusFlag.UOML) >= 0) {
			bb.putShort((short) mobile.getMaxWeight());
			bb.put((byte) mobile.getRaceFlag().getCode());
		}
		if (mobile.getStatusFlag().compareTo(StatusFlag.UOR) >= 0) {
			bb.putShort((short) 225); // Stats cap
			bb.put((byte) 0); // Followers
			bb.put((byte) 0); // Max followers
		}
		if (mobile.getStatusFlag().compareTo(StatusFlag.AOS) >= 0) {
			bb.putShort((short) 0); // FireResist
			bb.putShort((short) 0); // ColdResist
			bb.putShort((short) 0); // PoisonResist
			bb.putShort((short) 0); // EnergyResist
			bb.putShort((short) 0); // Luck
			bb.putShort((short) 0); // DamageMinimum
			bb.putShort((short) 0); // DamageMaximum
			bb.putInt(0); // TithingPoints (Paladin books)
		}
		return bb;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mobile == null) ? 0 : mobile.hashCode());
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
		StatusBarInfo other = (StatusBarInfo) obj;
		if (mobile == null) {
			if (other.mobile != null)
				return false;
		} else if (!mobile.equals(other.mobile))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "StatusBarInfo [mobile=" + mobile + "]";
	}
}
