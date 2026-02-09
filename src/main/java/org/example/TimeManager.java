package org.example;

import java.util.ArrayList;

public class TimeManager {
	private final ArrayList<FrameSnapshot> frames = new ArrayList<>(); // אתחול הרשימה שתחזיק את הפריימים

	// פעולה המוסיפה לרשימת הפריימים פריים
	public void record(double t, double[] pos, double[] vel) {

		frames.add(new FrameSnapshot(t, pos, vel));
	}

	// פעולה המחזירה את גודל הרשימה
	public int size() {
		return frames.size();
	}

	// פעולה המחזירה פריים במיקום x ברשימה
	public FrameSnapshot get(int x) {
		return frames.get(x);
	}

	// פעולה המחזירה את הפריים האחרון
	public FrameSnapshot getLatest() {
		if (frames.isEmpty()) {
			return null;
		}

		return frames.get(frames.size() - 1);
	}
}
