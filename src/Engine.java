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
        List<String> tripInfo;
        String border = "";
        StringTokenizer tokenizer;

        ui.printMenuHeader("> Main Menu > Display Stops");
        tripNum = ui.getUserIntInput("Enter the trip number");
        tripInfo = DBInterface.getTripInfo(tripNum);
        if (tripInfo.size() > 0) {
            ui.println("");
            ui.println(String.format("%-11s | %-15s | %-12s", "Stop Number", "Sequence Number", "Driving Time"));
            for (int i = 0; i < 44; i++) {
                border += "-";
            }
            ui.println(border);
            for (String row : tripInfo) {
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

    private void addTripInfo() {
        // print menu header: > Main Menu > Add Trip Info
        // print a warning that any existing info will be overwritten
        // prompt user for input for all the needed attributes
        // call DBInterface.addFullTripInfo(...) with the necessary args
        // output success or failure depending on returned value
        int tripNum;
        String date;
        String startTime;
        int stopNum;
        String arrivalTime;
        String realStartTime;
        String realArrivalTime;
        int numPassengersIn;
        int numPassengersOut;
        ui.printMenuHeader("> Main Menu > Add Trip Info");
        tripNum = ui.getUserIntInput("Enter the trip number");
        date = ui.getUserStringInput("Enter the trip date");
        startTime = ui.getUserStringInput("Enter the trip start time");
        stopNum = ui.getUserIntInput("Enter the number of stops on the trip");
        arrivalTime = ui.getUserStringInput("Enter the arrival time of the trip");
        realStartTime = ui.getUserStringInput("Enter the real start time of the trip");
        realArrivalTime = ui.getUserStringInput("Enter the real arrival time of the trip");
        numPassengersIn = ui.getUserIntInput("Enter the total no. of passengers in");
        numPassengersOut = ui.getUserIntInput("Enter the total no. of passengers out");
        
      
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
    
   	 	String name;
       	ui.printMenuHeader("> Main Menu > Delete Driver");
        name = ui.getUserStringInput("Enter the driver's name");
        if (DBInterface.deleteDriver(name)) {
            ui.printMsg("Driver successfully deleted");
        } else {
            ui.printError("Could not delete driver, may have already deleted.");
        }
    }

    private void deleteBus() {
    
    	int id;
        ui.printMenuHeader("> Main Menu > Delete Bus");
        id = ui.getUserIntInput("Enter the bus ID");
        if (DBInterface.deleteBus(id)) {
            ui.printMsg("Bus successfully deleted.");
        } else {
            ui.printError("Could not delete bus, may have already deleted.");
        }
    }

    private void deleteStop() {
    	
    	int stopNum;
        ui.printMenuHeader("> Main Menu > Delete Stop");
        stopNum = ui.getUserIntInput("Enter the stop number");
        if (DBInterface.deleteStop(stopNum)) {
            ui.printMsg("Stop successfully deleted");
        } else {
            ui.printError("Could not delete stop, may have already deleted.");
        }
    	
    }

    private void deleteTrip() {
    	
    	int tripNum;
        ui.printMenuHeader("> Main Menu > Delete Trip");
        tripNum = ui.getUserIntInput("Enter the trip number");

        if (DBInterface.deleteTrip(tripNum)) {
            ui.printMsg("Trip successfully deleted.");
        } else {
            ui.printError("Could not delete trip, may have already deleted.");
        }
    	
    }
}