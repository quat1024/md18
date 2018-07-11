package quaternary.dazzle.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import quaternary.dazzle.block.*;
import quaternary.dazzle.item.*;
import quaternary.dazzle.particle.ParticleLightSource;

public class ClientProxy extends ServerProxy {
	@Override
	public void preinit() {
		MinecraftForge.EVENT_BUS.register(ClientProxy.class);
	}
	
	@SubscribeEvent
	public static void blockColors(ColorHandlerEvent.Block e) {
		for(BlockLamp lamp : DazzleBlocks.getLamps()) {
			e.getBlockColors().registerBlockColorHandler(lamp::getBlockColor, lamp);
		}
	}
	
	@SubscribeEvent
	public static void itemColors(ColorHandlerEvent.Item e) {
		for(ItemBlockLamp lamp : DazzleItems.getLampItems()) {
			e.getItemColors().registerItemColorHandler((stack, tintIndex) -> lamp.color.getColorValue(), lamp);
		}
	}
	
	@Override
	public void spawnLightSourceParticle(World w, BlockPos pos, EnumDyeColor color) {
		Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleLightSource(w, pos, color));
	}
}
