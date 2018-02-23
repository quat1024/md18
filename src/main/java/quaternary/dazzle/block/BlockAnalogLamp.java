package quaternary.dazzle.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import quaternary.dazzle.block.statemapper.RenamedIgnoringStatemapper;
import quaternary.dazzle.compat.shaderlights.IDazzleStaticLight;
import quaternary.dazzle.item.ItemBlockLamp;

public class BlockAnalogLamp extends BlockBase implements IDazzleStaticLight {
	public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
	
	public final EnumDyeColor color;
	private final String variant;
	
	private final boolean inverted;
	private IBlockState inverseState;
	
	public BlockAnalogLamp(EnumDyeColor c, String variant, boolean inverted) {
		super((inverted ? "inverted_" : "") + c.getName() + "_" + variant + "_analog_lamp", Material.REDSTONE_LIGHT);
		
		this.inverted = inverted;
		this.variant = variant;
		this.color = c;
	}
	
	Item item;
	@Override
	public Item getItemForm() {
		if(item == null) {
			item = new ItemBlockLamp(this, color, variant, "tile.dazzle.analog_lamp.name");
		}
		return item;
	}
	
	//Block colors
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasBlockColors() {
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IBlockColor getBlockColors() {
		return (state, worldIn, pos, tintIndex) -> {
			if(tintIndex != 0) return -1;
			
			Block block = state.getBlock();
			
			int color = ((BlockAnalogLamp)block).color.getColorValue();
			
			int r = (color & 0xFF0000) >> 16;
			int g = (color & 0x00FF00) >> 8;
			int b = (color & 0x0000FF);
			
			//This is just a really lazy rgb lerp so it needs a little finaggling to look nice.
			double lightness = 1 - (block.getLightValue(state) / 15d);
			lightness = Math.pow(lightness, 1.7); //Oh god it's awful
			lightness = MathHelper.clampedLerp(1, 5, lightness);
			r /= lightness;
			g /= lightness;
			b /= lightness;
			return (r << 16) | (g << 8) | b;
		};
	}
	
	//Inversion
	//I can't use states for inversion because I'm using all 15.
	//Thus, this is implemented using two blocks.
	public void setInverseBlockstate(IBlockState b) {
		inverseState = b;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(player.getHeldItem(hand).getItem() == Item.getItemFromBlock(Blocks.REDSTONE_TORCH)) {
			world.setBlockState(pos, inverseState.withProperty(POWER, state.getValue(POWER)));
			return true;
		}
		return false;
	}
	
	@Override
	public boolean hasItemForm() {
		return !inverted;
	}
	
	//Light level based on states
	@Override
	public int getLightValue(IBlockState state) {
		return inverted ? 15 - state.getValue(POWER) : state.getValue(POWER);
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
		
		world.setBlockState(pos, getDefaultState().withProperty(POWER, level));
	}
	
	//Blockstate boilerplate
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(POWER);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(POWER, meta);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, POWER);
	}
	
	//Statemapper
	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasCustomStatemapper() {
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IStateMapper getCustomStatemapper() {
		return new RenamedIgnoringStatemapper("lamp_" + variant);
	}
}
