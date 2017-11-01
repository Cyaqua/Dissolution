package ladysnake.dissolution.api;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

/**
 * The interface providing methods related to the Incorporeal capability
 * This is basically used as a replacement to extended properties
 * @author Pyrofab
 *
 */
public interface IIncorporealHandler {

	enum CorporealityStatus {
		NORMAL(true),
		ECTOPLASM(false),
		SOUL(false);

		private final boolean corporeal;

		CorporealityStatus(boolean corporeal) {
			this.corporeal = corporeal;
		}

		public boolean isIncorporeal() {
			return !this.corporeal;
		}
	}

	/**
	 * Sets the tangibility of the player specified, along with the corresponding attributes
	 */
	void setCorporealityStatus(CorporealityStatus newStatus);

	/**
	 * @return The current status of this player
	 */
	@Nonnull
	CorporealityStatus getCorporealityStatus();

	/**
	 *
	 * @param possessable the entity to possess
	 * @return false if the change could not occur
	 * @throws IllegalArgumentException if the possessable argument is not an entity
	 */
	boolean setPossessed(IPossessable possessable);

	IPossessable getPossessed();

	EctoplasmStats getEctoplasmStats();

	/**
	 * Sets the synchronization status of this handler (between server and client)
	 * @param synced if true, this handler should be synchronized next tick
	 */
	void setSynced(boolean synced);
	
	/**
	 * Whether this handler needs updating from the server
	 * @return true if this handler has already been synchronized at least once
	 */
	boolean isSynced();

	void tick();
	
	String getLastDeathMessage();
	
	void setLastDeathMessage(String lastDeath);
	
	void setDisguise(UUID usurpedId);
	
	Optional<UUID> getDisguise();

}
