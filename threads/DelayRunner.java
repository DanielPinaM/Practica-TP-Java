package es.ucm.fdi.threads;

import static java.lang.Thread.sleep;

import java.io.IOException;

import es.ucm.fdi.control.Controller;
import es.ucm.fdi.exception.MapException;
import es.ucm.fdi.exception.SimulationError;

public class DelayRunner implements Runnable{
	
	private int Delay;
	private int Steps;
	private Controller c;
	
	public DelayRunner(int Delay, int Steps, Controller c) {
		
		this.Delay = Delay;
		this.Steps = Steps;
		this.c = c;
		
	}
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(this.Steps > 0 && !Thread.interrupted()) {
			
			try {
				sleep(this.Delay);
			} catch (InterruptedException e) {
				
				Thread.currentThread().interrupt();
			}
			this.Steps--;
			try {
				this.c.execute(1);
			} catch (MapException | SimulationError | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
