package ladysnake.dissolution.soulstates;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface ISoulState extends IPlayerState, IForgeRegistryEntry<ISoulState> {
    boolean allowStateChange(EntityPlayer player, ISoulState newState);

    boolean isPlayerSubscribed(EntityPlayer player);
}
