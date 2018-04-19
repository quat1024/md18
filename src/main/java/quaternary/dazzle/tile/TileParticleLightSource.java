package quaternary.dazzle.tile;

import com.elytradev.mirage.event.GatherLightsEvent;
import com.elytradev.mirage.lighting.ILightEventConsumer;
import elucent.albedo.lighting.ILightProvider;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.Optional;
import quaternary.dazzle.Dazzle;
import quaternary.dazzle.block.BlockParticleLightSource;
import quaternary.dazzle.compat.shaderlights.ColoredLightingMods;
import quaternary.dazzle.compat.shaderlights.WrappedLight;
import quaternary.dazzle.compat.shaderlights.albedo.AlbedoLightGatherer;
import quaternary.dazzle.compat.shaderlights.mirage.MirageLightGatherer;

@Optional.InterfaceList( {
	@Optional.Interface(iface = "com.elytradev.mirage.lighting.ILightEventConsumer", modid="mirage"),
	@Optional.Interface(iface = "elucent.albedo.lighting.ILightProvider", modid="albedo")
})
//)
public class TileParticleLightSource extends TileEntity implements ITickable, ILightEventConsumer, ILightProvider {
	EnumDyeColor color;
	
	WrappedLight wrappedLight;
	
	public TileParticleLightSource() {
		
	}
	
	public TileParticleLightSource(EnumDyeColor color) {
		this.color = color;
	}
	
	@Override
	public void update() {
		if(!world.isRemote || world.getTotalWorldTime() % 5 != 0) return;
		
		color = world.getBlockState(pos).getValue(BlockParticleLightSource.COLOR);
		
		if(wrappedLight == null) {
			wrappedLight = new WrappedLight(pos, color.getColorValue(), color == EnumDyeColor.BLACK ? .2f : .5f, 15);
		}
		
		Dazzle.PROXY.spawnLightSourceParticle(world, pos, color);
	}
	
	@Optional.Method(modid="mirage")
	@Override
	public void gatherLights(GatherLightsEvent e) {
		if(wrappedLight != null && ColoredLightingMods.shouldUseShaderLights()) {
			e.add(MirageLightGatherer.buildMirage(wrappedLight));
		}
	}
	
	@Optional.Method(modid="albedo")
	@Override
	public elucent.albedo.lighting.Light provideLight() {
		if(wrappedLight != null && ColoredLightingMods.shouldUseShaderLights()) {
			return AlbedoLightGatherer.buildAlbedo(wrappedLight);
		} else return null;
	}
}
