package ladysnake.dissolution.soulstates;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class SoulState extends IForgeRegistryEntry.Impl<SoulState> {

    public abstract NBTTagCompound saveData(EntityPlayer player);

    public abstract void readData(EntityPlayer player, NBTTagCompound stateData);

    public abstract boolean isEventListener();

    public boolean allowStateChange(EntityPlayer player, SoulState newState) {
        return true;
    }

    public void initState(EntityPlayer player) {

    }

    public void resetState(EntityPlayer player) {

    }

}
