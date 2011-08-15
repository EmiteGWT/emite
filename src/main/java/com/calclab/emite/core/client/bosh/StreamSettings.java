/*
 * ((e)) emite: A pure Google Web Toolkit XMPP library
 * Copyright (c) 2008-2011 The Emite development team
 * 
 * This file is part of Emite.
 *
 * Emite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * Emite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with Emite.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.calclab.emite.core.client.bosh;

public class StreamSettings {
	public long rid;
	public String sid;
	public String wait;
	private int inactivity;
	long lastRequestTime;
	private int maxPause;

	public StreamSettings() {
		rid = (long) (Math.random() * 10000000) + 1000;
	}

	public int getInactivity() {
		return inactivity;
	}

	public String getInactivityString() {
		return Integer.toString(inactivity);
	}

	public int getMaxPause() {
		return maxPause;
	}

	public String getMaxPauseString() {
		return Integer.toString(maxPause);
	}

	public String getNextRid() {
		rid++;
		return "" + rid;
	}

	public void setInactivity(final String inactivity) {
		try {
			this.inactivity = Integer.parseInt(inactivity);
		} catch (final NumberFormatException e) {
			this.inactivity = 0;
		}
	}

	public void setMaxPause(final String maxPause) {
		try {
			this.maxPause = Integer.parseInt(maxPause);
		} catch (final NumberFormatException e) {
			this.maxPause = 1000;
		}
	}

}
