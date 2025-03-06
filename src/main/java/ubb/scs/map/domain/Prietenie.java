package ubb.scs.map.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Prietenie extends Entity<Tuplu<Long, Long>> {
    private LocalDate date;
    private final Long idUser1;
    private final Long idUser2;
    private String status;

    public Prietenie(Long idUser1, Long idUser2) {
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
        this.status = "";
        this.date = LocalDate.now();
        this.setId(new Tuplu<>(idUser1, idUser2));
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String statusNou) {
        this.status = statusNou;
    }
    public Long getIdUser2() {

        return idUser2;
    }

    public Long getIdUser1() {

        return idUser1;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean containsUser(Long userId) {
        return userId.equals(this.getIdUser1()) || userId.equals(this.getIdUser2());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prietenie prietenie = (Prietenie) o;
        return Objects.equals(date, prietenie.date)
        &&
                (Objects.equals(idUser1, prietenie.idUser1) && Objects.equals(idUser2, prietenie.idUser2))||
               (Objects.equals(idUser2, prietenie.idUser1) && Objects.equals(idUser1, prietenie.idUser2));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(date);
    }

    @Override
    public String toString() {
        return "Prietenie{" +
                "idUser1=" + idUser1 +
                ", idUser2=" + idUser2 +
                '}';
    }
}
