package edu.ryerson.mvassair.ryersonandroidweatherapp;

import java.io.*;
import java.net.*;
import android.net.*;

class OWMHandler {

    URL owmURL;
    BufferedReader result;

    public OWMHandler(){

        try{
            owmURL = new URL("https://www.openweathermap.org");
            System.out.println("URL made");

            try {
                result = new BufferedReader(new InputStreamReader(owmURL.openStream()));

                System.out.println("Buffered Reader get");

                String line = "";

                while ((line = result.readLine()) != null)
                    System.out.print(line);

                result.close();
            }catch (IOException e){
                System.out.println("BufferedReader failed. Reason " + e.getMessage() + "\n\n" + e.getStackTrace());
            }
        }catch(MalformedURLException e){
            System.out.println("URL not made. Reason " + e.getMessage() + "\n\n" + e.getStackTrace());
        }



    }

}
