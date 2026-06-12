module org.barbertrack.barbertrack_poo {

    requires javafx.controls;
    requires javafx.fxml;

    exports org.barbertrack.barbertrack_poo.app;

    opens org.barbertrack.barbertrack_poo.app to javafx.fxml;
}