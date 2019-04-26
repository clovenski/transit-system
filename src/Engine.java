import java.util.ArrayList;
import java.util.Scanner;

class Engine {
    private ScheduleEditor scheduleEditor;
    private ArrayList<String> mainMenuOpts;
    private UI ui;
    private Scanner scanner;

    public Engine() {
        scanner = new Scanner(System.in);
        ui = new UI(scanner);
        scheduleEditor = new ScheduleEditor(ui);
        mainMenuOpts = new ArrayList<String>();
        mainMenuOpts.add("Display schedule of a trip"); // 0
        mainMenuOpts.add("Edit a schedule"); // 1
        mainMenuOpts.add("Display stops of a trip"); // 2
        mainMenuOpts.add("Display schedule of a driver by date"); // 3
        mainMenuOpts.add("Add a driver"); // 4
        mainMenuOpts.add("Add a bus"); // 5
        mainMenuOpts.add("Add a stop"); // 6
        mainMenuOpts.add("Add a trip"); // 7
        mainMenuOpts.add("Add full info to a current trip"); // 8
        mainMenuOpts.add("Add trip-stop info"); // 9
        mainMenuOpts.add("Delete a driver"); // 10
        mainMenuOpts.add("Delete a bus"); // 11
        mainMenuOpts.add("Delete a stop"); // 12
        mainMenuOpts.add("Delete a trip offering"); // 13
        mainMenuOpts.add("Exit"); // 14
    }

    public void run() {
        boolean done = false;

        ui.printStartMsg();
        while (!done) {
            ui.printMenuHeader("> Main Menu");
            ui.printMenu(mainMenuOpts);
            switch (ui.getUserMenuChoice(mainMenuOpts.size())) {
            case 0:
                displayTripSchedule();
                break;
            case 1:
                editSchedule();
                break;
            case 2:
                displayStops();
                break;
            case 3:
                displayDriverSchedule();
                break;
            case 4:
                addDriver();
                break;
            case 5:
                addBus();
                break;
            case 6:
                addStop();
                break;
            case 7:
                addTrip();
                break;
            case 8:
                addTripInfo();
                break;
            case 9:
                addTripStopInfo();
                break;
            case 10:
                deleteDriver();
                break;
            case 11:
                deleteBus();
                break;
            case 12:
                deleteStop();
                break;
            case 13:
                deleteTrip();
                break;
            case 14:
                done = true;
                break;
            }
        }
        scanner.close();
    }

    private void displayTripSchedule() {
        // dummy code
        for (String row : DBInterface.getSchedule("test", "testing", "april 24, 2019")) {
            System.out.println(row);
        }
    }

    private void editSchedule() {
        scheduleEditor.run();
    }

    private void displayStops() {
    }

    private void displayDriverSchedule() {
    }

    private void addDriver() {
        String name, phoneNumber;

        ui.printMenuHeader("> Main Menu > Add Driver");
        name = ui.getUserStringInput("Enter the driver's name");
        phoneNumber = ui.getUserStringInput("Enter the phone number");
        if (DBInterface.addDriver(name, phoneNumber)) {
            ui.printMsg("Driver successfully added");
        } else {
            ui.printError("Could not add driver, may already exist");
        }
    }

    private void addBus() {
        int id, year;
        String model;

        ui.printMenuHeader("> Main Menu > Add Bus");
        id = ui.getUserIntInput("Enter the bus ID");
        model = ui.getUserStringInput("Enter the bus model");
        year = ui.getUserIntInput("Enter its year");
        if (DBInterface.addBus(id, model, year)) {
            ui.printMsg("Bus successfully added");
        } else {
            ui.printError("Could not add bus, may already exist");
        }
    }

    private void addStop() {
        int stopNum;
        String address;

        ui.printMenuHeader("> Main Menu > Add Stop");
        stopNum = ui.getUserIntInput("Enter the stop number");
        address = ui.getUserStringInput("Enter the stop address");
        if (DBInterface.addStop(stopNum, address)) {
            ui.printMsg("Stop successfully added");
        } else {
            ui.printError("Could not add stop, may already exist");
        }
    }

    private void addTrip() {
        int tripNum;
        String startLocName, destinationName;

        ui.printMenuHeader("> Main Menu > Add Trip");
        tripNum = ui.getUserIntInput("Enter the trip number");
        startLocName = ui.getUserStringInput("Enter the start location name");
        destinationName = ui.getUserStringInput("Enter the destination name");
        if (DBInterface.addTrip(tripNum, startLocName, destinationName)) {
            ui.printMsg("Trip successfully added");
        } else {
            ui.printError("Could not add trip, may already exist");
        }
    }

    private void addTripInfo() {
        // print menu header: > Main Menu > Add Trip Info
        // print a warning that any existing info will be overwritten
        // prompt user for input for all the needed attributes
        // call DBInterface.addFullTripInfo(...) with the necessary args
        // output success or failure depending on returned value
    }

    private void addTripStopInfo() {
        boolean done = false;
        int tripNum, stopNum, seqNum, drivingTime;

        while (!done) {
            ui.printMenuHeader("> Main Menu > Add Trip-Stop Info");
            tripNum = ui.getUserIntInput("Enter the trip number");
            if (!DBInterface.containsTrip(tripNum)) {
                ui.printError("Given trip number does not exist");
                continue;
            }
            stopNum = ui.getUserIntInput("Enter the stop number");
            if (!DBInterface.containsStop(stopNum)) {
                ui.printError("Given stop number does not exist");
                continue;
            }
            seqNum = ui.getUserIntInput("Enter the sequence number");
            drivingTime = ui.getUserIntInput("Enter the driving time in minutes");
            if (DBInterface.addTripStopInfo(tripNum, stopNum, seqNum, drivingTime)) {
                ui.printMsg("Successfully added trip-stop info");
            } else {
                ui.printError("Could not add trip-stop, may already exist");
            }
            done = true;
        }
    }

    private void deleteDriver() {
    }

    private void deleteBus() {
    }

    private void deleteStop() {
    }

    private void deleteTrip() {
    }
}