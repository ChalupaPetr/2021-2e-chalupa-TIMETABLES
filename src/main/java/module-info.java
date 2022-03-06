module SCHEDULES {
    requires javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.fxml;
    requires itextpdf;

    opens com.mycompany.schedules;

    exports com.mycompany.schedules;
}
