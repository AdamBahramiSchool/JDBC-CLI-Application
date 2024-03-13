package net.codejava.sql;


import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import java.sql.PreparedStatement;

public class Config {
	private static Connection getConnection() throws SQLException {
		String url="jdbc:sqlserver://cypress.csil.sfu.ca;databaseName=____";
		String usernameString="___";
		String passwordString="___";
		return DriverManager.getConnection(url, usernameString, passwordString);
	}
	
	 private static String logIn(Connection connection) {
	        boolean userChecker = false;
	        String query = "SELECT user_id FROM dbo.user_yelp WHERE user_id=?";
	        String userName = "";
	        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	            Scanner scanner = new Scanner(System.in);
	            while (!userChecker) {
	                System.out.println("Enter a valid user ID: ");
	                userName = scanner.nextLine();
	                pstmt.setString(1, userName);
	                ResultSet rs = pstmt.executeQuery();
	                if (rs.next()) {
	                    userChecker = true;
	                    System.out.println("Congrats you are a valid User!\n");
	                } else {
	                    System.out.println("Invalid user ID. Try again.\n");
	                }
	            }
	            return userName;
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return userName;
	    }
	 	private static void makeFriends(int userId) {
	 		
	 		return;
	 	}
	    private static boolean greetings(Connection connection, Scanner scanner, String loggedinUser) {
	        boolean condition = true;
	        System.out.println("Welcome to the Yelp database! Here's a catalog describing my services."
	                + " Type the number beside the function to perform that service\n"
	                + "1. Search Businesses\n"
	                + "2. Search Users\n"
	                + "3. Make Friend\n"
	                + "4. Review Business\n"
	                + "5. Exit the Program\n");
	        try {
	        Double functionNumber = scanner.nextDouble();
	        scanner.nextLine(); // Consume the newline character left by nextInt()

	        

	        if (functionNumber == 1) {
	        	try {
	                System.out.println("Enter minimum stars:");
	                Double minNumStars = scanner.nextDouble();
	                scanner.nextLine(); // Consume the newline character left by nextDouble()

	                System.out.println("Enter city:");
	                String city = scanner.nextLine();

	                System.out.println("Enter name:");
	                String name = scanner.nextLine();

	                searchBusiness(minNumStars, city, name, connection, scanner);
	            } catch (InputMismatchException e) {
	                System.out.println("Invalid input for minimum stars. Please enter a valid number next time. \n");
	                scanner.nextLine(); // Consume the invalid input
	            }
	            
	        } else if (functionNumber == 2) {
	        	try {
	        	System.out.println("Please enter the user's name: ");
	        	String userName=scanner.nextLine();
	        	System.out.println("Please enter a minimum review count: ");
	        	Double reviewCount=scanner.nextDouble();
	        	System.out.println("Please enter the minimum average stars count: ");
	        	Double averageStars=scanner.nextDouble();
	            searchUsers(userName,reviewCount,averageStars, connection, scanner, loggedinUser);
	        	}
	        	catch (InputMismatchException e) {
	                System.out.println("The review count and average stars must be number inputs. Please enter a valid number next time. \n");
	                scanner.nextLine(); // Consume the invalid input
	            }
	        } 
	        else if (functionNumber == 3) {
	            while (true) {
	                System.out.println("Please enter a valid user id part of our yelp user database to create a friendship: ");
	                String promptedUser = scanner.nextLine();

	                Boolean validUser = userChecker(promptedUser, connection);
	                Boolean duplicateFriend = duplicateFriend(promptedUser,loggedinUser, connection);

	                if (validUser && !duplicateFriend) {
	                    makeFriend(promptedUser, loggedinUser, connection);
	                    break;
	                } else if (!validUser) {
	                    System.out.println("That user id does not exist in our user yelp database. Friendship not created. Please try again!\n");
	                } else {
	                    System.out.println("Duplicate friend. Friendship not created. Please try again!\n");
	                }
	            }
	        }
	        else if (functionNumber == 4) {
	        	String businessId = getValidBusinessId(scanner, connection);
	            int numStars = getValidNumStars(scanner);
	            reviewBusiness(businessId, numStars, loggedinUser, connection);
		    	
	        } else if (functionNumber == 5) {
	            condition = false; // Exit the program
	        }
	        else {
	        	System.out.println("You need to enter an integer value between 1 to 5! \n");
	        }
	        } catch (InputMismatchException e) {
	            System.out.println("Invalid input. Please enter a valid number. \n");
	            scanner.nextLine(); // Consume the invalid input
	        }
	        return condition;
	    }
	    
	    private static String getValidBusinessId(Scanner scanner, Connection connection) {
	        while (true) {
	            System.out.println("Please enter the business id of the business you would like to review: ");
	            String businessId = scanner.nextLine();
	            Boolean businessValid = businessChecker(businessId, connection);
	            if (businessValid) {
	                System.out.println("Successful Business id entry. Let's carry on reviewing this business!\n");
	                return businessId;
	            } else {
	                System.out.println("This business id does not exist, retry!\n");
	            }
	        }
	        
	    }
	    
	    private static int getValidNumStars(Scanner scanner) {
	        while (true) {
	            try {
	                System.out.println("Please enter a integer number of stars for this business: ");
	                int numStars = scanner.nextInt();
	                if (numStars >= 1 && numStars <= 5) {
	                    // Consume the remaining newline character
	                    scanner.nextLine();
	                    return numStars;
	                } else {
	                    System.out.println("Error: Please enter a integer between 1 and 5.\n");
	                }
	            } catch (InputMismatchException e) {
	                // Handle the exception when the input is not a valid float
	                System.out.println("Error: Please enter a valid integer number.");

	                // Consume the invalid input to avoid an infinite loop
	                scanner.nextLine();
	            }
	        }
	    }

	    
	    private static boolean businessChecker(String businessId, Connection connection) {
	        boolean condition = false;
	        String sql = "SELECT business_id FROM dbo.business WHERE business_id = ?";

	        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	            pstmt.setString(1, businessId);
	            try (ResultSet rs = pstmt.executeQuery()) {
	                condition = rs.next();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return condition;
	    }

	    
	    private static boolean duplicateFriend(String promptedUser,String loggedinUser, Connection connection) {
	    	boolean condition=false;
	    	String sql="SELECT COUNT(*) FROM dbo.friendship WHERE user_id = ? AND friend = ?";
	    	try {
				PreparedStatement pstmt=connection.prepareStatement(sql);
				pstmt.setString(1,loggedinUser);
				pstmt.setString(2,promptedUser);
				 try (ResultSet rs = pstmt.executeQuery()) {
			            if (rs.next()) {
			                int count = rs.getInt(1);
			                condition = count > 0; 
			            }
			        }
			    } catch (SQLException e) {
			        e.printStackTrace();
			    }

			    return condition;
	    }
	    
	    private static boolean userChecker(String promptedUser, Connection connection) {
	    	boolean condition=false;
	    	//logic here
	    	String sql="SELECT user_id FROM dbo.user_yelp";
	    	try {
				PreparedStatement pstmt=connection.prepareStatement(sql);
				ResultSet rs=pstmt.executeQuery();
				while(rs.next()) {
					String user=rs.getString("user_id");
					if(promptedUser.equals(user))
					{
						condition=true;
						break;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	return condition;
	    	
	    }
	    
	    private static void makeFriend(String promptedUser, String loginUser, Connection connection) {
	        String sql = "INSERT INTO dbo.friendship(user_id, friend) VALUES (?, ?)";
	        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	            pstmt.setString(1, loginUser);
	            pstmt.setString(2, promptedUser);

	            int rowsAffected = pstmt.executeUpdate();

	            if (rowsAffected > 0) {
	                System.out.println("Friendship created successfully! \n");
	            } 
	        } catch (SQLException e) {
	        	e.printStackTrace();
	            System.out.println("Failed to create friendship, as they're already friends. \n");
	        }
	    }


	    private static void searchUsers(String userName, Double reviewCount, Double averageStars, Connection connection,Scanner scanner, String loggedinUsers) {
	    	Map<String,Integer> hmUsers=new HashMap<>();
	    	
	    	try {
	    		String sql = "SELECT user_id, name, review_count, useful, funny, cool, average_stars, yelping_since FROM dbo.user_yelp WHERE name LIKE ? AND review_count >= ? AND average_stars >= ? ORDER BY name";
				PreparedStatement pstmt=connection.prepareStatement(sql);
				pstmt.setString(1,"%"+userName+"%");
				pstmt.setDouble(2,reviewCount);
				pstmt.setDouble(3,averageStars);
				ResultSet rs=pstmt.executeQuery();
				if(!rs.next()) {
					System.out.println("Sorry we could not find any users matching your criteria. As a result, we cannot make friends either :( \n");
					return;
				}
				int index=0;
				do
				{
					String user=rs.getString("user_id");
					String name=rs.getString("name");
					Double reviewCountNum=rs.getDouble("review_count");
					Double usefulCount=rs.getDouble("useful");
					Double funnyCount=rs.getDouble("funny");
					Double coolCount=rs.getDouble("cool");
					Double averageStarsCount=rs.getDouble("average_stars");
					String dateTime=rs.getString("yelping_since");
					hmUsers.put(user,index);
					index++;
					System.out.println("User ID: " + user +
			                   ", Name: " + name +
			                   ", Review Count: " + reviewCountNum +
			                   ", Useful Count: " + usefulCount +
			                   ", Funny Count: " + funnyCount +
			                   ", Cool Count: " + coolCount +
			                   ", Average Stars: " + averageStarsCount +
			                   ", Yelping Since: " + dateTime+"\n");
							
				}while(rs.next());
				scanner.nextLine();
				//call function make friend with the hashmap hmUsers as parameter. If input user_id in hmUsers, add userId with userId of logged in user
				
				System.out.println("Would you like to form a friendship with a prompted user from searchUsers and the logged in user?\n"
	                    + "Type y or n");
				String resultString = scanner.nextLine();
			
				String capitalizedResult = resultString.toUpperCase();
				if (capitalizedResult.equals("Y") || capitalizedResult.equals("YES")) {
				    while (true) {
				        System.out.println("Enter a user Id from the results of the searchUsers function to create a friendship with the logged in user: ");
				        String promptedUserId = scanner.nextLine().trim();
			
				        if (hmUsers.containsKey(promptedUserId)) {
				            makeFriend(promptedUserId, loggedinUsers, connection);
				            break;
				        } else {
				            System.out.println("Prompted user_id was not part of userSearch user_id results. Try again \n");
				        }
				    }
				} else if (capitalizedResult.equals("N") || capitalizedResult.equals("NO")) {
				    System.out.println("See you later then! \n");
				} else {
				    System.out.println("I didn't understand, let's bring you back to the main menu :) \n");
				}

				
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	return;
	    }
	    
	    public static String generateRandomId(int length, Connection connection) throws SQLException {
	        String VALID_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	        SecureRandom random = new SecureRandom();
	        StringBuilder sb = new StringBuilder(length);
	        while (true) {
	            for (int i = 0; i < length; i++) {
	                int randomIndex = random.nextInt(VALID_CHARACTERS.length());
	                char randomChar = VALID_CHARACTERS.charAt(randomIndex);
	                sb.append(randomChar);
	            }

	            String generatedId = sb.toString();
	            if (!reviewIdExists(generatedId, connection)) {
	                return generatedId;
	            }

	            sb.setLength(0); 
	        }
	    }

	    // Check if the review_id exists in dbo.review
	    private static boolean reviewIdExists(String reviewId, Connection connection) throws SQLException {
	        String sql = "SELECT review_id FROM dbo.review WHERE review_id = ?";
	        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	            pstmt.setString(1, reviewId);
	            try (ResultSet rs = pstmt.executeQuery()) {
	                return rs.next();
	            }
	        }
	    }
	    
	    private static void reviewBusiness(String businessId, int numStars, String loggedInUser, Connection connection) {
	        String randomReviewid = null;
	        try {
	            randomReviewid = generateRandomId(22, connection);
	        } catch (SQLException e1) {
	            e1.printStackTrace();
	            return; // Exit the method if ID generation fails
	        }

	        LocalDateTime currentDateTime = LocalDateTime.now();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
	        String formattedDateTime = currentDateTime.format(formatter);
	        LocalDateTime dateTime = LocalDateTime.parse(formattedDateTime, formatter);
	        Timestamp timestamp = Timestamp.valueOf(dateTime);

	        String sql = "INSERT INTO dbo.review(review_id,user_id,business_id,stars,date) VALUES (?,?,?,?,?)";

	        try {
	            connection.setAutoCommit(false); 

	            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	                pstmt.setString(1, randomReviewid);
	                pstmt.setString(2, loggedInUser);
	                pstmt.setString(3, businessId);
	                pstmt.setInt(4, numStars);
	                pstmt.setTimestamp(5, timestamp);

	                int rowsAffected = pstmt.executeUpdate();

	                if (rowsAffected > 0) {
	                    connection.commit();
	                    System.out.println("Review made with Review ID: "+randomReviewid);
	                } else {
	                    // Rollback the transaction if no rows were affected
	                    connection.rollback();
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            try {
	                // Rollback the transaction in case of an exception
	                connection.rollback();
	            } catch (SQLException rollbackException) {
	                rollbackException.printStackTrace();
	            }
	        } finally {
	            try {
	                connection.setAutoCommit(true); // Ensure auto-commit is enabled for subsequent operations
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    //trigger already takes care of what updateBusiness does so dont need to call it
	    private static void updateBusiness(String businessId, Float stars, Connection connection) {
	    	String sql="SELECT review_count, stars FROM dbo.business WHERE business_id=?";
	    	try {
				PreparedStatement pstmt=connection.prepareStatement(sql);
				ResultSet rs=pstmt.executeQuery();
				if(rs.next()) {
					int previousReviewCount=rs.getInt("review_count");
					Float previousStars=rs.getFloat("stars");
					int newReviewCount=previousReviewCount+1;
					Float newStarsAverage=((previousStars*previousReviewCount)+stars/newReviewCount);
					String sql2="UPDATE dbo.business SET stars=?, review_count=?";
					PreparedStatement pstmt2=connection.prepareStatement(sql2);
					pstmt2.setFloat(1,newStarsAverage);
					pstmt2.setInt(2,newReviewCount);
					pstmt2.executeUpdate();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    }
	    
	    private static void searchBusiness(Double minNumStars, String city, String name, Connection connection, Scanner scanner) {
	        String capitalizedName = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
	        String capitalizedCity = city.substring(0, 1).toUpperCase() + city.substring(1).toLowerCase();
	        String ordering = "";

	        System.out.println("Choose your ordering by typing the number associated with the attribute: Order Result by"
	                + " 1. name"
	                + " 2. city"
	                + " 3. number of stars");

	        int orderResult;
	        while (true) {
	            try {
	                orderResult = scanner.nextInt();
	                if (orderResult >= 1 && orderResult <= 3) {
	                    break; // Exit the loop if a valid number is entered
	                } else {
	                    System.out.println("Please enter a valid integer between 1 and 3.");
	                }
	            } catch (java.util.InputMismatchException e) {
	                System.out.println("Invalid input. Please enter the valid options either 1,2 or 3.");
	                scanner.nextLine(); // Consume the invalid input
	            }
	        }

	        if (orderResult == 1) {
	            ordering = "name";
	        } else if (orderResult == 2) {
	            ordering = "city";
	        } else if (orderResult == 3) {
	            ordering = "stars";
	        }

	        String sql = "SELECT * FROM dbo.business WHERE stars>=? AND name LIKE ? AND city = ? ORDER BY " + ordering;

	        try {
	            PreparedStatement pstmt = connection.prepareStatement(sql);
	            pstmt.setDouble(1, minNumStars);
	            pstmt.setString(2, "%" + capitalizedName + "%");
	            pstmt.setString(3, capitalizedCity);

	            ResultSet rs = pstmt.executeQuery();

	            if (!rs.next()) {
	                System.out.println("Sorry, there are no results of businesses matching your criteria");
	                return;
	            }

	            do {
	                String address = rs.getString("address");
	                String businessId = rs.getString("business_id");
	                String businessName = rs.getString("name");
	                String cityString = rs.getString("city");
	                Double numStars = rs.getDouble("stars");
	                System.out.println("Business_ID: " + businessId + ", Name: " + businessName + ", Address: " + address +
	                        ", City: " + cityString + ", Stars: " + numStars+"\n");
	            } while (rs.next());

	            rs.close();
	            pstmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }


	    public static void main(String[] args) {
	        Connection connection;
	        boolean greetingsCondition = true;
	        try {
	            connection = getConnection();
	            Scanner scanner = new Scanner(System.in);
	            String loggedinUser=logIn(connection);
	            while (greetingsCondition) {
	                greetingsCondition = greetings(connection, scanner,loggedinUser);
	            }
	            System.out.println("Here's where I terminate. Thanks for your time! \n");
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}
