package controller;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

import static view.ConsoleInterface.log;

public class CopyFileVisitor implements FileVisitor<Path>{

	private String destDir;
	private String startDir;
	
	public CopyFileVisitor(String startDir, String destDir) {
		this.startDir = startDir;
		this.destDir = destDir;
	}
	
	@Override
	public FileVisitResult postVisitDirectory(Path arg0, IOException arg1) throws IOException {
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes arg1) throws IOException {
		Path dirDest = getDestinationPath(dir);
		Files.createDirectories(dirDest);
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes arg1) throws IOException {
		Files.copy(file, getDestinationPath(file), StandardCopyOption.REPLACE_EXISTING);
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException e) throws IOException {
		log("Visit file failed: " + file + " Exception: " + e.getMessage());
		e.printStackTrace();
		return FileVisitResult.CONTINUE;
	}

	
	private Path getDestinationPath(Path path) {
		Path startPath = Paths.get(startDir);
		Path relativePath = startPath.relativize(path).normalize();
		Path destPath = Paths.get(destDir);
		return destPath.resolve(relativePath);		
	}
}
