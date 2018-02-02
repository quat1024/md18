package quaternary.dazzle.compat.albedo;

import elucent.albedo.ConfigManager;

/**
 * Created by Weissmoon on 2/2/18.
 * Used to check for albedo lights once the mod has verified to be installed.
 */
public class ConfigChecker {

    boolean getLightEnabled(){
        return ConfigManager.enableLights;
    }
}
