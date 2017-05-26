package ladysnake.dissolution.common.blocks;

import javax.annotation.Nullable;

import ladysnake.dissolution.common.Reference;
import ladysnake.dissolution.common.capabilities.IIncorporealHandler;
import ladysnake.dissolution.common.capabilities.IncorporealDataHandler;
import ladysnake.dissolution.common.tileentities.TileEntitySoulCandle;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSulfurCandle extends AbstractCandle {
	
	public BlockSulfurCandle() {
		super(Material.GLASS);
		this.setUnlocalizedName(Reference.Blocks.SULFUR_CANDLE.getUnlocalizedName());
		this.setRegistryName(Reference.Blocks.SULFUR_CANDLE.getRegistryName());
		this.setHardness(1.0f);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, @Nullable ItemStack stack, EnumFacing facing, float hitX, float hitY, float hitZ) {
		final IIncorporealHandler playerCorp = IncorporealDataHandler.getHandler(playerIn);
		playerCorp.setSoulCandleNearby(true, 2);
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntitySoulCandle();
	}
	
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
}
