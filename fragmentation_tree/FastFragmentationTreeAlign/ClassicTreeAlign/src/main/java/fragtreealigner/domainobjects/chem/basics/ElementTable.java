/*
 *  This file is part of the SIRIUS library for analyzing MS and MS/MS data
 *
 *  Copyright (C) 2013-2015 Kai Dührkop
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with SIRIUS.  If not, see <http://www.gnu.org/licenses/>.
 */
package fragtreealigner.domainobjects.chem.basics;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("serial")
public class ElementTable implements Serializable {
	private HashMap<String, Element> elements;
	private List<String> elementList;
	
	public ElementTable() {
		elements = new HashMap<String, Element>();
		elementList = new LinkedList<String>();
	}
	
	public void addCHNOPS() {
		addElement(new Element("C", "Carbon", 12.0107, 4));
		addElement(new Element("H", "Hydrogen", 1.00794, 1));
		addElement(new Element("N", "Nitrogen", 14.0067, 3));
		addElement(new Element("O", "Oxygen", 15.9994, 2));
		addElement(new Element("P", "Phosphorus", 30.973762, 5));
		addElement(new Element("S", "Sulfur", 32.065, 2));
	}
	
	public List<String> getElementList() {
		return elementList;
	}
	
	public void addElement(Element element) {
		String elementSymbol = element.getSymbol();
		if (elements.containsKey(elementSymbol)) {
			System.err.println("The element " + elementSymbol + " is already contained in the element table.");
			return;
		}
		elements.put(elementSymbol, element);
		elementList.add(elementSymbol);
	}
	
	public Element getElement(String symbol) {
		return elements.get(symbol);
	}
	
	public int getNumberOfElements() {
		return elementList.size();
	}
	
	public int[] getNumberOfBondingElectrons() {
		int k = 0;
		int[] numberOfBondingElectrons = new int[getNumberOfElements()];
		for (String elementSymbol: getElementList()) {
			numberOfBondingElectrons[k] = getElement(elementSymbol).getNumberOfBondingElectrons();
			k++;
		}
		return numberOfBondingElectrons;
	}
}
