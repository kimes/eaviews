package ph.easyaf.eaviews.views.tree.utils;

public class VectorF {

    private float x = 0, y = 0;

    public VectorF() {}

    public VectorF(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public VectorF add(VectorF operand) {
        return new VectorF(operand.x + x, operand.y + y);
    }

    public VectorF add(float x, float y) {
        return new VectorF(this.x + x, this.y + y);
    }

    public VectorF subtract(VectorF operand) {
        return new VectorF(x - operand.x, y - operand.y);
    }

    public VectorF subtract(float x, float y) {
        return new VectorF(this.x - x, this.y - y);
    }

    public VectorF multiply(VectorF operand) {
        return new VectorF(x * operand.x, y * operand.y);
    }

    public VectorF multiply(float operand) {
        return new VectorF(x * operand, y * operand);
    }

    public VectorF divide(VectorF operand) {
        return new VectorF(x / operand.x, y / operand.y);
    }

    public VectorF divide(float operand) {
        return new VectorF(x / operand, y / operand);
    }

    public float length() {
        return (float)Math.sqrt((double)(x * x + y * y));
    }

    public float getX() { return x; }
    public float getY() { return y; }

    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
}
