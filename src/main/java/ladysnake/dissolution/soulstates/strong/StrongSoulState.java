package ladysnake.dissolution.soulstates.strong;

import ladysnake.dissolution.client.events.RegisterSoulGuiEvent;
import ladysnake.dissolution.client.gui.GuiIncorporeal;
import ladysnake.dissolution.events.PossessionEvent;
import ladysnake.dissolution.soulstates.*;
import ladysnake.dissolution.util.IEventCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StrongSoulState extends SoulState {

    private final IEventCallback<Event> LOCK_CALLBACK = event -> EnumActionResult.SUCCESS;
    private final IEventCallback<PlayerInteractEvent.EntityInteractSpecific> POSSESSION_CALLBACK =
            event -> attemptPossession(event.getEntityPlayer(), event.getTarget());

    @Override
    public void initState(EntityPlayer player) {
        super.initState(player);
        enableSubState(player, ModSubStates.NONE);
    }

    @Override
    public void resetState(EntityPlayer player) {
        super.resetState(player);
        if (!isPlayerSubscribed(player)) return;
        playerActiveStateMap.get(player).forEach(state -> disableSubState(player, state));
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
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            if (disableSubState(player, ModSubStates.POSSESSING)) {
                event.getEntityLiving().heal(20f);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onPossessionStop(PossessionEvent event) {
        if (!event.isPossessionStart() && isPlayerSubscribed(event.getEntityPlayer())) {
            IncorporealSubState state = IncorporealSubState.getInstance();
            enableSubState(event.getEntityPlayer(), state);
            state.removeCallback(LOCK_CALLBACK);
        }
    }

    private void makePlayerIncorporeal(EntityPlayer player) {
        IncorporealSubState soulState = IncorporealSubState.getInstance();
        soulState.initState(player);
        enableSubState(player, soulState);
        soulState.addCallback(
                PlayerInteractEvent.EntityInteractSpecific.class,
                POSSESSION_CALLBACK,
                event -> player == event.getEntityPlayer()
        );
    }

    private EnumActionResult attemptPossession(final EntityPlayer possessor, Entity entityIn) {
        if (entityIn instanceof EntityLivingBase) {
            EntityLivingBase possessed = (EntityLivingBase) entityIn;
            if(possessed.isEntityUndead()) {
                PossessingSubState possessingState = PossessingSubState.getInstance();
                possessingState.initState(possessor);
                possessingState.startPossession(possessor, possessed);
                IncorporealSubState.getInstance().initState(possessor);
                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.PASS;
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
