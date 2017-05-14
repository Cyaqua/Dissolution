package ladysnake.dissolution.common.items;

import ladysnake.dissolution.common.Reference;
import ladysnake.dissolution.common.TartarosConfig;
import ladysnake.dissolution.common.capabilities.IncorporealDataHandler;
import ladysnake.dissolution.common.handlers.CustomTartarosTeleporter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ItemDebug extends Item {
	
	protected int debugWanted = 0;

	public ItemDebug() {
		super();
		this.setUnlocalizedName(Reference.Items.DEBUG.getUnlocalizedName());
        this.setRegistryName(Reference.Items.DEBUG.getRegistryName());
        this.setMaxStackSize(1);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if(playerIn.isSneaking()) {
			if(!worldIn.isRemote) {
				debugWanted = (debugWanted + 1) % 5;
				playerIn.sendStatusMessage(new TextComponentTranslation("debug: " + debugWanted, new Object[0]));
			}
			return super.onItemRightClick(itemStack, worldIn, playerIn, handIn);
		}
		switch(debugWanted) {
		case 0 : 
			playerIn.sendStatusMessage(new TextComponentTranslation("dissolution.jei.recipe.crystallizer", new Object[0]));
			break;
		case 1 :	
			IncorporealDataHandler.getHandler(playerIn).setIncorporeal(!IncorporealDataHandler.getHandler(playerIn).isIncorporeal(), playerIn);
			break;
		case 2 :
			if(!playerIn.world.isRemote)
				CustomTartarosTeleporter.transferPlayerToDimension((EntityPlayerMP) playerIn, playerIn.dimension == -1 ? 0 : -1);
			break;
		case 3 :
			if(!playerIn.world.isRemote) {
				TartarosConfig.flightMode = TartarosConfig.flightMode + 1;
				if(TartarosConfig.flightMode > 3) TartarosConfig.flightMode = -1;
				playerIn.sendStatusMessage(new TextComponentTranslation("flight mode : " + TartarosConfig.flightMode, new Object[0]));
			} 
			break;
		case 4 :
			playerIn.sendStatusMessage(new TextComponentTranslation("flight speed:" + playerIn.capabilities.getFlySpeed()));
			break;
		default : break;
		}
		return super.onItemRightClick(itemStack, worldIn, playerIn, handIn);
	}
}
