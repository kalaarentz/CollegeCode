package DrugzLLC;

import DrugzLLC.AddDialogs.AddDoctorDialogController;
import DrugzLLC.AddDialogs.AddPatientDialogController;
import DrugzLLC.AddDialogs.AddPrescriptionDialogController;
import DrugzLLC.AdvancedSearchDialogs.DoctorAdvancedSearchDialogController;
import DrugzLLC.AdvancedSearchDialogs.PatientAdvancedSearchDialogController;
import DrugzLLC.AdvancedSearchDialogs.PrescriptionAdvancedSearchDialogController;
import DrugzLLC.RelationshipConnections.HaveDialogController;
import DrugzLLC.RelationshipConnections.PrescribeDialogController;
import DrugzLLC.RelationshipConnections.SeeDialogController;
import DrugzLLC.Tables.Doctor;
import DrugzLLC.Tables.Patient;
import DrugzLLC.Tables.Prescription;
import DrugzLLC.UpdateDialogs.UpdateDoctorDialogController;
import DrugzLLC.UpdateDialogs.UpdatePatientDialogController;
import DrugzLLC.UpdateDialogs.UpdatePrescriptionDialogController;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.ResourceBundle;

import static DrugzLLC.Main.showErrorAlertDialog;
import static DrugzLLC.TableTools.*;

public class Controller implements Initializable {

    public TableView<Doctor> doctorTableView;
    public TableView<Patient> patientTableView;
    public TableView<Prescription> prescriptionTableView;
    public TextField searchBarTextField;

    public VBox deleteButton;
    public VBox editButton;
    public VBox addSeeButton;
    public VBox addPrescribeButton;
    public VBox addHaveButton;

    public Button prescribeButton;
    public Button haveButton;
    public Button seeButton;

    private Table currentTable;

    public Label userFeedBackLabel;

    public void onSearchComplete() {
        userFeedBackLabel.setText("Search results for: " + searchBarTextField.getText());
        switch (currentTable) {
            case Doctors:
                if (JDBCTools.isItemInTable(Main.getConnection(), Table.Doctors.name(), Doctor.NAME_JDBC_KEY, searchBarTextField.getText())) {
                    doctorTableView.setItems(getDoctorObservableList(JDBCTools.getResultSetInDB(
                            Main.getConnection(),
                            Table.Doctors.name(),
                            Doctor.NAME_JDBC_KEY,
                            searchBarTextField.getText()
                    )));
                } else {
                    showErrorAlertDialog("Doctor not found.");
                }
                break;
            case Patients:
                if (JDBCTools.isItemInTable(Main.getConnection(), Table.Patients.name(), Patient.LNAME_JDBC_KEY, searchBarTextField.getText())) {
                    patientTableView.setItems(getPatientObservableList(JDBCTools.getResultSetInDB(
                            Main.getConnection(), Table.Patients.name(),
                            Patient.LNAME_JDBC_KEY,
                            searchBarTextField.getText())));
                } else {
                    showErrorAlertDialog("Patient not found.");
                }
                break;
            case Prescriptions:
                if (JDBCTools.isItemInTable(Main.getConnection(), Table.Prescriptions.name(), Prescription.RX_JDBC_KEY, searchBarTextField.getText())) {
                    prescriptionTableView.setItems(getPrescriptionObservableList(JDBCTools.getResultSetInDB(
                            Main.getConnection(),
                            Table.Prescriptions.name(),
                            Prescription.RX_JDBC_KEY,
                            searchBarTextField.getText())));
                } else {
                    showErrorAlertDialog("Prescription not found.");
                }
                break;
            case have:
                break;
            case prescribe:
                break;
            case see:
                break;
        }
    }

    public void onAdvancedSearchComplete() {
        switch (currentTable) {
            case Doctors:
                onDoctorsAdvancedSearch();
                break;
            case Patients:
                onPatientsAdvancedSearch();
                break;
            case Prescriptions:
                onPrescriptionsAdvancedSearch();
                break;
            case have:
                break;
            case prescribe:
                break;
            case see:
                break;
        }
    }

    private void onPrescriptionsAdvancedSearch() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("AdvancedSearchDialogs/prescription_advanced_search_dialog.fxml"));
            AnchorPane anchorPane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(anchorPane);

            dialogStage.setScene(scene);
            PrescriptionAdvancedSearchDialogController prescriptionAdvancedSearchDialogController = loader.getController();
            prescriptionAdvancedSearchDialogController.setDialogStage(dialogStage, this::setAdvancedSearchPrescriptionTable);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onPatientsAdvancedSearch() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("AdvancedSearchDialogs/patient_advanced_search_dialog.fxml"));
            AnchorPane anchorPane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(anchorPane);

            dialogStage.setScene(scene);
            PatientAdvancedSearchDialogController patientAdvancedSearchDialogController = loader.getController();
            patientAdvancedSearchDialogController.setDialogStage(dialogStage, this::setAdvancedSearchPatientTable);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onDoctorsAdvancedSearch() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("AdvancedSearchDialogs/doctor_advanced_search_dialog.fxml"));
            AnchorPane anchorPane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(anchorPane);

            dialogStage.setScene(scene);
            DoctorAdvancedSearchDialogController doctorAdvancedSearchDialogController = loader.getController();
            doctorAdvancedSearchDialogController.setDialogStage(dialogStage, this::setAdvancedSearchDoctorTable);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setAdvancedSearchDoctorTable(ResultSet resultSet) {
        doctorTableView.setItems(getDoctorObservableList(resultSet));
        userFeedBackLabel.setText("");
    }

    private void setAdvancedSearchPatientTable(ResultSet resultSet) {
        patientTableView.setItems(getPatientObservableList(resultSet));
        userFeedBackLabel.setText("");
    }

    private void setAdvancedSearchPrescriptionTable(ResultSet resultSet) {
        prescriptionTableView.setItems(getPrescriptionObservableList(resultSet));
        userFeedBackLabel.setText("");
    }

    public void onDoctorsClicked() {
        currentTable = Table.Doctors;
        searchBarTextField.setPromptText("Search doctors name");
        userFeedBackLabel.setText("");

        deleteButton.setVisible(false);
        editButton.setVisible(false);
        addSeeButton.setVisible(false);
        addPrescribeButton.setVisible(false);
        addHaveButton.setVisible(false);

        doctorTableView.setVisible(true);
        patientTableView.setVisible(false);
        prescriptionTableView.setVisible(false);

        haveButton.setVisible(false);
        seeButton.setVisible(false);
        prescribeButton.setVisible(true);

        doctorTableView.setItems(getDoctorObservableList(JDBCTools.retrieveAllItems(Main.getConnection(), Table.Doctors.name())));
    }

    public void onPatientsClicked() {
        currentTable = Table.Patients;
        searchBarTextField.setPromptText("Search patients last name");
        userFeedBackLabel.setText("");

        deleteButton.setVisible(false);
        editButton.setVisible(false);
        addSeeButton.setVisible(false);
        addPrescribeButton.setVisible(false);
        addHaveButton.setVisible(false);

        doctorTableView.setVisible(false);
        patientTableView.setVisible(true);
        prescriptionTableView.setVisible(false);

        prescribeButton.setVisible(false);
        haveButton.setVisible(true);
        seeButton.setVisible(true);

        patientTableView.setItems(getPatientObservableList(JDBCTools.retrieveAllItems(Main.getConnection(), Table.Patients.name())));
    }

    public void onPrescriptionsClicked() {
        currentTable = Table.Prescriptions;
        searchBarTextField.setPromptText("Search prescription #");
        userFeedBackLabel.setText("");

        deleteButton.setVisible(false);
        editButton.setVisible(false);
        addSeeButton.setVisible(false);
        addPrescribeButton.setVisible(false);
        addHaveButton.setVisible(false);

        prescribeButton.setVisible(false);
        haveButton.setVisible(false);
        seeButton.setVisible(false);

        doctorTableView.setVisible(false);
        patientTableView.setVisible(false);
        prescriptionTableView.setVisible(true);

        prescriptionTableView.setItems(getPrescriptionObservableList(JDBCTools.retrieveAllItems(Main.getConnection(), Table.Prescriptions.name())));
    }

    public void onSeeClicked() {
        Patient patient = patientTableView.getSelectionModel().getSelectedItem();
        if (patient != null) {
            onDoctorsClicked();
            userFeedBackLabel.setText("Showing which doctor(s) patient : " + patient.getLastName() + " sees.");
            doctorTableView.setItems(getDoctorObservableList(JDBCTools.getResultSetNaturalJoinInDB(
                    Main.getConnection(),
                    Table.Doctors.name(),
                    Table.see.name(),
                    Patient.SSN_JDBC_KEY,
                    patient.getSsn()
            )));
            if (doctorTableView.getItems().isEmpty()) {
                showErrorAlertDialog(patient.getLastName() + " does not see any doctors.");
            }
        }
    }

    public void onHaveClicked() {
        Patient patient = patientTableView.getSelectionModel().getSelectedItem();
        if (patient != null) {
            onPrescriptionsClicked();
            userFeedBackLabel.setText("Showing which prescription(s) patient : " + patient.getLastName() + " has.");
            prescriptionTableView.setItems(getPrescriptionObservableList(JDBCTools.getResultSetNaturalJoinInDB(
                    Main.getConnection(),
                    Table.Prescriptions.name(),
                    Table.have.name(),
                    Patient.SSN_JDBC_KEY,
                    patient.getSsn()
            )));
            if (prescriptionTableView.getItems().isEmpty()) {
                showErrorAlertDialog(patient.getLastName() + " does not have any prescriptions.");
            }
        }

    }

    public void onPrescribeClicked() {
        Doctor doctor = doctorTableView.getSelectionModel().getSelectedItem();
        if (doctor != null) {
            onPrescriptionsClicked();
            userFeedBackLabel.setText("Showing which prescription(s) doctor : " + doctor.getName() + " prescribes.");
            prescriptionTableView.setItems(getPrescriptionObservableList(JDBCTools.getResultSetNaturalJoinInDB(
                    Main.getConnection(),
                    Table.Prescriptions.name(),
                    Table.prescribe.name(),
                    Doctor.ID_JDBC_KEY,
                    doctor.getId()
            )));
            if (prescriptionTableView.getItems().isEmpty()) {
                showErrorAlertDialog(doctor.getName() + " does not prescribe any prescriptions.");
            }
        }
    }

    public void onAddClicked() {
        switch (currentTable) {
            case Doctors:
                showAddDoctorDialog();
                onDoctorsClicked();
                break;
            case Patients:
                showAddPatientDialog();
                onPatientsClicked();
                break;
            case Prescriptions:
                showAddPrescriptionDialog();
                onPrescriptionsClicked();
                break;
            case have:
                break;
            case prescribe:
                break;
            case see:
                break;
        }
    }

    public void onDeleteClicked() {
        switch (currentTable) {
            case Doctors:
                deleteSelectedDoctors();
                break;
            case Patients:
                deleteSelectedPatients();
                break;
            case Prescriptions:
                deleteSelectedPrescriptions();
                break;
            case have:
                break;
            case prescribe:
                break;
            case see:
                break;
        }
    }

    public void onEditClicked() {
        switch (currentTable) {
            case Doctors:
                updateSelectedDoctors();
                onDoctorsClicked();
                break;
            case Patients:
                updateSelectedPatients();
                onPatientsClicked();
                break;
            case Prescriptions:
                updateSelectedPrescriptions();
                onPrescriptionsClicked();
                break;
            case have:
                break;
            case prescribe:
                break;
            case see:
                break;
        }
    }

    private void showAddDoctorDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("AddDialogs/add_doctor_dialog.fxml"));
            AnchorPane anchorPane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(anchorPane);

            dialogStage.setScene(scene);
            AddDoctorDialogController addDoctorDialogController = loader.getController();
            addDoctorDialogController.setDialogStage(dialogStage);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAddPatientDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("AddDialogs/add_patient_dialog.fxml"));
            AnchorPane anchorPane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(anchorPane);

            dialogStage.setScene(scene);
            AddPatientDialogController addPatientDialogController = loader.getController();
            addPatientDialogController.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateSelectedPatients() {
        Patient patient = patientTableView.getSelectionModel().getSelectedItem();
        if (patient != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("UpdateDialogs/update_patient_dialog.fxml"));
                AnchorPane anchorPane = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                Scene scene = new Scene(anchorPane);

                dialogStage.setScene(scene);
                UpdatePatientDialogController updatePatientDialogController = loader.getController();
                updatePatientDialogController.setDialogStage(dialogStage, patient);

                dialogStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateSelectedDoctors() {
        Doctor doctor = doctorTableView.getSelectionModel().getSelectedItem();
        if (doctor != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("UpdateDialogs/update_doctor_dialog.fxml"));
                AnchorPane anchorPane = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                Scene scene = new Scene(anchorPane);

                dialogStage.setScene(scene);
                UpdateDoctorDialogController updateDoctorDialogController = loader.getController();
                updateDoctorDialogController.setDialogStage(dialogStage, doctor);

                dialogStage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateSelectedPrescriptions() {
        Prescription prescription = prescriptionTableView.getSelectionModel().getSelectedItem();
        if (prescription != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("UpdateDialogs/update_prescription_dialog.fxml"));
                AnchorPane anchorPane = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                Scene scene = new Scene(anchorPane);

                dialogStage.setScene(scene);
                UpdatePrescriptionDialogController updatePrescriptionDialogController = loader.getController();
                updatePrescriptionDialogController.setDialogStage(dialogStage, prescription);

                dialogStage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showAddPrescriptionDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("AddDialogs/add_prescription_dialog.fxml"));
            AnchorPane anchorPane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(anchorPane);

            dialogStage.setScene(scene);
            AddPrescriptionDialogController addPrescriptionDialogController = loader.getController();
            addPrescriptionDialogController.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteSelectedPatients() {
        if (patientTableView.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Patient");
            alert.setHeaderText("Are you sure you would like to delete " +
                    patientTableView.getSelectionModel().getSelectedItem().getLastName() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {

                ObservableList<Patient> patientObservableList, allPatients;
                allPatients = patientTableView.getItems();
                patientObservableList = patientTableView.getSelectionModel().getSelectedItems();
                for (Patient patient : patientObservableList) {
                    // delete from data base
                    JDBCTools.deleteFromPatient(Main.getConnection(), patient.getSsn());
                }

                patientObservableList.forEach(allPatients::remove);
            }
        }

    }

    private void deleteSelectedDoctors() {
        // TODO remove check
        if (doctorTableView.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Doctor");
            alert.setHeaderText("Are you sure you would like to delete " +
                    doctorTableView.getSelectionModel().getSelectedItem().getName() + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                ObservableList<Doctor> doctorsObservableList, allDoctors;
                allDoctors = doctorTableView.getItems();
                doctorsObservableList = doctorTableView.getSelectionModel().getSelectedItems();
                for (Doctor doctor : doctorsObservableList) {
                    // delete from data base
                    JDBCTools.deleteFromDoctor(Main.getConnection(), doctor.getId());
                }

                doctorsObservableList.forEach(allDoctors::remove);
            }
        }
    }

    private void deleteSelectedPrescriptions() {
        if (prescriptionTableView.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Prescription");
            alert.setHeaderText("Are you sure you would like to delete " +
                    prescriptionTableView.getSelectionModel().getSelectedItem().getName() + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                ObservableList<Prescription> prescriptionObservableList, allPrescriptions;
                allPrescriptions = prescriptionTableView.getItems();
                prescriptionObservableList = prescriptionTableView.getSelectionModel().getSelectedItems();
                for (Prescription prescription : prescriptionObservableList) {
                    // delete from data base
                    JDBCTools.deleteFromPrescription(Main.getConnection(), prescription.getRx());
                }

                prescriptionObservableList.forEach(allPrescriptions::remove);
            }
        }
    }

    public void onAddSeeClicked() {
        Patient patient = patientTableView.getSelectionModel().getSelectedItem();
        if (patient != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("RelationshipConnections/see_dialog.fxml"));
                AnchorPane anchorPane = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                Scene scene = new Scene(anchorPane);

                dialogStage.setScene(scene);
                SeeDialogController seeDialogController = loader.getController();
                seeDialogController.setDialogStage(dialogStage, patient);
                dialogStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onAddPrescribeClicked() {
        Doctor doctor = doctorTableView.getSelectionModel().getSelectedItem();
        if (doctor != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("RelationshipConnections/prescribe_dialog.fxml"));
                AnchorPane anchorPane = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                Scene scene = new Scene(anchorPane);

                dialogStage.setScene(scene);
                PrescribeDialogController prescribeDialogController = loader.getController();
                prescribeDialogController.setDialogStage(dialogStage, doctor);
                dialogStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onAddHaveClicked() {
        Patient patient = patientTableView.getSelectionModel().getSelectedItem();
        if (patient != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("RelationshipConnections/have_dialog.fxml"));
                AnchorPane anchorPane = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                Scene scene = new Scene(anchorPane);

                dialogStage.setScene(scene);
                HaveDialogController haveDialogController = loader.getController();
                haveDialogController.setDialogStage(dialogStage, patient);
                dialogStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initDoctorTableView(doctorTableView);
        initPatientTableView(patientTableView);
        initPrescriptionTableView(prescriptionTableView);

        patientTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            deleteButton.setVisible(true);
            editButton.setVisible(true);
            addSeeButton.setVisible(true);
            addPrescribeButton.setVisible(false);
            addHaveButton.setVisible(true);
        });

        doctorTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            deleteButton.setVisible(true);
            editButton.setVisible(true);
            addSeeButton.setVisible(false);
            addPrescribeButton.setVisible(true);
            addHaveButton.setVisible(false);
        });

        prescriptionTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            deleteButton.setVisible(true);
            editButton.setVisible(true);
            addSeeButton.setVisible(false);
            addPrescribeButton.setVisible(false);
            addHaveButton.setVisible(false);
        });

        onPatientsClicked();
    }
}
