package quaternary.dazzle.block;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import quaternary.dazzle.block.statemapper.RenamedIgnoringStatemapper;
import quaternary.dazzle.item.ItemBlockLamp;

import java.util.Collection;
import java.util.Collections;

public class BlockDigitalLamp extends BlockBase {
	public static final PropertyBool LIT = PropertyBool.create("lit");
	public static final PropertyBool INVERTED = PropertyBool.create("inverted");
	
	private final EnumDyeColor color;
	private final String variant;
	
	public BlockDigitalLamp(EnumDyeColor color, String variant) {
		super(color.getDyeColorName() + "_" + variant + "_digital_lamp", Material.REDSTONE_LIGHT);
		this.color = color;
		this.variant = variant;
		
		setDefaultState(getDefaultState().withProperty(LIT, false).withProperty(INVERTED, false));
	}
	
	Item item;
	@Override
	public Item getItemForm() {
		if(item == null) {
			item = new ItemBlockLamp(this, color, variant, "tile.dazzle.digital_lamp.name");
		}
		return item;
	}
	
	//Allow for transparency in layers when this gets iblockcolored
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	//IBlockColor stuff
	@Override
	public boolean hasBlockColors() {
		return true;
	}
	
	@Override
	public IBlockColor getBlockColors() {
		return (state, worldIn, pos, tintIndex) -> {
			int color = this.color.getColorValue();
			
			int r = (color & 0xFF0000) >> 16;
			int g = (color & 0x00FF00) >> 8;
			int b = (color & 0x0000FF);
			
			if(!isLit(state)) {
				r /= 5;
				g /= 5;
				b /= 5;
			}
			
			return (r << 16) | (g << 8) | b;
		};
	}
	
	@Override
	public IItemColor getItemColors() {
		return (stack, tintIndex) -> color.getColorValue();
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
	
	public boolean isLit(IBlockState state) {
		return state.getValue(LIT) ^ state.getValue(INVERTED);
	}
	
	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return isLit(state) ? 15 : 0;
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
		return new RenamedIgnoringStatemapper("lamp_" + variant);
	}
}
