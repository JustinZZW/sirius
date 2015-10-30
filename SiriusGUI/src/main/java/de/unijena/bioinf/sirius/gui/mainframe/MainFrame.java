package de.unijena.bioinf.sirius.gui.mainframe;

import de.unijena.bioinf.sirius.IdentificationResult;
import de.unijena.bioinf.sirius.Sirius;
import de.unijena.bioinf.sirius.gui.compute.BackgroundComputation;
import de.unijena.bioinf.sirius.gui.compute.BatchComputeDialog;
import de.unijena.bioinf.sirius.gui.compute.CompoundModel;
import de.unijena.bioinf.sirius.gui.compute.ComputeDialog;
import de.unijena.bioinf.sirius.gui.configs.ConfigStorage;
import de.unijena.bioinf.sirius.gui.dialogs.*;
import de.unijena.bioinf.sirius.gui.filefilter.SupportedBatchDataFormatFilter;
import de.unijena.bioinf.sirius.gui.filefilter.SupportedExportCSVFormatsFilter;
import de.unijena.bioinf.sirius.gui.io.SiriusDataConverter;
import de.unijena.bioinf.sirius.gui.io.WorkspaceIO;
import de.unijena.bioinf.sirius.gui.load.LoadController;
import de.unijena.bioinf.sirius.gui.mainframe.results.ResultPanel;
import de.unijena.bioinf.sirius.gui.structure.ComputingStatus;
import de.unijena.bioinf.sirius.gui.structure.ExperimentContainer;
import de.unijena.bioinf.sirius.gui.structure.ReturnValue;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class MainFrame extends JFrame implements WindowListener, ActionListener, ListSelectionListener, DropTargetListener,MouseListener, KeyListener{
	
	private CompoundModel compoundModel;
	private JList<ExperimentContainer> compoundList;
	private JButton newB, loadB, closeB, saveB, editB, computeB, batchB, computeAllB, exportResultsB,aboutB;
	
	private HashSet<String> names;
	private int nameCounter;
	
	private JPanel resultsPanel;
	private CardLayout resultsPanelCL;
	private ResultPanel showResultsPanel;
	private static final String DUMMY_CARD = "dummy";
	private static final String RESULTS_CARD = "results";
	private ConfigStorage config;

	private BackgroundComputation backgroundComputation;

	private boolean removeWithoutWarning = false;

    private DropTarget dropTarget;
	
	private JPopupMenu expPopMenu;
	private JMenuItem newExpMI, batchMI, editMI, closeMI, computeMI, cancelMI;
	
	public MainFrame(){
		super(Sirius.VERSION_STRING);
		
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}


		this.config = new ConfigStorage();

		this.backgroundComputation = new BackgroundComputation(this);

		nameCounter=1;
		this.names = new HashSet<>();
		
		this.addWindowListener(this);
		this.setLayout(new BorderLayout());
		
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		this.add(mainPanel,BorderLayout.CENTER);
//		JButton dummy = new JButton("compute");
//		dummy.addActionListener(this);
//		mainPanel.add(dummy,BorderLayout.CENTER);
		
		resultsPanelCL = new CardLayout();
		resultsPanel = new JPanel(resultsPanelCL);
		JPanel dummyPanel = new JPanel();
		resultsPanel.add(dummyPanel,DUMMY_CARD);
		
		showResultsPanel = new ResultPanel(this,config);
//		resultsPanel.add(showResultsPanel,RESULTS_CARD);
//		resultsPanelCL.show(resultsPanel, RESULTS_CARD);
		mainPanel.add(showResultsPanel,BorderLayout.CENTER);
		
		JPanel compoundPanel = new JPanel(new BorderLayout());
		compoundPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"experiments"));
		
		compoundModel = new CompoundModel();
		compoundList = new JList<>(compoundModel);
		compoundList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		compoundList.setCellRenderer(new CompoundCellRenderer());
		compoundList.addListSelectionListener(this);
		compoundList.setMinimumSize(new Dimension(200,0));
		compoundList.addMouseListener(this);
//		compoundList.setPreferredSize(new Dimension(200,0));
		
		JScrollPane pane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pane.setViewportView(compoundList);
		
		pane.getViewport().setPreferredSize(new Dimension(200,(int)pane.getViewport().getPreferredSize().getHeight()));
//		pane.getViewport().setMinimumSize(new Dimension(200,(int)pane.getViewport().getPreferredSize().getHeight()));
		
//		System.err.println(pane.getViewport().getPreferredSize().getWidth()+" "+pane.getViewport().getPreferredSize().getHeight());
//		
//		System.err.println(pane.getVerticalScrollBar().getPreferredSize().getWidth());
//		pane.setPreferredSize(new Dimension(221,0));
		
		compoundPanel.add(pane,BorderLayout.WEST);
		
		mainPanel.add(compoundPanel,BorderLayout.WEST);

		JPanel controlPanel = new JPanel(new WrapLayout(FlowLayout.LEFT,3,0));
//		controlPanel.setBorder(BorderFactory.createEtchedBorder());
		
		JPanel tempP = new JPanel(new FlowLayout(FlowLayout.LEFT,5,2));
		tempP.setBorder(BorderFactory.createEtchedBorder());
		
		newB = new JButton("Import",new ImageIcon(MainFrame.class.getResource("/icons/document-new.png")));
		newB.addActionListener(this);
        //newB.setToolTipText("Import measurements of a single compound");
		tempP.add(newB);
		batchB = new JButton("Batch Import",new ImageIcon(MainFrame.class.getResource("/icons/document-multiple.png")));
		batchB.addActionListener(this);
        //batchB.setToolTipText("Import measurements of several compounds");
		tempP.add(batchB);
		editB = new JButton("Edit",new ImageIcon(MainFrame.class.getResource("/icons/document-edit.png")));
		editB.addActionListener(this);
		editB.setEnabled(false);
        //editB.setToolTipText("Edit an experiment");
		tempP.add(editB);
		closeB = new JButton("Close",new ImageIcon(MainFrame.class.getResource("/icons/document-close.png")));
		closeB.addActionListener(this);
		closeB.setEnabled(false);
        //closeB.setToolTipText("Remove an experiment together with its results from the workspace");
		tempP.add(closeB);
		controlPanel.add(tempP);
		
		tempP = new JPanel(new FlowLayout(FlowLayout.LEFT,5,2));
		tempP.setBorder(BorderFactory.createEtchedBorder());
		
		loadB = new JButton("Open Workspace",new ImageIcon(MainFrame.class.getResource("/icons/document-open.png")));
		loadB.addActionListener(this);
        //loadB.setToolTipText("Load all experiments and computed results from a previously saved workspace.");
		tempP.add(loadB);
		saveB = new JButton("Save Workspace",new ImageIcon(MainFrame.class.getResource("/icons/media-floppy.png")));
        //saveB.setToolTipText("Save the entire workspace (all experiments and computed results).");
		saveB.addActionListener(this);
		saveB.setEnabled(false);
		tempP.add(saveB);
		controlPanel.add(tempP);
		
		tempP = new JPanel(new FlowLayout(FlowLayout.LEFT,5,2));
		tempP.setBorder(BorderFactory.createEtchedBorder());

		computeB = new JButton("Compute",new ImageIcon(MainFrame.class.getResource("/icons/applications-system.png")));
		/*
		computeB.addActionListener(this);
		computeB.setEnabled(false);
		tempP.add(computeB);
		*/
		computeAllB = new JButton("Compute All", new ImageIcon(MainFrame.class.getResource("/icons/applications-system.png")));
		computeAllB.addActionListener(this);
		computeAllB.setEnabled(true);
        //computeAllB.setToolTipText("Compute all compounds asynchronously");
		tempP.add(computeAllB);

        exportResultsB = new JButton("Export Results", new ImageIcon(MainFrame.class.getResource("/icons/document-export.png")));
        exportResultsB.addActionListener(this);
        exportResultsB.setEnabled(false);
        //exportResultsB.setToolTipText("Export identified molecular formulas into a CSV file.");
        tempP.add(exportResultsB);
        controlPanel.add(tempP);

        tempP = new JPanel(new FlowLayout(FlowLayout.LEFT,5,2));
        tempP.setBorder(BorderFactory.createEtchedBorder());

        aboutB = new JButton("About", new ImageIcon(MainFrame.class.getResource("/icons/help.png")));
        aboutB.addActionListener(this);
        aboutB.setEnabled(true);
        tempP.add(aboutB);

        controlPanel.add(tempP);

		mainPanel.add(controlPanel,BorderLayout.NORTH);
		
		this.dropTarget = new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
		
		constructExperimentListPopupMenu();
		{
			KeyStroke delKey = KeyStroke.getKeyStroke("DELETE");
			KeyStroke enterKey = KeyStroke.getKeyStroke("ENTER");
			String delAction = "deleteItems";
			compoundList.getInputMap().put(delKey, delAction);
			compoundList.getInputMap().put(enterKey, "compute");
			compoundList.getActionMap().put(delAction, new AbstractAction()
			{
				@Override public void actionPerformed(ActionEvent e) {
					deleteCurrentCompound();
				}
			});
			compoundList.getActionMap().put("compute", new AbstractAction()
			{
				@Override public void actionPerformed(ActionEvent e) {
					computeCurrentCompound();
				}
			});
		}



		this.setSize(new Dimension(1368, 1024));

		addKeyListener(this);
		
		this.setVisible(true);
	}
	
	public void constructExperimentListPopupMenu(){
		expPopMenu = new JPopupMenu();
		newExpMI = new JMenuItem("Import Experiment",new ImageIcon(MainFrame.class.getResource("/icons/document-new.png")));
		batchMI = new JMenuItem("Batch Import",new ImageIcon(MainFrame.class.getResource("/icons/document-multiple.png")));
		editMI = new JMenuItem("Edit Experiment",new ImageIcon(MainFrame.class.getResource("/icons/document-edit.png")));
		closeMI = new JMenuItem("Close Experiment",new ImageIcon(MainFrame.class.getResource("/icons/document-close.png")));
		computeMI = new JMenuItem("Compute",new ImageIcon(MainFrame.class.getResource("/icons/applications-system.png")));

        cancelMI = new JMenuItem("Cancel Computation", new ImageIcon(MainFrame.class.getResource("/icons/cancel.png")));



		
		newExpMI.addActionListener(this);
		batchMI.addActionListener(this);
		editMI.addActionListener(this);
		closeMI.addActionListener(this);
		computeMI.addActionListener(this);
        cancelMI.addActionListener(this);
		
		editMI.setEnabled(false);
		closeMI.setEnabled(false);
		computeMI.setEnabled(false);
        cancelMI.setEnabled(false);
		
		expPopMenu.add(computeMI);
        expPopMenu.add(cancelMI);
		expPopMenu.addSeparator();
		expPopMenu.add(newExpMI);
		expPopMenu.add(batchMI);
//		expPopMenu.addSeparator();
		expPopMenu.add(editMI);
		expPopMenu.add(closeMI);
		expPopMenu.addSeparator();
	}

	public BackgroundComputation getBackgroundComputation() {
		return backgroundComputation;
	}

	public static void main(String[] args){
		MainFrame mf = new MainFrame();
	}

	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowClosing(WindowEvent e) {
		this.dispose();
	}

	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public Enumeration<ExperimentContainer> getCompounds() {
		return compoundModel.elements();
	}

	public void refreshCompound(ExperimentContainer c) {
		compoundModel.refresh(c);
        refreshComputationMenuItem();
        refreshExportMenuButton();
	}

    private void refreshExportMenuButton() {
        final Enumeration<ExperimentContainer> ecs = getCompounds();
        while (ecs.hasMoreElements()) {
            final ExperimentContainer e = ecs.nextElement();
            if (e.getComputeState()== ComputingStatus.COMPUTED) {
                exportResultsB.setEnabled(true);
                return;
            }
        }
        exportResultsB.setEnabled(false);
    }

    private void refreshComputationMenuItem() {
        final ExperimentContainer ec = this.compoundList.getSelectedValue();
        if (ec != null && (ec.isComputing() || ec.isQueued()) ) {
            cancelMI.setEnabled(true);
        } else {
            cancelMI.setEnabled(false);
        }
    }

    public void actionPerformed(ActionEvent e) {
		if(e.getSource()==newB || e.getSource()==newExpMI) {
            LoadController lc = new LoadController(this, config);
            lc.showDialog();
            if (lc.getReturnValue() == ReturnValue.Success) {
                ExperimentContainer ec = lc.getExperiment();

                importCompound(ec);
            }
        } else if (e.getSource()==exportResultsB) {
            exportResults();
		}else if(e.getSource()==computeB || e.getSource()==computeMI) {
			computeCurrentCompound();
		} else if (e.getSource() == computeAllB) {
            final BatchComputeDialog dia = new BatchComputeDialog(this);
        } else if (e.getSource() == cancelMI) {
            final ExperimentContainer ec = compoundList.getSelectedValue();
            if (ec!=null)
                backgroundComputation.cancel(ec);
		}else if(e.getSource()==saveB){
			
			JFileChooser jfc = new JFileChooser();
			jfc.setCurrentDirectory(config.getDefaultSaveFilePath());
			jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			jfc.setAcceptAllFileFilterUsed(false);
			jfc.addChoosableFileFilter(new SiriusSaveFileFilter());
			
			File selectedFile = null;
			
			while(selectedFile==null){
				int returnval = jfc.showSaveDialog(this);
				if(returnval == JFileChooser.APPROVE_OPTION){
					File selFile = jfc.getSelectedFile();
					config.setDefaultSaveFilePath(selFile.getParentFile());
					
					String name = selFile.getName();
					if(!selFile.getAbsolutePath().endsWith(".sirius")){
						selFile = new File(selFile.getAbsolutePath()+".sirius");
					}
					
					if(selFile.exists()){
						FilePresentDialog fpd = new FilePresentDialog(this, selFile.getName());
						ReturnValue rv = fpd.getReturnValue();
						if(rv==ReturnValue.Success){
							selectedFile = selFile;
						}
//						int rt = JOptionPane.showConfirmDialog(this, "The file \""+selFile.getName()+"\" is already present. Override it?");
					}else{
						selectedFile = selFile;	
					}
					
					
				}else{
					break;
				}
			}
			
			if(selectedFile!=null){
				try{
					WorkspaceIO io = new WorkspaceIO();
					io.store(new AbstractList<ExperimentContainer>() {
                        @Override
                        public ExperimentContainer get(int index) {
                            return compoundList.getModel().getElementAt(index);
                        }

                        @Override
                        public int size() {
                            return compoundList.getModel().getSize();
                        }
                    }, selectedFile);
				}catch(Exception e2){
					new ExceptionDialog(this, e2.getMessage());
				}
				
			}
			
			
		}else if(e.getSource()==loadB) {

            JFileChooser jfc = new JFileChooser();
            jfc.setCurrentDirectory(config.getDefaultSaveFilePath());
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jfc.setAcceptAllFileFilterUsed(false);
            jfc.addChoosableFileFilter(new SiriusSaveFileFilter());

            int returnVal = jfc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                WorkspaceIO io = new WorkspaceIO();
                File selFile = jfc.getSelectedFile();
                config.setDefaultSaveFilePath(selFile.getParentFile());
                ImportWorkspaceDialog workspaceDialog = new ImportWorkspaceDialog(this);
                final WorkspaceWorker worker = new WorkspaceWorker(this, workspaceDialog, selFile);
                worker.execute();
                workspaceDialog.start();
                worker.flushBuffer();
            }
        } else if (e.getSource()==aboutB) {
            new AboutDialog(this);
		}else if(e.getSource()==closeB || e.getSource()==closeMI){
			deleteCurrentCompound();
		}else if(e.getSource()==editB || e.getSource()==editMI){
			ExperimentContainer ec = this.compoundList.getSelectedValue();
            if (ec==null) return;
			String guiname = ec.getGUIName();
			
			LoadController lc = new LoadController(this,ec,config);
			lc.showDialog();
			if(lc.getReturnValue() == ReturnValue.Success){
//				ExperimentContainer ec = lc.getExperiment();
				
				if(!ec.getGUIName().equals(guiname)){
					while(true){
						if(ec.getGUIName()!=null&&!ec.getGUIName().isEmpty()){
							if(this.names.contains(ec.getGUIName())){
								ec.setSuffix(ec.getSuffix()+1);
							}else{
								this.names.add(ec.getGUIName());
								break;
							}
						}else{
							ec.setName("Unknown");
							ec.setSuffix(1);
						}
					}
				}
				this.compoundList.repaint();
			}
			
		}else if(e.getSource()==batchB || e.getSource()==batchMI){
			JFileChooser chooser = new JFileChooser(config.getDefaultLoadDialogPath());
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			chooser.setMultiSelectionEnabled(true);
			chooser.addChoosableFileFilter(new SupportedBatchDataFormatFilter());
			chooser.setAcceptAllFileFilterUsed(false);
			int returnVal = chooser.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION){
				File[] files = chooser.getSelectedFiles();
				config.setDefaultLoadDialogPath(files[0].getParentFile());
				importOneExperimentPerFile(files);
			}
			
			
			//zu unfangreich, extra Methode
			
		}
		
		
		
	}

    private void exportResults() {

		JFileChooser jfc = new JFileChooser();
		jfc.setCurrentDirectory(config.getCsvExportPath());
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.setAcceptAllFileFilterUsed(false);
		jfc.addChoosableFileFilter(new SupportedExportCSVFormatsFilter());

		File selectedFile = null;

		while(selectedFile==null){
			int returnval = jfc.showSaveDialog(this);
			if(returnval == JFileChooser.APPROVE_OPTION){
				File selFile = jfc.getSelectedFile();
				config.setCsvExportPath(selFile.getParentFile());

				String name = selFile.getName();
				if(!selFile.getAbsolutePath().endsWith(".csv") && !selFile.getName().endsWith(".tsv")){
					selFile = new File(selFile.getAbsolutePath()+".csv");
				}

				if(selFile.exists()){
					FilePresentDialog fpd = new FilePresentDialog(this, selFile.getName());
					ReturnValue rv = fpd.getReturnValue();
					if(rv==ReturnValue.Success){
						selectedFile = selFile;
					}
//						int rt = JOptionPane.showConfirmDialog(this, "The file \""+selFile.getName()+"\" is already present. Override it?");
				}else{
					selectedFile = selFile;
				}


			}else{
				break;
			}
		}

		if(selectedFile!=null){
			try {
				try (final BufferedWriter fw = new BufferedWriter(new FileWriter(selectedFile))) {
					final Enumeration<ExperimentContainer> ecs = getCompounds();
					while (ecs.hasMoreElements()) {
						final ExperimentContainer ec = ecs.nextElement();
						if (ec.isComputed() && ec.getResults().size()>0) {
							IdentificationResult.writeIdentifications(fw, SiriusDataConverter.experimentContainerToSiriusExperiment(ec), ec.getRawResults());
						}
					}
				}
			} catch (IOException e) {
				new ExceptionDialog(this, e.toString());
			}
		}
    }

    private void computeCurrentCompound() {
		ExperimentContainer ec = this.compoundList.getSelectedValue();
		if (ec != null) {
			ComputeDialog cd = new ComputeDialog(this, ec);
			if (cd.isSuccessful()) {
//					System.err.println("ComputeDialog erfolgreich");
//					System.err.println("Anzahl Ergebnisse: "+ec.getResults().size());
				this.showResultsPanel.changeData(ec);
				resultsPanelCL.show(resultsPanel, RESULTS_CARD);
			} else {
//					System.err.println("ComputeDialog nicht erfolgreich");
			}
		}
	}

	private void deleteCurrentCompound() {
		int index = this.compoundList.getSelectedIndex();
		if (index < 0) return;
		ExperimentContainer cont = this.compoundModel.get(index);
		if (cont.getResults()!=null && cont.getResults().size()>0 && !config.isCloseNeverAskAgain()) {
			CloseDialogNoSaveReturnValue diag = new CloseDialogNoSaveReturnValue(this, "When removing this experiment you will loose the computed identification results for \""  +cont.getGUIName()+"\"?", config);
			CloseDialogReturnValue val = diag.getReturnValue();
			if (val==CloseDialogReturnValue.abort) return;
		}
		this.compoundModel.remove(index);
		//this.compoundList.setSelectedIndex(-1);
		this.names.remove(cont.getGUIName());
	}

	public void importOneExperimentPerFile(List<File> msFiles, List<File> mgfFiles){
		BatchImportDialog batchDiag = new BatchImportDialog(this);
		batchDiag.start(msFiles,mgfFiles);
		
		List<ExperimentContainer> ecs = batchDiag.getResults();
		List<String> errors = batchDiag.getErrors(); 
		importOneExperimentPerFileStep2(ecs, errors);
	}
	
	public void importOneExperimentPerFile(File[] files){
		BatchImportDialog batchDiag = new BatchImportDialog(this);
		batchDiag.start(resolveFileList(files));
		
		List<ExperimentContainer> ecs = batchDiag.getResults();
		List<String> errors = batchDiag.getErrors(); 
		importOneExperimentPerFileStep2(ecs, errors);
	}

	public File[] resolveFileList(File[] files) {
		final ArrayList<File> filelist = new ArrayList<>();
		for (File f : files) {
			if (f.isDirectory()) {
				final File[] fl = f.listFiles();
				if (fl!=null) {
					for (File g : fl)
						if (!g.isDirectory()) filelist.add(g);
				}
			} else {
				filelist.add(f);
			}
		}
		return filelist.toArray(new File[filelist.size()]);
	}
	
	public void importOneExperimentPerFileStep2(List<ExperimentContainer> ecs, List<String> errors){
		if(ecs!=null){
			for(ExperimentContainer ec : ecs){
				if(ec==null){
					continue;
				}else{
					importCompound(ec);
				}
			}
		}
		
		
		if(errors!=null){
			if(errors.size()>1){
				ErrorListDialog elDiag = new ErrorListDialog(this, errors);
			}else if(errors.size()==1){
				ExceptionDialog eDiag = new ExceptionDialog(this, errors.get(0)); 
			}
			
		}
	}
	
	
	
	
	public void importCompound(ExperimentContainer ec){
		while(true){
			if(ec.getGUIName()!=null&&!ec.getGUIName().isEmpty()){
				if(this.names.contains(ec.getGUIName())){
					ec.setSuffix(ec.getSuffix()+1);
				}else{
					this.names.add(ec.getGUIName());
					break;
				}
			}else{
				ec.setName("Unknown");
				ec.setSuffix(1);
			}
		}
		this.compoundModel.addElement(ec);
		this.compoundList.setSelectedValue(ec, true);
        if (ec.getComputeState()==ComputingStatus.COMPUTED) {
            this.exportResultsB.setEnabled(true);
        }
	}

	public void clearWorkspace() {
		this.names.clear();
		this.compoundModel.removeAllElements();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(e.getSource()==this.compoundList){
			int index = compoundList.getSelectedIndex();
            refreshComputationMenuItem();
			if(index<0){
				closeB.setEnabled(false);
				editB.setEnabled(false);
				saveB.setEnabled(false);
				computeB.setEnabled(false);
				
				closeMI.setEnabled(false);
				editMI.setEnabled(false);
				computeMI.setEnabled(false);
				this.showResultsPanel.changeData(null);
			}else{
				closeB.setEnabled(true);
				editB.setEnabled(true);
				saveB.setEnabled(true);
				computeB.setEnabled(true);
				
				closeMI.setEnabled(true);
				editMI.setEnabled(true);
				computeMI.setEnabled(true);
				this.showResultsPanel.changeData(compoundModel.getElementAt(index));
				resultsPanelCL.show(resultsPanel,RESULTS_CARD);
			}
		}
	}
	
	//////////////////////////////////////////////////
	////////////////// drag and drop /////////////////
	//////////////////////////////////////////////////
	
	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragExit(DropTargetEvent dte) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drop(DropTargetDropEvent dtde) {
		Transferable tr = dtde.getTransferable();
		dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
		DataFlavor[] flavors = tr.getTransferDataFlavors();
	    List<File> newFiles = new ArrayList<File>();
	    try{
			for (int i = 0; i < flavors.length; i++) {
				if (flavors[i].isFlavorJavaFileListType()) {
					List files = (List) tr.getTransferData(flavors[i]);
					for (Object o : files) {
						File file = (File) o;
						newFiles.add(file);
					}
				}
				dtde.dropComplete(true);
			}
	    }catch(Exception e){
	    	e.printStackTrace();
	    	try {
                dtde.rejectDrop();
            } catch (Exception e2) {
                e.printStackTrace();
            }
	    }
	    
		if(newFiles.size()>0){
			importDragAndDropFiles(Arrays.asList(resolveFileList(newFiles.toArray(new File[newFiles.size()]))));
		}
	}
	
	private void importDragAndDropFiles(List<File> rawFiles){
		
		// entferne nicht unterstuetzte Files und suche nach CSVs

		// suche nach Sirius files
		final List<File> siriusFiles = new ArrayList<>();
		for (File f : rawFiles) {
			if (f.getName().toLowerCase().endsWith(".sirius")) {
				siriusFiles.add(f);
			}
		}
		if (siriusFiles.size() > 0 ) {
			final ImportWorkspaceDialog wd = new ImportWorkspaceDialog(this);
			final WorkspaceWorker io = new WorkspaceWorker(this, wd, siriusFiles);
			io.execute();
			wd.start();
			io.flushBuffer();
		}

		DropImportDialog dropDiag = new DropImportDialog(this, rawFiles);
		if(dropDiag.getReturnValue()==ReturnValue.Abort){
			return;
		}
		
		List<File> csvFiles = dropDiag.getCSVFiles();
		List<File> msFiles = dropDiag.getMSFiles();
		List<File> mgfFiles = dropDiag.getMGFFiles();
		
		if(csvFiles.isEmpty()&&msFiles.isEmpty()&&mgfFiles.isEmpty()) return;
		
		//Frage den Anwender ob er batch-Import oder alles zu einen Experiment packen moechte
		
		if(csvFiles.size()>0&&(msFiles.size()+mgfFiles.size()==0)){   //nur CSV
			LoadController lc = new LoadController(this, config);
//			files
			
			lc.addSpectra(csvFiles,msFiles,mgfFiles);
			lc.showDialog();
			
			if(lc.getReturnValue() == ReturnValue.Success){
				ExperimentContainer ec = lc.getExperiment();
				
				importCompound(ec);
			}
		}else{
			DragAndDropOpenDialog diag = new DragAndDropOpenDialog(this);
			DragAndDropOpenDialogReturnValue rv = diag.getReturnValue();
			if(rv==DragAndDropOpenDialogReturnValue.abort){
				return;
			}else if(rv==DragAndDropOpenDialogReturnValue.oneExperimentForAll){
				LoadController lc = new LoadController(this, config);
				lc.addSpectra(csvFiles,msFiles,mgfFiles);
				lc.showDialog();
				
				if(lc.getReturnValue() == ReturnValue.Success){
					ExperimentContainer ec = lc.getExperiment();
					
					importCompound(ec);
				}
			}else if(rv==DragAndDropOpenDialogReturnValue.oneExperimentPerFile){
				importOneExperimentPerFile(msFiles,mgfFiles);
			}
		}
	}
	
	/////////////////// Mouselistener ///////////////////////

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.isPopupTrigger()){
			this.expPopMenu.show(e.getComponent(), e.getX(), e.getY());			
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.isPopupTrigger()){
			this.expPopMenu.show(e.getComponent(), e.getX(), e.getY());			
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	public int numberOfCompounds() {
		return compoundModel.getSize();
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode()==27) {
			deleteCurrentCompound();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
}

class SiriusSaveFileFilter extends FileFilter{

	@Override
	public boolean accept(File f) {
		if(f.isDirectory()) return true;
		String name = f.getName();
		if(name.endsWith(".sirius")){
			return true;
		}
		return false;
	}

	@Override
	public String getDescription() {
		return ".sirius";
	}
	
}
