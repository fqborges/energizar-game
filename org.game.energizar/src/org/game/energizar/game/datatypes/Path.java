package org.game.energizar.game.datatypes;

import java.util.Vector;

import net.rim.device.api.ui.XYPoint;

public class Path {
	private Vector _path;

	public Path(XYPoint startPoint, XYPoint endPoint) {
		this._path = new Vector();
		this._path.addElement(startPoint);
		this._path.addElement(endPoint);
	}

	public void add(XYPoint point) {
		this._path.addElement(point);
	}

	public XYPoint getFirst() {
		return (XYPoint) _path.firstElement();
	}

	public Object getLast() {
		return (XYPoint) _path.lastElement();
	}

	public XYPoint get(int i) {
		return (XYPoint) _path.elementAt(i);
	}

	public void addToBegining(XYPoint point) {
		_path.insertElementAt(point, 0);
	}

	public void addToEnd(XYPoint point) {
		_path.addElement(point);
	}

	public int length() {
		return _path.size();
	}

}
