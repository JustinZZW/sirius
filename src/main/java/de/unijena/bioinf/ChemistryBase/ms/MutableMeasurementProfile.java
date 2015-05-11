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
package de.unijena.bioinf.ChemistryBase.ms;

import de.unijena.bioinf.ChemistryBase.algorithm.ParameterHelper;
import de.unijena.bioinf.ChemistryBase.algorithm.Parameterized;
import de.unijena.bioinf.ChemistryBase.chem.FormulaConstraints;
import de.unijena.bioinf.ChemistryBase.data.DataDocument;

import java.util.Set;

public class MutableMeasurementProfile implements MeasurementProfile, Parameterized{

    private Deviation allowedMassDeviation, standardMs1MassDeviation, standardMs2MassDeviation, standardMassDifferenceDeviation;
    private FormulaConstraints formulaConstraints;
    private double intensityDeviation;
    private double medianNoiseIntensity;

    /**
     * Merges two profiles a1 and a2 such that the resulting profile contains all fields from a2 and, for all fields
     * that are null in a2 it contains the value of a1.
     * @param a1
     * @param a2
     * @return
     */
    public static MeasurementProfile merge(MeasurementProfile a1, MeasurementProfile a2) {
        final MutableMeasurementProfile profile = new MutableMeasurementProfile(a2);
        if (profile.getAllowedMassDeviation() == null) profile.setAllowedMassDeviation(a1.getAllowedMassDeviation());
        if (profile.getIntensityDeviation()==0) profile.setIntensityDeviation(a1.getIntensityDeviation());
        if (profile.getStandardMassDifferenceDeviation()==null) profile.setStandardMassDifferenceDeviation(a1.getStandardMassDifferenceDeviation());
        if (profile.getStandardMs1MassDeviation()==null) profile.setStandardMs1MassDeviation(a1.getStandardMs1MassDeviation());
        if (profile.getStandardMs2MassDeviation()==null) profile.setStandardMs2MassDeviation(a1.getStandardMs2MassDeviation());
        if (profile.getFormulaConstraints()==null) profile.setFormulaConstraints(a1.getFormulaConstraints());
        if (profile.getMedianNoiseIntensity()==0) profile.setMedianNoiseIntensity(a1.getMedianNoiseIntensity());
        return profile;
    }

    public MutableMeasurementProfile() {
    }

    public MutableMeasurementProfile(MeasurementProfile profile) {
        this(profile.getAllowedMassDeviation(), profile.getStandardMs1MassDeviation(), profile.getStandardMs2MassDeviation(), profile.getStandardMassDifferenceDeviation(),
                profile.getFormulaConstraints(), profile.getIntensityDeviation(), profile.getMedianNoiseIntensity());
    }

    public MutableMeasurementProfile(Deviation allowedMassDeviation, Deviation standardMs1MassDeviation, Deviation standardMs2MassDeviation,
                                     Deviation standardMassDifferenceDeviation, FormulaConstraints formulaConstraints, double intensityDeviation, double medianNoiseIntensity) {
        this.allowedMassDeviation = allowedMassDeviation;
        this.standardMs1MassDeviation = standardMs1MassDeviation;
        this.standardMs2MassDeviation = standardMs2MassDeviation;
        this.standardMassDifferenceDeviation = standardMassDifferenceDeviation;
        this.formulaConstraints = formulaConstraints;
        this.intensityDeviation = intensityDeviation;
        this.medianNoiseIntensity = medianNoiseIntensity;
    }

    public Deviation getAllowedMassDeviation() {
        return allowedMassDeviation;
    }

    public void setAllowedMassDeviation(Deviation allowedMassDeviation) {
        this.allowedMassDeviation = allowedMassDeviation;
    }

    public Deviation getStandardMs1MassDeviation() {
        return standardMs1MassDeviation;
    }

    public void setStandardMs1MassDeviation(Deviation standardMs1MassDeviation) {
        this.standardMs1MassDeviation = standardMs1MassDeviation;
    }

    public Deviation getStandardMs2MassDeviation() {
        return standardMs2MassDeviation;
    }

    public void setStandardMs2MassDeviation(Deviation standardMs2MassDeviation) {
        this.standardMs2MassDeviation = standardMs2MassDeviation;
    }

    public Deviation getStandardMassDifferenceDeviation() {
        return standardMassDifferenceDeviation;
    }

    public void setStandardMassDifferenceDeviation(Deviation standardMassDifferenceDeviation) {
        this.standardMassDifferenceDeviation = standardMassDifferenceDeviation;
    }

    public FormulaConstraints getFormulaConstraints() {
        return formulaConstraints;
    }

    public void setFormulaConstraints(FormulaConstraints formulaConstraints) {
        this.formulaConstraints = formulaConstraints;
    }

    public double getIntensityDeviation() {
        return intensityDeviation;
    }

    public void setIntensityDeviation(double intensityDeviation) {
        this.intensityDeviation = intensityDeviation;
    }

    public double getMedianNoiseIntensity() {
        return medianNoiseIntensity;
    }

    public void setMedianNoiseIntensity(double medianNoiseIntensity) {
        this.medianNoiseIntensity = medianNoiseIntensity;
    }

    @Override
    public <G, D, L> void importParameters(ParameterHelper helper, DataDocument<G, D, L> document, D dictionary) {
        final Set<String> keys = document.keySetOfDictionary(dictionary);
        if (keys.contains("intensityDeviation")) setIntensityDeviation(document.getDoubleFromDictionary(dictionary, "intensityDeviation"));
        if (keys.contains("medianNoiseIntensity")) setMedianNoiseIntensity(document.getDoubleFromDictionary(dictionary, "medianNoiseIntensity"));
        if (keys.contains("formulaConstraints")) setFormulaConstraints((FormulaConstraints)helper.unwrap(document, document.getFromDictionary(dictionary, "formulaConstraints")));
        if (keys.contains("allowedMassDeviation")) allowedMassDeviation = Deviation.fromString(document.getStringFromDictionary(dictionary, "allowedMassDeviation"));
        if (keys.contains("standardMs1MassDeviation")) standardMs1MassDeviation = Deviation.fromString(document.getStringFromDictionary(dictionary, "standardMs1MassDeviation"));
        if (keys.contains("standardMs2MassDeviation")) standardMs2MassDeviation = Deviation.fromString(document.getStringFromDictionary(dictionary, "standardMs2MassDeviation"));
        if (keys.contains("standardMassDifferenceDeviation")) standardMassDifferenceDeviation = Deviation.fromString(document.getStringFromDictionary(dictionary, "standardMassDifferenceDeviation"));
    }

    @Override
    public <G, D, L> void exportParameters(ParameterHelper helper, DataDocument<G, D, L> document, D dictionary) {
        document.addToDictionary(dictionary, "$name", helper.toClassName(MutableMeasurementProfile.class));
        if (intensityDeviation!=0 && !Double.isNaN(intensityDeviation)) document.addToDictionary(dictionary, "intensityDeviation", getIntensityDeviation());
        if (getMedianNoiseIntensity()!=0 && !Double.isNaN(getMedianNoiseIntensity())) document.addToDictionary(dictionary, "medianNoiseIntensity", getMedianNoiseIntensity());
        if (formulaConstraints!=null) document.addToDictionary(dictionary, "formulaConstraints", helper.wrap(document, getFormulaConstraints()));
        if (getAllowedMassDeviation()!=null)document.addToDictionary(dictionary, "allowedMassDeviation", getAllowedMassDeviation().toString());
        if (getStandardMs1MassDeviation()!=null)document.addToDictionary(dictionary, "standardMs1MassDeviation", getStandardMs1MassDeviation().toString());
        if (getStandardMs2MassDeviation()!=null)document.addToDictionary(dictionary, "standardMs2MassDeviation", getStandardMs2MassDeviation().toString());
        if (getStandardMassDifferenceDeviation()!=null)document.addToDictionary(dictionary, "standardMassDifferenceDeviation", getStandardMassDifferenceDeviation().toString());
    }
}
