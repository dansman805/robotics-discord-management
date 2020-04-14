package com.github.dansman805.discordbot.commands

import com.google.common.eventbus.Subscribe
import me.aberrantfox.kjdautils.api.annotation.CommandSet
import me.aberrantfox.kjdautils.api.dsl.command.commands
import me.aberrantfox.kjdautils.api.dsl.embed
import me.aberrantfox.kjdautils.extensions.jda.hasRole
import me.aberrantfox.kjdautils.extensions.jda.message
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
                it.channel.message(messageContent) { message: Message ->
                    deleteEvents.add(message.delete())
                }
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

class Infection
/*@Subscribe
fun onMessageReceived(event: MessageReceivedEvent) {


    val nextMessage = event
            .message
            .textChannel
            .getHistoryAfter(event.message, 2)
            .complete()
            .retrievedHistory
            .first()*/

        /*val previousMessage = event
                .message
                .textChannel
                .getHistoryBefore(event.message, 2)
                .complete()
                .retrievedHistory
                .first()

        if (Random.nextDouble(0.0, 1.0) > 0.2 || LocalDateTime.now().dayOfMonth != 1) {
            return
        }

        if (!previousMessage.author.isBot) {
            if (previousMessage.member!!.roles.none {it.idLong == infectedID}) {
                event.guild.addRoleToMember(
                        previousMessage.guild.getMember(previousMessage.author)!!,
                        event.guild.getRoleById(infectedID)!!
                ).complete()

                println("${previousMessage.author},${LocalDateTime.now().hour},${LocalDateTime.now().minute},${LocalDateTime.now().second}")
            }
        }

        /*if (!nextMessage.author.isBot && Random.nextDouble(0.0, 1.0) < 0.75) {
            println("Infecting: ${nextMessage.author}")

            event.guild.addRoleToMember(
                    nextMessage.guild.getMember(nextMessage.author)!!,
                    event.guild.getRoleById(infectedID)!!
            )
        }*/
    }*/
