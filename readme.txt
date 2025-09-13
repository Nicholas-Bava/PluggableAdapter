PluggableAdapter

There are two implementations. The primary implementation is run from the TreeApplication class. The secondary implementation is contained within the hybrid package and is run from the Facade class. This is the Facade implementation of the same program.

IMPORTANT

To run the program, from the root directory, run:

mvn clean compile

Then, to run the program, you can either:

mvn javafx:run -Poriginal

or 

mvn javafx:run


This is because I have profiles set up in the pom.xml file to switch between main classes. TreeApplication is the default but can also be run with the original profile.

In order to run the Facade implementation, you must use the command:

mvn javafx:run -Phybrid
