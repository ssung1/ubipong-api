# ubipong-api

## Production Environment

```
https://ubipong-api.herokuapp.com
```

## Tournament Organization

- Tournament (eg. Atlanta Giant Round Robin)

  - Event (eg. Prelim Group 1)

    *An event is mapped to a "tournament" on challonge.com*

    *An event is represented by `List<MatchWrapper>`*

    - Round (eg. Round of 16)

      For single elimination, each round is defined by the number of
      players in that round.

    - Round Robin Group

      For round robin, the players are divided into groups.  Challonge.com
      does not have support for this, so we need to simulate each group
      as one "tournament" on challonge.com.

      - Match

## How to Set up a Tournament

## How to Set up an Event

Log on to challonge.com and create a tournament.

- For tournament name, enter the name of the round robin group, such as
  "Preliminary Group 1"

- For URL, enter the name that is easily typed, we recommend using a format
  such as {org}\_{yyyyMM}\_{event}\_{type}\_{groupNumber}

  where org is the organization hosting the tournament

  where yyyyMM is the year and month of the tournament

  where event is

  - pg: preliminary group
  - ch: championship
  - ca: class a
  - cb: class b
  - cc: class c
  - cd: class d
  - u1500: under 1500
  - u17yo: under 17 year old

  where type is

  - rr: round robin
  - se: single elimination

  where group is a number, only applicable in round robin

Once the "tournament" is created on challonge, run this application
and try these URLs:

```
https://{host}/rest/v0/events/{eventUrl}
```

shows the event details.

```
https://{host}/rest/v0/event/{eventUrl}/roundRobinGrid
```

returns the contents that can be used to display a round robin grid.

<!-- and the UI.  Then go to the URL

    https://{host}/#/rr-grid?eventList=%5B%22{event}%22%5D

to view the round robin grid. -->

## How to Set up a Round Robin Event

Remember, a round robin event is made of multiple groups.  Each group is one
"tournament" on challonge.com.

Go to the UI and enter all the names.  Choose the number of players per group.
Select "Create Group".  After that, copy the names of each group and paste them
onto challonge.com particpant bulk add.

## How to Set up an Event to Collect Results

The results in challonge.com cannot be used to calculate ratings.  To allow
results to be exported to the correct format, create a tournament, then for
each event in the tournament, create an event:

```
POST https://{host}/crud/events

{
    "name": "Preliminary Group 1",
    "challongeUrl": "ecs_201904_rr_pg_1",
    "tournamentId": 1
}
```

## How to Generate the Results File

To look at the results of one event, try this

```
https://{host}/rest/v0/events/{eventName}/result
```

However, we need the results of all the events, so we can use this

```
https://{host}rest/v0/tournaments/{tournamentId}/result
```


