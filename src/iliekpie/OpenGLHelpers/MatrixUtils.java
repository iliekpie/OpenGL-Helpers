package iliekpie.OpenGLHelpers;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class MatrixUtils {
    static final float PI = (float)Math.PI;

    public static Matrix4f getPerspectiveMatrix(float fovy, float aspectRatio, float zNear, float zFar){
        //Adapted from the LWJGL github.
        float sine, cotangent, depth;
        float radians = fovy / 2 * PI / 180;

        depth = zFar - zNear;
        sine = (float) Math.sin(radians);

        if ((depth == 0) || (sine == 0) || (aspectRatio == 0)) {
            throw new IllegalArgumentException();
        }

        cotangent = (float) Math.cos(radians) / sine;

        Matrix4f tempMatrix = new Matrix4f();

        tempMatrix.m00 = cotangent / aspectRatio;
        tempMatrix.m11 = cotangent;
        tempMatrix.m22 = -(zFar + zNear) / depth;
        tempMatrix.m23 = -1.0f;
        tempMatrix.m32 = -2.0f * zFar * zNear / depth;
        tempMatrix.m33 = 0.0f;

        return tempMatrix;
    }

    public static Matrix4f getLookAtMatrix(Vector3f eyePosition, Vector3f targetPosition){
        //Makes a view matrix with the default up vector: right side up.
        return getLookAtMatrix(eyePosition, targetPosition, new Vector3f(0.0f, 1.0f, 0.0f));
    }

    public static Matrix4f getLookAtMatrix(Vector3f eyePosition, Vector3f targetPosition, Vector3f up){
        up.normalise();
        Vector3f forward = Vector3f.sub(targetPosition, eyePosition, null);
        forward.normalise();
        Vector3f right = Vector3f.cross(forward, up, null);
        right.normalise();
        Vector3f.cross(right, forward, up);

        Matrix4f tempMatrix = new Matrix4f();

        tempMatrix.m00 = right.getX();
        tempMatrix.m01 = up.getX();
        tempMatrix.m02 = -forward.getX();

        tempMatrix.m10 = right.getY();
        tempMatrix.m11 = up.getY();
        tempMatrix.m12 = -forward.getY();

        tempMatrix.m20 = right.getZ();
        tempMatrix.m21 = up.getZ();
        tempMatrix.m22 = -forward.getZ();

        tempMatrix.translate(eyePosition.negate(null));

        return tempMatrix;
    }

    public static Matrix4f applyTranslations(Vector3f position, Vector3f rotation, Matrix4f targetMatrix) {
        //Rotation vector: x = pitch, y = yaw, z = roll
        targetMatrix.rotate((float)Math.toRadians(rotation.getX()), new Vector3f(1.0f, 0.0f, 0.0f));
        targetMatrix.rotate((float)Math.toRadians(rotation.getY()), new Vector3f(0.0f, 1.0f, 0.0f));
        targetMatrix.rotate((float)Math.toRadians(rotation.getZ()), new Vector3f(0.0f, 0.0f, 1.0f));
        targetMatrix.translate(new Vector3f(-position.getX(), -position.getY(), -position.getZ()));

        return targetMatrix;
    }

    public static Matrix4f applyTranslations(Vector3f position, Vector3f rotation){
        Matrix4f tempMatrix = new Matrix4f();
        return applyTranslations(position, rotation, tempMatrix);
    }

    public static Matrix4f multiplyMVP(Matrix4f model, Matrix4f view, Matrix4f projection) {
        Matrix4f tempMatrix = Matrix4f.mul(view, model, null);
        return Matrix4f.mul(projection, tempMatrix, null);
    }

    public static void dumpMatrixData(String variableName, Matrix4f matrix){
        //Dumps the matrix for debugging purposes.
        System.out.println(variableName + ":\r\n" + matrix.toString());
    }
}