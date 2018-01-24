package quaternary.dazzle.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;
import java.util.Collections;

public class BlockInvisibleLightSource extends BlockBase {
	
	public static final PropertyInteger LIGHT_LEVEL = PropertyInteger.create("light_level", 0, 15);
	
	public BlockInvisibleLightSource() {
		//                                vvv The closest thing to Material.AIR that isn't.
		super("invisible_light_source", Material.STRUCTURE_VOID);
		
		setDefaultState(getDefaultState().withProperty(LIGHT_LEVEL, 15));
	}
	
	//signals to BlockBase
	@Override
	public boolean hasItemForm() {
		return false;
	}
	
	@Override
	public boolean hasCustomStatemapper() {
		return true;
	}
	
	@Override
	public IStateMapper getCustomStatemapper() {
		return block -> Collections.emptyMap();
	}
	
	//Light level based on states
	@Override
	public int getLightValue(IBlockState state) {
		return state.getValue(LIGHT_LEVEL);
	}
	
	//Blockstate boilerplate lol
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(LIGHT_LEVEL);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(LIGHT_LEVEL, meta);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, LIGHT_LEVEL);
	}
	
	//Make it invisible
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
	}
	
	//Make it invisible super for real
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
	
	//Make it non collideable
	public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid)
	{
		return false;
	}
	
	@Nullable
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return NULL_AABB;
	}
	
	//Uhh
	@Override
	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
	{
		return true;
	}
}
