package ladysnake.dissolution.soulstates;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class DefaultState extends SoulState {
    @Override
    public NBTTagCompound saveData(EntityPlayer player) {
        return new NBTTagCompound();
    }

    @Override
    public void readData(EntityPlayer player, NBTTagCompound stateData) {

    }

    @Override
    public boolean isEventListener() {
        return false;
    }
}
