package ubb.scs.map.domain.dto;

import java.util.Optional;

public class UserFilterDTO {
    private Optional<String> username = Optional.empty();

    public Optional<String> getUsername() {
        return username;
    }

    public void setUsername(Optional<String> username) {
        this.username = username;
    }
}
