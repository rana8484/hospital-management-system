package com.example.hospitalmanagementsystemfinalproject.service;

import com.example.hospitalmanagementsystemfinalproject.model.Appointment;
import com.example.hospitalmanagementsystemfinalproject.model.AppointmentRequest;
import com.example.hospitalmanagementsystemfinalproject.model.Doctor;
import com.example.hospitalmanagementsystemfinalproject.model.Patient;
import com.example.hospitalmanagementsystemfinalproject.repository.AppointmentRepository;
import com.example.hospitalmanagementsystemfinalproject.repository.AppointmentRequestRepository;
import com.example.hospitalmanagementsystemfinalproject.repository.DoctorRepository;
import com.example.hospitalmanagementsystemfinalproject.repository.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentRequestService {

    private final AppointmentRequestRepository appointmentRequestRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public AppointmentRequestService(AppointmentRequestRepository appointmentRequestRepository,
                                     AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.appointmentRequestRepository = appointmentRequestRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    public List<AppointmentRequest> getRequestedAppointments(String doctorId) {
        List<Object[]> rows = appointmentRequestRepository.findPendingRequestsByDoctorId(doctorId);
        List<AppointmentRequest> requests = new ArrayList<>();

        for (Object[] row : rows) {
            int requestId = ((Number) row[0]).intValue();
            LocalDate date = LocalDate.parse(row[1].toString());
            LocalTime time = LocalTime.parse(row[2].toString());
            String patientUsername = row[3].toString();

            requests.add(new AppointmentRequest(requestId, date, time, patientUsername));
        }

        return requests;
    }

    @Transactional
    public boolean acceptAppointment(int requestId) {
        Optional<AppointmentRequest> optionalRequest = appointmentRequestRepository.findById(requestId);

        if (optionalRequest.isPresent()) {
            AppointmentRequest request = optionalRequest.get();

            if (request.getStatus() != AppointmentRequest.RequestStatus.PENDING) {
                return false;
            }

            Appointment appointment = new Appointment();
            appointment.setDoctor(request.getDoctor());
            appointment.setPatient(request.getPatient());
            appointment.setDate(request.getRequestedDate());
            appointment.setTime(request.getRequestedTime());

            appointmentRepository.save(appointment);

            request.setStatus(AppointmentRequest.RequestStatus.ACCEPTED);
            appointmentRequestRepository.save(request);

            return true;
        }

        return false;
    }

    @Transactional
    public boolean rejectAppointment(int requestId) {
        Optional<AppointmentRequest> optionalRequest = appointmentRequestRepository.findById(requestId);

        if (optionalRequest.isPresent()) {
            AppointmentRequest request = optionalRequest.get();

            if (request.getStatus() == AppointmentRequest.RequestStatus.PENDING) {
                request.setStatus(AppointmentRequest.RequestStatus.REJECTED);
                appointmentRequestRepository.save(request);
                return true;
            }
        }

        return false;
    }

    @Transactional
    public boolean requestAppointment(String doctorId, LocalDate date, LocalTime time, String patientId) {
        int conflicts = appointmentRequestRepository.countConflicts(doctorId, patientId, date, time);

        if (conflicts > 0) {
            return false;
        }

        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        Optional<Patient> patientOpt = patientRepository.findById(patientId);

        if (doctorOpt.isPresent() && patientOpt.isPresent()) {
            AppointmentRequest request = new AppointmentRequest(
                    doctorOpt.get(),
                    patientOpt.get(),
                    date,
                    time,
                    AppointmentRequest.RequestStatus.PENDING
            );
            appointmentRequestRepository.save(request);
            return true;
        }

        return false;
    }

}
