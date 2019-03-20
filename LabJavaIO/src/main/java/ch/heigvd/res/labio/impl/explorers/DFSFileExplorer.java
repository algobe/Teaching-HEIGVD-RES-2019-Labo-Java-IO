package ch.heigvd.res.labio.impl.explorers;

import ch.heigvd.res.labio.interfaces.IFileExplorer;
import ch.heigvd.res.labio.interfaces.IFileVisitor;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This implementation of the IFileExplorer interface performs a depth-first
 * exploration of the file system and invokes the visitor for every encountered
 * node (file and directory). When the explorer reaches a directory, it visits all
 * files in the directory and then moves into the subdirectories.
 * 
 * @author Olivier Liechti
 */
public class DFSFileExplorer implements IFileExplorer {

  @Override
  public void explore(File rootDirectory, IFileVisitor visitor) {

    File[] sub = rootDirectory.listFiles();
    
    visitor.visit(rootDirectory);
    if (sub != null) {
      for (final File file : sub) {
        ArrayList<File> subdirectories = new ArrayList<>();
        if (!file.isDirectory()) {
          visitor.visit(file);
        } else {
          subdirectories.add(file);
        }

        for (File subdirectory : subdirectories) {
          explore(subdirectory, visitor);
        }
      }
    }
  }

}
