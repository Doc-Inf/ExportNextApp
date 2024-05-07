package controller;

import static view.ConsoleInterface.log;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

public class ReplaceFileVisitor implements FileVisitor<Path>{

	private String fileRegex;
	private String startPattern;
	private String endPattern;
	
	public ReplaceFileVisitor(String fileRegex, String startPattern, String endPattern) {
		this.fileRegex = fileRegex;
		this.startPattern = startPattern;
		this.endPattern = endPattern;
	}
	
	@Override
	public FileVisitResult postVisitDirectory(Path arg0, IOException arg1) throws IOException {
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path arg0, BasicFileAttributes arg1) throws IOException {
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes arg1) throws IOException {
		if(file.getFileName().toString().matches(fileRegex)) {
			log("File trovato: " + file);
			List<String> lineeOriginali = Files.readAllLines(file);
			try(PrintWriter out = new PrintWriter(file.toString()) ){
				lineeOriginali.stream().forEach( linea -> {
					out.println(linea.replaceAll(startPattern, endPattern));
				});				
			}
		}
		
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException e) throws IOException {
		log("Visit file failed: " + file + " Exception: " + e.getMessage());
		return FileVisitResult.CONTINUE;
	}

}
