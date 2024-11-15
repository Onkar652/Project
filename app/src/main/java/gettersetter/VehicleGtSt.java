package gettersetter;

public class VehicleGtSt {
    private String make;
    private String model;
    private String fuel;
    private String seatno;
    private boolean ac;
    private String milage;
    private String rate;
    private String yearmanufcture;

    double vlat;
    double vlong;

    public double getVlat() {
        return vlat;
    }

    public void setVlat(double vlat) {
        this.vlat = vlat;
    }

    public double getVlong() {
        return vlong;
    }

    public void setVlong(double vlong) {
        this.vlong = vlong;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getSeatno() {
        return seatno;
    }

    public void setSeatno(String seatno) {
        this.seatno = seatno;
    }

    public boolean isAc() {
        return ac;
    }

    public void setAc(boolean ac) {
        this.ac = ac;
    }

    public String getMilage() {
        return milage;
    }

    public void setMilage(String milage) {
        this.milage = milage;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getYearmanufcture() {
        return yearmanufcture;
    }

    public void setYearmanufcture(String yearmanufcture) {
        this.yearmanufcture = yearmanufcture;
    }
}
