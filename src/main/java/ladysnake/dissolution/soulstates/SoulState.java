package ladysnake.dissolution.soulstates;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.*;

public abstract class SoulState extends IForgeRegistryEntry.Impl<ISoulState> implements ISoulState {

    protected Map<EntityPlayer, Set<ISubState>> playerActiveStateMap = new WeakHashMap<>();

    @Override
    public boolean enableSubState(EntityPlayer player, ISubState subState) {
        playerActiveStateMap.computeIfAbsent(player, p -> new HashSet<>()).add(subState);
        return true;
    }

    public boolean disableSubState(EntityPlayer player, ISubState subState) {
        if (playerActiveStateMap.containsKey(player) && playerActiveStateMap.get(player).remove(subState)) {
            subState.resetState(player);
            return true;
        }
        return false;
    }

    protected boolean isSubStateEnabled(EntityPlayer player, ISubState subState) {
        return !playerActiveStateMap.containsKey(player) || playerActiveStateMap.get(player).contains(subState);
    }

    @Override
    public boolean isPlayerSubscribed(EntityPlayer player) {
        return playerActiveStateMap.containsKey(player);
    }

    @Override
    public NBTTagCompound saveData(EntityPlayer player, NBTTagCompound compound) {
        NBTTagCompound nbt = new NBTTagCompound();
        if (isPlayerSubscribed(player)) {
            NBTTagList list = new NBTTagList();
            for (ISubState subState : playerActiveStateMap.get(player)) {
                list.appendTag(subState.saveData(player, new NBTTagCompound()));
            }
            nbt.setTag("sub_states", list);
        }
        return nbt;
    }

    @Override
    public void readData(EntityPlayer player, NBTTagCompound stateData) {
        if (playerActiveStateMap.containsKey(player)) {
            NBTTagList list = stateData.getTagList("sub_states", 10);
            for (NBTBase nbt : list) {
                NBTTagCompound compound = (NBTTagCompound) nbt;
                ISubState subState = ModSubStates.REGISTRY.getValue(new ResourceLocation(compound.getString("id")));
                if (subState == null) continue;
                enableSubState(player, subState);
                subState.initState(player);
                subState.readData(player, stateData);
            }
        }
    }

}