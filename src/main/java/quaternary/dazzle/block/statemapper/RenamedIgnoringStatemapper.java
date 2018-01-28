package quaternary.dazzle.block.statemapper;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import quaternary.dazzle.Dazzle;

/** A statemapper that ignores all IProperties, and returns whatever name you pass in for the name
 * of the blockstate file, instead of using the block's registry name. Used in lamps, to avoid writing
 * 48 *identical* blockstate JSONs for the 48 identical lamps. The lamps are all colored using
 * IBlockColors anyways, and it's unlikely someone will want to remodel only one lamp of a set. */
public class RenamedIgnoringStatemapper extends StateMapperBase {
	private final String modelName;
	
	public RenamedIgnoringStatemapper(String modelName) {
		this.modelName = modelName;
	}
	
	@Override
	protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
		return new ModelResourceLocation(Dazzle.MODID + ":" + modelName, "normal");
	}
}
