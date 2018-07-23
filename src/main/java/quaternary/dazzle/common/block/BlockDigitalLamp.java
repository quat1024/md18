package quaternary.dazzle.common.block;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import quaternary.dazzle.common.etc.EnumLampVariant;

public class BlockDigitalLamp extends AbstractBlockLamp {
	public static final PropertyBool LIT = PropertyBool.create("lit");
	public static final PropertyBool INVERTED = PropertyBool.create("inverted");
	
	public BlockDigitalLamp(EnumDyeColor color, EnumLampVariant variant) {
		super(color, variant);
		
		setDefaultState(getDefaultState().withProperty(LIT, false).withProperty(INVERTED, false));
	}
	
	@Override
	public boolean hasItemForm() {
		return true;
	}
	
	@Override
	public String getLampTypeTranslationKey() {
		return "tile.dazzle.digital_lamp.name";
	}
	
	@Override
	protected int getBrightnessFromState(IBlockState state) {
		return (state.getValue(LIT) ^ state.getValue(INVERTED)) ? 15 : 0;
	}
	
	@Override
	protected IBlockState setStateBrightness(IBlockState state, int powerLevel) {
		return state.withProperty(LIT, powerLevel != 0);
	}
	
	@Override
	protected IBlockState getInvertedState(IBlockState in) {
		return in.withProperty(INVERTED, !in.getValue(INVERTED));
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
}
