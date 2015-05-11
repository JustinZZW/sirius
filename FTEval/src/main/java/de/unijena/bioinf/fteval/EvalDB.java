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
package de.unijena.bioinf.fteval;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

public class EvalDB {

    final File root;

    public EvalDB(File root) {
        this.root = root;
        checkPath();
    }

    File profile(String name) {
        return new File(new File(root, "profiles"), name);
    }

    File profile(File name) {
        return new File(new File(root, "profiles"), removeExtName(name));
    }

    File msDir() {
        return new File(root, "ms");
    }

    File[] msFiles() {
        return msDir().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".ms");
            }
        });
    }

    File[] dotFiles(String profile) {
        return dotDir(profile).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".dot");
            }
        });
    }

    File scoreMatrix(String profile) {
        return new File(profile(profile), "matrix.csv");
    }

    String[] profiles() {
        final File[] files = new File(root, "profiles").listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() && new File(pathname, "dot").exists();
            }
        });
        final String[] names = new String[files.length];
        for (int i = 0; i < names.length; ++i) {
            names[i] = files[i].getName();
        }
        return names;
    }

    String removeExtName(File name) {
        return name.getName().substring(0, name.getName().lastIndexOf('.'));
    }

    private void checkPath() {
        if (!root.exists() || !(new File(root, "profiles").exists()))
            throw new RuntimeException("Path '" + root.getAbsolutePath() + "' is no valid dataset path. Create a new" +
                    " evaluation dataset with\nfteval initiate <name>");
    }


    public File sdf(String name) {
        return new File(new File(root, "sdf"), removeExtName(new File(name)) + ".sdf");
    }

    public File fingerprint(String name) {
        return new File(new File(new File(root, "fingerprints"), name), "tanimoto.csv");
    }

    public String[] fingerprints() {
        final File[] dirs = new File(root, "fingerprints").listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() && new File(pathname, "tanimoto.csv").exists();
            }
        });
        final String[] names = new String[dirs.length];
        for (int i = 0; i < names.length; ++i) names[i] = dirs[i].getName();
        return names;
    }

    public File[] sdfFiles() {
        return new File(root, "sdf").listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".sdf") && new File(new File(root, "ms"), removeExtName(new File(name)) + ".ms").exists();
            }
        });
    }

    public File inchiDir() {
        return new File(root, "inchis");
    }

    public File[] inchiFiles() {
        return inchiDir().listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".inchi") && new File(new File(root, "ms"), removeExtName(new File(name)) + ".ms").exists();
            }
        });
    }

    public File garbageDir() {
        return new File(root, "garbage");
    }

    public File otherScoreDir() {
        return new File(root, "scores");
    }

    public File[] otherScores() {
        return otherScoreDir().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".csv");
            }
        });
    }

    public File decoy(String profil) {
        final File d1 = new File(profile(profil), "decoy");
        if (d1.exists()) return d1;
        return new File(root, "decoy");
    }

    public File qvalueMatrix(String p) {
        return new File(profile(p), "qvalues.csv");
    }

    public File dotDir(String prof) {
        return new File(profile(prof), "dot");
    }
}
