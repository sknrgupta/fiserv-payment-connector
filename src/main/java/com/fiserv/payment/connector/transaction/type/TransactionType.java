/**
 * 
 */
package com.fiserv.payment.connector.transaction.type;

/**
 * 
 */
public enum TransactionType {

    SALE("01", "sale", "Sale"),
    REFUND("03", "return", "Return"),
    VOID("08", "void", "Void"),
    REVERSAL("04", "reversal", "Reversal");

    private final String transactionType;
    private final String transactionTypeARCSI;
    private final String transactionDescription;

    TransactionType(String transactionType, String transactionTypeARCSI, String transactionDescription) {
        this.transactionType = transactionType;
        this.transactionTypeARCSI = transactionTypeARCSI;
        this.transactionDescription = transactionDescription;
    }

    public String getTransactionType() {
        return this.transactionType;
    }

    /**
     * @return the transactionTypeARCSI
     */
    public String getTransactionTypeARCSI() {
        return transactionTypeARCSI;
    }

    /**
     * @return the transactionDescription
     */
    public String getTransactionDescription() {
        return transactionDescription;
    }

    public static TransactionType fromString(String value) {
        for (TransactionType transactionType : TransactionType.values()) {
            if (transactionType.getTransactionType().equalsIgnoreCase(value)) {
                return transactionType;
            }
        }
        return null;
    }

}
