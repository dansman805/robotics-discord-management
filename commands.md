# Commands

## Key
| Symbol     | Meaning                    |
| ---------- | -------------------------- |
| (Argument) | This argument is optional. |

## Developer
| Commands       | Arguments | Description             |
| -------------- | --------- | ----------------------- |
| DBMessageCount | <none>    | No Description Provider |
| Refresh        | <none>    | No Description Provider |

## Fun
| Commands | Arguments        | Description                        |
| -------- | ---------------- | ---------------------------------- |
| Bolb     | File, (Bolbways) | No Description Provider            |
| Fight    | Member           | Start a fight with another member. |

## Info
| Commands                                                                  | Arguments | Description                                       |
| ------------------------------------------------------------------------- | --------- | ------------------------------------------------- |
| Guild, Server, GuildInformation, ServerInformation, GuildInfo, ServerInfo | <none>    | Retrieve information about this guild.            |
| Member, UserInformation, UserInfo, User                                   | (Member)  | Retrieve information about a member of the guild. |

## Mechanism Analysis
| Commands       | Arguments                    | Description                                                                                     |
| -------------- | ---------------------------- | ----------------------------------------------------------------------------------------------- |
| Drivetrain, DT | Gear Ratio, Decimal, (Motor) | Provides statistics about a drivetrain with a wheel diameter, gear ratio and optionally a motor |
| Gear           | Gear Ratio, (Motor)          | Provides statistics about a motor when geared                                                   |
| Motor          | Motor                        | Provides statistics about a motor                                                               |

## Moderation
| Commands                                                       | Arguments      | Description                                                  |
| -------------------------------------------------------------- | -------------- | ------------------------------------------------------------ |
| Ban                                                            | Member, (Text) | Bans someone in the guild for a given reason                 |
| DeleteRoles                                                    | <none>         | Delete's the time-based roles                                |
| Kick                                                           | Member, (Text) | Kick someone in the guild for a given reason                 |
| MemberFirstJoin                                                | Member         | No Description Provider                                      |
| RefreshRoles                                                   | <none>         | Refreshes the time-based roles                               |
| RoleStatistics, RoleStat, RoleStats, RoleInformation, RoleInfo | (Role)         | Shows the number of users with a given role or all the roles |
| Unban                                                          | User, (Text)   | Unbans someone in the guild                                  |
| Warn                                                           | User, Text     | Warn an user                                                 |

## Statistics
| Commands                 | Arguments                            | Description                                                                                   |
| ------------------------ | ------------------------------------ | --------------------------------------------------------------------------------------------- |
| ChannelDistribution      | (User), (Boolean)                    | Graphs the amount of messages in each channel for either a provided user or the guild.        |
| CumulativeMessages       | (User), (Word), (Integer), (Boolean) | Graphs the cumulative amount of messages for either a provided user or the guild.             |
| HourDistribution, Hourly | (User), (Word), (Boolean)            | Graphs the amount of messages hourly in each channel for either a provided user or the guild. |
| Markov                   | User, (Integer), (Word)              | Uses a Markov chain to generate text in the style of a given user                             |
| MessageRanking           | (Integer), (Word), (Boolean)         | Graphs the ranking of members by messages of the server with an optional filter.              |
| Messages                 | (User), (Word), (Integer), (Boolean) | Graphs the amount of messages for either a provided user or the guild.                        |
| WordsPerMessage          | (Reversed), (SVG)                    | Graphs the amount of words per message each member of a guild.                                |

## Utility
| Commands                             | Arguments   | Description                                                                                |
| ------------------------------------ | ----------- | ------------------------------------------------------------------------------------------ |
| Help                                 | (Command)   | Display a help menu.                                                                       |
| Ping                                 | <none>      | Responds with Pong! (As well as the server name, and the time it takes the bot to respond) |
| SetNickname, SetNick, Nickname, Nick | Text        | Allows a member to change their nickname.                                                  |
| TheBlueAlliance, TBA                 | Team Number | Provides data on a given FRC team from The Blue Alliance                                   |
| TheOrangeAlliance, TOA               | Team Number | Provides data on a given FTC team from The Orange Alliance                                 |
| Wikipedia, Wiki, W                   | Text        | Provides the Wikipedia summary on a given topic                                            |

