// Abstract class for ParkingSlot
abstract class ParkingSlot {
    // Instance variables
    private int slotNumber;
    private boolean isOccupied;

    // Class variable
    static String parkingName = "City Center Parking";

    // Static block
    static {
        System.out.println("Static Block: Welcome to " + parkingName);
    }

    // Constructor
    public ParkingSlot(int slotNumber) {
        this.slotNumber = slotNumber;
        this.isOccupied = false;
    }

    // Abstract method
    public abstract void slotType();

    // Getter and Setter (Encapsulation)
    public int getSlotNumber() {
        return slotNumber;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }
}

// Interface for reservation
interface Reservable {
    void reserve(String customerName);
}

// Regular Slot class (Inheritance - IS-A)
class RegularSlot extends ParkingSlot implements Reservable {
    public RegularSlot(int slotNumber) {
        super(slotNumber);
    }

    @Override
    public void slotType() {
        System.out.println("Regular Parking Slot #" + getSlotNumber());
    }

    @Override
    public void reserve(String customerName) {
        System.out.println("Slot #" + getSlotNumber() + " reserved for " + customerName);
        setOccupied(true);
    }
}

// EV Slot class (Inheritance - IS-A)
class EVSlot extends ParkingSlot implements Reservable {
    public EVSlot(int slotNumber) {
        super(slotNumber);
    }

    @Override
    public void slotType() {
        System.out.println("EV Charging Slot #" + getSlotNumber());
    }

    @Override
    public void reserve(String customerName) {
        System.out.println("EV Slot #" + getSlotNumber() + " reserved for " + customerName + " with charging enabled.");
        setOccupied(true);
    }
}

// VIP Slot class
class VIPSlot extends ParkingSlot {
    public VIPSlot(int slotNumber) {
        super(slotNumber);
    }

    @Override
    public void slotType() {
        System.out.println("VIP Priority Slot #" + getSlotNumber());
    }
}

// Parking Lot (HAS-A relationship)
class ParkingLot {
    // Nested class
    class ValetService {
        void parkVehicle(ParkingSlot slot) {
            if (!slot.isOccupied()) {
                System.out.println("Valet has parked your car in Slot #" + slot.getSlotNumber());
                slot.setOccupied(true);
            } else {
                System.out.println("Slot #" + slot.getSlotNumber() + " is already occupied.");
            }
        }
    }

    // Method overloading
    public void assignSlot(ParkingSlot slot) {
        slot.slotType();
        System.out.println("Slot assigned successfully.");
    }

    public void assignSlot(ParkingSlot slot, String customerName) {
        if (slot instanceof Reservable) {
            ((Reservable) slot).reserve(customerName);
        } else {
            System.out.println("This slot type cannot be reserved.");
        }
    }
}

public class ParkingManagement {
    public static void main(String[] args) {
        var lot = new ParkingLot();

        // Creating slots
        ParkingSlot regular = new RegularSlot(1);
        ParkingSlot ev = new EVSlot(2);
        ParkingSlot vip = new VIPSlot(3);

        // Dynamic method dispatch
        ParkingSlot slotRef;

        slotRef = regular;
        slotRef.slotType();

        slotRef = ev;
        slotRef.slotType();

        slotRef = vip;
        slotRef.slotType();

        // Reservation
        lot.assignSlot(regular, "Alice");
        lot.assignSlot(ev, "Bob");
        lot.assignSlot(vip, "Charlie");

        // Valet Parking using Nested class
        ParkingLot.ValetService valet = lot.new ValetService();
        valet.parkVehicle(vip);

        // Anonymous class for a temporary parking slot
        ParkingSlot tempSlot = new ParkingSlot(99) {
            @Override
            public void slotType() {
                System.out.println("Temporary Event Slot #" + getSlotNumber());
            }
        };
        lot.assignSlot(tempSlot);
    }

}
