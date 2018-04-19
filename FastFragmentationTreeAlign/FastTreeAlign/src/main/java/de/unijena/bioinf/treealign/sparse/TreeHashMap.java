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
package de.unijena.bioinf.treealign.sparse;

import de.unijena.bioinf.treealign.Tree;

import java.util.Collections;

class TreeHashMap<T> {
    private final HashTable<T>[] tables;
    private final HashTable<T> emptyTable;
    private final int colSize;
    private float score;

    TreeHashMap(int leftSize, int rightSize) {
        this.tables = new HashTable[leftSize * rightSize];
        this.colSize = leftSize;
        this.score = Float.POSITIVE_INFINITY;
        this.emptyTable = new HashTable<T>(Collections.<Tree<T>>emptyList(), Collections.<Tree<T>>emptyList(), true);
    }

    // TODO: REMOVE
    float getScore() {
        return score;
    }

    void setScore(float score) {
        this.score = score;
    }

    HashTable<T> get(int left, int right) {
        if (left < 0 || right < 0) return emptyTable;
        return this.tables[left + right * colSize];
    }

    void set(int left, int right, HashTable<T> table) {
        this.tables[left + right * colSize] = table;
    }
}
