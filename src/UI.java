import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

class UI {
    private Scanner keyboard;

    public UI(Scanner keyboard) {
        this.keyboard = keyboard;
    }

    public void printStartMsg() {
        System.out.println("Transit System");
    }

    public void printMenuHeader(String header) {
        System.out.println("\n" + header);
    }

    public void printMenu(ArrayList<String> options) {
        for (int i = 1; i <= options.size(); i++) {
            System.out.printf("%2d. %s\n", i, options.get(i - 1));
        }
    }

    public int getUserMenuChoice(int menuSize) {
        int userInput;
        StringTokenizer tokenizer;

        while (true) {
            System.out.print("Enter choice: ");
            try {
                tokenizer = new StringTokenizer(keyboard.nextLine());
                userInput = Integer.parseInt(tokenizer.nextToken());
                if (userInput < 1 || userInput > menuSize) {
                    throw new IllegalArgumentException();
                } else {
                    break;
                }
            } catch (InputMismatchException ime) {
                System.err.println("Please enter an input");
            } catch (NoSuchElementException nsee) {
                System.err.println("Please enter an input");
            } catch(NumberFormatException nfe) {
                System.err.println("Please enter an integer");
            } catch (IllegalArgumentException iae) {
                System.err.println("Out of range error");
            } catch (Exception e) {
                System.err.println("Invalid input");
            }
        }
        return userInput - 1;
    }

    public String getUserStringInput(String prompt) {
        String input;

        while (true) {
            System.out.print(prompt + ": ");
            try {
                input = keyboard.nextLine();
                if (input.equals("")) {
                    throw new NoSuchElementException();
                } else {
                    return input;
                }
            } catch (NoSuchElementException nsee) {
                System.err.println("Please enter an input");
            } catch (Exception e) {
                System.err.println("Invalid input");
            }
        }
    }

    public int getUserIntInput(String prompt) {
        String input;

        while (true) {
            System.out.print(prompt + ": ");
            try {
                input = keyboard.nextLine();
                if (input.equals("")) {
                    throw new NoSuchElementException();
                } else {
                    return Integer.parseInt(input);
                }
            } catch (NoSuchElementException nsee) {
                System.err.println("Please enter an input");
            } catch (NumberFormatException nfe) {
                System.err.println("Please enter an integer");
            } catch (Exception e) {
                System.err.println("Invalid input");
            }
        }
    }

    public void println() {
        System.out.println();
    }

    public void println(String s) {
        System.out.println(s);
    }
}