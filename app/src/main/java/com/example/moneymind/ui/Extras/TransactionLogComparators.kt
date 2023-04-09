package com.example.moneymind.ui.Extras

import com.example.moneymind.data.TransactionLog

class TransactionLogComparators {
    companion object{
        //constant values to use in dialog
        val COMPARE_BY_NAME = 0;
        val COMPARE_BY_VALUE = 1;
        val COMPARE_BY_DATE = 2;

        //comparators
        val compareByNameProperOrder = Comparator {a : TransactionLog, b :TransactionLog -> a.name.compareTo(b.name)}
        val compareByNameReverseOrder = Comparator {a : TransactionLog, b :TransactionLog -> b.name.compareTo(a.name)}

        val compareByValueProperOrder = Comparator {a : TransactionLog, b :TransactionLog -> a.amount.compareTo(b.amount)}
        val compareByValueReverseOrder = Comparator {a : TransactionLog, b :TransactionLog -> b.amount.compareTo(a.amount)}

        val compareByDateProperOrder = Comparator {a : TransactionLog, b :TransactionLog -> a.date.compareTo(b.date)}
        val compareByDateReverseOrder = Comparator {a : TransactionLog, b :TransactionLog -> b.date.compareTo(a.date)}

        val emptyComparator = Comparator {a : TransactionLog, b :TransactionLog -> 0}
    }
}