//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package de.unijena.bioinf.GibbsSampling.model;

import de.unijena.bioinf.GibbsSampling.model.EdgeFilter;
import de.unijena.bioinf.GibbsSampling.model.Graph;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class LocalEdgeFilter implements EdgeFilter {
    private final double alpha;

    public LocalEdgeFilter(double alpha) {
        this.alpha = alpha;
    }

    public void filterEdgesAndSetThreshold(Graph graph, int candidateIdx, double[] logEdgeScores) {
        ArrayList<WeightedEdge> weightedEdges = new ArrayList();
        int peakIdx = graph.getPeakIdx(candidateIdx);

        int num;
        for(num = 0; num < logEdgeScores.length; ++num) {
            if(peakIdx != graph.getPeakIdx(num)) {
                double max = logEdgeScores[num];
                weightedEdges.add(new LocalEdgeFilter.WeightedEdge(candidateIdx, num, max));
            }
        }

        Collections.sort(weightedEdges);
        num = 0;
        int var12 = (int)Math.max(Math.round(this.alpha * (double)weightedEdges.size()), 1L);
        double threshold;
        if(var12 >= weightedEdges.size()) {
            threshold = weightedEdges.get(weightedEdges.size()-1).weight; //todo take smallest?
        } else {
            threshold = weightedEdges.get(var12).weight;
        }

        Iterator var10 = weightedEdges.iterator();

        while(var10.hasNext()) {
            LocalEdgeFilter.WeightedEdge weightedEdge = (LocalEdgeFilter.WeightedEdge)var10.next();
            ++num;
            if(num > var12) {
                break;
            }

            graph.setLogWeight(weightedEdge.index1, weightedEdge.index2, weightedEdge.weight - threshold);
        }

        graph.setEdgeThreshold(candidateIdx, threshold);
    }

    public void setThreshold(double threshold) {
    }

    public int[][] postprocessCompleteGraph(Graph graph) {
        throw new NoSuchMethodError("not implemented");
    }

    protected class WeightedEdge implements Comparable<LocalEdgeFilter.WeightedEdge> {
        public final int index1;
        public final int index2;
        public final double weight;

        public WeightedEdge(int index1, int index2, double weight) {
            this.index1 = index1;
            this.index2 = index2;
            this.weight = weight;
        }

        public int compareTo(LocalEdgeFilter.WeightedEdge o) {
            return Double.compare(this.weight, o.weight);
        }

        public boolean equals(Object o) {
            if(this == o) {
                return true;
            } else if(!(o instanceof LocalEdgeFilter.WeightedEdge)) {
                return false;
            } else {
                LocalEdgeFilter.WeightedEdge that = (LocalEdgeFilter.WeightedEdge)o;
                return this.index1 == that.index1 && this.index2 == that.index2?true:this.index1 == that.index2 && this.index2 == that.index1;
            }
        }

        public int hashCode() {
            return 31 * this.index1 + 31 * this.index2;
        }
    }
}
