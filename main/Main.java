package es.ucm.fdi.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import es.ucm.fdi.control.Controller;
import es.ucm.fdi.exception.MapException;
import es.ucm.fdi.exception.SimulationError;
import es.ucm.fdi.model.TrafficSimulator;
import es.ucm.fdi.view.MainWindow;


public class Main {


	private final static Integer limiteTiempoPorDefecto = 10;
	private static Integer limiteTiempo = null;
	private static String ficheroEntrada = null;
	private static String ficheroSalida = null;
	private static String type = null;
	private static void ParseaArgumentos(String[] args) {

		// define the valid command line options
		Options opcionesLineaComandos = Main.construyeOpciones();

		// parse the command line as provided in args
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine linea = parser.parse(opcionesLineaComandos, args);
			parseaOpcionHELP(linea, opcionesLineaComandos);
			parseaOpcionFicheroIN(linea);
			parseaOpcionFicheroOUT(linea);
			parseaOpcionSTEPS(linea);
			parseaOpcionModo(linea);
			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			String[] resto = linea.getArgs();
			if (resto.length > 0) {
				String error = "Illegal arguments:";
				for (String o : resto)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options construyeOpciones() {
		Options opcionesLineacomandos = new Options();

		opcionesLineacomandos.addOption(Option.builder("h").longOpt("help").desc("Muestra la ayuda.").build());
		opcionesLineacomandos.addOption(Option.builder("i").longOpt("input").hasArg().desc("Fichero de entrada de eventos.").build());
		opcionesLineacomandos.addOption(
				Option.builder("o").longOpt("output").hasArg().desc("Fichero de salida, donde se escriben los informes.").build());
		opcionesLineacomandos.addOption(Option.builder("t").longOpt("ticks").hasArg()
				.desc("Pasos que ejecuta el simulador en su bucle principal (el valor por defecto es " + Main.limiteTiempoPorDefecto + ").")
				.build());
		opcionesLineacomandos.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Modo de vista").build());
		return opcionesLineacomandos;
	}

	private static void parseaOpcionHELP(CommandLine linea, Options opcionesLineaComandos) {
		if (linea.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), opcionesLineaComandos, true);
			System.exit(0);
		}
	}

	private static void parseaOpcionFicheroIN(CommandLine linea) throws ParseException {
		Main.ficheroEntrada = linea.getOptionValue("i");
		if (Main.ficheroEntrada == null) {
			throw new ParseException("El fichero de eventos no existe");
		}
	}

	private static void parseaOpcionFicheroOUT(CommandLine linea) throws ParseException {
		Main.ficheroSalida = linea.getOptionValue("o");
	}

	private static void parseaOpcionSTEPS(CommandLine linea) throws ParseException {
		String t = linea.getOptionValue("t", Main.limiteTiempoPorDefecto.toString());
		try {
			Main.limiteTiempo = Integer.parseInt(t);
			assert (Main.limiteTiempo < 0);
		} catch (Exception e) {
			throw new ParseException("Valor invalido para el limite de tiempo: " + t);
		}
	}
	
	private static void parseaOpcionModo(CommandLine line) throws ParseException{
		type = line.getOptionValue("m", ModoEjecucion.BATCH.getModelDesc());
		if(type == null){
			throw new ParseException("Modo de vista no soportado");
		}
	}

	private static void iniciaModoEstandar() throws Exception {
		InputStream is = new FileInputStream(new File(Main.ficheroEntrada));
		OutputStream os = Main.ficheroSalida == null ? System.out : new FileOutputStream(new File(Main.ficheroSalida));
		TrafficSimulator sim = new TrafficSimulator();
		Controller ctrl = new Controller(sim,Main.limiteTiempo,is,os);
		try {
			ctrl.execute();
		} catch (SimulationError e) {
			System.out.println("FATAL ERROR: " + e.getMessage());
		} catch (MapException e) {
			System.out.println("FATAL ERROR: " + e.getMessage());
		}
		is.close();
		System.out.println("Done!");

	}

	private static void iniciaModoGrafico() throws FileNotFoundException,
	InvocationTargetException, InterruptedException {
		
		InputStream is = new FileInputStream(new File(Main.ficheroEntrada));
		OutputStream os = Main.ficheroSalida == null ?
				System.out : new FileOutputStream(new File(Main.ficheroSalida));
		
		TrafficSimulator sim = new TrafficSimulator();
		
		Controller ctrl = new Controller(sim, Main.limiteTiempo, is, os);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainWindow(sim, Main.ficheroEntrada, ctrl);
			}
		});
	}


	// este m�todo permite ejecutar todos los ficheros .ini que tengais en
	// un directorio, y genera en el mismo directorio los correspondientes
	// ficheros .out

	// de esta manera podeis ejecutar multiples ficheros .ini a la vez

	// ejecuta la simulaci�n 10 pasos.

	// en vuestro m�todo "main" s�lo ten�is que a�adir
	// Main.ejecutaFicheros("... el directorio que contiene los .ini");
	// y comentar el resto.


	@SuppressWarnings("unused")
	private static void ejecutaFicheros(String path) throws Exception {

		File dir = new File(path);

		if ( !dir.exists() ) {
			throw new FileNotFoundException(path);
		}

		File[] files = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".ini");
			}
		});

		for (File file : files) {
			Main.ficheroEntrada = file.getAbsolutePath();
			Main.ficheroSalida = file.getAbsolutePath() + ".out";
			Main.limiteTiempo = 10;
			Main.type = "batch";
		}

	}
	public static void main(String[] args) throws Exception {

		// example command lines:
		//
		// -i resources/examples/events/basic/ex1.ini
		// -i resources/examples/events/advanced/ex1.ini
		// --help
		//

		
		
		Main.ParseaArgumentos(args);
		if(type.equalsIgnoreCase("batch"))
			Main.iniciaModoEstandar();
		else if(type.equalsIgnoreCase("gui"))
			Main.iniciaModoGrafico();
			
	}

	private enum ModoEjecucion {

		BATCH("batch"), GUI("gui");

		private String descModo;

		private ModoEjecucion(String modeDesc) {
			descModo = modeDesc;
		}

		private String getModelDesc() {
			return descModo;
		}
	}

}
