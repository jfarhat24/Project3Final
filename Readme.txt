CIS 427 Project 3
README
Chase Best, Joseph Farhat, and Amr Mashrah
Github Link: 
Commands Implemented:
-       Solve command
-       Flags of either -r or -c to decide if you want to use find a circumference of a circle or perimeter and area of a rectangle
-       The shutdown command allows for the server to be shutdown and exits the program
-       Attempted other commands but didn’t work out. (Explained in bugs)
Build and Run: You build and run the program by first starting up the server before the client, so the client has something to connect to. The program does not have the login feature due to errors and not being able to figure it out correctly. When the program is running…
-        You can say hello to the server
-       Type “solve” followed by one or two numbers and then flag either -c or -r depending on if you want the circumference of the circle or the perimeter and area of a rectangle with the inserted user measurements
-       You may choose to do more than one solver after the other or follow to the next step.
-       Type “Shutdown” to shut the server down and exit the program.
Known Bugs:
-       Unfortunately, we were not able to figure out the login part 100% for our program so we had to leave that part out and go right into the program when connecting the server and the client together.
-       Not able to send data information to list/ solutions file
-       Logout is not able to run
-       Server no longer can create the Root_Solutions.txt and attempts to delete the file causes server to crash
-       Allowing root access to all list’s functions but there us no way to create lists of other usernames currently
-	Message was implemented but was not able to function properly.
Output at client side with all commands implemented:

Send command to server:	hello
Server says: hello client!
Send command to server:	list
Server says: root
line 1: These two lines were written into the root_solutions document during the project's run time.
Final Line: This shows that, while the solve print does not work, the actual line print of the document does.

Send command to server:	solve 2 3 -r
Server says: Sides 2.0 3.0: Rectangle’s perimeter and area is: 10.0, 6.0
Send command to server:	solve 2 -r
Server says: Sides 2.0 2.0: Square’s perimeter and area is: 8.0, 4.0
Send command to server:	solve 3 -c
Server says: Circumference of circle is: 18.84
Send command to server:	list -all
Server says: You have root access, but this file does not have the ability to have other users or access their files. Please try again without the -all.
Send command to server:	shutdown
Server says: quit
200 OK
Process finished with exit code 0
