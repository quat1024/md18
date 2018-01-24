package quaternary.dazzle.block.stadium;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import quaternary.dazzle.Dazzle;

public class BlockStadiumLightPole extends Block {
	public BlockStadiumLightPole() {
		super(Material.IRON);
		
		setRegistryName(new ResourceLocation(Dazzle.MODID, "stadium_pole"));
		setUnlocalizedName(Dazzle.MODID + ".stadiumpole");
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		Block b = world.getBlockState(pos.down()).getBlock();
		return super.canPlaceBlockAt(world, pos) && (b instanceof BlockStadiumLightBase || b instanceof  BlockStadiumLightPole);
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
