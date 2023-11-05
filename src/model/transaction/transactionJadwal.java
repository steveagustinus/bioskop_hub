package src.model.transaction;

import java.time.LocalDateTime;

public class transactionJadwal extends transaction{
    private String idJadwal;
    private String idSeat;

    public transactionJadwal(String idTransaction, LocalDateTime transactionDate, String idUser, String idJadwal,
            String idSeat) {
        super(idTransaction, transactionDate, idUser);
        this.idJadwal = idJadwal;
        this.idSeat = idSeat;
    }

    public String getIdJadwal() {
        return idJadwal;
    }

    public void setIdJadwal(String idJadwal) {
        this.idJadwal = idJadwal;
    }

    public String getIdSeat() {
        return idSeat;
    }

    public void setIdSeat(String idSeat) {
        this.idSeat = idSeat;
    }
}
