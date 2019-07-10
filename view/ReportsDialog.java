package es.ucm.fdi.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import es.ucm.fdi.Objects.Junction;
import es.ucm.fdi.Objects.Road;
import es.ucm.fdi.Objects.RoadsMap;
import es.ucm.fdi.Objects.Vehicle;
import es.ucm.fdi.control.Controller;
import es.ucm.fdi.events.Event;
import es.ucm.fdi.exception.SimulationError;
import es.ucm.fdi.model.TrafficSimulatorObserver;

@SuppressWarnings("serial")
public class ReportsDialog extends JDialog implements TrafficSimulatorObserver {
	
	private JButton gen;
	private JButton cancel;
	private JTextArea text;
	private MainWindow mw;
	private SimObjectPanel<Vehicle> panelVehiculos;
	private SimObjectPanel<Road> panelCarreteras;
	private SimObjectPanel<Junction>panelCruces;
	
	public ReportsDialog(MainWindow main, Controller ctrl) {
		super(main, "Reports Dialog");
		this.mw = main;
		ctrl.addObserver(this);
		initGUI();
		
	}
	
	private void initGUI() {
		//...
		this.setLayout(new GridLayout(3,1));
		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new GridLayout(1,3));
		JPanel bottomPanel = new JPanel();
		//JPanel topPanel = new JPanel();
		
		this.text = new JTextArea("In order to generate reports, select objects." + System.lineSeparator()
		+ "Use 'C' to deselect all" + System.lineSeparator() +
		"Use 'Ctrl+A' to select all", 1, 3);
		this.text.setEditable(false);
		this.text.setLineWrap(true);
		this.text.setWrapStyleWord(true);
		//topPanel.add(text);
		//topPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		//this.add(topPanel);
		this.add(this.text);
		
		this.panelVehiculos = new SimObjectPanel<Vehicle>("Vehicles");
		this.panelCarreteras = new SimObjectPanel<Road>("Roads");
		this.panelCruces = new SimObjectPanel<Junction>("Junctions");
		middlePanel.add(panelVehiculos);
		middlePanel.add(panelCarreteras);
		middlePanel.add(panelCruces);
		this.add(middlePanel);
		
		this.gen = new JButton("Generate");
		this.gen.setSize(50, 50);
		this.gen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				mw.generateSpecificReports(panelVehiculos.getSelectedItems(),
											panelCarreteras.getSelectedItems(),
											panelCruces.getSelectedItems());
			}
			
		});
		this.cancel = new JButton("Cancel");
		this.cancel.setSize(50, 50);
		
		this.cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				esconder();
			}
		});
		bottomPanel.add(gen);
		bottomPanel.add(cancel);
		this.add(bottomPanel);
		this.setTitle("Reports selection");
		this.setSize(300, 300);
		this.pack();
		this.setVisible(false);
	}
	@Override
	public void registered(int time, RoadsMap map, List<Event> events) {
		
	}
	@Override
	public void reset(int time, RoadsMap map, List<Event> events) {
		this.setMap(map);
		
	}
	@Override
	public void eventAdded(int time, RoadsMap map, List<Event> events) {
		this.setMap(map);
	}
	@Override
	public void advanced(int time, RoadsMap map, List<Event> events) {
		this.setMap(map);
	}
	@Override
	public void simulatorError(int time, RoadsMap map, List<Event> events, SimulationError e) {
		
	}
	

	public void mostrar() { 
		this.setVisible(true);
	}
	
	public void esconder() {
		this.setVisible(false);
	}
	
	private void setMap(RoadsMap mapa) {
	 this.panelVehiculos.setList(mapa.getVehiculos());
	 this.panelCarreteras.setList(mapa.getCarreteras());
	 this.panelCruces.setList(mapa.getCruces());
	}
	
	public List<Vehicle> getVehiculosSeleccionados() {
	 return this.panelVehiculos.getSelectedItems();
	}
	
	public List<Road> getCarreterasSeleccionadas() {
	 return this.panelCarreteras.getSelectedItems();
	}
	
	public List<Junction>getCrucesSeleccionados() {
	 return this.panelCruces.getSelectedItems();
	}
}