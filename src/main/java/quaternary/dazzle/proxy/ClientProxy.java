package quaternary.dazzle.proxy;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import quaternary.dazzle.Dazzle;
import quaternary.dazzle.block.BlockBase;
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
	}
	
	@Override
	public void spawnLightSourceParticle(World w, BlockPos pos, EnumDyeColor color) {
		Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleLightSource(w, pos, color));
	}
}
