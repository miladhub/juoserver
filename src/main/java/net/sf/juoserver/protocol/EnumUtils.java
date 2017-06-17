package net.sf.juoserver.protocol;

import net.sf.juoserver.api.Coded;

public class EnumUtils {
	/**
	 * Retrieves the enum constant belonging to the specified
	 * enum class ({@code clazz}) and identified by the provided
	 * integer {@code code} through the {@link Coded} interface.
	 * <p/>
	 * <tt>null</tt> is returned in case the enum type contains
	 * no constant that can be identified by the specified
	 * {@code code}.
	 * 
	 * @param <T> the enum type modeled by the specified enum
	 * class {@code clazz}
	 * @param code the integer code that identifies the enum
	 * constant to be returned
	 * @param clazz the enum class
	 * @return the enum constant belonging to the specified
	 * enum class and identified by the provided integer code,
	 * or <tt>null</tt> if the provided enum contains no such
	 * constant
	 */
	public static <T extends Enum<T> & Coded> T byCode(int code, Class<T> clazz) {
		for (T el : clazz.getEnumConstants()) {
			if (el.getCode() == code) {
				return el;
			}
		}
		return null;
	}
}
