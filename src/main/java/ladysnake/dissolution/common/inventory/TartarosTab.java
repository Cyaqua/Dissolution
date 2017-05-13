package ladysnake.dissolution.common.inventory;

import ladysnake.dissolution.common.Reference;
import ladysnake.dissolution.common.items.ItemBaseResource;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class TartarosTab extends CreativeTabs {

	public TartarosTab() {
		super(Reference.MOD_ID);
	}
	
	@Override
	public Item getTabIconItem() {
		return ItemBaseResource.resourceFromName("ectoplasm").getItem();
	}

}
