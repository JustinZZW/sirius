package de.unijena.bioinf.FragmentationTreeConstruction.computation.scoring;

import de.unijena.bioinf.ChemistryBase.algorithm.Called;
import de.unijena.bioinf.ChemistryBase.algorithm.ParameterHelper;
import de.unijena.bioinf.ChemistryBase.chem.Element;
import de.unijena.bioinf.ChemistryBase.chem.MolecularFormula;
import de.unijena.bioinf.ChemistryBase.chem.PeriodicTable;
import de.unijena.bioinf.ChemistryBase.data.DataDocument;
import de.unijena.bioinf.ChemistryBase.ms.ft.Loss;
import de.unijena.bioinf.FragmentationTreeConstruction.model.ProcessedInput;

/**
 * If there's a conversion from head to tail of TMS into DMS this LossScorer punishes Losses which consist of more then
 * a single CH3. Because the CH3 group of TMS is not directly connected to other groups but the DMS part.
 */
@Called("TmsToDmsLossScorer")
public class TmsToDmsLossScorer implements LossScorer {
    @Override
    public Object prepare(ProcessedInput inputh) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public double score(Loss loss, ProcessedInput input, Object precomputed) {
        final MolecularFormula lossFormula = loss.getFormula();
        final MolecularFormula headFormula = loss.getSource().getFormula();
        final MolecularFormula tailFormula = loss.getTarget().getFormula();
        final PeriodicTable periodicTable = PeriodicTable.getInstance();
//        int tmsHead = 0;
//        int tmsTail = 0;
        int dmsTail = 0;
        int dmsHead = 0;

//        final Element tmsElement = periodicTable.getByName("Tms");
//        if (tmsElement != null){
//            tmsHead = headFormula.numberOf(tmsElement);
//            tmsTail = tailFormula.numberOf(tmsElement);
//        }
        final Element dmsElement = periodicTable.getByName("Dms");
        if (dmsElement != null) {
            dmsTail = tailFormula.numberOf(dmsElement);
            dmsHead = headFormula.numberOf(dmsElement);
        }


        double score = 0;
        //if (tmsHead>0 && dmsTail>0){
        if (dmsTail - dmsHead > 0) {
            score -= 0.1;//todo is it uncommon, that TMS looses its CH3 group? If yes, does this scoring of -0.1 influence anything if CH3 as common loss scores log(100)~=4,6..
            //if loss != CH3 score -10
            if (lossFormula.elements().size() != 2 || lossFormula.numberOfCarbons() != 1 || lossFormula.numberOfHydrogens() != 3) {
                score -= 10;
            }
        }
        return score;
    }

    @Override
    public <G, D, L> void importParameters(ParameterHelper helper, DataDocument<G, D, L> document, D dictionary) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <G, D, L> void exportParameters(ParameterHelper helper, DataDocument<G, D, L> document, D dictionary) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
