package com.fossgalaxy.games.fireworks.ai.rg20332;

import com.fossgalaxy.games.fireworks.ai.osawa.rules.OsawaDiscard;
import com.fossgalaxy.games.fireworks.ai.osawa.rules.TellPlayableCardOuter;
import com.fossgalaxy.games.fireworks.ai.rule.CompleteTellUsefulCard;
import com.fossgalaxy.games.fireworks.ai.rule.DiscardHighest;
import com.fossgalaxy.games.fireworks.ai.rule.DiscardOldestFirst;
import com.fossgalaxy.games.fireworks.ai.rule.DiscardOldestNoInfoFirst;
import com.fossgalaxy.games.fireworks.ai.rule.DiscardSafeCard;
import com.fossgalaxy.games.fireworks.ai.rule.DiscardUnidentifiedCard;
import com.fossgalaxy.games.fireworks.ai.rule.DiscardUselessCard;
import com.fossgalaxy.games.fireworks.ai.rule.PlaySafeCard;
import com.fossgalaxy.games.fireworks.ai.rule.PlayUniquePossibleCard;
import com.fossgalaxy.games.fireworks.ai.rule.ProductionRuleAgent;
import com.fossgalaxy.games.fireworks.ai.rule.Rule;
import com.fossgalaxy.games.fireworks.ai.rule.TellAboutOnes;
import com.fossgalaxy.games.fireworks.ai.rule.TellAnyoneAboutOldestUsefulCard;
import com.fossgalaxy.games.fireworks.ai.rule.TellAnyoneAboutUsefulCard;
import com.fossgalaxy.games.fireworks.ai.rule.TellAnyoneAboutUselessCard;
import com.fossgalaxy.games.fireworks.ai.rule.TellDispensable;
import com.fossgalaxy.games.fireworks.ai.rule.TellFives;
import com.fossgalaxy.games.fireworks.ai.rule.TellIllInformed;
import com.fossgalaxy.games.fireworks.ai.rule.TellMostInformation;
import com.fossgalaxy.games.fireworks.ai.rule.TellToSave;
import com.fossgalaxy.games.fireworks.ai.rule.TellToSavePartialOnly;
import com.fossgalaxy.games.fireworks.ai.rule.TellUnknown;
import com.fossgalaxy.games.fireworks.ai.rule.TryToUnBlock;
import com.fossgalaxy.games.fireworks.ai.rule.finesse.PlayFinesse;
import com.fossgalaxy.games.fireworks.ai.rule.finesse.PlayFinesseTold;
import com.fossgalaxy.games.fireworks.ai.rule.finesse.TellFinesse;
import com.fossgalaxy.games.fireworks.ai.rule.random.DiscardLeastLikelyToBeNecessary;
import com.fossgalaxy.games.fireworks.ai.rule.random.DiscardProbablyUselessCard;
import com.fossgalaxy.games.fireworks.ai.rule.random.DiscardRandomly;
import com.fossgalaxy.games.fireworks.ai.rule.random.PlayProbablySafeCard;
import com.fossgalaxy.games.fireworks.ai.rule.random.TellPlayableCard;
import com.fossgalaxy.games.fireworks.ai.rule.random.TellRandomly;
import com.fossgalaxy.games.fireworks.ai.rule.simple.DiscardIfCertain;
import com.fossgalaxy.games.fireworks.ai.rule.simple.PlayIfCertain;

/**
 * Example extending the ProductionRuleAgent such that it can be initialised
 * with a chromosome array.
 */
public class RuleBasedAgentGivenListOfRulesViaChromosome extends ProductionRuleAgent {
  static int amountOfDiscardRules = 20;
  static int tellRulesLowBound = amountOfDiscardRules;
  static int amountOfTellRules = 17;
  static int playRulesLowBound = tellRulesLowBound + amountOfTellRules;
  static int amountOfPlayRules = 14;
  static int specialRulesLowBound = playRulesLowBound + amountOfPlayRules;
  static int amountOfSpecialRules = 1;
  static int amountOfRules = amountOfDiscardRules + amountOfPlayRules + amountOfSpecialRules + amountOfTellRules;

  private static Rule createDiscardRule(int ruleNumber) {
    switch (ruleNumber) {
      case 0:
        return new DiscardRandomly();
      case 1:
        return new DiscardIfCertain();
      case 2:
        return new DiscardSafeCard();
      case 3:
        return new DiscardOldestFirst();
      case 4:
        return new DiscardUselessCard();
      case 5:
        return new OsawaDiscard();
      case 6:
        return new DiscardLeastLikelyToBeNecessary();
      case 7:
        return new DiscardProbablyUselessCard();
      case 8:
        return new DiscardOldestNoInfoFirst();
      case 9:
        return new DiscardProbablyUselessCard(.1);
      case 10:
        return new DiscardProbablyUselessCard(.2);
      case 11:
        return new DiscardProbablyUselessCard(.3);
      case 12:
        return new DiscardProbablyUselessCard(.4);
      case 13:
        return new DiscardProbablyUselessCard(.5);
      case 14:
        return new DiscardProbablyUselessCard(.6);
      case 15:
        return new DiscardProbablyUselessCard(.7);
      case 16:
        return new DiscardProbablyUselessCard(.8);
      case 17:
        return new DiscardProbablyUselessCard(.9);
      case 18:
        return new DiscardHighest();
      case 19:
        return new DiscardUnidentifiedCard();
      default:
        return null;
    }
  }

  private static Rule createTellRule(int ruleNumber) {
    switch (ruleNumber) {
      case 20:
        return new TellPlayableCard();
      case 21:
        return new TellPlayableCardOuter();
      case 22:
        return new TellRandomly();
      case 23:
        return new TellAboutOnes();
      case 24:
        return new TellAnyoneAboutUsefulCard();
      case 25:
        return new TellUnknown();
      case 26:
        return new TellAnyoneAboutUselessCard();
      case 27:
        return new TellDispensable();
      case 28:
        return new TellMostInformation();
      case 29:
        return new TellIllInformed();
      case 30:
        return new TellFives();
      case 31:
        return new TellMostInformation(true);
      case 32:
        return new CompleteTellUsefulCard();
      case 33:
        return new TellAnyoneAboutOldestUsefulCard();
      case 34:
        return new TellToSave();
      case 35:
        return new TellToSavePartialOnly();
      case 36:
        return new TellFinesse();

      default:
        return null;
    }
  }

  private static Rule createPlayRule(int ruleNumber) {
    switch (ruleNumber) {
      case 37:
        return new PlaySafeCard();
      case 38:
        return new PlayIfCertain();
      case 39:
        return new PlayProbablySafeCard(.1);
      case 40:
        return new PlayProbablySafeCard(.2);
      case 41:
        return new PlayProbablySafeCard(.3);
      case 42:
        return new PlayProbablySafeCard(.4);
      case 43:
        return new PlayProbablySafeCard(.5);
      case 44:
        return new PlayProbablySafeCard(.6);
      case 45:
        return new PlayProbablySafeCard(.7);
      case 46:
        return new PlayProbablySafeCard(.8);
      case 47:
        return new PlayProbablySafeCard(.9);
      case 48:
        return new PlayUniquePossibleCard();
      case 49:
        return new PlayFinesse();
      case 50:
        return new PlayFinesseTold();

      default:
        return null;
    }
  }

  private static Rule createNewRule(int ruleNumber) {
    if (ruleNumber <= 19)
      return createDiscardRule(ruleNumber);
    if (ruleNumber <= 36)
      return createTellRule(ruleNumber);
    if (ruleNumber <= 50)
      return createPlayRule(ruleNumber);
    if (ruleNumber == 51)
      return new TryToUnBlock();
    return null;
  }

  public RuleBasedAgentGivenListOfRulesViaChromosome(Integer[] chromosome) {
    super();
    for (int i = 0; i < chromosome.length; i++) {
      int ruleNumber = chromosome[i];
      Rule newRule = createNewRule(ruleNumber);
      if (newRule != null) {
        this.addRule(newRule);
      }
    }
  }
}
