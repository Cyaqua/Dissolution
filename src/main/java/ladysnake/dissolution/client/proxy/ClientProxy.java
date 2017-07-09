package ladysnake.dissolution.client.proxy;

import ladysnake.dissolution.client.gui.GuiIncorporealOverlay;
import ladysnake.dissolution.client.handlers.EventHandlerClient;
import ladysnake.dissolution.client.renders.blocks.RenderSoulAnchor;
import ladysnake.dissolution.client.renders.entities.RenderBrimstoneFire;
import ladysnake.dissolution.client.renders.entities.RenderMinionPigZombie;
import ladysnake.dissolution.client.renders.entities.RenderMinionSkeleton;
import ladysnake.dissolution.client.renders.entities.RenderMinionStray;
import ladysnake.dissolution.client.renders.entities.RenderMinionWitherSkeleton;
import ladysnake.dissolution.client.renders.entities.RenderMinionZombie;
import ladysnake.dissolution.client.renders.entities.RenderPlayerCorpse;
import ladysnake.dissolution.client.renders.entities.RenderWanderingSoul;
import ladysnake.dissolution.common.entity.EntityPlayerCorpse;
import ladysnake.dissolution.common.entity.EntityWanderingSoul;
import ladysnake.dissolution.common.entity.boss.EntityMawOfTheVoid;
import ladysnake.dissolution.common.entity.item.EntityBrimstoneFire;
import ladysnake.dissolution.common.entity.minion.EntityMinionPigZombie;
import ladysnake.dissolution.common.entity.minion.EntityMinionSkeleton;
import ladysnake.dissolution.common.entity.minion.EntityMinionStray;
import ladysnake.dissolution.common.entity.minion.EntityMinionWitherSkeleton;
import ladysnake.dissolution.common.entity.minion.EntityMinionZombie;
import ladysnake.dissolution.common.handlers.EventHandlerCommon;
import ladysnake.dissolution.common.init.CommonProxy;
import ladysnake.dissolution.common.init.ModBlocks;
import ladysnake.dissolution.common.init.ModEntities;
import ladysnake.dissolution.common.init.ModFluids;
import ladysnake.dissolution.common.init.ModItems;
import ladysnake.dissolution.common.tileentities.TileEntitySoulAnchor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit() {
		super.preInit();
		ModBlocks.registerRenders();
		ModItems.registerRenders();
		registerEntitiesRenders();
		ModFluids.RegisterManager.registerAllModels();
	}
	
	@Override
	public void init() {
		super.init();
		MinecraftForge.EVENT_BUS.register(new EventHandlerClient());
		MinecraftForge.EVENT_BUS.register(new GuiIncorporealOverlay(Minecraft.getMinecraft()));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySoulAnchor.class, new RenderSoulAnchor());
	}
    
    @SideOnly(Side.CLIENT)
    public static void registerEntitiesRenders() {
    	RenderingRegistry.registerEntityRenderingHandler(EntityWanderingSoul.class, RenderWanderingSoul::new);
    	RenderingRegistry.registerEntityRenderingHandler(EntityMinionZombie.class, RenderMinionZombie::new);
    	RenderingRegistry.registerEntityRenderingHandler(EntityMinionPigZombie.class, new RenderMinionPigZombie.Factory());
    	RenderingRegistry.registerEntityRenderingHandler(EntityMinionSkeleton.class, new RenderMinionSkeleton.Factory());
    	RenderingRegistry.registerEntityRenderingHandler(EntityMinionStray.class, new RenderMinionStray.Factory());
    	RenderingRegistry.registerEntityRenderingHandler(EntityMinionWitherSkeleton.class, new RenderMinionWitherSkeleton.Factory());
    	RenderingRegistry.registerEntityRenderingHandler(EntityPlayerCorpse.class, RenderPlayerCorpse::new);
    	RenderingRegistry.registerEntityRenderingHandler(EntityMawOfTheVoid.class, renderManager -> new RenderBiped(renderManager, new ModelBiped(), 1.0f));
    	RenderingRegistry.registerEntityRenderingHandler(EntityBrimstoneFire.class, RenderBrimstoneFire::new);
    }

}
