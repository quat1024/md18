package quaternary.dazzle.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import quaternary.dazzle.common.etc.EnumLampVariant;
import quaternary.dazzle.common.etc.Util;

public abstract class BlockLamp extends Block {
	final EnumDyeColor color;
	final EnumLampVariant variant;
	
	public BlockLamp(EnumDyeColor color, EnumLampVariant variant) {
		super(Material.REDSTONE_LIGHT);
		
		this.color = color;
		this.variant = variant;
		
		setHardness(.3f);
		setSoundType(SoundType.GLASS);
	}
	
	abstract int getBrightnessFromState(IBlockState state);
	abstract IBlockState setStateBrightness(IBlockState state, int powerLevel);
	abstract IBlockState getInvertedState(IBlockState in);
	
	public abstract boolean hasItemForm();
	
	public EnumDyeColor getColor() {
		return color;
	}
	
	public EnumLampVariant getVariant() {
		return variant;
	}
	
	//Lightiness
	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return getBrightnessFromState(state);
	}
	
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if(!world.isRemote) updateLevel(world, pos, world.isBlockIndirectlyGettingPowered(pos));
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		if(!world.isRemote) updateLevel(world, pos, world.isBlockIndirectlyGettingPowered(pos));
	}
	
	private void updateLevel(World world, BlockPos pos, int level) {
		world.setBlockState(pos, setStateBrightness(world.getBlockState(pos), level));
	}
	
	//Interactions
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(player.getHeldItem(hand).getItem() == Item.getItemFromBlock(Blocks.REDSTONE_TORCH)) {
			world.setBlockState(pos, getInvertedState(state));
			return true;
		}
		return false;
	}
	
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	@SideOnly(Side.CLIENT)
	public int getBlockColor(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
		Block block = state.getBlock();
		
		int color = ((BlockLamp) block).color.getColorValue();
		
		int r = (color & 0xFF0000) >> 16;
		int g = (color & 0x00FF00) >> 8;
		int b = (color & 0x0000FF);
		
		double divisor = Util.map(getBrightnessFromState(state), 0, 15, 5, 1);
		
		divisor = Math.pow(divisor, 1.7); //Oh god it's awful
		divisor = MathHelper.clampedLerp(1, 5, divisor);
		
		r /= divisor;
		g /= divisor;
		b /= divisor;
		
		return (r << 16) | (g << 8) | b;
	}
}
