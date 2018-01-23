package quaternary.dazzle.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import quaternary.dazzle.Dazzle;

public class BlockInvisibleLightSource extends Block {
	public BlockInvisibleLightSource() {
		//The closest thing to Material.AIR that isn't.
		super(Material.STRUCTURE_VOID);
		
		setLightLevel(1f);
		
		setRegistryName(new ResourceLocation(Dazzle.MODID, "invisible_light_source"));
		setUnlocalizedName(Dazzle.MODID + ".invisiblelightsource");
	}
	
	//Make it invisible
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
	}
	
	//make it invisible super for real
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
	
	@Override //VERY GOOD MOJANGLE
	public boolean isBlockNormalCube(IBlockState state) {
		return false;
	}
	
	public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid)
	{
		return false;
	}
	
	@Override
	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
	{
		return true;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return BlockFaceShape.UNDEFINED;
	}
}
