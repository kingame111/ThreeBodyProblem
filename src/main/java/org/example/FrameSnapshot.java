package org.example;

public final class FrameSnapshot {
	private final double t; // הזמן של הפריים
	private final double[] pos; // המיקום של הגופים בפריים
	private final double[] vel; // המהירות של הגופים בפריים
	// TODO אם אין שימוש במהירויות עדיף להוריד

	// בנאי
	public FrameSnapshot(double t, double[] pos, double[] vel) {
		if (pos == null || vel == null) {
			throw new IllegalArgumentException("pos/vel cannot be null");
		}
		if (pos.length != vel.length) {
			throw new IllegalArgumentException("pos and vel must have same length");
		}
		if (pos.length % 3 != 0) {
			throw new IllegalArgumentException("pos/vel length must be multiple of 3");
		}

		this.t = t;

		this.pos = new double[pos.length];
		this.vel = new double[vel.length];
		System.arraycopy(pos, 0, this.pos, 0, pos.length);
		System.arraycopy(vel, 0, this.vel, 0, vel.length);
	}

	// פעולה המחזריה את הזמן של הפריים
	public double getTime() {
		return t;
	}

	// פעולה המחזירה את מספר הגופים בפריים
	public int getBodyNum() {
		return pos.length / 3;
	}

	// פעולה המעתיקה את ערכי המיקום
	public void copyPos(double[] out) {
		if (out.length != pos.length) throw new IllegalArgumentException("wrong length");
		System.arraycopy(pos, 0, out, 0, pos.length);
	}

	// פעולה המעתיקה את ערכי המהירות
	public void copyVel(double[] out) {
		if (out.length != vel.length) throw new IllegalArgumentException("wrong length");
		System.arraycopy(vel, 0, out, 0, vel.length);
	}
}
