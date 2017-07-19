package ladysnake.dissolution.common.init;

import ladysnake.dissolution.common.Dissolution;
import ladysnake.dissolution.common.Reference;
import ladysnake.dissolution.common.blocks.BlockCrystallizer;
import ladysnake.dissolution.common.blocks.BlockDriedLava;
import ladysnake.dissolution.common.blocks.BlockEctoplasm;
import ladysnake.dissolution.common.blocks.BlockMercuriusWaystone;
import ladysnake.dissolution.common.blocks.BlockMercuryCandle;
import ladysnake.dissolution.common.blocks.BlockResuscitator;
import ladysnake.dissolution.common.blocks.BlockSepulture;
import ladysnake.dissolution.common.blocks.BlockSoulAnchor;
import ladysnake.dissolution.common.blocks.BlockSoulExtractor;
import ladysnake.dissolution.common.blocks.BlockSulfurCandle;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {

	public static Block ECTOPLASMA;
	public static BlockEctoplasm ECTOPLASM;
    public static BlockCrystallizer CRYSTALLIZER;
    public static BlockMercuriusWaystone MERCURIUS_WAYSTONE;
    public static BlockSoulAnchor SOUL_ANCHOR;
    public static BlockMercuryCandle MERCURY_CANDLE;
    public static BlockSulfurCandle SULFUR_CANDLE;
    public static BlockSoulExtractor SOUL_EXTRACTOR;
    public static BlockSepulture SEPULTURE;
    
    public static void init() {
    	CRYSTALLIZER = new BlockCrystallizer();
    	ECTOPLASM = new BlockEctoplasm();
    	ECTOPLASMA = new Block(Material.CLOTH);
    	ECTOPLASMA.setUnlocalizedName(Reference.Blocks.ECTOPLASMA.getUnlocalizedName());
    	ECTOPLASMA.setRegistryName(Reference.Blocks.ECTOPLASMA.getRegistryName());
    	ECTOPLASMA.setHardness(0.5f);
    	MERCURIUS_WAYSTONE = new BlockMercuriusWaystone();
    	SOUL_ANCHOR = new BlockSoulAnchor();
    	MERCURY_CANDLE = new BlockMercuryCandle();
    	SULFUR_CANDLE = new BlockSulfurCandle();
    	SOUL_EXTRACTOR = new BlockSoulExtractor();
    	SEPULTURE = new BlockSepulture();
    }
    
    public static void register() {
    	registerBlock(CRYSTALLIZER);
    	//registerBlock(DRIED_LAVA);
    	registerBlock(ECTOPLASMA);
    	registerBlock(ECTOPLASM);
    	registerBlock(MERCURIUS_WAYSTONE).setMaxStackSize(1);
    	registerBlock(SOUL_ANCHOR);
    	registerBlock(MERCURY_CANDLE);
    	registerBlock(SULFUR_CANDLE);
    	GameRegistry.register(SEPULTURE);
    	registerBlock(SOUL_EXTRACTOR);
    	//registerBlock(RESUSCITATOR);
    }
    
    static Item registerBlock(Block block) {
    	return registerBlock(block, true);
    }
    
    static Item registerBlock(Block block, boolean putInCreativeTab) {
    	GameRegistry.register(block);
    	ItemBlock item = new ItemBlock(block);
    	item.setRegistryName(block.getRegistryName());
    	GameRegistry.register(item);
    	if(putInCreativeTab)
    		block.setCreativeTab(Dissolution.CREATIVE_TAB);
    	return item;
    }
    
    @SideOnly(Side.CLIENT)
    public static void registerRenders() {
    	registerRender(CRYSTALLIZER);
    	//registerRender(DRIED_LAVA);
    	registerRender(SOUL_EXTRACTOR);
    	registerRender(MERCURIUS_WAYSTONE);
    	registerRender(SEPULTURE);
    	registerRender(ECTOPLASM);
    	registerRender(ECTOPLASMA);
    	registerRender(SOUL_ANCHOR);

    	//registerRender(RESUSCITATOR);

    	registerRender(MERCURY_CANDLE);
    	registerRender(SULFUR_CANDLE);
    }
    
    @SideOnly(Side.CLIENT)
    public static void registerRender(Block block) {
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + block.getUnlocalizedName().toString().substring(5)));
    }
}
