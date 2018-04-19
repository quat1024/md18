package quaternary.dazzle.compat.shaderlights;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;


/** Manage "static lights", i.e. Albedomirage lights that don't have tile entities connected to them. */
@SideOnly(Side.CLIENT)
public class StaticShaderLightManager {
	public final Map<BlockPos, WrappedLight> lights = new HashMap<>();
	int lastDimensionID = 0;
	
	static boolean instantiated = false;
	public StaticShaderLightManager() {
		if(instantiated) {
			throw new RuntimeException("Only one static shader light manager should exist");
		} else instantiated = true;
	}
	
	public void tick() {
		WorldClient w = Minecraft.getMinecraft().world;
		
		if(w == null) { //on the main menu or something
			lights.clear();
			return;
		}
		
		int playerDim = w.provider.getDimension();
		
		if(lastDimensionID != playerDim) { //left for a different dimension
			lights.clear();
			lastDimensionID = playerDim;
			return;
		}
		
		if(lights.isEmpty() || w.getTotalWorldTime() % 10 != 0) return;
		
		//Clean up old shader lights
		lights.entrySet().removeIf(e -> !w.isBlockLoaded(e.getKey()));
		lights.entrySet().removeIf(e -> !(w.getBlockState(e.getKey()).getBlock() instanceof IDazzleStaticLight));
	}
	
	public void put(BlockPos p, WrappedLight w) {
		lights.put(p, w);
	}
	
	public boolean has(BlockPos p) {
		return lights.containsKey(p);
	}
}
