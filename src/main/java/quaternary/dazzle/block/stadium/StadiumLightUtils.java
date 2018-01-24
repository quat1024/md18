package quaternary.dazzle.block.stadium;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StadiumLightUtils {
	public static void breakPole(World world, BlockPos startingPos, boolean drop) {
		while(startingPos.getY() < 255 && world.getBlockState(startingPos).getBlock() instanceof BlockStadiumLightPole) {
			world.destroyBlock(startingPos, drop);
			startingPos = startingPos.up();
		}
	}
}
