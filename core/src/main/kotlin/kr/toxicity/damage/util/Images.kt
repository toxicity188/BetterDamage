package kr.toxicity.damage.util

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun File.toImage(): BufferedImage = ImageIO.read(this)