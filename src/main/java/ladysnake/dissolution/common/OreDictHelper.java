package ladysnake.dissolution.common;

import ladysnake.dissolution.common.init.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.PersistentRegistryManager;
import net.minecraftforge.fml.common.registry.RegistryDelegate;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Arrays;

public class OreDictHelper {

    public static final String PESTLE = "pestle";
    public static final String PESTLE_AND_MORTAR = "pestleAndMortar";

    public static void registerOres() {
        OreDictionary.registerOre(PESTLE, ModItems.PESTLE);
    }

    public static boolean doesItemMatch(ItemStack itemStack, String... ores) {
        return !itemStack.isEmpty() && Arrays.stream(ores).map(OreDictionary::getOreID).anyMatch(id -> Arrays.stream(OreDictionary.getOreIDs(itemStack)).anyMatch(id::equals));
    }
}
