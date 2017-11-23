package ladysnake.dissolution.common.init;

import ladysnake.dissolution.common.items.ItemBaseResource;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Class previously used to register recipes
 *
 * @author Pyrofab
 */
public class ModCrafting {

    public static void register() {
//		GameRegistry.addShapelessRecipe(ItemBaseResource.resourceFromName("ectoplasm", 9), ModBlocks.ECTOPLASM);
//		GameRegistry.addShapelessRecipe(ItemBaseResource.resourceFromName("ectoplasma", 9), ModBlocks.ECTOPLASMA);
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.EYE_OF_THE_UNDEAD), "IGI", "IEI", "ISI", 'I', Items.IRON_INGOT, 'G', Blocks.GLASS, 'E', Items.ENDER_EYE, 'S', ItemBaseResource.resourceFromName("ectoplasma"));
//		GameRegistry.addShapedRecipe(new ItemStack(ModItems.SEPULTURE), "CCC", "QGS", 'C', new ItemStack(Blocks.STONE_SLAB, 1, 0), 'Q', Items.QUARTZ, 'G', ModItems.SOUL_GEM, 'S', new ItemStack(Items.SKULL, 1, 1));
//		GameRegistry.addShapedRecipe(new ItemStack(Blocks.SOUL_SAND, 8), "SSS", "SBS", "SSS", 'S', Blocks.SAND, 'B', ModItems.SOUL_IN_A_BOTTLE);
    }
}
