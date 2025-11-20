package com.example.hospitalmanagementsystemfrontendfinal.controller;

import com.example.hospitalmanagementsystemfrontendfinal.ApiClient;
import com.example.hospitalmanagementsystemfrontendfinal.model.Appointment;
import com.example.hospitalmanagementsystemfrontendfinal.model.Note;
import com.example.hospitalmanagementsystemfrontendfinal.model.Patient;
import com.example.hospitalmanagementsystemfrontendfinal.view.DoctorViews.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DoctorController {
    private final String drId;
    private boolean menuOpen = false;
    private final BorderPane root;
    private Button menuBtn;
    private final Stage stage;
    private final String BASE_URL = "http://localhost:8080/api";

    public void showDoctorView() {
        DoctorMainView mainView = new DoctorMainView(this);
        Scene scene = new Scene(mainView.getView());
        stage.setScene(scene);
        stage.setFullScreenExitHint("");
        stage.setFullScreen(true);
        stage.show();
    }

    public DoctorController(String drId, Stage stage) {
        this.drId = drId;
        this.stage = stage;
        this.root = new BorderPane();
        initializeMenuButton();
    }

    private void initializeMenuButton() {
        this.menuBtn = new Button("☰");
        menuBtn.setStyle("-fx-font-size: 20px; -fx-background-color: #3498db; -fx-text-fill: white;");
    }

    public String getDrId() {
        return drId;
    }

    public BorderPane getRoot() {
        return root;
    }

    public Button getMenuBtn() {
        return menuBtn;
    }

    public void toggleMenu(VBox menu) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), menu);
        transition.setToX(menuOpen ? 220 : 0);
        menuOpen = !menuOpen;
        transition.play();
    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    public boolean isValidPhoneNumber(String phone) {
        String phoneRegex = "^[0-9]{8}$";
        return phone.matches(phoneRegex);
    }

    public void animateButton(Button button) {
        button.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), button);
            scale.setToX(1.1);
            scale.setToY(1.1);
            scale.play();
        });

        button.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), button);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });
    }

    public List<Patient> getPatients() {
        String rawResponse = getPatientsForDr(drId);

        List<Patient> patients = new ArrayList<>();

        if (rawResponse != null && !rawResponse.isEmpty()) {
            try {
                JSONArray jsonArray = new JSONArray(rawResponse);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject patientData = jsonArray.getJSONObject(i);
                    Patient patient = new Patient();

                    patient.setPatientId(patientData.getString("patientId"));
                    patient.setName(patientData.getString("name"));
                    patient.setPhoneNumber(patientData.getString("phoneNumber"));
                    patient.setGender(patientData.getString("gender"));
                    patient.setEmail(patientData.getString("email"));
                    String dateOfBirthStr = patientData.getString("dateOfBirth");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate dateOfBirth = LocalDate.parse(dateOfBirthStr, formatter);
                    patient.setDateOfBirth(dateOfBirth);

                    patients.add(patient);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return patients;
    }

    public List<Appointment> getRequestedAppointments() {
        return getRequestedAppointments(drId);
    }

    public String[] getUserInfo() {
        return getDrInfo(drId);
    }

    public boolean updateProfile(String email, String phone, byte[] imageData) {
        return updateDrProfile(drId, email, phone, imageData);
    }

    public ImageView getProfileImage(String userId, String gender, String table) {
        byte[] imageData = new byte[0];
        if (table.equals("doctors")) {
            imageData = getDoctorProfilePhoto(userId);
        }
        else if (table.equals("patients")) {
            imageData = getPatientProfilePhoto(userId);
        }

        ImageView imageView;

        if (imageData != null) {
            imageView = new ImageView(new Image(new ByteArrayInputStream(imageData)));
        } else {
            String defaultImagePath = gender.equalsIgnoreCase("Female") ? "/female.png" : "/male.png";
            imageView = new ImageView(defaultImagePath);
        }
        imageView.setFitHeight(150);
        imageView.setFitWidth(150);
        imageView.setStyle("-fx-margin: 20px;");

        return imageView;
    }

    public void handleMenuSelection(String menuItem) {
        switch (menuItem) {
            case "Profile":
                getRoot().setCenter(new DoctorProfileView(this).getView());
                break;
            case "Dashboard":
                getRoot().setCenter(new DoctorDailyScheduleView(this).getView());
                break;
            case "Schedule":
                getRoot().setCenter(new DoctorScheduleView(this).getView());
                break;
            case "Patient List":
                getRoot().setCenter(new DoctorPatientListView(this).getView());
                break;
            case "Requested Appointments":
                getRoot().setCenter(new DoctorRequestedAppointmentsView(this).getView());
                break;
            case "Logout":
                handleLogout();
                break;
        }
    }

    public ObservableList<Appointment> getDailyAppointments() {
        List<Appointment> appointments = getDrDailyAppointments(drId);
        return FXCollections.observableArrayList(appointments);
    }

    public List<Note> getPatientNotes(String patientId) {
        return getPtNotes(drId, patientId);
    }

    public Map<String, List<String>> getPatientPrescriptions(String patientId) {
        return getPtPrescriptions(drId, patientId);
    }

    public void showNoteDialog(Patient patient) {
        VBox noteBox = new VBox(10);

        Label title = new Label("Add a note for " + patient.getName());
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextArea noteText = new TextArea();
        noteText.setPromptText("Enter your notes here...");

        Button saveBtn = new Button("Save Note");
        saveBtn.setOnAction(e -> {
            saveDoctorNote(patient.getPatientId(), noteText.getText());
            noteText.clear();
        });

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> getRoot().setCenter(new DoctorPatientListView(this).getView()));

        noteBox.getChildren().addAll(title, noteText, saveBtn, backBtn);
        noteBox.setStyle("-fx-padding: 10px;");
        getRoot().setCenter(noteBox);
    }

    public void showPrescriptionDialog(Patient patient) {
        VBox prescriptionBox = new VBox(10);

        Label title = new Label("Add Prescription for " + patient.getName());
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextArea medicationText = new TextArea();
        medicationText.setPromptText("Enter medications, separated by commas");

        Button saveBtn = new Button("Save Prescription");
        saveBtn.setOnAction(e -> {
            List<String> medications = List.of(medicationText.getText().split("\\s*,\\s*"));
            addPrescription(patient.getPatientId(), medications);
            medicationText.clear();
        });

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> getRoot().setCenter(new DoctorPatientListView(this).getView()));

        prescriptionBox.getChildren().addAll(title, medicationText, saveBtn, backBtn);
        prescriptionBox.setStyle("-fx-padding: 10px;");
        getRoot().setCenter(prescriptionBox);
    }

    public void showPatientHistory(Patient patient) {
        VBox historyBox = new VBox(10);

        Label notesTitle = new Label("Medical Notes:");
        notesTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        VBox notesContainer = new VBox(5);
        List<Note> notes = getPatientNotes(patient.getPatientId());
        if (notes.isEmpty()) {
            notesContainer.getChildren().add(new Label("No notes available"));
        } else {
            notes.forEach(note -> notesContainer.getChildren().add(
                    new Label(note.getCreatedAt() + ": " + note.getText())
            ));
        }

        Label prescriptionsTitle = new Label("Prescriptions:");
        prescriptionsTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        VBox prescriptionsContainer = new VBox(5);
        Map<String, List<String>> prescriptions = getPatientPrescriptions(patient.getPatientId());
        if (prescriptions.isEmpty()) {
            prescriptionsContainer.getChildren().add(new Label("No prescriptions available"));
        } else {
            prescriptions.forEach((date, meds) -> {
                prescriptionsContainer.getChildren().add(new Label(date + ":"));
                meds.forEach(med -> prescriptionsContainer.getChildren().add(
                        new Label("  • " + med)
                ));
            });
        }

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> getRoot().setCenter(new DoctorPatientListView(this).getView()));

        historyBox.getChildren().addAll(
                new Label("Medical History for " + patient.getName()),
                notesTitle,
                notesContainer,
                prescriptionsTitle,
                prescriptionsContainer,
                backBtn
        );

        ScrollPane scrollPane = new ScrollPane(historyBox);
        scrollPane.setFitToWidth(true);
        getRoot().setCenter(scrollPane);
    }

    public void saveDoctorNote(String patientId, String note) {
        if (note != null && !note.trim().isEmpty()) {
            saveDoctorNote(drId, patientId, note);
        }
    }

    public void addPrescription(String patientId, List<String> medications) {
        if (medications != null && !medications.isEmpty()) {
            addPrescription(drId, patientId, medications);
        }
    }

    public void handleLogout() {
        LoginController loginController = new LoginController();
        loginController.showLoginView(stage);
    }

    public boolean updateDrPassword(String oldPassword, String newPassword) {
        return updateDoctorPassword(drId, oldPassword, newPassword);
    }

    public void updateHeaderView() {
        getRoot().setTop(new DoctorHeaderView(this).getView());
    }

    public ObservableList<Appointment> getDoctorAppointments() {
        return FXCollections.observableArrayList(getDrAppointments(drId));
    }

    public void confirmAndDeleteAppointment(Appointment appointment) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Confirm Deletion");
        dialog.setHeaderText("Enter your password to confirm deletion:");
        dialog.setContentText("Password:");

        dialog.showAndWait().ifPresent(password -> {
            boolean success = deleteAppointmentAsDoctor(
                    appointment.getApptId(),
                    drId,
                    password
            );
            showAlert(success ? "Appointment deleted successfully!"
                    : "Incorrect password. Deletion failed.");
        });
    }

    private ObservableList<Appointment> getDrDailyAppointments(String doctorId) {
        String url = BASE_URL + "/appointments/daily/" + doctorId;

        String response = ApiClient.executeAPI("GET", url, "");

        if (response != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                List<Map<String, Object>> data = objectMapper.readValue(response, new TypeReference<>() {});

                List<Appointment> appointments = new ArrayList<>();
                for (Map<String, Object> item : data) {
                    int apptId = (int) item.get("apptId");
                    LocalDate date = LocalDate.parse((String) item.get("date"));
                    String time = (String) item.get("time");
                    String name = (String) item.get("name");
                    String department = (String) item.get("extraInfo");

                    appointments.add(new Appointment(apptId, date, time, name, department));
                }

                return FXCollections.observableArrayList(appointments);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return FXCollections.observableArrayList();
    }

    public String[] getDrInfo(String doctorId) {
        String url = BASE_URL + "/doctors/" + doctorId + "/info";
        String response = ApiClient.executeAPI("GET", url, null);

        if (response != null && !response.isEmpty()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> doctorInfo = objectMapper.readValue(response, new TypeReference<>() {});

                return new String[]{
                        doctorInfo.get("username"),
                        doctorInfo.get("email"),
                        doctorInfo.get("phonenb"),
                        doctorInfo.get("gender"),
                        doctorInfo.get("department_name")
                };
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Doctor info not found or empty response.");
        }

        return new String[0];
    }

    public byte[] getDoctorProfilePhoto(String doctorId) {
        String url = BASE_URL + "/doctors/" + doctorId + "/profilephoto";
        return ApiClient.executeAPIForBytes("GET", url);
    }

    public byte[] getPatientProfilePhoto(String patientId) {
        String url = BASE_URL + "/patients/" + patientId + "/profilephoto";
        return ApiClient.executeAPIForBytes("GET", url);
    }

    public String getPatientsForDr(String doctorId) {
        String url = BASE_URL + "/patients/doctor/" + doctorId;
        return ApiClient.executeAPI("GET", url, "");
    }

    public void saveDoctorNote(String doctorId, String patientId, String note) {
        String url = BASE_URL + "/doctornotes/save";

        String requestParams = "doctorId=" + URLEncoder.encode(doctorId, StandardCharsets.UTF_8) +
                "&patientId=" + URLEncoder.encode(patientId, StandardCharsets.UTF_8) +
                "&note=" + URLEncoder.encode(note, StandardCharsets.UTF_8);

        String response = ApiClient.executeAPI("POST", url + "?" + requestParams, "");

        if (response != null && response.equals("Note saved successfully.")) {
            System.out.println("Note saved successfully.");
        } else {
            System.out.println("Failed to save note. Response: " + response);
        }

    }

    public void addPrescription(String doctorId, String patientId, List<String> medications) {
        String url = BASE_URL + "/prescriptions/add";

        StringBuilder requestParams = new StringBuilder();
        requestParams.append("doctorId=").append(URLEncoder.encode(doctorId, StandardCharsets.UTF_8))
                .append("&patientId=").append(URLEncoder.encode(patientId, StandardCharsets.UTF_8));

        String medicationsStr = String.join(",", medications);
        requestParams.append("&medications=").append(URLEncoder.encode(medicationsStr, StandardCharsets.UTF_8));

        String response = ApiClient.executeAPI("POST", url + "?" + requestParams.toString(), "");

        if (response != null && response.equals("Prescription added successfully.")) {
            System.out.println("Prescription added successfully.");
        } else {
            System.out.println("Failed to add prescription. Response: " + response);
        }

    }

    public List<Note> getPtNotes(String doctorId, String patientId) {
        String url = BASE_URL + "/doctornotes/getptnotes";
        String requestParams = "doctorId=" + URLEncoder.encode(doctorId, StandardCharsets.UTF_8) +
                "&patientId=" + URLEncoder.encode(patientId, StandardCharsets.UTF_8);

        String response = ApiClient.executeAPI("GET", url + "?" + requestParams, "");

        if (response != null) {

            try {
                JSONArray jsonArray = new JSONArray(response);
                List<Note> notes = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject noteData = jsonArray.getJSONObject(i);
                    String createdAt = noteData.getString("createdAt");
                    String text = noteData.getString("note");

                    Note note = new Note(createdAt, text);
                    notes.add(note);
                }
                return notes;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return Collections.emptyList();
    }

    public Map<String, List<String>> getPtPrescriptions(String doctorId, String patientId) {
        String url = BASE_URL + "/prescriptions/getbydoctorpatient";

        String requestParams = "doctorId=" + URLEncoder.encode(doctorId, StandardCharsets.UTF_8) +
                "&patientId=" + URLEncoder.encode(patientId, StandardCharsets.UTF_8);

        String response = ApiClient.executeAPI("GET", url + "?" + requestParams, "");

        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                Map<String, List<String>> prescriptionsMap = new HashMap<>();

                for (String key : jsonObject.keySet()) {
                    JSONArray jsonArray = jsonObject.getJSONArray(key);
                    List<String> values = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        values.add(jsonArray.getString(i));
                    }

                    prescriptionsMap.put(key, values);
                }

                return prescriptionsMap;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return Collections.emptyMap();
    }

    public boolean updateDrProfile(String id, String email, String phone, byte[] imageData) {
        String url = BASE_URL + "/doctors/" + id + "/updateprofile";
        return ApiClient.executeMultipartAPI(url, email, phone, imageData);
    }

    public boolean updateDoctorPassword(String doctorId, String oldPassword, String newPassword) {
        String url = BASE_URL + "/doctors/" + doctorId + "/updatepassword";
        String form = "oldPassword=" + URLEncoder.encode(oldPassword, StandardCharsets.UTF_8) +
                "&newPassword=" + URLEncoder.encode(newPassword, StandardCharsets.UTF_8);

        String response = ApiClient.executeAPI("PUT", url, form);
        return response != null ;
    }

    public List<Appointment> getRequestedAppointments(String doctorId) {
        String url = BASE_URL + "/appointmentrequests/requested?doctorId=" + URLEncoder.encode(doctorId, StandardCharsets.UTF_8);
        String response = ApiClient.executeAPI("GET", url, null);

        List<Appointment> appointments = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<Map<String, Object>> list = objectMapper.readValue(response, new TypeReference<List<Map<String, Object>>>() {});

            for (Map<String, Object> map : list) {
                int requestId = (Integer) map.get("requestId");
                String requestedDateStr = (String) map.get("requestedDate");
                String requestedTimeStr = (String) map.get("requestedTime");
                String patientName = null;

                if (map.get("username") != null) {
                    patientName = (String) map.get("username");
                }

                if (patientName == null) {
                    patientName = "Unknown";
                }

                LocalDate requestedDate = LocalDate.parse(requestedDateStr);

                appointments.add(new Appointment(requestId, requestedDate, requestedTimeStr, patientName));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return appointments;
    }

    public boolean acceptAppointment(int requestId) {
        String url = BASE_URL + "/appointmentrequests/accept/" + requestId;
        String response = ApiClient.executeAPI("PUT", url, "");
        return response != null && !response.isEmpty();
    }

    public boolean rejectAppointment(int requestId) {
        String url = BASE_URL + "/appointmentrequests/reject/" + requestId;
        String response = ApiClient.executeAPI("PUT", url, "");
        return response != null && !response.isEmpty();
    }

    public ObservableList<Appointment> getDrAppointments(String doctorId) {
        String url = BASE_URL + "/appointments/doctor/" + doctorId;

        try {
            String responseBody = ApiClient.executeAPI("GET",url, "");

            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> responseList = objectMapper.readValue(responseBody, new TypeReference<List<Map<String, Object>>>() {
            });

            ObservableList<Appointment> appointments = FXCollections.observableArrayList();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (Map<String, Object> appointmentData : responseList) {
                int apptId = (int) appointmentData.get("apptId");
                String dateString = (String) appointmentData.get("date");
                String time = (String) appointmentData.get("time");
                String patientName = (String) appointmentData.get("patientName");
                Object ageObj = appointmentData.get("patientAge");
                String patientAge = (ageObj != null) ? String.valueOf(ageObj) : "N/A";

                LocalDate date = LocalDate.parse(dateString, formatter);
                Appointment appointment = new Appointment(apptId, date, time, patientName, patientAge);
                appointments.add(appointment);
            }

            return appointments;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return FXCollections.observableArrayList();
    }

    public boolean deleteAppointmentAsDoctor(int appointmentId, String doctorId, String password) {
        try {
            String url = BASE_URL + "/appointments/delete";

            String params = "appointmentId=" + URLEncoder.encode(String.valueOf(appointmentId), StandardCharsets.UTF_8)
                    + "&userId=" + URLEncoder.encode(doctorId, StandardCharsets.UTF_8)
                    + "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8);

            String response = ApiClient.executeAPI("DELETE", url, params);

            if (response != null && response.contains("successfully")) {
                System.out.println("Doctor appointment deleted successfully.");
                return true;
            } else {
                System.out.println("Failed to delete appointment for doctor: " + response);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
