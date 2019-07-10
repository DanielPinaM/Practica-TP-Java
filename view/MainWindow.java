package es.ucm.fdi.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;

import es.ucm.fdi.Objects.Junction;
import es.ucm.fdi.Objects.Road;
import es.ucm.fdi.Objects.RoadsMap;
import es.ucm.fdi.Objects.Vehicle;
import es.ucm.fdi.control.Controller;
import es.ucm.fdi.events.Event;
import es.ucm.fdi.exception.SimulationError;
import es.ucm.fdi.model.TrafficSimulator;
import es.ucm.fdi.model.TrafficSimulatorObserver;
import es.ucm.fdi.threads.DelayRunner;

@SuppressWarnings("serial")
public class MainWindow extends JFrame implements TrafficSimulatorObserver{

	private Controller ctrl;
	private RoadsMap map;
	private int time;
	@SuppressWarnings("unused")
	private List<Event> events;
	private boolean redirectOutput;
	private Thread t;

	private JPanel mainPanel;
	//MENU
	private JMenu fileMenu;
	private JMenu simulatorMenu;
	private JMenu reportsMenu;
	@SuppressWarnings("unused")
	private ReportsDialog reportsDialog;
	//TOOLBAR
	private JToolBar toolBar;
	private JFileChooser fc;
	private JButton loadButton;
	private JButton saveButton;
	private JButton clearEventsButton;
	private JButton checkInEventsButton;
	private JButton runButton;
	private JButton stopButton;
	private JButton resetButton;
	private JSpinner delaySpinner;
	private JSpinner stepsSpinner;
	private JTextField timeViewer;
	private JButton genReportsButton;
	private JButton clearReportsButton;
	private JButton saveReportsButton;
	private JButton quitButton;
	//TOP PANEL
	private JTextArea eventsEditor;
	private TablePanel<Event> eventsView;
	private JTextArea reportsArea;
	//BOTTOM PANEL
	private TablePanel<Vehicle> vehicleView; 
	private TablePanel<Road> roadsView; 
	private TablePanel<Junction> junctionsView; 
	private MapComponent mapView;

	//BOTTOM STATUS BAR
	private StatusBar statusBar; // opcional

	private File currentFile;
	private int steps;
	public MainWindow(TrafficSimulator model, String inFileName, Controller ctrl) {
		super("Traffic Simulator");
		this.ctrl = ctrl;

		currentFile = inFileName != null ? new File(inFileName) : null;
		initGUI();
		ctrl.addObserver(this);
		redirectOutput = true;
		time = 0;
	}

	private void initGUI() {
		mainPanel = new JPanel(new BorderLayout());

		this.setContentPane(mainPanel);
		this.setBackground(Color.BLACK);

		JPanel centralPanel = this.createCentralPanel();
		//MENU
		addMenuBar(this);
		//TOOLBAR
		addToolBar(this);
		//Reports dialog...
		this.reportsDialog = new ReportsDialog(this, this.ctrl);
		//STATUS BAR
		addStatusBar(this.mainPanel); 
		//PANEL CENTRAL
		mainPanel.add(centralPanel,BorderLayout.CENTER);

		// 	PANEL SUPERIOR
		this.createTopPanel(centralPanel);
		// 	PANEL INFERIOR
		this.createBottomPanel(centralPanel);
		// FILE CHOOSER
		this.fc = new JFileChooser();



		// Añade configuraciones de la ventana principal
		this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowListener() {


			@Override
			public void windowClosing(WindowEvent e) {
				exitWithConfirmation();
			}

			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {

			}

			@Override
			public void windowActivated(WindowEvent e) {

			}

			@Override
			public void windowDeactivated(WindowEvent e) {

			}



		});
		this.pack();
		this.setVisible(true);
		if(this.currentFile != null){
			try {
				this.eventsEditor.setText(this.leeFichero(this.currentFile));
				this.eventsEditor.setBorder(
						BorderFactory.createTitledBorder(
								BorderFactory.createLineBorder(Color.black), this.currentFile.getName()));
				this.statusBar.setInfo("Fichero " + currentFile.getName() +
						" loaded into the editor");
			} catch (FileNotFoundException e1) {
				this.showErrorMessage("File" + this.currentFile.toString() + " not found");
			} catch (IOException e1) {
				this.showErrorMessage("Error reading from: " + this.currentFile.toString());
			}
		}
	}

	private void addMenuBar(MainWindow mainWindow) {
		this.fileMenu = new JMenu("File");
		this.simulatorMenu = new JMenu("Simulator");
		this.reportsMenu = new JMenu("Reports");
		JMenuBar menuBar = new JMenuBar();
		//File:
		//	Load
		JMenuItem loadEvents = new JMenuItem("Load Events");
		loadEvents.setMnemonic(KeyEvent.VK_L);
		loadEvents.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, 
				ActionEvent.ALT_MASK));
		loadEvents.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.loadFile();
			}
		});
		//	Save Events
		JMenuItem saveEvents = new JMenuItem("Save Events");
		saveEvents.setMnemonic(KeyEvent.VK_S);
		saveEvents.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, 
				ActionEvent.ALT_MASK));
		saveEvents.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					mainWindow.saveEventsToTxt();
				} catch (IOException e1) {
					mainWindow.showErrorMessage("Not able to save events: " + e1.getMessage());
				}
			}
		});

		// Save Report
		JMenuItem saveReport = new JMenuItem("Save Report");	
		saveReport.setMnemonic(KeyEvent.VK_R);
		saveReport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, 
				ActionEvent.ALT_MASK));
		saveReport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					mainWindow.saveReportsToTxt();
				} catch (IOException e1) {
					mainWindow.showErrorMessage("Not able to save reports: " + e1.getMessage());
				}
			}
		});
		JMenuItem exit = new JMenuItem("Exit");
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, 
				ActionEvent.ALT_MASK));
		exit.setMnemonic(KeyEvent.VK_E);
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.exitWithConfirmation();
			}
		});
		this.fileMenu.add(loadEvents);
		this.fileMenu.add(saveEvents);
		this.fileMenu.addSeparator();
		this.fileMenu.add(saveReport);
		this.fileMenu.addSeparator();
		this.fileMenu.add(exit);
		this.fileMenu.setMnemonic(KeyEvent.VK_F);

		menuBar.add(this.fileMenu);

		//Simulator:
		JMenuItem run = new JMenuItem("Run");
		run.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				mainWindow.run();
			}
		});
		JMenuItem reset = new JMenuItem("Reset");
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.reset();
			}
		});
		JCheckBoxMenuItem redirect = new JCheckBoxMenuItem("Redirect Output");
		redirect.setSelected(true);
		redirect.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e){
				mainWindow.redirectOutput();
			}
		});
		this.simulatorMenu.add(run);
		this.simulatorMenu.add(reset);
		this.simulatorMenu.add(redirect);
		this.simulatorMenu.setMnemonic(KeyEvent.VK_S);
		menuBar.add(this.simulatorMenu);
		//Reports:
		JMenuItem gen = new JMenuItem("Generate reports for all simulation objects");
		gen.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				mainWindow.generateReports();
			}
		});

		JMenuItem gen2 = new JMenuItem("Generate specific reports");
		gen2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				mainWindow.showReportsDialog();
			}
		});
		JMenuItem clear = new JMenuItem("Clear");
		clear.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				mainWindow.clearReports();
			}
		});
		gen.setMnemonic(KeyEvent.VK_G);
		clear.setMnemonic(KeyEvent.VK_C);
		this.reportsMenu.add(gen);
		this.reportsMenu.add(gen2);
		this.reportsMenu.add(clear);
		this.reportsMenu.setMnemonic(KeyEvent.VK_R);
		menuBar.add(this.reportsMenu);
		this.setJMenuBar(menuBar);
	}

	
	public void showReportsDialog() {
		this.reportsDialog.mostrar();
	}
	
	protected void generateSpecificReports(List<Vehicle> vehicles,
											List<Road> roads,
											List<Junction> junctions) {
		//JOptionPane.showMessageDialog(this, "Parte opcional no realizada");
		this.reportsArea.setText(this.map.getReports(vehicles, roads, junctions, this.time));
	}

	//----IMPLEMENTACIÓN DE LOS BOTONES DEL MENUBAR.

	private void loadFile() {
		this.fc.setDialogTitle("Open File");
		int returnVal = this.fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File fichero = this.fc.getSelectedFile();
			try {
				String s = leeFichero(fichero);
				this.ctrl.reset();
				this.currentFile = fichero;
				this.eventsEditor.setText(s);
				this.eventsEditor.setBorder(
						BorderFactory.createTitledBorder(
								BorderFactory.createLineBorder(Color.black), this.currentFile.getName()));
				this.statusBar.setInfo("Fichero " + fichero.getName() +
						" loaded into the editor");
			}
			catch (IOException e) {
				this.showErrorMessage("Error ocurred while reading file: " +
						e.getMessage());
			}
		}
	}

	private void showErrorMessage(String string) {
		JOptionPane.showMessageDialog(this.mainPanel,
				string, "ERROR",
				JOptionPane.WARNING_MESSAGE);
	}

	private String leeFichero(File fichero) throws IOException, FileNotFoundException{
		BufferedReader br = new BufferedReader(new FileReader(fichero.getAbsolutePath()));
		String ret;
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			ret = sb.toString();
		} finally {
			br.close();
		}

		return ret;
	}

	protected void saveEventsToTxt() throws IOException {
		this.fc.setDialogTitle("Save events");
		this.fc.setCurrentDirectory(new File("C:\\hlocal"));
		int selection = this.fc.showSaveDialog(null);

		if(selection == JFileChooser.APPROVE_OPTION){
			File saveFile = this.fc.getSelectedFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile));
			bw.write(this.eventsEditor.getText());
			bw.flush();
			bw.close();
			this.statusBar.setInfo("File " + saveFile.getName() +
					" saved at: " + this.fc.getCurrentDirectory());
		}


	}

	protected void saveReportsToTxt() throws IOException {
		this.fc.setDialogTitle("Save report");
		this.fc.setCurrentDirectory(new File("C:\\hlocal"));
		int selection = this.fc.showSaveDialog(null);
		BufferedWriter bw = new BufferedWriter(new FileWriter(this.fc.getSelectedFile()));
		if(selection == JFileChooser.APPROVE_OPTION){
			bw.write(this.reportsArea.getText());
		}

		this.statusBar.setInfo("Events saved into file: " +this.fc.getSelectedFile().toString());
		bw.close();
	}

	protected void exitWithConfirmation() {
		if(JOptionPane.showConfirmDialog(mainPanel, "Are you sure you want to exit?") ==
				JOptionPane.YES_OPTION)
			exit();
	}
	
	protected void exit() {
		this.setVisible(false);
		this.dispose();
		System.exit(0);
	}


	private void addToolBar(MainWindow mainWindow) {
		toolBar = new JToolBar();
		//Load:
		loadButton = new JButton();
		loadButton.setActionCommand("load");
		loadButton.setToolTipText("Load a file");
		loadButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				mainWindow.loadFile();
			}
		});
		loadButton.setIcon(new ImageIcon("iconsp5/Abrir26.png"));
		toolBar.add(loadButton);
		//Save:
		saveButton = new JButton();
		saveButton.setActionCommand("save");
		saveButton.setToolTipText("Save a file");
		saveButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				try {
					mainWindow.saveEventsToTxt();
				} catch (IOException e1) {
					mainWindow.showErrorMessage("Not able to save files: " + e1.getMessage());
				}
			}
		});;
		saveButton.setIcon(new ImageIcon("iconsp5/Guardar26.png"));
		toolBar.add(saveButton);
		//Clear:
		clearEventsButton = new JButton();
		clearEventsButton.setActionCommand("clear events");
		clearEventsButton.setToolTipText("Clear the events zone");
		clearEventsButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				mainWindow.clearEvents();
			}
		});
		clearEventsButton.setIcon(new ImageIcon("iconsp5/Limpiar26.png"));
		toolBar.add(clearEventsButton);

		toolBar.addSeparator();
		//CheckIn:
		checkInEventsButton = new JButton();
		checkInEventsButton.setActionCommand("checkin events");
		checkInEventsButton.setToolTipText("Check in events");
		checkInEventsButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				mainWindow.checkInEvents();
			}
		});
		checkInEventsButton.setIcon(new ImageIcon("iconsp5/Insertar26.png"));
		toolBar.add(checkInEventsButton);
		//Run:
		runButton = new JButton();
		runButton.setActionCommand("run");
		runButton.setToolTipText("Run the simulator");
		runButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				mainWindow.run();
			}
		});
		runButton.setIcon(new ImageIcon("iconsp5/Play26.png"));
		toolBar.add(runButton);
		
		//Stop:
		stopButton = new JButton();
		stopButton.setActionCommand("stop");
		stopButton.setToolTipText("Stop the simulator");
		stopButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				t.interrupt();
				enableButtons();

			}
		});
		stopButton.setIcon(new ImageIcon("iconsp5/stop26.png"));
		stopButton.setEnabled(false);
		toolBar.add(stopButton);
		
		//Reset:
		resetButton = new JButton();
		resetButton.setActionCommand("reset");
		resetButton.setToolTipText("Reset the simulator");
		resetButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				mainWindow.reset();
			}
		});
		resetButton.setIcon(new ImageIcon("iconsp5/Reset26.png"));
		toolBar.add(resetButton);
		
		//Delay Spinner:
		toolBar.add(new JLabel("Delay: "));
		delaySpinner = new JSpinner((new SpinnerNumberModel(0, 0, 1000000, 1)));
		delaySpinner.setToolTipText("Delay");
		delaySpinner.setMaximumSize(new Dimension(70, 70));
		delaySpinner.setMinimumSize(new Dimension(70, 70));
		delaySpinner.setValue(0);
		toolBar.add(delaySpinner);
		
		//Steps spinner:
		toolBar.add(new JLabel("Steps: "));
		stepsSpinner = new JSpinner((new SpinnerNumberModel(5, 1, 1000, 1)));
		stepsSpinner.setToolTipText("Number of steps to simulate (1-1000)");
		stepsSpinner.setMaximumSize(new Dimension(70, 70));
		stepsSpinner.setMinimumSize(new Dimension(70, 70));
		stepsSpinner.setValue(1);
		toolBar.add(stepsSpinner);
		//Time:
		toolBar.add(new JLabel(" Tiempo: "));
		timeViewer = new JTextField("0", 5);
		timeViewer.setToolTipText("Current time");
		timeViewer.setMaximumSize(new Dimension(70, 70));
		timeViewer.setMinimumSize(new Dimension(70, 70));
		timeViewer.setEditable(false);
		toolBar.add(timeViewer);

		toolBar.addSeparator();
		//Reports:
		genReportsButton = new JButton();
		genReportsButton.setActionCommand("genReports");
		genReportsButton.setToolTipText("Generate reports for current simulation");
		genReportsButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				mainWindow.generateReports();
			}
		});
		genReportsButton.setIcon(new ImageIcon("iconsp5/report26.png"));
		toolBar.add(genReportsButton);

		//clearReports
		clearReportsButton = new JButton();
		clearReportsButton.setActionCommand("Clear reports");
		clearReportsButton.setToolTipText("Clear the reports zone");
		clearReportsButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				mainWindow.clearReports();
			}
		});
		clearReportsButton.setIcon(new ImageIcon("iconsp5/clearReports26.png"));
		toolBar.add(clearReportsButton);
		//saveReportsButton	
		saveReportsButton = new JButton();
		saveReportsButton.setToolTipText("Save reports to txt");
		saveReportsButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				try {
					mainWindow.saveReportsToTxt();
				} catch (IOException e1) {
					mainWindow.showErrorMessage("Not able to save reports: " + e1.getMessage());
				}
			}
		});
		saveReportsButton.setIcon(new ImageIcon("iconsp5/txt26.png"));
		toolBar.add(saveReportsButton);
		//Quit button
		quitButton = new JButton();
		quitButton.setActionCommand("quit");
		quitButton.setToolTipText("Exit the application");
		quitButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				mainWindow.exitWithConfirmation();
			}
		});
		quitButton.setIcon(new ImageIcon("iconsp5/off26.png"));
		toolBar.add(quitButton);

		this.add(toolBar, BorderLayout.PAGE_START);
	}

	//----IMPLEMENTACIÓN DE LOS BOTONES DEL TOOLBAR.

	protected void clearEvents() {
		this.eventsEditor.setText("");
	}

	protected void checkInEvents() {
		String s = this.eventsEditor.getText();

		try {
			if(s.equalsIgnoreCase(""))
				this.showWarningMessage("Not able to read events, events editor zone is empty.");
			this.ctrl.loadEvents(new ByteArrayInputStream(s.getBytes()));
		} catch (Exception e) {
			this.showErrorMessage("Cannot checkin events."
					+ System.lineSeparator() + e.getMessage());
		}
	}

	private void showWarningMessage(String string) {
		JOptionPane.showMessageDialog(this.mainPanel, string, "WARNING", JOptionPane.INFORMATION_MESSAGE);

	}

	protected void run() {
		int stepsLeft = steps;
		try {
			this.steps = Integer.parseInt(this.stepsSpinner.getValue().toString());
			int delay = Integer.parseInt(this.delaySpinner.getValue().toString());
			if(delay == 0) {
				if(steps > 0)
					this.ctrl.execute(this.steps);
			}else {
				stepsLeft = executeDelay(delay, this.steps);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			this.showErrorMessage(e.getMessage());
		}
		
		if(stepsLeft == 0 || stepsLeft == -1) {
			enableButtons();
			this.t.interrupt();
		}
		
	}
	

	public int executeDelay(int delay, int stepsLeft) {
		disableButtons();
		Runnable dt = new DelayRunner(delay, stepsLeft, this.ctrl);
		this.steps--;
		this.t = new Thread(dt);
		this.t.start();
		return stepsLeft;
	}

	protected void generateReports() {
		String s =  this.map.generateReport(time);
		this.reportsArea.setText(s);
		this.reportsArea.setBorder(
				BorderFactory.createTitledBorder(
						BorderFactory.createLineBorder(Color.black), this.currentFile.getName()));
	}

	protected void clearReports() {
		this.reportsArea.setText("");
	}


	protected void redirectOutput() {
		this.redirectOutput = !this.redirectOutput;
	}	

	protected void reset() {
		this.ctrl.reset();
		this.clearEvents();
		this.clearReports();
	}

	protected void stop() {
		this.stopButton.setEnabled(false);
		this.ctrl.stop();
	}

	private void createTopPanel(JPanel centralPanel) {
		JPanel topPanel = new JPanel();

		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		addEventsEditor(topPanel); // editor de eventos
		addEventsView(topPanel); // cola de eventos
		addReportsArea(topPanel); // zona de informes
		centralPanel.add(topPanel);
	}



	private void addEventsEditor(JPanel panel) {		
		eventsEditor = new JTextArea("");		
		eventsEditor.setEditable(true);
		eventsEditor.setLineWrap(true);
		eventsEditor.setWrapStyleWord(true);
		eventsEditor.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Events"));

		addEventsEditorMenu(eventsEditor, this);
		panel.add(eventsEditor); 

		JScrollPane sP = new JScrollPane(eventsEditor,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		eventsEditor.setMinimumSize(new Dimension(300, 200));
		eventsEditor.setPreferredSize(new Dimension(300, 200));
		eventsEditor.setMaximumSize(new Dimension(300, 200));
		sP.setPreferredSize(new Dimension(300, 200));
		panel.add(sP);
		refreshEventsAreaBorder();

	}

	private void addEventsEditorMenu(JTextArea panel, MainWindow mw){
		JPopupMenu menu = new JPopupMenu();
		JMenu addTemplateMenu = new JMenu("Add Template");

		menu.add(addTemplateMenu);
		menu.addSeparator();
		panel.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				if(arg0.isPopupTrigger() && panel.isEnabled()) {
					panel.setCaretPosition(arg0.getY());
					menu.show(arg0.getComponent(), arg0.getX(), arg0.getY());

				}

			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				if(arg0.isPopupTrigger() && panel.isEnabled()) {
					menu.show(arg0.getComponent(), arg0.getX(), arg0.getY());
				}

			}

		});

		JMenuItem load = new JMenuItem("Load");
		load.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				mw.loadFile();
			}
		});
		menu.add(load);
		JMenuItem save = new JMenuItem("Save");
		menu.add(save);
		save.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				try {
					mw.saveEventsToTxt();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		JMenuItem clear = new JMenuItem("Clear");
		menu.add(clear);
		clear.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				mw.clearEvents();
			}
		});


		JMenuItem newRR = new JMenuItem("New RR Junction");
		newRR.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				panel.insert("[new_junction]" + System.lineSeparator() +
						"time = <NONEG-INTEGER>" + System.lineSeparator() +
						"id = <ROAD-ID>" + System.lineSeparator() +
						"type = rr" + System.lineSeparator() +
						"max_time_slice = <POSITIVE-INTEGER>" + System.lineSeparator() +
						"min_time_slice = <POSITIVE-INTEGER>"+System.lineSeparator(), panel.getCaretPosition());
			}

		});

		JMenuItem newMC = new JMenuItem("New MC Junction");
		newMC.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				panel.insert("[new_junction]" + System.lineSeparator() +
						"time = <NONEG-INTEGER>" + System.lineSeparator() +
						"id = <ROAD-ID>" + System.lineSeparator() +
						"type = mc" + System.lineSeparator(),panel.getCaretPosition());
			}

		});

		JMenuItem newJ = new JMenuItem("New Junction");
		newJ.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				panel.insert("[new_junction]" + System.lineSeparator() +
						"time = <NONEG-INTEGER>" + System.lineSeparator() +
						"id = <ROAD-ID>" + System.lineSeparator(),panel.getCaretPosition());
			}

		});
		JMenuItem newDirt = new JMenuItem("New Dirt Road");
		newDirt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				panel.insert("[new_road]" + System.lineSeparator() +
						"time = <NONEG-INTEGER>" + System.lineSeparator() +
						"id = <ROAD-ID>" + System.lineSeparator() +
						"src = <JUNC-ID>" + System.lineSeparator() +
						"dest = <JUNC-ID>" + System.lineSeparator() +
						"max_speed = <POSITIVE-INTEGER>" + System.lineSeparator() +
						"length = <POSITIVE-INTEGER>" + System.lineSeparator() +
						"type = dirt" + System.lineSeparator(), panel.getCaretPosition());

			}

		});

		JMenuItem newLanes = new JMenuItem("New Lanes Road");
		newLanes.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				panel.insert("[new_road]" + System.lineSeparator() +
						"time = <NONEG-INTEGER>" + System.lineSeparator() +
						"id = <ROAD-ID>" + System.lineSeparator() +
						"src = <JUNC-ID>" + System.lineSeparator() +
						"dest = <JUNC-ID>" + System.lineSeparator() +
						"max_speed = <POSITIVE-INTEGER>" + System.lineSeparator() +
						"length = <POSITIVE-INTEGER>" + System.lineSeparator() +
						"type = lanes" + System.lineSeparator() +
						"lanes = <POSITIVE-INTEGER>" + System.lineSeparator(), panel.getCaretPosition());

			}

		});
		JMenuItem newRoad = new JMenuItem("New Road");
		newRoad.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				panel.insert("[new_road]" + System.lineSeparator() +
						"time = <NONEG-INTEGER>" + System.lineSeparator() +
						"id = <ROAD-ID>" + System.lineSeparator() +
						"src = <JUNC-ID>" + System.lineSeparator() +
						"dest = <JUNC-ID>" + System.lineSeparator() +
						"max_speed = <POSITIVE-INTEGER>" + System.lineSeparator() +
						"length = <POSITIVE-INTEGER>" + System.lineSeparator(), panel.getCaretPosition());

			}

		});

		JMenuItem newBike = new JMenuItem("New Bike");
		newBike.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				panel.insert("[new_vehicle]" + System.lineSeparator() +
						"time = <NONEG-INTEGER>" + System.lineSeparator() +
						"id = <ROAD-ID>" + System.lineSeparator() +
						"itinerary = <JUNC-ID>,<JUNC-ID>(,<JUNC-ID>)*" + System.lineSeparator() +
						"max_speed = <POSITIVE-INTEGER>" + System.lineSeparator() +
						"type = bike" + System.lineSeparator(), panel.getCaretPosition());

			}

		});

		JMenuItem newCar = new JMenuItem("New Car");
		newCar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				panel.insert("[new_vehicle]" + System.lineSeparator() +
						"time = <NONEG-INTEGER>" + System.lineSeparator() +
						"id = <ROAD-ID>" + System.lineSeparator() +
						"itinerary = <JUNC-ID>,<JUNC-ID>(,<JUNC-ID>)*" + System.lineSeparator() +
						"max_speed = <POSITIVE-INTEGER>" + System.lineSeparator() +
						"type = car" + System.lineSeparator() +
						"resistance = <POSITIVE-INTEGER>" + System.lineSeparator() +
						"fault_probability = <NONEG-DOUBLE>" + System.lineSeparator() +
						"max_fault_duration = <POSITIVE-INTEGER>" + System.lineSeparator() +
						"seed = <POSITIVE-LONG>", panel.getCaretPosition());

			}

		});

		JMenuItem newVehicle = new JMenuItem("New Vehicle");
		newVehicle.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				panel.insert("[new_vehicle]" + System.lineSeparator() +
						"time = <NONEG-INTEGER>" + System.lineSeparator() +
						"id = <ROAD-ID>" + System.lineSeparator() +
						"max_speed = <POSITIVE-INTEGER>" + System.lineSeparator() +
						"itinerary = <JUNC-ID>,<JUNC-ID>(,<JUNC-ID>)*" + System.lineSeparator(), panel.getCaretPosition());

			}

		});

		JMenuItem newMakeFaulty = new JMenuItem("Make Vehicle Faulty");
		newMakeFaulty.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				panel.insert("[make_vehicle_faulty]" + System.lineSeparator() +
						"time = <NONEG-INTEGER>" + System.lineSeparator() +
						"vehicles = <VEHICLE-ID>(,<VEHICLE-ID>)*" + System.lineSeparator() +
						"duration = <POSITIVE-INTEGER>" + System.lineSeparator(), panel.getCaretPosition());

			}

		});
		addTemplateMenu.add(newRR);
		addTemplateMenu.add(newMC);
		addTemplateMenu.add(newJ);
		addTemplateMenu.add(newDirt);
		addTemplateMenu.add(newLanes);
		addTemplateMenu.add(newRoad);
		addTemplateMenu.add(newBike);
		addTemplateMenu.add(newCar);
		addTemplateMenu.add(newVehicle);
		addTemplateMenu.add(newMakeFaulty);




		panel.add(menu);
	}

	private void addEventsView(JPanel panel) {

		String[] eventsColumIds = {"#", "Time", "Type"};

		eventsView =  new TablePanel<Event>("Events Queue: ",
				new EventsTableModel(eventsColumIds, this.ctrl)); 
		eventsView.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Events queue"));
		panel.add(eventsView);

	}

	private void addReportsArea(JPanel panel) {
		reportsArea = new JTextArea("");
		reportsArea.setEditable(false);
		reportsArea.setLineWrap(true);
		reportsArea.setWrapStyleWord(true);
		reportsArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Reports"));
		reportsArea.setMinimumSize(new Dimension(300, 200));
		reportsArea.setPreferredSize(new Dimension(300, 200));
		reportsArea.setMaximumSize(new Dimension(300, 200));

		panel.add(reportsArea);
		JScrollPane sP = new JScrollPane(reportsArea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		panel.add(sP);

	}


	private void createBottomPanel(JPanel centralPanel) {
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(1,2));
		addLeftSideBottomPanel(bottomPanel);
		addMap(bottomPanel);
		centralPanel.add(bottomPanel);
	}

	private void addLeftSideBottomPanel(JPanel panel){
		JPanel leftSide = new JPanel();
		leftSide.setLayout(new GridLayout(3,1));
		addVehiclesTable(leftSide);
		addRoadsTable(leftSide);
		addJunctionsTable(leftSide);
		panel.add(leftSide);
	}

	private void addVehiclesTable(JPanel panel) {
		String[] columIds = {"ID", "Road", "Location", "Speed", "Km", "Faulty Time", "Itinerary"};

		vehicleView = new TablePanel<Vehicle>("Vehicles",
				new VehiclesTableModel(columIds, this.ctrl));
		vehicleView.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Vehicles"));
		panel.add(vehicleView);

	}

	private void addRoadsTable(JPanel panel) {
		String[] columIds = {"ID", "From", "To", "Length", "Max Speed", "Vehicles"};

		roadsView = new TablePanel<Road>("Roads", 
				new RoadsTableModel(columIds, this.ctrl));

		roadsView.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Roads"));
		panel.add(roadsView);
	}


	private void addJunctionsTable(JPanel panel) {
		String[] columIds = {"ID", "Green", "Red"};

		junctionsView = new TablePanel<Junction>("Junctions", 
				new JunctionsTableModel<Junction>(columIds, this.ctrl));

		junctionsView.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Junctions"));
		panel.add(junctionsView);
	}

	private void addMap(JPanel panel) {
		this.mapView = new MapComponent(this.ctrl);
		JScrollPane sP = new JScrollPane(mapView, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		panel.add(sP);
	}






	private void addStatusBar(JPanel mainPanel) {
		this.statusBar = new StatusBar("Bienvenido al simulador !",this.ctrl);
		mainPanel.add(this.statusBar,BorderLayout.PAGE_END);
	}



	private void refreshEventsAreaBorder() {
	}

	private JPanel createCentralPanel() {
		JPanel panelCentral = new JPanel();
		panelCentral.setLayout(new GridLayout(2,1));
		return panelCentral;
	}

	@Override
	public void registered(int time, RoadsMap map, List<Event> events) {
		
	}

	@Override
	public void reset(int time, RoadsMap map, List<Event> events) {
		this.map = map;
		this.time = time;
		this.events = events;
		this.eventsEditor.setBorder(
				BorderFactory.createTitledBorder(
						BorderFactory.createLineBorder(Color.black), "Events"));
		this.statusBar.setInfo("Traffic Simulator has ben restarted.");
	}

	@Override
	public void eventAdded(int time, RoadsMap map, List<Event> events) {

	}

	@Override
	public void advanced(int time, RoadsMap map, List<Event> events) {
		this.map = map;
		this.time = time;
		this.events = events;
		if(this.redirectOutput)
			this.generateReports();
		this.statusBar.setInfo("Generating info for step: " + (this.time) + ".");
	}

	@Override
	public void simulatorError(int time, RoadsMap map, List<Event> events,
			SimulationError e) {
		this.showErrorMessage(e.getMessage());
		this.statusBar.setInfo("An error has occured while simulating: " + e.getMessage());
	}

	private void enableButtons() {
		this.checkInEventsButton.setEnabled(true);
		this.clearEventsButton.setEnabled(true);
		this.clearReportsButton.setEnabled(true);
		this.genReportsButton.setEnabled(true);
		this.loadButton.setEnabled(true);
		this.quitButton.setEnabled(true);
		this.resetButton.setEnabled(true);
		this.runButton.setEnabled(true);
		this.saveButton.setEnabled(true);
		this.saveReportsButton.setEnabled(true);
		this.stopButton.setEnabled(false);
	}
	

	private void disableButtons() {
		this.checkInEventsButton.setEnabled(false);
		this.clearEventsButton.setEnabled(false);
		this.clearReportsButton.setEnabled(false);
		this.genReportsButton.setEnabled(false);
		this.loadButton.setEnabled(false);
		this.quitButton.setEnabled(false);
		this.resetButton.setEnabled(false);
		this.runButton.setEnabled(false);
		this.saveButton.setEnabled(false);
		this.saveReportsButton.setEnabled(false);
		this.stopButton.setEnabled(true);
	}


}
