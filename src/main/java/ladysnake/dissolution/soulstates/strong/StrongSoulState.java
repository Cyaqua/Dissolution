package ladysnake.dissolution.soulstates.strong;

import ladysnake.dissolution.client.events.RegisterSoulGuiEvent;
import ladysnake.dissolution.client.gui.GuiIncorporeal;
import ladysnake.dissolution.events.PossessionEvent;
import ladysnake.dissolution.soulstates.*;
import ladysnake.dissolution.util.IEventCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Map;
import java.util.WeakHashMap;

public class StrongSoulState extends SoulState {

    private final IEventCallback<Event> LOCK_CALLBACK = event -> EnumActionResult.SUCCESS;
    private final IEventCallback<PlayerInteractEvent.EntityInteractSpecific> POSSESSION_CALLBACK =
            event -> attemptPossession(event.getEntityPlayer(), event.getTarget());

    private Map<EntityPlayer, ISubState> playerActiveStateMap = new WeakHashMap<>();

    @Override
    public void initState(EntityPlayer player) {
        super.initState(player);
        playerActiveStateMap.put(player, ModSubStates.NONE);
    }

    @Override
    public void resetState(EntityPlayer player) {
        super.resetState(player);
        if (playerActiveStateMap.get(player) instanceof PossessingSubState)
            playerActiveStateMap.get(player).resetState(player);
        if (playerActiveStateMap.get(player) instanceof IncorporealSubState)
            playerActiveStateMap.get(player).resetState(player);
        playerActiveStateMap.remove(player);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onPlayerClone(PlayerEvent.Clone event) {
        // If you die in soul form, you are reset. Otherwise, you become a soul
        if (isPlayerSubscribed(event.getOriginal()) && !(playerActiveStateMap.get(event.getOriginal()) instanceof IncorporealSubState)) {
            makePlayerIncorporeal(event.getEntityPlayer());
        }
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        @SuppressWarnings("SuspiciousMethodCalls")
        IPlayerState state = playerActiveStateMap.get(event.getEntityLiving());
        if (state instanceof PossessingSubState) {
            state.resetState((EntityPlayer) event.getEntityLiving());
            event.getEntityLiving().heal(20f);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPossessionStop(PossessionEvent event) {
        if (!event.isPossessionStart() && playerActiveStateMap.containsKey(event.getEntityPlayer())) {
            IncorporealSubState state = IncorporealSubState.getInstance();
            playerActiveStateMap.put(event.getEntityPlayer(), state);
            state.removeCallback(LOCK_CALLBACK);
        }
    }

    private void makePlayerIncorporeal(EntityPlayer player) {
        IncorporealSubState soulState = IncorporealSubState.getInstance();
        playerActiveStateMap.put(player, soulState);
        soulState.initState(player);
        soulState.addCallback(
                PlayerInteractEvent.EntityInteractSpecific.class,
                POSSESSION_CALLBACK,
                event -> isPlayerSubscribed(event.getEntityPlayer())
        );
    }

    private EnumActionResult attemptPossession(EntityPlayer possessor, Entity entityIn) {
        if (entityIn instanceof EntityLivingBase) {
            EntityLivingBase possessed = (EntityLivingBase) entityIn;
            if(possessed.isEntityUndead()) {
                PossessingSubState possessingState = PossessingSubState.getInstance();
                possessingState.initState(possessor);
                possessingState.startPossession(possessor, possessed);
                IncorporealSubState.getInstance().addCallback(LOCK_CALLBACK,
                        event -> event instanceof PlayerEvent && isPlayerSubscribed(((PlayerEvent)event).getEntityPlayer())
                        || event instanceof TickEvent.PlayerTickEvent && isPlayerSubscribed(((TickEvent.PlayerTickEvent)event).player));
                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.PASS;
    }

    @Override
    public NBTTagCompound saveData(EntityPlayer player) {
        if (playerActiveStateMap.containsKey(player)) {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setString("current_sub_state", String.valueOf(playerActiveStateMap.get(player).getRegistryName()));
            return playerActiveStateMap.get(player).saveData(player);
        }
        return new NBTTagCompound();
    }

    @Override
    public void readData(EntityPlayer player, NBTTagCompound stateData) {
        if (playerActiveStateMap.containsKey(player)) {
            ISubState subState = ModSubStates.REGISTRY.getValue(new ResourceLocation(stateData.getString("current_sub_state")));
            if (subState == null) return;
            playerActiveStateMap.put(player, subState);
            subState.readData(player, stateData);
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

}
