package quaternary.dazzle.etc;

public class Util {
	//Thanks to TheGreyGhost, adapted from Minecraft By Example.
	public static float map(float x, float x1, float x2, float y1, float y2) {
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
}
