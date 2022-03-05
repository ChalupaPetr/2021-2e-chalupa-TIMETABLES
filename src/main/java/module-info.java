module TIMETABLES {
    requires javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires itextpdf;

    opens com.mycompany.timetables;

    exports com.mycompany.timetables;
}
