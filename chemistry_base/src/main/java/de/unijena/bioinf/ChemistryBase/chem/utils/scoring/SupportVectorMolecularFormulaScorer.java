package de.unijena.bioinf.ChemistryBase.chem.utils.scoring;

import de.unijena.bioinf.ChemistryBase.algorithm.HasParameters;
import de.unijena.bioinf.ChemistryBase.chem.MolecularFormula;
import de.unijena.bioinf.ChemistryBase.chem.utils.MolecularFormulaScorer;

import java.util.HashSet;
@HasParameters
public class SupportVectorMolecularFormulaScorer implements MolecularFormulaScorer {

    public SupportVectorMolecularFormulaScorer() {
    }

    public static boolean inWhiteset(MolecularFormula f) {
        if (WHITESET.isEmpty()) {
            for (String s : whiteset) WHITESET.add(MolecularFormula.parse(s));
        }
        return WHITESET.contains(f);
    }

    public static double getDecisionValue(MolecularFormula formula) {
        final double[] vector = new FormulaFeatureVector(formula).getLogFeatures();
        FormulaFeatureVector.normalizeAndCenter(new double[][]{vector}, CENTERING_VECTOR, NORMALIZATION_VECTOR);
        double decisionValue = 0d;
        for (int k=0; k < SUPPORT_VECTORS.length; ++k) {
            decisionValue += SUPPORT_VECTORS[k] * vector[k];
        }
        return decisionValue;
    }

    public static double getLogScore(MolecularFormula f) {
        if (inWhiteset(f)) return 0d;
        else return 5*Math.max(-3,Math.min(0, getDecisionValue(f)));
    }

    private final static HashSet<MolecularFormula> WHITESET = new HashSet<>();

    private final static String[] whiteset = new String[]{
            "C52H40N4","C32H29P3","C42H42N14","C3H8N3O5P","C41H25N9O2","C5H13N4O6P","C5H2N4O2","H3O5P","C6H15O15P3","C12H20N5O13P3","C6H8N6O18","ClFO2S","C9H15N6O4P","C12H17N6O12P3","C6H6N12O12","C6H16O16P4","C2H5O7P","C6H17NO11P2","C10H8N12O2","C6H11N3O8P2","H6O12P4","C5Cl5N","H6O18P6","C10H17N4O21P5","ClO4","C16H6N6","C2H5O8PS","C12H7N4O12P","C10H13N8O7P","O4S","Br2OS","H2O14","C5H14O22P6","C9H13N6O7P","C48H34N8O4","C11H18N5O14P3","C4H10N3O5P","C40H39N13O","C9H16N5O8P","C10H17N5O9P2","C8H15N6O6","C13H18N7O12P3","S8","C9H14FN2O13P3","C3H6N2O6P2","C11H18N5O12P3","C2H8O8P2","C6H15N7O4","H4O6P2S","C52H37N5","BrI","C5H13N2O8P","O6P4","C9H15N4O16P3","C17H24N10O9P2","C6H20N2O12P4","C10H16BN6O11P3","O10P4","O3P","C5H13O11P3","C9H17N4O12P3","Cl3N","C12H6N8O12","C6H2F2N2O4","C10H15N8O11P3","C9H16N3O11P3","Cl8N4P4","C11H12N12O2","C3H7N2O6P","C8H14N5O12P3","C3H10O8P2","C6H14O12P2","C16H5N5O6","C6H14N2O8P2","H2S5","C6H2N4O6","C13H10N10O4","CH5O8P3","C26H12N4","C14H33N10O7PS","C32H18N4O4","C10H2N10O2","C28H12N2O2","C10H16N5O13P3","C74H46","H5NO6P2","C10H18N5O12P3","H2O6P2","H2O8S2","C3H6N2O7","C9H17N4O13P3","H2O5S","C21H13N11O2","C10H4N4O10","C32H14","C14H4N8O13","C10H11N5O10P2","C3H8NO7P","CH3O7P","C10H16N6O8P2","C2O5P","C7H13N2O12P3","C10H13N8O6P","C5H2N2O6","C36H36N24O12","C11H17FN5O12P3","C10H12N6O12P3","H2O20","C16H20N9O12P3","C3H9N2O5P","CH7O10P3","BrHO3","C10H22N5O13P3","C10H32N12P4","C41H39P3","C3H12NO9P3","C63H40N4","C10H6N10","C10H10N8O6","C10H14N5O10P3","H4O8P2","H6O13P4","C11H18N5O13P3","C6H15N4O5P","C8H16N10O6","C10H18N5O13P3","C10H14N5O12P3","C8H13NO15P4","C11H18N6O7P2","BrHO4","C2H5FNO5P","C15H26N8O10P2","C7H16N8O4","C9H6N6O6","C9H15FN3O12P3","C11H16N8O8","C44H37N8","C3H11N3O7P2","C54H18","CH6O6P2","C25H15N5","C6H16O24P6","C60","C8H16N3O12P3","C12H6N8O7S","C10H8N10O","C20H13N9","C7H12N7O4P","C43H41N15","C10H17N6O9P3","C6H13N2O12P3","C9H13N2O14P3","C42H18","C55H36N4O8","C24H48N9P3","C12H7N5O9","C9H15N5O8P2","C11H16N5O14P3","C9H14N5O11P3","C80H22","P4S10","C4H11NO8P2","C5H14O12P2","C31H20N4","C10H14N6O9P2","P4","C8H12N4O8P2","C11H26N10","HI","C30H24N10","C15H6N4O13","C2HN4O3P","C10H19N6O12P3","C10H15N6O12P3","C6H19O24P7","C6H14FO14P3","C10H6N6O6","C22H10N4O8","C6H15N4O9","H2O6S4","C2H4N2O6","C4H18N3O15P5","C5H14N3O7PS","C6H13F2O14P3","C8H21N7O2","C42H28","C3H6O9P2","C6H13O14P3","BBr3","C9H13N6O6P","C11H16N5O13P3","C82H72N4","C8H12FN6O4P","C5HN3O4S2","C8H12N5O6P","CH7N3O6P2","C9H12N5O8P","C25H20N12O","C10H17N6O13P3","C44H32N4","C10H17N6O14P3","C11H20N5O12P3","C16H13N8O19P3","C6H2ClN3O6","C22H10N4O4","C4O2S4","C16H6N4O8","C60H38N12","C9H18N5O8P","C18H34N12O11","C36H12","C44H38P2","C8H20O25P6","H2O13","C38H16","H2O9","C66H74N12","C10H15N5O9P2","C10H15N6O11P3","C9H15N2O16P3","C7H17NO10P2","C6H19O27P7","C35H23N7","C10H18N6O15P4","C9H16N5O13P3","C48H36N4O2","C6H10O16P4","C5H10O10P2","C11H22N8O5","C16H14N11O13P","C63H59N3","C8H14N4O11P2","O3S","C12H19N6O12P3","C21H14N8","C8H21N7O3","I2O5","C6H2FN3O3","C38H34N12O2","C7H17N4O6P","C6H2N20","C44H30N4O4","C70","BrO","C5H13NO7P2","H2I","C10H20N6O12","C10H15N5O8P2","C6H3N5O5","ClFO3","C4H10O9P2","Cl2O4","C10H22N8O4","CH7O9P3","C3H6N6O6","C10H4N2S5","C2H6NO5P","C7H12N11O11","C6H3FN2O7S","H4O4P","C11H20N5O13P3","O5P2","C48H24","C10H7N7O8S","C6H21N9O6","C66H20","C5H13O12P3","C10H11N8O7P","C9H11N6O6P","C6H17O21P5","C9H14N7O7P","C44H24N4O8","C14H6N4O12","C7H14N5O5P","C7H13N2O13P3","C3H6NO6P","S4","C9H18N3O19P5","C4H12N3O4P","C16H32N9P3","C5H13O14P3","C41H42N14","C3H10N3O6PS","C13H17N7O9P2","C8H20N10","H6NO9P3","C10H19N5O21P6","C8H12N10O5","C24H12N4","C10H16N5O12P3","C42H39N15","C8H13N4O9P","C5H8NO9P3","C9H15N6O11P3","C4HN5O","C10H18N5O19P5","C6H14N2O11P2","C10H17FN3O13P3","C11H19N5O10P2","C6N12O6","C6H15NO11P2","C3H12O12P4","S5","C12H18N5O15P3","C50H36P2","C9H18N3O15P5","C10H15BN5O11P3","C5H12O11P2","C9H15FN5O10P3","C10H14N11O13P3","C7H17N4O4P","C10H15N8O5P","C29H20N6","C7H10N5O6P","C68H53N8","C3H8O17","C4H8N2O7P2","IO","C9H16N3O13P3","C9H16N5O9P","C17H7N11O16","Cl2O2S","C6H15O14P3S","C48H34","H2O6S2","C7H9N5O8P2","H2O4S2","C3H15NO18P6","C11H21N4O12P3","C9H9N5O12P3","C4H4N8O14","C7H15N4O11P3","C10H14FN5O10P2","C36H30N10O","C3HN5O6","C8H8N12O8","C7H3F3N2O7S","CCl2N2O4","C12H20N5O14P3","C19H19N13O7","C22H34N9P3","C40H20","C32H24N8O","C3H9O12P3","C7H14N3O11P3","C9H16N2O18P4","C9H12F2N3O12P3","C10H15N8O13P3","C10H11N8O11P3","C9H28N3O15P5","C7H16N3O8P","C9H14N5O12P3","C6H17N4O3P","C9H14N6O10P2","C28H24N12O2","C6H13NO12P2","C60H46P4","C8H24N4O3P2","C8H18N6O4","HIO3","C5H14NO12P3","C10H19N4O13P3","C24H10N4O4","H4O5P2S2","C8H14N5O11P3","C2H6O9P2","I","C8H14N5O10P3","C6H19NO24P6","C10H18N5O18P5","C14H4N6","C8H15N6O10P3","C2H6O7P2","C40H26","C21H32N16","C8H15N4O14P3","B4O7","C16H30N18","C9H15N6O14P3","C30H20N6","C48H32","C10H15N6O14P3","C16H15N17O2","C14H4N6O16","C16H20N9O13P3","C2H9NO6P2","C8H13N5O9P2","C12N6","C2H6O8S2","C8H17N4O7P","C12H4N7O14","BrF2PS","C10H24N10","HO4P","C10H14FN5O9P2","C38H26N2","C10H15N8O12P3","I2","C4H10NO8P","C4H13N3O7P2","C10H16N4O17P4","C2H6O8P2","C16H32N12P4","C6Cl9N3","C5H2N8O2","I3","C12H4N6O12S","C14H28N9OP3","C8H4N6O6","C18H39N15","C10H13N5O10P2","H4O5P2","C24H51N15","C6H4N6O6","C2H9NO12P4","C36H18","N3P3","C12H32N4O12P4","C8H13FN3O12P3S","C9H14FN6O13P3","C12H18N5O12P3","C3H9O13P3","C3H7O9PS","C17H17N12O16P3","C32H18N2","IO2","C44H40N14","C6H2F3N5O2","C9H10FN3O10P3","C5H10N2O7P2","C9H13N6O5P","C18H38N14","C7H5N5O8","C3HN3O3","C10H13N4O14P3","C44H38N8","C11H17N6O13P3","C4H7N3O9","C5H10N3O5P","C10H18N6O16P4","C8H15N2O12P3","C5H8N4O12","C6H15N7O3","C10H14N6O11P2","C10H20N2S8","C6H15NO8P2","C4H6N4O11","C40H30N8","C30H30N20O10","C9H14N5O14P3","N2O5","CH5NO6P2","HIO4","H2O7P2","C31H24N8","H4NO6P2","C2H4NO5P","C16H34N14","C32H64N12P4","C6H15O12P3S3","C9H16N3O13P3S","C6H14O11P2","CI2O2","C7H17O15P3","C9H14N5O7P","C40H90N10","C77H86N14","H4O6P2","H2O6","C36H75N25O6","C27H18N6","C3H7O13P3","B3O7","C5H15N4O6PS","C28H18N8","C11H4N6O4S","C10H17N4O13P3","C10H17FN3O12P3","C8H17N4O9P","C22H16N10","CH8N3O9P3","C9H16N5O12P3","C16H22N10O7P2","C6H10N4O12S2","C8H13BN3O11P3S","C10H17N5O10P2","C9H14F2N3O13P3","C3H10N3O5PS","Cl2O7","C46H28O","C11H20N5O14P3","C5H13O13P3S","C5H12NO9P","C3H4N4O6","C13H33N4O9P3","C10H16N5O10P3","C14H22N9P3","HO5S","C34H16O2","C6H11N3O7P2","C36H30NP2","C6H14O14P2","C6H2N2O8","C2H7NO6P2","C10H13N5O11P2","C4H11N3O7P2","BrO2","H2O5S2","C36H20N2O4","C7H11N6O5P","C9H15BN3O12P3","C9H27N3O18P6","C6H13FO10P2","C9H13N5O11P2","C9H14N3O12P3","B4H4O9","C11H15N6O13P3","C44H32P2","C44H34N8","C3H6O10P2","C10H13N6O12P3","C12H17N8O13P3","C5H12O9P2","C44H30N4O5","C9H14FN2O15P3","C52H36N6","C9H11N9O2","C5H12O12P2","C6H3N3O9","C15H34N9O2P3","C9H15N6O12P3","C2H12N2O12P4","C9H17N3O15P4","C9H15N5O10P2","C8H14N3O12P3S","C6H15O12P3","C6H15N4O6P","C40H22N4O8","C5H11FO10P2","C6H3N3O9S","O5","ClO3","C31H18N4O","CN8O8","C11H17BN5O11P3","C10H15N4O12P3","C4H8N6O4","C9H13FN3O12P3","C49H32N4O8","C9H15N2O13P3","C3H10O7P2","C11H24N9P3","C32H18N8","C10H15N8O14P3","C9H15N3O12P2","H4O7P2","H3O4P","C6H12O21P6","Br2","C72H46N4O8","C12H20N5O12P3","H5O9P3S","C4H9N2O6P","C8H13N6O5P","C5H9N3O10","C8H9N11","C5H11N4O5P","C6H15NO10P2","C12H6N4O10","C6H18N2O6P2","C10H17N5O8P2","C6H3N3O10S","C11H19N5O15P4","C9H16N3O12P3S","C9H16N3O12P3","C10H13N6O9P","ClI","C6H5N4O6P","C48H26N8","C8H14N7O4P","C5H13N4O5PS","C11H16N5O10P3","H5P3","C10H18N3O14P3","C6H16NO14P3","B2H4O8","C10H15FN5O13P3","C10H19N5O11P2","C48H108N12","C7H4N6O4","C5H11O15P3","H2S6","C3H6N6O5","C8H16N6O6","C40H24","Cl3P","C10H19N2O20P5","C9H4N4O8","H2O4P","C3H14N4O3P2","C10H10N7O7P","BH4O5","C6H16O18P4","C6Cl6","C12H19FN5O12P3","C3H9NO9P2","C4H11N2O7P","C42H42P2","F4P","C9H14FN2O14P3","C44H38N16","C38H36N14","C10H15BN5O10P3","C16H20N10O17P4","C10H16N6O9P2","C4H8N8O8","C9H15FN3O13P3","C6H12O13P2","C6H3N7O3","H7N2O8P3","C10H19N4O12P3","C30H22N8O","C4H11NO7P2","C4H8N3O4P","C10H15N5O11P2","C24H14N6","C6H16N4O9P2","C6H13O17P3","C6H11N4O5P","C7H13N4O6P","C7H12N5O11P3","C6H3N5O4","C10H14N8O11P2","C12H26N9P3","C5H13O13P3","C6H3N3O8","C4H4N6O14","C7H22N2O13P4","C11H18N5O11P3","C6H12F2O10P2","C5H12O13P2","C11H16BFN5O11P3","C5H13N2O6PS","C12H18N5O13P3","C8H2N4O6","H2O8","C8H14N5O7P","C2H8O6P2","CN4O8","C11H20N7O12P3","B3H5O7","C4H11N2O5P","H3O9P","C9H7N11O2","C44H36N8","BrO4","C14H26N14","C11H15F2N8O11P3","C6Cl6O","C40H26N8","C45H36N10O3","C31H22N8O2","C48H30N4O8","C6H12N3O8P","H2O5P","C28H38N18","C6H12N4O9","Cl2O5S2","C10H12N5O13P3","C27H22N10","C39H38N14","C13H11N11","C6H14O18P4","C8H19N6O7P","C9H16N5O10P3","C11H20N6O7P2","C9H16N7O4P","C10H15FN5O11P3","C7H2N2O6S2","C6H15O14P3","C2H6NO6P","C8H13N6O4P","C11H19N5O11P2","C30H12O9","C3H8NO6P","C42H16","C9H16N3O15P3","Br3OP","CH3O5P","C28H22N12","CH4O7P2","C3H11O12P3","C8H13N3O12P2","IO3","C47H30N4O6","C10H9N7O7P","C10H15FN5O12P3","C10H13N5O9P2","C43H46N14","C8H2N4O7","C6H9O12P3","CH6O8P2","C10H15N4O13P3","C6H2N4O2S","C10H18N5O14P3","C6H13NO14S3","HIO2","C5H3N5O4","C9H15N6O13P3","H2O7S2","C10H6N10O2","C13H6N4O11","H2S7","C7H2N4O6","CH4NO6P","C3H8O10P2","C7H3N3O9","C7H11N5O8P2","C14H22N8O11P2","C10H13N6O9P3","C10H16N6O10P2","C10H15BN5O12P3","H3O6S4","C2H9O10P3","C60H38N4O4","C10H12N7O8P","C7H2N4O6S","C8H14N7O5P","C10H14N8O10P2","C6H3N3O7","C38H18O8","C4H4N14","C9H16N5O11P3","C48H40N4","C3HBrN2O2S","C3H9NO7P2","C3HBr2N3O3","C9H13N6O8P","C11H16N5O11P3","C10H9N11","C6H12N6O8","C9H18N5O14P3","C9H17N4O14P3","C9H16N3O14P3","C9H15N6O5P","C5H13NO6P2","C7H3N3O8","C3HN3O2S","C9H15N2O15P3","C12H21N5O15P4","H4O6P2S2","C10H14N5O13P3","C14H36N4O12P4","C36H28P2","C10H18N5O20P5","C16H15N11O16P2","Br","C15H34N9O8PS","C19H12N8","C40H16","C8H5N7O2","C2H8O9P2","C10H17N5O17P4","C5H14NO13P3","C9H10N5O10P3","C34H18","C2H9NO7P2","C42H44N14","C5Cl7N3","C10H14N5O14P3","C11H25N13","Br3P","C5H10N2O6P2S","C5H13NO6P2S","C40H40N14","C5H13O15P3","C6H18O24P6","C9H11N8O7P","C2H9O8P3","C16H19N9O10P2","C3H6NO7P","C10H15N4O14P3","C3H11NO7P2","C24H10N2O4","C10H16N5O15P3","C8H12N5O12P3","C15H6FN5O","C9H17N3O17P4","C3H8O7P2","C10H15N5O12P2","C5HCl4NO","C6H15O13P3","C20H12N8O","C21H29N15O4","C3H9O10P3","C4H2N4O4","C5H15N4O3P","P4S3","C7H18O26P6","C12H5N7O12","C40H28N8","C9H14N5O15P3","C4Cl3NO4","C10H15FN5O14P3","C4H6N4O12","C13H4N4O9","N2O4","C2H8O7P2","C44H30N4","C10H9N11O","C4H8N2O5P","C10H17N5O16P4","C3H5N3O9","C8H14N7O13P3","C6H6N4O9P2","H2O7","C46H56P2","C32H24N12","C5H6N8O13","H3O9P3","Cl3OP","C10H19N6O13P3","C10H15N4O15P3","C10H15N5O10P2","C10H25N9","C10H14N6O10P2","C7H2O8","C9H5N5O6","C3Cl3N3O3","C10H18N6O10P2","C36H26N6O2","C40H30N10O6","C10H14N7O14P3","Cl2O2","C8H15N4O13P3","C9H14N5O13P3","C6H14O24P6","C5H8N2O9P2","C8H22N2O14P4","C2H8N3OP3","C6H2N6O2","C74H74N10","HNO5S","C44H48N14","C6H2ClN5O4S","C10H17N6O12P3","C6H4N4O7","C9H15N2O14P3","C9H11N3O11P3","C5H2Cl4N6O4","C12H24N9P3","C10H13FN5O11P3","C6H14O13P2","C5H12O10P2","C11H18N7O13P3","C5Cl6","Cl3PS","C96H24","BrO3","C7H11N6O4P","C12H4N8O8","C30H18N6","C42H27B","H5O10P3","C8H14N3O15P3","C10H18N5O15P3","C36H20N8O8","C39H36N12O2","C6H17NO12P2","C27H20N12O5","C10H11N8O6P","C10H16N5O14P3","C9H13N8O5P","C9H14N9O4P","C10H19N5O10P2","C4H13NO7P2","C9H18N5O13P3","C7H11N4O9P","C60H44N2O4","C2H4O8P2","C7H3N3O5S2","C5H12N2O9P2","C6H2FN3O6","C3HCl2N3O3","C30H22N8","C24H12N6","C8H15N2O13P3","C8H13FN7O4P","C12H27N15","C16H16N11O19P3","C10H13N6O6P2","C17H36N14","CH7N2O11P3","C40H22N10","C10H11FN5O9P3","C50H34FN7O3","C10H16N4O18P4","C10H18N3O13P3","C3H10O9P2","C8H12N8O6","C10H17N6O11P3","C32H25N9","C44H24N2","C6H2FN5O4","C38H22","H3O8","C9H18N3O14P3","CH6O7P2","C8H19N4O6P","C8H24N9P3","Cl6N3P3","C6H2N4O4","C4H11NO6P2","C9H3N13O2S","C4H10N2O6P2","CH3O6P","C7H12N5O7P","C10H4S8","C3H2N6O14","C11H19N5O16P4","C5H13NO10P2","C7H17N4O7P","C10H18N6O9P2","CH7N2O10P3","C3H11NO6P2","C31H15N3O4","C10H16N5O11P3","C10H25N15","C13H23N6O13P3","C8H14N3O13P3","H2O6S3","C12H33N3O18P6","C27H14N4O","C8H3F4N3O2","C28H12N4O4","C9H18N5O15P3","C4HCl2N3O2","C8H10N8O6","C4H13NO6P2","C10H17N5O11P2","C36H24N4","C36H21N3O4","C34H16O4","C18H15N11","IO4","C8H11N5O9P2","C6H12N6O7","C4H8N4O5",

    };

    private final static double[] CENTERING_VECTOR = new double[]{
            14.081403720438159, 0.03770126856258589, 0.017422975895816817, 549.0730010450461, 6.207361890231946, 0.2188192675108061, 2.657936780929546, 2.4518678835837364, 3.7662013516731863, 0.36083736646381936, 2.86764674631906, 0.8760180785721892, 0.6144314443211486, 3.563168501725521, 0.4139427230948098, 0.20303283329615826, 1.7429364627554633, 3.7373283171204887, 0.3707732535273054, 0.37374226578775166, 0.30083371029501593, 0.1560248017793067, 0.15273211423121272, 0.15995204284624687, 0.33888318727497696, -2.480922654469885, -2.48789536403013, -2.2271053363480036, -2.4836866604247874, 0.3478477998200143, -6.286612381442548, -2.0274100953417107, -5.986739895106151, -3.286240003000486, 25.500866222768547, 19.116498247092508, 13.285774697200956, 5.056024833235066, 0.3872447750018567, 0.18852281356430586, 0.0821388351236281, 0.02631862144018819, 0.006032487521996602, 0.15874839525222012, 2.8923968718683577, 2.633583666933497, 2.183118902883388, 1.479433864504316, 0.1966048014322282, 0.10894112265425118, 0.04536730066642169, 0.015749121012547135, 0.0036860304837130105, 0.07785119231878253, 0.7521905719468124, 2.2185140848783282, 0.03367299116683849, 0.09488065724019697, 0.010201973436464203, 0.0034565661367059832, 7.715320215614474E-4, 0.01979821734244017, 5.616047778877511, 1.1753837379424115
    };

    private final static double[] NORMALIZATION_VECTOR = new double[]{
            105.41859627956184, 0.03770126856258589, 0.9825770241041831, 950.7986448029537, 2.295331006354835, 0.7102882443236487, 2.657936780929546, 2.4518638026774844, 127.48379864832681, 0.36083730142383913, 128.38235325368095, 4.197784456518434, 0.6144313846108818, 127.6868295424971, 0.4139426806582531, 125.79696716670384, 1.742935202928931, 1036.262656185672, 0.5706111431619422, 0.37374226578775166, 75.69916628970499, 0.23138223471355165, 0.15273211418794921, 43.73706359382393, 12.748074660933694, 14.067341021507435, 15.547807175123445, 14.40665652050497, 14.491569000599458, 13.932384318977457, 401.65867926878667, 41.879970253473225, 132.81617091163818, 20.577471715635237, 185.49913377723146, 101.88350175290749, 91.71422530279904, 50.943975166764936, 19.612755224998143, 16.811477186435695, 17.91786116487637, 9.973681378559812, 6.993967512478004, 45.84125160474778, 2.8923968718683577, 2.633583666933497, 2.480320191228679, 2.5636174033302344, 2.8479176362911947, 2.781430635241913, 2.8990716785000186, 2.3821461517858236, 2.075755511196123, 3.772296409391276, 69.24780942805319, 129.03148591512166, 21.21632700883316, 24.905119342759804, 11.239798026563536, 6.246543433863294, 4.999228467978439, 7.48020178265756, 125.63395222112248, 130.0746162620576
    };

    private final static double[] SUPPORT_VECTORS = new double[]{
            -4.306468428966796 ,
            0.1011265149232857 ,
            0.2448067167982835 ,
            5.225619601104487 ,
            -8.449087345554052 ,
            0.9186354923256140 ,
            0.1314520385582383 ,
            -0.1768353468562531 ,
            -1.824679099027970 ,
            -0.1109238093197155 ,
            -1.310966350528658 ,
            -0.3836955324505591 ,
            0.4106650831892570 ,
            -0.3427143308206374 ,
            0.2183607341150822 ,
            -1.501283838295508 ,
            0.04855428999319739 ,
            -0.5769879218654809 ,
            0.2683933894006911 ,
            0.3664712817336896 ,
            -7.088263720116375 ,
            0.1094252068361437 ,
            0.1680986273320471 ,
            -2.614971276767211 ,
            2.158329194273924 ,
            -1.686403044169044 ,
            1.416569910227320 ,
            -5.407816368386094 ,
            0.6280499795617230 ,
            0.009198836796559834,
            0.7338982212409175 ,
            -0.9496281327415093 ,
            0.6293206939056671 ,
            -2.166773260024790 ,
            -2.792491857865272 ,
            -2.657280539037505 ,
            -2.213305897937872 ,
            0.2526875078781987 ,
            0.6288107432065434 ,
            -1.418936114533641 ,
            0.1899886054711750 ,
            2.051580876350681 ,
            1.677478799253787 ,
            11.29125986360079 ,
            1.154243180156942 ,
            4.712558092313726 ,
            -0.4108347807117678 ,
            0.5184458033107960 ,
            -0.2980633193329733 ,
            1.679718732260526 ,
            2.290841799964191 ,
            2.967521212141592 ,
            3.417806633094355 ,
            -0.9151283971069962,
            3.725697313624094 ,
            5.474242121968781 ,
            -0.9374289595457790,
            2.760243628107611 ,
            -1.172549214390660 ,
            -0.3941367206569520 ,
            1.037811640556803 ,
            -0.3452661838393020,
            1.279489326666242 ,
            0.03416222569380024
    };

    @Override
    public double score(MolecularFormula formula) {
        return getLogScore(formula);
    }
}