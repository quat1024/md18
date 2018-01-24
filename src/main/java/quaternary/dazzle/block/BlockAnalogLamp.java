package quaternary.dazzle.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockAnalogLamp extends BlockBase {
	public static final PropertyInteger LIGHT_LEVEL = PropertyInteger.create("light_level", 0, 15);
	
	public BlockAnalogLamp() {
		super("analog_lamp", Material.REDSTONE_LIGHT);
	}
	
	//Light level based on states
	@Override
	public int getLightValue(IBlockState state) {
		return state.getValue(LIGHT_LEVEL);
	}
	
	//Updating light level
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if(!world.isRemote) updateLevel(world, pos, world.isBlockIndirectlyGettingPowered(pos));
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		if(!world.isRemote) updateLevel(world, pos, world.isBlockIndirectlyGettingPowered(pos));
	}
	
	private void updateLevel(World world, BlockPos pos, int level) {
		level = MathHelper.clamp(level, 0, 15); //sanity check
		world.setBlockState(pos, getDefaultState().withProperty(LIGHT_LEVEL, level));
	}
	
	//Blockstate boilerplate
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
}
