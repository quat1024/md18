package quaternary.dazzle.proxy;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import quaternary.dazzle.Dazzle;
import quaternary.dazzle.DazzleConfig;
import quaternary.dazzle.block.BlockBase;
import quaternary.dazzle.compat.shaderlights.WrappedLight;
import quaternary.dazzle.compat.shaderlights.albedo.AlbedoLightGatherer;
import quaternary.dazzle.compat.shaderlights.mirage.MirageLightGatherer;
import quaternary.dazzle.item.ItemBlockLamp;
import quaternary.dazzle.item.ItemParticleLight;
import quaternary.dazzle.particle.ParticleLightSource;

public class ClientProxy extends ServerProxy {
	@Override
	public void init() {
		for(Block b : Dazzle.BLOCKS) {
			if(b instanceof BlockBase) {
				BlockBase bbase = (BlockBase) b;
				if(bbase.hasBlockColors()) {
					Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(bbase.getBlockColors(), bbase);
				}
			}
		}
		
		for(Item i : Dazzle.ITEMS) {
			if(i instanceof ItemBlockLamp) {
				ItemBlockLamp lampItem = (ItemBlockLamp) i;
				Minecraft.getMinecraft().getItemColors().registerItemColorHandler(lampItem.getItemColors(), i);
			} else if (i instanceof ItemParticleLight) {
				Minecraft.getMinecraft().getItemColors().registerItemColorHandler((stack, tint) -> {
					return (tint != 1) ? -1 : EnumDyeColor.values()[stack.getMetadata()].getColorValue();
				}, i);
			}
		}
		
		if(shouldUseShaderLights()) {
			boolean albedo = Loader.isModLoaded("albedo");
			boolean mirage = Loader.isModLoaded("mirage");
			
			if(albedo) {
				MinecraftForge.EVENT_BUS.register(AlbedoLightGatherer.class);
			}
			
			if(mirage) {
				MinecraftForge.EVENT_BUS.register(MirageLightGatherer.class);
			}
			
			if(albedo && mirage) {
				Dazzle.LOGGER.info("Wait, when I said Dazzle had support for Mirage and Albedo, I didn't mean both at the same time!!!");
				Dazzle.LOGGER.info("This could cause problems down the road, so be careful out there!");
			}
		}
	}
	
	@Override
	public boolean shouldUseShaderLights() {
		return DazzleConfig.CLIENT.COMPAT.SHADER_LIGHT_SUPPORT && (Loader.isModLoaded("albedo") || Loader.isModLoaded("mirage"));
	}
	
	@Override
	public Object createWrappedLight(BlockPos pos, int color, float intensity, float radius) {
		return new WrappedLight(pos, color, intensity, radius);
	}
	
	@Override
	public void spawnLightSourceParticle(World w, BlockPos pos, EnumDyeColor color) {
		Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleLightSource(w, pos, color));
	}
}
