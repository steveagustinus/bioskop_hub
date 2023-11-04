package src.model.seat;

public class Seat implements SeatStatusInterface{
    private String idSeat;
    private int seatStatus;

    public Seat(String idSeat, int seatStatus) {
        this.idSeat = idSeat;
        this.seatStatus = seatStatus;
    }
    
    public String getIdSeat() {
        return idSeat;
    }

    public void setIdSeat(String idSeat) {
        this.idSeat = idSeat;
    }

    public int getSeatStatus() {
        return seatStatus;
    }

    public void setSeatStatus(int seatStatus) {
        this.seatStatus = seatStatus;
    }
}
