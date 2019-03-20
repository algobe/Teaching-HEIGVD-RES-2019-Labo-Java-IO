package ch.heigvd.res.labio.impl.filters;

import ch.heigvd.res.labio.impl.Utils;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

/**
 * This class transforms the streams of character sent to the decorated writer.
 * When filter encounters a line separator, it sends it to the decorated writer.
 * It then sends the line number and a tab character, before resuming the write
 * process.
 *
 * Hello\n\World -> 1\Hello\n2\tWorld
 *
 * @author Olivier Liechti
 */
public class FileNumberingFilterWriter extends FilterWriter {
  private int lineNumber = 0;
  private boolean writeNewLine = true;

  private static final Logger LOG = Logger.getLogger(FileNumberingFilterWriter.class.getName());

  public FileNumberingFilterWriter(Writer out) {
    super(out);
  }

  @Override
  public void write(String str, int off, int len) throws IOException {
    String[] lineSplit = Utils.getNextLine(str.substring(off, off + len));
    if (writeNewLine) {
      writeLineBeginning();
    }

    while (!lineSplit[0].equals("")) {
      String newLine = lineSplit[0] + getLineBeginning();
      super.write(newLine, 0, newLine.length());

      lineSplit = Utils.getNextLine(lineSplit[1]);
    }

    super.write(lineSplit[1], 0, lineSplit[1].length());
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    for (char c : cbuf) {
      write((int) c);
    }
  }

  @Override
  public void write(int c) throws IOException {
    if (writeNewLine && c != '\n') {
      writeLineBeginning();
    } else if (c == '\r' || c == '\n') {
      writeNewLine = true;
    }
    super.write(c);
  }

  private void writeLineBeginning() throws IOException {
    String newLine = getLineBeginning();
    super.write(newLine, 0, newLine.length());
    writeNewLine = false;
  }

  private String getLineBeginning() {
    return ++lineNumber + "\t";
  }

}
