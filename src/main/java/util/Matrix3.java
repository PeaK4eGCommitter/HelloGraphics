package util;

public class Matrix3 {
    private double[] values;

    public Matrix3(double[] values) {
        this.values = values;
    }

    public static Matrix3 getMatrix3XZ(double angle){
        double[] result = new double[]{
            Math.cos(angle), 0, -Math.sin(angle),
            0, 1, 0,
            Math.sin(angle), 0, Math.cos(angle)
        };
        return new Matrix3(result);
    }

    public static Matrix3 getMatrix3YZ(double angle){
        double[] result = new double[]{
                1, 0, 0,
                0, Math.cos(angle), Math.sin(angle),
                0, -Math.sin(angle), Math.cos(angle)
        };
        return new Matrix3(result);
    }

    public static Matrix3 getMatrix3XY(double angle){
        double[] result = new double[]{
                Math.cos(angle), -Math.sin(angle), 0,
                Math.sin(angle), Math.cos(angle), 0,
                0, 0, 1
        };
        return new Matrix3(result);
    }

    public Matrix3 multiply(Matrix3 other) {
        double[] result = new double[9];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                for (int i = 0; i < 3; i++) {
                    result[row * 3 + col] += this.values[row * 3 + i] * other.values[i * 3 + col];
                }
            }
        }
        return new Matrix3(result);
    }

    public Vertex transform(Vertex in) {
        return new Vertex(
                in.getX() * values[0] + in.getY() * values[3] + in.getZ() * values[6],
                in.getX() * values[1] + in.getY() * values[4] + in.getZ() * values[7],
                in.getX() * values[2] + in.getY() * values[5] + in.getZ() * values[8]
        );
    }
}
