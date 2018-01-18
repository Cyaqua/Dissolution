package ladysnake.dissolution.soulstates;

import ladysnake.dissolution.Dissolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

@Mod.EventBusSubscriber
public class CapabilitySoulState {
    @CapabilityInject(ISoulStateHandler.class)
    public static Capability<ISoulStateHandler> CAPABILITY_STATE_OF_SOUL;

    public static void register() {
        CapabilityManager.INSTANCE.register(ISoulStateHandler.class, new Storage(), SoulStateHandler::new);
    }

    public static Optional<ISoulStateHandler> getHandler(Entity entity) {
        if (entity instanceof EntityPlayer)
            return Optional.ofNullable(entity.getCapability(CAPABILITY_STATE_OF_SOUL, null));
        return Optional.empty();
    }

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent event) {
        if (event.getObject() instanceof EntityPlayer)
            event.addCapability(new ResourceLocation(Dissolution.MODID, "soul_state_cap"), new Provider((EntityPlayer) event.getObject()));
    }

    public static class SoulStateHandler implements ISoulStateHandler {
        private SoulState currentState;
        private final EntityPlayer owner;

        private SoulStateHandler() {
            this(null);
        }

        public SoulStateHandler(EntityPlayer owner) {
            this.owner = owner;
            this.currentState = SoulStates.WEAK;
        }

        @Override
        public SoulState getCurrentState() {
            return currentState;
        }

        @Override
        public void setCurrentState(SoulState newState) {
            if (currentState.allowStateChange(owner, newState)) {
                currentState.resetState(owner);
                this.currentState = newState;
                currentState.initState(owner);
//                return true;
            }
//            return false;
        }

        @Override
        public EntityPlayer getOwner() {
            return owner;
        }
    }

    public static class Provider implements ICapabilitySerializable<NBTTagCompound> {
        ISoulStateHandler instance;

        public Provider(EntityPlayer owner) {
            this.instance = new SoulStateHandler(owner);
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == CAPABILITY_STATE_OF_SOUL;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            return capability == CAPABILITY_STATE_OF_SOUL ? CAPABILITY_STATE_OF_SOUL.cast(instance) : null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return (NBTTagCompound) CAPABILITY_STATE_OF_SOUL.getStorage().writeNBT(CAPABILITY_STATE_OF_SOUL, instance, null);
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            CAPABILITY_STATE_OF_SOUL.getStorage().readNBT(CAPABILITY_STATE_OF_SOUL, instance, null, nbt);
        }
    }

    public static class Storage implements Capability.IStorage<ISoulStateHandler> {

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<ISoulStateHandler> capability, ISoulStateHandler instance, EnumFacing side) {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setString("current_state", Objects.requireNonNull(instance.getCurrentState().getRegistryName(), "attempted to save an unregistered state of soul").toString());
            nbt.setTag("state_data", instance.getCurrentState().saveData(instance.getOwner()));
            return nbt;
        }

        @Override
        public void readNBT(Capability<ISoulStateHandler> capability, ISoulStateHandler instance, EnumFacing side, NBTBase nbt) {
            SoulState state = SoulStates.REGISTRY.getValue(new ResourceLocation(((NBTTagCompound)nbt).getString("current_state")));
            if (state != null) {
                state.readData(instance.getOwner(), ((NBTTagCompound) nbt).getCompoundTag("state_data"));
            }
            instance.setCurrentState(state);
        }
    }
}
