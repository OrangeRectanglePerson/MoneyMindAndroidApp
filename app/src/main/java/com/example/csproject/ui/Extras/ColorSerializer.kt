package com.example.csproject.ui.Extras

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

object ColorSerializer : KSerializer<Color> {

    override val descriptor: SerialDescriptor
        get() = buildClassSerialDescriptor("Color") {
            element<Int>("ARGB Int")
        }


    override fun serialize(encoder: Encoder, value: Color) =
        encoder.encodeStructure(descriptor) {
            encodeIntElement(descriptor, 0, (value.toArgb()))
        }


    override fun deserialize(decoder: Decoder): Color =
        decoder.decodeStructure(descriptor) {
           Color(decodeIntElement(descriptor, 0))
        }




}