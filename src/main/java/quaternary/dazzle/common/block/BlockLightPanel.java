package quaternary.dazzle.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import quaternary.dazzle.common.Dazzle;
import quaternary.dazzle.common.etc.IUnfinished;

public class BlockLightPanel extends Block implements IUnfinished {
	public static final PropertyDirection FACING = BlockDirectional.FACING;
	
	@GameRegistry.ObjectHolder(Dazzle.MODID + ":invisible_light_source")
	public static final Block INVISIBLE_LIGHT_SOURCE = Blocks.AIR;
	
	private static final double THICKNESS = 2/16d;
	
	//Down, up, north, south, east, west.
	private static final AxisAlignedBB[] AABBs = new AxisAlignedBB[] {
		new AxisAlignedBB(0, 0, 0, 1, THICKNESS, 1),
		new AxisAlignedBB(0, 1-THICKNESS, 0, 1, 1, 1),
		new AxisAlignedBB(0, 0, 0, 1, 1, THICKNESS),
		new AxisAlignedBB(0, 0, 1-THICKNESS, 1, 1, 1),
		new AxisAlignedBB(0, 0, 0, THICKNESS, 1, 1),
		new AxisAlignedBB(1-THICKNESS, 0, 0, 1, 1, 1)
	};
	
	public BlockLightPanel() {
		super(Material.IRON, MapColor.WHITE_STAINED_HARDENED_CLAY);
		
		setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.UP));
	}
	
	//Solidity
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	//side placement
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(FACING, facing.getOpposite());
	}
	
	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
		BlockPos offsetPos = pos.offset(side.getOpposite());
		IBlockState offsetState = world.getBlockState(offsetPos);
		return offsetState.getBlockFaceShape(world, offsetPos, side) == BlockFaceShape.SOLID;
	}
	
	//light casting
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if(!world.isRemote) placeLights(world, pos, world.getBlockState(pos), world.isBlockIndirectlyGettingPowered(pos));
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		if(!world.isRemote) placeLights(world, pos, world.getBlockState(pos), world.isBlockIndirectlyGettingPowered(pos));
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		placeLights(world, pos, state, 0);
	}
	
	private static int lightConeLength = 3;
	private static int lightMaxSpread = 15 * lightConeLength;
	
	private void placeLights(World world, BlockPos pos, IBlockState myState, int power) {
		EnumFacing whichWay = myState.getValue(FACING).getOpposite();
		BlockPos p = pos;
		
		int strength = power;
		
		for(int i = 0; i < lightMaxSpread; i++) {
			p = p.offset(whichWay);
			
			if(strength > 0 && i % lightConeLength == 0) {
				strength--;
			}
			
			if(p.getY() < 0) break;
			if(!world.isBlockLoaded(p)) break;
			
			IBlockState stateToReplace = world.getBlockState(p);
			Block blockToReplace = stateToReplace.getBlock();
			
			if(blockToReplace.isReplaceable(world, p)) {
				if(power == 0) {
					world.setBlockState(p, Blocks.AIR.getDefaultState());
				} else {
					world.setBlockState(p, INVISIBLE_LIGHT_SOURCE.getDefaultState().withProperty(BlockInvisibleLightSource.LIGHT_LEVEL, strength));
				}
			} else {
				strength = 0;
			}
		}
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
		//TODO recheck this.
		return face == state.getValue(FACING).getOpposite() ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}
	
	//Blockstate boilerplate
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).ordinal();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		if(meta >= EnumFacing.values().length) meta = 0;
		return getDefaultState().withProperty(FACING, EnumFacing.values()[meta]);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}
	
	//Aabb
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABBs[state.getValue(FACING).ordinal()];
	}
}
