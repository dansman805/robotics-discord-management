package com.github.dansman805.discordbot.commands

import com.github.dansman805.discordbot.botConfig
import com.github.dansman805.discordbot.extensions.safe
import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.MutableImage
import com.sksamuel.scrimage.ScaleMethod
import com.sksamuel.scrimage.angles.Degrees
import com.sksamuel.scrimage.angles.Radians
import com.sksamuel.scrimage.color.RGBColor
import com.sksamuel.scrimage.nio.PngWriter
import com.sksamuel.scrimage.pixels.Pixel
import com.twelvemonkeys.imageio.ImageWriterBase
import me.aberrantfox.kjdautils.api.annotation.CommandSet
import me.aberrantfox.kjdautils.api.dsl.command.commands
import me.aberrantfox.kjdautils.api.dsl.embed
import me.aberrantfox.kjdautils.internal.arguments.*
import net.dv8tion.jda.api.requests.RestAction
import java.awt.Color
import java.io.File
import java.time.LocalDateTime
import kotlin.math.PI
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.math.sqrt
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

    command("Bolb") {
        execute (FileArg,
                ChoiceArg("Bolb Type", "Bolb", "Bolbolb", "Bolbways",
                        "Squishbolbolb", "Tallbolb").makeOptional { "Bolb" },
                HexColorArg("Face Color").makeOptional(Color(47, 47, 47))) {
            var bolbImage = ImmutableImage.loader().fromFile("bolbs/" +
                    when (it.args.second) {
                        "Bolbways" -> "bolbways-face"
                        else -> "bolb-face"
                    } + ".png"
            )


            if (it.args.second == "Bolbolb" || it.args.second == "Squishbolbolb") {
                bolbImage = bolbImage.flipX()
            }

            val inputImage = ImmutableImage.loader().fromFile(it.args.first).scaleTo(bolbImage.width, bolbImage.height)

            var outputImage = bolbImage.blank()
            val outputFile = File("${botConfig.dateTimeFormatter.format(LocalDateTime.now())}.png")

            it.safe {
                inputImage.forEach { inputPixel ->
                    val bolbPixel = bolbImage.pixel(inputPixel.x, inputPixel.y)

                    if (bolbPixel.toColor().toAWT() == Color.BLACK) {
                        outputImage.setColor(inputPixel.x, inputPixel.y, RGBColor.fromAwt(it.args.third))
                    }
                    else if (bolbPixel.toColor().toAWT() == Color.WHITE) {
                        outputImage.setPixel(inputPixel)
                    }
                    else {
                        outputImage.setColor(inputPixel.x, inputPixel.y, RGBColor.fromAwt(Color(0, 0, 0, 0)))
                    }
                }

                outputImage = when (it.args.second) {
                    "Squishbolbolb" -> {
                        outputImage.scaleToHeight(outputImage.height/4, ScaleMethod.Bicubic,
                                false)
                    }
                    "Tallbolb" -> {
                        outputImage.scaleToWidth(outputImage.width/4, ScaleMethod.Bicubic,
                                false)
                    }
                    else -> {
                        outputImage
                    }
                }

                outputImage.output(PngWriter(), outputFile)

                it.channel.sendFile(outputFile).complete()
            }

            Thread.sleep(1000)

            it.args.first.delete()
            outputFile.delete()
        }
    }
}
