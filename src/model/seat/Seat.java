package src.model.seat;

public class Seat implements SeatStatusInterface {
    private String idSeat;
    private String seatCode;
    private int seatStatus;

    public Seat(String idSeat, String seatCode, int seatStatus) {
        this.idSeat = idSeat;
        this.seatCode = seatCode;
        this.seatStatus = seatStatus;
    }
    
    public String getIdSeat() {
        return idSeat;
    }

    public void setIdSeat(String idSeat) {
        this.idSeat = idSeat;
    }

    public String getSeatCode() {
        return seatCode;
    }

    public void setSeatCode(String seatCode) {
        this.seatCode = seatCode;
    }

    public int getSeatStatus() {
        return seatStatus;
    }

    public void setSeatStatus(int seatStatus) {
        this.seatStatus = seatStatus;
    }
}
