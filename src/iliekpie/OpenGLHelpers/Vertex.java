package iliekpie.OpenGLHelpers;

public class Vertex {
    //iliekpie.OpenGLHelpers.Vertex data
    protected float[] xyzw = new float[] {0f, 0f, 0f, 1f};
    protected float[] rgba = new float[] {1f, 1f, 1f, 1f};

    //Element size in bytes
    public static final int elementBytes = 4;

    //Elements per parameter
    public static final int positionElementCount = 4;
    public static final int colorElementCount = 4;

    //Bytes per parameter
    public static final int positionBytesCount = positionElementCount * elementBytes;
    public static final int colorByteCount = colorElementCount * elementBytes;

    //Byte offsets per parameter
    public static final int positionByteOffset = 0;
    public static final int colorByteOffset = positionBytesCount; //positionByteOffset + positionBytesCount

    //The amount of elements that a vertex has
    public static int elementCount = positionElementCount + colorElementCount;
    //The size of a vertex in bytes, like in C/C++: sizeof(iliekpie.OpenGLHelpers.Vertex)
    public static int stride = positionBytesCount + colorByteCount;

    //Setters
    public void setXYZ(float x, float y, float z) {
        this.setXYZW(x, y, z, 1f);
    }

    public void setRGB(float r, float g, float b) {
        this.setRGBA(r, g, b, 1f);
    }

    public void setXYZW(float x, float y, float z, float w) {
        this.xyzw = new float[] {x, y, z, w};
    }

    public void setRGBA(float r, float g, float b, float a) {
        this.rgba = new float[] {r, g, b, 1f};
    }

    //Getters
    public float[] getElements() {
        float[] out = new float[TexturedVertex.elementCount];
        int i = 0;

        //Insert XYZW elements
        out[i++] = this.xyzw[0];
        out[i++] = this.xyzw[1];
        out[i++] = this.xyzw[2];
        out[i++] = this.xyzw[3];

        //Insert RGBA elements
        out[i++] = this.rgba[0];
        out[i++] = this.rgba[1];
        out[i++] = this.rgba[2];
        out[i] = this.rgba[3];

        return out;
    }

    public float[] getXYZW() {
        return new float[] {this.xyzw[0], this.xyzw[1], this.xyzw[2], this.xyzw[3]};
    }

    public float[] getRGBA() {
        return new float[] {this.rgba[0], this.rgba[1], this.rgba[2], this.rgba[3]};
    }
}