
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class EAF {

    private static ArrayList<String> emailAddresses;
    static ArrayList<String> garbage = new ArrayList<String>();
    static String garb = "";

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

                for (String t : temp) {
                    emailAddresses.add(t);
                }
            }

            pw.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> findEmailAddresses(String input) {
        ArrayList<String> list = new ArrayList<String>();
        // holds the string extracted upon a top level domain match e.g for a string of "are we dead.shane@gmail.com"
        // temp will hold dead.shane@gmail.com
        String temp = "";
        // holds the top level domain
        String tld="";
        // holds the email after extraction if a valid email was found, otherwise it holds the empty string
        String email="";
        for (int i=0; i < input.length(); i++) {
            // if we see .com or .net
            if ( (input.charAt(i)=='.' && input.charAt(i+1)=='c' &&input.charAt(i+2)=='o'&& input.charAt(i+3)=='m') ||(input.charAt(i)=='.' && input.charAt(i+1)=='n' &&input.charAt(i+2)=='e'&& input.charAt(i+3)=='t')){
                temp  =  input.substring(0,i+4);
                tld = temp.substring(i,i+4);
                email = extraction(temp,tld);
                if (!email.equals("")) list.add(email);
                else{
                    garb = input.substring(0,i+4);
                    garbage.add(garb);
                }
                input = input.substring(i+4);
                i=0;
            }
            // if we see .uk, .jp, .de or .ro
            if ( (input.charAt(i)=='.' && input.charAt(i+1)=='u' &&input.charAt(i+2)=='k') ||(input.charAt(i)=='.' && input.charAt(i+1)=='j' &&input.charAt(i+2)=='p') ||(input.charAt(i)=='.' && input.charAt(i+1)=='d' &&input.charAt(i+2)=='e') ||(input.charAt(i)=='.' && input.charAt(i+1)=='r' &&input.charAt(i+2)=='o')){
                temp  =  input.substring(0,i+3);
                tld = temp.substring(i,i+3);
                email = extraction(temp,tld);
                if (!email.equals("")) list.add(email);
                else{
                    garb = input.substring(0,i+3);
                    garbage.add(garb);
                }
                input = input.substring(i+3);
                i=0;
            }
        }
//        for (int j=0;j< list.size();j++){
//            System.out.println((j+1)+"th element is :"+list.get(j));
//        }
        for (int j=0;j< garbage.size();j++){
            System.out.println((j+1)+"th element is :"+garbage.get(j));
        }
        return list;
    }
    public static String extraction(String temp, String tld){
        boolean isLocal=false;
        boolean isDomain =false;

        String localTemp;
        String local ="";
        String domainTemp="";
        String email="";

        // if the temp string contains an @ then it may contain a valid email address
        // if not the method will return an empty string as the isLocal will remain false
        if (temp.contains("@")) {
            // store the from the start of the temp string to the last index of @ which is the local part of the email
            localTemp = temp.substring(0, temp.lastIndexOf('@'));
            // calls a method which checks whether the localTemp string contains invalid characters as per the requirements
            // if it does not contain any invalid characters, then we can proceed to validate whether it has the correct
            // number of '.' characters and that they are only used as a separation
            if (isLocalValid(localTemp)) {
                local = localTemp;
                // as our check for invalid characters does not include the '.' character, but it has some specificity over
                // its use, we check if the local part does not start with a '.' as technically .michael@gmail.com is
                // not valid but michael@gmail.com is
                // the while ensures that if we have ....michael@gmail.com, we still extract michael@gmail.com
                int x=local.indexOf('.');
                while(x==0 && local.charAt(x)=='.'){
                    local = local.substring(x+1);
                }
//                for (int l=0;l<local.length();l++){
//                    if (l==0 && local.charAt(l)==' '){
//                        local = local.substring(l+1);
//                    }
//                }

            } else {
                // the string does contain special characters
                // what we will do is we are going to loop backwards on the string as part of it may be valid from
                // the last occurrence of the invalid character to the end of the string
                // e.g shane$%nex@gmail.com, shane$%nex is not a valid local part, but nex is and instead of throwing
                // the whole string away, we can still extract nex@gmail.com
                for (int j = localTemp.length() - 1; j >= 0; j--) {
                    //we loop backwards and look for the first occurrence of the invalid character
                    if (!(localTemp.charAt(j)>='A' && localTemp.charAt(j)<='Z'
                            || localTemp.charAt(j)>='a'&& localTemp.charAt(j)<='z'
                            || localTemp.charAt(j)>='0'&& localTemp.charAt(j)<='9'
                            || localTemp.charAt(j)=='_'
//                            || localTemp.charAt(j)=='.'
//                            || localTemp.charAt(j)==' '
                    )) {
                        //when we find it we extract the string one index from its occurrence to the end of the string
                        local = localTemp.substring(j + 1);
                        garb = localTemp.substring(0,j+1);
                        garbage.add(garb);

                        break;
                    }
                }
                // as our check for invalid characters does not include the '.' character, but it has some specificity over
                // its use, we check if the local part does not start with a '.' as technically .michael@gmail.com is
                // not valid but michael@gmail.com is
                // the while ensures that if we have a case of many '.' at the start of a local part
                // e.g ....michael@gmail.com, we still extract michael@gmail.com
                int x=local.indexOf('.');
                while(x==0 && local.charAt(x)=='.'){
                    local = local.substring(x+1);
                }
//                for (int l=0;l<local.length();l++){
//                    if (l==0 && local.charAt(l)==' '){
//                        local = local.substring(l+1);
//                    }
//                }
            }
            // check if the local part contains the correct number of '.' characters and if it is used as a separation
            // only. If it is, the boolean is local is set to true which confirms we have a valid local part
            isLocal= isLocalPeriodRequirementsSatisfied(local);
        }
        // if the local part is valid(isLocal=true), we can now check is the domain is valid. otherwise we skip this
        // process as it would be redundant
        if (isLocal){
            // we extract the domain part from the temp string which contains everything up to the top level domain of the email
            // address, into domainTemp string (everything after the @ to the .)
            domainTemp = temp.substring(temp.lastIndexOf('@')+1,temp.lastIndexOf('.'));
            // we call two functions which must both be true for the domain to be valid
            // one checks is the domain period requirements are met and the other if it does not contain invalid characters
            isDomain= isDomainPeriodRequirementsSatisfied(domainTemp) && isDomainValid(domainTemp);
        }
        // if both the local part and the domain part are true, we have found a valid email address which we can return
        // we assemble the emain using the local part, an @, the domain part and the top level domain(tld) collected at
        // the beginning
        if (isLocal && isDomain) {
            email = local + "@" + domainTemp +tld;
        }
        // if an email was not found due to either the local or domain part not being valid, email returns an empty string
        return email;
    }
    public static boolean isLocalValid (String input){
        int count=0;
        for (int i =0; i <input.length() ; i++) {
            if (!(input.charAt(i)>='A' && input.charAt(i)<='Z'
                    || input.charAt(i)>='a'&& input.charAt(i)<='z'
                    || input.charAt(i)>='0'&& input.charAt(i)<='9'
                    || input.charAt(i)=='.'|| input.charAt(i)=='_')) {
                count+=1;
            }
        }
        return count == 0;
    }
    public static boolean isDomainValid(String domainTemp) {
        int count = 0;
        for (int k = 0; k < domainTemp.length(); k++) {
            //if the domain contains invalid characters
            if (domainTemp.charAt(k)>='a'&& domainTemp.charAt(k)<='z'||domainTemp.charAt(k) =='.') {
                count++;
            }
        }
        return count == domainTemp.length();
    }
    public static boolean isLocalPeriodRequirementsSatisfied (String local){
        int countPeriod = 0;
        for (int k = 0; k < local.length(); k++)
        {
            //if the local part starts with a . OR ends with a dot
            if ((k == local.length()-1 && local.charAt(k)=='.')) {
                return false;
            }
            if (local.charAt(k) == '.') {
                countPeriod++;
            }
        }
        if (countPeriod <= 1) return true;
        else return false;
    }
    public static boolean isDomainPeriodRequirementsSatisfied (String domainTemp){
        int countPeriod = 0;
        for (int k = 0; k < domainTemp.length(); k++)
        {
            //if the domain part starts with a . OR ends with a dot
            if ((k == 0 && (domainTemp.charAt(k)=='.') || (k == domainTemp.length()-1 && domainTemp.charAt(k)=='.'))) {
                return false;
            }
            if (domainTemp.charAt(k) == '.') {
                countPeriod++;
            }
        }
        return countPeriod <= 2;
    }

}
