package org.example;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BodyRow {
	private final StringProperty name = new SimpleStringProperty("");
	private final DoubleProperty m = new SimpleDoubleProperty();
	private final DoubleProperty x = new SimpleDoubleProperty();
	private final DoubleProperty y = new SimpleDoubleProperty();
	private final DoubleProperty z = new SimpleDoubleProperty();
	private final DoubleProperty vx = new SimpleDoubleProperty();
	private final DoubleProperty vy = new SimpleDoubleProperty();
	private final DoubleProperty vz = new SimpleDoubleProperty();

	/*
	 י property הוא משתנה חכם שמתנהג כמו משתנה רגיל
	אבל יודע להודיע כשמשנים אותו
	וניתן לעשות שהוא יתעדכן אוטומתית במסך (UI)
	בקיצור: property = ערך + התראות על שינוי + יכולת קישור לUI
	*/
	public BodyRow(String name, double m, double x, double y, double z, double vx, double vy, double vz) {

		this.name.set(name);
		this.m.set(m);
		this.x.set(x);
		this.y.set(y);
		this.z.set(z);
		this.vx.set(vx);
		this.vy.set(vy);
		this.vz.set(vz);
	}

	public StringProperty nameProperty() {
		return name;
	}

	public DoubleProperty mProperty() {
		return m;
	}

	public DoubleProperty xProperty() {
		return x;
	}

	public DoubleProperty yProperty() {
		return y;
	}

	public DoubleProperty zProperty() {
		return z;
	}

	public DoubleProperty vxProperty() {
		return vx;
	}

	public DoubleProperty vyProperty() {
		return vy;
	}

	public DoubleProperty vzProperty() {
		return vz;
	}

	/*
	ההסדלים המתודות:
	י get מחזיר את הערך לעומת זאת Property מחזיר את המקום שמחזיק את הערך
	כלומר אם יש את שני הקודים הבאים:
	double x = row.getX();
	DoubleProperty xp = row.xProperty();
	במקרה הראשון x קבוע אולם במקרה השני xp משתנה כשrow.xProperty משתנה
	אני בעתיד בהצלחה עם להבין
	 */

	public String getName() {
		return name.get();
	}

	public double getM() {
		return m.get();
	}

	public double getX() {
		return x.get();
	}

	public double getY() {
		return y.get();
	}

	public double getZ() {
		return z.get();
	}

	public double getVx() {
		return vx.get();
	}

	public double getVy() {
		return vy.get();
	}

	public double getVz() {
		return vz.get();
	}
}
