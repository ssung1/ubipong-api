# ubipong-api

## Production Environment

```
https://ubipong-api.herokuapp.com
```

## Tournament Organization

- Tournament (eg. Atlanta Giant Round Robin)

  In challonge, this is called an "event" (exactly the opposite of how we name
  things).  However, challonge.com does not have API to manage "events", so we
  are not using challonge.com for this.  However, feel free to create an 
  "event" if working through challonge.com UI.

- Event

  An event is includes a set of players that have the chance of playing each
  other.

  For round robins, things are a bit more confusing.  We usually break round
  robins into groups because there are too many games to play if all the 
  players who sign up play with each other.  Each round robin group is
  considered an event because only the players in a group play each other.

  An event is mapped to a "tournament" on challonge.com.  In our database,
  we map our own event to a "tournament" through the "tournament" URL.

- Round (eg. Round of 16)

  For single elimination, each round is defined by the number of
  players in that round.

  For round robin, there is also the concept of a round, but it's not very
  important.  In each round, one player gets to play one match.

- Match

  A match consists of any odd number of games as defined by ITTF.

- Game

  A game is played until a player accumulates a number of points (called
  points_to_win) and has at least 2 points more than the opponent.

## How to Run a Tournament

*To get up-to-date info on the API calls, check Swagger.*

Create a tournament that contains all the events by issuing:

```
POST https://{host}/crud/tournaments

{
  "name": "Eat Sleep Pong Open 2019",
  "tournamentDate": "2019-03-15T00:00:00-0500"
}
```

The response status should be 201.  Get the location header of the response
and call the URL in the header.  We should get the complete tournament 
details:

```json
{
  "name": "Eat Sleep Pong Open 2019",
  "tournamentDate": "2019-03-15T00:00:00-0500"
  "tournamentId": 1
}
```

Keep the tournament ID for future reference.

### Adding Events

For each event in the tournament, call

```
POST https://{host}/rest/v0/events

{
  "tournamentId": 1,
  "name": "Preliminary Group 1",
  "challongeUrl": "esp_201903_pg_rr_1"
}
```

The response status should be 201.  The response body should contain the
ID for the newly created event:

```json
{
  "eventId": 101,
  "tournamentId": 1,
  "name": "Preliminary Group 1",
  "challongeUrl": "esp_201903_pg_rr_1"
}
```

The challongeUrl should be globally unique.  We recommend using this format:

```
{org}\_{yyyyMM}\_{event}\_{type}\_{groupNumber}
```

where

  - org is the organization hosting the tournament

  - yyyyMM is the year and month of the tournament

  - event is

    - pg: preliminary group
    - ch: championship
    - ca: class a
    - cb: class b
    - cc: class c
    - cd: class d
    - u1500: under 1500
    - u17yo: under 17 years old
    - o50yo: over 50 years old

  - type is

    - rr: round robin
    - se: single elimination

  - group is a number, only applicable in round robin

## Get Event Details

Events can now be retrieved through

```
GET https://{host}/crud/events
```

```json
{
  "_embedded": {
    "events": [
      {
        "eventId": 1,
        "challongeUrl": "esp_201903_pg_rr_1",
        "name": "Preliminary Group 1",
        "tournamentId": 1,
        "challongeTournament": null,
        "_links": { ... }
      }
    ]
  },
  "_links": { ... },
  "page": { ... }
}
```

This shows the information in the database.  To see challonge.com details,
use

```
https://{host}/rest/v0/events/{challongeUrl}
```

shows the event details.

```json
{
  "eventId": 9234850,
  "challongeUrl": null,
  "name": "esp_201903_pg_rr_1",
  "tournamentId": null,
  "challongeTournament": {
    "id": 9234850,
    "name": "Preliminary Group 1",
    "url": "esp_201903_pg_rr_1",
    "description": "Preliminary Group 1",
    "subdomain": null,
    "tournament_type": "round robin",
    "game_name": "Table Tennis"
  }
}
```

# TODO TODO TODO TODO TODO TODO TODO Below

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

## Swagger UI

```
https://{host}/swagger-ui/index.html
```
