package com.jedk1.projectkorra.mobs.object;

import com.jedk1.projectkorra.mobs.MobMethods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum SubElement {

	Lava (Element.Earth),
	Metal (Element.Earth),
	Lightning (Element.Fire),
	Combustion (Element.Fire),
	Ice (Element.Water);
	
	private Element element;
	
	SubElement(Element element) {
		this.setElement(element);
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}
	
	public static SubElement getRandomSubElement(Element element) {
		List<SubElement> subs = new ArrayList<SubElement>();
		for (SubElement sub : SubElement.values()) {
			if (sub.getElement().equals(element)) {
				subs.add(sub);
			}
		}
		if (subs.isEmpty()) return null;
		return subs.get(MobMethods.rand.nextInt(subs.size()));
	}
	
	public static SubElement getType(String string) {
		for (SubElement sub : SubElement.values()) {
			if (sub.toString().equalsIgnoreCase(string)) {
				return sub;
			}
		}
		return null;
	}

	public static SubElement getType(int index) {
		if (index == -1)
			return null;
		return Arrays.asList(values()).get(index);
	}
}
