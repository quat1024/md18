package quaternary.dazzle.block.stadium;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import quaternary.dazzle.block.BlockBase;

import javax.annotation.Nullable;

public class BlockStadiumLightBase extends BlockBase {
	public BlockStadiumLightBase() {
		super("stadium_base", Material.IRON);
	}
	
	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}
	
	//remove the top of the pole
	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
		StadiumLightUtils.breakPole(world, pos.up(), !player.capabilities.isCreativeMode);
		super.harvestBlock(world, player, pos, state, te, stack);
	}
}
