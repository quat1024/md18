package quaternary.dazzle.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import quaternary.dazzle.Dazzle;

public class ParticleLightSource extends Particle {
	static final ResourceLocation particleRes = new ResourceLocation(Dazzle.MODID, "particle/light_source");
	static TextureAtlasSprite sprite;
	
	public ParticleLightSource(World w, BlockPos p, EnumDyeColor color) {
		super(w, p.getX() + .6, p.getY() + .5, p.getZ() + .6);
		
		canCollide = false;
		setSize(0, 0);
		particleScale = 0;
		particleMaxAge = rand.nextInt(10) + 20;
		
		motionX = map(rand.nextFloat(), 0, 1, -0.01f, 0.01f);
		motionY = map(rand.nextFloat(), 0, 1, 0.02f, 0.04f);
		motionZ = map(rand.nextFloat(), 0, 1, -0.01f, 0.01f);;
		
		if(sprite == null) {
			sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(particleRes.toString());
		}
		
		particleRed = 255 - (0xFF0000 & color.getColorValue()) >> 16;
		particleGreen = 255 - (0x00FF00 & color.getColorValue()) >> 8;
		particleBlue = 255 - (0x0000FF & color.getColorValue());
		
		setParticleTexture(sprite);
	}
	
	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		
		float s;
		if(particleAge < particleMaxAge / 2) {
			s = map(particleAge, 0, particleMaxAge / 2, 0, 2);
		} else {
			s = map(particleAge, particleMaxAge / 2, particleMaxAge, 2, 0);
		}
		particleScale = s;
		
		if(particleAge > particleMaxAge) {
			setExpired(); return;
		}
		
		particleAge++;
		
		move(motionX, motionY, motionZ);
	}
	
	@Override
	public int getBrightnessForRender(float p_189214_1_) {
		//format the number 15 in the weird way that IBlockAccess#getCombinedLight does :shrug:
		return (15 << 20) | (15 << 4);
	}
	
	//Thanks to TheGreyGhost, adapted from Minecraft By Example.
	private static float map(float x, float x1, float x2, float y1, float y2) {
		if(x1 > x2) {
			float temp = x1;
			x1 = x2;
			x2 = temp;
			temp = y1;
			y1 = y2;
			y2 = temp;
		}
		
		if(x <= x1) return y1;
		if(x >= x2) return y2;
		float xFraction = (x - x1) / (x2 - x1);
		return y1 + xFraction * (y2 - y1);
	}
	
	@Override
	public int getFXLayer() {
		return 1;
	}
	
	public static void textureStitch(TextureStitchEvent.Pre e) {
		e.getMap().registerSprite(particleRes);
	}
}
