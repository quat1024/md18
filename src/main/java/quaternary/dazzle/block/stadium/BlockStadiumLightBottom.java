package quaternary.dazzle.block.stadium;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockStadiumLightBottom extends BlockStadiumLightBase {
	public BlockStadiumLightBottom() {
		super("stadium_base", ComponentType.BASE);
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		//is the center ok?
		if(!world.getBlockState(pos).getBlock().isReplaceable(world, pos)) return false;
		
		//are the sides ok?
		for(EnumFacing h : EnumFacing.HORIZONTALS) {
			BlockPos offsetPos = pos.offset(h);
			if(!world.getBlockState(offsetPos).getBlock().isReplaceable(world, offsetPos)) return false;
		}
		
		//cool! let's do it
		return true;
	}
	
	//place surrounding structure blocks
	@GameRegistry.ObjectHolder("dazzle:stadium_base_structure")
	public static final Block BASE_STRUCTURE_BLOCK = Blocks.AIR;
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		for(EnumFacing h : EnumFacing.HORIZONTALS) {
			world.setBlockState(pos.offset(h), BASE_STRUCTURE_BLOCK.getDefaultState());
		}
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		doLighting(world, pos);
		
		//HACKY SPAGHETTI SHIT
		//make sure the structures are present
		int structureCount = 0;
		for(EnumFacing h : EnumFacing.HORIZONTALS) {
			BlockPos offsetPos = pos.offset(h);
			Block b = world.getBlockState(offsetPos).getBlock();
			
			if(b instanceof BlockStadiumLightBottomStructure) {
				structureCount++;
			}
		}
		if(structureCount < 4) world.destroyBlock(pos, true);
		
		super.neighborChanged(state, world, pos, block, fromPos);
	}
	
	public void doLighting(World world, BlockPos pos) {
		BlockPos poleTop = climbPole(world, pos);
		IBlockState topState = world.getBlockState(poleTop);
		Block topBlock = topState.getBlock();
		if(topBlock instanceof BlockStadiumLightTop) {
			int level = ((BlockStadiumLightTop) topBlock).getLightPowerFromBase(world, pos);
			((BlockStadiumLightTop) topBlock).placeLightBlocks(world, poleTop, level);
		}
	}
}
