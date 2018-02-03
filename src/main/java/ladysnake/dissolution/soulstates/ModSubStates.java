package ladysnake.dissolution.soulstates;

import ladysnake.dissolution.Dissolution;
import ladysnake.dissolution.soulstates.strong.IncorporealSubState;
import ladysnake.dissolution.soulstates.strong.PossessingSubState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

@SuppressWarnings("unused")
@GameRegistry.ObjectHolder(Dissolution.MODID)
@Mod.EventBusSubscriber(modid = Dissolution.MODID)
public class ModSubStates {
    public static IForgeRegistry<ISubState> REGISTRY;

    public static final SubState NONE = new DefaultSubState();
    public static final SubState INCORPOREAL = NONE;
    public static final SubState POSSESSING = NONE;

    @SubscribeEvent
    public void addRegistries(RegistryEvent.NewRegistry event) {
        REGISTRY = new RegistryBuilder<ISubState>()
                .setType(ISubState.class)
                .setName(new ResourceLocation(Dissolution.MODID, "sub_states_of_soul"))
                .setMaxID(255)
                .create();
    }

    @SubscribeEvent
    public void onSoulStateRegister(RegistryEvent.Register<ISubState> event) {
        event.getRegistry().registerAll(
                NONE.setRegistryName(Dissolution.MODID, "none"),
                new IncorporealSubState().setRegistryName(Dissolution.MODID, "incorporeal"),
                new PossessingSubState().setRegistryName(Dissolution.MODID, "possessing")
        );
    }
}
