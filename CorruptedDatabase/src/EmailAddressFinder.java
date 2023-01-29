//package com.bham.pij.assignments.a1a;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class EmailAddressFinder {

    private static ArrayList<String> emailAddresses;
    public static void main(String[] args) {

        emailAddresses = new ArrayList<>();
        EmailAddressFinder eaf = new EmailAddressFinder();
        eaf.run();
        System.out.println("Email addresses found: " + emailAddresses.size());
        System.out.println("Email addresses found: " + emailAddresses.toString());

    }
    public void run() {

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader("corrupteddb"));

            String input = "";

            PrintWriter pw = new PrintWriter("eaf");

            while ((input = reader.readLine()) != null) {

                input = input.trim();

                ArrayList<String> temp = new ArrayList<String>();

                temp = findEmailAddresses(input);

                for (String t: temp) {
                    emailAddresses.add(t);
                }
            }

            pw.close();
            reader.close();
        }

        catch(IOException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<String> findEmailAddresses(String input) {

        ArrayList<String> list =new ArrayList<>();
        String tempEmail="", topLevelDomain="", email="";

        // find email address by their top level domains and validate them from there - .com, .net | .uk, .jp, .de, .ro
        for (int i=0; i<input.length();i++){
            if ((input.length()>=4) && ((input.charAt(i)=='.' && input.charAt(i+1)=='c' &&input.charAt(i+2)=='o'&& input.charAt(i+3)=='m') || (input.charAt(i)=='.' && input.charAt(i+1)=='n' &&input.charAt(i+2)=='e'&& input.charAt(i+3)=='t'))){
                tempEmail = input.substring(0,i+4);
                topLevelDomain = tempEmail.substring(i,i+4);
                email=emailExtraction(tempEmail,topLevelDomain);
                if (!email.equals("")) list.add(email);
                input = input.substring(i+4);
                i=0;
            }
            if ((input.length()>=3) && ((input.charAt(i)=='.' && input.charAt(i+1)=='u' &&input.charAt(i+2)=='k')||(input.charAt(i)=='.' && input.charAt(i+1)=='j' &&input.charAt(i+2)=='p') ||(input.charAt(i)=='.' && input.charAt(i+1)=='d' &&input.charAt(i+2)=='e') ||(input.charAt(i)=='.' && input.charAt(i+1)=='r' &&input.charAt(i+2)=='o'))) {
                tempEmail = input.substring(0,i+3);
                topLevelDomain = tempEmail.substring(i,i+3);
                email=emailExtraction(tempEmail,topLevelDomain);
                if (!email.equals("")) list.add(email);
                input = input.substring(i+3);
                i=0;
            }
        }
        return list;
    }
    public static String emailExtraction(String input, String topLevelDomain){

        boolean isLocalValid=false, isDomainValid=false;
        String localTemp = "", local="", afterIVCRemoval="", domainTemp="", email="";
        if (input.contains("@")) {
            localTemp=input.substring(0,input.lastIndexOf("@"));
            if (isLocalValid(localTemp)) local = localTemp;
            else {
                afterIVCRemoval = removeInvalidCharactersFromLocal(localTemp);
                local = meetLocalPeriodRequirements(afterIVCRemoval);
            }
            isLocalValid = !local.equals("");
            if(isLocalValid){
                domainTemp = input.substring(input.lastIndexOf('@')+1,input.lastIndexOf('.'));
                isDomainValid = isDomainValid(domainTemp) && isDomainPeriodRequirementsSatisfied(domainTemp);
            }
            if(isLocalValid&&isDomainValid) email = local + "@" + domainTemp + topLevelDomain;
        }
        return email;
    }
    public static String removeInvalidCharactersFromLocal(String input){
        String local="";
        for (int i=input.length()-1 ; i>=0 ; i--) {
            if (!((Character.isUpperCase(input.charAt(i)))
                    || (Character.isLowerCase(input.charAt(i)))
                    || (Character.isDigit(input.charAt(i)))
                    || (input.charAt(i)=='_'||input.charAt(i)=='.'))) {
                if (i==input.length()-1){
                    local = "invalid";
                    break;
                }
                local = input.substring(i + 1);
                break;
            }
        }
        switch (local) {
            case "":
                return input;
            case "invalid":
                return "";
            default:
                return local;
        }
    }
    public static String meetLocalPeriodRequirements(String input){
        String local="";
        int countP=0;
        for (int t=0;t<input.length();t++) {
            if (input.charAt(t)=='.'){
                countP++;
            }
        }
        if (input.length() != countP) {
            int x = input.indexOf('.');
            while (x == 0 && input.charAt(x) == '.') {
                input = input.substring(x + 1);
            }
        }
        int count=0;
        for (int j=input.length()-1 ; j>=0 ; j--) {
            //we loop backwards and look for the first occurrence of the invalid character
            if (input.charAt(j)=='.') count++;
            if(j==input.length()-1 && input.charAt(j)=='.'){
                local="";
                break;
            }
            else if(input.charAt(j)=='.'&& count==2) {
                local = input.substring(j + 1);
                break;
            }
            else local =input;
        }
        if(!local.equals("")&&local.charAt(0)=='.') {
            local =meetLocalPeriodRequirements(local);
        }
        return local;
    }
    public static boolean isLocalValid (String input){
        int count=0;
        int countPeriod=0;
        for (int i =0; i <input.length() ; i++) {
            if(input.charAt(i)=='.') countPeriod++;
            if (!(Character.isUpperCase(input.charAt(i)) || Character.isLowerCase(input.charAt(i)) || Character.isDigit(input.charAt(i)) || input.charAt(i)=='.'||input.charAt(i)=='_')) count += 1;
            if((i==0 && input.charAt(i)=='.') || (i==input.length()-1 && input.charAt(i)=='.')|| countPeriod>1) return false;
        }
        return count == 0 ;
    }
    public static boolean isDomainValid(String domainTemp) {
        int count = 0;
        for (int k = 0; k < domainTemp.length(); k++) {
            if (Character.isLowerCase(domainTemp.charAt(k))||domainTemp.charAt(k) =='.') count++;
        }
        return count == domainTemp.length();
    }
    public static boolean isDomainPeriodRequirementsSatisfied (String domainTemp){
        int countPeriod = 0;
        for (int k = 0; k < domainTemp.length(); k++){
            if ((k == 0 && (domainTemp.charAt(k)=='.') || (k == domainTemp.length()-1 && domainTemp.charAt(k)=='.'))) return false;
            if (domainTemp.charAt(k) == '.') countPeriod++;
        }
        return countPeriod <= 2;
    }

}
