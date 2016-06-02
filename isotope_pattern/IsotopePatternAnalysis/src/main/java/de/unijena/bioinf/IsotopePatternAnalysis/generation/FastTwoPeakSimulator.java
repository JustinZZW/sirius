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
package de.unijena.bioinf.IsotopePatternAnalysis.generation;

public class FastTwoPeakSimulator {
    /*
    protected IsotopicDistribution distribution;

    public FastTwoPeakSimulator(IsotopicDistribution distribution) {
        this.distribution = distribution;
    }

    public FastTwoPeakSimulator() {
        this(PeriodicTable.getInstance().getDistribution());
    }

    public double computeRatioOfFirstAndSecondIsotopicPeak(MolecularFormula formula, Ionization ion) {
        final MolecularFormula adduct = ion.getAtoms();
        if (adduct!=null) formula=formula.add(adduct);
        return computeRatioOfFirstAndSecondIsotopicPeak(formula);
    }

    public boolean filter(SimpleSpectrum spectrum, MolecularFormula formula, Ionization ion, double percentageThreshold) {
        final MolecularFormula adduct = ion.getAtoms();
        if (adduct!=null) formula = formula.add(adduct);
        final double[] peaks = computeFirstTwoPeaks(formula);
        return (Math.abs(spectrum.getMzAt(0)-peaks[0]) + Math.abs(spectrum.getMzAt(1)-peaks[1]) < percentageThreshold);
    }

    public double computeRatioOfFirstAndSecondIsotopicPeak(MolecularFormula formula) {
        final double[] peaks = computeFirstTwoPeaks(formula);
        return peaks[0]/peaks[1];
    }

    public double[] computeFirstTwoPeaks(MolecularFormula formula) {
        final short[] amounts = formula.copyToBuffer();
        final TableSelection sel = formula.getTableSelection();
        final int n=amounts.length;
        final Isotopes[] distributions = new Isotopes[n];
        double nullProbability=1d;
        double oneProbability=0d;
        for (int k=0; k < n; ++k) {
            if (amounts[k]==0) continue;
            distributions[k] = distribution.getIsotopesFor(sel.get(k));
            if (distributions[k].getNumberOfIsotopes()<=1) continue;
            nullProbability *= Math.pow(distributions[k].getAbundance(0), amounts[k]);
        }
        for (int k=0; k < n; ++k) {
            if (amounts[k]==0) continue;
            if (distributions[k].getNumberOfIsotopes()<=1) continue;
            oneProbability += (nullProbability/distributions[k].getAbundance(0)) * amounts[k] * distributions[k].getAbundance(1);
        }

        return new double[]{nullProbability, oneProbability};

    }
    */

}
