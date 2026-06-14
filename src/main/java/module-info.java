module org.barbertrack.barbertrack_poo {

    requires javafx.controls;
    requires javafx.fxml;

    exports org.barbertrack.barbertrack_poo.app;
    exports org.barbertrack.barbertrack_poo.views;
    exports org.barbertrack.barbertrack_poo.model;
    exports org.barbertrack.barbertrack_poo.repository;

    opens org.barbertrack.barbertrack_poo.app to javafx.fxml;
    opens org.barbertrack.barbertrack_poo.views to javafx.fxml;
}