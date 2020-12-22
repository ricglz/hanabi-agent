package com.fossgalaxy.games.fireworks.ai.rg20332;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;

/**
 * Class representing an individual of a GA population, containing its
 * chromosome and the fitness of it. It also manages the crossover of the
 * chromosome with another individual, in addition to the management of the
 * mutation of the chromosome
 */
public class Individual {
  public static final Comparator<Individual> sortByFitness = (Individual obj1, Individual obj2) -> Double
      .compare(obj2.getFitness(), obj1.getFitness());

  private Integer[] chromosome;
  private double fitness;
  static Random random = new Random();

  public Individual() {
    this.chromosome = createChromosome();
    this.fitness = 0;
  }

  public Individual(Integer[] chromosome) {
    this.chromosome = chromosome;
    this.fitness = 0;
  }

  private static Integer[] concat(Integer[] a, Integer[] b) {
    List<Integer> result = new ArrayList<>(Arrays.asList(a));
    result.addAll(Arrays.asList(b));
    return result.toArray(new Integer[0]);
  }

  private static Integer[] onePointCrossover(Integer[] a, Integer[] b) {
    int minLength = Math.min(a.length, b.length);
    int crossoverPoint = random.nextInt(minLength - 1);
    Integer[] partOne;
    Integer[] partTwo;
    if (minLength == a.length) {
      partOne = Arrays.copyOfRange(a, 0, crossoverPoint);
      partTwo = Arrays.copyOfRange(b, crossoverPoint, b.length);
    } else {
      partOne = Arrays.copyOfRange(b, 0, crossoverPoint);
      partTwo = Arrays.copyOfRange(a, crossoverPoint, a.length);
    }

    return concat(partOne, partTwo);
  }

  public static Integer[] removeDuplicates(Integer[] a) {
    return new LinkedHashSet<>(Arrays.asList(a)).toArray(new Integer[0]);
  }

  public static Individual crossover(Individual a, Individual b) {
    Integer[] offspringChromosome;
    int crossoverOption = random.nextInt(3);
    if (crossoverOption == 0) {
      offspringChromosome = concat(a.chromosome, b.chromosome);
    } else {
      offspringChromosome = onePointCrossover(a.chromosome, b.chromosome);
    }
    offspringChromosome = removeDuplicates(offspringChromosome);
    Individual offspring = new Individual(offspringChromosome);
    float mutationChance = random.nextFloat();
    if (mutationChance >= 0.33) {
      offspring.mutate();
    }
    return offspring;
  }

  private void changeInt() {
    int index = random.nextInt(this.chromosome.length);
    int amountOfRules = RuleBasedAgentGivenListOfRulesViaChromosome.amountOfRules;
    int newInt = random.nextInt(amountOfRules);
    int originalInt = this.chromosome[index];
    ArrayList<Integer> list = getChromosomeList();
    if (!list.contains(newInt) && newInt != originalInt) {
      this.chromosome[index] = newInt;
    }
  }

  private ArrayList<Integer> getChromosomeList() {
    return new ArrayList<>(Arrays.asList(this.chromosome));
  }

  private void addInt() {
    int amountOfRules = RuleBasedAgentGivenListOfRulesViaChromosome.amountOfRules;
    int newInt = random.nextInt(amountOfRules);
    ArrayList<Integer> list = getChromosomeList();
    if (!list.contains(newInt)) {
      list.add(newInt);
    }
    setChromosomeList(list);
  }

  private void removeInt() {
    int index = random.nextInt(this.chromosome.length);
    ArrayList<Integer> list = getChromosomeList();
    list.remove(index);
    setChromosomeList(list);
  }

  private void shuffleChromosome() {
    ArrayList<Integer> list = getChromosomeList();
    Collections.shuffle(list);
    setChromosomeList(list);
  }

  private void shortChromosomeMutate() {
    float option = random.nextFloat();
    if (option <= 0.6) {
      addInt();
    } else if (option <= 0.7) {
      shuffleChromosome();
    } else {
      changeInt();
    }
  }

  private void fullChromosomeMutate() {
    int option = random.nextInt(2);
    if (option == 0) {
      removeInt();
    } else {
      shuffleChromosome();
    }
  }

  private void normalMutate() {
    float option = random.nextFloat();
    if (option <= 0.3) {
      removeInt();
    } else if (option <= 0.6) {
      addInt();
    } else if (option <= 0.7) {
      shuffleChromosome();
    } else {
      changeInt();
    }
  }

  private void mutate() {
    boolean veryShortChromosome = this.chromosome.length > 3;
    boolean fullChromosome = this.chromosome.length == RuleBasedAgentGivenListOfRulesViaChromosome.amountOfRules;
    if (veryShortChromosome) {
      shortChromosomeMutate();
    } else if (fullChromosome) {
      fullChromosomeMutate();
    } else {
      normalMutate();
    }
  }

  public Integer[] getChromosome() {
    return this.chromosome;
  }

  public String[] getChromosomeCSVReady() {
    String[] entries = new String[this.chromosome.length];
    for (int i = 0; i < this.chromosome.length; i++) {
      entries[i] = this.chromosome[i].toString();
    }
    return entries;
  }

  private void setChromosomeList(ArrayList<Integer> list) {
    setChromosome(list.toArray(new Integer[0]));
  }

  public void setChromosome(Integer[] chromosome) {
    this.chromosome = chromosome;
  }

  public double getFitness() {
    return this.fitness;
  }

  public void setFitness(double fitness) {
    this.fitness = fitness;
  }

  private int randomDiscardRuleNumber() {
    return random.nextInt(RuleBasedAgentGivenListOfRulesViaChromosome.amountOfDiscardRules);
  }

  private int randomTellRuleNumber() {
    int lowBound = RuleBasedAgentGivenListOfRulesViaChromosome.tellRulesLowBound;
    int amount = RuleBasedAgentGivenListOfRulesViaChromosome.amountOfTellRules;
    return random.nextInt(amount) + lowBound;
  }

  private int randomPlayRuleNumber() {
    int lowBound = RuleBasedAgentGivenListOfRulesViaChromosome.playRulesLowBound;
    int amount = RuleBasedAgentGivenListOfRulesViaChromosome.amountOfPlayRules;
    return random.nextInt(amount) + lowBound;
  }

  private Integer[] createChromosome() {
    int firstDisc = randomDiscardRuleNumber();
    int secondDisc = randomDiscardRuleNumber();
    while (secondDisc != firstDisc) {
      secondDisc = randomDiscardRuleNumber();
    }
    int firstTell = randomTellRuleNumber();
    int secondTell = randomTellRuleNumber();
    while (secondTell != firstTell) {
      secondTell = randomTellRuleNumber();
    }
    int firstPlay = randomPlayRuleNumber();
    int secondPlay = randomPlayRuleNumber();
    while (secondPlay != firstPlay) {
      secondPlay = randomPlayRuleNumber();
    }
    return new Integer[] { firstDisc, secondDisc, firstTell, secondTell, firstPlay, secondPlay };
  }
}
