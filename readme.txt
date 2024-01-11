# JDBC-CLI-Application

**README for Yelp CLI Application**

All the code is in `Config.java` and can be executed using the executable JAR file `assignment7executable.jar`.

**Login Authentication:**
# JDBC-CLI-Application

**README for Yelp CLI Application**

All the code is in `Config.java` and can be executed using the executable JAR file `assignment7executable.jar`.

**Login Authentication:**

Upon execution, the login method prompts the user to enter a valid `user_id` from the `dbo.user_yelp` relation.
If an invalid `user_id` is entered, the user is prompted to re-enter until a valid `user_id` is provided.

**Main Menu:**

After successful authentication, the user is presented with a menu of five options.
Enter the number associated with the desired method to execute. Invalid inputs are handled gracefully.
The menu is in a loop until the user chooses to exit the program by typing 5.

**Option 1 - Search Businesses:**

Typing 1 invokes the `searchBusiness` method.
Prompts the user for minimum stars, city, and business name.
Uses a `PreparedStatement` to retrieve and query tuples.
Handles invalid inputs, bringing the user back to the menu if the stars input is not valid.

**Option 2 - Search Users and Create Friendship:**

Typing 2 calls the `searchUsers` function, taking user inputs for `user_id`, minimum `review_count`, and minimum average `stars`.
Handles invalid inputs, bringing the user back to the main menu.
If inputs are valid, prompts the user to create a friendship between the logged-in user and a user from the result set.
Ensures the entered `user_id` for friendship is valid, resulting in a friendship between the logged-in user and the selected user.

**Option 3 - Make Friendship:**

Typing 3 calls the `makeFriend` function.
Validates that the prompted `user_id` is in `dbo.user_yelp` and that a friendship doesn't already exist.
Performs an INSERT operation with a `PreparedStatement` to create the friendship in the `dbo.friendship` table.

**Option 4 - Review Business:**

Typing 4 invokes the `reviewBusiness` method.
Validates user inputs for business ID existence and stars as an integer from 1 to 5.
Generates a distinct `review_id` using a random ID generator method.
Performs checks to avoid duplicate review IDs.
Inserts the review into `dbo.review` with a `PreparedStatement`.
Displays the generated `review_id` for potential future searches.

**Option 5 - Exit Program:**

Typing 5 exits the menu loop, allowing the user to gracefully terminate the program.
