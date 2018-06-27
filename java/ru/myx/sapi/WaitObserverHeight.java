/*
 * Created on 08.07.2005
 */
package ru.myx.sapi;

import java.awt.Image;
import java.awt.image.ImageObserver;

final class WaitObserverHeight implements ImageObserver {
	boolean	finished	= false;
	
	@Override
	public boolean imageUpdate(
			final Image img,
			final int infoflags,
			final int x,
			final int y,
			final int width,
			final int height) {
		if ((infoflags & ImageObserver.HEIGHT) == 0) {
			return true;
		}
		synchronized (this) {
			this.finished = true;
			this.notify();
		}
		return false;
	}
	
	boolean isFinished() {
		synchronized (this) {
			return this.finished;
		}
	}
}
