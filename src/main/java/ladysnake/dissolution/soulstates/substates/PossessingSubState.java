package ladysnake.dissolution.soulstates.substates;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

public class PossessingSubState implements ISubState {
    private EntityLivingBase possessed;

    public void startPossessing(EntityLivingBase possessed) {

    }

    public void reset() {
        this.possessed = null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return new NBTTagCompound();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {

    }
}
