package cn.rong.wechat.yuv;

public class YuvInfo {
    public static final String INPUT_VERTEX_COORDINATE_NAME = "in_pos";
    public static final String INPUT_TEXTURE_COORDINATE_NAME = "in_tc";
    public static final String TEXTURE_MATRIX_NAME = "tex_mat";
    public static final String DEFAULT_VERTEX_SHADER_STRING =
        "varying vec2 tc;\n"
            + "attribute vec4 in_pos;\n"
            + "attribute vec4 in_tc;\n"
            + "uniform mat4 tex_mat;\n"
            + "void main() {\n"
            + "  gl_Position = in_pos;\n"
            + "  tc = (tex_mat*in_tc).xy;\n"
            + "}\n";

    public static final String FRAGMENT_SHADER =
        "void main() {\n" + "  gl_FragColor = sample(tc);\n" + "}\n";


    public static final String IMAGE_RENDER_VERTEX_SHADER =
        "uniform mat4 uMVPMatrix;\n"
            + "attribute vec4 aPosition;\n"
            + "attribute vec4 aTextureCoord;\n"
            + "varying vec2 vTextureCoord;\n"
            + "void main(){\n"
            + "    gl_Position = uMVPMatrix * aPosition;\n"
            +
            //                    "    gl_Position =  aPosition;\n" +
            "    vTextureCoord = aTextureCoord.xy;\n"
            + "}";
    public static final String IMAGE_RENDER_FRAGMENT_SHADER =
        "precision mediump float;\n"
            + "varying vec2 vTextureCoord;\n"
            + "uniform sampler2D sTexture;\n"
            + "void main(){\n"
            + "    gl_FragColor = texture2D(sTexture,vTextureCoord);\n"
            + "}";
}
