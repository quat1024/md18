package quaternary.dazzle.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import quaternary.dazzle.common.tile.TileLightSensor;

import javax.annotation.Nullable;

public class BlockLightSensor extends Block {
	//the side it's facing: the "face"
	//the opposite side: the area power is output
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	
	public BlockLightSensor() {
		super(Material.IRON);
		
		setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.UP));
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(FACING,EnumFacing.getDirectionFromEntityLiving(pos, placer));
	}
	
	//powering
	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
		return state.getValue(FACING) == side;
	}
	
	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}
	
	@Override
	public int getStrongPower(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		if(blockAccess instanceof ChunkCache) return 0;
		
		return getWeakPower(state, blockAccess, pos, side);
	}
	
	@Override
	public int getWeakPower(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		if(blockAccess instanceof ChunkCache) return 0;
		
		EnumFacing sensedDirection = state.getValue(FACING);
		if(side != sensedDirection) return 0;
		
		return ((TileLightSensor) blockAccess.getTileEntity(pos)).lightLevel;
	}
	
	//tile
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileLightSensor();
	}
	
	//blockstate boilerplate
	@Override
	public IBlockState getStateFromMeta(int meta) {
		if(meta >= EnumFacing.values().length) meta = 0;
		return getDefaultState().withProperty(FACING, EnumFacing.values()[meta]);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).ordinal();
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}
}
