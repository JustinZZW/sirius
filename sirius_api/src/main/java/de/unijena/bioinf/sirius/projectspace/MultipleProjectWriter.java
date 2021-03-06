package de.unijena.bioinf.sirius.projectspace;

import java.io.IOException;

public class MultipleProjectWriter implements ProjectWriter {

    protected ProjectWriter[] writers;

    public MultipleProjectWriter(ProjectWriter[] writers) {
        this.writers = writers;
    }

    @Override
    public void writeExperiment(ExperimentResult result) throws IOException {
        for (ProjectWriter w : writers)
            w.writeExperiment(result);
    }

    @Override
    public void close() throws IOException {
        for (ProjectWriter w : writers)
            w.close();
    }
}
