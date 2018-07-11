package quaternary.dazzle.common.block;

import net.minecraft.block.properties.PropertyInteger;
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

public class BlockAnalogLamp extends BlockLamp {
	public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
	
	private final boolean inverted;
	private IBlockState inverseState;
	
	public BlockAnalogLamp(EnumDyeColor color, EnumLampVariant variant, boolean inverted) {
		super("analog_lamp" + (inverted ? "_inverted" : ""), color, variant);
		
		this.inverted = inverted;
	}
	
	@Override
	public boolean hasItemFormBlahBlahRenameWhenBlockBaseGoesAway() {
		return !inverted;
	}
	
	Item item;
	@Override
	public Item getItemForm() {
		if(item == null) {
			item = new ItemBlockLamp(this, color, variant, "tile.dazzle.analog_lamp.name");
		}
		return item;
	}
	
	@Override
	public boolean hasItemForm() {
		return !inverted;
	}
	
	@Override
	int getBrightnessFromState(IBlockState state) {
		return state.getValue(POWER);
	}
	
	@Override
	IBlockState setStateBrightness(IBlockState state, int powerLevel) {
		return state.withProperty(POWER, powerLevel);
	}
	
	@Override
	IBlockState getInvertedState(IBlockState in) {
		return inverseState.withProperty(POWER, in.getValue(POWER));
	}
	
	//Inversion
	//I can't use states for inversion because I'm using all 15.
	//Thus, this is implemented using two blocks.
	public void setInverseBlockstate(IBlockState b) {
		inverseState = b;
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
