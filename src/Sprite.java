
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public abstract class Sprite {
	protected Point absolutePosition;
	protected Point relativePosition;
	protected ImageIcon image;

	public Sprite() {
		relativePosition = new Point();
		absolutePosition = new Point();
	}

	public void setLocation(Point p) {
		if (p == null) {
			relativePosition = null;
			absolutePosition = null;
		} else {
			relativePosition.setLocation(p);
			if (image != null) {
				int iconWidth = image.getIconWidth();
				int iconHeight = image.getIconHeight();
				int x = relativePosition.x * Main.rowSize + 10 + (50 - iconWidth)/2;
				int y = relativePosition.y * Main.columnSize + 10 + (50 - iconHeight)/2;
				absolutePosition.setLocation(x, y);
			}
		}
	}
	
	public void setLocation(int x, int y) {
		setLocation(new Point(x,y));
	}

	public Point getLocation() {
		return relativePosition;
	}

	public void draw(Graphics g) {
		if (absolutePosition != null) {
			image.paintIcon(null, g, absolutePosition.x, absolutePosition.y);
		}
	}

//	public boolean isTouching(Sprite other) {
//		return this.relativePosition.equals(other.relativePosition);
//	}

	public boolean isNear(Sprite other) {
		var nearthug = new ArrayList<Point>();
		nearthug.add(new Point(other.relativePosition.x - 1, other.relativePosition.y - 1));
		nearthug.add(new Point(other.relativePosition.x, other.relativePosition.y - 1));
		nearthug.add(new Point(other.relativePosition.x + 1, other.relativePosition.y - 1));
		nearthug.add(new Point(other.relativePosition.x + 1, other.relativePosition.y));
		nearthug.add(new Point(other.relativePosition.x + 1, other.relativePosition.y + 1));
		nearthug.add(new Point(other.relativePosition.x, other.relativePosition.y + 1));
		nearthug.add(new Point(other.relativePosition.x - 1, other.relativePosition.y));
		nearthug.add(new Point(other.relativePosition.x - 1, other.relativePosition.y+1));
		for (var t : nearthug) {
			if (this.relativePosition.equals(t)) {
				return true;
			}
		}
		return false;
	}

	//override the equals method.
	@Override
	public boolean equals(Object o) {
		if (o instanceof Point other) {
			if (this.relativePosition == other) {
				return true;
			} else {
				return false;
			}
		}else{
			return false;
		}
	}

}
