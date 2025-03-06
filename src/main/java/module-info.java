module ubb.scs.map {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.dotenv;
    requires java.desktop;
    requires org.postgresql.jdbc;


    //opens com.example.demo to javafx.fxml;
    exports ubb.scs.map;
    //exports ubb.scs.map;
    opens ubb.scs.map to javafx.fxml;
    exports ubb.scs.map.controller;
    opens ubb.scs.map.controller to javafx.fxml;
}