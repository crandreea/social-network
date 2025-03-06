package ubb.scs.map.domain;

import java.util.*;

public class Utilizator extends Entity<Long> {
    private final String username;
    private final String password;

    public Utilizator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {

        return username;
    }

    public String getPassword() {

        return password;
    }

    @Override
    public String toString() {
        return "Utilizator{" + "username='" + username + '\'' + ", password='" + password + '\n';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilizator that)) return false;
        return getUsername().equals(that.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword());
    }
}