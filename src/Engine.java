import java.util.ArrayList;
import java.util.Scanner;

class Engine {
    private DBInterface dbInterface;
    private ArrayList<String> mainMenuOpts;
    private UI ui;
    private Scanner scanner;

    public Engine() {
        scanner = new Scanner(System.in);
        ui = new UI(scanner);
        dbInterface = new DBInterface();
        mainMenuOpts = new ArrayList<String>();
        mainMenuOpts.add("Display schedule of a trip");
        mainMenuOpts.add("Edit a schedule");
        mainMenuOpts.add("Display stops of a trip");
        mainMenuOpts.add("Display schedule of a driver by date");
        mainMenuOpts.add("Add a driver");
        mainMenuOpts.add("Add a bus");
        mainMenuOpts.add("Add a trip offering");
        mainMenuOpts.add("Add full info to a current trip");
        mainMenuOpts.add("Delete a driver");
        mainMenuOpts.add("Delete a bus");
        mainMenuOpts.add("Delete a trip offering");
        mainMenuOpts.add("Exit");
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
                addTrip();
                break;
            case 7:
                addTripInfo();
                break;
            case 8:
                deleteDriver();
                break;
            case 9:
                deleteBus();
                break;
            case 10:
                deleteTrip();
                break;
            case 11:
                done = true;
                break;
            }
        }
        scanner.close();
    }

    private void displayTripSchedule() {
    }

    private void editSchedule() {
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
        ui.println();
        if (dbInterface.addDriver(name, phoneNumber)) {
            ui.println("Driver successfully added");
        } else {
            ui.println("Could not add driver, may already exist");
        }
    }

    private void addBus() {
        int id, year;
        String model;

        ui.printMenuHeader("> Main Menu > Add Bus");
        id = ui.getUserIntInput("Enter the bus ID");
        model = ui.getUserStringInput("Enter the bus model");
        year = ui.getUserIntInput("Enter its year");
        ui.println();
        if (dbInterface.addBus(id, model, year)) {
            ui.println("Bus successfully added");
        } else {
            ui.println("Could not add bus, may already exist");
        }
    }

    private void addTrip() {
        int tripNum;
        String startLocName, destinationName;

        ui.printMenuHeader("> Main Menu > Add Trip");
        tripNum = ui.getUserIntInput("Enter the trip number");
        startLocName = ui.getUserStringInput("Enter the start location name");
        destinationName = ui.getUserStringInput("Enter the destination name");
        ui.println();
        if (dbInterface.addTrip(tripNum, startLocName, destinationName)) {
            ui.println("Trip successfully added");
        } else {
            ui.println("Could not add trip, may already exist");
        }
    }

    private void addTripInfo() {
    }

    private void deleteDriver() {
    }

    private void deleteBus() {
    }

    private void deleteTrip() {
    }
}