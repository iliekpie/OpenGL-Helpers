package iliekpie.OpenGLHelpers;

public class TexturedVertex extends Vertex {
    //Additional vertex data
    private float[] st = new float[]{0.0f, 0.0f};

    public static final int textureElementCount = 2;
    public static final int textureByteCount = textureElementCount * Vertex.elementBytes;
    public static final int textureByteOffset = Vertex.colorByteOffset + Vertex.colorByteCount;
    public static final int elementCount = Vertex.positionElementCount + Vertex.colorElementCount + textureElementCount;
    public static final int stride = Vertex.positionBytesCount + Vertex.colorByteCount + textureByteCount;

    public void setST(float s, float t) {
        this.st = new float[]{s, t};
    }

    //It's faster to type it out again than it is to copy the old result
    public float[] getElements() {
        float[] out = new float[TexturedVertex.elementCount];
        int i = 0;

        //Insert XYZW elements
        out[i++] = xyzw[0];
        out[i++] = xyzw[1];
        out[i++] = xyzw[2];
        out[i++] = xyzw[3];
        //Insert RGBA elements
        out[i++] = rgba[0];
        out[i++] = rgba[1];
        out[i++] = rgba[2];
        out[i++] = rgba[3];
        //Insert ST elements
        out[i++] = st[0];
        out[i] = st[1];

        return out;
    }

    public float[] getST() {
        return new float[]{st[0], st[1]};
    }
}
