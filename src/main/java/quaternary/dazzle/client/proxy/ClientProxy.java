package quaternary.dazzle.client.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import quaternary.dazzle.client.ClientEvents;
import quaternary.dazzle.common.block.*;
import quaternary.dazzle.common.item.*;
import quaternary.dazzle.common.particle.ParticleLightSource;
import quaternary.dazzle.common.proxy.ServerProxy;

public class ClientProxy extends ServerProxy {
	@Override
	public void preinit() {
		MinecraftForge.EVENT_BUS.register(ClientEvents.class);
	}
	
	@Override
	public void spawnLightSourceParticle(World w, BlockPos pos, EnumDyeColor color) {
		Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleLightSource(w, pos, color));
	}
}
