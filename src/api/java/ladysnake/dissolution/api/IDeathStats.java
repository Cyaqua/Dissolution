package ladysnake.dissolution.api;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.INBTSerializable;

public interface IDeathStats extends INBTSerializable<NBTTagCompound> {

    void setDead(boolean dead);

    /**
     * Used to tell if respawn-specific logic should be applied on the player
     * @return true if the player is dead or respawned recently
     */
    boolean wasDead();

    Vec3d getDeathLocation();

    void setDeathLocation(Vec3d deathLocation);

    int getDeathDimension();

    void setDeathDimension(int dimension);

    String getLastDeathMessage();

    void setLastDeathMessage(String lastDeath);
}
