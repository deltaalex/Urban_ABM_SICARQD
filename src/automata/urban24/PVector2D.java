package automata.urban24;

public class PVector2D {

	public float x;
	public float y;

	public PVector2D() {
	}

	public PVector2D(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public PVector2D(PVector2D v) {
		set(v);
	}

	public PVector2D copy() {
		return new PVector2D(this.x, this.y);
	}

	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void set(PVector2D v) {
		this.x = v.x;
		this.y = v.y;
	}

	public void setZero() {
		x = 0;
		y = 0;
	}

	public float[] getComponents() {
		return new float[] { x, y };
	}

	public float getLength() {
		return (float) Math.sqrt(x * x + y * y);
	}

	public float getLengthSq() {
		return (x * x + y * y);
	}

	public float distanceSq(float vx, float vy) {
		vx -= x;
		vy -= y;
		return (vx * vx + vy * vy);
	}

	public float distanceSq(PVector2D v) {
		float vx = v.x - this.x;
		float vy = v.y - this.y;
		return (vx * vx + vy * vy);
	}

	public float distance(float vx, float vy) {
		vx -= x;
		vy -= y;
		return (float) Math.sqrt(vx * vx + vy * vy);
	}

	public float distance(PVector2D v) {
		float vx = v.x - this.x;
		float vy = v.y - this.y;
		return (float) Math.sqrt(vx * vx + vy * vy);
	}

	public float getAngle() {
		return (float) Math.atan2(y, x);
	}

	public void normalize() {
		double magnitude = getLength();
		x /= magnitude;
		y /= magnitude;
	}

	public PVector2D getNormalized() {
		float magnitude = getLength();
		return new PVector2D(x / magnitude, y / magnitude);
	}

	public static PVector2D toCartesian(float magnitude, float angle) {
		return new PVector2D((float) (magnitude * Math.cos(angle)), (float) (magnitude * Math.sin(angle)));
	}

	public void add(PVector2D v) {
		this.x += v.x;
		this.y += v.y;
	}

	public void add(double vx, double vy) {
		this.x += vx;
		this.y += vy;
	}

	public static PVector2D add(PVector2D v1, PVector2D v2) {
		return new PVector2D(v1.x + v2.x, v1.y + v2.y);
	}

	public PVector2D getAdded(PVector2D v) {
		return new PVector2D(this.x + v.x, this.y + v.y);
	}

	public void subtract(PVector2D v) {
		this.x -= v.x;
		this.y -= v.y;
	}

	public void subtract(double vx, double vy) {
		this.x -= vx;
		this.y -= vy;
	}

	public static PVector2D subtract(PVector2D v1, PVector2D v2) {
		return new PVector2D(v1.x - v2.x, v1.y - v2.y);
	}

	public PVector2D getSubtracted(PVector2D v) {
		return new PVector2D(this.x - v.x, this.y - v.y);
	}

	public void multiply(double scalar) {
		x *= scalar;
		y *= scalar;
	}

	public PVector2D getMultiplied(float scalar) {
		return new PVector2D(x * scalar, y * scalar);
	}

	public void divide(double scalar) {
		x /= scalar;
		y /= scalar;
	}

	public PVector2D getDivided(float scalar) {
		return new PVector2D(x / scalar, y / scalar);
	}

	public PVector2D getPerp() {
		return new PVector2D(-y, x);
	}

	public float dot(PVector2D v) {
		return (this.x * v.x + this.y * v.y);
	}

	public float dot(float vx, float vy) {
		return (this.x * vx + this.y * vy);
	}

	public static float dot(PVector2D v1, PVector2D v2) {
		return v1.x * v2.x + v1.y * v2.y;
	}

	public float cross(PVector2D v) {
		return (this.x * v.y - this.y * v.x);
	}

	public float cross(float vx, float vy) {
		return (this.x * vy - this.y * vx);
	}

	public static float cross(PVector2D v1, PVector2D v2) {
		return (v1.x * v2.y - v1.y * v2.x);
	}

	public float project(PVector2D v) {
		return (this.dot(v) / this.getLength());
	}

	public float project(float vx, float vy) {
		return (this.dot(vx, vy) / this.getLength());
	}

	public static float project(PVector2D v1, PVector2D v2) {
		return (dot(v1, v2) / v1.getLength());
	}

	public PVector2D getProjectedVector(PVector2D v) {
		return this.getNormalized().getMultiplied(this.dot(v) / this.getLength());
	}

	public PVector2D getProjectedVector(float vx, float vy) {
		return this.getNormalized().getMultiplied(this.dot(vx, vy) / this.getLength());
	}

	public static PVector2D getProjectedVector(PVector2D v1, PVector2D v2) {
		return v1.getNormalized().getMultiplied(PVector2D.dot(v1, v2) / v1.getLength());
	}

	public void rotateBy(float angle) {
		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);
		float rx = x * cos - y * sin;
		y = x * sin + y * cos;
		x = rx;
	}

	public PVector2D getRotatedBy(float angle) {
		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);
		return new PVector2D(x * cos - y * sin, x * sin + y * cos);
	}

	public void rotateTo(float angle) {
		set(toCartesian(getLength(), angle));
	}

	public PVector2D getRotatedTo(float angle) {
		return toCartesian(getLength(), angle);
	}

	public void reverse() {
		x = -x;
		y = -y;
	}

	public PVector2D getReversed() {
		return new PVector2D(-x, -y);
	}

	@Override
	public PVector2D clone() {
		return new PVector2D(x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof PVector2D) {
			PVector2D v = (PVector2D) obj;
			return (x == v.x) && (y == v.y);
		}
		return false;
	}

	@Override
	public String toString() {
		return "PVector2D[" + x + ", " + y + "]";
	}
}
