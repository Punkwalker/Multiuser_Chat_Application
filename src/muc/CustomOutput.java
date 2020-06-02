package muc;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;
/**
 * @author Pankaj Walke
 * Name: Pankaj Walke
 *UTA ID: 1001781008
 *Lab 1 - Distributed Systems CSE5306-001 Sring2020 
 */

public class CustomOutput extends OutputStream
{
    private JTextArea textArea;

    public CustomOutput(JTextArea textArea) {
        this.textArea = textArea;
    }

    public void write(int b) throws IOException {
        // redirects data to the text area
        textArea.append(String.valueOf((char)b));
        // scrolls the text area to the end of data
        textArea.setCaretPosition(textArea.getDocument().getLength());
        // keeps the textArea up to date
        textArea.update(textArea.getGraphics());
    }
    
}
