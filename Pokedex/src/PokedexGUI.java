import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class PokedexGUI {
    private Search search;
    private JFrame frame;
    private JTable table;
    private JLabel imageLabel;
    private JButton searchButton;
    private JTextField searchField;
    private Thread soundThread;  

    public PokedexGUI() {
        search = new Search();
        createGUI();
    }

    private void createGUI() {
        frame = new JFrame("Pokédex Search");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 700);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.WHITE);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        topPanel.add(new JLabel("Enter Pokémon Name or ID"));
        topPanel.add(searchField);
        topPanel.add(searchButton);

        JPanel screenPanel = new JPanel();
        screenPanel.setBackground(new Color(173, 216, 230));
        screenPanel.setBorder(new LineBorder(Color.BLACK, 3));
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(200, 200));
        screenPanel.add(imageLabel);

        table = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(580, 150));

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(screenPanel, BorderLayout.CENTER);
        frame.add(tableScrollPane, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> performSearch());

        frame.setVisible(true);
    }

    private void performSearch() {
        String input = searchField.getText().trim();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter a Pokémon name or ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(input);
            Pokedex result = search.searchByID(id);
            if (result != null) {
                displayResults(List.of(result));
                displayImage(id);
                playSound(id);
            } else {
                JOptionPane.showMessageDialog(frame, "No Pokémon found with ID: " + id, "Not Found", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            List<Pokedex> nameResults = search.searchByName(input);
            List<Pokedex> typeResults = search.searchByType(input);

            if (!nameResults.isEmpty()) {
                displayResults(nameResults);
                displayImage(nameResults.get(0).getID());
                playSound(nameResults.get(0).getID());
            } else if (!typeResults.isEmpty()) {
                displayResults(typeResults);
                displayImage(typeResults.get(0).getID());
                playSound(typeResults.get(0).getID());
            } else {
                JOptionPane.showMessageDialog(frame, "No Pokémon found with name or type: " + input, "Not Found", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void displayResults(List<Pokedex> results) {
        String[] columnNames = {"ID", "Name", "Type1", "Type2", "HP", "ATK", "DEF", "SPD"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Pokedex p : results) {
            model.addRow(new Object[]{
                    p.getID(), p.getName(), p.getType1(), p.getType2(),
                    p.getHp(), p.getAtk(), p.getDef(), p.getSpeed()
            });
        }

        table.setModel(model);
    }

    private void displayImage(int id) {
        String imagePath = "src/151/" + id + ".png";
        File imageFile = new File(imagePath);

        if (imageFile.exists()) {
            ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
            Image scaledImage = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
        } else {
            displayPlaceholderImage();
        }
    }

    private void displayPlaceholderImage() {
        imageLabel.setIcon(new ImageIcon(new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB)));
    }

    private void playSound(int id) {
        String soundPath = "src/sounds/" + id + ".mp3";
        File soundFile = new File(soundPath);

        if (soundFile.exists()) {
            stopPreviousSound();

            soundThread = new Thread(() -> {
                try (FileInputStream fileInputStream = new FileInputStream(soundFile)) {
                    Player player = new Player(fileInputStream);
                    player.play();
                } catch (IOException | JavaLayerException e) {
                    System.out.println("Error playing sound: " + e.getMessage());
                }
            });

            soundThread.start();
        } else {
            System.out.println("Sound file not found: " + soundPath);
        }
    }

    private void stopPreviousSound() {
        if (soundThread != null && soundThread.isAlive()) {
            soundThread.interrupt();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PokedexGUI::new);
    }
}


