package quaternary.dazzle.block;

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
import quaternary.dazzle.tile.TileEntityLightSensor;

import javax.annotation.Nullable;

public class BlockLightSensor extends BlockBase {
	//the side it's facing: the "face"
	//the opposite side: the area power is output
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	
	public BlockLightSensor() {
		super("light_sensor", Material.IRON);
		
		setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.UP));
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(FACING,EnumFacing.getDirectionFromEntityLiving(pos, placer));
	}
	
	//powering
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
		
		World w = (World) blockAccess;
		BlockPos sensedPos = pos.offset(sensedDirection);
		
		int lightLevel = w.getLightFor(EnumSkyBlock.BLOCK, sensedPos);
		System.out.println("getweakpower " + lightLevel);
		return lightLevel;
	}
	
	//tile
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityLightSensor();
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
