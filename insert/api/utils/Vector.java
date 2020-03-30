package api.utils;

import org.schema.common.util.linAlg.Vector3i;

import javax.vecmath.Vector3f;

public class Vector {
    private Vector3f internalVector;
    public Vector(Vector3f v){
        this.internalVector = v;
    }
    public Vector(float x, float y, float z){
        this(new Vector3f(x,y,z));
    }
    public Vector add(Vector v){
        return null;
    }
}
