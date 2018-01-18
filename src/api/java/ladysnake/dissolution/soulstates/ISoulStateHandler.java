package ladysnake.dissolution.soulstates;

import net.minecraft.entity.player.EntityPlayer;

public interface ISoulStateHandler {

    SoulState getCurrentState();

    void setCurrentState(SoulState newState);

    EntityPlayer getOwner();

}
