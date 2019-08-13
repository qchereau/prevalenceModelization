# prevalenceModelization
This package makes a modelization of the universe since the big bang with or without the repulsion between matter and antimatter

Contact: quentin.chereau.research@gmail.com

# Disclaimer
This simulation is partly flawed and extremely simple. For any suggestion of improvement, please contact the e-mail adress stated in the contact.

# How to run it
Prerequisite:
Please install java with a version at least 1.8.

Then run the following command in the target folder:
java -classpath prevalenceModelization-0.0.1-SNAPSHOT.jar prevalence.modelization.RepulsionModelization

A panel shall open filled with red circles (representing antimatter) and black circles (representing matter). Afterwards, it shall evolve according to the laws stated in the parameters.

# How to modify it

The configuration part of RepulsionModelization hold all the parameters of the simulation that you can freely modify.

Should you want to make other modifications, please condier the disclaimer session.

This is an eclipse project. Therefore, once loaded in eclipse you should be able to run directly the run command over the RepulsionModelization class. To see the modification in this situation, change a parameter than run the class.

Should you want to do without eclipse, you can consider:
1. Install maven on your environment
2. Modify your class
3. Run mvn clean install in the root folder
4. Run the command line to run the project. You shall see the updated version.
