import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.print.*;

/** An example of a printable window in Java 1.2. The key point
 *  here is that <B>any</B> component is printable in Java 1.2.
 *  However, you have to be careful to turn off double buffering
 *  globally (not just for the top-level window).
 *  See the PrintUtilities class for the printComponent method
 *  that lets you print an arbitrary component with a single
 *  function call.
 *  7/99 Marty Hall, http://www.apl.jhu.edu/~hall/java/
 */

public class PrintExample extends JFrame
                          implements ActionListener {
  public static void main(String[] args) {
    new PrintExample();
  }

  public PrintExample() {
    super("Printing Swing Components");
    WindowUtilities.setNativeLookAndFeel();
    addWindowListener(new ExitListener());
    Container content = getContentPane();
    JButton printButton = new JButton("Print");
    printButton.addActionListener(this);
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground(Color.white);
    buttonPanel.add(printButton);
    content.add(buttonPanel, BorderLayout.SOUTH);
    DrawingPanel drawingPanel = new DrawingPanel();
    content.add(drawingPanel, BorderLayout.CENTER);
    pack();
    setVisible(true);
  }

  public void actionPerformed(ActionEvent event) {
    PrintUtilities.printComponent(this);
  }
}
    
