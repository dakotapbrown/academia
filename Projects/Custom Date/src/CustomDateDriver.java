   import java.util.Arrays;
   import java.io.*;

   public class CustomDateDriver {
   
      public static void main(String[] args) {
         CustomDate[] dates = new CustomDate[9];
         int index = 0;
         
         PrintWriter out = null;
         try {
            out = new PrintWriter(new FileWriter("output.txt"));
         } 
         catch(IOException ioe){
            System.out.println("Unable to write to the file.");
         }
                
         out.println("\nTest instantiation and validity:");
         dates[index++] = new CustomDate(2, 3, 2018);      //project due date
         dates[index++] = new CustomDate(12, 31, 2018);    //New Year's Eve
         dates[index++] = new CustomDate(1, 1, 2019);      //New Year's Day
         dates[index++] = new CustomDate(2, 28, 2008);     //day before a leap day
         dates[index++] = new CustomDate(2, 29, 2008);     //leap day
         dates[index++] = new CustomDate(2, 28, 2009);     //last day of Feb - non leap year
         dates[index++] = new CustomDate(1, 1, 2019);      //New Year's Day, again
         out.println(" Valid Dates:  " + Arrays.toString(dates));
         out.println("Testing constructor with invalid dates:  ");
         try {
            dates[index++] = new CustomDate(2, 29, 2009);     //invalid leap day
         } catch (Exception e) {
               out.println(e.getMessage());
         }
         try {
            dates[index++] = new CustomDate(6, 31, 2018);     //invalid day
         } catch (Exception e) {
               out.println(e.getMessage());
         }
            
         out.println("Dates created are:  " + Arrays.toString(dates));
      
         out.println("\nTest toString method:");
         for (CustomDate day : dates) {
            if (day != null) { 
            	out.println(day.toString());
            }
         }
      
         out.println("\nTest advanceOneDay method:");
         for (CustomDate day : dates) {
            if(day != null) { 
               day.advanceOneDay();
               out.println(day);
            }
         }

         out.println("\nTest advanceOneWeek method:");
         for (CustomDate day : dates) {
            if(day != null) { 
               day.advanceOneWeek();
               out.println(day);
            }
         }
         
         
         out.println("\nTest equals method:");
         out.println(dates[1] + " equals " + dates[2] + ": " + dates[1].equals(dates[2]));
         out.println(dates[1] + " equals " + dates[1] + ": " + dates[1].equals(dates[1]));
         out.println(dates[2] + " equals " + dates[6] + ": " + dates[2].equals(dates[6]));
      
         out.println("\nTest isLeapYear method:");
         for (CustomDate day : dates) {
            if (day != null) { 
            	out.println("" + day.getYear() + " is a leap year - " + day.isLeapYear());
            }
         }
      
         out.close();
      }
   }