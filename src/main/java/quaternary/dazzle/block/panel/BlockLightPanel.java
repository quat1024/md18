package quaternary.dazzle.block.panel;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import quaternary.dazzle.Dazzle;
import quaternary.dazzle.block.BlockInvisibleLightSource;

public class BlockLightPanel extends Block {
	public static final PropertyDirection FACING = BlockDirectional.FACING;
	
	public BlockLightPanel() {
		super(Material.IRON, MapColor.WHITE_STAINED_HARDENED_CLAY);
		
		setRegistryName(new ResourceLocation(Dazzle.MODID, "panel"));
		setUnlocalizedName(Dazzle.MODID + ".panel");
		
		setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.UP));
	}
	
	//side placement
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(FACING, facing);
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
					world.setBlockState(p, Dazzle.INVISIBLE_LIGHT.getDefaultState().withProperty(BlockInvisibleLightSource.LIGHT_LEVEL, strength));
				}
			} else {
				strength = 0;
			}
		}
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
}
