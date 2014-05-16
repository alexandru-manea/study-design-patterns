package singleton;

/**
 * LAZY-LOADED THREAD-SAFE SINGLETON 
 * 
 * @author Alexandru Manea
 *
 */
public class InitializationOnDemandHolder {

	/**
	 * **************************************************************************************************************
	 * INITIALIZATION-ON-DEMAND HOLDER :: DESIGN PATTERN WHICH ENABLES A SAFE, HIGHLY CONCURRENT, LAZY INITIALIZATION 
	 * WITH GOOD PERFORMANCE.
	 * **************************************************************************************************************
	 * 
	 * -> The implementation relies on the well-specified initialization phase of execution within the JVM in that
	 * static members are loaded together with the class in a thread-safe manner
	 * -> This yields a highly-efficient thread-safe singleton cache, without synchronization overhead
	 * 
	 */
	
	/*
	 * Initialization-on-demand holder implementation.
	 * 
	 * -> The static class definition <LazyHolder> is NOT initialized until the JVM determines it must be executed
	 * -> This only happens when the static method <getInstance> is invoked
	 * -> This initialization results in static variable INSTANCE being initialized
	 * -> Since the class initialization phase is guaranteed b the JLS to be non-concurrent, no further synchronization
	 *    is required 
	 * 
	 */
	static class Something {
		
		private Something() {}
		
		private static class LazyHolder {
			
			private static final Something INSTANCE = new Something();
		}
		
		public static Something getInstance() {
			
			return LazyHolder.INSTANCE;
		}
	}
	
}
