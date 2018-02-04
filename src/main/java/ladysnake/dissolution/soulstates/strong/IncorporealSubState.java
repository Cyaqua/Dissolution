package ladysnake.dissolution.soulstates.strong;

import ladysnake.dissolution.Dissolution;
import ladysnake.dissolution.soulstates.ModSubStates;
import ladysnake.dissolution.soulstates.SubState;
import ladysnake.dissolution.util.IEventCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Predicate;

public class IncorporealSubState extends SubState {

    public static IncorporealSubState getInstance() {
        return (IncorporealSubState) ModSubStates.INCORPOREAL;
    }

    private static MethodHandle isImmuneToFireMH, foodTimer, foodExhaustionLevel, flyToggleTimer;

    static {
        try {
            Field field = ReflectionHelper.findField(FoodStats.class, "foodTimer", "field_75123_d");
            foodTimer = MethodHandles.lookup().unreflectSetter(field);
            field = ReflectionHelper.findField(FoodStats.class, "foodExhaustionLevel", "field_75126_c");
            foodExhaustionLevel = MethodHandles.lookup().unreflectSetter(field);
            field = ReflectionHelper.findField(EntityPlayer.class, "flyToggleTimer", "field_71101_bC");
            flyToggleTimer = MethodHandles.lookup().unreflectSetter(field);
            field = ReflectionHelper.findField(Entity.class, "field_70178_ae", "isImmuneToFire");
            isImmuneToFireMH = MethodHandles.lookup().unreflectSetter(field);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * True if a player is actively incorporeal, false if a player is technically incorporeal, absent if a player is neither
     */
    private Map<EntityPlayer, Boolean> subscribedPlayers = new WeakHashMap<>();
    private Map<IEventCallback, Predicate> callbacks = new HashMap<>();

    public <T extends Event> void addCallback(Class<T> eventType, IEventCallback<T> callback, Predicate<T> precondition) {
        addCallback(callback, ((Predicate<T>)eventType::isInstance).and(precondition));
    }

    public <T extends Event> void addCallback(IEventCallback<T> callback, Predicate<T> precondition) {
        callbacks.put(callback, precondition);
    }

    public void removeCallback(IEventCallback callback) {
        callbacks.remove(callback);
    }

    /**
     * @return true if the method can continue processing the event
     */
    @SuppressWarnings("unchecked")
    private boolean callCallbacks(Event event) {
        for (Map.Entry<IEventCallback, Predicate> entry : callbacks.entrySet()) {
            if (entry.getValue().test(event)) {
                switch (entry.getKey().call(event)) {
                    case SUCCESS:return false;  // event processed
                    case FAIL:return true;  // no more calls
                    default:            // continue
                }
            }
        }
        return true;
    }

    @Override
    public void initState(EntityPlayer player, Object... args) {
        Boolean active = args.length > 0 && args[0] instanceof Boolean ? (Boolean) args[0] : Boolean.TRUE;
        subscribedPlayers.put(player, active);
        changeState(player, active);
    }

    @Override
    public void resetState(EntityPlayer player) {
        changeState(player, false);
        subscribedPlayers.remove(player);
    }

    private boolean isSubscribed(EntityPlayer player) {
        return subscribedPlayers.containsKey(player) && !player.isCreative();
    }

    @Override
    public boolean isEventListener() {
        return true;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerAttackEntity(AttackEntityEvent event) {
        if (isSubscribed(event.getEntityPlayer()) && callCallbacks(event))
            event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (isSubscribed(event.getEntityPlayer()) && callCallbacks(event)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onTickPlayerTick(TickEvent.PlayerTickEvent event) {
        if (isSubscribed(event.player) && callCallbacks(event)) {
            handleSoulFlight(event.player);

            try {
                foodTimer.invokeExact(event.player.getFoodStats(), 0);
                foodExhaustionLevel.invokeExact(event.player.getFoodStats(), 0f);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

            if (event.player.experience > 0 && event.player.world.rand.nextBoolean())
                event.player.experience--;
            else if (event.player.world.rand.nextInt() % 300 == 0 && event.player.experienceLevel > 0)
                event.player.addExperienceLevel(-1);
        }
    }

    protected void changeState(EntityPlayer owner, boolean init) {
        try {
            if (isImmuneToFireMH != null)
                isImmuneToFireMH.invoke(owner, init);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        if (!owner.isCreative()) {
            owner.capabilities.allowFlying = init;
            owner.capabilities.isFlying = init && owner.capabilities.isFlying;
        }
    }

    private void handleSoulFlight(EntityPlayer player) {
        player.capabilities.isFlying = true;
        player.onGround = false;
        player.capabilities.allowFlying = true;
        try {
            flyToggleTimer.invokeExact(player, 0);
        } catch (Throwable throwable) {
            Dissolution.logger.error("an error occurred while handling soul flight", throwable);
        }
    }
}
