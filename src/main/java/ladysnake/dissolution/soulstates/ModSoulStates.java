package ladysnake.dissolution.soulstates;

import ladysnake.dissolution.Dissolution;
import ladysnake.dissolution.soulstates.strong.StrongSoulState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
@GameRegistry.ObjectHolder(Dissolution.MODID)
@Mod.EventBusSubscriber(modid = Dissolution.MODID)
public class ModSoulStates {
    public static IForgeRegistry<ISoulState> REGISTRY;

    public static final SoulState WEAK_SOUL = new DefaultState();
    public static final SoulState STRONG_SOUL = new StrongSoulState();

    @SubscribeEvent
    public static void addRegistries(RegistryEvent.NewRegistry event) {
        REGISTRY = new RegistryBuilder<ISoulState>()
                .setType(ISoulState.class)
                .setName(new ResourceLocation(Dissolution.MODID, "states_of_soul"))
                .add((owner, stage, id, obj, oldObj) -> ModSoulStates.onAdd(obj, oldObj))
                .setMaxID(255)
                .create();
    }

    static void onAdd(IPlayerState obj, @Nullable IPlayerState oldObj) {
        if (oldObj != null && oldObj.isEventListener())
            MinecraftForge.EVENT_BUS.unregister(oldObj);
        if (obj.isEventListener())
            MinecraftForge.EVENT_BUS.register(obj);
    }

    @SubscribeEvent
    public static void onSoulStateRegister(RegistryEvent.Register<ISoulState> event) {
        event.getRegistry().registerAll(
                WEAK_SOUL.setRegistryName(Dissolution.MODID, "weak_soul"),
                STRONG_SOUL.setRegistryName(Dissolution.MODID, "strong_soul")
        );
    }
}
