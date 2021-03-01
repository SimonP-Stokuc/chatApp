package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static BufferedWriter writer;

    public static int countingLogins(String username) {
        int counter = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data.txt"));

            String readingNow = reader.readLine();
            while (reader.readLine() != null) {
                if(readingNow.equals(username + ":KL25647:HAVE READ ALL MESSAGES TO THIS POINT :SystemNote:")) {
                    counter++;
                }
                readingNow = reader.readLine();
            }

            counter--;

            return counter;
        } catch (IOException e) {
            System.out.println("something went wrong with error..." + e);
            return counter;
        }
    }

    public static void write(String input) {
        try {
            if (writer == null) {
                writer = new BufferedWriter(new FileWriter("data.txt", true));
            }

            writer.append(input);
            writer.newLine();
            writer.flush();
        } catch (Exception e) {
            System.out.println("something went wrong with error..." + e);
        }
    }

    public static void userLastMessageRead(String user) {
        String key = ":KL25647:";
        String toWrite = user + key + "HAVE READ ALL MESSAGES TO THIS POINT :SystemNote:";
        write(toWrite);
    }


    public static String[][] getMessages(String user) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data.txt"));
            String key = ":KL25647:";

            int line = countingLogins(user);
            String readingNow = reader.readLine();
            while (readingNow != null) {
                int counter = 0;
                //readingNow.equals(user + key + "HAVE READ ALL MESSAGES TO THIS POINT :SystemNote:")
                if (counter > line) {
                    ArrayList<String> usernames = new ArrayList<>();
                    ArrayList<String> messages = new ArrayList<>();
                    readingNow = reader.readLine();
                    while (readingNow != null) {
                        if (!(readingNow.contains("HAVE READ ALL MESSAGES TO THIS POINT :SystemNote:") && !(readingNow.contains("WAS REGISTERED :SystemNote:")))) {
                            String[] organizer = readingNow.split(key);
                            usernames.add(organizer[0]);
                            messages.add(organizer[1]);
                        }
                        readingNow = reader.readLine();
                    }
                    reader.close();
                    String[][] result = new String[usernames.size()][2];
                    for (int i = 0; i < usernames.size(); i++) {
                        result[i][0] = usernames.get(i);
                        result[i][1] = messages.get(i);
                    }
                    return result;
                }
                counter++;
                readingNow = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("something went wrong with error..." + e);
        }
        return null;
    }

    public static void writeMessage(String username, String message) {
        String key = ":KL25647:";
        write(username + key + message);
    }

    public static void newUser(String userName) {
        String key = ":KL25647:";
        write(userName + key + "WAS REGISTERED :SystemNote:");
    }

    public static boolean userFinder(String userName) {
        try {
            String key = ":KL25647:";
            BufferedReader reader = new BufferedReader(new FileReader("data.txt"));

            String nowReading = reader.readLine();
            while (nowReading != null) {
                if (nowReading.equals(userName + key + "WAS REGISTERED :SystemNote:")) {
                    return true;
                }
                nowReading = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("something wet wrong with error..." + e);
        }
        return false;
    }


    public static String inputScanValidator() {
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();
        while (input.equals("")) {
            input = in.nextLine();
        }
        return input;
    }

    public static void main(String[] args) {
        System.out.println("enter your name:");
        String userName = inputScanValidator();
        Scanner in = new Scanner(System.in);

        if (!userFinder(userName)) {
            newUser(userName);
        }

        String[][] newMessages = getMessages(userName);

        try {
            if (newMessages.length != 0) {
                System.out.println("You got " + newMessages.length + " New Messages");
                for (int i = 0; i < newMessages.length; i++) {
                    System.out.println(newMessages[i][0] + ": " + newMessages[i][1]);
                }
            } else {
                System.out.println("You dont have any new messages :(");
            }
        } catch (NullPointerException e) {
            System.out.println("You dont have any new messages :(");
        }

        userLastMessageRead(userName);

        System.out.println("enter message to others (for stop type '!stop')");
        String userInput = in.nextLine();
        while (!userInput.equals("!stop")) {
            writeMessage(userName, userInput);
            System.out.println(userName + ": " + userInput);
            System.out.println("enter message to others (for stop type '!stop')");
            userInput = in.nextLine();
        }

        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Thank you for using our chatting app Hope to see you soon");
    }
}
