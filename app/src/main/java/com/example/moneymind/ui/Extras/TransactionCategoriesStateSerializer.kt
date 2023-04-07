package com.example.moneymind.ui.Extras

import com.example.moneymind.data.TransactionCategoriesState
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

object TransactionCategoriesStateSerializer  : KSerializer<TransactionCategoriesState> {

    override val descriptor: SerialDescriptor
        get() = buildClassSerialDescriptor("TransactionCategoriesState") {
            element<String>("categories")
        }


    override fun serialize(encoder: Encoder, value: TransactionCategoriesState) =
        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(descriptor, 0, SnapshotListTransactionCategorySerializer, value.categories)
        }


    override fun deserialize(decoder: Decoder): TransactionCategoriesState =
        decoder.decodeStructure(descriptor) {
            TransactionCategoriesState(categories = decodeSerializableElement(
                descriptor, 0, SnapshotListTransactionCategorySerializer))
        }

}