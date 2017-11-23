package ladysnake.dissolution.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemLogo extends Item {

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        tooltip.add("You expected a great item but it was me, Dio !");
        super.addInformation(stack, playerIn, tooltip, advanced);
    }
}
