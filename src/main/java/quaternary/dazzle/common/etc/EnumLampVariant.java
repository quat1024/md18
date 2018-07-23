package quaternary.dazzle.common.etc;

import java.util.Locale;

public enum EnumLampVariant {
	CLASSIC,
	MODERN,
	PULSATING,
	LANTERN,
	;
	
	public boolean isEnabled = true;
	
	@Override
	public String toString() {
		return name().toLowerCase(Locale.ROOT);
	}
}
