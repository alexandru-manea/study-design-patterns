package obsever;

import java.util.ArrayList;
import java.util.List;

import java.util.Observable;
import java.util.Observer;

/**
 *  A weather station prototype used to illustrate the Observer Design Pattern.
 *
 */

public class WheatherStation
{
	/*
	 * SCENARIO
	 * 
	 * Internet-based Weather Monitoring Station
	 * 
	 * - WeatherData object: which tracks current weather conditions (temperature, humidity and pressure) (see interface bellow)
	 * 
	 * - Display elements  : initially a current conditions display, a statistics display, and a forecast display
	 * 					   : they must be updated each time WeatherData has new measurements
	 * 					   : system is expandable, other developers can add/remove displays and create their own
	 * 
	 * 
	 * TASK :: Create an API that uses the WeatherData object to update the three displays in real time, as new measurements arrive. 
	 *         How do we notify all of them? Also, developers should be able to write their own displays and plug them right in.
	 * 
	 */

	interface WeatherDataSkeleton // interface just to show the methods
	{
		public double getTemperature(); // return the most recent measurements
		public double getHumidity();    // we don't care HOW they are set
		public double getPressure();    // they are already implemented 

		/* NEED TO IMPLEMENT THIS */
		public void measurementsChanged(); // gets called (don't care how, just that it is) whenever the measurements have changed
	}


	/*
	 * SOLUTION 1
	 * 
	 * Update the displays by manually calling the display element of each one, passing in the most recent measurements.
	 * 
	 */

	class WeatherData_Solution1
	{
		// display objects -- in this solution they reside here; they are actually of another type implementing the Display interface
		Display currentConditionsDisplay, statisticsDisplay, forecastDisplay;

		// mock implementations
		public double getTemperature() {return 25.00;}
		public double getHumidity() {return 80.5;}
		public double getPressure() {return 200.00;}

		// method of interest
		public void measurementsChanged()
		{
			double temperature = getTemperature();
			double humidity = getHumidity();
			double pressure = getPressure();

			currentConditionsDisplay.update(temperature, humidity, pressure);
			statisticsDisplay.update(temperature, humidity, pressure);
			forecastDisplay.update(temperature, humidity, pressure);
		}
	}

	/* mock interface implemented by all display elements just to give a type in the above class */
	interface Display
	{
		public void update(double temperature, double humidity, double pressure);
	}


	/*
	 * <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	 * << PROBLEMS:: $ We are coding to complete implementations, not interfaces      >> 
	 * <<            $ There is no way to add or remove display elements at run time. >> 
	 * <<			 $ For every new display element, we need to alter code.          >>
	 * <<			 $ We are not encapsulating the part that varies.			      >> 
	 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	 */


	/**
	 * *********************************************************************************************************************
	 * DESIGN PATTERN :: OBSERVER
	 * 
	 * DEFINE A ONE-TO-MANY DEPENDENCY BETWEEN OBJECTS SO THAT WHEN ONE OBJECT CHANGES, ALL OF ITS DEPENDANTS ARE NOTIFIED.
	 * *********************************************************************************************************************
	 * 
	 * ANALOGY --> NEWSPAPER SUBSCRIPTION
	 * - You (and others as well) subscribe to a particular publisher
	 * - Every time there is a new edition, it gets delivered to you
	 * - You unsubscribe when you don't want to receive papers anymore
	 * 
	 * => in OBSERVER pattern :: Publisher == SUBJECT
	 * 								:: Subject manages some bit of data
	 * 						  :: Subscribers == OBSERVERS
	 * 								:: The Observers subscribe to the subject to receive updates when the Subject's data changes
	 * 						  :: When data in the Subject changes, the Observers are notified by communicating the new values
	 * *************************************************************************************************************************
	 */

	
	/* Objects use this interface to register/unregister as observers */
	interface Subject_Definition
	{
		public void registerObserver(Observer_Definition observer);
		public void removeObserver(Observer_Definition observer);
		public void notifyObservers();
	}

	/* A concrete subject always implements the Subject inteface */
	class ConcreteSubject_Definition implements Subject_Definition
	{
		List<Observer_Definition> observers; // IMPORTANT

		public void registerObserver(Observer_Definition observer) {}
		public void removeObserver(Observer_Definition observer) {}
		public void notifyObservers() {}
	}

	/* All potential observers need to implement the Observer interface */
	interface Observer_Definition
	{
		public void update(); // gets called when the Subject's state changes
	}

	class ConcreteObserver_Definition implements Observer_Definition
	{
		public void update() {}

	}


	/**
	 * ------------------------------------------------------------------------------------
	 * DESIGN PRINCIPLE :: STRIVE FOR LOOSELY COUPLED DESIGNS BETWEEN OBJECTS THAT INTERACT
	 * ------------------------------------------------------------------------------------
	 * 
	 * The OBSERVER pattern provides a design where subjects and observers are loosely coupled, i.e. they interact, but with 
	 * little knowledge of each other => flexible OO systems. Why?
	 * 
	 * - The only thing the subject knows about an observer is that it implements a certain interface (the Observer interface).
	 *   
	 * - We can add/remove/replace observers at any time. The subject only depends on a list of object that implement Observer.
	 * 
	 * - We don't need to modify the subject if we want to add new types of observers.
	 * 
	 * - We can reuse subjects and observers independently of each other.
	 * 
	 * - Changes to either one will not affect the other.   
	 * 
	 */


	/*
	 * SOLUTION 2 (FINAL)
	 * 
	 * Use the (hand-written) Observer Pattern.
	 * 
	 */


	/* Subject interface required in the pattern */
	interface Subject_Solution2
	{
		public void registerObserver(Observer_Solution2 observer);
		public void removeObserver(Observer_Solution2 observer);

		public void notifyObservers();
	}


	/* Concrete subject class */
	class WeatherData_Solution2 implements Subject_Solution2
	{
		// list of observers
		private List<Observer_Solution2> observers = new ArrayList<Observer_Solution2>();;

		// state
		private double temperature;
		private double humidity;
		private double pressure;


		public void registerObserver(Observer_Solution2 observer)
		{
			this.observers.add(observer);
		}

		public void removeObserver(Observer_Solution2 observer)
		{
			this.observers.remove(observer);
		}


		// notify all observers of the new state -- easy because they all implement Observer
		public void notifyObservers()
		{
			for (Observer_Solution2 observer: this.observers)
			{
				observer.update(this.temperature, this.humidity, this.pressure);
			}
		}

		// this method gets called (artificially from the next method) when new measurements are available
		public void measurementsChanged()
		{
			notifyObservers();
		}


		// simulative method 
		public void setMeasurements(double temperature, double humidity, double pressure)
		{
			this.temperature = temperature;
			this.humidity = humidity;
			this.pressure = pressure;

			// trigger method
			measurementsChanged();
		}
	}


	/* Observer interface required in the pattern */
	interface Observer_Solution2
	{
		public void update(double temperature, double humidity, double pressure); // update with the new state values of the Subject
	}

	/* Further interface for display elements */
	interface DisplayElement_Solution2
	{
		public void display();
	}


	/* Concrete observer 1 -- only one implemented*/
	class CurrentConditionsDisplay_Solution2 implements Observer_Solution2, DisplayElement_Solution2
	{
		// reference to the subject used to register/unregister as an observer
		private Subject_Solution2 weatherData;

		// last received values
		private double temperature;
		private double humidity;


		/* Constructor -- save the Subject reference and register to it */
		public CurrentConditionsDisplay_Solution2(Subject_Solution2 weatherData)
		{
			this.weatherData = weatherData;
			weatherData.registerObserver(this);
		}


		public void update(double temperature, double humidity, double pressure)
		{
			this.temperature = temperature;
			this.humidity = humidity;

			display();
		}

		// display current measurements
		public void display()
		{
			System.out.println("Current conditions: " + temperature + " C degrees and " + humidity + "% humidity");
		}
	}

	/* Concrete observer 2 */
	class StatisticsDisplay_Solution2 implements Observer_Solution2, DisplayElement_Solution2
	{
		public void update(double temperature, double humidity, double pressure){}

		// display average, min and max measurements
		public void display(){}
	}

	/* Concrete observer 3 */
	class ForecastDisplay_Solution2 implements Observer_Solution2, DisplayElement_Solution2
	{
		public void update(double temperature, double humidity, double pressure){}

		// display the forecast
		public void display(){}
	}

	/* New concrete observer. Developers can implement the Observer and Display interfaces to create their own display elements */
	class ThirdPartyDisplay_Solution2 implements Observer_Solution2, DisplayElement_Solution2
	{
		public void update(double temperature, double humidity, double pressure){}

		// display smt else
		public void display(){}
	}


	/**
	 * JAVA's BUILT-IN OBSERVER PATTERN
	 * 
	 * Java has built-in support for the Observer pattern in several of its APIs. The most general is the OBSERVABLE class and the 
	 * OBSERVER interface in the java.util package. {OBSERVABLE == SUBJECT}
	 * 
	 * 
	 * FOR AN OBSERVABLE TO SEND NOTIFICATIONS
	 * 1. Extend java.util.Observable
	 * 2. Call the setChanged() method to signify that the state of the object has changed. 
	 *    (without it, the observers will NOT be notified. Why? More flexibility - send notifications only when you want.)
	 * 3. Call notifyObservers() method, either
	 * 		a. notifyObservers() or 
	 * 		b. notifyObservers(Object arg) - takes an arbitrary data object that gets passed to each Observer
	 * 
	 * FOR AN OBSERVER TO RECEIVE NOTIFICATIONS
	 * 1. Implement java.util.Observer
	 * 2. Call addObserver() on an Observable object
	 * 3. Implement the update method
	 * 	  update(Observable observable, Object arg) - where arg is the data object passed to notifyObservers or null if none specified.
	 *    If you want to push data to the observers, you can pass it in the data object to the notifyObserver(arg) method. If not,
	 *    the Observer has to pull the data it wants from the Observable object passed to it via its getters.
	 * 
	 * 
	 * ADVANTAGES
	 * - A lot of functionality out of the box
	 * - Can implement either a push or pull style of update
	 * 
	 * DISADVANTAGES
	 * - Observable is a class => we need to subclass it => limited reusability
	 * - Observable has methods such as setChanged() as protected => cannot call it without subclassing (see protected visibility)
	 *   => cannot create instance of Observable class and compose it with own objects , you have to subclass {composition better}
	 * 
	 * NOTE :: There are other places in the JDK where the Observer Pattern is present. E.g., Swing listeners.
	 * 
	 */


	/*
	 * JAVA BUILT-IN OBSERVER PATTERN SOLUTION
	 * 
	 * Use the Observable class and the Observer interface in java.util.
	 */


	class WeatherData_SolutionJava extends Observable
	{
		// state
		private double temperature;
		private double humidity;
		private double pressure;

		// OBS --> don't need to keep track of observers anumore, or manage registration/removal


		// method required in specification
		public void measurementsChanged()
		{
			setChanged(); // indicate the state has changed
			notifyObservers(); // we're not sending a data object with the call => PULL model
		}


		// simulative method 
		public void setMeasurements(double temperature, double humidity, double pressure)
		{
			this.temperature = temperature;
			this.humidity = humidity;
			this.pressure = pressure;

			// trigger method
			measurementsChanged();
		}

		
		// getters used in the PULL model -- observers will use them to tet at the instance variables
		public double getTemperature()
		{
			return temperature;
		}

		public double getHumidity()
		{
			return humidity;
		}

		public double getPressure()
		{
			return pressure;
		}
	}


	class CurrentConditionsDisplay_SolutionJava implements Observer
	{
		Observable observable;
		
		private double temperature;
		private double humidity;
		
		
		/* Constructor (same as our solution, but with Observable) -- save the Subject reference and register to it */
		public CurrentConditionsDisplay_SolutionJava(Observable observable)
		{
			this.observable = observable;
			observable.addObserver(this);
		}
		
		
		/* Method now takes both an Observable and an optional data argument (if none, then PULL what we need) */
		public void update(Observable observable, Object arg)
		{
			// safety check, then PULL
			if (observable instanceof WeatherData_SolutionJava)
			{
				WeatherData_SolutionJava weatherData = (WeatherData_SolutionJava) observable;
				this.temperature = weatherData.getTemperature();
				this.humidity = weatherData.getHumidity();
				
				display();
			}
		}
		
		// mock implementation
		public void display(){}
	}


	/**
	 * $$$ CONCLUSIONS UPDATED $$$
	 * 
	 * OO BASICS
	 * - ABSTRACTION
	 * - ENCAPSULATION
	 * - POLYMORPHISM
	 * - INHERITANCE
	 * 
	 * OO PRINCIPLES
	 * - ENCAPSULATE WHAT VARIES
	 * - FAVOR COMPOSITION OVER INHERITANCE
	 * - PROGRAM TO AN INTERFACE, NOT AN IMPLEMENTATION
	 * + STRIVE FOR LOOSELY COUPLED DESIGNS BETWEEN OBJECTS THAT INTERACT
	 * 
	 * OO PATTERNS
	 * 
	 * - STRATEGY: DEFINE A FAMILY OF ALGORITHMS, ENCAPSULATE EACH ONE, AND MAKE THEM INTERCHANGEABLE. ALLOW THEM TO VARY
	 * INDEPENDENTLY FROM THE CLIENTS THAT USE THEM
	 * + OBSERVER: DEFINE A ONE-TO-MANY DEPENDENCY BETWEEN OBJECTS SO THAT WHEN ONE OBJECT CHANGES STATE, ALL ITS DEPENDENTS
	 * ARE NOTIFIED AND UPDATED AUTOMATICALLY
	 * 
	 */
}
