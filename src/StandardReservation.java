public class StandardReservation extends Reservation {
    private static final double PRICE_PER_NIGHT = 50.0;

    public StandardReservation(String id, String clientName, int nights) {
        super(id, clientName, nights);
    }

    @Override
    public double calculatePrice() {
        return getNights() * PRICE_PER_NIGHT;
    }

    @Override
    public String getType() {
        return "Standard";
    }

    @Override
    public String exportData() {
        return getType() + "\n" + getId() + "\n" + getClientName() + "\n" + getNights();
    }
}