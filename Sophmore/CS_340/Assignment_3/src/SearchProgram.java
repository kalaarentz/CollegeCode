import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.io.*;
import java.util.LinkedList;

/**
 * A class which creates a GUI and implements methods to open source-files,
 * create prefix-trees from them, display search results, and save them to an
 * output text-file.
 *
 * @author J. Hursey, M. Allen
 */

public class SearchProgram implements ActionListener {

    private JFrame window;

    private JButton loadBtn, findShortBtn, findTallBtn, saveShortBtn, saveTallBtn;

    private TextArea textAreaShort, textAreaTall;

    private Label fileField;
    private JFileChooser chooser;
    private JTextField inputField;
    private File inFile;

    // graphical constants
    private final int windowWidth = 650;
    private final int windowHeight = 650;

    // Current PrefixTree
    private PrefixTree treeShort, treeTall;

    /**
     * Simple initiating main().
     *
     * @param args Not used.
     */
    public static void main(String[] args) {
        final SearchProgram search = new SearchProgram();
        search.createWindow();
    }

    /*
     * post: creates window with text fields for file-name and text,
     * and action buttons to handle the text input
     */
    private void createWindow() {
        int mid = windowWidth / 2;
        mid -= 5;

        // set up the basic window
        window = new JFrame("Prefix Tree");
        window.setBounds(10, 10, windowWidth, windowHeight);
        window.setVisible(true);
        window.setResizable(false);
        window.setLayout(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(Color.lightGray);

        // add label and field for FileName
        Label fLabel = new Label(" Filename: ");
        fLabel.setBackground(Color.lightGray);
        fLabel.setBounds(125, 10, 75, 20);
        window.add(fLabel, 0);
        fileField = new Label("<NONE SELECTED>   ");
        fileField.setBackground(Color.lightGray);
        fileField.setBounds(205, 10, 250, 20);
        window.add(fileField, 0);


        // chooser for files
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        chooser.setFileFilter(filter);


        // label and text-field for input string
        JLabel pLabel = new JLabel("Search for Prefix");
        pLabel.setBackground(Color.lightGray);
        pLabel.setBounds(135, 37, 380, 20);
        pLabel.setHorizontalAlignment( JLabel.CENTER );
        window.add(pLabel, 0);

        inputField = new JTextField();
        inputField.setBounds(135, 60, 380, 20);
        inputField.addActionListener(this);
        window.add(inputField, 0);


        // create and add buttons
        loadBtn = new JButton("Load File");
        loadBtn.setBounds(5, 5, 120, 30);
        loadBtn.addActionListener(this);
        window.add(loadBtn, 0);

        findTallBtn = new JButton("Find Tall");
        findTallBtn.setBounds(5, 30, 120, 30);
        findTallBtn.addActionListener(this);
        window.add(findTallBtn, 0);

        saveTallBtn = new JButton("Save Tall");
        saveTallBtn.setBounds(5, 55, 120, 30);
        saveTallBtn.addActionListener(this);
        window.add(saveTallBtn, 0);

        findShortBtn = new JButton("Find Short");
        findShortBtn.setBounds( windowWidth-125, 30, 120, 30);
        findShortBtn.addActionListener(this);
        window.add(findShortBtn, 0);

        saveShortBtn = new JButton("Save Short");
        saveShortBtn.setBounds( windowWidth-125, 55, 120, 30);
        saveShortBtn.addActionListener(this);
        window.add(saveShortBtn, 0);


        // add text display area
        JLabel label = new JLabel("Tall Prefix Tree");
        label.setHorizontalAlignment( JLabel.CENTER );
        label.setBounds(5, 80, mid-5, 20);
        window.add(label);

        label = new JLabel("Short Prefix Tree");
        label.setHorizontalAlignment( JLabel.CENTER );
        label.setBounds(5+mid, 80, mid-5, 20);
        window.add(label);

        textAreaTall = new TextArea();
        textAreaTall.setEditable(false);
        textAreaTall.setBounds(5, 100, mid-5, windowHeight - 120);
        window.add(textAreaTall, 0);

        textAreaShort = new TextArea();
        textAreaShort.setEditable(false);
        textAreaShort.setBounds(5 + mid, 100, mid-5, windowHeight - 120);
        window.add(textAreaShort, 0);

        window.repaint();
    }

    /*
     * Post: If user chooses a valid file, then file field is set to name of
     * file, all the text fields are cleared, a new PrefixSearcher object is
     * created, and it builds a prefix-tree with the chosen input file.
     */
    private void openFile() {
        int returnVal = chooser.showOpenDialog(window);
        if ( returnVal == JFileChooser.APPROVE_OPTION ) {
            inFile = chooser.getSelectedFile();
            fileField.setText(inFile.getName());

            textAreaShort.setText("");
            textAreaTall.setText("");

            inputField.setText("");

            // Build the Prefix tree
            buildPrefixTree( inFile );
        }
    }

    /**
     * Build the prefix tree from the file specified
     * File must contain one word per line of the file.
     * 
     * @param filename Filename to read words from
     */
    private void buildPrefixTree( File filename ) {
        treeTall  = new TallPrefixTree();
        treeShort = new ShortPrefixTree();

        try {
            FileReader fin = new FileReader( filename );
            BufferedReader reader = new BufferedReader( fin );

            String line = reader.readLine();
            while ( line != null ) {
                treeTall.insert( line.toUpperCase() );
                treeShort.insert( line.toUpperCase() );
                line = reader.readLine();
            }

            reader.close();
        }
        catch ( IOException e ) {
            System.err.println( "File load failed: " + e );
        }
    }

    /**
     * Saves the current window contents to the output file.
     * Filenamed "out_TYPE_PREFIX.EXT" where
     *   TYPE: tall, short
     *   PREFIX: Prefix typed by the user
     *   EXT: txt, dot
     *   
     * @param isTall If we are focusing on the tall tree
     */
    private void saveFile(boolean isTall) {        
        String prefix = inputField.getText();
        String baseFilename = "out_";
        if( isTall ) {
            baseFilename += "tall_";
        }
        else {
            baseFilename += "short_";            
        }
        baseFilename += inputField.getText();
        
        try {
            // Write out the contexts of the text area
            String outFileName = baseFilename + ".txt";
            PrintWriter writer = new PrintWriter( new BufferedWriter( new FileWriter(outFileName)));
            if( isTall ) {
                writer.print( textAreaTall.getText() );
            }
            else {
                writer.print( textAreaShort.getText() );
            }
            writer.close();

            // Write out the DOT file as well
            if( isTall ) {
                treeTall.writeDotFile( baseFilename + ".dot", prefix );
            }
            else {
                treeShort.writeDotFile( baseFilename + ".dot", prefix );
            }
        } catch (IOException e) {
            System.err.println("File write failed: " + e);
        }
    }

    /**
     * Search the tree and update the text field
     * 
     * @param isTall If we are focusing on the tall tree
     * @param value Value to search for
     */
    private void searchTree(boolean isTall, String value) {
        LinkedList<String> matches;
        if( isTall ) {
            matches = treeTall.getMatches( value );
            textAreaTall.setText("");
            for(String word : matches ) {
                textAreaTall.append(word);
                textAreaTall.append("\n");
            }
        }
        else {
            matches = treeShort.getMatches( value );
            textAreaShort.setText("");
            for(String word : matches ) {
                textAreaShort.append(word);
                textAreaShort.append("\n");
            }
        }
        window.repaint();
    }

    /**
     * Handles events; required by ActionListener interface.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if ( e.getSource() == loadBtn ) {
            openFile();
        }
        else if( e.getSource() == inputField && inFile != null ) {
            String input = inputField.getText().toUpperCase();
            searchTree(false, input);
            searchTree(true, input);
        }           
        else if( e.getSource() == findTallBtn && inFile != null ) {
            String input = inputField.getText().toUpperCase();
            searchTree(true, input);
            //System.out.println("Tall : " + treeTall.toPrefixString() );
        }
        else if ( e.getSource() == saveTallBtn ) {
            if( !textAreaTall.getText().isEmpty()  ) {
                saveFile(true);
            }
        }
        else if( e.getSource() == findShortBtn && inFile != null ) {
            String input = inputField.getText().toUpperCase();
            searchTree(false, input);
            //System.out.println("Short: " + treeShort.toPrefixString() );
        }
        else if ( e.getSource() == saveShortBtn ) {
            if( !textAreaShort.getText().isEmpty()  ) {
                saveFile(false);
            }
        }
    }
}