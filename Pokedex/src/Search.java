import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Search {
    private List<Pokedex> pokedexList;

    public Search() {
        pokedexList = new ArrayList<>();
        loadCSV("src/Kanto.csv"); 
    }

    private void loadCSV(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            br.readLine(); 
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",\\s*"); 
                if (data.length < 13) {
                    System.out.println("Skipping incomplete row: " + Arrays.toString(data));
                    continue; 
                }

                try {
                    Pokedex pokemon = new Pokedex(
                        Integer.parseInt(data[0]), data[1], data[2], data[3], data[4],
                        Integer.parseInt(data[5]), Integer.parseInt(data[6]), Integer.parseInt(data[7]),
                        Integer.parseInt(data[8]), Integer.parseInt(data[9]), Integer.parseInt(data[10]),
                        Integer.parseInt(data[11]), Integer.parseInt(data[12])
                    );
                    pokedexList.add(pokemon);
                } catch (NumberFormatException e) {
                    System.out.println("Skipping row due to parsing error: " + Arrays.toString(data));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading CSV: " + e.getMessage());
        }
    }

    public Pokedex searchByID(int id) {
        return pokedexList.stream().filter(p -> p.getID() == id).findFirst().orElse(null);
    }

    public List<Pokedex> searchByName(String name) {
        return pokedexList.stream()
                .filter(p -> p.getName().trim().equalsIgnoreCase(name.trim()))
                .collect(Collectors.toList());
    }

    public List<Pokedex> searchByType(String type) {
        return pokedexList.stream()
                .filter(p -> p.getType1().trim().equalsIgnoreCase(type.trim()) || p.getType2().trim().equalsIgnoreCase(type.trim()))
                .collect(Collectors.toList());
    }

    public void printResults(List<Pokedex> results) {
        if (results.isEmpty()) {
            System.out.println("No results found.");
            return;
        }

        
        System.out.println("\n==============================================================");
        System.out.printf("| %-3s | %-12s | %-8s | %-8s | %-8s | %-3s | %-3s | %-3s | %-3s | %-3s | %-3s | %-3s | %-3s |\n",
                "ID", "Name", "Form", "Type1", "Type2", "HP", "ATK", "DEF", "SpATK", "SpDEF", "SPD", "GEN", "Total");
        System.out.println("==============================================================");

       
        for (Pokedex p : results) {
            System.out.printf("| %-3d | %-12s | %-8s | %-8s | %-8s | %-3d | %-3d | %-3d | %-3d | %-3d | %-3d | %-3d | %-3d |\n",
                    p.getID(), p.getName(), p.getForm(), p.getType1(), p.getType2(),
                    p.getHp(), p.getAtk(), p.getDef(), p.getSpatk(), p.getSpdef(), p.getSpeed(),
                    p.getGeneration(), p.getTotalstat());
        }
        System.out.println("==============================================================\n");
    }

    public static void main(String[] args) {
        Search search = new Search();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Pokedex search system!");
        System.out.println("Type 'exit' at any time to quit.");

        while (true) {
            System.out.print("\nEnter Pokémon Name, ID, or Type: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Goodbye!");
                break;
            }

            try {
                int id = Integer.parseInt(input);
                Pokedex result = search.searchByID(id);
                if (result != null) {
                    search.printResults(Collections.singletonList(result));
                } else {
                    System.out.println("No Pokémon found with ID: " + id);
                }
            } catch (NumberFormatException e) {
                List<Pokedex> nameResults = search.searchByName(input);
                List<Pokedex> typeResults = search.searchByType(input);

                if (!nameResults.isEmpty()) {
                    search.printResults(nameResults);
                } else if (!typeResults.isEmpty()) {
                    search.printResults(typeResults);
                } else {
                    System.out.println("No Pokémon found with name or type: " + input);
                }
            }
        }

        scanner.close();
    }
}

