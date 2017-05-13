package ladysnake.dissolution.common.inventory;

import javax.annotation.Nullable;

import ladysnake.dissolution.common.items.ItemSoulInABottle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

/**
 * Provides various methods for managing inventories
 * @author Pyrofab
 *
 */
public abstract class Helper {

	/**
	 * Finds an item in the player inventory
	 * @param player The player to search
	 * @param item The item
	 * @return The first ItemStack in the target inventory corresponding to the item
	 */
	public static ItemStack findItem(EntityPlayer player, Item target)
    {
        if (isItem(player.getHeldItem(EnumHand.OFF_HAND), target))
        {
            return player.getHeldItem(EnumHand.OFF_HAND);
        }
        else if (isItem(player.getHeldItem(EnumHand.MAIN_HAND), target))
        {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        }
        else
        {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
            {
                ItemStack itemstack = player.inventory.getStackInSlot(i);

                if (isItem(itemstack, target))
                {
                    return itemstack;
                }
            }

            return null;
        }
    }
	
	protected static boolean isItem(@Nullable ItemStack searchedStack, Item targetItem)
    {
        return searchedStack != null && searchedStack.getItem() == targetItem;
    }
	
	public static ItemStack findItemInstance(EntityPlayer player, Item item) {
		return findItemInstance (player, item.getClass());
	}

	public static ItemStack findItemInstance(EntityPlayer player, Class<? extends Item> item)
    {
        if (item.isInstance(player.getHeldItem(EnumHand.OFF_HAND).getItem()))
        {
            return player.getHeldItem(EnumHand.OFF_HAND);
        }
        else if (item.isInstance(player.getHeldItem(EnumHand.MAIN_HAND)))
        {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        }
        else
        {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
            {
                ItemStack itemstack = player.inventory.getStackInSlot(i);

                if (item.isInstance(itemstack))
                {
                    return itemstack;
                }
            }

            return (ItemStack)null;
        }
    }

	public static boolean compareItemStacks(ItemStack stack1, ItemStack stack2)
	{
	    return stack1 != null && stack2 != null && stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
	}
}
