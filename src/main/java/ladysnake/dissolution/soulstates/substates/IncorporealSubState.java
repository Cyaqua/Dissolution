package ladysnake.dissolution.soulstates.substates;

import net.minecraft.nbt.NBTTagCompound;

public class IncorporealSubState implements ISubState {
    @Override
    public NBTTagCompound serializeNBT() {
        return new NBTTagCompound();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {

    }
}
