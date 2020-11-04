package com.github.dansman805.discordbot.commands

import com.github.dansman805.discordbot.arguments.FileURLArg
import com.github.dansman805.discordbot.botConfig
import com.gitlab.kordlib.core.behavior.channel.createEmbed
import com.gitlab.kordlib.core.behavior.channel.createMessage
import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.ScaleMethod
import com.sksamuel.scrimage.color.RGBColor
import com.sksamuel.scrimage.nio.PngWriter
import me.jakejmattson.discordkt.api.arguments.ChoiceArg
import me.jakejmattson.discordkt.api.arguments.HexColorArg
import me.jakejmattson.discordkt.api.arguments.MemberArg
import me.jakejmattson.discordkt.api.dsl.commands
import java.awt.Color
import java.io.File
import java.time.LocalDateTime
import kotlin.math.max
import kotlin.random.Random

fun funCommands() = commands("Fun") {
    guildCommand("Fight") {
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

            val players = listOf(getMember()!!, args.first)

            val healths = arrayListOf(1000, 1000)

            var turn = Random.nextInt(0, 1)

            val turnMessages = arrayListOf<String>()

            while (healths[0] > 0 && healths[1] > 0) {
                val opponentIndex = (turn + 1) % 2
                val damage = Random.nextInt(5, 43) * 10

                healths[opponentIndex] = max(healths[opponentIndex] - damage, 0)

                turnMessages.add(
                        "**${players[opponentIndex].displayName}** " +
                                "${responses.random()} " +
                                "**${players[turn].displayName}**! " +
                                "*-$damage hp, ${healths[opponentIndex]} HP remaining*"
                )

                turn = opponentIndex
            }

            val messagesIdsToDelete = turnMessages.map {
                channel.createMessage(it).id
            }

            val winner = players[healths.indexOf(healths.max())]
            val loser = players[healths.indexOf(healths.min())]

            channel.createEmbed {
                title = "Fight Result"
                description = "Describes the result of a fight"

                field {
                    name = "Winner"
                    value = winner.displayName
                    inline = true
                }

                field {
                    name = "Loser"
                    value = loser.displayName
                    inline = true
                }
            }

            Thread.sleep(5000)

            channel.bulkDelete(messagesIdsToDelete)
        }
    }

    command("Bolb") {
        execute(FileURLArg,
                ChoiceArg("Bolb Type", "Bolb", "Bolbolb", "Bolbways",
                        "Squishbolbolb", "Tallbolb").makeOptional { "Bolb" },
                HexColorArg("Face Color").makeOptional(Color(47, 47, 47))) {
            var bolbImage = ImmutableImage.loader().fromFile("bolbs/" +
                    when (args.second) {
                        "Bolbways" -> "bolbways-face"
                        else -> "bolb-face"
                    } + ".png"
            )


            if (args.second == "Bolbolb" || args.second == "Squishbolbolb") {
                bolbImage = bolbImage.flipX()
            }

            val inputImage = ImmutableImage.loader().fromFile(args.first).scaleTo(bolbImage.width, bolbImage.height)

            var outputImage = bolbImage.blank()
            val outputFile = File("${botConfig.dateTimeFormatter.format(LocalDateTime.now())}.png")

            inputImage.forEach { inputPixel ->
                val bolbPixel = bolbImage.pixel(inputPixel.x, inputPixel.y)

                if (bolbPixel.toColor().toAWT() == Color.BLACK) {
                    outputImage.setColor(inputPixel.x, inputPixel.y, RGBColor.fromAwt(args.third))
                } else if (bolbPixel.toColor().toAWT() == Color.WHITE) {
                    outputImage.setPixel(inputPixel)
                } else {
                    outputImage.setColor(inputPixel.x, inputPixel.y, RGBColor.fromAwt(Color(0, 0, 0, 0)))
                }
            }

            outputImage = when (args.second) {
                "Squishbolbolb" -> {
                    outputImage.scaleToHeight(outputImage.height / 4, ScaleMethod.Bicubic,
                            false)
                }
                "Tallbolb" -> {
                    outputImage.scaleToWidth(outputImage.width / 4, ScaleMethod.Bicubic,
                            false)
                }
                else -> {
                    outputImage
                }
            }

            channel.createMessage {
                addFile("probablycursedbolb.png",
                        outputImage.forWriter(PngWriter.NoCompression).stream())
            }
        }
    }
}
