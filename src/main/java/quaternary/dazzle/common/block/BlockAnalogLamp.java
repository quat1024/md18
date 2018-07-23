package quaternary.dazzle.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import quaternary.dazzle.common.etc.EnumLampVariant;

public class BlockAnalogLamp extends AbstractBlockLamp {
	public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
	
	private final boolean inverted;
	private IBlockState inverseState;
	
	public BlockAnalogLamp(EnumDyeColor color, EnumLampVariant variant, boolean inverted) {
		super(color, variant);
		
		this.inverted = inverted;
	}
	
	@Override
	public boolean hasItemForm() {
		return !inverted;
	}
	
	@Override
	public String getLampTypeTranslationKey() {
		return "tile.dazzle.analog_lamp.name";
	}
	
	@Override
	protected int getBrightnessFromState(IBlockState state) {
		if(inverted) return 15 - state.getValue(POWER);
		else return state.getValue(POWER);
	}
	
	@Override
	protected IBlockState setStateBrightness(IBlockState state, int powerLevel) {
		return state.withProperty(POWER, powerLevel);
	}
	
	@Override
	protected IBlockState getInvertedState(IBlockState in) {
		return inverseState.withProperty(POWER, in.getValue(POWER));
	}
	
	//Inversion
	//I can't use states for inversion because I'm using all 15.
	//Thus, this is implemented using two blocks.
	public void setInverseBlockstate(IBlockState b) {
		inverseState = b;
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		//Since inverted analog lamps are a separate block for "running-out-of-data-values" reasons
		//getPickBlock becomes broken on inverted lamps.
		//1.13, please save us all.
		if(inverted) {
			Block invertedBlock = getInvertedState(state).getBlock();
			return invertedBlock.getPickBlock(state, target, world, pos, player);
		} else return super.getPickBlock(state, target, world, pos, player);
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
}
