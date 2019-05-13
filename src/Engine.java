import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

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
        mainMenuOpts.add("Display trip-stop info"); // 3
        mainMenuOpts.add("Display schedule of a driver by date"); // 4
        mainMenuOpts.add("Add a driver"); // 5
        mainMenuOpts.add("Add a bus"); // 6
        mainMenuOpts.add("Add a stop"); // 7
        mainMenuOpts.add("Add a trip"); // 8
        mainMenuOpts.add("Add full info to a current trip-stop"); // 9
        mainMenuOpts.add("Add trip-stop info"); // 10
        mainMenuOpts.add("Delete a driver"); // 11
        mainMenuOpts.add("Delete a bus"); // 12
        mainMenuOpts.add("Delete a stop"); // 13
        mainMenuOpts.add("Delete a trip"); // 14
        mainMenuOpts.add("Exit"); // 15
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
                displayTripStopFullInfo();
                break;
            case 4:
                displayDriverSchedule();
                break;
            case 5:
                addDriver();
                break;
            case 6:
                addBus();
                break;
            case 7:
                addStop();
                break;
            case 8:
                addTrip();
                break;
            case 9:
                addTripStopFullInfo();
                break;
            case 10:
                addTripStopInfo();
                break;
            case 11:
                deleteDriver();
                break;
            case 12:
                deleteBus();
                break;
            case 13:
                deleteStop();
                break;
            case 14:
                deleteTrip();
                break;
            case 15:
                done = true;
                break;
            }
        }
        scanner.close();
    }

    private void displayTripSchedule() {
        String date, startLoc, destination;
        List<String> schedule;
        StringTokenizer tokenizer;
        String border = "";

        ui.printMenuHeader("> Main Menu > Display Schedule");
        date = ui.getUserStringInput("Enter the date of the trip");
        startLoc = ui.getUserStringInput("Enter the starting location");
        destination = ui.getUserStringInput("Enter the destination");
        schedule = DBInterface.getSchedule(startLoc, destination, date);
        if (schedule.size() > 0) {
            ui.println("");
            ui.println(String.format("%-20s | %-20s | %-20s | %-10s | %-12s | %-20s | %-6s", 
                                     "Date", "Start Location", "Destination", "Start Time",
                                     "Arrival Time", "Driver Name", "Bus ID"));
            for (int i = 0; i < 126; i++) {
                border += "-";
            }
            ui.println(border);
            for (String row : schedule) {
                tokenizer = new StringTokenizer(row, "\t");
                ui.println(String.format("%-20s | %-20s | %-20s | %10s | %12s | %-20s | %6s",
                                         tokenizer.nextToken(),     // date
                                         tokenizer.nextToken(),     // start loc
                                         tokenizer.nextToken(),     // destination
                                         tokenizer.nextToken(),     // start time
                                         tokenizer.nextToken(),     // arrival time
                                         tokenizer.nextToken(),     // driver name
                                         tokenizer.nextToken()));   // bus id
            }
        } else {
            ui.printMsg("Empty schedule");
        }
    }

    private void editSchedule() {
        scheduleEditor.run();
    }

    private void displayStops() {
        int tripNum;
        List<String> tripStopInfo;
        String border = "";
        StringTokenizer tokenizer;

        ui.printMenuHeader("> Main Menu > Display Stops");
        tripNum = ui.getUserIntInput("Enter the trip number");
        tripStopInfo = DBInterface.getTripStopInfo(tripNum);
        if (tripStopInfo.size() > 0) {
            ui.println("");
            ui.println(String.format("%-11s | %-15s | %-12s", "Stop Number", "Sequence Number", "Driving Time"));
            for (int i = 0; i < 44; i++) {
                border += "-";
            }
            ui.println(border);
            for (String row : tripStopInfo) {
                tokenizer = new StringTokenizer(row, "\t");
                ui.println(String.format("%11s | %15s | %12s",
                                         tokenizer.nextToken(),     // stop number
                                         tokenizer.nextToken(),     // seq number
                                         tokenizer.nextToken()));   // driving time
            }
        } else {
            ui.printMsg("Empty trip info");
        }
    }

    private void displayTripStopFullInfo() {
        int tripNum, stopNum;
        String date, startTime, border = "";
        List<String> tripStopFullInfo;
        StringTokenizer tokenizer;

        ui.printMenuHeader("> Main Menu > Display Trip-Stop Info");
        tripNum = ui.getUserIntInput("Enter the trip number");
        date = ui.getUserStringInput("Enter the date");
        startTime = ui.getUserStringInput("Enter the scheduled start time");
        stopNum = ui.getUserIntInput("Enter the stop number");
        tripStopFullInfo = DBInterface.getTripStopFullInfo(tripNum, date, startTime, stopNum);
        if (tripStopFullInfo.size() > 0) {
            ui.println("");
            ui.println(String.format("%-22s | %-17s | %-19s | %-13s | %-14s",
                                     "Scheduled Arrival Time", "Actual Start Time",
                                     "Actual Arrival Time", "Passengers In", "Passengers Out"));
            for (int i = 0; i < 97; i++) {
                border += "-";
            }
            ui.println(border);
            for (String row : tripStopFullInfo) {
                tokenizer = new StringTokenizer(row, "\t");
                ui.println(String.format("%-22s | %-17s | %-19s | %13s | %14s",
                                         tokenizer.nextToken(),     // sched. arrival time
                                         tokenizer.nextToken(),     // actual start time
                                         tokenizer.nextToken(),     // actual arrival time
                                         tokenizer.nextToken(),     // passengers in
                                         tokenizer.nextToken()));   // passengers out
            }
        } else {
            ui.printMsg("Empty trip-stop info");
        }
    }

    private void displayDriverSchedule() {
        String driverName, date;
        List<String> schedule;
        String border = "";
        StringTokenizer tokenizer;

        ui.printMenuHeader("> Main Menu > Display Driver Schedule");
        driverName = ui.getUserStringInput("Enter the driver's name");
        date = ui.getUserStringInput("Enter the date");
        schedule = DBInterface.getDriverSchedule(driverName, date);
        if (schedule.size() > 0) {
            ui.println("");
            ui.println(String.format("%-11s | %-14s | %-11s | %-10s | %-12s | %-6s",
                                     "Trip Number", "Start Location", "Destination",
                                     "Start Time", "Arrival Time", "Bus ID"));
            for (int i = 0; i < 79; i++) {
                border += "-";
            }
            ui.println(border);
            for (String row : schedule) {
                tokenizer = new StringTokenizer(row, "\t");
                ui.println(String.format("%11s | %-14s | %-11s | %10s | %12s | %6s",
                                         tokenizer.nextToken(),     // trip number
                                         tokenizer.nextToken(),     // start location
                                         tokenizer.nextToken(),     // destination
                                         tokenizer.nextToken(),     // start time
                                         tokenizer.nextToken(),     // arrival time
                                         tokenizer.nextToken()));   // bus ID
            }
        } else {
            ui.printMsg("Empty schedule");
        }
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

    private void addTripStopFullInfo() {
        // print menu header: > Main Menu > Add Trip Info
        // print a warning that any existing info will be overwritten
        // prompt user for input for all the needed attributes
        // call DBInterface.addFullTripInfo(...) with the necessary args
        // output success or failure depending on returned value
        if (!DBInterface.tripsExist()) {
            ui.printError("No trips exist, cannot possibly add full trip-stop info");
            return;
        } else if (!DBInterface.stopsExist()) {
            ui.printError("No stops exist, cannot possible add full trip-stop-info");
            return;
        }

        int tripNum;
        String date;
        String startTime;
        int stopNum;
        String arrivalTime;
        String realStartTime;
        String realArrivalTime;
        int numPassengersIn;
        int numPassengersOut;
        boolean done = false;

        while (!done) {
            ui.printMenuHeader("> Main Menu > Add Trip Info");
            tripNum = ui.getUserIntInput("Enter the trip number");
            if (!DBInterface.containsTrip(tripNum)) {
                ui.printError("Given trip number does not exist");
                continue;
            }
            date = ui.getUserStringInput("Enter the trip date");
            startTime = ui.getUserStringInput("Enter the scheduled start time");
            if (!DBInterface.containsOffering(tripNum, date, startTime)) {
                ui.printError("Given offering does not exist");
                continue;
            }
            stopNum = ui.getUserIntInput("Enter the stop number");
            if (!DBInterface.containsStop(stopNum)) {
                ui.printError("Given stop number does not exist");
                continue;
            }
            arrivalTime = ui.getUserStringInput("Enter the scheduled arrival time of the trip");
            realStartTime = ui.getUserStringInput("Enter the actual start time of the trip");
            realArrivalTime = ui.getUserStringInput("Enter the actual arrival time of the trip");
            numPassengersIn = ui.getUserIntInput("Enter the total no. of passengers in");
            numPassengersOut = ui.getUserIntInput("Enter the total no. of passengers out");
    
            if (DBInterface.addFullTripInfo(tripNum, date, startTime, stopNum, arrivalTime, realStartTime, realArrivalTime,numPassengersIn, numPassengersOut)){
                ui.printMsg("Trip Info successfully added");
            } else {
                ui.printError("Could not add trip-stop info, may already exist");
            }
            done = true;
        }
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
        if (!DBInterface.driversExist()) {
            ui.printError("No drivers exist");
            return;
        }

        boolean done = false;
        String name;

        while (!done) {
            ui.printMenuHeader("> Main Menu > Delete Driver");
            name = ui.getUserStringInput("Enter the driver's name");
            if (!DBInterface.containsDriver(name)) {
                ui.printError("Given driver name does not exist");
                continue;
            }
            if (DBInterface.deleteDriver(name)) {
                ui.printMsg("Driver successfully deleted");
            } else {
                ui.printError("Could not delete driver");
            }
            done = true;
        }
    }

    private void deleteBus() {
        if (!DBInterface.busesExist()) {
            ui.printError("No buses exist");
            return;
        }

        boolean done = false;
        int id;

        while (!done) {
            ui.printMenuHeader("> Main Menu > Delete Bus");
            id = ui.getUserIntInput("Enter the bus ID");
            if (!DBInterface.containsBus(id)) {
                ui.printError("Given bus ID does not exist");
                continue;
            }
            if (DBInterface.deleteBus(id)) {
                ui.printMsg("Bus successfully deleted.");
            } else {
                ui.printError("Could not delete bus");
            }
            done = true;
        }
    }

    private void deleteStop() {
        if (!DBInterface.stopsExist()) {
            ui.printError("No stops exist");
            return;
        }

        boolean done = false;
        int stopNum;

        while (!done) {
            ui.printMenuHeader("> Main Menu > Delete Stop");
            stopNum = ui.getUserIntInput("Enter the stop number");
            if (!DBInterface.containsStop(stopNum)) {
                ui.printError("Given stop does not exist");
                continue;
            }
            if (DBInterface.deleteStop(stopNum)) {
                ui.printMsg("Stop successfully deleted");
            } else {
                ui.printError("Could not delete stop");
            }
            done = true;
        }
    }

    private void deleteTrip() {
        if (!DBInterface.tripsExist()) {
            ui.printError("No trips exist");
            return;
        }

        boolean done = false;
        int tripNum;
        
        while (!done) {
            ui.printMenuHeader("> Main Menu > Delete Trip");
            tripNum = ui.getUserIntInput("Enter the trip number");
            if (!DBInterface.containsTrip(tripNum)) {
                ui.printError("Given trip does not exist");
                continue;
            }
            if (DBInterface.deleteTrip(tripNum)) {
                ui.printMsg("Trip successfully deleted.");
            } else {
                ui.printError("Could not delete trip");
            }
            done = true;
        }
    }
}