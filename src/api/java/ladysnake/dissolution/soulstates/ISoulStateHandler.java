package ladysnake.dissolution.soulstates;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public interface ISoulStateHandler {

    @CapabilityInject(ISoulStateHandler.class)
    Capability<ISoulStateHandler> CAPABILITY_STATE_OF_SOUL = null;

    SoulState getCurrentState();

    void setCurrentState(SoulState newState);

    EntityPlayer getOwner();

}
