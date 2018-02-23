package quaternary.dazzle.compat.shaderlights;

/** Marker interface for blocks that might provide a "static albedo light".
 * I.e. they don't use a tile entity and place lights in StaticShaderLightManager via
 * some other means. */
public interface IDazzleStaticLight {
}
