package net.sf.juoserver.protocol;

import java.io.Serializable;

import net.sf.juoserver.api.Coded;

public abstract class Subcommand<T extends AbstractMessage, S extends Coded>
		implements Serializable {
	private static final long serialVersionUID = 1L;
	private S subCommandType;

	public Subcommand(S subCommandType) {
		this.subCommandType = subCommandType;
	}

	public S getType() {
		return subCommandType;
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((subCommandType == null) ? 0 : subCommandType.hashCode());
		return result;
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Subcommand<?, ?> other = (Subcommand<?, ?>) obj;
		if (subCommandType == null) {
			if (other.subCommandType != null)
				return false;
		} else if (!subCommandType.equals(other.subCommandType))
			return false;
		return true;
	}

}
