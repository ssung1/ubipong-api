# ubipong-api

## Production Environment

```
https://ubipong-api.herokuapp.com
```

## Deploying to Heroku

If Java plugin has not been installed:

    heroku plugins:install java

If application does not yet exist on Heroku:

    heroku create --no-remote ubipong-api

Deploy:

    heroku deploy:jar target/my-app.jar --app ubipong-api

## Swagger UI

```
https://{host}/swagger-ui/index.html
```

## Tournament Organization

- Tournament (eg. Atlanta Giant Round Robin)

  In challonge, this is called an "event" (exactly the opposite of how we name
  things).  However, challonge.com does not have API to manage "events", so we
  are not using challonge.com for this.

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

The response should have a status of 201 and should contain complete tournament 
details:

```json
{
  "id": 1,
  "name": "Eat Sleep Pong Open 2019",
  "tournamentDate": "2019-03-15T00:00:00-0500"
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
  "id": 101,
  "challongeUrl": "esp_201903_pg_rr_1",
  "name": "Preliminary Group 1",
  "tournamentId": 1
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

  - group is a number, mostly used by round robin when breaking event into smaller groups

## Get Event Details

Events can now be retrieved through

```
GET https://{host}/crud/events/{eventId}
```

```json
{
  "id": 1,
  "challongeUrl": "esp_201903_pg_rr_1",
  "name": "Preliminary Group 1",
  "tournamentId": 1,
  "challongeTournament": null,
  "_links": { ... }
}
```

This shows the information in the database.  To see challonge.com details,
use

```
GET https://{host}/rest/v0/events/{id}
```

shows the event details.

```json
{
  "id": 9234850,
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

To see all the events within a tournament, use

```
GET https://{host}/crud/events/search/findByTournamentId?tournamentId={tournamentId}
```

```json
{
  "_embedded": {
    "events": [
      {
        "id": 1,
        "challongeUrl": "esp_201903_pg_rr_1",
        "name": "Preliminary Group 1",
        "tournamentId": 1,
        "challongeTournament": null,
        "_links": { ... }
      }
    ]
  },
  "_links": { ... }
}
```

## Print Match Sheets

Players will require matches sheets to record their scores.  To print the match
sheet for each event, make sure players are added to the "tournament" from
challonge.com, and start the "tournament".

Then make this call:

```
GET https://{host}/rest/v0/events/{challongeUrl}/roundRobinMatchList
```

```json
[
  {
    "matchId": 222695811,
    "status": 11,
    "player1Id": 83173696,
    "player2Id": 83173698,
    "winnerId": null,
    "resultCode": null,
    "player1Seed": "B",
    "player2Seed": "C",
    "player1Name": "patrick",
    "player2Name": "squidward"
  }
]
```

Each of these can now be use to generate a match sheet (probably best to use
the UI for this).  For example:

| Seed | Player | Game 1 | Game 2 | Game 3 | Game 4 | Game 5 |
| ---  | ---    | ---    | ---    | ---    | ---    | ---    |
| B    | patrick |       |        |        |        |        |
| C    | squidward |     |        |        |        |        |

Keep in mind that for elimination events, we need to complete a few matches
before we know the subsequent matches.

## Enter Scores

As players complete each match, enter the scores in challonge.com.

To view the current status of each event, use this API:

```
https://{host}/rest/v0/events/{challongeUrl}/roundRobinGrid
```

We would get a table of cells that contains the 

```json
[
  [
    {
      "type": 11,
      "content": "",
      "winForPlayer1": false,
      "winByDefault": false,
      "gameList": []
    },
    {
      "type": 11,
      "content": "",
      "winForPlayer1": false,
      "winByDefault": false,
      "gameList": []
    },
    {
      "type": 14,
      "content": "A",
      "winForPlayer1": false,
      "winByDefault": false,
      "gameList": []
    },
    ...
  ],
  ...
],
```

The output is for display only since if we just care about the statuses of the matches,
we can get them from challonge.com.  We can display the output using the UI.  The output
would look like this:

|     |           | A  | B         | C   |
| --- | ---       | ---| ---       | --- |
| A   | SpongeBob | -- | W 9 8 9   |     |
| B   | Patrick   |    | --        |     |
| C   | Squidward |    |           | --  |

### Generate the Results File

To look at the results of one event, try this

```
GET https://{host}/rest/v0/events/{challongeUrl}/result
```

```json
[
  {
    "winner": "squidward",
    "loser": "patrick",
    "eventName": "Preliminary Group 1",
    "resultString": "7 7 9"
  },
  {
    "winner": "spongebob",
    "loser": "patrick",
    "eventName": "Preliminary Group 1",
    "resultString": "3 5 5"
  }
]
```

To get results of all the events of one tournament, so we can use this

```
GET https://{host}/rest/v0/tournaments/{id}/result
```

```json
{
  "tournamentName": "Eat Sleep Pong Open 2019",
  "tournamentDate": "2019-03-15T05:00:00.000+00:00",
  "tournamentResultList": [
    {
      "winner": "squidward",
      "loser": "patrick",
      "eventName": "Preliminary Group 1",
      "resultString": "7 7 9"
    },
    {
      "winner": "spongebob",
      "loser": "patrick",
      "eventName": "Preliminary Group 1",
      "resultString": "3 5 5"
    }
  ]
}
```

To get results for USATT reporting, do this

```
GET https://{host}/rest/v0/tournaments/{id}/usatt-result
```

```csv
EventID,MemNum_W,MemNum_L,Score,Division
12345,123,234,"3,5,6",Preliminary Group 1
```
