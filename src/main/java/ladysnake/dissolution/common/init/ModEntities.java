package ladysnake.dissolution.common.init;

import ladysnake.dissolution.client.renders.entities.RenderBrimstoneFire;
import ladysnake.dissolution.client.renders.entities.RenderMinionPigZombie;
import ladysnake.dissolution.client.renders.entities.RenderMinionSkeleton;
import ladysnake.dissolution.client.renders.entities.RenderMinionStray;
import ladysnake.dissolution.client.renders.entities.RenderMinionWitherSkeleton;
import ladysnake.dissolution.client.renders.entities.RenderMinionZombie;
import ladysnake.dissolution.client.renders.entities.RenderPlayerCorpse;
import ladysnake.dissolution.client.renders.entities.RenderWanderingSoul;
import ladysnake.dissolution.common.Dissolution;
import ladysnake.dissolution.common.Reference;
import ladysnake.dissolution.common.entity.EntityPlayerCorpse;
import ladysnake.dissolution.common.entity.EntityWanderingSoul;
import ladysnake.dissolution.common.entity.boss.EntityMawOfTheVoid;
import ladysnake.dissolution.common.entity.item.EntityBrimstoneFire;
import ladysnake.dissolution.common.entity.minion.EntityMinionPigZombie;
import ladysnake.dissolution.common.entity.minion.EntityMinionSkeleton;
import ladysnake.dissolution.common.entity.minion.EntityMinionStray;
import ladysnake.dissolution.common.entity.minion.EntityMinionWitherSkeleton;
import ladysnake.dissolution.common.entity.minion.EntityMinionZombie;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModEntities {
	
    public static void register() {
    	int id = 0;
    	EntityRegistry.registerModEntity(EntityWanderingSoul.class, "wandering_soul", id++, Dissolution.instance, 64, 1, true, 0xA8E4E4, 0x00D2D2);
    	EntityRegistry.addSpawn(EntityWanderingSoul.class, 50, 1, 1, EnumCreatureType.CREATURE, Biomes.HELL);
    	LootTableList.register(EntityWanderingSoul.LOOT);

    	EntityRegistry.registerModEntity(EntityMinionZombie.class, "minion_zombie", id++, Dissolution.instance, 64, 1, true);
    	EntityRegistry.registerModEntity(EntityMinionPigZombie.class, "minion_pig_zombie", id++, Dissolution.instance, 64, 1, true);
    	EntityRegistry.registerModEntity(EntityMinionSkeleton.class, "minion_skeleton", id++, Dissolution.instance, 64, 1, true);
    	EntityRegistry.registerModEntity(EntityMinionStray.class, "minion_stray", id++, Dissolution.instance, 64, 1, true);
    	EntityRegistry.registerModEntity(EntityMinionWitherSkeleton.class, "minion_wither_skeleton", id++, Dissolution.instance, 64, 1, true);
    	EntityRegistry.registerModEntity(EntityPlayerCorpse.class, "player_corpse", id++, Dissolution.instance, 64, 1, true);
    	EntityRegistry.registerModEntity(EntityMawOfTheVoid.class, "maw_of_the_void", id++, Dissolution.instance, 64, 1, true);
    	EntityRegistry.registerModEntity(EntityBrimstoneFire.class, "brimstone_fire", id++, Dissolution.instance, 32, 1, false);
    }

}
