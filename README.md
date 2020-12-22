# Hanabi Agent

Agent done for Game AI course. The Hanabi framework used was the [Fossgalaxy framework](https://github.com/fossgalaxy/hanabi)

## How to run the Genetic Algorithm engine

This can be easily by doing the following

- `cd agent`
- `src/main/otherCode/run.zsh`

This should run the algorithm 60 times, you can modify the `loopAlgoTimes` variable to _1_ so that it runs just once in the `RuleBasedSystemChromosomeEvaluator` class.

## How to run the Agent

The name of the Agent is named "BestIndividualAgent", this can be run by doing:

```java
AgentUtils.buildAgent("BestIndividualAgent")
```
