# ubipong-api

## Tournament Organization

- Tournament (eg. Atlanta Giant Round Robin)
- Event (eg. Prelim Group 1)

  *An event is mapped to a "tournament" on challonge.com*

  *An event is represented by `List<MatchWrapper>`*

- Round (eg. Round of 16)

  For single elimination, each round is defined by the number of
  players in that round.

  For round robin, the nth round is roughly the nth match of a single
  player, but it is not as important to divide round robin into
  rounds.

- Match
