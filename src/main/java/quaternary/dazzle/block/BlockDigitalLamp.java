package quaternary.dazzle.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDigitalLamp extends BlockBase {
	public static final PropertyBool LIT = PropertyBool.create("lit");
	public static final PropertyBool INVERTED = PropertyBool.create("inverted");
	
	public BlockDigitalLamp(EnumDyeColor color) {
		super(color.getDyeColorName() + "_digital_lamp", Material.REDSTONE_LIGHT);
		
		setDefaultState(getDefaultState().withProperty(LIT, false).withProperty(INVERTED, false));
	}
	
	//Inversion
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(player.getHeldItem(hand).getItem() == Item.getItemFromBlock(Blocks.REDSTONE_TORCH)) {
			world.setBlockState(pos, world.getBlockState(pos).withProperty(INVERTED, !state.getValue(INVERTED)));
			return true;
		}
		return false;
	}
	
	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return (state.getValue(LIT) ^ state.getValue(INVERTED)) ? 15 : 0;
	}
	
	//Updating light level
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if(!world.isRemote) updateLevel(world, pos, world.isBlockPowered(pos));
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		if(!world.isRemote) updateLevel(world, pos, world.isBlockPowered(pos));
	}
	
	private void updateLevel(World world, BlockPos pos, boolean powered) {
		world.setBlockState(pos, world.getBlockState(pos).withProperty(LIT, powered));
	}
	
	//Blockstate boilerplate
	@Override
	public int getMetaFromState(IBlockState state) {
		return (state.getValue(LIT) ? 1 : 0) | (state.getValue(INVERTED) ? 2 : 0);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(LIT, (meta & 1) != 0).withProperty(INVERTED, (meta & 2) != 0);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, LIT, INVERTED);
	}
	
	//Statemapper
	@Override
	public boolean hasCustomStatemapper() {
		return true;
	}
	
	@Override
	public IStateMapper getCustomStatemapper() {
		return new StateMap.Builder().ignore(INVERTED).build();
	}
}
