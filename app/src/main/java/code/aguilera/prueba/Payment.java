package code.aguilera.prueba;

/**
 * Created by s.aguilera on 14/11/2016.
 */

public class Payment {

    int ID;
    Float amount;
    String date;
    String description;
    Boolean state; //positivo o negativo

    public Payment() {
    }

    public Payment(int ID, Float amount, String date, String description, Boolean state) {
        this.ID = ID;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.state = state;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "ID=" + ID +
                ", amount=" + amount +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", state=" + state +
                '}';
    }
}
