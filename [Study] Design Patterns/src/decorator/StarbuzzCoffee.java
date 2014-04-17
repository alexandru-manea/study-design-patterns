package decorator;

import java.io.*;

/**
 * An ordering system for a coffee shop used to illustrate the Decorator Design Pattern.
 * 
 */

public class StarbuzzCoffee
{
	/*
	 * SCENARIO/INITIAL DESIGN
	 * 
	 * The Starbuzz coffee shop sells bevarages, currently 4 types: HouseBlend, DarkRoast, Decaf and Espresso, each with a different price.
	 * The following classes illustrate how the inital class design was developed.
	 * 
	 */
	
	abstract class Beverage_Initial
	{
		private String description = "Unknown Bevarage";
		
		public String getDescription() {return this.description;}
		
		// abstract cost method -- subclasses each implement it to set their own cost
		public abstract double cost();
	}
	
	
	/* Subclasses of Bevarage are the actual bevarages offered in the shop */
	
	class HouseBlend_Initial extends Beverage_Initial
	{
		public double cost() {return 0.89;}
	}
	
	class DarkRoast_Initial extends Beverage_Initial
	{
		public double cost() {return 0.99;}
	}
	
	class Decaf_Initial extends Beverage_Initial
	{
		public double cost() {return 1.05;}
	}
	
	class Espresso_Initial extends Beverage_Initial
	{
		public double cost() {return 1.99;}
	}
	
	
	/*
	 * SPECIFICATION CHANGE
	 * 
	 * In addition to cofee, you can also asj for several condiments like steamed milk, soy, mocha and whipped milk. Starbuzz charges
	 * a bit for each of these, so they really need to get them built into their system.
	 * 
	 */
	
	
	/*
	 * SOLUTION 1
	 * 
	 * Have a whole bunch of classes - one for each possible combination of base coffee with condiments. Each cost() method in the
	 * subclasses compute the cost of the coffee, along with the condiments it has.
	 * 
	 */
	
	
	// same as before
	abstract class Beverage_Solution1
	{
		private String description = "Unknown Bevarage";
		
		public String getDescription() {return this.description;}
		
		// abstract cost method -- subclasses each implement it to set their own cost
		public abstract double cost();
	}
	
	
	/* only three subclasses shown here */
	
	class HouseBlendWithMilkandMocha_Solution1 extends Beverage_Solution1
	{
		public double cost() {return 1.15;}
	}
	
	class DarkRoastWithSoyandCaramel_Solution1 extends Beverage_Solution1
	{
		public double cost() {return 1.25;}
	}
	
	class DarkRoastWithWhip_Solution1 extends Beverage_Solution1
	{
		public double cost() {return 1.05;}
	}
	
	
	/*
	 * <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	 * << PROBLEMS:: $ Class explosion       							>> 
	 * <<            $ Maintainance nightmare							>>
	 * <<				 # What happens when the price of milk goes up? >>
	 * <<				 # What fo they do when they add a new topping? >>
	 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	 */
	
	
	/*
	 * SOLUTION 2
	 * 
	 * Add instance variables in the base Beverage class to represent whether or not each beverage has milk, soy, etc.
	 * 
	 */
	
	
	abstract class Beverage_Solution2
	{
		private String description = "Unknown Bevarage";
		private boolean milk, soy, mocha, whip; // new addition
		
		public String getDescription() {return this.description;}
		
		public boolean isMilk() {return milk;}
		public void setMilk(boolean milk) {this.milk = milk;}
		public boolean isSoy() {return soy;}
		public void setSoy(boolean soy) {this.soy = soy;}
		public boolean isMocha() {return mocha;}
		public void setMocha(boolean mocha) {this.mocha = mocha;}
		public boolean isWhip() {return whip;}
		public void setWhip(boolean whip){this.whip = whip;}

		// no longer astract, now computes the cost for all the condiments
		public double cost()
		{
			double totalCostOfCondiments = 0.0;
			
			if (isMilk())
				totalCostOfCondiments += 0.1;
			if (isSoy())
				totalCostOfCondiments += 0.15;
			// same for the rest
			
			return totalCostOfCondiments;
		}
	}
	
	
	class HouseBlend_Solution2 extends Beverage_Solution2
	{
		private double houseBlendCost = 0.8;
		
		// overriden to compute the total cost (= cost of the base beverage + the condiments)
		public double cost()
		{
			return houseBlendCost + super.cost();
		}
	}
	
	class DarkRoast_Solution2 extends Beverage_Solution2
	{
		private double darkRoastCost = 0.9;
		
		// overriden to compute the total cost (= cost of the base beverage + the condiments)
		public double cost()
		{
			return darkRoastCost + super.cost();
		}
	}
	
	class Decaf_Solution2 extends Beverage_Solution2
	{
		private double decafCost = 1.05;
		
		// overriden to compute the total cost (= cost of the base beverage + the condiments)
		public double cost()
		{
			return decafCost + super.cost();
		}
	}
	
	class Espresso_Solution2 extends Beverage_Solution2
	{
		private double espressoCost = 1.5;
		
		// overriden to compute the total cost (= cost of the base beverage + the condiments)
		public double cost()
		{
			return espressoCost + super.cost();
		}
	}
	
	
	/*
	 * <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	 * << PROBLEMS:: $ Price changes for condiments will force us to alter existing code       						 >> 
	 * <<            $ New condiments will force us to add new methods and alter the cost mtehod in the superclass   >>
	 * <<			 $ We might have new beverages for which the existing condiments are not appropriate (e.g., tea) >>
	 * <<			 $ What if a costumer wants a double mocha?											             >>
	 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	 */
	
	
	/**
	 * -------------------------------------------------------------------------------------------------------------
	 * DESIGN PRINCIPLE :: CLASSES SHOULD BE OPEN FOR EXTENSION, BUT CLOSED FOR MODIFICATION (OPEN-CLOSED PRINCIPLE)
	 * -------------------------------------------------------------------------------------------------------------
	 * 
	 * The goal is to allow classes to be easily extended to incorporate new behabiour without modifying existing code.
	 * While inheritance is powerful, it doesn't always lead to the most flexible or maintainable designs. When inheritting
	 * behaviour by subclassing, that behaviour is set statically at compile time. In addition, all subclasses have the same
	 * behaviour. If I can extend an object's behaviour through composition, then I can do it dynamically at runtime.
	 * 
	 * Therefore, BY DINAMICALLY COMPOSING OBJECTS, I CAN ADD NEW FUNCTIONALITY BY WRITING NEW CODE, RATHER THAN ALTERING THE
	 * EXISTING ONE => CHANCES OF NEW BUGS MUCH REDUCED.
	 * 
	 * NOTE --> Applying the Open-Closed Principle EVERYWHERE is wasteful, unnecessary, and can lead to over-complex code.
	 * 
	 */
	
	
	/**
	 * *************************************************************************************************************************
	 * DESIGN PATTERN :: DECORATOR
	 * 
	 * ATTACH ADDITIONAL RESPONSIBILITIES TO AN OBJECT DYNAMICALLY. DECORATORS PROVIDE A FLEXIBLE ALTERNATIVE TO SUBCLASSING FOR
	 * EXTENDING FUNCTIONALITY.
	 * **************************************************************************************************************************
	 * 
	 * HOW --> create a decorator class with the same supertype as the object and add a instance variable in the decorator with a
	 * reference to the original object. Add behaviour and delegate to the methods in the wrapped object to use them.
	 * 
	 * # Decorators - same supertype as the objects they decorate => can pass around a decorated object just as the original 
	 * # A decorator adds its own behaviour before or after delegating to the wraped object to do the rest of the job
	 * # Objects can be decorated at any time and with as many decorators as it is required
     *
	 * **************************************************************************************************************************
	 */
	
	
	// basis class/interface -- provides supertype
	abstract class Component_Definition
	{
		public abstract void methodA();
		public abstract void methodB();
		//other methods
	}
	
	// object we're going to dynamically add new behaviour to by wrapping it with a decorator (although it can also be used on its own)
	class ConcreteComponent_Definition extends Component_Definition
	{
		public void methodA() {}
		public void methodB() {}
		//other methods
	}
	
	// the abstract decorator -- implements the same interface or extends the same abstract class as the component they are decorating
	abstract class Decorator_Definition extends Component_Definition
	{
		public abstract void methodA();
		public abstract void methodB();
		//other methods
	}
	
	// concrete decorator A
	class ConcreteDecoratorA_Definition extends Decorator_Definition
	{
		Component_Definition wrappedObject; // each decorator HAS-A (wraps) a component, i.e. it holds a reference to that component
		
		public void methodA() {}
		public void methodB() {}
		//other methods
	}
	
	// concrete decorator B
	class ConcreteDecoratorB_Definition extends Decorator_Definition
	{
		Component_Definition wrappedObject;
		Object newState; // decorators can also extend te state of the component
		
		public void methodA() {}
		public void methodB() {}
		//other methods
	}
	
	
	/*
	 * SOLUTION FINAL
	 * 
	 * Use the Decorator Design Pattern to dynamically add condiments to the beverages.
	 * 
	 */
	
	
	// ABSTRACT COMPONENT -- the supertype common to objects and their decorators
	abstract class Beverage_SolutionFinal
	{
		String description = "Unknown Beverage"; 
		
		public String getDescription() {return this.description;}
		
		// need to implement this in subclasses
		public abstract double cost();
	}
	
	
	/* CONCRETE COMPONENTS -- objects to  be decorated -- only first one explained*/
	
	class HouseBlend_SolutionFinal extends Beverage_SolutionFinal
	{
		// in the constructor, set the description field inherited from Beverage
		public HouseBlend_SolutionFinal()
		{
			description = "House Blend";
		}
		
		// return cost of a house blend, don't worry about condiments in here
		public double cost() 
		{
			return 0.89;
		}
	}
	
	class DarkRoast_SolutionFinal extends Beverage_SolutionFinal
	{
		public DarkRoast_SolutionFinal()
		{
			description = "Dark Roast";
		}
		
		public double cost()
		{
			return 0.99;
		}
	}
	
	class Decaf_SolutionFinal extends Beverage_SolutionFinal
	{
		public Decaf_SolutionFinal()
		{
			description = "Decaf";
		}
		
		public double cost()
		{
			return 1.05;
		}
	}
	
	class Espresso_SolutionFinal extends Beverage_SolutionFinal
	{
		public Espresso_SolutionFinal()
		{
			description = "Espresso";
		}
		
		public double cost()
		{
			return 1.99;
		}
	}
	
	
	// ABSTRACT DECORATOR class -- needs to be interchangeable with a Beverage
	abstract class CondimentDecorator_SolutionFinal extends Beverage_SolutionFinal
	{
		public abstract String getDescription(); // each condiment should have a different description
	}
	
	
	/* CONCRETE DECORATOR classes -- only first one explained */
	
	class Milk_SolutionFinal extends CondimentDecorator_SolutionFinal // IS-A Condiment, so IS-A Beverage
	{
		private Beverage_SolutionFinal beverage; // instance variable to hold the beverage we are wrapping
		
		public Milk_SolutionFinal(Beverage_SolutionFinal beverage)
		{
			this.beverage = beverage;
		}
		
		// to get the full description, delegate to the orig. object, then append
		public String getDescription()
		{
			return beverage.getDescription() + ", Milk"; 
		}
		
		// delegate to the object we're decorating and add cost of Milk condiment
		public double cost()
		{
			return 0.10 + beverage.cost(); 
		}
	}
	
	class Mocha_SolutionFinal extends CondimentDecorator_SolutionFinal
	{
		private Beverage_SolutionFinal beverage;
		
		public Mocha_SolutionFinal(Beverage_SolutionFinal beverage)
		{
			this.beverage = beverage;
		}
		
		public String getDescription()
		{
			return beverage.getDescription() + ", Mocha"; 
		}
		
		public double cost()
		{
			return 0.15 + beverage.cost();
		}
	}
	
	class Soy_SolutionFinal extends CondimentDecorator_SolutionFinal
	{
		private Beverage_SolutionFinal beverage;
		
		public Soy_SolutionFinal(Beverage_SolutionFinal beverage)
		{
			this.beverage = beverage;
		}
		
		public String getDescription()
		{
			return beverage.getDescription() + ", Soy"; 
		}
		
		public double cost()
		{
			return 0.20 + beverage.cost();
		}
	}
	
	class Whip_SolutionFinal extends CondimentDecorator_SolutionFinal
	{
		private Beverage_SolutionFinal beverage;
		
		public Whip_SolutionFinal(Beverage_SolutionFinal beverage)
		{
			this.beverage = beverage;
		}
		
		public String getDescription()
		{
			return beverage.getDescription() + ", Whip"; 
		}
		
		public double cost()
		{
			return 0.30 + beverage.cost();
		}
	}
	
	/*
	 * TESTING METHOD - to show how to create decorated objects
	 */
	public static void main(String[] args)
	{
		StarbuzzCoffee x = new StarbuzzCoffee(); // just need enclosing object
		
		// order an Espresso, with no condiments
		Beverage_SolutionFinal beverage1 = x.new Espresso_SolutionFinal();
		System.out.println(beverage1.getDescription() + " $" + beverage1.cost());
		
		// order a Dark Roast with double Mocha and Whip 
		Beverage_SolutionFinal beverage2 = x.new DarkRoast_SolutionFinal(); // make a DarkRoast object
		beverage2 = x.new Mocha_SolutionFinal(beverage2); 					// wrap it with a Mocha
		beverage2 = x.new Mocha_SolutionFinal(beverage2);					// wrap it with a second Mocha
		beverage2 = x.new Whip_SolutionFinal(beverage2); 					// weap it with a Whip
		System.out.println(beverage2.getDescription() + " $" + beverage1.cost());
	}
	
	
	/**
	 * DOWNSIDES OF THE DECORATOR PATTERN
	 * 
	 * - If you have code that relies on the concrete component's type, decorators will break the code. As long as you only 
	 *   write code against the abstract component type, the use of decorators will remain transparent to your code.
	 *   
	 * - Using this pattern often results in a large number of small classes => harder to understand the API
	 * 
	 * - The complexity of the code needed to instantiate the component is increased using decorators
	 * 
	 *   NOTE :: To ensure that we don't end up with a decorator that is not the outermost one, decorators are typically 
	 *   created by using other design patterns such as Factory and Builder, which will encapsulate the creation.
	 * 
	 */
	
	
	/** 
	 * REAL-WORLD DECORATORS :: JAVA I/O
	 * 
	 * The java.io pachage is largely based on the Decorator Design Pattern.
	 * 
	 * Example:
	 * 
	 */
	
	public static void javaIO_Decorator()
	{
		try
		{
			FileInputStream fileStream = new FileInputStream(new File("text.txt")); // component being decorated
			BufferedInputStream bufferedStream = new BufferedInputStream(fileStream); // concrete decorator
			LineNumberInputStream lineStream = new LineNumberInputStream(bufferedStream); // concrete decorator
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
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
	 * - STRIVE FOR LOOSELY COUPLED DESIGNS BETWEEN OBJECTS THAT INTERACT
	 * + CLASSES SHOULD BE OPEN FOR EXTENSION, BUT CLOSED FOR MODIFICATION
	 * 
	 * OO PATTERNS
	 * 
	 * - STRATEGY: DEFINE A FAMILY OF ALGORITHMS, ENCAPSULATE EACH ONE, AND MAKE THEM INTERCHANGEABLE. ALLOW THEM TO VARY
	 * INDEPENDENTLY FROM THE CLIENTS THAT USE THEM
	 * - OBSERVER: DEFINE A ONE-TO-MANY DEPENDENCY BETWEEN OBJECTS SO THAT WHEN ONE OBJECT CHANGES STATE, ALL ITS DEPENDENTS
	 * ARE NOTIFIED AND UPDATED AUTOMATICALLY
	 * + DECORATOR: ATTACH ADDITIONAL RESPONSIBILITIES TO AN OBJECT DYNAMICALLY; A FLEXIBLE ALTENATIVE TO SUBCLASSING FOR
	 * EXTENDING FUNCTIONALITY
	 * 
	 */
}
