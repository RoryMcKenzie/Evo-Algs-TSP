import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

class EATest {


    @org.junit.jupiter.api.Test
    void mutate2Opt() {
        Problem problem = new Problem("berlin52" + ".tsp");

        Individual parent1 = new Individual(problem);
        Individual parent2 = new Individual(problem);

        ArrayList<Individual> temp = new ArrayList<>();
        temp.add(parent1);
        temp.add(parent2);

        EA x = new EA("PMX", "Swap", "berlin52", 3);

        x.problem = new Problem("berlin52.tsp");

        ArrayList<Individual> after = x.mutate2Opt(temp);

        assert (after instanceof ArrayList<Individual>);
        assert (after.size() == temp.size());
    }

    @org.junit.jupiter.api.Test
    void mutateSwap() {
        Problem problem = new Problem("berlin52" + ".tsp");

        Individual parent1 = new Individual(problem);
        Individual parent2 = new Individual(problem);

        ArrayList<Individual> temp = new ArrayList<>();
        temp.add(parent1);
        temp.add(parent2);

        EA x = new EA("PMX", "Swap", "1", 3);

       ArrayList<Individual> after = x.mutateSwap(temp);

        assert (after instanceof ArrayList<Individual>);
        assert (after.size() == temp.size());
    }

    @org.junit.jupiter.api.Test
    void mutateScramble() {
        Problem problem = new Problem("berlin52" + ".tsp");

        Individual parent1 = new Individual(problem);
        Individual parent2 = new Individual(problem);

        ArrayList<Individual> temp = new ArrayList<>();
        temp.add(parent1);
        temp.add(parent2);

        EA x = new EA("PMX", "Swap", "1", 3);

        ArrayList<Individual> after = x.mutateScramble(temp);

        assert (after instanceof ArrayList<Individual>);
        assert (after.size() == temp.size());
    }

    @org.junit.jupiter.api.Test
    void mutateInsert() {
        Problem problem = new Problem("berlin52" + ".tsp");

        Individual parent1 = new Individual(problem);
        Individual parent2 = new Individual(problem);

        ArrayList<Individual> temp = new ArrayList<>();
        temp.add(parent1);
        temp.add(parent2);

        EA x = new EA("PMX", "Swap", "1", 3);

        ArrayList<Individual> after = x.mutateInsert(temp);

        assert (after instanceof ArrayList<Individual>);
        assert (after.size() == temp.size());
    }

    @org.junit.jupiter.api.Test
    void orderCrossover() {
        Problem problem = new Problem("berlin52" + ".tsp");

        Individual parent1 = new Individual(problem);
        Individual parent2 = new Individual(problem);

        EA x = new EA("PMX", "Swap", "1", 3);

        ArrayList<Individual> after = x.orderCrossover(parent1,parent2);

        assert (after instanceof ArrayList<Individual>);
        assert (after.size() == 2);
    }

    @org.junit.jupiter.api.Test
    void pmxCrossover() {
        Problem problem = new Problem("berlin52" + ".tsp");

        Individual parent1 = new Individual(problem);
        Individual parent2 = new Individual(problem);

        EA x = new EA("PMX", "Swap", "1", 3);

        ArrayList<Individual> after = x.pmxCrossover(parent1,parent2);

        assert (after instanceof ArrayList<Individual>);
        assert (after.size() == 2);
    }

    @org.junit.jupiter.api.Test
    void cycleCrossover() {
        Problem problem = new Problem("berlin52" + ".tsp");

        Individual parent1 = new Individual(problem);
        Individual parent2 = new Individual(problem);

        EA x = new EA("PMX", "Swap", "1", 3);

        ArrayList<Individual> after = x.cycleCrossover(parent1,parent2);

        assert (after instanceof ArrayList<Individual>);
        assert (after.size() == 2);
    }
}