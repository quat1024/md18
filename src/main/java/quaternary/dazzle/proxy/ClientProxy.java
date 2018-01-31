package quaternary.dazzle.proxy;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import quaternary.dazzle.block.BlockAnalogLamp;
import quaternary.dazzle.block.BlockDigitalLamp;
import quaternary.dazzle.block.statemapper.RenamedIgnoringStatemapper;
import quaternary.dazzle.item.ItemBlockLamp;
import quaternary.dazzle.particle.ParticleLightSource;

import java.util.Collections;

public class ClientProxy extends ServerProxy {
	@Override
	public Object getDigitalLampBlockColors() {
		return (IBlockColor) (state, worldIn, pos, tintIndex) -> {
			Block block = state.getBlock();
			
			int color = ((BlockDigitalLamp)block).color.getColorValue();
			
			int r = (color & 0xFF0000) >> 16;
			int g = (color & 0x00FF00) >> 8;
			int b = (color & 0x0000FF);
			
			if(!BlockDigitalLamp.isLit(state)) {
				r /= 5;
				g /= 5;
				b /= 5;
			}
			
			return (r << 16) | (g << 8) | b;
		};
	}
	
	@Override
	public Object getLampItemColors() {
		return (IItemColor) (ItemStack stack, int tintIndex) -> {
			ItemBlockLamp item = (ItemBlockLamp) stack.getItem();
			return item.color.getColorValue();
		};
	}
	
	@Override
	public Object getAnalogLampBlockColors() {
		return (IBlockColor) (state, worldIn, pos, tintIndex) -> {
			if(tintIndex != 0) return -1;
			
			Block block = state.getBlock();
			
			int color = ((BlockAnalogLamp)block).color.getColorValue();
			
			int r = (color & 0xFF0000) >> 16;
			int g = (color & 0x00FF00) >> 8;
			int b = (color & 0x0000FF);
			
			//This is just a really lazy rgb lerp so it needs a little finaggling to look nice.
			double lightness = 1 - (block.getLightValue(state) / 15d);
			lightness = Math.pow(lightness, 1.7); //Oh god it's awful
			lightness = MathHelper.clampedLerp(1, 5, lightness);
			r /= lightness;
			g /= lightness;
			b /= lightness;
			return (r << 16) | (g << 8) | b;
		};
	}
	
	@Override
	public Object getLampStatemapper(String blah) {
		return new RenamedIgnoringStatemapper(blah);
	}
	
	@Override
	public Object getEmptyStatemapper() {
		return (IStateMapper) block -> Collections.emptyMap();
	}
	
	@Override
	public void spawnLightSourceParticle(World w, BlockPos pos, EnumDyeColor color) {
		Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleLightSource(w, pos, color));
	}
}
