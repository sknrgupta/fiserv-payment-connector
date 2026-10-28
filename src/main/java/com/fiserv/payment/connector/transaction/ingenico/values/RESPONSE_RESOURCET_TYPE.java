/**
 * 
 */
package com.fiserv.payment.connector.transaction.ingenico.values;

/**
 * 
 */
public enum RESPONSE_RESOURCET_TYPE {
    card_inserted,
    card_read,
    card_removed,
    manual_entry_started,
    amount_confirmation,
    amount_confirmed,
    pin_prompted,
    pin_entered,
    authorizing,
    transaction_completed,
    transaction_error,
    post_txn_reset
}
