package ubb.scs.map.ui;

import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.service.CommunityService;
import ubb.scs.map.service.NetworkService;

import java.util.*;
import java.util.stream.StreamSupport;

public class Console {
    private final CommunityService communityService;
    private final Scanner scanner;
    private final NetworkService networkService;

    public Console(CommunityService communityService, NetworkService networkService, Scanner scanner) {
        this.communityService = communityService;
        this.scanner = scanner;
        this.networkService = networkService;
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("> ");
            int input = scanner.nextInt();
            switch (input) {
                case 0:
                    running = false;
                    break;
                case 1:
                    addUtilizator();
                    break;
                case 2:
                    deleteUtilizator();
                    break;
                case 3:
                    printUsers();
                    break;
                case 4:
                    addPrieten();
                    break;
                case 5:
                    deletePrieten();
                    break;
                case 6:
                    printFriendships();
                    break;
                case 7:
                    showCommunities();
                    break;
                case 8:
                    showLargestCommunity();
                    break;
                default:
                    System.out.println("Invalid input! Choose again!");
            }

        }
    }

    private void printMenu() {
        System.out.println("\n1. Adaugă utilizator");
        System.out.println("2. Șterge utilizator");
        System.out.println("3. Afiseaza utilizatori");
        System.out.println("4. Adaugă prietenie");
        System.out.println("5. Șterge prietenie");
        System.out.println("6. Afiseaza prietenie");
        System.out.println("7. Afișează numărul de comunități");
        System.out.println("8. Afișează cea mai sociabilă comunitate");
        System.out.println("0. Exit");
    }

    private void addUtilizator() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Prenume: ");
        String prenume = scanner.nextLine();
        System.out.println("Nume: ");
        String nume = scanner.nextLine();
        try {
            Utilizator utilizator = new Utilizator(prenume, nume);
            networkService.addUtilizator(utilizator);
            System.out.println("Utilizator adăugat cu succes!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void deleteUtilizator() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("ID: ");
        Long id = Long.valueOf(scanner.nextLine());
        try {
            if(networkService.findUser(id).isPresent()) {
                networkService.deleteUtilizator(id);
                System.out.println("Utilizator sters cu succesc!");
            }
            else{
                System.out.println("Utilizatorul nu exista!");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void addPrieten() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("ID primului utilizator: ");
        Long id1 = Long.valueOf(scanner.nextLine());
        System.out.println("ID al doilea utilizator: ");
        Long id2 = Long.valueOf(scanner.nextLine());
        try {
            networkService.addPrietenie(id1, id2);
            System.out.println("Priete adaugata cu succes!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void deletePrieten() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("ID primului utilizator: ");
        Long id1 = Long.valueOf(scanner.nextLine());
        System.out.println("ID al doilea utilizator: ");
        Long id2 = Long.valueOf(scanner.nextLine());
        try {
            if(networkService.findUser(id1).isPresent() && networkService.findUser(id2).isPresent()) {
                networkService.deletePrietenie(id1, id2);
                System.out.println("Prietenie stearsa cu succes!");
            }
            else {
                System.out.println("Prietenia nu exista!");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    private void showCommunities() {
        if (communityService.connectedCommunities() == 0)
            System.out.println("Nu exista comunitati!");
        else {
            System.out.println("Numărul de comunități: " + communityService.connectedCommunities());
        }
    }

    private void showLargestCommunity() {
        if (communityService.connectedCommunities() == 0)
            System.out.println("Nu exista comunitati!");
        else{
            System.out.println("Largest community: " + communityService.mostSocialCommunity());
        }
    }

    private void printUsers() {
        System.out.println("\t\t\tUSERS\t\t\t");
        StreamSupport.stream(networkService.getAllUtilizatori().spliterator(), false)
                .forEach(u -> System.out.println(u.getId() + ":  " + u.getUsername() + " " + u.getPassword()));
    }

    private void printFriendships() {
        System.out.println("\t\t\tPRIETENI\t\t\t");
        StreamSupport.stream(networkService.getAllPrietenii().spliterator(), false)
                .forEach(p -> System.out.println(p.getIdUser1() + " - " + p.getIdUser2()));
    }

}
