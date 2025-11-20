package com.example.hospitalmanagementsystemfrontendfinal.controller;

import com.example.hospitalmanagementsystemfrontendfinal.ApiClient;
import com.example.hospitalmanagementsystemfrontendfinal.model.*;
import com.example.hospitalmanagementsystemfrontendfinal.view.PatientViews.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PatientController {
    private String ptId;
    private PatientMainView mainView;
    private Stage primaryStage;
    private final String BASE_URL = "http://localhost:8080/api";

    public PatientController(String ptId, Stage primaryStage) {
        this.ptId = ptId;
        this.primaryStage = primaryStage;
    }
    public void showPatientView() {
        this.mainView = new PatientMainView(this, ptId);
        Scene scene = new Scene(mainView.getView());
        primaryStage.setScene(scene);
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    public void showProfileView() {
        mainView.getView().setCenter(new PatientProfileView(this).getView());
    }

    public void showDashboard() {
        mainView.getView().setCenter(new PatientDashboardView(this).getView());
    }

    public void showScheduleView() {
        mainView.getView().setCenter(new PatientScheduleView(this).getView());
    }

    public void showDoctorListView() {
        mainView.getView().setCenter(new PatientDoctorListView(this).getView());
    }

    public void showPrescriptionView() {
        mainView.getView().setCenter(new PatientPrescriptionView(this).getView());
    }

    public void showHistoryView() {
        mainView.getView().setCenter(new PatientHistoryView(this).getView());
    }

    public void showAppointmentBookingView(Doctor doctor) {
        VBox bookingView = new VBox(15);
        bookingView.setPadding(new Insets(20));

        Label title = new Label("Book Appointment with Dr. " + doctor.getName());
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        DatePicker datePicker = new DatePicker();
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isBefore(LocalDate.now()));
            }
        });

        ComboBox<String> timeCombo = new ComboBox<>();
        timeCombo.getItems().addAll("09:00", "10:00", "11:00", "13:00", "14:00", "15:00");
        timeCombo.setPromptText("Select time");

        Button submitBtn = new Button("Book Appointment");
        submitBtn.setOnAction(e -> {
            if (datePicker.getValue() == null || timeCombo.getValue() == null) {
                showAlert("Please select both date and time");
                return;
            }

            boolean success = bookAppointment(doctor, datePicker.getValue(), timeCombo.getValue());
            if (success) {
                showAlert("Appointment booked successfully!");
                showDoctorListView();
            }
            else{
                showAlert("Appointment booking failed!");
            }
        });

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> showDoctorListView());

        HBox buttonBox = new HBox(10, submitBtn, backBtn);
        bookingView.getChildren().addAll(title, datePicker, timeCombo, buttonBox);
        mainView.getView().setCenter(bookingView);
    }

    public String[] getPatientInfo() {
            return getPtInfo(ptId);
    }

    public List<Appointment> getAppointments() {
        return getPatientAppointments(ptId);
    }

    public List<Appointment> getDailyAppointments() {
        return getPtDailyAppointments(ptId);
    }

    public List<Appointment> getAllPreviousAppointments() {
        return getAllPreviousAppointments(ptId);
    }

    public Map<String, List<String>> getAllPrescriptions() {
        return getAllPrescriptions(ptId);
    }

    public List<Doctor> getAllDoctors() {
        return getAllDr();
    }

    public void confirmAndDeleteAppointment(Appointment appointment) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Confirm Deletion");
        dialog.setHeaderText("Enter your password to confirm deletion:");
        dialog.setContentText("Password:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(password -> {
            boolean success = deleteAppointmentAsPatient(appointment.getApptId(), ptId, password);
            if (success) {
                showAlert("Appointment deleted successfully!");
                showScheduleView();
            } else {
                showAlert("Incorrect password or deletion failed");
            }
        });
    }

    public boolean updateProfile(String email, String phone, byte[] imageData) {
        return updatePtProfile(ptId, email, phone, imageData);
    }


    public boolean updatePassword(String oldPassword, String newPassword) {
        return updatePatientPassword(ptId, oldPassword, newPassword);
    }

    public boolean bookAppointment(Doctor doctor, LocalDate date, String time) {
        return requestAppointment(doctor.getDoctorId(), date, time, ptId);
    }

    public void refreshUserHeader() {
        mainView.updateUserHeader();
    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public boolean isValidEmail(String email) {
        return email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    public boolean isValidPhoneNumber(String phone) {
        return phone.matches("^\\d{8}$");
    }

    public String getPtId() {
        return ptId;
    }

    private byte[] selectedImageData;

    public void handleChangePhoto(ImageView profileImage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                selectedImageData = Files.readAllBytes(file.toPath());
                profileImage.setImage(new Image(new ByteArrayInputStream(selectedImageData)));
            } catch (IOException ex) {
                showAlert("Error loading image: " + ex.getMessage());
            }
        }
    }

    public void handleUpdateProfile(String email, String phone) {
        if (!isValidEmail(email)) {
            showAlert("Invalid email format!");
            return;
        }

        if (!isValidPhoneNumber(phone)) {
            showAlert("Phone must be 8 digits!");
            return;
        }

        if (updateProfile(email, phone, selectedImageData)) {
            showAlert("Profile updated!");
            refreshUserHeader();
        } else {
            showAlert("Profile update failed.");
        }
    }

    private PatientProfileView currentProfileView;

    public void handleUpdatePassword(String oldPassword, String newPassword) {
        if (oldPassword.isEmpty() || newPassword.isEmpty()) {
            showAlert("Both fields required!");
            return;
        }

        if (updatePassword(oldPassword, newPassword)) {
            showAlert("Password updated!");
            if (currentProfileView != null) {
                currentProfileView.clearPasswordFields();
            }
        } else {
            showAlert("Incorrect old password");
        }
    }

    public void logout() {
        LoginController loginController = new LoginController();
        loginController.showLoginView(primaryStage);
    }

    public ObservableList<Appointment> getPtDailyAppointments(String patientId) {
        String url = BASE_URL + "/appointments/daily/" + patientId;

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
                    String age = (String) item.get("extraInfo");

                    appointments.add(new Appointment(apptId, date, time, name, age));
                }

                return FXCollections.observableArrayList(appointments);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return FXCollections.observableArrayList();
    }

    public String[] getPtInfo(String patientId) {
        String url = BASE_URL + "/patients/" + patientId + "/info";
        String response = ApiClient.executeAPI("GET", url, "");

        if (response != null && !response.isEmpty()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> patientInfo = objectMapper.readValue(response, new TypeReference<>() {});

                return new String[]{
                        patientInfo.get("username"),
                        patientInfo.get("email"),
                        patientInfo.get("phonenb"),
                        patientInfo.get("gender")
                };
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Patient info not found or empty response.");
        }

        return new String[0];
    }

    public byte[] getPatientProfilePhoto(String patientId) {
        String url = BASE_URL + "/patients/" + patientId + "/profilephoto";
        return ApiClient.executeAPIForBytes("GET", url);
    }

    public byte[] getDoctorProfilePhoto(String doctorId) {
        String url = BASE_URL + "/doctors/" + doctorId + "/profilephoto";
        return ApiClient.executeAPIForBytes("GET", url);
    }


    public boolean updatePtProfile(String id, String email, String phone, byte[] imageData) {
        String url = BASE_URL + "/patients/" + id + "/updateprofile";
        return ApiClient.executeMultipartAPI(url, email, phone, imageData);
    }

    public boolean updatePatientPassword(String patientId, String oldPassword, String newPassword) {
        String url = BASE_URL + "/patients/" + patientId + "/updatepassword";
        String form = "oldPassword=" + URLEncoder.encode(oldPassword, StandardCharsets.UTF_8) +
                "&newPassword=" + URLEncoder.encode(newPassword, StandardCharsets.UTF_8);

        String response = ApiClient.executeAPI("PUT", url, form);

        return response != null ;
    }

    public ObservableList<Appointment> getPatientAppointments(String patientId) {
        String url = BASE_URL + "/appointments/patient/" + patientId;

        try {
            String responseBody = ApiClient.executeAPI("GET", url, "");

            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> responseList = objectMapper.readValue(responseBody, new TypeReference<List<Map<String, Object>>>() {});

            ObservableList<Appointment> appointments = FXCollections.observableArrayList();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (Map<String, Object> appointmentData : responseList) {
                int apptId = (int) appointmentData.get("apptId");
                String dateString = (String) appointmentData.get("date");
                String time = (String) appointmentData.get("time");
                String doctorName = (String) appointmentData.get("doctorName");
                String department = (String) appointmentData.get("department");

                LocalDate date = LocalDate.parse(dateString, formatter);
                Appointment appointment = new Appointment(apptId, date, time, doctorName, department);
                appointments.add(appointment);
            }

            return appointments;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return FXCollections.observableArrayList();
    }

    public boolean deleteAppointmentAsPatient(int appointmentId, String patientId, String password) {
        try {
            String url = BASE_URL + "/appointments/delete";

            String params = "appointmentId=" + URLEncoder.encode(String.valueOf(appointmentId), StandardCharsets.UTF_8)
                    + "&userId=" + URLEncoder.encode(patientId, StandardCharsets.UTF_8)
                    + "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8);

            String response = ApiClient.executeAPI("DELETE", url, params);

            if (response != null && response.contains("successfully")) {
                System.out.println("Patient appointment deleted successfully.");
                return true;
            } else {
                System.out.println("Failed to delete appointment for patient: " + response);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ObservableList<Appointment> getAllPreviousAppointments(String patientId) {
        String url = BASE_URL + "/appointments/previous/" + patientId;
        String jsonResponse = ApiClient.executeAPI("GET", url,"");

        if (jsonResponse == null || jsonResponse.isEmpty()) {
            return FXCollections.observableArrayList();
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> data = objectMapper.readValue(jsonResponse, new TypeReference<>() {});
            List<Appointment> appointments = new ArrayList<>();

            for (Map<String, Object> item : data) {
                int apptId = (int) item.get("apptId");
                LocalDate date = LocalDate.parse((String) item.get("date"));
                String time = (String) item.get("time");
                String name = (String) item.get("doctorName");
                String extraInfo = (String) item.get("department");

                appointments.add(new Appointment(apptId, date, time, name, extraInfo));
            }

            return FXCollections.observableArrayList(appointments);

        } catch (IOException e) {
            e.printStackTrace();
            return FXCollections.observableArrayList();
        }
    }

    public Map<String, List<String>> getAllPrescriptions(String patientId) {
        String url = BASE_URL + "/prescriptions/getbypatient";
        String params = "patientId=" + patientId;

        try {
            String jsonResponse = ApiClient.executeAPI("GET", url + "?" + params, "");

            if (jsonResponse != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(jsonResponse, new TypeReference<Map<String, List<String>>>() {});
            } else {
                System.out.println("Failed to fetch prescriptions. Response was null.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new HashMap<>();
    }

    public List<Doctor> getAllDr() {
        String url = BASE_URL + "/doctors";

        try {
            String response = ApiClient.executeAPI("GET", url, "");

            if (response != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                List<Map<String, Object>> doctorList = objectMapper.readValue(response, new TypeReference<>() {});
                List<Doctor> result = new ArrayList<>();

                for (Map<String, Object> item : doctorList) {
                    String name = (String) item.get("name");
                    String phone = (String) item.get("phoneNumber");
                    String email = (String) item.get("email");
                    String doctorId = (String) item.get("doctorId");
                    String gender = (String) item.get("gender");

                    Map<String, Object> departmentMap = (Map<String, Object>) item.get("department");
                    String department = (String) departmentMap.get("departmentName");

                    byte[] imageBytes = null;
                    if (item.get("profilePhoto") != null) {
                        String base64Image = (String) item.get("profilePhoto");
                        imageBytes = Base64.getDecoder().decode(base64Image);
                    } else {
                        imageBytes = getDoctorProfilePhoto(doctorId);
                    }

                    Doctor doctor = new Doctor(imageBytes, name, phone, email, department, doctorId, gender);
                    result.add(doctor);
                }

                return result;
            } else {
                System.out.println("Failed to fetch doctors. Null response from API.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public boolean requestAppointment(String doctorId, LocalDate date, String timeStr, String patientId) {
        String url = BASE_URL + "/appointmentrequests/request" +
                "?doctorId=" + URLEncoder.encode(doctorId, StandardCharsets.UTF_8) +
                "&patientId=" + URLEncoder.encode(patientId, StandardCharsets.UTF_8) +
                "&date=" + URLEncoder.encode(date.toString(), StandardCharsets.UTF_8) +
                "&time=" + URLEncoder.encode(timeStr, StandardCharsets.UTF_8);

        try {
            String response = ApiClient.executeAPI("POST", url, "");

            if (response != null && !response.isEmpty()) {
                System.out.println("Appointment request successful: " + response);
                return true;
            } else {
                System.out.println("Failed to request appointment.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
