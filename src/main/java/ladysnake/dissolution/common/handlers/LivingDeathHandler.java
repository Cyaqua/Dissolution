package ladysnake.dissolution.common.handlers;

import ladysnake.dissolution.api.corporeality.IIncorporealHandler;
import ladysnake.dissolution.common.Dissolution;
import ladysnake.dissolution.common.capabilities.CapabilityIncorporealHandler;
import ladysnake.dissolution.common.config.DissolutionConfig;
import ladysnake.dissolution.common.config.DissolutionConfigManager;
import ladysnake.dissolution.common.entity.EntityPlayerCorpse;
import ladysnake.dissolution.common.inventory.DissolutionInventoryHelper;
import ladysnake.dissolution.common.inventory.InventoryPlayerCorpse;
import ladysnake.dissolution.common.registries.SoulStates;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Team;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LivingDeathHandler {

    private static Method destroyVanishingCursedItems;

    static {
        destroyVanishingCursedItems = ReflectionHelper.findMethod(EntityPlayer.class, "destroyVanishingCursedItems", "func_190776_cN");
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof EntityPlayer)
            this.handlePlayerDeath(event);

//        if (event.getSource().getTrueSource() instanceof EntityPlayer)
//            this.handlePlayerKill((EntityPlayer) event.getSource().getTrueSource(), event.getEntityLiving());
    }

    protected void handlePlayerDeath(LivingDeathEvent event) {
        final EntityPlayer p = (EntityPlayer) event.getEntity();
        final IIncorporealHandler corp = CapabilityIncorporealHandler.getHandler(p);
        if (corp.getCorporealityStatus().isIncorporeal() && corp.getPossessed() instanceof EntityLivingBase) {
            ((EntityLivingBase) corp.getPossessed()).attackEntityFrom(event.getSource(), Float.MAX_VALUE);
            corp.setPossessed(null);
        }
        if (!corp.isStrongSoul() || corp.getCorporealityStatus() == SoulStates.SOUL)
            return;
        if (corp.getCorporealityStatus() == SoulStates.ECTOPLASM) {
            corp.setCorporealityStatus(SoulStates.SOUL);
            p.setHealth(20f);
            event.setCanceled(true);
            return;
        }

        corp.getDeathStats().setLastDeathMessage(
                p.getDisplayNameString() + event.getSource().getDeathMessage(p).getUnformattedComponentText());
        corp.getDeathStats().setDeathDimension(p.dimension);

        if (!p.world.isRemote && Dissolution.config.respawn.spawnCorpses) {
            final EntityPlayerCorpse body = new EntityPlayerCorpse(p.world);
            body.setPosition(p.posX, p.posY, p.posZ);
            body.setCustomNameTag(p.getName());
            body.setDecompositionCountdown(EntityPlayerCorpse.MAX_DECAY_TIME);

            if (Dissolution.config.respawn.bodiesHoldInventory) {
                if (Dissolution.config.respawn.bodiesHoldInventory && !p.isSpectator() && !p.world.getGameRules().getBoolean("keepInventory")) {
                    DissolutionInventoryHelper.transferEquipment(p, body);
                    body.setInventory(new InventoryPlayerCorpse(p.inventory.mainInventory, body));
                    p.inventory.clear();
                }
            }

            body.onUpdate();

            p.world.spawnEntity(body);
            body.setPlayer(p.getUniqueID());
        }

        if (Dissolution.config.respawn.skipDeathScreen) {
            if (!p.world.isRemote)
                fakePlayerDeath((EntityPlayerMP) p, event.getSource());
            corp.setCorporealityStatus(Dissolution.config.respawn.respawnCorporealityStatus.status);
            p.setHealth(20f);
            //noinspection ConstantConditions
            if (p.getBedLocation() != null && !Dissolution.config.respawn.respawnInNether && Dissolution.config.respawn.wowLikeRespawn) {
                BlockPos respawnLoc = p.getBedLocation();
                p.setPosition(respawnLoc.getX(), respawnLoc.getY(), respawnLoc.getZ());
            }
            if (Dissolution.config.respawn.respawnInNether && !p.world.isRemote)
                CustomDissolutionTeleporter.transferPlayerToDimension((EntityPlayerMP) p, Dissolution.config.respawn.respawnDimension);
            event.setCanceled(true);
        }
    }

    public static void fakePlayerDeath(EntityPlayerMP player, DamageSource cause) {
        boolean flag = player.world.getGameRules().getBoolean("showDeathMessages");

        if (flag) {
            Team team = player.getTeam();

            if (team != null && team.getDeathMessageVisibility() != Team.EnumVisible.ALWAYS) {
                if (team.getDeathMessageVisibility() == Team.EnumVisible.HIDE_FOR_OTHER_TEAMS) {
                    player.mcServer.getPlayerList().sendMessageToAllTeamMembers(player, player.getCombatTracker().getDeathMessage());
                } else if (team.getDeathMessageVisibility() == Team.EnumVisible.HIDE_FOR_OWN_TEAM) {
                    player.mcServer.getPlayerList().sendMessageToTeamOrAllPlayers(player, player.getCombatTracker().getDeathMessage());
                }
            } else {
                player.mcServer.getPlayerList().sendMessage(player.getCombatTracker().getDeathMessage());
            }
        }

        if (!player.world.getGameRules().getBoolean("keepInventory") && !player.isSpectator()) {
            player.captureDrops = true;
            player.capturedDrops.clear();
            try {
                destroyVanishingCursedItems.invoke(player);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
            player.inventory.dropAllItems();

            net.minecraftforge.event.entity.player.PlayerDropsEvent event = new net.minecraftforge.event.entity.player.PlayerDropsEvent(player, cause, player.capturedDrops, false);
            if (!net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) {
                for (net.minecraft.entity.item.EntityItem item : player.capturedDrops) {
                    player.world.spawnEntity(item);
                }
            }
        }

        for (ScoreObjective scoreobjective : player.world.getScoreboard().getObjectivesFromCriteria(IScoreCriteria.DEATH_COUNT)) {
            Score score = player.getWorldScoreboard().getOrCreateScore(player.getName(), scoreobjective);
            score.incrementScore();
        }

        EntityLivingBase entitylivingbase = player.getAttackingEntity();

        if (entitylivingbase != null) {
            EntityList.EntityEggInfo entitylist$entityegginfo = EntityList.ENTITY_EGGS.get(EntityList.getKey(entitylivingbase));

            if (entitylist$entityegginfo != null) {
                player.addStat(entitylist$entityegginfo.entityKilledByStat);
            }

            //entitylivingbase.func_191956_a(player, player.scoreValue, cause);
        }

        player.clearActivePotions();
        player.addStat(StatList.DEATHS);
        player.takeStat(StatList.TIME_SINCE_DEATH);
        player.extinguish();
        //player.setFlag(0, false);
        player.getCombatTracker().reset();
    }

}
