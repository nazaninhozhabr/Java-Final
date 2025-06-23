
import java.io.*;
import java.util.*;

public class ReservationManager {

    private Map<String, Reservation> reservations = new HashMap<>();
    private int reservationCounter = 1;
    private static final String FOLDER_PATH = "reservations";

    public ReservationManager() {
        File folder = new File(FOLDER_PATH);
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    public void loadReservationsFromFiles() {
        File folder = new File(FOLDER_PATH);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files == null) return;

        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String type = reader.readLine();
                String id = reader.readLine();
                String name = reader.readLine();
                int nights = Integer.parseInt(reader.readLine());
                Reservation r = null;
                if ("Standard".equalsIgnoreCase(type)) {
                    r = new StandardReservation(id, name, nights);
                } else if ("Premium".equalsIgnoreCase(type)) {
                    boolean breakfast = Boolean.parseBoolean(reader.readLine());
                    r = new PremiumReservation(id, name, nights, breakfast);
                }
                if (r != null) {
                    reservations.put(id, r);
                    reservationCounter = Math.max(reservationCounter, Integer.parseInt(id.replace("RES", "")) + 1);
                }
            } catch (Exception e) {
                System.out.println("Error loading reservation from file: " + file.getName());
            }
        }
    }

    public void createReservation(Scanner scanner) {
        System.out.println("Reservation type (1: Standard, 2: Premium):");
        String type = scanner.nextLine();

        System.out.println("Client name:");
        String name = scanner.nextLine();

        System.out.println("Number of nights:");
        int nights = Integer.parseInt(scanner.nextLine());

        String id = "RES" + reservationCounter++;
        Reservation reservation = null;

        if (type.equals("1")) {
            reservation = new StandardReservation(id, name, nights);
        } else if (type.equals("2")) {
            System.out.println("Include breakfast? (y/n):");
            boolean breakfast = scanner.nextLine().equalsIgnoreCase("y");
            reservation = new PremiumReservation(id, name, nights, breakfast);
        } else {
            System.out.println("Invalid reservation type.");
            return;
        }

        reservations.put(id, reservation);
        System.out.println("Reservation created with ID: " + id);

        System.out.println("Do you want to export this reservation to a file? (y/n):");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            exportToFile(reservation, scanner);
        }
    }

    public void deleteReservation(Scanner scanner) {
        System.out.println("Enter reservation ID to delete:");
        String id = scanner.nextLine();
        if (reservations.remove(id) != null) {
            System.out.println("Reservation deleted.");
        } else {
            System.out.println("Reservation not found.");
        }
    }

    public void modifyReservation(Scanner scanner) {
        System.out.println("Enter reservation ID to modify:");
        String id = scanner.nextLine();
        Reservation r = reservations.get(id);
        if (r == null) {
            System.out.println("Reservation not found.");
            return;
        }

        boolean editing = true;
        while (editing) {
            System.out.println("\nCurrent Reservation:");
            System.out.println(r.getSummary());
            System.out.println("Modify:");
            System.out.println("1. Client name");
            System.out.println("2. Nights");
            if (r instanceof PremiumReservation) {
                System.out.println("3. Includes breakfast");
                System.out.println("4. Export to file");
                System.out.println("5. Exit");
            } else {
                System.out.println("3. Export to file");
                System.out.println("4. Exit");
            }

            String opt = scanner.nextLine();
            switch (opt) {
                case "1":
                    System.out.println("New client name:");
                    r.setClientName(scanner.nextLine());
                    break;
                case "2":
                    System.out.println("New number of nights:");
                    r.setNights(Integer.parseInt(scanner.nextLine()));
                    break;
                case "3":
                    if (r instanceof PremiumReservation) {
                        System.out.println("Include breakfast? (y/n):");
                        ((PremiumReservation) r).setIncludesBreakfast(scanner.nextLine().equalsIgnoreCase("y"));
                    } else {
                        exportToFile(r, scanner);
                    }
                    break;
                case "4":
                    if (r instanceof PremiumReservation) {
                        exportToFile(r, scanner);
                    } else {
                        editing = false;
                    }
                    break;
                case "5":
                    editing = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    public void viewReservation(Scanner scanner) {
        System.out.println("Enter reservation ID:");
        String id = scanner.nextLine();
        Reservation r = reservations.get(id);
        if (r != null) {
            System.out.println("Reservation summary:");
            System.out.println(r.getSummary());
            System.out.println("Total price: $" + r.calculatePrice());
        } else {
            System.out.println("Reservation not found.");
        }
    }

    public void importReservation(Scanner scanner) {
        System.out.println("Enter file name to import (without .txt):");
        String fileName = scanner.nextLine();
        File file = new File(FOLDER_PATH + "/" + fileName + ".txt");
        if (!file.exists()) {
            System.out.println("File not found.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String type = reader.readLine();
            String id = reader.readLine();
            String name = reader.readLine();
            int nights = Integer.parseInt(reader.readLine());

            Reservation r = null;
            if ("Standard".equalsIgnoreCase(type)) {
                r = new StandardReservation(id, name, nights);
            } else if ("Premium".equalsIgnoreCase(type)) {
                boolean breakfast = Boolean.parseBoolean(reader.readLine());
                r = new PremiumReservation(id, name, nights, breakfast);
            }

            if (r != null) {
                if (reservations.containsKey(id)) {
                    System.out.println("Reservation already exists. Overwrite? (y/n):");
                    if (!scanner.nextLine().equalsIgnoreCase("y")) return;
                }
                reservations.put(id, r);
                System.out.println("Reservation imported successfully.");
            }
        } catch (Exception e) {
            System.out.println("Error reading the reservation file.");
        }
    }

    private void exportToFile(Reservation reservation, Scanner scanner) {
        String fileName = reservation.getClientName().replaceAll("\\s+", "_");
        File file = new File(FOLDER_PATH + "/" + fileName + ".txt");

        if (file.exists()) {
            System.out.println("File already exists. Overwrite? (y/n):");
            if (!scanner.nextLine().equalsIgnoreCase("y")) return;
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(reservation.exportData());
            System.out.println("Reservation exported to: " + file.getPath());
        } catch (IOException e) {
            System.out.println("Error writing file.");
        }
    }

    public void viewReservationFile(Scanner scanner) {
        System.out.println("Enter file name to view (without .txt):");
        String fileName = scanner.nextLine();
        File file = new File(FOLDER_PATH + "/" + fileName + ".txt");

        if (!file.exists()) {
            System.out.println("File not found.");
            return;
        }

        System.out.println("\n--- Contents of " + file.getName() + " ---");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading the file.");
        }
    }

}