import java.util.Scanner;

public class HotelSystem {

    private ReservationManager manager = new ReservationManager();
    private Scanner scanner = new Scanner(System.in);

    public void start() {
        manager.loadReservationsFromFiles();
        System.out.println("Welcome to the Hotel Paradise Reservation System!");
        System.out.println("Address: Fictitious Street 123 - Phone: 555-HOTEL\n");

        boolean running = true;
        while (running) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Create new reservation");
            System.out.println("2. Delete reservation");
            System.out.println("3. Modify reservation");
            System.out.println("4. View reservation");
            System.out.println("5. Import reservation from file");
            System.out.println("6. View reservation file");
            System.out.println("7. Exit");

            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    manager.createReservation(scanner);
                    break;
                case "2":
                    manager.deleteReservation(scanner);
                    break;
                case "3":
                    manager.modifyReservation(scanner);
                    break;
                case "4":
                    manager.viewReservation(scanner);
                    break;
                case "5":
                    manager.importReservation(scanner);
                    break;
                case "6":
                    manager.viewReservationFile(scanner);
                    break;
                case "7":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }

        System.out.println("Thank you for using the system. Goodbye!");
    }

    public static void main(String[] args) {
        new HotelSystem().start();
    }
}
