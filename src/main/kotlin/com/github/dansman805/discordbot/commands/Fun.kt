package com.github.dansman805.discordbot.commands

import com.github.dansman805.discordbot.extensions.safe
import com.google.common.eventbus.Subscribe
import me.aberrantfox.kjdautils.api.annotation.CommandSet
import me.aberrantfox.kjdautils.api.dsl.command.commands
import me.aberrantfox.kjdautils.api.dsl.embed
import me.aberrantfox.kjdautils.extensions.jda.hasRole
import me.aberrantfox.kjdautils.internal.arguments.MemberArg
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.requests.RestAction
import java.time.LocalDateTime
import kotlin.math.max
import kotlin.random.Random

@CommandSet("Fun")
fun funCommands() = commands {
    command("Fight") {
        description = "Start a fight with another member."

        execute(MemberArg) {
            it.safe {
                val responses = listOf(
                        "was hit on the head by",
                        "was kicked by",
                        "was slammed into a wall by",
                        "was dropkicked by",
                        "was DDoSed by",
                        "was chokeslammed by",
                        "was run over with a robot by",
                        "had their IQ dropped 15 points by",
                        "had a heavy object dropped on them by",
                        "was beat up by"
                )
                // val damageValues = listOf(100, 150, 200, 300, 50, 250, 420, 69)

                val players = listOf(it.guild!!.getMember(it.author)!!, it.args.first)

                val healths = arrayListOf(1000, 1000)

                var turn = Random.nextInt(0, 1)

                val turnMessages = arrayListOf<String>()

                while (healths[0] > 0 && healths[1] > 0) {
                    val opponentIndex = (turn + 1) % 2
                    val damage = Random.nextInt(5, 43) * 10

                    healths[opponentIndex] = max(healths[opponentIndex] - damage, 0)

                    turnMessages.add(
                            "**${players[opponentIndex].effectiveName}** " +
                                    "${responses.random()} " +
                                    "**${players[turn].effectiveName}**! " +
                                    "*-$damage hp, ${healths[opponentIndex]} HP remaining*"
                    )

                    turn = opponentIndex
                }

                val deleteEvents = arrayListOf<RestAction<Void>>()

                turnMessages.forEach { messageContent ->
                    deleteEvents.add(it.channel.sendMessage(messageContent).complete().delete())
                    Thread.sleep(1500)
                }

                val winner = players[healths.indexOf(healths.max())]
                val loser = players[healths.indexOf(healths.min())]

                it.respond(embed {
                    title = "Fight Result"
                    description = "Describes the result of a fight"

                    field {
                        name = "Winner"
                        value = winner.effectiveName
                        inline = true
                    }

                    field {
                        name = "Loser"
                        value = loser.effectiveName
                        inline = true
                    }
                })

                Thread.sleep(5000)

                deleteEvents.forEach { deleteEvent ->
                    deleteEvent.complete()
                }
            }
        }
    }
}
