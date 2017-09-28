package ladysnake.dissolution.common.items;

import com.google.common.collect.Multimap;
import ladysnake.dissolution.api.SoulTypes;
import ladysnake.dissolution.common.inventory.DissolutionInventoryHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemScythe extends ItemSword implements ICustomLocation {

	protected float attackSpeed, attackRadius;
	protected boolean alreadyRunningAOE;
	
	public ItemScythe(ToolMaterial material) {
		super(material);
        this.setMaxStackSize(1);
        this.attackSpeed = -3.5f;
        this.attackRadius = 2.0f;
        this.alreadyRunningAOE = false;
	}

	protected static Map<Material, Float> effectiveBlocks = new HashMap<Material, Float>();
	
	static {
		effectiveBlocks.put(Material.PLANTS, 2.0f);
		effectiveBlocks.put(Material.VINE, 2.0f);
		effectiveBlocks.put(Material.LEAVES, 1.5f);
	}

	
	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
		return !entityLiving.getHeldItemOffhand().isEmpty();
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, @Nonnull EntityLivingBase attacker) {
		stack.damageItem(1, attacker);
		return !attacker.getHeldItemOffhand().isEmpty();
	}
	
	@Override
	public float getDamageVsEntity() {
		return super.getDamageVsEntity();
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity target)
    {
        if(!player.getHeldItemOffhand().isEmpty()) return true;
        if(alreadyRunningAOE) return false;
        Integer initialCooldown = 100;
        player.spawnSweepParticles();
        int initialDamage = stack.getItemDamage();
        try {
        	initialCooldown = ObfuscationReflectionHelper.getPrivateValue(EntityLivingBase.class, player, new String[]{"ticksSinceLastSwing", "field_184617_aD"});
        } catch (Exception e) {
        	e.printStackTrace();
        }
        alreadyRunningAOE = true;
        AxisAlignedBB aoe = new AxisAlignedBB(target.posX - attackRadius, target.posY - 1, target.posZ - attackRadius, target.posX + attackRadius, target.posY + 1, target.posZ + attackRadius);
        List<EntityLiving> targets = target.world.getEntitiesWithinAABB(EntityLiving.class, aoe);
        if(!targets.isEmpty())
	        for(EntityLiving entity : targets) {
	        	try {
	        		ObfuscationReflectionHelper.setPrivateValue(EntityLivingBase.class, player, initialCooldown, "ticksSinceLastSwing", "field_184617_aD");
				} catch (Exception e) {
					e.printStackTrace();
				}
	        	player.attackTargetEntityWithCurrentItem(entity);
	        }
        alreadyRunningAOE = false;
        stack.setItemDamage(--initialDamage);
        return true;
    }
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add("Two-handed");
	}
	
	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state) {
		Material material = state.getMaterial();
        return effectiveBlocks.get(material) == null ? 0.8F : effectiveBlocks.get(material);
	}
	
	@Override
	public int getItemEnchantability()
    {
        return super.getItemEnchantability();
    }
	
	@Override
	public boolean onBlockDestroyed(@Nonnull ItemStack stack, @Nonnull World worldIn, IBlockState state,
									@Nonnull BlockPos pos, @Nonnull EntityLivingBase entityLiving)
    {
        if ((double)state.getBlockHardness(worldIn, pos) != 0.0D)
        {
            stack.damageItem(2, entityLiving);
        } else {
        	stack.damageItem((worldIn.rand.nextInt(5) == 0) ? 1 : 0, entityLiving);
        }

        return true;
    }
	
	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
        {
        	multimap.removeAll(SharedMonsterAttributes.ATTACK_SPEED.getName());
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", (double)this.attackSpeed, 0));
        }

        return multimap;
    }
	
	/**
	 * Fills an empty bottle in the wielder's inventory with a soul
	 * @param killer the player wielding this scythe
	 */
	public void harvestSoul(EntityPlayer killer, EntityLivingBase victim) {
		if(victim.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) return;
		if(!(victim instanceof EntityLiving)) return;
		this.fillBottle(killer, 1, SoulTypes.getSoulFor((EntityLiving)victim));
	}
	
	public void fillBottle(EntityPlayer p, int nb, SoulTypes soul) {
		ItemStack bottle = DissolutionInventoryHelper.findItem(p, Items.GLASS_BOTTLE);
		if (!bottle.isEmpty()) {
			bottle.shrink(nb);
			p.inventory.addItemStackToInventory(ItemSoulInABottle.newTypedSoulBottle(soul));
		}
	}

	@Override
	public ModelResourceLocation getModelLocation() {
		//noinspection ConstantConditions
		return new ModelResourceLocation(this.getRegistryName().getResourceDomain() + ":scythes/" + this.getRegistryName().getResourcePath());
	}
	
}
