package quaternary.dazzle.tile;

import com.elytradev.mirage.event.GatherLightsEvent;
import com.elytradev.mirage.lighting.ILightEventConsumer;
import com.elytradev.mirage.lighting.Light;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.Optional;
import quaternary.dazzle.Dazzle;
import quaternary.dazzle.DazzleConfig;
import quaternary.dazzle.block.BlockParticleLightSource;

@Optional.Interface(iface="com.elytradev.mirage.lighting.ILightEventConsumer", modid="mirage")
public class TileParticleLightSource extends TileEntity implements ITickable, ILightEventConsumer {
	EnumDyeColor color;
	public TileParticleLightSource() {
		
	}
	
	public TileParticleLightSource(EnumDyeColor color) {
		this.color = color;
	}
	
	@Override
	public void update() {
		if(!world.isRemote || world.getTotalWorldTime() % 5 != 0) return;
		
		color = world.getBlockState(pos).getValue(BlockParticleLightSource.COLOR);
		
		Dazzle.PROXY.spawnLightSourceParticle(world, pos, color);
	}
	
	@Optional.Method(modid="mirage")
	@Override
	public void gatherLights(GatherLightsEvent e) {
		if(DazzleConfig.MIRAGE_SUPPORT) {
			e.add(Light.builder().pos(pos).color(color.getColorValue(), false).intensity(color == EnumDyeColor.BLACK ? .2f : .5f).radius(15).build());
		}
	}
}
