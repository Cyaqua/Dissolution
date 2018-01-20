package ladysnake.dissolution.soulstates;

import ladysnake.dissolution.Dissolution;
import ladysnake.dissolution.soulstates.strong.StrongSoulState;
import ladysnake.dissolution.soulstates.SoulState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = Dissolution.MODID)
public class SoulStates {
    public static final IForgeRegistry<SoulState> REGISTRY = new RegistryBuilder<SoulState>()
            .setName(new ResourceLocation(Dissolution.MODID, "states_of_soul"))
            .setMaxID(255)
            .add((owner, stage, id, obj, oldObj) -> onAdd(obj, oldObj))
            .create();

    public static final SoulState WEAK = new DefaultState().setRegistryName(Dissolution.MODID, "weak_soul");
    public static final SoulState STRONG = new StrongSoulState().setRegistryName(Dissolution.MODID, "strong_soul");

    private static void onAdd(SoulState obj, @Nullable SoulState oldObj) {
        if (oldObj != null && oldObj.isEventListener())
            MinecraftForge.EVENT_BUS.unregister(oldObj);
        if (obj.isEventListener())
            MinecraftForge.EVENT_BUS.register(obj);
    }

    @SubscribeEvent
    public void onSoulStateRegister(RegistryEvent.Register<SoulState> event) {
        event.getRegistry().registerAll(
                WEAK, STRONG
        );
    }
}
