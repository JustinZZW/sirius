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
package de.unijena.bioinf.treealign.map;

import java.util.NoSuchElementException;

public interface IntPairFloatIterator {

    public boolean hasNext();
    public int getLeft();
    public int getRight();
    public float getValue();
    public void next();

    public static IntPairFloatIterator Empty = new IntPairFloatIterator() {


      public void next() {
          throw new NoSuchElementException();
      }

      public boolean hasNext() {
          return false;
      }

        public int getLeft() {
            throw new NoSuchElementException();
        }

        public int getRight() {
            throw new NoSuchElementException();
        }

        public float getValue() {
            throw new NoSuchElementException();
        }
    };

}
