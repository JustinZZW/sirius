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
package de.unijena.bioinf.FTAnalysis;

import de.unijena.bioinf.ChemistryBase.ms.*;
import de.unijena.bioinf.ChemistryBase.ms.utils.SimpleSpectrum;
import de.unijena.bioinf.babelms.GenericParser;
import de.unijena.bioinf.babelms.ms.JenaMsParser;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class InputCache {

    private final HashMap<File, WeakReference<Ms2Experiment>> inputData;
    private final GenericParser<Ms2Experiment> parser;
    private final Lock lock;
    private final AtomicInteger reallocations;

    public InputCache() {
        this.inputData = new HashMap<File, WeakReference<Ms2Experiment>>(1000);
        this.parser = new GenericParser<Ms2Experiment>(new JenaMsParser());
        this.lock = new ReentrantLock();
        this.reallocations = new AtomicInteger(0);
    }

    public int getReallocations() {
        return reallocations.get();
    }

    public List<InputFile> asList(List<File> files) {
        final File[] fileArray = files.toArray(new File[files.size()]);
        return new AbstractList<InputFile>() {
            @Override
            public InputFile get(int index) {
                return new InputFile(fetch(fileArray[index]), fileArray[index]);
            }

            @Override
            public int size() {
                return fileArray.length;
            }
        };
    }
    public List<InputFile> asCopyList(List<File> files) {
        final File[] fileArray = files.toArray(new File[files.size()]);
        return new AbstractList<InputFile>() {
            @Override
            public InputFile get(int index) {
                return new InputFile(fetchCopy(fileArray[index]), fileArray[index]);
            }

            @Override
            public int size() {
                return fileArray.length;
            }
        };
    }

    public Ms2Experiment fetch(File f) {
        final WeakReference<Ms2Experiment> experiment = inputData.get(f);
        if (experiment != null) {
            final Ms2Experiment exp = experiment.get();
            if (exp != null) return exp;
        }
        lock.lock();
        try {
            final WeakReference<Ms2Experiment> anotherTry = inputData.get(f);
            if (anotherTry != null) {
                final Ms2Experiment exp = anotherTry.get();
                if (exp != null) return exp;
            }
            try {
                final Ms2Experiment parsed = parser.parseFile(f);
                reallocations.incrementAndGet();
                inputData.put(f, new WeakReference<Ms2Experiment>(parsed));
                return parsed;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } finally {
            lock.unlock();
        }
    }

    public MutableMs2Experiment fetchCopy(File f) {
        MutableMs2Experiment expCopy = new MutableMs2Experiment(fetch(f));
        final List<? extends Spectrum<Peak>> ms1 = expCopy.getMs1Spectra();
        final List<? extends Ms2Spectrum> ms2 = expCopy.getMs2Spectra();
        final ArrayList<SimpleSpectrum> newMs1 = new ArrayList<SimpleSpectrum>();
        final ArrayList<MutableMs2Spectrum> newMs2 = new ArrayList<MutableMs2Spectrum>();
        for (Spectrum<Peak> spec : ms1) newMs1.add(new SimpleSpectrum(spec));
        for (Ms2Spectrum spec : ms2) newMs2.add(new MutableMs2Spectrum(spec));
        return expCopy;
    }

}
