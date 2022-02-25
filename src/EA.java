import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class EA extends Observable implements Runnable {

	public EA(String crossover, String mutation, String instance, Integer number){
		this.crossover = crossover;
		this.mutation = mutation;
		this.filename = instance;
		this.number = number;
	}

	private static final Object lock = new Object();
	String filename;
	String mutation;
	String crossover;
	int number;
	Problem problem;
	static Random random = new Random();
	ArrayList<Individual> population;
	Individual best;
	int popSize = 500;
	int tournamentSize = 5;
	int maxGenerations = 20000;
	int generation;
	int pause = 0;// set to zero for max speed
	double mutationRate = 0.5;
	double crossoverRate = 0.85;
	//private ArrayList<EA> islands;

	@Override
	public void run() {
		problem = new Problem(filename + ".tsp");
		population = new ArrayList<>();

		// initialise population. The Individual constructor generates a random
		// permutation of customers (Locations)
		for (int i = 0; i < popSize; i++) {
			Individual individual = new Individual(problem);
			population.add(individual);
			// System.out.println(individual);
		}
		best = getBest();
		generation = 0;

		while (generation < maxGenerations) {
			generation++;
			
			ArrayList<Individual> pop2 = new ArrayList<>();

			//seems to all be island-related, can be commented out
			/*if(generation % 100 == 0) {
				System.out.println("swap");
				synchronized (lock) {
					//same island or in loop different .... original was same island and popsize / 10
					int idx = random.nextInt(islands.size());
					for(int i = 0; i < popSize / 100; i++) {
//						int idx = random.nextInt(islands.size());
						pop2.add(islands.get(idx).population.get(random.nextInt(popSize)).copy());
					}
					//add best 
//					pop2.add(islands.get(idx).best.copy());
					
					//add 1 from each?
//					for(EA ea : islands) {
//						pop2.add(ea.population.get(random.nextInt(popSize)));
//					}
				}				
			} */
			// elitism
			
			pop2.add(best.copy());
			while (pop2.size() < popSize) {
				Individual parent1 = select();
				Individual parent2 = select();
				ArrayList<Individual> children;

				if(random.nextDouble() < crossoverRate) {
					children = switch (crossover) {
						case "PMX" -> pmxCrossover(parent1, parent2);
						case "Order" -> orderCrossover(parent1, parent2);
						case "Cycle" -> cycleCrossover(parent1, parent2);
						default -> pmxCrossover(parent1, parent2);
					};
				} else {
					ArrayList<Individual> temp = new ArrayList<>();
					temp.add(parent1);
					temp.add(parent2);
					children = temp;
				}

				if (random.nextDouble() < mutationRate) {
					children = switch (mutation) {
						case "Swap" -> mutateSwap(children);
						case "Scramble" -> mutateScramble(children);
						case "Invert" -> mutateInvert(children);
						case "2-opt" -> mutate2Opt(children);
						default -> mutateSwap(children);
					};
				}

				for (Individual child : children) {
					child.evaluate();
					pop2.add(child);
					// replace(child);
				}

				// pause so can see the effect
//				try {
//					Thread.sleep(pause);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
			synchronized (lock) {
				population = pop2;
			}
			Individual bestCandidate = getBest();
			if (bestCandidate.fitness < best.fitness) {
				best = bestCandidate;
			}
			if(generation % 100 == 0){
				System.out.println(generation);
			}
			//printStats(generation);
			setChanged();
			notifyObservers(bestCandidate);
			writeStats();
		}
		/*Individual theIslandBest = null;
		 for(EA ea : islands) {
			if(theIslandBest == null || ea.best.fitness < theIslandBest.fitness) {
				theIslandBest = ea.best;
			}
		} */
		setChanged();
		notifyObservers(best);
		printStats(generation);
		//writeStats();
		Thread.currentThread().interrupt();
		//System.out.println(theIslandBest.fitness);
	}

	private void printStats(int generation) {
		System.out.println(generation + "\t" + best.fitness);
	}

	private void writeStats(){
		String writename = crossover + "_" + mutation + "_" + filename +  "_" + number  + ".csv";
		try{
			FileWriter myWriter = new FileWriter("results/" + writename, true);
			myWriter.write(best.fitness + "\n");
			myWriter.close();
		} catch (IOException e){
			System.out.println("error");
			e.printStackTrace();
		}
	}
	private Individual getBest() {
		Individual bestInPop = null;
		for (Individual individual : population) {
			if (bestInPop == null || individual.fitness < bestInPop.fitness) {
				bestInPop = individual;
			}
		}
		return bestInPop.copy();
	}

	private Individual getWorst() {
		Individual worstInPop = null;
		for (Individual individual : population) {
			if (worstInPop == null || individual.fitness > worstInPop.fitness) {
				worstInPop = individual;
			}
		}
		return worstInPop;
	}

	private void replace(Individual child) {
		Individual worst = getWorst();
		if (child.fitness < worst.fitness) {
			population.set(population.indexOf(worst), child);
		}
	}

	//MUTATION

	//2-opt mutation
	private ArrayList<Individual> mutate2Opt(ArrayList<Individual> children) {
		ArrayList<Individual> result = new ArrayList<>();
		for (Individual child : children) {
			int cut1 = random.nextInt(child.chromosome.size() - 1);
			int cut2 = cut1 + random.nextInt(child.chromosome.size() - cut1);
			Individual individual = new Individual(problem);
			int i;
			for (i = 0; i < cut1; i++) {
				individual.chromosome.set(i, child.chromosome.get(i));
			}
			for (int k = cut2; k >= cut1; k--) {
				individual.chromosome.set(i, child.chromosome.get(k));
				i++;
			}
			for (i = cut2 + 1; i < individual.chromosome.size(); i++) {
				individual.chromosome.set(i, child.chromosome.get(i));
			}
			result.add(individual);
		}
		return result;
	}

	// swap mutation
	private ArrayList<Individual> mutateSwap(ArrayList<Individual> children) {
		for (Individual child : children) {
			Location temp;
			int idx1 = random.nextInt(child.chromosome.size());
			int idx2 = random.nextInt(child.chromosome.size());

			temp = child.chromosome.get(idx1);
			child.chromosome.set(idx1, child.chromosome.get(idx2));
			child.chromosome.set(idx2, temp);
		}
		return children;
	}

	private ArrayList<Individual> mutateScramble(ArrayList<Individual> children){
		for (Individual child : children){
			int scrambleCut1 = random.nextInt(child.chromosome.size());
			int scrambleCut2 = random.nextInt(child.chromosome.size());

			//swap cut points if the second > the first
			if (scrambleCut1 > scrambleCut2) {
				int temp = scrambleCut1;
				scrambleCut1 = scrambleCut2;
				scrambleCut2 = temp;
			}

			List<Location> x = child.chromosome.subList(scrambleCut1, scrambleCut2);

			Collections.shuffle(x);

			/*int j = 0;
			for (int i = scrambleCut1; i < scrambleCut2; i++){
				child.chromosome.set(i, x.get(j));
				j++;
			} */
		}
		return children;
	}

	private ArrayList<Individual> mutateInvert(ArrayList<Individual> children){
		for (Individual child : children){
			int invertCut1 = random.nextInt(child.chromosome.size());
			int invertCut2 = random.nextInt(child.chromosome.size());

			//swap cut points if the second > the first
			if (invertCut1 > invertCut2) {
				int temp = invertCut1;
				invertCut1 = invertCut2;
				invertCut2 = temp;
			}

			List<Location> x = child.chromosome.subList(invertCut1, invertCut2);

			Collections.reverse(x);

			/*int j = 0;
			for (int i = scrambleCut1; i < scrambleCut2; i++){
				child.chromosome.set(i, x.get(j));
				j++;
			} */
		}
		return children;
	}

	//CROSSOVER

	private ArrayList<Individual> orderCrossover(Individual parent1, Individual parent2) {
		int size = parent1.chromosome.size();

		//generate cut points
		int oxPoint1 = random.nextInt(parent1.chromosome.size());
		int oxPoint2 = random.nextInt(parent1.chromosome.size());

		//swap cut points if the second > the first
		if (oxPoint1 > oxPoint2) {
			int temp = oxPoint1;
			oxPoint1 = oxPoint2;
			oxPoint2 = temp;
		}

		//Initialise 2 new children
		Individual oxchild1 = new Individual();
		oxchild1.depot = parent1.depot;
		oxchild1.chromosome = new ArrayList<>();
		Individual oxchild2 = new Individual();
		oxchild2.depot = parent1.depot;
		oxchild2.chromosome = new ArrayList<>();

		//add elements from parents between cut points
		oxchild1.chromosome.addAll(parent1.chromosome.subList(oxPoint1,oxPoint2));
		oxchild2.chromosome.addAll(parent2.chromosome.subList(oxPoint1,oxPoint2));

		//declare variables for loop
		int currentIndex = 0;

		for (int i = 0; i <= size; i++){
			currentIndex = (oxPoint1 + i) % size;

			//if the current location in the parent isn't present in the child, it is added
			if(!oxchild1.contains(parent2.chromosome.get(currentIndex))){
				oxchild1.chromosome.add(parent2.chromosome.get(currentIndex));
			}

			if(!oxchild2.contains(parent1.chromosome.get(currentIndex))){
				oxchild2.chromosome.add(parent1.chromosome.get(currentIndex));
			}
		}

		//rotate both children so they start at the correct place
		Collections.rotate(oxchild1.chromosome,oxPoint1);
		Collections.rotate(oxchild2.chromosome,oxPoint1);

		//create children object to be returned by the method
		ArrayList<Individual> children = new ArrayList<>();
		children.add(oxchild1);
		children.add(oxchild2);
		return children;
	}

	private ArrayList<Individual> pmxCrossover(Individual parent1, Individual parent2) {
		//generate start and end numbers to cut
		int xPoint1 = random.nextInt(parent1.chromosome.size());
		int xPoint2 = random.nextInt(parent1.chromosome.size());
		//swap cut points if the second > the first
		if (xPoint1 > xPoint2) {
			int temp = xPoint1;
			xPoint1 = xPoint2;
			xPoint2 = temp;
		}

		//initialise 2 new children
		Individual child1 = new Individual();
		child1.depot = parent1.depot;
		child1.chromosome = new ArrayList<>();
		Individual child2 = new Individual();
		child2.depot = parent1.depot;
		child2.chromosome = new ArrayList<>();
		//make all values null
		for (int i = 0; i < parent1.chromosome.size(); i++) {
			child1.chromosome.add(null);
			child2.chromosome.add(null);
		}

		// crossover between cut points
		for (int i = xPoint1; i <= xPoint2; i++) {
			child1.chromosome.set(i, parent2.chromosome.get(i));
			child2.chromosome.set(i, parent1.chromosome.get(i));
		}

		// fill up to xpoint1 from original parent if possible
		for (int i = 0; i < xPoint1; i++) {
			if (!child1.contains(parent1.chromosome.get(i))) {
				child1.chromosome.set(i, parent1.chromosome.get(i));
			}
			if (!child2.contains(parent2.chromosome.get(i))) {
				child2.chromosome.set(i, parent2.chromosome.get(i));
			}
		}

		// fill from after xpoint2 from original parent if possible
		for (int i = xPoint2 + 1; i < parent1.chromosome.size(); i++) {
			if (!child1.contains(parent1.chromosome.get(i))) {
				child1.chromosome.set(i, parent1.chromosome.get(i));
			}
			if (!child2.contains(parent2.chromosome.get(i))) {
				child2.chromosome.set(i, parent2.chromosome.get(i));
			}
		}

		// fill in remainder of child1 based on map;
		for (int i = 0; i < parent1.chromosome.size(); i++) {
			if (child1.chromosome.get(i) == null) {
				Boolean filled = false;
				// this value is already in the child as it wasn't set in previous steps
				Location locFromParent = parent1.chromosome.get(i);
				while (!filled) {
					// look up the corresponding mapped value (the value at the same position in the
					// other child where valFromParent exists)
					Location locFromOtherChild = getMappedVal(child1, child2, locFromParent);
					if (!child1.contains(locFromOtherChild)) {
						child1.chromosome.set(i, locFromOtherChild);
						filled = true;
					} else {
						// continue while loop until we find a value not in the child
						locFromParent = locFromOtherChild;
					}
				}
			}
		}

		// do the same for child2
		for (int i = 0; i < parent1.chromosome.size(); i++) {
			if (child2.chromosome.get(i) == null) {
				Boolean filled = false;
				// this value is already in the child as it wasn't set in previous steps
				Location locFromParent = parent2.chromosome.get(i);
				while (!filled) {
					// look up the corresponding mapped value (the value at the same position in the
					// other child where valFromParent exists)
					Location locFromOtherChild = getMappedVal(child2, child1, locFromParent);
					if (!child2.contains(locFromOtherChild)) {
						child2.chromosome.set(i, locFromOtherChild);
						filled = true;
					} else {
						// continue while loop until we find a value not in the child
						locFromParent = locFromOtherChild;
					}
				}
			}
		}
		for (Location l : child1.chromosome) {
			if (l == null) {
				System.err.println();
			}
		}
		for (Location l : child2.chromosome) {
			if (l == null) {
				System.err.println();
			}
		}
		ArrayList<Individual> children = new ArrayList<>();
		children.add(child1);
		children.add(child2);
		return children;
	}

	private ArrayList<Individual> cycleCrossover(Individual parent1, Individual parent2){
		int length = parent1.chromosome.size();

		//Initialise 2 children, set chromosome to parents' to make later easier
		Individual cxchild1 = new Individual();
		cxchild1.depot = parent1.depot;
		cxchild1.chromosome = parent1.chromosome;
		Individual cxchild2 = new Individual();
		cxchild2.depot = parent1.depot;
		cxchild2.chromosome = parent2.chromosome;

		// the set of all visited indices so far
		final Set<Integer> visitedIndices = new HashSet(length);
		// the indices of the current cycle
		final List<Integer> indices = new ArrayList(length);

		int currentIndex = 0;
		int cycle = 1;

		while (visitedIndices.size() < length){
			indices.add(currentIndex);

			Location item = parent2.chromosome.get(currentIndex);
			currentIndex = parent1.chromosome.indexOf(item);

			//until it reaches the start again
			while(currentIndex != indices.get(0)){
				indices.add(currentIndex);
				item = parent2.chromosome.get(currentIndex);
				currentIndex = parent1.chromosome.indexOf(item);
			}

			// for even cycles: swap the child elements on the indices found in this cycle
			if (cycle++ % 2 != 0){
				for (int i : indices){
					Location temp = cxchild1.chromosome.get(i);
					cxchild1.chromosome.set(i, cxchild2.chromosome.get(i));
					cxchild2.chromosome.set(i, temp);
				}
			}

			visitedIndices.addAll(indices);
			//find next starting index: last one + 1 until unvisited one
			currentIndex = (indices.get(0) + 1) % length;
			while(visitedIndices.contains(currentIndex) && visitedIndices.size() < length) {
				currentIndex++;
				if (currentIndex >= length){
					currentIndex = 0;
				}
			}
			indices.clear();
		}
		//create children object to be returned by the method
		ArrayList<Individual> children = new ArrayList<>();
		children.add(cxchild1);
		children.add(cxchild2);
		return children;
	}

	// simple crossover. Probably not very good. Long-winded with customer indices
	//appears to just be one point crossover? won't use this but might keep
	private ArrayList<Individual> crossover(Individual parent1, Individual parent2) {
		Individual child = new Individual();
		child.depot = parent1.depot.copy();
		child.chromosome = new ArrayList<>();
		int cutPoint = random.nextInt(parent1.chromosome.size());

		// add from parent1 up to cutpoint
		for (int i = 0; i < cutPoint; i++) {
			child.chromosome.add(parent1.chromosome.get(i).copy());
		}

		// 1 is depot. Add all location indices
		ArrayList<Integer> locationIndexesNotUsed = new ArrayList<>();
		for (int i = 2; i < problem.customers.size() + 2; i++) {
			locationIndexesNotUsed.add(i);
		}

		// remove indices copied from parent1
		for (int i = 0; i < child.chromosome.size(); i++) {
			int idx = child.chromosome.get(i).idx;
			int usedIdx = locationIndexesNotUsed.indexOf(idx);
			locationIndexesNotUsed.remove(usedIdx);
		}

		// add locations not used from cutpoint to end of parent2
		for (int i = cutPoint; i < parent2.chromosome.size(); i++) {
			Location loc = parent2.chromosome.get(i);
			if (locationIndexesNotUsed.contains(loc.idx)) {
				child.chromosome.add(loc.copy());
				int usedIdx = locationIndexesNotUsed.indexOf(loc.idx);
				locationIndexesNotUsed.remove(usedIdx);
			}
		}

		// add remaining locations not in child
		for (int i : locationIndexesNotUsed) {
			// Problem has locations in order starting with location 2
			Location loc = problem.customers.get(i - 2).copy();
			child.chromosome.add(loc);
		}

		// check 1
		if (child.chromosome.size() != parent1.chromosome.size()) {
			System.err.println("Error in crossover wrong size " + child.chromosome.size());
			System.exit(-1);
		}

		// check 2. All indices from 2 .. end should be included
		ArrayList<Integer> indexCheck = new ArrayList<>();
		for (int i = 2; i < problem.customers.size() + 2; i++) {
			indexCheck.add(i);
		}
		for (Location loc : child.chromosome) {
			int usedIdx = indexCheck.indexOf(loc.idx);
			indexCheck.remove(usedIdx);
		}
		if (indexCheck.size() != 0) {
			System.err.println("Unused indices " + child);
			System.exit(-1);
		}
		ArrayList<Individual> children = new ArrayList<>();
		children.add(child);
		return children;
	}

	/**
	 * Return the customer in the same position in child2 as the customer defined by
	 * locFromParent is in child1
	 *
	 * @param child1
	 * @param child2
	 * @param locFromParent
	 * @return
	 */
	private Location getMappedVal(Individual child1, Individual child2, Location locFromParent) {
		for (int i = 0; i < child1.chromosome.size(); i++) {
			if (child1.chromosome.get(i) != null) {
				if (child1.chromosome.get(i).idx == locFromParent.idx) {
					return child2.chromosome.get(i);
				}
			}
		}
		return null;
	}
	//SELECTION

	//Add Roulette Selection, preliminary testing to figure out which is better

	//Tournament Selection
	private Individual select() {
		Individual winner = population.get(random.nextInt(popSize));
		for (int i = 1; i < tournamentSize; i++) {
			Individual candidate2 = population.get(random.nextInt(popSize));
			if (candidate2.fitness < winner.fitness) {
				winner = candidate2;
			}
		}
		return winner.copy();
	}

	public static void main(String[] args) {
		/*

		//ArrayList<EA> islands = new ArrayList<>();
		for(int i = 0; i < 20; i++) {
			Gui gui = new Gui(i);
			EA ea = new EA();
			//islands.add(ea);
			ea.addObserver(gui);
			Thread t = new Thread(ea);
			t.start();			
		}
		/*for(EA ea : islands) {
			ea.islands = islands;
		}			*/

	}

}
