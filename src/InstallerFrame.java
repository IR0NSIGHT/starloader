import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class InstallerFrame extends JFrame {
    public static void main(String[] args) {
        new InstallerFrame();
    }

    public JTextArea makeText(Color c, int y, String text) {
        JTextArea area = new JTextArea("    " + text);
        area.setBounds(0, y, 400, 18);
        area.setBackground(c);
        area.setEditable(false);
        getContentPane().add(area);
        return area;
    }

    public JButton makeButton(int x, int y, String text, ActionListener listener) {
        JButton area = new JButton(text);
        area.setBounds(x, y, text.length() * 10, 20);
        area.addActionListener(listener);
        getContentPane().add(area);
        return area;
    }

    File selectedJar = null;

    public InstallerFrame() {
        setSize(400, 400);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setTitle("StarLoader installer");
        setLayout(null);
        JTextArea title = makeText(new Color(169, 169, 169), 5, "StarLoader Installer ");
        title.setSize(400, 26);
        title.setFont(title.getFont().deriveFont(18.0f));
        makeText(new Color(147, 147, 147), 40, "1) Select StarMade.jar:");
        JTextArea filePath = makeText(new Color(255, 0, 28), 110, "no jar selected............................. select one");
        JButton button = makeButton(20, 245, "Click to install", (a) -> {
            try {
                Installer.install(selectedJar);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to install! Check console for error");
            }
        });
        makeButton(20, 70, "Select JAR", (a) -> {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "StarMade.jar file", "jar");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                filePath.setText(selectedFile.getAbsolutePath());
                filePath.setBackground(new Color(12, 255, 0));
                button.setEnabled(true);
                selectedJar = selectedFile;
            }
        });
        makeText(new Color(147, 147, 147), 220, "2) Install Modloader: ");

        makeButton(20, 290, "Report a bug", (a) -> {
            JOptionPane.showMessageDialog(this, "    there are no bugs");
        });

        button.setEnabled(false);
        repaint();
    }
}