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
package de.unijena.bioinf.ChemistryBase.ms.utils;

import de.unijena.bioinf.ChemistryBase.ms.Peak;
import de.unijena.bioinf.ChemistryBase.ms.Spectrum;

import java.util.Iterator;
import java.util.List;

/**
 * Simple and efficient way to wrap a list of peaks into a spectrum object, such that it can be
 * used for {{@link Spectrums}} methods
 * @param <P>
 */
public class PeaklistSpectrum<P extends Peak> implements Spectrum<P> {

    protected final List<P> peaks;

    public PeaklistSpectrum(Spectrum<P> spec) {
        this(Spectrums.extractPeakList(spec));
    }

    public PeaklistSpectrum(List<P> peaks) {
        this.peaks = peaks;
    }

    @Override
    public double getMzAt(int index) {
        return peaks.get(index).getMass();
    }

    @Override
    public double getIntensityAt(int index) {
        return peaks.get(index).getIntensity();
    }

    @Override
    public P getPeakAt(int index) {
        return peaks.get(index);
    }

    @Override
    public int size() {
        return peaks.size();
    }

    @Override
    public Iterator<P> iterator() {
        return peaks.iterator();
    }

}
