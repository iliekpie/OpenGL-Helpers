package iliekpie.OpenGLHelpers;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Map;

public class ShaderProgram {
    private final int programID;

    private FloatBuffer matrix44Buffer = null;

    /**
     * Creates a shader program.
     * @param vertexFile The path of the vertex shader
     * @param fragmentFile The path of the fragment shader
     * @throws LWJGLException
     */
    public ShaderProgram(String vertexFile, String fragmentFile) throws LWJGLException {
        this(vertexFile, fragmentFile, null);
    }

    /**
     * Creates a shader program with attributes.
     * @param vertexFile The path of the vertex shader
     * @param fragmentFile The path of the fragment shader
     * @param attributes A Map<Integer, String> of attributes to be bound to the shader program
     * @throws LWJGLException
     */
    public ShaderProgram(String vertexFile, String fragmentFile, Map<Integer, String> attributes) throws LWJGLException {
        //Load and compile the vertex and fragment shaders.
        final int vertexID = compileShader(loadCode(vertexFile), GL20.GL_VERTEX_SHADER);
        final int fragmentID = compileShader(loadCode(fragmentFile), GL20.GL_FRAGMENT_SHADER);

        //Create a program and get its ID.
        programID = GL20.glCreateProgram();

        //Attach the shaders to the program.
        GL20.glAttachShader(programID, vertexID);
        GL20.glAttachShader(programID, fragmentID);

        //Bind attribute locations.
        if(attributes != null){
            for (Map.Entry<Integer, String> entry : attributes.entrySet()){
                GL20.glBindAttribLocation(programID, entry.getKey(), entry.getValue());
            }
        }

        //Link the program
        GL20.glLinkProgram(programID);

        //Get info log
        String infoLog = GL20.glGetProgramInfoLog(programID, GL20.glGetProgrami(programID, GL20.GL_INFO_LOG_LENGTH));

        //if some log exists, append it
        if (infoLog!=null && infoLog.trim().length()!=0)
            System.err.println(infoLog);

        //If the link failed, throw some sort of exception
        if (GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == GL11.GL_FALSE)
            throw new LWJGLException(
                    "Failure in linking program. Error log:\n" + infoLog);



        //Shader cleanup.
        GL20.glDetachShader(programID, vertexID);
        GL20.glDetachShader(programID, fragmentID);
        GL20.glDeleteShader(vertexID);
        GL20.glDeleteShader(fragmentID);
    }

    //Compiles a shader and returns its ID.
    private int compileShader(String shaderCode, int type) throws LWJGLException {
        //Creates a shader object based on type and gets its ID
        int shaderID = GL20.glCreateShader(type);

        //Loads and compiles the shader
        GL20.glShaderSource(shaderID, shaderCode);
        GL20.glCompileShader(shaderID);

        //if info/warnings are found, append it to our shader log
        String infoLog = GL20.glGetShaderInfoLog(shaderID,
                GL20.glGetShaderi(shaderID, GL20.GL_INFO_LOG_LENGTH));
        if (infoLog!=null && infoLog.trim().length()!=0)
            System.err.println(getTypeName(type) +": "+infoLog + "\n");

        //if the compiling was unsuccessful, throw an exception
        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
            throw new LWJGLException("Failure in compiling " + getTypeName(type)
                    + ". Error log:\n" + infoLog);

        return shaderID;
    }

    //Returns the human-readable name of shaderType.
    private String getTypeName(int shaderType){
        final String name;
        switch (shaderType){
            case GL20.GL_VERTEX_SHADER:
                name = "iliekpie.OpenGLHelpers.Vertex Shader";
                break;
            case GL20.GL_FRAGMENT_SHADER:
                name = "Fragment Shader";
                break;
            default:
                name = "Generic Shader";
                break;
        }

        return name;
    }

    //Basic file loader: Reads shader code from file.
    private String loadCode(String filename) {
        String shaderCode = null;

        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));

            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            shaderCode = sb.toString();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return shaderCode;
    }

    /**
     * Sets this shader as the active program.
     */
    public void use() {
        GL20.glUseProgram(programID);
    }

    /**
     * Disables (set active program to 0) the shader program.
     */
    public void disable() {
        GL20.glUseProgram(0);
    }

    /**
     * Destroys the shader program.
     */
    public void destroy() {
        GL20.glDeleteProgram(programID);
    }

    //Uniform getter and setters

    /**
     * Gets the location of the specified uniform name.
     * @param uniformName The name of the uniform
     * @return The location of the uniform
     */
    public int getUniformLocation(String uniformName) {
        return GL20.glGetUniformLocation(programID, uniformName);
    }

    /**
     * Sets the uniform at the specified location to a 4x4 matrix.
     * @param location The uniform (mat4)'s location
     * @param transpose If the matrix should be transposed
     * @param matrix The matrix to be passed
     */
    public void setUniformMatrix(int location, boolean transpose, Matrix4f matrix){
        if (location == -1) return;
        if (matrix44Buffer == null){
            matrix44Buffer = BufferUtils.createFloatBuffer(16);
        }
        matrix44Buffer.clear();
        matrix.store(matrix44Buffer);
        matrix44Buffer.flip();
        GL20.glUniformMatrix4(location, transpose, matrix44Buffer);
    }

    /**
     * Gets the location of the specified attribute name. (For OpenGL 2.1)
     * @param attributeName The name of the attribute
     * @return (int) The location of the attribute
     */
    public int getAttributeLocation(String attributeName) {
        return GL20.glGetAttribLocation(programID, attributeName);
    }
}
