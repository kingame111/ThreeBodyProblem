package org.example;

import javafx.animation.AnimationTimer;

import java.util.function.Consumer;

public class FramePlayer {
	private final TimeManager timeManager; // רשימת הפריימים
	private int currentFrame = 0; // הפריים שמוצג כרגע

	private double fps = 60.0; // כרגע מאותחל לfps קבוע
	private AnimationTimer timer; // י AnimationTimer היא לולאה שפועלת כל הצגה של פריים

	//י long הוא משתנה מסוג שלם שהמקסימום שלו הוא פי 2 מ-int
	private long lastNanos = 0; // שומר את הזמן בו הוצג הפריים הקודם
	private double accumulator = 0; // משתנה שיכיל את הזמן מהפריים הקודם עד לרגע הנוכחי

	// בנאי
	public FramePlayer(TimeManager timeManager) {
		this.timeManager = timeManager;
	}

	// קובע את הfps של הסימולציה
	public void setFps(double fps) {
		this.fps = Math.max(0.1, fps);
	}

	// מחזיר את הניגון לפריים הראשון
	public void reset() {
		currentFrame = 0;
	}

	public void play(Consumer<FrameSnapshot> onFrame) {
		// י Consumer הוא משתנה המקבל פעולה ממני ומפעיל את הפעולה על האובייקט שהוא מחובר אליו (FrameSnapshot)
		stop(); // עוצר סימולציות קודמות במידה ויש

		timer = new AnimationTimer() { // י AnimationTimer היא לולאה שפועלת כל הצגה של פריים
			@Override
			public void handle(long now) { // lastNanosפעולה המעכדנת את
				if (lastNanos == 0) {
					lastNanos = now;
					return;
				}

				double dt = (now - lastNanos) / 1_000_000_000.0;
				// בודק כמה זמן עבר מהצגת הפריים הקודם עד לרגע הנוכחי ואז ממיר את התשובה לשניות
				lastNanos = now; // משנה את lastNanos לערך העכשוי בשביל הפעם הבאה שהפעולה תופעל

				accumulator = accumulator + dt;
				double step = 1.0 / fps; // יחידת הזמן בה צריך לנגן פריים בודד

				while (accumulator >= step) { // לולאה ולא תנאי בגלל שאם יש תקיעות כמה פריימים יצטרכו להתנגן
					// אם אין עוד פריימים לנגן יעצור
					if (currentFrame >= timeManager.size()) {
						stop();
						return;
					}

					onFrame.accept(timeManager.get(currentFrame));
					// מפעיל את הפעולה שנתתי לFramePlayer (נמצא בMainController)
					currentFrame++; // פעם הבאה שהפעולה הזאת תופעל זה ינגן את הcurrentFrame המעודכן
					accumulator = accumulator - step;
					// מחסרים מהaccumulator יחידת זמן של פריים אחד אחת למקראה ויותר מפריים אחד צריך להתנגן (תקיעות)
				}
			}
		};

		timer.start(); // מתחיל את הטיימר
	}

	public void stop() { // פעולה העוצרת את הטיימר ומכינה את התכנות של המחלקה לקראת הריצה הבאה
		if (timer != null){
			timer.stop();
		}

		timer = null;
		lastNanos = 0;
		accumulator = 0;
	}
}
