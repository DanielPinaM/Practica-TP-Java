package es.ucm.fdi.view;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import es.ucm.fdi.Objects.RoadsMap;
import es.ucm.fdi.control.Controller;
import es.ucm.fdi.events.Event;
import es.ucm.fdi.exception.SimulationError;
import es.ucm.fdi.model.TrafficSimulatorObserver;

@SuppressWarnings("serial")
public class StatusBar extends JPanel implements TrafficSimulatorObserver {

	private JLabel info;
	
	public StatusBar(String mensaje, Controller controlador) {
		 this.setLayout(new FlowLayout(FlowLayout.LEFT));
		 this.info = new JLabel(mensaje);
		 this.add(this.info);
		 this.setBorder(BorderFactory.createBevelBorder(1));
		 controlador.addObserver(this);
	}
	
	public void setInfo(String info){
		this.info.setText(info);
	}
	@Override
	public void registered(int time, RoadsMap map, List<Event> events) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset(int time, RoadsMap map, List<Event> events) {
		this.info.setText("Simulator restarted!");
		
	}

	@Override
	public void eventAdded(int time, RoadsMap map, List<Event> events) {
		this.info.setText("Event added to simulator!");
		
	}

	@Override
	public void advanced(int time, RoadsMap map, List<Event> events) {
		this.info.setText("Step: " + time);
	}

	@Override
	public void simulatorError(int time, RoadsMap map, List<Event> events,
			SimulationError e) {
		// TODO Auto-generated method stub
		
	}

}
