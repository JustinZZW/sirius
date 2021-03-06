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
package de.unijena.bioinf.ChemistryBase.ms.ft;

import de.unijena.bioinf.ChemistryBase.chem.MolecularFormula;

import java.util.Arrays;

public class Loss {

    private final static Object[] EMPTY_ARRAY = new Object[0];
    protected final Fragment source;
    protected final Fragment target;
    protected MolecularFormula formula;
    protected double weight;
    protected int sourceEdgeOffset, targetEdgeOffset;
    protected Object[] annotations;
    protected double probability;

    public Loss(Fragment from, Fragment to, MolecularFormula loss, double weight) {
        this.source = from;
        this.target = to;
        this.formula = loss;
        this.weight = weight;
        this.annotations = EMPTY_ARRAY;
        this.sourceEdgeOffset = 0;
        this.targetEdgeOffset = 0;
        this.probability = 0;
    }

    protected Loss(Loss old, Fragment newFrom, Fragment newTo) {
        this.source = newFrom;
        this.target = newTo;
        this.formula = old.formula;
        this.weight = old.weight;
        this.annotations = old.annotations.clone();
        this.sourceEdgeOffset = old.sourceEdgeOffset;
        this.targetEdgeOffset = old.targetEdgeOffset;
        this.probability = old.probability;
    }

    public Loss(Fragment from, Fragment to) {
        this(from, to, from.formula.subtract(to.formula), 0d);
    }

    public boolean isDeleted() {
        return sourceEdgeOffset >= 0;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Fragment getSource() {
        return source;
    }

    public Fragment getTarget() {
        return target;
    }

    public MolecularFormula getFormula() {
        return formula;
    }

    public void setFormula(MolecularFormula formula) {
        this.formula = formula;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    final Object getAnnotation(int id) {
        if (id >= annotations.length) return null;
        return annotations[id];
    }

    final void setAnnotation(int id, int capa, Object o) {
        if (id >= annotations.length) annotations = Arrays.copyOf(annotations, Math.max(capa, id + 1));
        annotations[id] = o;
    }

    public String toString() {
        return formula.toString();
    }

}
