package quaternary.dazzle.proxy;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ServerProxy {
	public void init() {
		; //no-op
	}
	
	public boolean shouldUseShaderLights() {
		return false;
	}
	
	public Object createWrappedLight(BlockPos pos, int color, float intensity, float radius) {
		return null;
	}
	
	public void spawnLightSourceParticle(World w, BlockPos pos, EnumDyeColor color) {
		//Noop
	}
}
