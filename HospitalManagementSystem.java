import java.io.*;
import java.util.*;

// Doctor class
class Doctor {
    int id;
    String name;
    String specialization;

    Doctor(int id, String name, String specialization) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return id + ". " + name + " (" + specialization + ")";
    }
}

// Appointment class
class Appointment {
    String patientName;
    Doctor doctor;
    double fee;

    Appointment(String patientName, Doctor doctor, double fee) {
        this.patientName = patientName;
        this.doctor = doctor;
        this.fee = fee;
    }

    @Override
    public String toString() {
        return "Patient: " + patientName + " | Doctor: " + doctor.name + " | Fee: " + fee;
    }
}

// Thread class to simulate appointment confirmation
class AppointmentThread extends Thread {
    Appointment appointment;

    AppointmentThread(Appointment appointment) {
        this.appointment = appointment;
    }

    public void run() {
        try {
            System.out.println("Processing appointment for " + appointment.patientName + "...");
            Thread.sleep(2000); // simulate delay
            System.out.println("Appointment confirmed with Dr. " + appointment.doctor.name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// Main Hospital Management System
public class HospitalManagementSystem {
    static List<Doctor> doctors = new ArrayList<>();
    static List<Appointment> appointments = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        // Adding doctors
        doctors.add(new Doctor(1, "Dr. Sita", "Cardiologist"));
        doctors.add(new Doctor(2, "Dr. gita", "Dermatologist"));
        doctors.add(new Doctor(3, "Dr. rita", "Pediatrician"));
        doctors.add(new Doctor(4, "Dr. raj", "Cardiologist"));
        doctors.add(new Doctor(5, "Dr. raju", "Dermatologist"));
        doctors.add(new Doctor(6, "Dr. rani", "Pediatrician"));

        int choice;
        do {
            System.out.println("\n===== Hospital Management System =====");
            System.out.println("1. Show Doctors");
            System.out.println("2. Book Appointment");
            System.out.println("3. Display Appointments");
            System.out.println("4. Generate Billing");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> showDoctors();
                case 2 -> bookAppointment();
                case 3 -> displayAppointments();
                case 4 -> generateBilling();
                case 5 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid choice!");
            }
        } while (choice != 5);
    }

    // Show all doctors
    static void showDoctors() {
        System.out.println("\n--- Available Doctors ---");
        for (Doctor d : doctors) {
            System.out.println(d);
        }
    }

    // Book an appointment
    static void bookAppointment() {
        System.out.print("Enter Patient Name: ");
        String patientName = sc.nextLine();

        showDoctors();
        System.out.print("Choose Doctor ID: ");
        int docId = sc.nextInt();
        sc.nextLine();

        Doctor selectedDoctor = null;
        for (Doctor d : doctors) {
            if (d.id == docId) {
                selectedDoctor = d;
                break;
            }
        }

        if (selectedDoctor == null) {
            System.out.println("Invalid Doctor ID!");
            return;
        }

        double fee = 500; // fixed consultation fee
        Appointment appointment = new Appointment(patientName, selectedDoctor, fee);
        appointments.add(appointment);

        // Start thread to simulate booking process
        new AppointmentThread(appointment).start();

        // Save to file
        try (FileOutputStream fos = new FileOutputStream("appointments.txt", true)) {
            String data = appointment.toString() + "\n";
            fos.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Display appointments
    static void displayAppointments() {
        System.out.println("\n--- Appointments ---");
        if (appointments.isEmpty()) {
            System.out.println("No appointments booked.");
        } else {
            for (Appointment a : appointments) {
                System.out.println(a);
            }
        }
    }

    // Generate billing
    static void generateBilling() {
        System.out.print("Enter Patient Name for billing: ");
        String patientName = sc.nextLine();

        Appointment found = null;
        for (Appointment a : appointments) {
            if (a.patientName.equalsIgnoreCase(patientName)) {
                found = a;
                break;
            }
        }

        if (found == null) {
            System.out.println("No appointment found for this patient!");
            return;
        }

        System.out.println("\n--- Billing Receipt ---");
        System.out.println("Patient: " + found.patientName);
        System.out.println("Doctor: " + found.doctor.name);
        System.out.println("Specialization: " + found.doctor.specialization);
        System.out.println("Consultation Fee: Rs. " + found.fee);
        System.out.println("Total: Rs. " + found.fee);

        // Save bill to file
        try (FileOutputStream fos = new FileOutputStream("billing.txt", true)) {
            String data = "BILL - " + found.toString() + "\n";
            fos.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
