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

import de.unijena.bioinf.ChemistryBase.ms.MutableSpectrum;
import de.unijena.bioinf.ChemistryBase.ms.Peak;
import de.unijena.bioinf.ChemistryBase.ms.Spectrum;

/**
 * Spectrum implementation which is using an array for intensities and masses.
 * Different than other spectrum implementations, it does not copy the array, such that
 * each modification of the spectrum also modifies the given array. Therefore, ArrayWrapperSpectrum
 * should be used carefully in special situations, where someone want to avoid multiple copying of
 * arrays (for example while transforming an immutable spectrum to a mutable one and back to an
 * immutable).
 */
public class ArrayWrapperSpectrum extends AbstractSpectrum<Peak> implements MutableSpectrum<Peak> {

    private final double[] mzs, ints;

    public <P extends Peak, S extends Spectrum<P>> ArrayWrapperSpectrum(S s) {
        this(new double[s.size()], new double[s.size()]);
        for (int i = 0; i < s.size(); ++i) {
            mzs[i] = s.getMzAt(i);
            ints[i] = s.getIntensityAt(i);
        }
    }

    public ArrayWrapperSpectrum(double[] mzs, double[] ints) {
        if (mzs.length != ints.length) throw new IllegalArgumentException("Size of masses and intensities differs");
        this.mzs = mzs;
        this.ints = ints;
    }

    public double[] getMzs() {
        return mzs;
    }

    public double[] getInts() {
        return ints;
    }

    @Override
    public double getMzAt(int index) {
        return mzs[index];
    }

    @Override
    public double getIntensityAt(int index) {
        return ints[index];
    }

    @Override
    public Peak getPeakAt(int index) {
        return new Peak(mzs[index], ints[index]);
    }

    @Override
    public int size() {
        return mzs.length;
    }

    @Override
    public void addPeak(Peak peak) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addPeak(double mz, double intensity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPeakAt(int index, Peak peak) {
        mzs[index] = peak.getMass();
        ints[index] = peak.getIntensity();
    }

    @Override
    public Peak removePeakAt(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMzAt(int index, double mz) {
        mzs[index] = mz;
    }

    @Override
    public void setIntensityAt(int index, double intensity) {
        ints[index] = intensity;
    }

    @Override
    public void swap(int index1, int index2) {
        final double mz = mzs[index1];
        final double in = ints[index1];
        mzs[index1] = mzs[index2];
        ints[index1] = ints[index2];
        mzs[index2] = mz;
        ints[index2] = in;
    }

}
