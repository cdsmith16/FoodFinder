PROJECT: SF Food Trucks
TRACK: Full
Tech Choices: Java/Maven for core functionality (familiar); Jax-RS/Jersey server (First time using); Heroku/Git hosting and version control (still inexperienced, need work)
Trade-offs: Java applet as front-end instead of web client. Detailed below
Other Work: https://github.com/csmooth/Jars


FoodFinder is a application that queries data.sfgov.org as well as Google Maps in order to find the food trucks nearby a given
location. The current client is a runnable jar file which has all the necessary package elements included. The application allows you
to enter in either an address or latitude/longitude coordinates and outputs a list of nearby food trucks.  The back end includes 
FoodHandler.java, FoodFinder.java, and FoodFrame.java, as well as many referenced supporting classes and packages. 

**FoodHandler** handles
the queries to SFData and/or Google Maps, as well as light parsing of the JSON received from either GET request.

**FoodFinder** serves as a RESTful Jax-rs/Jersey server which takes in two simple GET requests: 
/findfood/(lat)&(lon) and 
/findfood/(address) .
In the interest of time, I was unable to properly host the server on Heroku or Amazon currently, and thus this REST api is not currently
serving any clients.

**FoodFrame** acts as a simple Java client that essentially copies the two GET requests featured by **FoodFinder** and allows users
to make said calls from the Java applet. Going forward, this would be replaced by a full html/css/javascript client that would make
calls to **FoodFinder**'s API, but **FoodFrame** serves as a proof of concept for the functionality of FoodFinder as a whole.

Details of implementation of each class are commented/explained in the java files themselves.

@Author Christian D. Smith