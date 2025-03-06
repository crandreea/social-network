package ubb.scs.map.domain;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

public class Notification extends Entity<Long>{
    private final String description;
    private LocalDateTime date;
    private final Long userId;

    public Notification(Long id, String description, Long userId) {
        super.setId(id);
        this.description = description;
        this.date = LocalDateTime.now();
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Long getUserId() {
        return userId;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "@NOTIFICATION | " +
                "ID <" + getId() + ">" +
                "\n              | DESCRIPTION <" + getDescription() + ">" +
                "\n              | DATE <" + getDate() + ">" +
                "\n              | USER ID <" + getUserId() + ">";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Notification that = (Notification) o;
        return Objects.equals(description, that.description) && Objects.equals(date, that.date) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), description, date, userId);
    }

}
