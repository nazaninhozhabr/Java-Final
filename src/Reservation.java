public abstract class Reservation {
    private String id;
    private String clientName;
    private int nights;

    public Reservation(String id, String clientName, int nights) {
        this.id = id;
        this.clientName = clientName;
        this.nights = nights;
    }

    public String getId() {
        return id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public int getNights() {
        return nights;
    }

    public void setNights(int nights) {
        this.nights = nights;
    }

    public abstract double calculatePrice();
    public abstract String getType();
    public abstract String exportData();

    public String getSummary() {
        return "Reservation ID: " + id + ", Client: " + clientName + ", Nights: " + nights;
    }
}