package ladysnake.dissolution.soulstates;

import ladysnake.dissolution.client.events.RegisterSoulGuiEvent;
import ladysnake.dissolution.client.gui.GuiIncorporeal;
import ladysnake.dissolution.soulstates.substates.ISubState;
import ladysnake.dissolution.soulstates.substates.IncorporealSubState;
import ladysnake.dissolution.soulstates.substates.PossessingSubState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;
import java.util.WeakHashMap;

public class StrongSoulState extends SoulState {

    private Map<EntityPlayer, StrongSoulStateData> playerDataMap = new WeakHashMap<>();

    @Override
    public void initState(EntityPlayer player) {
        super.initState(player);
        playerDataMap.put(player, new StrongSoulStateData());
    }

    @Override
    public void resetState(EntityPlayer player) {
        super.resetState(player);
        playerDataMap.remove(player);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onPlayerClone(PlayerEvent.Clone event) {
        if (playerDataMap.containsKey(event.getOriginal())) {
            playerDataMap.put(event.getEntityPlayer(), playerDataMap.get(event.getOriginal()));
        }
    }

    @Override
    public NBTTagCompound saveData(EntityPlayer player) {
        NBTTagCompound nbt = new NBTTagCompound();
        if (playerDataMap.containsKey(player)) {
            StrongSoulStateData data = playerDataMap.get(player);
            nbt.setTag("possession_data", data.possessedState.serializeNBT());
            nbt.setTag("incorporeal_data", data.soulState.serializeNBT());
        }
        return nbt;
    }

    @Override
    public void readData(EntityPlayer player, NBTTagCompound stateData) {
        if (playerDataMap.containsKey(player)) {
            StrongSoulStateData data = playerDataMap.get(player);
            data.possessedState.deserializeNBT(stateData.getCompoundTag("possession_data"));
            data.soulState.deserializeNBT(stateData.getCompoundTag("incorporeal_data"));
        }
    }

    @Override
    public boolean isEventListener() {
        return true;
    }

    @SubscribeEvent
    public void onRegisterSoulGui(RegisterSoulGuiEvent event) {
        event.registerGui(this, new GuiIncorporeal());
    }

    class StrongSoulStateData {
        ISubState soulState = new IncorporealSubState();
        PossessingSubState possessedState = new PossessingSubState();

        void possess(EntityLivingBase possessed) {
            if (possessed == null) {
                possessedState.reset();
            } else if (possessed.isEntityUndead()) {
                possessedState.startPossessing(possessed);
            }
        }
    }
}
