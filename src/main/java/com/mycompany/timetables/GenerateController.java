package com.mycompany.timetables;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.controlsfx.control.ToggleSwitch;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;

/**
 * @author chalu
 */
public class GenerateController implements Initializable {

    // Observable Lists
    private ObservableList<Class> classes = FXCollections.observableArrayList(new ArrayList<>());
    private ObservableList<Classroom> classrooms = FXCollections.observableArrayList(new ArrayList<>());
    private ObservableList<Subject> subjects = FXCollections.observableArrayList(new ArrayList<>());

    @FXML
    TabPane mainTabPane;
    // FXML fields for the Timetables Tab
    @FXML
    ComboBox<String> ttSelect;
    @FXML
    ComboBox ttSelectSpecific;
    @FXML
    ToggleButton ttBtnToggleColors;
    @FXML
    GridPane ttTable;
    @FXML
    Label ttTableDesc;
    // FXML fields for the Gen - Classes Tab
    @FXML
    TableView<Class> genClassesTable;
    @FXML
    TableColumn<Class, String> genClassesTableYear;
    @FXML
    TableColumn<Class, String> genClassesTableName;
    @FXML
    Spinner<Integer> genClassesAddYear;
    @FXML
    TextField genClassesAddName;
    @FXML
    VBox genClassesEditBox;
    @FXML
    Label genClassesEditLabel;
    @FXML
    TableView<SubjectGroup> genClassesEditSubjectsTable;
    @FXML
    TableColumn<SubjectGroup, String> genClassesEditSubjectsTableName;
    @FXML
    TableColumn<SubjectGroup, Integer> genClassesEditSubjectsTableHours;
    @FXML
    TableColumn<SubjectGroup, Boolean> genClassesEditSubjectsTableTwoHours;
    @FXML
    Spinner<Integer> genClassesEditSubjectsHours;
    @FXML
    ComboBox genClassesEditSubjectsSubject;
    // FXML fields for the Gen - Classrooms Tab
    @FXML
    TableView<Classroom> genClassroomsTable;
    @FXML
    TableColumn<Classroom, Integer> genClassroomsTableNumber;
    @FXML
    TableColumn<Classroom, String> genClassroomsTableName;
    @FXML
    Spinner<Integer> genClassroomsAddNumber;
    // @FXML
    // TextField genClassroomsAddNumber;
    @FXML
    TextField genClassroomsAddName;
    @FXML
    VBox genClassroomsEditSubjectsBox;
    @FXML
    Label genClassroomsEditSubjectsLabel;
    @FXML
    TableView<Subject> genClassroomsEditSubjectsTable;
    @FXML
    TableColumn<Subject, String> genClassroomsEditSubjectsTableName;
    @FXML
    ComboBox genClassroomsEditSubjectsSubject;
    // FXML fields for the Gen - Subjects Tab
    @FXML
    TableView<Subject> genSubjectsTable;
    @FXML
    TableColumn<Subject, String> genSubjectsTableName;
    @FXML
    TableColumn<Subject, Color> genSubjectsTableColor;
    @FXML
    TextField genSubjectsAddName;
    @FXML
    ColorPicker genSubjectsAddColor;

    /**
     * Switches to the HOMEPAGE
     *
     * @throws IOException
     */
    @FXML
    private void switchToHomepage() throws IOException {
        App.setRoot("homepage");
    }

    // ------------------------------------------------------------------------//
    /**
     * Adds new object Class to Observable List classes
     */
    @FXML
    private void addClass() {
        String year = genClassesAddYear.getValue().toString();
        String name = genClassesAddName.getText(0, 1).toUpperCase();
        if (name.isBlank()) {
            return;
        }
        //
        Class cNew = new Class(year, name);
        for (Class c : classes) {
            if (c.toString().equals(cNew.toString())) {
                return;
            }
        }
        classes.add(new Class(year, name));
    }

    /**
     * Removes present object Class from Observable List classes
     *
     * @param selected - Object Class to remove
     */
    private void removeClass(Class selected) {
        if (selected == null || !App.showConfirmationDialog()) {
            return;
        }
        classes.remove(selected);
        //
        if (genClassesEditBox.getUserData() != null && genClassesEditBox.getUserData().equals(selected)) {
            genClassesEditBox.setVisible(false);
            genClassesEditBox.setUserData(null);
            genClassesEditLabel.setText("");
        }
    }

    /**
     * Loads objects SubjectGroup for selected object Class
     *
     * @param selected - Selected object Class
     */
    private void loadClassSubjects(Class selected) {
        if (selected == null) {
            return;
        }
        genClassesEditBox.setVisible(true);
        genClassesEditBox.setUserData(selected);
        genClassesEditLabel.setText(selected.toString());
        //
        ObservableList<SubjectGroup> sg = FXCollections.observableArrayList(selected.getSubjectGroups());
        genClassesEditSubjectsTable.getItems().clear();
        genClassesEditSubjectsTable.setItems(sg);
    }

    /**
     * Edits dotations of selected object SubjectGroup of selected object Class
     * (removes or adds object if needed)
     */
    @FXML
    private void editClassSubjects() {
        if (genClassesEditSubjectsSubject.getItems().isEmpty()) {
            return;
        }
        int dotation = genClassesEditSubjectsHours.getValue();
        if (dotation < 0) {
            App.showErrorDialog();
            return;
        }
        Subject selectedS = (subjects
                .filtered(s -> s.equals(genClassesEditSubjectsSubject.getSelectionModel().getSelectedItem()))).get(0);
        Class selectedC = (Class) genClassesEditBox.getUserData();
        ArrayList<SubjectGroup> sg = selectedC.getSubjectGroups();
        //
        if (!sg.isEmpty()) {
            for (int i = 0; i < sg.size(); i++) {
                SubjectGroup group = sg.get(i);
                if (group.getSubjects().size() == 1 && group.getSubjects().get(0).equals(selectedS)) {
                    if (dotation == 0) {
                        removeClassSubject(group);
                    } else {
                        group.setDotation(dotation);
                    }
                    break;
                }
                if (i == sg.size() - 1 && dotation != 0) {
                    sg.add(new SubjectGroup(new ArrayList<Subject>() {
                        {
                            add(selectedS);
                        }
                    }, dotation, false));
                }
            }
        } else if (dotation != 0) {
            sg.add(new SubjectGroup(new ArrayList<Subject>() {
                {
                    add(selectedS);
                }
            }, dotation, false));

        }
        selectedC.setSubjectGroups(sg);
        loadClassSubjects(selectedC);
    }

    /**
     * Removes object SubjectGroup
     *
     * @param selected - Selected object SubjectGroup
     */
    private void removeClassSubject(SubjectGroup selected) {
        Class selectedC = (Class) genClassesEditBox.getUserData();
        ArrayList<SubjectGroup> sg = selectedC.getSubjectGroups();
        sg.remove(selected);
        selectedC.setSubjectGroups(sg);
        loadClassSubjects(selectedC);
    }

    /**
     * Joins two or more objects SubjectGroup to one SubjectGroup (merges with
     * existing one if is present)
     *
     * @param selectedItems - Selectes objects SubjectGroup
     */
    @FXML
    private void joinClassSubjects(ArrayList<SubjectGroup> selectedItems) {
        Class c = (Class) genClassesEditBox.getUserData();
        ObservableList<SubjectGroup> sg = FXCollections.observableArrayList(c.getSubjectGroups());
        String number = genClassesEditSubjectsHours.getValue().toString();
        int dotation = Integer.parseInt(number);
        int maxDotation = selectedItems.get(0).getDotation();
        ArrayList<Subject> joinedSubjects = new ArrayList<>();
        for (SubjectGroup group : selectedItems) {
            maxDotation = Math.min(maxDotation, group.getDotation());
            group.getSubjects().stream().filter(s -> (!joinedSubjects.contains(s))).forEachOrdered(s -> {
                joinedSubjects.add(s);
            });
        }
        if (dotation <= 0 || dotation > maxDotation) {
            App.showErrorDialog();
            return;
        }
        selectedItems.stream().map(selGroup -> {
            selGroup.setDotation(selGroup.getDotation() - dotation);
            return selGroup;
        }).filter(selGroup -> (selGroup.getDotation() == 0)).forEachOrdered(selGroup -> {
            sg.remove(selGroup);
        });
        for (int i = 0; i < sg.size(); i++) {
            SubjectGroup group = sg.get(i);
            if (group.getSubjects().containsAll(joinedSubjects) && joinedSubjects.containsAll(group.getSubjects())) {
                group.setDotation(group.getDotation() + dotation);
                break;
            } else if (i == sg.size() - 1) {
                sg.add(new SubjectGroup(joinedSubjects, dotation, false));
                break;
            }
        }
        c.setSubjectGroups(new ArrayList<>(sg));
        loadClassSubjects(c);
    }

    /**
     * Splits two or more objects SubjectGroup (merges with existing ones if are
     * present)
     *
     * @param selected - Selected object SubjectGroup
     */
    private void splitClassSubjects(SubjectGroup selected) {
        Class selectedC = (Class) genClassesEditBox.getUserData();
        ArrayList<SubjectGroup> sg = selectedC.getSubjectGroups();
        ArrayList<Subject> groupSubjects = selected.getSubjects();
        if (groupSubjects.size() > 1) {
            for (int i = 0; i < groupSubjects.size(); i++) {
                Subject s = groupSubjects.get(i);
                for (int j = 0; j < sg.size(); j++) {
                    SubjectGroup g = sg.get(j);
                    if (g.getSubjects().size() == 1 && g.getSubjects().contains(s)) {
                        g.setDotation(g.getDotation() + selected.getDotation());
                        break;
                    } else if (j == sg.size() - 1) {
                        sg.add(new SubjectGroup(new ArrayList<>() {
                            {
                                add(s);
                            }
                        }, selected.getDotation(), false));
                        break;
                    }
                }
            }
            sg.remove(selected);
            selectedC.setSubjectGroups(sg);
            loadClassSubjects(selectedC);
        }
    }

    // ------------------------------------------------------------------------//
    /**
     * Adds new object Classroom to Observable List classrooms
     */
    @FXML
    private void addClassroom() {
        String number = genClassroomsAddNumber.getValue().toString();
        String name = genClassroomsAddName.getText();
        if (name.isBlank()) {
            return;
        }
        //
        Classroom crNew = new Classroom(number, name);
        for (Classroom cr : classrooms) {
            if (cr.toString().equals(crNew.toString())) {
                return;
            }
        }
        classrooms.add(crNew);
    }

    /**
     * Removes present object Classroom from Observable List classrooms
     *
     * @param selected - Selected object Classroom
     */
    private void removeClassroom(Classroom selected) {
        if (selected == null || !App.showConfirmationDialog()) {
            return;
        }
        classrooms.remove(selected);
        //
        if (genClassroomsEditSubjectsBox.getUserData() != null
                && genClassroomsEditSubjectsBox.getUserData().equals(selected)) {
            genClassroomsEditSubjectsBox.setVisible(false);
            genClassroomsEditSubjectsBox.setUserData(null);
            genClassroomsEditSubjectsLabel.setText("");
        }
    }

    /**
     * Loads objects SubjectGroup for selected object Classroom
     *
     * @param selected - Selected object Classroom
     */
    private void loadClassroomSubjects(Classroom selected) {
        genClassroomsEditSubjectsBox.setVisible(true);
        genClassroomsEditSubjectsBox.setUserData(selected);
        genClassroomsEditSubjectsLabel.setText(selected.toString());
        //
        ArrayList<Subject> ps = selected.getProhibitedSubjects();
        genClassroomsEditSubjectsTable.setItems(FXCollections.observableArrayList(ps));
    }

    /**
     * Adds object SubjectGroup of selected object Classroom if not present
     */
    @FXML
    private void addClassroomSubject() {
        if (genClassroomsEditSubjectsSubject.getItems().isEmpty()) {
            return;
        }
        Subject selectedS = (subjects
                .filtered(s -> s.equals(genClassroomsEditSubjectsSubject.getSelectionModel().getSelectedItem())))
                .get(0);
        //
        Classroom selectedCr = (Classroom) genClassroomsEditSubjectsBox.getUserData();
        ArrayList<Subject> ps = selectedCr.getProhibitedSubjects();
        if (!ps.contains(selectedS)) {
            ps.add(selectedS);
            selectedCr.setProhibitedSubjects(ps);
        }
        selectedCr.setProhibitedSubjects(ps);
        loadClassroomSubjects(selectedCr);
    }

    // ------------------------------------------------------------------------//
    /**
     * Adds new object Subject to Observable List subjects
     */
    @FXML
    private void addSubject() {
        String name = genSubjectsAddName.getText();
        Color color = genSubjectsAddColor.getValue().brighter().desaturate();
        if (name.isBlank()) {
            return;
        }
        //
        Subject sNew = new Subject(name, color);
        for (Subject s : subjects) {
            if (s.toString().equals(sNew.toString())) {
                return;
            }
        }
        subjects.add(sNew);
    }

    /**
     * Removes present object Subject from Observable List subjects and all
     * lists in objects Class and Classroom
     *
     * @param selected - Selected object Subject
     */
    private void removeSubject(Subject selected) {
        if (selected == null || !App.showConfirmationDialog()) {
            return;
        }
        subjects.remove(selected);
        //
        classes.forEach(c -> {
            ArrayList<SubjectGroup> sg = c.getSubjectGroups();
            sg.removeIf(group -> group.getSubjects().contains(selected));
            c.setSubjectGroups(sg);
            if (genClassesEditBox.getUserData() != null && genClassesEditBox.getUserData().equals(c)) {
                loadClassSubjects(c);
            }
        });
        classrooms.forEach(cr -> {
            ArrayList<Subject> ps = cr.getProhibitedSubjects();
            ps.remove(selected);
            cr.setProhibitedSubjects(ps);
            if (genClassroomsEditSubjectsBox.getUserData() != null
                    && genClassroomsEditSubjectsBox.getUserData().equals(cr)) {
                loadClassroomSubjects(cr);
            }
        });
    }

    /**
     * Edits color of selcted object Subject
     *
     * @param selected - Selected object Subject
     * @param color    - New color
     */
    private void editSubjectColor(Subject selected, Color color) {
        color = color.brighter().desaturate();
        selected.setColor(color);
        genSubjectsTable.refresh();
    }

    // ------------------------------------------------------------------------//
    /**
     * Generates timetable for all objects Class and Classroom
     */
    @FXML
    private void generateTimetable() {
        classrooms.forEach(Classroom::resetTimetable);
        classes.forEach((Class c) -> {
            ArrayList<ArrayList<TimetableHour>> timetable = new ArrayList<>();
            ArrayList<SubjectGroup> sg = new ArrayList<>(c.getSubjectGroups().size());
            c.getSubjectGroups().forEach(group -> {
                sg.add(new SubjectGroup(group.getSubjects(), group.getDotation(), group.isTwoHours()));
            });
            //
            int days = ttTable.getRowCount() - 1;
            int hours = ttTable.getColumnCount() - 1;
            for (int y = 0; y < days; y++) {
                timetable.add(y, new ArrayList<>());
                SubjectGroup preLastGroup = null;
                SubjectGroup lastGroup = null;
                SubjectGroup prefNextGroup = null;
                ArrayList<Classroom> prefNextCr = new ArrayList<>();
                int hourCount = sg.stream().map(group -> group.getDotation()).reduce(0, Integer::sum);
                int idealHours = (int) Math.ceil((float) hourCount / (float) (days - y));
                for (int x = 0; x < hours; x++) {
                    timetable.get(y).add(x, null);
                    SubjectGroup currentGroup;
                    HashMap<Subject, Classroom> currentCrs;
                    //
                    ArrayList<SubjectGroup> posGroups = new ArrayList<>(sg);
                    Collections.shuffle(posGroups);
                    if (posGroups.isEmpty()) {
                        continue;
                    }
                    if (prefNextGroup != null) {
                        posGroups.remove(prefNextGroup);
                        posGroups.add(0, prefNextGroup);
                        prefNextGroup = null;
                    }
                    for (SubjectGroup posG : posGroups) {
                        if (posG.equals(lastGroup)) {
                            if (posG.isTwoHours()) {
                                if (posG.equals(preLastGroup) && sg.size() > 1) {
                                    continue;
                                }
                            } else {
                                if (sg.size() > 1) {
                                    continue;
                                }
                            }
                        }
                        currentGroup = posG;

                        ArrayList<Classroom> posClassrooms = new ArrayList<>(classrooms);
                        Collections.shuffle(posClassrooms);
                        if (!prefNextCr.isEmpty()) {
                            posClassrooms.removeAll(prefNextCr);
                            posClassrooms.addAll(0, prefNextCr);
                            prefNextCr.clear();
                        }
                        HashMap<Subject, Classroom> posCrs = new HashMap<>();
                        for (Subject posS : currentGroup.getSubjects()) {
                            for (Classroom posCr : posClassrooms) {
                                if (posCr.getTimetable().get(y).get(x) != null
                                        || posCr.getProhibitedSubjects().contains(posS)
                                        || posCrs.containsValue(posCr)) {
                                    continue;
                                }
                                posCrs.put(posS, posCr);
                                break;
                            }
                        }
                        currentCrs = posCrs;

                        if (currentCrs.size() == currentGroup.getSubjects().size()) {
                            for (Classroom cr : currentCrs.values()) {
                                ArrayList<ArrayList<TimetableHour>> timetableCr = cr.getTimetable();
                                timetableCr.get(y).set(x, new TimetableHour(c, currentCrs, currentGroup));
                                cr.setTimetable(timetableCr);
                            }
                            timetable.get(y).set(x, new TimetableHour(c, currentCrs, currentGroup));

                            int d = currentGroup.getDotation() - 1;
                            if (d == 0) {
                                sg.remove(currentGroup);
                            } else {
                                currentGroup.setDotation(d);
                                if (currentGroup.isTwoHours() && !currentGroup.equals(preLastGroup)
                                        && !currentGroup.equals(lastGroup) && x < hours - 1) {
                                    prefNextCr.addAll(currentCrs.values());
                                    prefNextGroup = currentGroup;
                                }
                            }
                            preLastGroup = lastGroup;
                            lastGroup = currentGroup;
                            break;
                        }
                    }
                    if (prefNextGroup == null && x >= idealHours - 1) {
                        break;
                    }
                }
            }
            c.setTimetable(timetable);
        });
        //
        mainTabPane.getSelectionModel().selectFirst();
        ttTable.setVisible(false);
    }

    /**
     * Displays timetable of selected object Class or Classroom
     */
    @FXML
    private void showTimetable() {
        ttTable.setVisible(false);
        if (ttSelectSpecific.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        ttTableDesc.setText(ttSelectSpecific.getSelectionModel().getSelectedItem().toString());
        //
        ArrayList<ArrayList<TimetableHour>> timetable;
        switch (ttSelect.getSelectionModel().getSelectedIndex()) {
            case 0:
                timetable = (classes.filtered(
                        c -> c.toString().equals(ttSelectSpecific.getSelectionModel().getSelectedItem().toString())))
                        .get(0).getTimetable();
                break;
            case 1:
                timetable = (classrooms.filtered(
                        c -> c.toString().equals(ttSelectSpecific.getSelectionModel().getSelectedItem().toString())))
                        .get(0).getTimetable();
                break;
            default:
                return;
        }
        if (timetable != null && !timetable.isEmpty()) {
            ttTable.setVisible(true);
            ttTable.getChildren()
                    .removeIf(ch -> GridPane.getColumnIndex(ch) != null && GridPane.getRowIndex(ch) != null);
            //
            int days = ttTable.getRowCount() - 1;
            int hours = ttTable.getColumnCount() - 1;
            for (int y = 0; y < days; y++) {
                ArrayList<TimetableHour> day = timetable.get(y);
                for (int x = 0; x < hours; x++) {
                    VBox vboxWrapper = new VBox();
                    vboxWrapper.setId("data");
                    if (x < day.size() && day.get(x) != null) {
                        TimetableHour hour = day.get(x);
                        for (Subject s : hour.getSubjects().getSubjects()) {
                            VBox vboxData = new VBox();
                            vboxData.setUserData(s.getColor());
                            VBox.setVgrow(vboxData, Priority.ALWAYS);
                            //
                            HBox hboxData = new HBox();
                            Label lClass = new Label(hour.getParticipantClass().toString());
                            Label lClassroom = new Label(hour.getClassrooms().get(s).toString());
                            Label lSubject = new Label(s.toString());
                            //
                            hboxData.getChildren().addAll(lClass, lClassroom);
                            vboxData.getChildren().addAll(hboxData, lSubject);
                            vboxWrapper.getChildren().add(vboxData);
                        }
                    } else {
                        VBox vboxData = new VBox();
                        vboxData.setUserData(Color.WHITE);
                        HBox hboxData = new HBox();
                        Label lClass = new Label("");
                        Label lClassroom = new Label("");
                        Label lSubject = new Label("");
                        //
                        hboxData.getChildren().addAll(lClass, lClassroom);
                        vboxData.getChildren().addAll(hboxData, lSubject);
                        vboxWrapper.getChildren().add(vboxData);
                    }
                    ttTable.add(vboxWrapper, x + 1, y + 1);
                }
            }
            toggleTimetableColors();
        }
    }

    /**
     * Toggles colors of objects Subject
     */
    @FXML
    private void toggleTimetableColors() {
        for (Integer row = 0; row < ttTable.getRowCount(); row++) {
            for (Node ch : ttTable.getChildren()) {
                if ((Objects.equals(GridPane.getRowIndex(ch), row) || (GridPane.getRowIndex(ch) == null && row == 0))
                        && (ch.getId() != null && ch.getId().equals("data"))) {
                    VBox vboxWrapper = (VBox) ch;
                    for (Node wrapperCh : vboxWrapper.getChildren()) {
                        VBox vboxData = (VBox) wrapperCh;
                        Color color = ttBtnToggleColors.isSelected() ? (Color) vboxData.getUserData() : Color.WHITE;
                        vboxData.setBackground(
                                new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
                        //
                        String textFill = blackOrWhiteText(color).equals(Color.BLACK) ? "BLACK" : "WHITE";
                        vboxData.getChildren().get(1).setStyle("-fx-text-fill: " + textFill + ";");
                        ((HBox) vboxData.getChildren().get(0)).getChildren().forEach(child -> {
                            child.setStyle("-fx-text-fill: " + textFill + ";");
                        });
                    }
                }
            }
        }
    }

    // ------------------------------------------------------------------------//
    /**
     * Exports timetable of selected object Class or object Classroom
     */
    @FXML
    private void exportTimetable() {
        if (!ttTable.isVisible()) {
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(App.getBundle().getString("app.fileChooser.save"));
        fileChooser.setInitialFileName("Timetable" + ".pdf");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF File", "*.pdf"));
        //
        try (FileOutputStream outS = new FileOutputStream(fileChooser.showSaveDialog(App.stage))) {
            Document pdf = new Document();
            PdfWriter.getInstance(pdf, outS);
            pdf.open();
            //
            final BaseFont baseFont = BaseFont.createFont("src/main/resources/fonts/Arimo.ttf", BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED);
            //
            PdfPTable table = new PdfPTable(ttTable.getColumnCount());
            for (Integer row = 0; row < ttTable.getRowCount(); row++) {
                for (Node ch : ttTable.getChildren()) {
                    if (Objects.equals(GridPane.getRowIndex(ch), row)
                            || (GridPane.getRowIndex(ch) == null && row == 0)) {
                        if (ch.getId() != null && ch.getId().equals("data")) {
                            VBox vboxWrapper = (VBox) ch;
                            PdfPTable wrapperTable = new PdfPTable(1);
                            for (Node wrapperCh : vboxWrapper.getChildren()) {
                                VBox vboxData = (VBox) wrapperCh;
                                HBox hbox = (HBox) vboxData.getChildren().get(0);
                                Label lClass = (Label) hbox.getChildren().get(0);
                                Label lClassroom = (Label) hbox.getChildren().get(1);
                                Label lSubject = (Label) vboxData.getChildren().get(1);
                                //
                                Color color = ttBtnToggleColors.isSelected() ? (Color) vboxData.getUserData()
                                        : Color.WHITE;
                                BaseColor bgColor = new BaseColor((float) color.getRed(), (float) color.getGreen(),
                                        (float) color.getBlue());
                                BaseColor textFill = blackOrWhiteText(color).equals(Color.BLACK) ? BaseColor.BLACK
                                        : BaseColor.WHITE;
                                Font f6 = new Font(baseFont, 6, Font.NORMAL, textFill);
                                Font f12b = new Font(baseFont, 12, Font.BOLD, textFill);
                                //
                                PdfPCell cClass = new PdfPCell(new Phrase(lClass.getText(), f6));
                                cClass.setBorder(0);
                                cClass.setHorizontalAlignment(Element.ALIGN_LEFT);
                                cClass.setBackgroundColor(bgColor);
                                PdfPCell cClassroom = new PdfPCell(new Phrase(lClassroom.getText(), f6));
                                cClassroom.setBorder(0);
                                cClassroom.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cClassroom.setBackgroundColor(bgColor);
                                PdfPCell cSubject = new PdfPCell(new Phrase(lSubject.getText(), f12b));
                                cSubject.setBorder(0);
                                cSubject.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cSubject.setBackgroundColor(bgColor);
                                cSubject.setColspan(2);
                                PdfPTable dataTable = new PdfPTable(2);
                                dataTable.addCell(cClass);
                                dataTable.addCell(cClassroom);
                                dataTable.addCell(cSubject);
                                wrapperTable.addCell(dataTable);
                            }
                            PdfPCell wrapperCell = new PdfPCell(wrapperTable);
                            wrapperCell.setBorder(0);
                            table.addCell(wrapperCell);
                        } else {
                            VBox vbox = (VBox) ch;
                            Label l = (Label) vbox.getChildren().get(0);
                            //
                            Font f12 = new Font(baseFont, 12, Font.NORMAL, BaseColor.BLACK);
                            PdfPCell c = new PdfPCell(new Phrase(l.getText(), f12));
                            c.setBorder(0);
                            c.setHorizontalAlignment(Element.ALIGN_CENTER);
                            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            c.setMinimumHeight(30);
                            //
                            PdfPTable dataTable = new PdfPTable(1);
                            dataTable.addCell(c);
                            table.addCell(dataTable);
                        }
                    }
                }
            }
            pdf.add(table);
            pdf.close();
        } catch (FileNotFoundException | DocumentException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Exports all data into config file
     */
    @FXML
    private void exportConfig() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(App.getBundle().getString("app.fileChooser.save"));
        fileChooser.setInitialFileName("TimetablesConf" + ".conf");
        fileChooser.getExtensionFilters()
                .addAll(new FileChooser.ExtensionFilter("Conf Files", "*.conf", "*.dat", "*.ser", "*.bin"));
        App.confFile = fileChooser.showSaveDialog(App.stage);
        //
        if (App.confFile != null) {
            try (FileOutputStream fOut = new FileOutputStream(App.confFile);
                    ObjectOutputStream outS = new ObjectOutputStream(fOut)) {
                outS.writeObject(new ArrayList<>(classes));
                outS.writeObject(new ArrayList<>(classrooms));
                outS.writeObject(new ArrayList<>(subjects));
            } catch (FileNotFoundException ex) {
                System.out.println(ex);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }

    // ------------------------------------------------------------------------//
    public Color blackOrWhiteText(Color color) {
        int brightness = (int) Math
                .round((color.getRed() * 255 * 299) + (color.getGreen() * 255 * 587) +
                        (color.getBlue() * 255 * 114))
                / 1000;
        return brightness > 130 ? Color.BLACK : Color.WHITE;
    }

    // ------------------------------------------------------------------------//
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (App.confFile != null) {
            try (FileInputStream fIn = new FileInputStream(App.confFile);
                    ObjectInputStream inS = new ObjectInputStream(fIn)) {
                classes = FXCollections.observableArrayList((ArrayList<Class>) inS.readObject());
                classrooms = FXCollections.observableArrayList((ArrayList<Classroom>) inS.readObject());
                subjects = FXCollections.observableArrayList((ArrayList<Subject>) inS.readObject());
            } catch (FileNotFoundException ex) {
                System.out.println(ex);
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println(ex);
            }
        }
        //
        initTimetables(rb);
        initClasses(rb);
        initClassrooms(rb);
        initSubjects(rb);
    }

    private void initTimetables(ResourceBundle rb) {
        ttSelect.getItems().addAll(rb.getString("gen.tt.select.classes"),
                rb.getString("gen.tt.select.classrooms"));
        ttSelect.getSelectionModel().selectFirst();
        ttSelect.setOnAction((e) -> {
            int selected = ttSelect.getSelectionModel().getSelectedIndex();
            switch (selected) {
                case 0:
                    ttSelectSpecific.setItems(classes);
                    break;
                case 1:
                    ttSelectSpecific.setItems(classrooms);
                    break;
            }
            ttSelectSpecific.getSelectionModel().selectFirst();
        });

        ttSelect.fireEvent(new ActionEvent());
    }

    private void initClasses(ResourceBundle rb) {
        classes.addListener((ListChangeListener<Class>) (change) -> {
            genClassesTable.refresh();
            ttSelect.fireEvent(new ActionEvent());
        });
        //
        genClassesTableYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        genClassesTableName.setCellValueFactory(new PropertyValueFactory<>("name"));
        ContextMenu cm = new ContextMenu();
        MenuItem iEditSubjects = new MenuItem(rb.getString("gen.classes.table.btnEditSubjects"));
        iEditSubjects.setOnAction((e) -> {
            loadClassSubjects(genClassesTable.getSelectionModel().getSelectedItem());
        });
        MenuItem iRemove = new MenuItem(rb.getString("gen.classes.table.btnRemove"));
        iRemove.setOnAction((e) -> {
            removeClass(genClassesTable.getSelectionModel().getSelectedItem());
        });
        cm.getItems().addAll(iEditSubjects, iRemove);
        genClassesTable.setContextMenu(cm);
        genClassesTable.setItems(classes);
        //
        genClassesAddYear.setValueFactory(new IntegerSpinnerValueFactory(1, 9, 1));
        genClassesAddYear.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d+")) {
                genClassesAddYear.getEditor().setText(oldValue);
            } else {
                try {
                    Integer.parseInt(newValue);
                } catch (NumberFormatException ex) {
                    genClassesAddYear.getEditor().setText(oldValue);
                }
            }
        });
        //
        genClassesEditSubjectsTableName.setCellValueFactory(new PropertyValueFactory<>("subjectsString"));
        genClassesEditSubjectsTableHours.setCellValueFactory(new PropertyValueFactory<>("dotation"));
        genClassesEditSubjectsTableTwoHours.setCellValueFactory(new PropertyValueFactory<>("twoHours"));
        genClassesEditSubjectsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ContextMenu cmSI = new ContextMenu();
        MenuItem iEditTwoHoursSI = new MenuItem(rb.getString("gen.classes.table.subjects.btnEditTwoHours"));
        iEditTwoHoursSI.setOnAction((e) -> {
            ObservableList<SubjectGroup> selectedItems = genClassesEditSubjectsTable.getSelectionModel()
                    .getSelectedItems();
            selectedItems.forEach(item -> {
                item.setTwoHours(!item.isTwoHours());
            });
            genClassesEditSubjectsTable.refresh();
        });
        MenuItem iJoinGroupsSI = new MenuItem(rb.getString("gen.classes.table.subjects.btnJoin"));
        iJoinGroupsSI.setOnAction((e) -> {
            ObservableList<SubjectGroup> selectedItems = genClassesEditSubjectsTable.getSelectionModel()
                    .getSelectedItems();
            if (selectedItems.size() > 1) {
                joinClassSubjects(new ArrayList<>(selectedItems));
            }
        });
        MenuItem iSplitGroupsSI = new MenuItem(rb.getString("gen.classes.table.subjects.btnSplit"));
        iSplitGroupsSI.setOnAction((e) -> {
            splitClassSubjects(genClassesEditSubjectsTable.getSelectionModel().getSelectedItem());
        });
        MenuItem iRemoveSI = new MenuItem(rb.getString("gen.classes.table.subjects.btnRemove"));
        iRemoveSI.setOnAction((e) -> {
            removeClassSubject(genClassesEditSubjectsTable.getSelectionModel().getSelectedItem());
        });
        cmSI.getItems().addAll(iEditTwoHoursSI, iJoinGroupsSI, iSplitGroupsSI, iRemoveSI);
        genClassesEditSubjectsTable.setContextMenu(cmSI);
        genClassesEditSubjectsSubject.setItems(subjects);
        genClassesEditSubjectsSubject.getSelectionModel().selectFirst();
        //
        genClassesEditSubjectsHours.setValueFactory(new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 1));
        genClassesEditSubjectsHours.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d+")) {
                genClassesEditSubjectsHours.getEditor().setText(oldValue);
            } else {
                try {
                    Integer.parseInt(newValue);
                } catch (NumberFormatException ex) {
                    genClassesEditSubjectsHours.getEditor().setText(oldValue);
                }
            }
        });
    }

    private void initClassrooms(ResourceBundle rb) {
        classrooms.addListener((ListChangeListener<Classroom>) (change) -> {
            genClassroomsTable.refresh();
            ttSelect.fireEvent(new ActionEvent());
        });
        //
        genClassroomsTableNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
        genClassroomsTableName.setCellValueFactory(new PropertyValueFactory<>("name"));
        ContextMenu cm = new ContextMenu();
        MenuItem iEditSubjects = new MenuItem(rb.getString("gen.classroooms.table.btnEditSubjects"));
        iEditSubjects.setOnAction((e) -> {
            loadClassroomSubjects(genClassroomsTable.getSelectionModel().getSelectedItem());
        });
        MenuItem iRemove = new MenuItem(rb.getString("gen.classrooms.table.btnRemove"));
        iRemove.setOnAction((e) -> {
            removeClassroom(genClassroomsTable.getSelectionModel().getSelectedItem());
        });
        cm.getItems().addAll(iEditSubjects, iRemove);
        genClassroomsTable.setContextMenu(cm);
        genClassroomsTable.setItems(classrooms);
        //
        genClassroomsAddNumber.setValueFactory(new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
        genClassesAddYear.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d+")) {
                genClassesAddYear.getEditor().setText(oldValue);
            } else {
                try {
                    Integer.parseInt(newValue);
                } catch (NumberFormatException ex) {
                    genClassesAddYear.getEditor().setText(oldValue);
                }
            }
        });
        //
        genClassroomsEditSubjectsTableName.setCellValueFactory(new PropertyValueFactory<>("name"));
        genClassroomsEditSubjectsSubject.getSelectionModel().selectFirst();
        ContextMenu cmS = new ContextMenu();
        MenuItem iRemoveS = new MenuItem(App.getBundle().getString("gen.classrooms.table.subjects.btnRemove"));
        iRemoveS.setOnAction((e) -> {
            if (!App.showConfirmationDialog()) {
                return;
            }
            Classroom selectedCr = (Classroom) genClassroomsEditSubjectsBox.getUserData();
            ArrayList<Subject> ps = selectedCr.getProhibitedSubjects();
            Subject selectedS = genClassroomsEditSubjectsTable.getSelectionModel().getSelectedItem();
            ps.remove(selectedS);
            selectedCr.setProhibitedSubjects(ps);
            loadClassroomSubjects(selectedCr);
        });
        cmS.getItems().addAll(iRemoveS);
        genClassroomsEditSubjectsTable.setContextMenu(cmS);
        genClassroomsEditSubjectsSubject.setItems(subjects);
    }

    private void initSubjects(ResourceBundle rb) {
        subjects.addListener((ListChangeListener<Subject>) (change) -> {
            genSubjectsTable.refresh();
            //
            genClassesEditSubjectsSubject.setItems(subjects);
            genClassesEditSubjectsSubject.getSelectionModel().selectFirst();
            //
            genClassroomsEditSubjectsSubject.setItems(subjects);
            genClassroomsEditSubjectsSubject.getSelectionModel().selectFirst();
        });

        //
        genSubjectsTableName.setCellValueFactory(new PropertyValueFactory<>("name"));
        genSubjectsTableColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        genSubjectsTableColor.setCellFactory(new Callback<TableColumn<Subject, Color>, TableCell<Subject, Color>>() {
            @Override
            public TableCell<Subject, Color> call(TableColumn<Subject, Color> param) {
                return new TableCell<Subject, Color>() {
                    @Override
                    protected void updateItem(Color color, boolean empty) {
                        if (!empty) {
                            setText(color.toString());
                            setTextFill(blackOrWhiteText(color));
                            setBackground(
                                    new Background(new BackgroundFill(color, CornerRadii.EMPTY, new Insets(1.0))));
                        }
                    };
                };
            }
        });
        ContextMenu cm = new ContextMenu();
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setStyle("-fx-background-color: #ffffff;");
        MenuItem iEditColor = new CustomMenuItem(colorPicker, false);
        iEditColor.setOnAction((e) -> {
            editSubjectColor(genSubjectsTable.getSelectionModel().getSelectedItem(), colorPicker.getValue());
        });
        MenuItem iRemove = new MenuItem(rb.getString("gen.subjects.table.btnRemove"));
        iRemove.setOnAction((e) -> {
            removeSubject(genSubjectsTable.getSelectionModel().getSelectedItem());
        });
        cm.getItems().addAll(iEditColor, iRemove);
        genSubjectsTable.setContextMenu(cm);
        genSubjectsTable.setItems(subjects);
    }
}
