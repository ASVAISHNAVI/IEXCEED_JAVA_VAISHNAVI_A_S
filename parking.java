import java.time.LocalDateTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class parking {

    // Interface
    interface Billable {
        double releaseCustomer(LocalDateTime out);
    }

    // Abstract Class
    static abstract class ParkingSlot implements Billable {
        private int slotId;
        private String category;
        private boolean isOccupied;
        private String customerName;
        private String vehicleType;
        private LocalDateTime checkIn;
        private LocalDateTime checkOut;

        public ParkingSlot(int slotId, String category) {
            this.slotId = slotId;
            this.category = category;
            this.isOccupied = false;
        }

        public boolean isAvailable() {
            return !isOccupied;
        }

        // Method Overloading
        public void assignCustomer(String name, LocalDateTime in) {
            assignCustomer(name, "unknown", in);
        }

        public void assignCustomer(String name, String vehicleType, LocalDateTime in) {
            this.customerName = name;
            this.vehicleType = vehicleType;
            this.checkIn = in;
            this.isOccupied = true;
        }

        // Encapsulation: Getters
        public String getCategory() {
            return category;
        }

        public String getStatus() {
            return "Slot " + slotId + " [" + category + "] - " + (isOccupied ? "Occupied" : "Available");
        }

        // Abstract Method
        public abstract double getRate();

        // Dynamic Dispatch via Interface
        public double releaseCustomer(LocalDateTime out) {
            this.checkOut = out;
            long minutes = Duration.between(checkIn, checkOut).toMinutes();
            double hours = Math.ceil(minutes / 60.0);
            double rate = getRate();
            double amount = hours * rate;

            System.out.println("\n--- Customer Receipt ---");
            System.out.println("Customer: " + customerName);
            System.out.println("Vehicle: " + vehicleType + " | Category: " + category);
            System.out.println("Slot ID: " + slotId);
            System.out.println("Duration: " + minutes + " minutes");
            System.out.println("Amount Due: " + amount);

            // Reset slot
            this.customerName = null;
            this.vehicleType = null;
            this.checkIn = null;
            this.checkOut = null;
            this.isOccupied = false;

            return amount;
        }
    }

    // Subclasses using Inheritance + Overriding
    static class RegularSlot extends ParkingSlot {
        public RegularSlot(int id) {
            super(id, "regular");
        }

        @Override
        public double getRate() {
            return super.vehicleType.equalsIgnoreCase("2 wheeler") ? 100 : 200;
        }
    }

    static class EVSlot extends ParkingSlot {
        public EVSlot(int id) {
            super(id, "ev");
        }

        @Override
        public double getRate() {
            return super.vehicleType.equalsIgnoreCase("2 wheeler") ? 150 : 300;
        }
    }

    static class VIPSlot extends ParkingSlot {
        public VIPSlot(int id) {
            super(id, "vip");
        }

        @Override
        public double getRate() {
            return 500;
        }
    }

    // Has-a Relationship
    static class MallParkingManager {
        private List<ParkingSlot> slots = new ArrayList<>();

        public MallParkingManager() {
            for (int i = 1; i <= 5; i++) {
                slots.add(new RegularSlot(i));
                slots.add(new EVSlot(i + 5));
                slots.add(new VIPSlot(i + 10));
            }

            // Anonymous Class Example
            slots.add(new ParkingSlot(99, "custom") {
                @Override
                public double getRate() {
                    return 999;
                }
            });
        }

        public ParkingSlot allocateSlot(String category, String customerName, String vehicleType, LocalDateTime in) {
            for (ParkingSlot slot : slots) {
                if (slot.isAvailable() && slot.getCategory().equalsIgnoreCase(category)) {
                    slot.assignCustomer(customerName, vehicleType, in);
                    System.out.println("Slot " + slot.getStatus() + " assigned to " + customerName);
                    return slot;
                }
            }
            System.out.println("No available slots in category: " + category);
            return null;
        }

        public void releaseSlot(ParkingSlot slot, LocalDateTime out) {
            slot.releaseCustomer(out);
        }

        public void printSlotStatus() {
            SlotStatusPrinter.print(slots);
        }

        // Nested Class
        static class SlotStatusPrinter {
            public static void print(List<ParkingSlot> slots) {
                System.out.println("\n--- Mall Parking Slot Status ---");
                for (ParkingSlot slot : slots) {
                    System.out.println(slot.getStatus());
                }
            }
        }
    }

    public static void main(String[] args) {
        var manager = new MallParkingManager(); // var keyword

        var inTime = LocalDateTime.of(2025, 8, 12, 9, 0);
        var slot1 = manager.allocateSlot("ev", "Raj", "2 wheeler", inTime);
        var slot2 = manager.allocateSlot("vip", "Rani", "4 wheeler", inTime);
        var slot3 = manager.allocateSlot("regular", "Ravi", "4 wheeler", inTime);
        var slot4 = manager.allocateSlot("vip", "vaish", "4 wheeler", inTime);
        manager.printSlotStatus();

        var outTime = LocalDateTime.of(2025, 8, 12, 11, 30);
        if (slot1 != null) manager.releaseSlot(slot1, outTime);
        if (slot2 != null) manager.releaseSlot(slot2, outTime);
        if (slot3 != null) manager.releaseSlot(slot3, outTime);
        if (slot4 != null) manager.releaseSlot(slot4, outTime);

        manager.printSlotStatus();
    }
}
