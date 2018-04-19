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
package de.unijena.bioinf.ftalign.view;

import de.unijena.bioinf.ChemistryBase.ms.ft.FTree;

import java.util.BitSet;

/**
 * Created by kaidu on 07.08.14.
 */
public class DataElement {

    private final String name;
    private BitSet fingerprint;
    private FTree tree;

    public DataElement(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public BitSet getFingerprint() {
        return fingerprint;
    }

    public boolean isValid() {
        return fingerprint != null && tree != null && tree.numberOfVertices() >= 5;
    }

    public void setFingerprint(BitSet fingerprint) {
        this.fingerprint = fingerprint;
    }

    public FTree getTree() {
        return tree;
    }

    public void setTree(FTree tree) {
        this.tree = tree;
    }

    public double tanimoto(DataElement elem2) {
        final BitSet copy1 = (BitSet) fingerprint.clone();
        final BitSet copy2 = (BitSet) fingerprint.clone();
        copy1.and(elem2.fingerprint);
        copy2.or(elem2.fingerprint);
        return ((double) copy1.cardinality()) / copy2.cardinality();
    }

    @Override
    public String toString() {
        return "DataElement{" +
                "name='" + name + '\'' +
                ", fingerprint=" + fingerprint +
                ", tree=" + tree +
                '}';
    }
}
