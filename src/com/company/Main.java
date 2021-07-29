package com.company;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner keyboard = new Scanner(System.in);
        String input = "";
        
        while (!input.equals("0")) {

            System.out.println("If you want to check single IBAN press - 1");
            System.out.println("If you want to check IBAN list out of text file press - 2");
            System.out.println("If you want to close program press - 0");

            input = keyboard.nextLine();
            System.out.println(input);

            switch (input) {
                case "1" -> checkSingleIban(keyboard);
                case "2" -> checkIbanList(keyboard);
                case "0" -> System.out.println("IBAN checker is closed.");
                default -> System.out.println("Input is not understandable. Try to enter single digit");
            }
        }

        keyboard.close();
    }

    private static void checkIbanList(Scanner keyboard) {
        String countriesInfo = "src/com/company/countriesInfo.txt";
        List<Country> countries = getCountriesInformation(countriesInfo);
        List<String> ibanList;
        List<String> answers = new ArrayList<>();

        System.out.println("Enter file path");
        System.out.println(" Example: C:\\Dev\\seb_task\\src\\com\\company\\data.txt");
        String filepath = keyboard.nextLine();

        ibanList = getIbanList(filepath);

        if (!ibanList.isEmpty()) {
            for (String iban : ibanList) {
                if(isIbanCorrect(iban, countries)) {
                    answers.add(iban + " is correct");
                } else {
                    answers.add(iban + " is not correct");
                }
            }
            writeAnswersToFile(answers, filepath);
        } else {
            System.out.println("File is not found or it is empty");
        }
    }

    private static void writeAnswersToFile(List<String> answers, String filePath) {
        filePath = changeFilePath(filePath);

        File outputFile = new File(filePath);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(outputFile);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            for (String answer : answers) {
                bw.write(answer);
                bw.newLine();
            }

            System.out.println("Results are displayed here:");
            System.out.println(filePath);
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String changeFilePath(String filePath) {
        char lastCharacter;
        while(true) {
            lastCharacter = filePath.charAt(filePath.length() - 1);
            filePath = filePath.substring(0, filePath.length() - 1);
            if (lastCharacter == '.') {
                filePath += ".out";
                break;
            }
        }
        return filePath;
    }

    private static void checkSingleIban(Scanner keyboard) {
        String countriesInfo = "src/com/company/countriesInfo.txt";
        List<Country> countries = getCountriesInformation(countriesInfo);

        System.out.println("Enter IBAN");
        String iban = keyboard.nextLine();

        if (isIbanCorrect(iban, countries)) {
            System.out.println(iban + " is correct");
        } else {
            System.out.println(iban + " is not correct");
        }
    }

    private static boolean isIbanCorrect(String iban, List<Country> countries) {
        BigInteger ibanInInt;
        BigInteger mod = new BigInteger("97");

        for (Country c : countries) {
            if (c.getCountryCode().equals(iban.substring(0, 2)) && c.getIbanLength() == iban.length()) {
                iban = swapChars(iban);

                for (int i = 0; i < iban.length(); i++) {
                    if (!Character.isDigit(iban.charAt(i))) {
                        iban = changeIbanLettersToNumbers(iban, i);
                    }
                }

                ibanInInt = new BigInteger(iban);


                return ibanInInt.mod(mod).equals(new BigInteger("1"));
            }
        }

        return false;
    }

    private static List<String> getIbanList(String fileName) {
        List<String> iBanList = new ArrayList<>();

        File file = new File(fileName);
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return iBanList;
        }

        String fileLine;
        while (true) {
            try {
                if ((fileLine = br.readLine()) == null)  {
                    break;
                }
                else {
                    iBanList.add(fileLine.trim());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return iBanList;
    }

    private static String changeIbanLettersToNumbers(String iban, int i) {
        String newIban = iban.substring(0, i);
        newIban += Integer.toString(Character.getNumericValue(iban.charAt(i)));
        newIban += iban.substring(i + 1);
        return newIban;
    }

    private static String swapChars(String iban) {
        iban += iban.substring(0, 4);
        iban = iban.substring(4);
        return iban;
    }

    private static List<Country> getCountriesInformation(String fileName) {
        List<Country> countries = new ArrayList<>();

        File file = new File(fileName);
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return countries;
        }

        String fileLine;
        while (true) {
            try {
                if ((fileLine = br.readLine()) == null)  {
                    break;
                }
                else {
                    String[] countryInfo;
                    countryInfo = fileLine.split(" ");
                    countries.add(new Country(countryInfo[0], Integer.valueOf(countryInfo[1])));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return countries;
    }

}
