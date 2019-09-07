/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author seed
 */
public class Security {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        int final_success = 0;
        System.out.println("Type the username:");
        Scanner input = new Scanner(System.in);
        String username = input.nextLine();
        File dic_file = new File("Dictionary.txt");
        BufferedReader br = new BufferedReader(new FileReader(dic_file));
        String password = null;
        while ((password = br.readLine()) != null) {
            Httpthread threads = new Httpthread();
            try {
                //threads.get_request();
                threads.httpclient();
                if (threads.get_success() == 1) {
                    final_success = 1;
                    System.out.println("Log In Successful");
                    String page = threads.get_entity();
                    File newpage = new File("xsslogin.html");
                    FileWriter wr = new FileWriter(newpage);
                    wr.write(page);
                    wr.flush();
                    wr.close();
                    Desktop.getDesktop().open(newpage);
                }
                //System.out.println("hello");
            } catch (IOException ex) {
                Logger.getLogger(Security.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
