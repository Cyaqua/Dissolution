package ladysnake.dissolution.client.handlers;

import ladysnake.dissolution.api.PlayerIncorporealEvent;
import ladysnake.dissolution.api.corporeality.IIncorporealHandler;
import ladysnake.dissolution.api.corporeality.IPossessable;
import ladysnake.dissolution.api.corporeality.ISoulInteractable;
import ladysnake.dissolution.client.particles.DissolutionParticleManager;
import ladysnake.dissolution.common.Dissolution;
import ladysnake.dissolution.common.Reference;
import ladysnake.dissolution.common.blocks.BlockFluidMercury;
import ladysnake.dissolution.common.capabilities.CapabilityIncorporealHandler;
import ladysnake.dissolution.common.config.DissolutionConfigManager;
import ladysnake.dissolution.common.networking.PacketHandler;
import ladysnake.dissolution.common.networking.PingMessage;
import ladysnake.dissolution.common.registries.SoulStates;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToFindFieldException;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Reference.MOD_ID)
public class EventHandlerClient {

    /**True if this client is connected to a server without the mod*/
    private static boolean noServerInstall;
    private static int cameraAnimation = 0;

    private static final float SOUL_VERTICAL_SPEED = 0.1f;
    private static MethodHandle highlightingItemStack;
    private static int refreshTimer = 0;

    private static float prevHealth = 20;
    private static double prevMaxHealth = 20;
    private static boolean wasRidingLastTick = false;

    static {
        try {
            Field f = ReflectionHelper.findField(GuiIngame.class, "highlightingItemStack", "field_92016_l");
            highlightingItemStack = MethodHandles.lookup().unreflectSetter(f);
        } catch (UnableToFindFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
//		event.getMap().registerSprite(AdditiveParticle.STAR_PARTICLE_TEXTURE);
    }

    @SubscribeEvent
    public static void onGameTick(TickEvent.ClientTickEvent event) {
        final EntityPlayer player = Minecraft.getMinecraft().player;
        if (player == null || noServerInstall) {
            return;
        }

        DissolutionParticleManager.INSTANCE.updateParticles();

        final IIncorporealHandler playerCorp = CapabilityIncorporealHandler.getHandler(player);

        // Sends a request to the server
        if (!playerCorp.isSynced() && refreshTimer++ % 100 == 0) {
            IMessage msg = new PingMessage(player.getUniqueID().getMostSignificantBits(),
                    player.getUniqueID().getLeastSignificantBits());
            PacketHandler.NET.sendToServer(msg);
        } else if (playerCorp.isSynced()) {
            refreshTimer = 0;
        }

        // Convoluted way of displaying the health of the possessed entity
//        if (playerCorp.getPossessed() instanceof EntityLiving && ((EntityLiving) playerCorp.getPossessed()).getHealth() > 0) {
//            if (!wasRidingLastTick) {
//                prevHealth = player.getHealth();
//                IAttributeInstance maxHealth = player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
//                prevMaxHealth = maxHealth.getAttributeValue();
//                maxHealth.setBaseValue(playerCorp.getPossessed().getPurifiedHealth());
////                        ((EntityLiving) player.getRidingEntity()).getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue());
//                wasRidingLastTick = true;
//            }
//            if (player.getHealth() != ((EntityLiving) player.getRidingEntity()).getHealth())
//                player.setHealth(((EntityLiving) player.getRidingEntity()).getHealth());
//        } else if (wasRidingLastTick) {
//            player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(prevMaxHealth);
//            player.setHealth(prevHealth);
//            wasRidingLastTick = false;
//        }
    }

    @SubscribeEvent
    public static void onClientConnectedToServer(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        refreshTimer = 0;
        // if it's a local connection, the mod is installed
        noServerInstall = Dissolution.noServerInstall && !event.isLocal();
    }

    @SubscribeEvent
    public static void onPlayerIncorporeal(PlayerIncorporealEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        if (player == Minecraft.getMinecraft().player) {
            if (DissolutionConfigManager.isFlightSetTo(DissolutionConfigManager.FlightModes.CUSTOM_FLIGHT)) {
                player.capabilities.setFlySpeed(event.getNewStatus().isIncorporeal() ? 0.025f : 0.05f);
            }
            if (!event.getNewStatus().isIncorporeal()) {
                GuiIngameForge.renderHotbar = true;
                GuiIngameForge.renderFood = true;
                GuiIngameForge.renderArmor = true;
                GuiIngameForge.renderAir = true;
            }
        }
    }

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        IIncorporealHandler handler = CapabilityIncorporealHandler.getHandler(player);
        boolean show = !handler.getCorporealityStatus().isIncorporeal();
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {

            // Disables most gui renders
            GuiIngameForge.renderFood = show;
            GuiIngameForge.renderHotbar = show || player.isCreative() || handler.getPossessed() != null;
            GuiIngameForge.renderHealthMount = show;
            GuiIngameForge.renderArmor = show || handler.getPossessed() != null;
            GuiIngameForge.renderAir = show;

            // Prevents the display of the name of the selected ItemStack
            if (!show && !player.isCreative() && handler.getPossessed() == null) {
                try {
                    highlightingItemStack.invoke(Minecraft.getMinecraft().ingameGUI, ItemStack.EMPTY);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent event) {
        if (noServerInstall) {
            return;
        }

        final EntityPlayer player = event.player;
        final EntityPlayerSP playerSP = Minecraft.getMinecraft().player;
        if (player != playerSP) {
            return;
        }

        final IIncorporealHandler playerCorp = CapabilityIncorporealHandler.getHandler(player);

        if (cameraAnimation-- > 0 && event.player.eyeHeight < 1.8f) {
            player.eyeHeight += player.getDefaultEyeHeight() / 20f;
        }

        if (player.world.isMaterialInBB(player.getEntityBoundingBox()
                .grow(-0.1D, -0.4D, -0.1D), BlockFluidMercury.MATERIAL_MERCURY)) {
            playerSP.motionX *= 0.4f;
            playerSP.motionZ *= 0.4f;
        }

        if (!event.player.isCreative() &&
                playerCorp.getCorporealityStatus() == SoulStates.SOUL && event.phase == TickEvent.Phase.START) {

            if (DissolutionConfigManager.isFlightSetTo(DissolutionConfigManager.FlightModes.CUSTOM_FLIGHT)) {
                player.capabilities.setFlySpeed(0.025f);
                // Makes the player glide and stuff
                if (playerSP.movementInput.jump && player.getRidingEntity() == null) {
                    player.motionY = SOUL_VERTICAL_SPEED;
                    player.velocityChanged = true;
                } else if (player.motionY <= 0 && player.getRidingEntity() == null) {
                    if (player.world.getBlockState(player.getPosition()).getMaterial().isLiquid() ||
                            player.world.getBlockState(player.getPosition().down()).getMaterial().isLiquid()) {
                        player.velocityChanged = true;
                    } else {
                        player.motionY = -0.8f * SOUL_VERTICAL_SPEED;
                        player.fallDistance = 0;
                        player.velocityChanged = true;
                    }
                }
            }
        }
        if (playerCorp.getPossessed() != null) {
            playerCorp.getPossessed().possessTickClient();
        }
    }

    @SubscribeEvent
    public static void onEntityViewRenderCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        EntityPlayerSP playerSP = Minecraft.getMinecraft().player;
        IPossessable possessed = CapabilityIncorporealHandler.getHandler(playerSP).getPossessed();
        if (possessed != null) {
            float yaw = (float) (playerSP.prevRotationYaw + (playerSP.rotationYaw - playerSP.prevRotationYaw) * event.getRenderPartialTicks() + 180.0F);
            float pitch = (float) (playerSP.prevRotationPitch + (playerSP.rotationPitch - playerSP.prevRotationPitch) * event.getRenderPartialTicks());
            event.setYaw(yaw);
            event.setPitch(pitch);
        }
    }

    //    private static RenderWillOWisp<EntityPlayer> renderSoul;

    @SubscribeEvent
    public static void onPlayerRender(RenderPlayerEvent.Pre event) {
        if (noServerInstall) {
            return;
        }

        final IIncorporealHandler playerCorp = CapabilityIncorporealHandler.getHandler(event.getEntityPlayer());
        if (playerCorp.getCorporealityStatus().isIncorporeal()) {
            float alpha = CapabilityIncorporealHandler.getHandler(Minecraft.getMinecraft().player).isStrongSoul() ? 0.8F : 0.05F;
            GlStateManager.color(0.9F, 0.9F, 1.0F, alpha); // Tints the player blue and reduces the transparency
        }/* else if (playerCorp.getCorporealityStatus() == SoulStates.SOUL) {
            if (playerCorp.getPossessed() == null) {
                if (renderSoul == null)
                    renderSoul = new RenderWillOWisp<>(Minecraft.getMinecraft().getRenderManager());
                renderSoul.doRender(event.getEntityPlayer(), event.getX(), event.getY(), event.getZ(),
                        event.getEntityPlayer().getRotationYawHead(), event.getPartialRenderTick());
            }
            event.setCanceled(true);
        }*/
        event.getRenderer().shadowOpaque = playerCorp.getCorporealityStatus().isIncorporeal() ? 0F : 1F;
    }

    @SubscribeEvent
    public static void onPlayerRender(RenderPlayerEvent.Post event) {
        if (!noServerInstall && CapabilityIncorporealHandler.getHandler(event.getEntityPlayer()).getCorporealityStatus().isIncorporeal()) {
            GlStateManager.color(0, 0, 0, 0);
        }
    }

    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent event) {
        DissolutionParticleManager.INSTANCE.renderParticles(event.getPartialTicks());
    }

    @SubscribeEvent
    public static void onRenderSpecificHand(RenderSpecificHandEvent event) {
        if (noServerInstall) {
            return;
        }
        EntityPlayerSP playerSP = Minecraft.getMinecraft().player;
        IIncorporealHandler handler = CapabilityIncorporealHandler.getHandler(playerSP);
        if (handler.getCorporealityStatus().isIncorporeal()) {
            IPossessable possessed = handler.getPossessed();
            if (possessed == null && !playerSP.isCreative() || event.getItemStack().isEmpty()) {
                event.setCanceled(true);
            } else if (possessed instanceof EntityLivingBase && (event.getHand() == EnumHand.MAIN_HAND || playerSP.getHeldItemMainhand().isEmpty())) {
                EntityLivingBase livingPossessed = (EntityLivingBase) possessed;
                float f1 = playerSP.prevRotationPitch + (playerSP.rotationPitch - playerSP.prevRotationPitch) * event.getPartialTicks();
                float f2 = playerSP.prevRotationYaw + (playerSP.rotationYaw - playerSP.prevRotationYaw) * event.getPartialTicks();
                float f3 = livingPossessed.prevRotationPitch + (livingPossessed.rotationPitch - livingPossessed.prevRotationPitch) * event.getPartialTicks();
                float f4 = livingPossessed.prevRotationYaw + (livingPossessed.rotationYaw - livingPossessed.prevRotationYaw) * event.getPartialTicks();
                rotateArmReverse(playerSP, livingPossessed, f3, f4, event.getPartialTicks());
                rotateAroundXAndYReverse(f1, f2, f3, f4);
            }
        }
    }

    private static void rotateArmReverse(EntityPlayerSP entityplayersp, EntityLivingBase possessed, float f2, float f3, float partialTicks) {
        float f = entityplayersp.prevRenderArmPitch + (entityplayersp.renderArmPitch - entityplayersp.prevRenderArmPitch) * partialTicks;
        float f1 = entityplayersp.prevRenderArmYaw + (entityplayersp.renderArmYaw - entityplayersp.prevRenderArmYaw) * partialTicks;
        GlStateManager.rotate(((possessed.rotationPitch - f2) - (entityplayersp.rotationPitch - f)) * 0.1F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(((possessed.rotationYaw - f3) - (entityplayersp.rotationYaw - f1)) * 0.1F, 0.0F, 1.0F, 0.0F);
    }

    private static void rotateAroundXAndYReverse(float angle, float angleY, float angle1, float angleY1) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(angle1 - angle, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(angleY1 - angleY, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    @SubscribeEvent
    public static void onDrawBlockHighlight(DrawBlockHighlightEvent event) {
        if (!noServerInstall && event.getTarget().typeOfHit == RayTraceResult.Type.BLOCK) {
            event.setCanceled(CapabilityIncorporealHandler.getHandler(event.getPlayer()).getCorporealityStatus().isIncorporeal() &&
                    !(event.getPlayer().world.getBlockState(event.getTarget().getBlockPos()).getBlock() instanceof ISoulInteractable));
        }
    }
}
