package ubb.scs.map.service;

import ubb.scs.map.controller.GlobalService;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.validators.ValidationException;
import ubb.scs.map.utils.PasswordManager;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class Authenticator {


    public static Utilizator login(String username, String password){
        NetworkService network = GlobalService.getNetwork();
        if (username == null) {
            throw new ValidationException("Username must not be null");
        }
        if (password == null) {
            throw new ValidationException("Password must not be null");
        }

        StreamSupport
                .stream(network.getAllUtilizatori().spliterator(), false)
                .forEach(u -> System.out.println(u.getPassword()));

        String hashpass = PasswordManager.hashPassword(password);

        Optional<Utilizator> user = StreamSupport.stream(network.getAllUtilizatori().spliterator(), false)
                .filter(u -> Objects.equals(u.getUsername(), username) && Objects.equals(u.getPassword(), hashpass))
                .findFirst();


        return user.orElse(null);
    }

    public static Utilizator signUp(String username, String password, String confirmedPassword){
        NetworkService network = GlobalService.getNetwork();
        if (username == null) {
            throw new ValidationException("Username must not be null");
        }
        if (password == null) {
            throw new ValidationException("Password must not be null");
        }
        if (!Objects.equals(confirmedPassword, password)) {
            throw new ValidationException("Confirmed password is incorrect");
        }

        Optional<Utilizator> user = StreamSupport
                .stream(network.getAllUtilizatori().spliterator(), false)
                .filter(u -> Objects.equals(u.getUsername(), username))
                .findFirst();

        return user.orElse(null);

    }
}
