package quaternary.dazzle.block.stadium;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import quaternary.dazzle.block.BlockBase;

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
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		BlockPos p = pos.up();
		while(world.getBlockState(p).getBlock() instanceof BlockStadiumLightPole) {
			world.destroyBlock(p, true);
			p = p.up();
		}
		
		super.breakBlock(world, pos, state);
	}
}
