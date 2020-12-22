package com.fossgalaxy.games.fireworks.ai.rg20332;

import java.util.Random;

import com.fossgalaxy.games.fireworks.GameRunner;
import com.fossgalaxy.games.fireworks.GameStats;
import com.fossgalaxy.games.fireworks.ai.AgentPlayer;
import com.fossgalaxy.games.fireworks.players.Player;
import com.fossgalaxy.stats.BasicStats;
import com.fossgalaxy.stats.StatsSummary;

public class IndividualThread extends Thread {
  Individual individual;
  static Random random = new Random();

  IndividualThread(Individual individual) {
    this.individual = individual;
  }

  @Override
  public void run() {
    int numPlayers = RuleBasedSystemChromosomeEvaluator.numPlayers;
    int numGames = RuleBasedSystemChromosomeEvaluator.numGames;
    try {
      StatsSummary statsSummary = evaluateChromosome(individual.getChromosome(), numPlayers, numGames);
      individual.setFitness(statsSummary.getMean());
    } catch (InvalidRulesetException e) {
      individual.setFitness(0);
    }
  }

  private StatsSummary evaluateChromosome(Integer[] chromosome, int numPlayers, int numGames)
      throws InvalidRulesetException {
    String agentName = "RuleBasedAgentGivenListOfRulesViaChromosome";

    StatsSummary statsSummary = new BasicStats();

    for (int i = 0; i < numGames; i++) {
      GameRunner runner = new GameRunner("test-game", numPlayers);

      // add your agents to the game
      for (int j = 0; j < numPlayers; j++) {
        // the player class keeps track of our state for us...
        Player player = new AgentPlayer(agentName, new RuleBasedAgentGivenListOfRulesViaChromosome(chromosome));
        runner.addNamedPlayer(agentName, player);
      }
      GameStats stats = runner.playGame(random.nextLong());
      statsSummary.add(stats.score);
      if (stats.disqal != 0) {
        // Our agent was disqualified by the game engine.
        // Hence it appears that this chromosome is not a valid rule set.
        throw new InvalidRulesetException();
      }
    }
    return statsSummary;
  }

  public static class InvalidRulesetException extends Exception {
    private static final long serialVersionUID = 1L;
  }

}
