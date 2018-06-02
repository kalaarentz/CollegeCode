

import DrugzLLC.JDBCTools;
import DrugzLLC.Main;
import DrugzLLC.TableTools;
import DrugzLLC.Tables.Prescription;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;

public class UpdatePrescriptionDialogController {
        public Label rxLabel;
        public TextField nameTextField;
        public TextField numberSuppliedTextField;
        public TextField numberRefillsTextField;
        public TextField sideEffectsTextField;

        private Stage dialogStage;

        public Button positiveButton;
        public Label dialogTitle;

        private Prescription prescriptionToUpdate;

        public void setDialogStage(Stage dialogStage, Prescription prescription) {
            this.dialogStage = dialogStage;
            positiveButton.setText("UPDATE");
            dialogTitle.setText("Update Prescription");
            prescriptionToUpdate = prescription;

            rxLabel.setText(String.valueOf(prescription.getRx()));
            nameTextField.setText(prescription.getName());
            numberSuppliedTextField.setText(String.valueOf(prescription.getNumberSupplied()));
            numberRefillsTextField.setText(String.valueOf(prescription.getNumberOfRefills()));
            sideEffectsTextField.setText(String.valueOf(prescription.getSideEffects()));
        }

        public void onCancelClicked() {
            dialogStage.close();
        }

        public void onUpdateClicked() {
            ArrayList<String> attributes = new ArrayList<>();
            ArrayList<String> values = new ArrayList<>();

            if(!nameTextField.getText().equals(prescriptionToUpdate.getName())) {
                attributes.add(Prescription.NAME_JDBC_KEY);
                values.add(nameTextField.getText());
            }
            if(!numberSuppliedTextField.getText().equals(String.valueOf(prescriptionToUpdate.getNumberSupplied()))) {
                attributes.add(Prescription.NUM_SUPPLIED_JDBC_KEY);
                values.add(numberSuppliedTextField.getText());
            }
            if(!numberRefillsTextField.getText().equals(String.valueOf(prescriptionToUpdate.getNumberOfRefills()))) {
                attributes.add(Prescription.NUM_REFILLS_JDBC_KEY);
                values.add(numberRefillsTextField.getText());
            }
            if(!sideEffectsTextField.getText().equals(prescriptionToUpdate.getSideEffects())) {
                attributes.add(Prescription.SIDE_EFFECTS_JDBC_KEY);
                values.add(sideEffectsTextField.getText());
            }

            JDBCTools.updateFrom(Main.getConnection(), TableTools.Table.Prescriptions.name(), attributes, values, Prescription.RX_JDBC_KEY + " = '" + prescriptionToUpdate.getRx() + "'");
            dialogStage.close();
        }
}
