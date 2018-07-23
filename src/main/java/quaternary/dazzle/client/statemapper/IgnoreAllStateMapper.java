package quaternary.dazzle.client.statemapper;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;
import quaternary.dazzle.common.Dazzle;

public class IgnoreAllStateMapper extends StateMapperBase {
	private final String meme;
	
	public IgnoreAllStateMapper(String meme) {
		this.meme = meme;
	}
	
	@Override
	protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
		return new ModelResourceLocation(new ResourceLocation(Dazzle.MODID, meme), "normal");
	}
}
