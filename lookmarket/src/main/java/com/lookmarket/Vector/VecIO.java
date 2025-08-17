package com.lookmarket.Vector;

public final class VecIO {
	  public static byte[] toBytes(float[] v){
		    var bb = java.nio.ByteBuffer.allocate(v.length*4).order(java.nio.ByteOrder.LITTLE_ENDIAN);
		    for(float f: v) bb.putFloat(f);
		    return bb.array();
		  }
		  public static float[] toFloats(byte[] b){
		    var bb = java.nio.ByteBuffer.wrap(b).order(java.nio.ByteOrder.LITTLE_ENDIAN);
		    float[] v = new float[b.length/4];
		    for(int i=0;i<v.length;i++) v[i]=bb.getFloat();
		    return v;
		  }
}
