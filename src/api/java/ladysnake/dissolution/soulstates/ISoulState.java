package ladysnake.dissolution.soulstates;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface ISoulState extends IPlayerState, IForgeRegistryEntry<ISoulState> {

    /**
     * Attempts to enable a specific sub-state on this state of soul.
     * This operation may or may not be accepted by the state of soul
     */
    boolean enableSubState(EntityPlayer player, ISubState subState);

    boolean isPlayerSubscribed(EntityPlayer player);
}
