package ladysnake.dissolution.soulstates.strong;

import ladysnake.dissolution.Dissolution;
import ladysnake.dissolution.events.PossessionEvent;
import ladysnake.dissolution.soulstates.IPlayerState;
import ladysnake.dissolution.soulstates.SubState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

public class PossessingSubState extends SubState {
    public static final String NBT_TAG_POSSESSED_MOB = Dissolution.MODID + ":possessed_mob";

    private Map<EntityPlayer, PossessionData> possessionMap = new WeakHashMap<>();
    private static PossessingSubState instance;

    public static synchronized PossessingSubState getInstance() {
        if (instance == null)
            instance = new PossessingSubState();
        return instance;
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            if (possessionMap.containsKey(player)) {
                possessionMap.get(player).createSpawnPossessedEntity(player.world);
            }
        }
    }

    @SubscribeEvent
    public void onTickPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!possessionMap.containsKey(event.player)) return;
        possessionMap.get(event.player).getPossessed().ifPresent(possessed -> {
            possessed.setPositionAndRotation(event.player.posX, event.player.posY, event.player.posZ, event.player.rotationYaw, event.player.rotationPitch);
            event.player.setHealth(possessed.getHealth());
        });
    }

    /**
     * Remove every orphan possessed entity
     */
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (!event.getEntity().world.isRemote && event.getEntityLiving().getEntityData().hasKey(NBT_TAG_POSSESSED_MOB)) {
            for (PossessionData data : possessionMap.values())
                if (data.possessed == event.getEntityLiving()) return;
            event.getEntity().getEntityWorld().removeEntity(event.getEntity());
        }
    }

    @Override
    public void initState(EntityPlayer player) {
        possessionMap.put(player, new PossessionData());
    }

    /**
     * Call {@link #initState(EntityPlayer)} before this
     */
    public void startPossession(EntityPlayer player, EntityLivingBase possessed) {
        if (possessionMap.containsKey(player)) {
            if (MinecraftForge.EVENT_BUS.post(new PossessionEvent(player, possessed, true))) return;
            possessionMap.get(player).setPossessed(possessed);
            possessed.getEntityData().setBoolean(NBT_TAG_POSSESSED_MOB, true);
        }
    }

    @Override
    public void resetState(EntityPlayer player) {
        if (MinecraftForge.EVENT_BUS.post(new PossessionEvent(player, possessionMap.get(player).possessed, false))) return;
        possessionMap.get(player).getPossessed().ifPresent(possessed -> possessed.getEntityData().removeTag(NBT_TAG_POSSESSED_MOB));
        possessionMap.remove(player);
    }

    @Override
    public NBTTagCompound saveData(EntityPlayer player) {
        NBTTagCompound nbt = new NBTTagCompound();
        if (possessionMap.containsKey(player)) {
            nbt.setTag("possessed_data", possessionMap.get(player).possessedNBT);
        }
        return new NBTTagCompound();
    }

    @Override
    public void readData(EntityPlayer player, NBTTagCompound stateData) {
        if (possessionMap.containsKey(player) && stateData.hasKey("possessed_data")) {
            possessionMap.get(player).possessedNBT = stateData.getCompoundTag("possessed_data");
        }
    }

    @Override
    public boolean isEventListener() {
        return true;
    }

    class PossessionData {
        private EntityLivingBase possessed;
        private NBTTagCompound possessedNBT;

        public void setPossessed(EntityLivingBase possessed) {
            this.possessed = possessed;
            this.possessedNBT = possessed.serializeNBT();
            this.possessedNBT.removeTag("Dimension");
        }

        Optional<EntityLivingBase> getPossessed() {
            return Optional.ofNullable(possessed);
        }

        void createSpawnPossessedEntity(World worldIn) {
            Entity entity = EntityList.createEntityFromNBT(possessedNBT, worldIn);
            if (entity instanceof EntityLivingBase) {
                if (possessed != null)      // get rid of the old entity
                    possessed.world.removeEntity(possessed);
                possessed = (EntityLivingBase) entity;
                worldIn.spawnEntity(possessed);
            }
        }
    }
}
