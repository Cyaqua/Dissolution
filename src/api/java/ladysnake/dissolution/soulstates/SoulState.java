package ladysnake.dissolution.soulstates;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class SoulState extends IForgeRegistryEntry.Impl<ISoulState> implements ISoulState {

    @Override
    public boolean allowStateChange(EntityPlayer player, ISoulState newState) {
        return true;
    }

    @Override
    public boolean isPlayerSubscribed(EntityPlayer player) {
        @SuppressWarnings("ConstantConditions")
        ISoulStateHandler handler = player.getCapability(ISoulStateHandler.CAPABILITY_STATE_OF_SOUL, null);
        return handler != null && handler.getCurrentState() == this;
    }

}