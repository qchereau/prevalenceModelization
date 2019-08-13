package Prevalence.Modelisation;

public class ParticleDescription {
	private float x;
	private float y;
	private double mass;
	private float speedX;
	private float speedY;
	private boolean exist;

	public float getX() {
		return x;
	}

	public ParticleDescription setX(float x) {
		this.x = x;
		return this;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	public float getSpeedX() {
		return speedX;
	}

	public void setSpeedX(float speedX) {
		this.speedX = speedX;
	}

	public float getSpeedY() {
		return speedY;
	}

	public void setSpeedY(float speedY) {
		this.speedY = speedY;
	}

	public boolean exists() {
		return exist;
	}

	public void setExist(boolean exist) {
		this.exist = exist;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (exist ? 1231 : 1237);
		long temp;
		temp = Double.doubleToLongBits(mass);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + Float.floatToIntBits(speedX);
		result = prime * result + Float.floatToIntBits(speedY);
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParticleDescription other = (ParticleDescription) obj;
		if (exist != other.exist)
			return false;
		if (Double.doubleToLongBits(mass) != Double.doubleToLongBits(other.mass))
			return false;
		if (Float.floatToIntBits(speedX) != Float.floatToIntBits(other.speedX))
			return false;
		if (Float.floatToIntBits(speedY) != Float.floatToIntBits(other.speedY))
			return false;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ParticleDescription [x=" + x + ", y=" + y + ", mass=" + mass + ", speedX=" + speedX + ", speedY="
				+ speedY + ", exist=" + exist + "]";
	}
}
