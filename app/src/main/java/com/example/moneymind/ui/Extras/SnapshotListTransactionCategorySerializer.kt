package com.example.moneymind.ui.Extras

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.moneymind.data.TransactionCategory
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure

object SnapshotListTransactionCategorySerializer : KSerializer<SnapshotStateList<TransactionCategory>> {

    override val descriptor: SerialDescriptor
        get() = ListSerializer(TransactionCategorySerializer).descriptor


    override fun serialize(encoder: Encoder, value: SnapshotStateList<TransactionCategory>) =
        encoder.encodeSerializableValue(ListSerializer(TransactionCategorySerializer), value)


    override fun deserialize(decoder: Decoder): SnapshotStateList<TransactionCategory> =
        decoder.decodeStructure(descriptor) {
            val list = mutableStateListOf<TransactionCategory>()
            val items = decoder.decodeSerializableValue(ListSerializer(TransactionCategorySerializer))
            list.addAll(items)
            list
        }

}