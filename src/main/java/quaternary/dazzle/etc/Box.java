package quaternary.dazzle.etc;

//Used in fields to store a type that might not exist.
//Having a field of type, e.g. "elucent.albedo.lighting.Light", will cause class
//loading issues if Albedo is not installed. Fortunately generic type erasure
//is on our side here
public class Box<T> {
	T thing;
	
	public Box(T thing) {
		this.thing = thing;
	}
	
	public T unbox() {
		return thing;
	}
}