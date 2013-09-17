package de.unijena.bioinf.ChemistryBase.ms;

import de.unijena.bioinf.ChemistryBase.algorithm.HasParameters;
import de.unijena.bioinf.ChemistryBase.algorithm.Parameter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@HasParameters
public class Deviation implements Cloneable{

    private final double ppm;
    private final double absolute;

    public static Deviation fromMeasurementAndReference(double measurementMz, double referenceMz) {
        final double absMz = measurementMz - referenceMz;
        final double ppm = absMz*1e6/measurementMz;
        return new Deviation(ppm, absMz);
    }

    public Deviation(double ppm) {
        this.ppm = ppm;
        this.absolute = 1e-4*ppm; // set absolute to 100 Da with given ppm
    }

    public Deviation(@Parameter("ppm") double ppm, @Parameter("absolute") double absolute) {
        this.ppm = ppm;
        this.absolute = absolute;
    }

    public Deviation multiply(int scalar) {
        return new Deviation(ppm*scalar, absolute*scalar);
    }

    public Deviation multiply(double scalar) {
        return new Deviation(ppm*scalar, absolute*scalar);
    }

    public Deviation divide(double scalar) {
        return multiply(1d/scalar);
    }

    public Deviation roundUp() {
        return new Deviation(Math.ceil(ppm/0.2)*0.2, Math.ceil(absolute/0.00002)*0.00002);
    }

    public double absoluteFor(double value) {
        return Math.max(ppm * value * 1e-6, absolute);
    }

    public boolean inErrorWindow(double center, double value) {
        final double diff = Math.abs(center - value);
        return diff <= absoluteFor(center);
    }

    public double getPpm() {
        return ppm;
    }

    public double getAbsolute() {
        return absolute;
    }

    public String toString() {
        return ppm + " ppm (" + absolute + " m/z)";
    }

    private static Pattern pattern = Pattern.compile("(?:(.+)\\s*ppm\\s*)?(?:(?:,|\\(|)\\s*(.+?)\\s*(m\\/z|mDa|Da|u)\\s*\\)?)?");
    public static Deviation fromString(String s) {
        final Matcher m = pattern.matcher(s);
        if (!m.find()) throw new IllegalArgumentException("Pattern should have the format <number> ppm (<number> m/z)");
        final String ppm = m.group(1);
        final String abs = m.group(2);
        final String unit = m.group(3);
        double absolute = Double.NaN;
        if (abs != null && !abs.isEmpty()) {
            assert unit!=null && !unit.isEmpty();
            absolute = Double.parseDouble(abs);
            if (unit.equalsIgnoreCase("mDa")) absolute /= 1000;
        }
        if (ppm != null && !ppm.isEmpty()) {
            double ppmValue = Double.parseDouble(ppm);
            return Double.isNaN(absolute) ? new Deviation(ppmValue) : new Deviation(ppmValue, absolute);
        } else if (!Double.isNaN(absolute)) {
            return fromMeasurementAndReference(100+absolute, 100);
        } else throw new IllegalArgumentException("Pattern should have the format <number> ppm (<number> m/z)");
    }

    public Deviation clone() {
        return new Deviation(ppm, absolute);
    }

    public static Deviation valueOf(String s) {
        return fromString(s);
    }
}
