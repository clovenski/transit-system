import java.util.ArrayList;

class ScheduleEditor {
    private UI ui;
    private ArrayList<String> options;

    public ScheduleEditor(UI ui) {
        this.ui = ui;
        options = new ArrayList<String>();
        options.add("Add a set of trip offerings"); // 0
        options.add("Delete a trip offering"); // 1
        options.add("Change driver for a trip offering"); // 2
        options.add("Change bus for a trip offering"); // 3
        options.add("Return to Main Menu"); // 4
    }

    public void run() {
        boolean done = false;

        while (!done) {
            ui.printMenuHeader("> Main Menu > Edit Schedule");
            ui.printMenu(options);
            switch (ui.getUserMenuChoice(options.size())) {
            case 0:
                addOfferings();
                break;
            case 1:
                deleteOffering();
                break;
            case 2:
                changeDriver();
                break;
            case 3:
                changeBus();
                break;
            case 4:
                done = true;
                break;
            }
        }
    }

    private void addOfferings() {
        if (!DBInterface.tripsExist()) {
            ui.printError("No trips exist, cannot add any offerings");
            return;
        } else if (!DBInterface.driversExist()) {
            ui.printError("No drivers exist, cannot add any offerings");
            return;
        } else if (!DBInterface.busesExist()) {
            ui.printError("No buses exist, cannot add any offerings");
            return;
        }

        boolean done = false;
        int tripNum, busID, offeringsAdded = 0;
        String date, startTime, arrivalTime, driverName;

        while (!done) {
            ui.printMenuHeader("> Main Menu > Edit Schedule > Add Offerings");
            tripNum = ui.getUserIntInput("Enter the trip number");
            if (!DBInterface.containsTrip(tripNum)) {
                ui.printError("Given trip number does not exist");
                continue;
            }
            date = ui.getUserStringInput("Enter the date of the trip offering");
            startTime = ui.getUserStringInput("Enter the scheduled start time");
            arrivalTime = ui.getUserStringInput("Enter the scheduled arrival time");
            driverName = ui.getUserStringInput("Enter the driver's name");
            if (!DBInterface.containsDriver(driverName)) {
                ui.printError("Given driver does not exist");
                continue;
            }
            busID = ui.getUserIntInput("Enter the bus ID");
            if (!DBInterface.containsBus(busID)) {
                ui.printError("Given bus ID does not exist");
                continue;
            }
            if (DBInterface.addOffering(tripNum, date, startTime, arrivalTime, driverName, busID)) {
                ui.printMsg("Successfully added offering");
                offeringsAdded++;
            } else {
                ui.printError("Could not add offering, may already exist");
            }
            if (ui.getUserIntInput("Enter 0 to add more, or any other number to exit") != 0) {
                done = true;
            }
        }

        if (offeringsAdded > 0) {
            ui.printMsg("Added " + offeringsAdded + " offerings in total");
        }
    }

    private void deleteOffering() {
    }

    private void changeDriver() {
        if (!DBInterface.tripsExist()) {
            ui.printError("No trips exist, cannot possibly update an offering");
            return;
        } else if (!DBInterface.driversExist()) {
            ui.printError("No drivers exist, cannot possibly update an offering");
            return;
        } else  if (!DBInterface.busesExist()) {
            ui.printError("No buses exist, cannot possibly update an offering");
            return;
        } else if (DBInterface.countDrivers() < 2) {
            ui.printError("Only one driver exists, cannot possibly update the driver");
            return;
        }

        boolean done = false;
        int tripNum;
        String date, startTime, newDriverName;

        while (!done) {
            ui.printMenuHeader("> Main Menu > Edit Schedule > Change Driver");
            tripNum = ui.getUserIntInput("Enter the trip number");
            if (!DBInterface.containsTrip(tripNum)) {
                ui.printError("Given trip number does not exist");
                continue;
            }
            date = ui.getUserStringInput("Enter the offering date");
            startTime = ui.getUserStringInput("Enter the scheduled start time");
            newDriverName = ui.getUserStringInput("Enter the name of the new driver");
            if (!DBInterface.containsDriver(newDriverName)) {
                ui.printError("Given driver name does not exist");
                continue;
            }
            if (DBInterface.updateDriver(tripNum, date, startTime, newDriverName)) {
                ui.printMsg("Successfully updated driver");
            } else {
                ui.printError("Could not update driver, offering may not exist");
            }
            done = true;
        }
    }

    private void changeBus() {
        if (!DBInterface.tripsExist()) {
            ui.printError("No trips exist, cannot possibly update an offering");
            return;
        } else if (!DBInterface.driversExist()) {
            ui.printError("No drivers exist, cannot possibly update an offering");
            return;
        } else  if (!DBInterface.busesExist()) {
            ui.printError("No buses exist, cannot possibly update an offering");
            return;
        } else if (DBInterface.countBuses() < 2) {
            ui.printError("Only one bus exists, cannot possibly update the bus");
            return;
        }

        boolean done = false;
        int tripNum, newBusID;
        String date, startTime;

        while (!done) {
            ui.printMenuHeader("> Main Menu > Edit Schedule > Change Bus");
            tripNum = ui.getUserIntInput("Enter the trip number");
            if (!DBInterface.containsTrip(tripNum)) {
                ui.printError("Given trip number does not exist");
                continue;
            }
            date = ui.getUserStringInput("Enter the offering date");
            startTime = ui.getUserStringInput("Enter the scheduled start time");
            newBusID = ui.getUserIntInput("Enter the ID of the new bus");
            if (!DBInterface.containsBus(newBusID)) {
                ui.printError("Given bus ID does not exist");
                continue;
            }
            if (DBInterface.updateBus(tripNum, date, startTime, newBusID)) {
                ui.printMsg("Successfully updated bus");
            } else {
                ui.printError("Could not update bus, offering may not exist");
            }
            done = true;
        }
    }
}