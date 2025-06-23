public class PremiumReservation extends Reservation {
    private static final double PRICE_PER_NIGHT = 100.0;
    private boolean includesBreakfast;

    public PremiumReservation(String id, String clientName, int nights, boolean includesBreakfast) {
        super(id, clientName, nights);
        this.includesBreakfast = includesBreakfast;
    }

    public boolean isIncludesBreakfast() {
        return includesBreakfast;
    }

    public void setIncludesBreakfast(boolean includesBreakfast) {
        this.includesBreakfast = includesBreakfast;
    }

    @Override
    public double calculatePrice() {
        double base = getNights() * PRICE_PER_NIGHT;
        return includesBreakfast ? base + (10 * getNights()) : base;
    }

    @Override
    public String getType() {
        return "Premium";
    }

    @Override
    public String getSummary() {
        return super.getSummary() + " [Premium, Breakfast: " + (includesBreakfast ? "Yes" : "No") + "]";
    }

    @Override
    public String exportData() {
        return getType() + "\n" + getId() + "\n" + getClientName() + "\n" + getNights() + "\n" + includesBreakfast;
    }
}