package quaternary.dazzle.compat.albedo;

import net.minecraftforge.fml.common.Loader;

/**
 * Created by Weissmoon on 2/2/18.
 * Used to not crash VM after client check in {@see quaternary.dazzle.block.BlockParticleLightSource#getLightValue}
 */
public class AlbedoCompat {

    static boolean loaded = false;
    static ConfigChecker checker;

    public static void load(){
        loaded = true;
        checker = new ConfigChecker();
    }

    public boolean getLightsEnabled(){
        if(loaded)
            return checker.getLightEnabled();
        return false;
    }
}
