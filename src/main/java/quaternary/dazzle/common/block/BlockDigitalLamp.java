package quaternary.dazzle.common.block;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import quaternary.dazzle.common.block.statemapper.RenamedIgnoringStatemapper;
import quaternary.dazzle.common.etc.EnumLampVariant;
import quaternary.dazzle.common.item.ItemBlockLamp;

public class BlockDigitalLamp extends BlockLamp {
	public static final PropertyBool LIT = PropertyBool.create("lit");
	public static final PropertyBool INVERTED = PropertyBool.create("inverted");
	
	public BlockDigitalLamp(EnumDyeColor color, EnumLampVariant variant) {
		super("digital_lamp", color, variant);
		
		setDefaultState(getDefaultState().withProperty(LIT, false).withProperty(INVERTED, false));
	}
	
	@Override
	public boolean hasItemFormBlahBlahRenameWhenBlockBaseGoesAway() {
		return true;
	}
	
	Item item;
	@Override
	public Item getItemForm() {
		if(item == null) {
			item = new ItemBlockLamp(this, color, variant, "tile.dazzle.digital_lamp.name");
		}
		return item;
	}
	
	@Override
	int getBrightnessFromState(IBlockState state) {
		return (state.getValue(LIT) ^ state.getValue(INVERTED)) ? 15 : 0;
	}
	
	@Override
	IBlockState setStateBrightness(IBlockState state, int powerLevel) {
		return state.withProperty(LIT, powerLevel != 0);
	}
	
	@Override
	IBlockState getInvertedState(IBlockState in) {
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
