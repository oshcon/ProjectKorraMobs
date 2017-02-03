package com.jedk1.projectkorra.mobs.object;

import java.util.Arrays;

public enum Element {

	Air,
	Earth,
	Fire,
	Water,
	Avatar;
	
	public static Element getType(String string) {
		for (Element element : Element.values()) {
			if (element.toString().equalsIgnoreCase(string)) {
				return element;
			}
		}
		return null;
	}

	public static Element getType(int index) {
		if (index == -1)
			return null;
		return Arrays.asList(values()).get(index);
	}
	
	public boolean isAirbender() {
		return this.equals(Element.Air);
	}
	
	public boolean isEarthbender() {
		return this.equals(Element.Earth);
	}
	
	public boolean isFirebender() {
		return this.equals(Element.Fire);
	}
	
	public boolean isWaterbender() {
		return this.equals(Element.Water);
	}
	
	public boolean isAvatar() {
		return this.equals(Element.Avatar);
	}
}
