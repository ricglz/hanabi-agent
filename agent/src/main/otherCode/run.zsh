mvn clean install \
  -B \
  -Dorg.slf4j.simpleLogger.defaultLogLevel=off
mvn exec:java \
  -B \
  -Dexec.mainClass=com.fossgalaxy.games.fireworks.ai.rg20332.RuleBasedSystemChromosomeEvaluator \
  -Dorg.slf4j.simpleLogger.defaultLogLevel=off
