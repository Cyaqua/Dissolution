package ladysnake.dissolution.soulstates;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public interface IPlayerState {

    default NBTTagCompound saveData(EntityPlayer player) {
        return new NBTTagCompound();
    }

    default void readData(EntityPlayer player, NBTTagCompound stateData) {
        // NO-OP
    }

    boolean isEventListener();

    default void initState(EntityPlayer player, Object... args) {
        // NO-OP
    }

    default void resetState(EntityPlayer player) {
        // NO-OP
    }

}
