package controller;

import static view.ConsoleInterface.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ExportController {
	
	private final String DEFAULT_CONFIG_FILENAME = "config.txt";
	private String config_separator = ">";
	private Map<String,String> config;
	
	public ExportController() {
		config = new HashMap<>();
		if( !loadConfig() ) {
			askConfig();
		}
	}
	
	public void run() {
		
		if(config.get("copy").equals("true")) {
			log("Inizio copia di file verso la directory destinazione...");
			String startDir =  config.get("startDir");
			Path startPath = Paths.get( startDir );
			try {				
				Files.walkFileTree(startPath, new CopyFileVisitor( startDir, config.get("destDir") ) );
			} catch (IOException e) {
				log( "Eccezione Export Controller run method: " + e.getMessage() );
			}
			log("Copia completata.");
		}
		
		if(config.get("replace").equals("true")) {
			log("Inizio sostituzione dei pattern...");
			System.out.println("Ricerca del pattern: " + config.get("startPattern"));
			System.out.println("Ricerca del pattern: " + config.get("endPattern"));
			String fileRegex = config.get("fileRegex");
			String startPattern = config.get("startPattern");
			String endPattern = config.get("endPattern");
			try {
				String replaceStartDir = config.get("replaceStartDir");
				Files.walkFileTree(Paths.get(replaceStartDir), new ReplaceFileVisitor( fileRegex, startPattern, endPattern ) );
			} catch (IOException e) {
				log( "Eccezione Export Controller run method: " + e.getMessage() );
			}
			log("Sostituzione dei pattern completata");
		}
		log("Export completato" );
	}
	
	private boolean loadConfig() {
		try(BufferedReader in = new BufferedReader(new FileReader(DEFAULT_CONFIG_FILENAME))){
			String line = null;
			while( (line = in.readLine()) != null){
				String[] data = line.split(config_separator);
				config.put(data[0],data[1]);
			}
			return true;
		}catch(Exception e) {
			log(e.getMessage());
		}
		
		return false;
	}
	
	private void askConfig() {
		try (PrintWriter out = new PrintWriter(DEFAULT_CONFIG_FILENAME)){
			log("Il File di configurazione non esiste, inserire le informazioni necessarie...");
			String copyOption = null;
			boolean copy = false;
			do{
				copyOption = read(" Si desidera abilitare l'opzione copia per copiare il progetto in un'altra directory? (Y/N)");
				if(copyOption.trim().toLowerCase().equals("y")) {
					out.println("copy" + config_separator + "true");
					config.put("copy", "true");
					copy = true;
					break;
				}else {
					if(copyOption.trim().toLowerCase().equals("n")) {
						out.println("copy" + config_separator + "false");
						config.put("copy", "false");
						break;
					}else {
						log("Risposta inserita non valida...");
					}
				}
			}while(true);
			
			if(copy) {
				String startDir = read("Inserire la directory da copiare");
				String destDir = read("Inserire la directory di destinazione");
				out.println("startDir" + config_separator + startDir);
				out.println("destDir" + config_separator + destDir);
				out.flush();
				config.put("startDir", "startDir");
				config.put("destDir", "destDir");
			}
			
			String replaceOption = null;
			boolean replace = false;
			do{
				replaceOption = read(" Si desidera abilitare l'opzione Replace? (Y/N)");
				if(replaceOption.trim().toLowerCase().equals("y")) {
					out.println("replace" + config_separator + "true");
					config.put("replace","true");
					replace = true;
					break;
				}else {
					if(replaceOption.trim().toLowerCase().equals("n")) {
						out.println("replace" + config_separator + "false");
						config.put("replace","false");
						break;
					}else {
						log("Risposta inserita non valida...");
					}
				}
			}while(true);
			
			if(replace) {
				String replaceStartDir = read("Inserire la directory da cui iniziare il lavoro di sostituzione");
				String fileRegex = read("Inserire il regex dei file che si vuole modificare");
				String startPattern = read("Inserire il pattern che si vuole modificare");
				String endPattern = read("Inserire il pattern che sostituirà l'originale");
				out.println("replaceStartDir" + config_separator + replaceStartDir);
				out.println("fileRegex" + config_separator + fileRegex);
				out.println("startPattern" + config_separator + startPattern);
				out.println("endPattern" + config_separator + endPattern);
				out.flush();
				config.put("replaceStartDir",replaceStartDir);
				config.put("fileRegex",fileRegex);
				config.put("startPattern",startPattern);
				config.put("endPattern",endPattern);
			}
		}catch(Exception e) {
			log(e.getMessage());
		}
		
	}
}
