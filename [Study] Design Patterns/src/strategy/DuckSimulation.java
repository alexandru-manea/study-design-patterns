package strategy;

/**
 * Duck Pond Simulation used to illustrate both the Strategy Design Pattern and the correct use of OO principles.
 * 
 */

public class DuckSimulation
{
	/*
	 * INITIAL DESIGN
	 * 
	 * Scenario: a (large - only two shown here) variety of duck species quacking and swimming. Each one looks different.
	 */
	
	abstract class Duck_Initial
	{
		public void quack() {}
		public void swim() {}
		public abstract void display();
	}
	
	class MallardDuck_Initial extends Duck_Initial
	{
		public void display() {}
	}
	
	class RedheadDuck_Initial extends Duck_Initial
	{
		public void display() {}
	}
	
	
	/*
	 * <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	 * << SPECIFICATION CHANGE :: We need to allow some ducks to fly. Also, ducks can quack differently and some can be silent. >>
	 * << For example, rubber ducks "squeak" and can't fly and decoy ducks neither make sounds or fly.                          >>
	 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	*/


	/*
	 * SOLUTION 1
	 * 
	 * Add a fly() method in the Duck superclass. Override quack() and fly() in subclasses to cope for different behaviours or leave
	 * the method bodies empty when behaviour should not be present.
	 * 
	 */
	
	abstract class Duck_Solution1
	{
		public void quack() {} // the default implementation can now be overridden in subclasses
		public void swim() {}
		public abstract void display();
		// add fly() method s.t. all subclasses implement it
		public void fly() {}
	}
	
	class MallardDuck_Solution1 extends Duck_Solution1
	{
		public void display() {}
	}
	
	class RedheadDuck_Solution1 extends Duck_Solution1
	{
		public void display() {}
	}
	
	class RubberDuck_Solution1 extends Duck_Solution1
	{
		public void display() {}
		public void quack() {} // overridden to squeak
		public void fly() {} // do nothing
	}
	
	class DecoyDuck_Solution1 extends Duck_Solution1
	{
		public void display() {}
		public void quack() {} // do nothing
		public void fly() {} // do nothing
	}
	
	
	/*
	 * <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	 * << PROBLEM :: If the specification keeps changing, we're forced to keep overriding fly() and quack() >> 
	 * <<            for every new Duck subclass that will ever be added to the program.                    >> 
	 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	 */
	
	
	/**
	 * OOP OBSERVATION :: EVEN THOUGH INHERITANCE HAS GREAT BENEFITS IN TERMS OF CODE REUSE,
	 * IT CAN SOMETIMES BE COUNTER-PRODUCTIVE IN TERMS OF CODE MAINTENANCE.
	 */
	
	
	/*
	 * SOLUTION 2
	 * 
	 * Use two interfaces, Flyable and Quackable, to allow only some of the ducks to implement one or both
	 * of these behaviours.
	 * 
	 */
	
	abstract class Duck_Solution2
	{
		public void swim() {}
		public abstract void display();
	}
	
	interface Flyable // new addition
	{
		public void fly();
	}
	
	interface Quackable // new addition
	{
		public void quack();
	}
	
	class MallardDuck_Solution2 extends Duck_Solution2 implements Flyable, Quackable
	{
		public void display() {}
		public void quack() {} // needs implementation here
		public void fly() {} // needs implementation here
	}
	
	class RedheadDuck_Solution2 extends Duck_Solution2 implements Flyable, Quackable
	{
		public void display() {}
		public void quack() {} // needs implementation here
		public void fly() {} // needs implementation here
	}
	
	class RubberDuck_Solution2 extends Duck_Solution2 implements Quackable
	{
		public void display() {}
		public void quack() {} // needs implementation here
	}
	
	class DecoyDuck_Solution2 extends Duck_Solution2
	{
		public void display() {}
	}
	
	
	/*
	 * <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	 * << PROBLEM :: Code Duplication. A little change in the default flying behaviour will need to be manually >>
	 * <<            implemented in all subclasses => maintenance nightmare because of code reuse.              >> 
	 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	 */
	
	
	/**
	 * OOP OBSERVATION :: THE ONLY CONSTANT IN SOFTWARE DEVELOPEMENT IS CHANGE.
	 */
	
	
	/**
	 * -----------------------------------------------------------------------------------------------------------------
	 * DESIGN PRINCIPLE :: IDENTIFY THE ASPECTS OF THE APPLICATION THAT VARY AND SEPARATE THEM FROM WHAT STAYS THE SAME!
	 * -----------------------------------------------------------------------------------------------------------------
	 */
	
	
	/*
	 * SOLUTION 3 (FINAL)
	 * 
	 * quack() and fly() are the parts of the Duck class that vary across ducks. So we pull both methods out and create 
	 * a new set of classes to represent each behaviour. To keep things flexible, we will <assign> behaviours to each
	 * instance of Duck (e.g., instantiate a new MallardDuck with a <specific> type of flying behaviour). Moreover,
	 * allowing for the possibility of changing the behaviour dynamically would be a plus.
	 *
	 */
	
	
	/**
	 * -------------------------------------------------------------------
	 * DESIGN PRINCIPLE :: PROGRAM TO AN INTERFACE, NOT AN IMPLEMENTATION!
	 * -------------------------------------------------------------------
	 * 
	 * Interface does not necessarily refer to a Java interface, but more to a <supertype>. 
	 * 
	 * WHAT IT MEANS: the declared type of the variables in a program should be a supertype, usually an abstract class or 
	 * an interface, so that the objects assigned to those variables can be of any concrete implementation of the supertype, 
	 * which means that the class declaring them doesn't have to know about the actual object types.
	 * 
	 */
	
	
	/*
	 * We will use an interface to represent each behaviour (FlyBehaviour and QuackBehaviour). But it won't be the Duck classes
	 * that implement these interfaces, but a new set of classes whose entire reason is to represent a behaviour. Contrast this
	 * with the earlier solution in which the behaviour came from a a concrete implementation (either in the superclass or in the
	 * subclass) => programming to an implementation. With this design, other types of objects can reuse the fly and quack behaviours 
	 * because they are no longer hidden in the duck classes. Even more, we can add new behaviours without modifying any of the existing
	 * ones or touching the duck classes.
	 * 
	 * For the Duck class to delegate its flying and quacking behaviour, two instance variables of the interface type will be added.
	 * Each duck object will set these variables polymorphically to reference the specific behaviour it has at runtime.
	 * 
	 * Next, we replace fly() and quack() with performFly() and performQuack(). This way, a duck just allows the object that is
	 * referenced by quackBehaviour to quack for it, and the same for flying.
	 * 
	 * We also add two methods for setting the fly and quack behaviours at runtime. This is possible because, having programmed to
	 * an interface, we have the flexibility of changing the QuackBehaviour object to another subclass. Contrast this with using
	 * directly the subclass type when declaring it. 
	 */
	
	
	abstract class Duck_Final
	{
		QuackBehaviour quackBehaviour; // can change these
		FlyBehaviour flyBehaviour;     // polymorphically
		
		public void performQuack()
		{
			this.quackBehaviour.quack(); // delegate to the behaviour object to do the action
		}
		
		public void performFly()
		{
			this.flyBehaviour.fly(); // delegate to the behaviour object to do the action
		}
		
		// set the fly behaviour dynamically
		public void setFlyBehaviour(FlyBehaviour flyBehaviour)
		{
			this.flyBehaviour = flyBehaviour;
		}
		
		// set the quack behaviour dynamically
		public void setQuackBehaviour(QuackBehaviour quackBehaviour)
		{
			this.quackBehaviour = quackBehaviour;
		}
		
		public abstract void display();
		public void swim() {}
	}
	
	
	// the interface to represent flying behaviours
	interface FlyBehaviour
	{
		public void fly();
	}		
	
	// concrete flying behaviours
	class FlyWithWings implements FlyBehaviour
	{
		public void fly() {}
	}
	
	class FlyNoWay implements FlyBehaviour
	{
		public void fly() {}
	}
	
	// the interface to represent quacking behaviours
	interface QuackBehaviour
	{
		public void quack();
	}
	
	// concrete quacking behaviours
	class Quack implements QuackBehaviour
	{
		public void quack() {}
	}
	
	class Squeak implements QuackBehaviour
	{
		public void quack() {}
	}
	
	class MuteQuack implements QuackBehaviour
	{
		public void quack() {}
	}
	
	
	// example implementation of a constructor
	class MallardDuck_Final extends Duck_Final
	{
		public MallardDuck_Final()
		{
			quackBehaviour = new Quack(); // instantiate to concrete implementation
			flyBehaviour = new FlyWithWings(); // instantiate to concrete implementation
		}
		
		public void display() {}
	}
	
	class RedheadDuck_Final extends Duck_Final
	{
		public void display() {}
	}
	
	class RubberDuck_Final extends Duck_Final
	{
		public void display() {}
	}
	
	class DecoyDuck_Final extends Duck_Final
	{
		public void display() {}
	}
	
	
	/**
	 * -------------------------------------------------------
	 * DESIGN PRINCIPLE :: FAVOR COMPOSITION OVER INHERITANCE!
	 * -------------------------------------------------------
	 * 
	 * Creating systems using composition gives you a lot more flexibility. It allows you to encapsulate a family of algorithms
	 * into their own set of classes, but it also lets you change behaviour at runtime.
	 */
	
	
	/**
	 * *********************************************************************************************************************
	 * DESIGN PATTERN :: STRATEGY
	 * 
	 * DEFINE A FAMILY OF ALGORITHMS, ENCAPSULATE EACH ONE, AND MAKE THEM INTERCHANGEABLE. ALGORITHMS CAN VARY INDEPENDENTLY
	 * FROM THE CLIENTS THAT USE THEM.
	 * *********************************************************************************************************************
	 */
	
	
	/**
	 * $$$ CONCLUSIONS $$$
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
	 * 
	 * OO PATTERNS
	 * 
	 * - STRATEGY: DEFINE A FAMILY OF ALGORITHMS, ENCAPSULATE EACH ONE, AND MAKE THEM INTERCHANGEABLE. ALLOW THEM TO VARY
	 * INDEPENDENTLY FROM THE CLIENTS THAT USE THEM
	 * 
	 */
}
