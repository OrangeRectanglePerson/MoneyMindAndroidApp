package com.example.moneymind.ui.Extras

import androidx.compose.ui.graphics.Color
import com.example.moneymind.data.TransactionCategory
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

object TransactionCategorySerializer : KSerializer<TransactionCategory> {

    override val descriptor: SerialDescriptor
        get() = buildClassSerialDescriptor("Color") {
            element<String>("name")
            element<@Serializable(with = ColorSerializer::class) Color>("color")
        }


    override fun serialize(encoder: Encoder, value: TransactionCategory) =
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.name)
            encodeSerializableElement(descriptor, 1, ColorSerializer, value.color)
        }


    override fun deserialize(decoder: Decoder): TransactionCategory =
        decoder.decodeStructure(descriptor) {
            TransactionCategory(
                decodeStringElement(descriptor,0),
                decodeSerializableElement(descriptor, 1, ColorSerializer)
            )
        }

}