# Commands

## Key
| Symbol     | Meaning                    |
| ---------- | -------------------------- |
| (Argument) | This argument is optional. |

## Developer
| Commands       | Arguments | Description      |
| -------------- | --------- | ---------------- |
| DBMessageCount | <none>    | <No Description> |
| Eval           | Text      | <No Description> |
| Refresh        | <none>    | <No Description> |

## Fun
| Commands | Arguments                       | Description                        |
| -------- | ------------------------------- | ---------------------------------- |
| Bolb     | File, (Bolb Type), (Face Color) | <No Description>                   |
| Fight    | Member                          | Start a fight with another member. |

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
| MemberFirstJoin                                                | Member         | <No Description>                                             |
| RefreshRoles                                                   | <none>         | Refreshes the time-based roles                               |
| RoleStatistics, RoleStat, RoleStats, RoleInformation, RoleInfo | (Role)         | Shows the number of users with a given role or all the roles |
| Unban                                                          | User, (Text)   | Unbans someone in the guild                                  |
| Warn                                                           | Member, (Text) | Warn a member                                                |

## Statistics
| Commands                        | Arguments                    | Description                                                                                   |
| ------------------------------- | ---------------------------- | --------------------------------------------------------------------------------------------- |
| ChannelDistribution             | (User), (Boolean)            | Graphs the amount of messages in each channel for either a provided user or the guild.        |
| CumulativeMessages              | (User), (Integer), (Boolean) | Graphs the cumulative amount of messages for either a provided user or the guild.             |
| HourDistribution, Hourly        | (User), (Boolean)            | Graphs the amount of messages hourly in each channel for either a provided user or the guild. |
| MessageRanking, MessageRankings | (Integer), (Boolean)         | Graphs the ranking of members by messages.                                                    |
| Messages                        | (User), (Integer), (Boolean) | Graphs the amount of messages for either a provided user or the guild.                        |

## Utility
| Commands                             | Arguments   | Description                                                                                |
| ------------------------------------ | ----------- | ------------------------------------------------------------------------------------------ |
| Help                                 | (Command)   | Display a help menu.                                                                       |
| Ping                                 | <none>      | Responds with Pong! (As well as the server name, and the time it takes the bot to respond) |
| SetNickname, SetNick, Nickname, Nick | Text        | Allows a member to change their nickname.                                                  |
| TheBlueAlliance, TBA                 | Team Number | Provides data on a given FRC team from The Blue Alliance                                   |
| TheOrangeAlliance, TOA               | Team Number | Provides data on a given FTC team from The Orange Alliance                                 |
| Vote                                 | Text        | <No Description>                                                                           |
| Wikipedia, Wiki, W                   | Text        | Provides the Wikipedia summary on a given topic                                            |

