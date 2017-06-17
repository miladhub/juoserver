package net.sf.juoserver.api;

public enum Layer implements Coded {
	/**
	 * Invalid layer.
	 */
	Invalid(0x00),
	/**
	 * First valid layer. Equivalent to OneHanded.
	 */
	FirstValid(0x01),
	/**
	 * One handed weapon.
	 */
	OneHanded(0x01),
	/**
	 * Two handed weapon or shield.
	 */
	TwoHanded(0x02),
	/**
	 * Shoes.
	 */
	Shoes(0x03),
	/**
	 * Pants.
	 */
	Pants(0x04),
	/**
	 * Shirts.
	 */
	Shirt(0x05),
	/**
	 * Helmets), hats), and masks.
	 */
	Helm(0x06),
	/**
	 * Gloves.
	 */
	Gloves(0x07),
	/**
	 * Rings.
	 */
	Ring(0x08),
	/**
	 * Talismans.
	 */
	Talisman(0x09),
	/**
	 * Gorgets and necklaces.
	 */
	Neck(0x0A),
	/**
	 * Hair.
	 */
	Hair(0x0B),
	/**
	 * Half aprons.
	 */
	Waist(0x0C),
	/**
	 * Torso, inner layer.
	 */
	InnerTorso(0x0D),
	/**
	 * Bracelets.
	 */
	Bracelet(0x0E),
	/**
	 * Unused.
	 */
	Unused_xF(0x0F),
	/**
	 * Beards and mustaches.
	 */
	FacialHair(0x10),
	/**
	 * Torso), outer layer.
	 */
	MiddleTorso(0x11),
	/**
	 * Earrings.
	 */
	Earrings(0x12),
	/**
	 * Arms and sleeves.
	 */
	Arms(0x13),
	/**
	 * Cloaks.
	 */
	Cloak(0x14),
	/**
	 * Backpacks.
	 */
	Backpack(0x15),
	/**
	 * Torso, outer layer.
	 */
	OuterTorso(0x16),
	/**
	 * Leggings, outer layer.
	 */
	OuterLegs(0x17),
	/**
	 * Leggings, inner layer.
	 */
	InnerLegs(0x18),
	/**
	 * Last valid non-internal layer. Equivalent to <c>Layer.InnerLegs</c>.
	 */
	LastUserValid(0x18),
	/**
	 * Mount item layer.
	 */
	Mount(0x19),
	/**
	 * Vendor 'buy pack' layer.
	 */
	ShopBuy(0x1A),
	/**
	 * Vendor 'resale pack' layer.
	 */
	ShopResale(0x1B),
	/**
	 * Vendor 'sell pack' layer.
	 */
	ShopSell(0x1C),
	/**
	 * Bank box layer.
	 */
	Bank(0x1D),
	/**
	 * Last valid layer. Equivalent to <tt>Layer.Bank</tt>.
	 */
	LastValid(0x1D);
	private int code;
	private Layer(int code) {
		this.code = code;
	}
	@Override
	public int getCode() {
		return code;
	}
	public static Layer byCode(int code) {
		for (Layer layer : values()) {
			if (layer.getCode() == code) {
				return layer;
			}
		}
		throw new IllegalStateException("Invalid layer code: " + code);
	}
}
