package ubb.scs.map.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Message extends Entity<Long>{
    private Long fromId;
    private Long toId;
    private String message;
    private LocalDateTime date;

    public Message(Long fromId, Long toId, String message) {
        this.fromId = fromId;
        this.toId = toId;
        this.message = message;
        this.date = LocalDateTime.now();
    }

    public Long getFromId() {
        return fromId;
    }


    public Long getToId() {
        return toId;
    }


    public String getMessage() {
        return message;
    }


    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Message message1 = (Message) o;
        return Objects.equals(fromId, message1.fromId) && Objects.equals(toId, message1.toId) && Objects.equals(message, message1.message) && Objects.equals(date, message1.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fromId, toId, message, date);
    }
}
