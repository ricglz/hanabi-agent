package com.fossgalaxy.games.fireworks.ai.rg20332;

import java.io.IOException;

import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.annotations.AgentBuilderStatic;

public class BestIndividualAgent {

    @AgentBuilderStatic("BestIndividualAgent")
    public static Agent buildRuleBased() {
        Individual bestIndividual;
        try {
            bestIndividual = RuleBasedSystemChromosomeEvaluator.getCSVIndividuals().get(0);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return new RuleBasedAgentGivenListOfRulesViaChromosome(bestIndividual.getChromosome());
    }

}
