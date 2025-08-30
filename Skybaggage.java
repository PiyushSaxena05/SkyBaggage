import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.Scanner;

class BookYourTickets {
    String name;
    Long phone_no;
    String gmail_Id;
    int passengercount;
    String startlocation;
    String destination;
    String airLine;
    String input;
    int baggage;
    int baggageamount = 0;
    int payAmount = 0;
    String flightType;
    String seat;
    int pay;
    String start;
    String returndate;
    String query1 = "INSERT INTO information(name,gmail,mobile_no)VALUES(?,?,?)";
    String query2 = "INSERT INTO booking(Flight_type,Airline,startdate,Returndate,Destination,Payment)VALUES(?,?,?,?,?,?)";

    public BookYourTickets(Connection con, Scanner sc) throws Exception {
        try {
            try (PreparedStatement p = con.prepareStatement(query1)) {
                try (PreparedStatement p1 = con.prepareStatement(query2)) {
                    System.out.print("WELCOME TO SKY BAGGAGE");
                    System.out.println();
                    System.out.print("Please enter start location: ");
                    startlocation = sc.nextLine();
                    System.out.print("Please enter your destination: ");
                    destination = sc.nextLine();
                    System.out.print("please enter number of passengers: ");
                    passengercount = sc.nextInt();
                    sc.nextLine();
                    baggageamount = 0;


                    for (int i = 1; i <= passengercount; i++) {
                        System.out.print("Please enter passenger name " + i + ": ");
                        name = sc.nextLine();
                        System.out.print("Please enter mobile number: ");
                        phone_no = sc.nextLong();
                        sc.nextLine();
                        System.out.print("Please enter Gmail id: ");
                        gmail_Id = sc.nextLine();
                        System.out.print("Do you want to add baggage (y/n): ");
                        input = sc.next();
                        sc.nextLine();

                        if (input.equalsIgnoreCase("y")) {
                            System.out.print("You can add upto 10, 20 or 30 kg\nPlease enter your baggage need: ");
                            baggage = sc.nextInt();
                            sc.nextLine();

                            if (baggage == 10) {
                                baggageamount += 700;
                            } else if (baggage == 20) {
                                baggageamount += 1000;
                            } else if (baggage == 30) {
                                baggageamount += 1200;
                            }
                        }
                        System.out.println("Total BaggageAmount so far is: " + baggageamount);
                        
                        p.setString(1, name);
                        if (phone_no >= 1_000_000_000L && phone_no <= 9_999_999_999L) {
                            p.setLong(3, phone_no);
                        } else {
                            System.out.println("Please enter valid number");
                            p.setLong(3, 0L);
                        }
                        p.setString(2, gmail_Id);
                        int rowsaffect1 = p.executeUpdate();

                        if (rowsaffect1 > 0) {
                            System.out.println("Passenger " + i + " info saved successfully.");
                        } else {
                            System.out.println("Failed to save passenger " + i + " info.");
                            con.rollback();
                            return;
                        }
                    }

                    System.out.println();
                    System.out.println("Please choose an Airline: ");
                    System.out.println("Enter option 1: Indigo");
                    System.out.println("Enter option 2: Vistara");
                    System.out.println("Enter option 3: SpiceJet");
                    System.out.println("Enter option 4: Emirates");
                    System.out.println("Enter option 5: Lufthansa");
                    System.out.println("Enter option 6: Singapore Airlines");
                    System.out.println("Enter option 7: Air India");
                    System.out.println("Enter option 8: Qatar Airways");
                    System.out.println("Enter option 9: British Airways");

                    int opt = sc.nextInt();
                    sc.nextLine();

                    switch (opt) {
                        case 1:
                            airLine = "Indigo";
                            break;
                        case 2:
                            airLine = "Vistara";
                            break;
                        case 3:
                            airLine = "SpiceJet";
                            break;
                        case 4:
                            airLine = "Emirates";
                            flightType = "International";
                            break;
                        case 5:
                            airLine = "Lufthansa";
                            flightType = "International";
                            break;
                        case 6:
                            airLine = "SingaporeAirline";
                            flightType = "International";
                            break;
                        case 7:
                            airLine = "AirIndia";
                            break;
                        case 8:
                            airLine = "Qatar Airways";
                            flightType = "International";
                            break;
                        case 9:
                            airLine = "BritishAirways";
                            flightType = "International";
                            break;
                        default:
                            System.out.println("Airline not selected");
                    }
                    System.out.println("You choosed : " + airLine);
                    System.out.println("Enter 1 for domestic flights: ");
                    System.out.println("Enter 2 for International Flights: ");
                    int option = sc.nextInt();
                    sc.nextLine();
                    switch (option) {
                        case 1:
                            flightType = "Domestic";
                            break;
                        case 2:
                            flightType = "International";
                            break;
                        default:
                            System.out.println("Choose valid option");
                    }
                    System.out.println("Choose seat type");
                    System.out.println("Enter 1 for Economy: ");
                    System.out.println("Enter 2 for Premium Economy");
                    System.out.println("Enter 3 for Business ");
                    int op = sc.nextInt();
                    sc.nextLine();
                    switch (op) {
                        case 1:
                            seat = "Economy";
                            break;
                        case 2:
                            seat = "Premium Economy";
                            break;
                        case 3:
                            seat = "Business";
                            break;
                        default:
                            System.out.println("Choose valid option");
                    }
                    System.out.print("Please enter the starting date (dd/MM/yyyy): ");
                    start = sc.nextLine().trim();
                    System.out.print("Please enter the return date (dd/MM/yyyy): ");
                    returndate = sc.nextLine().trim();
                    DateTimeFormatter d1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate startDate = LocalDate.parse(start, d1);
                    LocalDate returnDate = LocalDate.parse(returndate, d1);

                    payAmount = payment(airLine, baggageamount, flightType, seat);
                    System.out.println("Please pay : " + payAmount);
                    pay = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Please choose payment method : ");
                    System.out.println("Choose option 1 for credit card");
                    System.out.println("Choose option 2 for UPI");
                    int option1 = sc.nextInt();
                    sc.nextLine();
                    switch (option1) {
                        case 1:
                            System.out.print("Please enter card holder name: ");
                            String cardholdername = sc.nextLine();
                            System.out.print("Please enter cvv code: ");
                            int cvv = sc.nextInt();
                            sc.nextLine();
                            System.out.print("Please enter expiry date: ");
                            String expiry = sc.nextLine();
                            System.out.println("Cardholder name: " + cardholdername);
                            break;
                        case 2:
                            System.out.print("Please enter UPI ID: ");
                            String upi = sc.nextLine();
                            System.out.print("Please enter PIN: ");
                            int pin = sc.nextInt();
                            sc.nextLine();
                            break;
                        default:
                            System.out.println("Enter valid option");
                    }


                    p1.setString(1, flightType);
                    p1.setString(2, airLine);
                    p1.setDate(3, Date.valueOf(startDate));
                    p1.setDate(4, Date.valueOf(returnDate));
                    p1.setString(5, destination);
                    p1.setInt(6, pay);

                    int rowsaffect2 = p1.executeUpdate();

                    if (rowsaffect2 > 0 && pay == payAmount) {
                        con.commit();
                        System.out.println("Your flight tickets are booked");
                        System.out.println("Have a safe and nice journey");
                        System.out.println("Data Saved");
                    } else {
                        con.rollback();
                        System.out.println("Something went wrong");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static int payment(String airline, int baggageAmount, String flightType, String seat) throws Exception {
        int amount = 0;
        Random r = new Random();
        int charge = r.nextInt(10000) + 10000;
        if (airline.equalsIgnoreCase("Vistara") && flightType.equalsIgnoreCase("Domestic")) {
            if (seat.equalsIgnoreCase("Economy")) {
                amount = charge + baggageAmount;
            } else if (seat.equalsIgnoreCase("Premium Economy")) {
                amount = charge + baggageAmount + 2000;
            } else if (seat.equalsIgnoreCase("Business")) {
                amount = charge + baggageAmount + 5000;
            }
        } else if (airline.equalsIgnoreCase("Vistara") && flightType.equalsIgnoreCase("International")) {
            charge = r.nextInt(80000) + 20000;
            if (seat.equalsIgnoreCase("Economy")) {
                amount = charge + baggageAmount;
            } else if (seat.equalsIgnoreCase("Premium Economy")) {
                amount = charge + baggageAmount + 2500;
            } else if (seat.equalsIgnoreCase("Business")) {
                amount = charge + baggageAmount + 6000;
            }
        } else if (airline.equalsIgnoreCase("Indigo") && flightType.equalsIgnoreCase("Domestic")) {
            charge = r.nextInt(8000) + 10000;
            if (seat.equalsIgnoreCase("Economy")) {
                amount = charge + baggageAmount;
            } else if (seat.equalsIgnoreCase("Premium Economy")) {
                amount = charge + baggageAmount + 1800;
            } else if (seat.equalsIgnoreCase("Business")) {
                amount = charge + baggageAmount + 5000;
            }
        } else if (airline.equalsIgnoreCase("Indigo") && flightType.equalsIgnoreCase("International")) {
            charge = r.nextInt(50000) + 30000;
            if (seat.equalsIgnoreCase("Economy")) {
                amount = charge + baggageAmount;
            } else if (seat.equalsIgnoreCase("Premium Economy")) {
                amount = charge + baggageAmount + 2000;
            } else if (seat.equalsIgnoreCase("Business")) {
                amount = charge + baggageAmount + 6000;
            }
        } else if (airline.equalsIgnoreCase("SpiceJet") && flightType.equalsIgnoreCase("International")) {
            charge = r.nextInt(60000) + 30000;
            if (seat.equalsIgnoreCase("Economy")) {
                amount = charge + baggageAmount;
            } else if (seat.equalsIgnoreCase("Premium Economy")) {
                amount = charge + baggageAmount + 2000;
            } else if (seat.equalsIgnoreCase("Business")) {
                amount = charge + baggageAmount + 6000;
            }
        } else if (airline.equalsIgnoreCase("SpiceJet") && flightType.equalsIgnoreCase("Domestic")) {
            charge = r.nextInt(10000) + 10000;
            if (seat.equalsIgnoreCase("Economy")) {
                amount = charge + baggageAmount;
            } else if (seat.equalsIgnoreCase("Premium Economy")) {
                amount = charge + baggageAmount + 1800;
            } else if (seat.equalsIgnoreCase("Business")) {
                amount = charge + baggageAmount + 5000;
            }
        } else if (airline.equalsIgnoreCase("Emirates")) {
            charge = r.nextInt(70000) + 30000;
            if (seat.equalsIgnoreCase("Economy")) {
                amount = charge + baggageAmount;
            } else if (seat.equalsIgnoreCase("Premium Economy")) {
                amount = charge + baggageAmount + 3800;
            } else if (seat.equalsIgnoreCase("Business")) {
                amount = charge + baggageAmount + 8000;
            }
        } else if (airline.equalsIgnoreCase("Emirates") && flightType.equalsIgnoreCase("Domestic")) {
            throw new Exception("INVALID, THESE FLIGHTS ARE FOR INTERNATIONAL TRIPS");
        } else if (airline.equalsIgnoreCase("Lufthansa")) {
            charge = r.nextInt(80000) + 30000;
            if (seat.equalsIgnoreCase("Economy")) {
                amount = charge + baggageAmount;
            } else if (seat.equalsIgnoreCase("Premium Economy")) {
                amount = charge + baggageAmount + 4800;
            } else if (seat.equalsIgnoreCase("Business")) {
                amount = charge + baggageAmount + 10000;
            }
        } else if (airline.equalsIgnoreCase("Lufthansa") && flightType.equalsIgnoreCase("Domestic")) {
            throw new Exception("INVALID, THESE FLIGHTS ARE FOR INTERNATIONAL TRIPS");
        } else if (airline.equalsIgnoreCase("BritishAirways")) {
            charge = r.nextInt(80000) + 30000;
            if (seat.equalsIgnoreCase("Economy")) {
                amount = charge + baggageAmount;
            } else if (seat.equalsIgnoreCase("Premium Economy")) {
                amount = charge + baggageAmount + 4800;
            } else if (seat.equalsIgnoreCase("Business")) {
                amount = charge + baggageAmount + 10000;
            }
        } else if (airline.equalsIgnoreCase("BritishAirways") && flightType.equalsIgnoreCase("Domestic")) {
            throw new Exception("INVALID, THESE FLIGHTS ARE FOR INTERNATIONAL TRIPS");
        } else if (airline.equalsIgnoreCase("QatarAirways")) {
            charge = r.nextInt(80000) + 30000;
            if (seat.equalsIgnoreCase("Economy")) {
                amount = charge + baggageAmount;
            } else if (seat.equalsIgnoreCase("Premium Economy")) {
                amount = charge + baggageAmount + 4800;
            } else if (seat.equalsIgnoreCase("Business")) {
                amount = charge + baggageAmount + 10000;
            }
        } else if (airline.equalsIgnoreCase("QatarAirways") && flightType.equalsIgnoreCase("Domestic")) {
            throw new Exception("INVALID, THESE FLIGHTS ARE FOR INTERNATIONAL TRIPS");
        } else if (airline.equalsIgnoreCase("AirIndia") && flightType.equalsIgnoreCase("Domestic")) {
            charge = r.nextInt(10000) + 10000;
            if (seat.equalsIgnoreCase("Economy")) {
                amount = charge + baggageAmount;
            } else if (seat.equalsIgnoreCase("Premium Economy")) {
                amount = charge + baggageAmount + 1800;
            } else if (seat.equalsIgnoreCase("Business")) {
                amount = charge + baggageAmount + 5000;
            }
        } else if (airline.equalsIgnoreCase("AirIndia") && flightType.equalsIgnoreCase("International")) {
            charge = r.nextInt(60000) + 30000;
            if (seat.equalsIgnoreCase("Economy")) {
                amount = charge + baggageAmount;
            } else if (seat.equalsIgnoreCase("Premium Economy")) {
                amount = charge + baggageAmount + 2000;
            } else if (seat.equalsIgnoreCase("Business")) {
                amount = charge + baggageAmount + 6000;
            }
        } else if (airline.equalsIgnoreCase("SingaporeAirline")) {
            charge = r.nextInt(50000) + 30000;
            if (seat.equalsIgnoreCase("Economy")) {
                amount = charge + baggageAmount;
            } else if (seat.equalsIgnoreCase("Premium Economy")) {
                amount = charge + baggageAmount + 4800;
            } else if (seat.equalsIgnoreCase("Business")) {
                amount = charge + baggageAmount + 7000;
            }
        } else if (airline.equalsIgnoreCase("SingaporeAirline") && flightType.equalsIgnoreCase("Domestic")) {
            throw new Exception("INVALID, THESE FLIGHTS ARE FOR INTERNATIONAL TRIPS");
        }
        return amount;
    }
}

public class Skybaggage {
    private static final String url = "jdbc:mysql://localhost:3306/skybaggage";
    private static final String user = "root";
    private static final String password = "Password007";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            con.setAutoCommit(false);
            while (true) {
                Scanner sc = new Scanner(System.in);
                BookYourTickets obj = new BookYourTickets(con, sc);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}


