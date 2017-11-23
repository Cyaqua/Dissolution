package ladysnake.dissolution.common.items;

import ladysnake.dissolution.common.Reference;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is basically an enum that allows my fellow modders to add nice stuff
 *
 * @author Pyrofab
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class AlchemyModuleTypes {

    private static final List<AlchemyModuleTypes> toRegister = new ArrayList<>();

    public static AlchemyModuleTypes ALCHEMICAL_INTERFACE_TOP;
    public static AlchemyModuleTypes ALCHEMICAL_INTERFACE_BOTTOM;
    public static AlchemyModuleTypes CLOCHE;
    public static AlchemyModuleTypes CRYSTALLIZER;
    public static AlchemyModuleTypes RESONANT_GENERATOR;
    public static AlchemyModuleTypes MINERAL_FILTER;
    public static AlchemyModuleTypes SOUL_FILTER;

    public final int maxTier;
    /**
     * Indicates which machine slots are used by this module. <br/>
     * If another module has an incompatible flag, they will not be able to be installed together.
     */
    private final int slotsTaken;
    private final List<AlchemyModuleTypes> aliases;

    private AlchemyModuleTypes(int maxTier, int slotsTaken, AlchemyModuleTypes... aliases) {
        this(maxTier, slotsTaken, true, aliases);
    }

    /**
     * Creates an alchemy module type to be used by modular machines
     *
     * @param maxTier    the number of tiers this module type possesses
     * @param slotsTaken a binary flag used to determine which modules are compatible
     * @param addToList  if true, this module and its associated items will be registered automatically
     * @param aliases    a list of other alchemy modules you can use in this module's place
     */
    public AlchemyModuleTypes(int maxTier, int slotsTaken, boolean addToList, AlchemyModuleTypes... aliases) {
        this.maxTier = maxTier;
        this.slotsTaken = slotsTaken;
        this.aliases = Arrays.asList(aliases);
        if (addToList)
            toRegister.add(this);
    }

    public boolean isCompatible(AlchemyModuleTypes module) {
        return (this.slotsTaken & module.slotsTaken) == 0;
    }

    public boolean isEquivalent(AlchemyModuleTypes module) {
        return false;
    }

    public ItemAlchemyModule.AlchemyModule readNBT(NBTTagCompound compound) {
        return new ItemAlchemyModule.AlchemyModule(compound);
    }

    public static AlchemyModuleTypes valueOf(String name) {
        return null;
    }


}
