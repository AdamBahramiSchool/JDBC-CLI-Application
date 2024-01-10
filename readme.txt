All the code is in Config.java and can be ran through the executable jar file assignment7executable.jar

1. Immediately you will be prompted by the login method to enter a valid user_id which is contained in the dbo.user_yelp relation.
   If a valid user_id is not entered, you will be prompted to enter it again until you enter a valid user_id

2. Once authenticated and logged in, you will be prompted with a menu of 5 options. Simply type the number associated with the method
   to perform that method. Invalid inputs are taken care of and the menu is in a loop until the user finally exits the program by typing
   5. This is all enlisted in my method greetings


Option 1- Typing 1, calls the searchBusiness method: Takes in the user prompt's for the minimum number of stars, the city and the name
          of the business. Retrieves and queries tuples using a preparedstatement. Invalid inputs are also handled and may bring you back
          to menu if stars input is not valid 

Option 2- Typing 2, calls the searchUsers function taking in the prompted user inputs including user_id, minimum review_count and the minimum average stars. If these inputs are not valid, the user will be brought back to the main menu and the error is handled. If the inputs are valid, the preparedStatement searches the users with the prompted inputs and once found, presents the user with an option to create a friendship between the logged in user and a user_id part of the result set of the searchUsers function. So if the user types y (yes), the user ***MUST*** input a result user_id from the searchUsers function, or else the input user_id will not be found. The EASIEST way is to copy a resulting user_id from the terminal and input it. This results in a friendship between the logged in user (user) and a user resulting from the SearchUsers function (friend).

Option 3-Typing 3, calls the makeFriend function. This could also be called when the user inputs 2 if they select the further option to make friends with a resulting searchUsers user. As for the makefriend function, before forming a friendship between the logged in user and the prompted user_id part of the dbo.user_yelp relation, the program checks if the prompted user_id is inside the dbo.user_yelp relation and ensures that the friendship between the loggedin user and the inputted user doesn't exist already, avoiding duplicates. Then makeFriend performs its functionality with an INSERT operation using a preparedStatement to create the result in the dbo.friendship table.

Option 4- Typing 4, calls the reviewBusiness method. Before performing its functionality, the user inputs for business id and stars are validated to see that the business id exists in the dbo.business relation and that the stars inputted represent an integer from 1 to 5. After the checks, I created a randomId generator method and called it inside my reviewBusiness method to use it as a distinct review_id, and duplicate checks are in place so the same review_id won't be formed. Furthermore, I retrieved the current date using the timestamp approach incorporated in the java.time package. Then, with my parameters (review_id,user_id,business_id,stars,date), I performed an insert operation with a preparedStatement and once the review is placed in the dbo.review relation, I display the review_id to the user in case they want to search by the review_id. 

Option 5- Typing 5, this exists the menu loop and allows the user to escape the program.