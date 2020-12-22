package com.fossgalaxy.games.fireworks.ai.rg20332;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import com.fossgalaxy.stats.BasicStats;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

/**
 * This class evaluates a ruleset defined by a list of integers.
 */
public class RuleBasedSystemChromosomeEvaluator {
  static Random random = new Random();
  static int generations = 40;
  static int populationSize = 60;
  static int numPlayers = 4;
  static int numGames = 100;
  static int loopAlgoTimes = 60;
  static ArrayList<Individual> currentBestIndividuals;

  private static ArrayList<Individual> createPopulation(int populationSize) {
    System.err.println("Creating random population");
    ArrayList<Individual> population = new ArrayList<>();
    for (int i = 0; i < populationSize; i++) {
      population.add(new Individual());
    }
    return population;
  }

  private static ArrayList<Individual> sample(int n, ArrayList<Individual> array) {
    ArrayList<Individual> copy = (ArrayList<Individual>) array.clone();
    Collections.shuffle(copy);
    ArrayList<Individual> sampleList = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      sampleList.add(copy.get(i));
    }
    return sampleList;
  }

  private static Individual tournamentSelection(ArrayList<Individual> population) {
    ArrayList<Individual> populationSample = sample(5, population);
    populationSample.sort(Individual.sortByFitness);
    return populationSample.get(0);
  }

  private static ArrayList<Individual> createNewGeneration(ArrayList<Individual> population) {
    ArrayList<Individual> newGeneration = new ArrayList<>();
    int offspringsPerBest = populationSize / 4;
    for (int j = 0; j < 4; j++) {
      Individual individual = population.get(j);
      ArrayList<Individual> withoutIndividual = (ArrayList<Individual>) population.clone();
      withoutIndividual.remove(j);
      for (int k = 0; k < offspringsPerBest; k++) {
        Individual selectedMate = tournamentSelection(withoutIndividual);
        Individual offspring = Individual.crossover(individual, selectedMate);
        newGeneration.add(offspring);
      }
    }
    return newGeneration;
  }

  private static void runGeneration(ArrayList<Individual> population) {
    IndividualThread[] threads = new IndividualThread[population.size()];
    for (int i = 0; i < threads.length; i++) {
      IndividualThread thread = new IndividualThread(population.get(i));
      thread.start();
      threads[i] = thread;
    }
    try {
      for (Thread thread : threads) {
        thread.join();
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static double printIndividualStats(ArrayList<Individual> individuals) {
    BasicStats stats = new BasicStats();
    for (Individual individual : individuals) {
      stats.add(individual.getFitness());
    }
    double mean = stats.getMean();
    String formattedStats = String.format("Mean: %f - Std Dev: %f - Max: %f - Min: %f - N: %d", mean, stats.getStdDev(),
        stats.getMax(), stats.getMin(), stats.getN());
    System.err.println(formattedStats);

    return mean;
  }

  private static String getResourcesFilename(String filename) {
    return String.format("src/main/resources/%s", filename);
  }

  private static String getBestChromosomeFilename() {
    return getResourcesFilename("best-chromosomes.csv");
  }

  private static String getOtherCodeFilename(String filename) {
    return String.format("src/main/otherCode/%s", filename);
  }

  private static void writeMeans(ArrayList<Double> means) {
    String[] entries = new String[means.size()];
    for (int i = 0; i < means.size(); i++) {
      entries[i] = means.get(i).toString();
    }
    CSVWriter writer;
    try {
      writer = new CSVWriter(new FileWriter(getOtherCodeFilename("chromosome-means.csv"), true));
      writer.writeNext(entries);
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void appendBestIndividual(Individual individual) {
    CSVWriter writer;
    try {
      writer = new CSVWriter(new FileWriter(getBestChromosomeFilename(), true));
      writer.writeNext(individual.getChromosomeCSVReady());
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static Individual runLoop(Optional<ArrayList<Individual>> populationArg) {
    ArrayList<Individual> population = populationArg.isPresent() ? populationArg.get()
        : createPopulation(populationSize);
    if (population.isEmpty()) {
      population = createPopulation(populationSize);
    }
    ArrayList<Individual> bestAllTime = new ArrayList<>();
    ArrayList<Double> means = new ArrayList<>();

    for (int i = 1; i <= generations; i++) {
      System.err.println(String.format("Running generation %d", i));
      runGeneration(population);
      means.add(printIndividualStats(population));
      population.sort(Individual.sortByFitness);
      bestAllTime.addAll(population.subList(0, 10));
      bestAllTime.sort(Individual.sortByFitness);
      if (bestAllTime.size() > populationSize) {
        bestAllTime = new ArrayList<>(bestAllTime.subList(0, populationSize));
      }
      population = createNewGeneration(bestAllTime);
    }

    writeMeans(means);
    Individual bestIndividual = bestAllTime.get(0);
    String formattedString = String.format("Best fitness of all generations: %f", bestIndividual.getFitness());
    System.err.println(formattedString);
    appendBestIndividual(bestIndividual);

    return bestIndividual;
  }

  private static List<Integer[]> parseRows(List<String[]> rows) {
    List<Integer[]> chromosomes = new ArrayList<>();
    for (String[] row : rows) {
      Integer[] chromosome = new Integer[row.length];
      for (int i = 0; i < row.length; i++) {
        chromosome[i] = Integer.parseInt(row[i]);
      }
      chromosomes.add(chromosome);
    }
    return chromosomes;
  }

  private static List<Individual> parseChromosomes(List<Integer[]> chromosomes) {
    List<Individual> individuals = new ArrayList<>();
    for (Integer[] chromosome : chromosomes) {
      individuals.add(new Individual(chromosome));
    }
    return individuals;
  }

  public static List<Individual> getCSVIndividuals() throws IOException {
    CSVReader reader = new CSVReader(new FileReader(getBestChromosomeFilename()));
    List<String[]> rows = reader.readAll();
    reader.close();
    return parseChromosomes(parseRows(rows));
  }

  private static void writeIndividualMeans(List<Individual> individuals) throws IOException {
    FileWriter fWriter = new FileWriter(getOtherCodeFilename("best-means.txt"));
    for (Individual individual : individuals) {
      String row = String.format("%f%n", individual.getFitness());
      fWriter.write(row);
    }
    fWriter.close();
  }

  private static void writeIndividuals() throws IOException {
    System.err.println("Writing individuals");
    ArrayList<Individual> individuals = currentBestIndividuals;
    numGames = 1000;
    runGeneration(individuals);
    individuals.sort(Individual.sortByFitness);
    if (individuals.size() > populationSize) {
      individuals = new ArrayList<>(individuals.subList(0, populationSize));
    }
    printIndividualStats(individuals);
    writeIndividualMeans(individuals);
    CSVWriter writer = new CSVWriter(new FileWriter(getBestChromosomeFilename()));
    List<String[]> rowsToWrite = individuals.stream().map(Individual::getChromosomeCSVReady)
        .collect(Collectors.toList());
    writer.writeAll(rowsToWrite);
    writer.close();
  }

  public static void main(String[] args) throws IOException {
    currentBestIndividuals = new ArrayList<>(getCSVIndividuals());
    for (int i = 1; i <= loopAlgoTimes; i++) {
      System.err.println(String.format("Running loop %d", i));
      runLoop(Optional.empty());
    }
    writeIndividuals();
  }
}
