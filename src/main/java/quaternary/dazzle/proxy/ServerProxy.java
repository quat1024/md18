package quaternary.dazzle.proxy;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ServerProxy {
	public void init() {
		; //no-op
	}
	
	public Object getDigitalLampBlockColors() {
		return null;
	}
	
	public Object getLampItemColors() {
		return null;
	}
	
	public Object getAnalogLampBlockColors() {
		return null;
	}
	
	public Object getLampStatemapper(String blah) {
		return null;
	}
	
	public Object getEmptyStatemapper() {
		return null;
	}
	
	public void spawnLightSourceParticle(World w, BlockPos pos, EnumDyeColor color) {
		//Noop
	}
}
