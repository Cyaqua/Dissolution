package ladysnake.dissolution.client.proxy;

import ladysnake.dissolution.client.gui.GuiIncorporealOverlay;
import ladysnake.dissolution.client.handlers.EventHandlerClient;
import ladysnake.dissolution.client.renders.blocks.RenderSoulAnchor;
import ladysnake.dissolution.common.handlers.EventHandlerCommon;
import ladysnake.dissolution.common.init.CommonProxy;
import ladysnake.dissolution.common.init.ModBlocks;
import ladysnake.dissolution.common.init.ModEntities;
import ladysnake.dissolution.common.init.ModFluids;
import ladysnake.dissolution.common.init.ModItems;
import ladysnake.dissolution.common.tileentities.TileEntitySoulAnchor;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit() {
		super.preInit();
		ModBlocks.registerRenders();
		ModItems.registerRenders();
		ModEntities.registerRenders();
		ModFluids.RegisterManager.registerAllModels();
	}
	
	@Override
	public void init() {
		super.init();
		MinecraftForge.EVENT_BUS.register(new EventHandlerClient());
		MinecraftForge.EVENT_BUS.register(new GuiIncorporealOverlay(Minecraft.getMinecraft()));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySoulAnchor.class, new RenderSoulAnchor());
	}

}
