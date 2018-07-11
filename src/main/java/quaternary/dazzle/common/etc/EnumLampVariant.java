package quaternary.dazzle.common.etc;

public enum EnumLampVariant {
	CLASSIC,
	MODERN,
	PULSATING,
	LANTERN,
	;
	
	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
