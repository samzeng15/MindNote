package MindNote;

import javax.swing.*;
import java.awt.event.*;
import java.util.*;

import filereader.*;
import graph.*;

public class MindNote {
    public static void main(String[] args) {
        JFrame f=new JFrame();
        JButton b=new JButton("import notes");//creating instance of JButton
        b.setBounds(80,100,200, 40);//x axis, y axis, width, height
        b.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //your actions
                System.out.println("button pressed");
                final JFileChooser fc = new JFileChooser();
                int r = fc.showOpenDialog(null);
                // if the user selects a file
                if (r == JFileChooser.APPROVE_OPTION) {
                    System.out.println(fc.getSelectedFile().getAbsolutePath());
                }
                // if the user cancelled the operation
                else {
                    System.out.println("camcl");
                }

                try {
                    // Open an input stream
                    filereader.FileReader.readDocxFile(fc.getSelectedFile().getAbsolutePath());
                }
                catch (Exception error){
                    System.out.println("Error");
                }
            }
        });

        f.add(b);//adding button in JFrame

        f.setSize(400,500);//400 width and 500 height
        f.setLayout(null);//using no layout managers
        f.setVisible(true);//making the frame visible
    }
}