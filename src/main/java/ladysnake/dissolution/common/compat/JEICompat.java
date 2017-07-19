package ladysnake.dissolution.common.compat;

import ladysnake.dissolution.common.init.ModBlocks;
import ladysnake.dissolution.common.init.ModItems;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class JEICompat implements IModPlugin {

    private IRecipeRegistry recipeRegistry;
    private IModRegistry registry;
	
	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {}

	@Override
	public void registerIngredients(IModIngredientRegistration registry) {}

	@Override
	public void register(IModRegistry registry) {
        this.registry = registry;
        blacklistStuff();
		addInformationTabs();
        CrystallizerRecipeCategory.register(registry);
	}
	
	private void addInformationTabs() {
		addInformationTab(ModBlocks.MERCURIUS_WAYSTONE);
		addInformationTab(ModBlocks.SOUL_EXTRACTOR);
		addInformationTab(ModItems.SEPULTURE);
		addInformationTab(ModItems.SCARAB_OF_ETERNITY);
	}
	
	private void addInformationTab(Block block) {
		this.addInformationTab(Item.getItemFromBlock(block));
	}
	
	private void addInformationTab(Item item) {
		//registry.addIngredientInfo(new ItemStack(item), ItemStack.class, I18n.format("jei.description.dissolution.%s", item.getUnlocalizedName()));
		registry.addDescription(new ItemStack(item), I18n.format("jei.description.dissolution.%s", item.getUnlocalizedName()));
	}
	
	private void blacklistStuff() {
		IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();
		blacklist.addIngredientToBlacklist(new ItemStack(ModItems.DEBUG_ITEM));
	}
	
	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		recipeRegistry = jeiRuntime.getRecipeRegistry();
	}

}
