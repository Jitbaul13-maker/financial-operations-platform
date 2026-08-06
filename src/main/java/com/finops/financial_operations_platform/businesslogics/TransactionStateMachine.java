package com.finops.financial_operations_platform.businesslogics;

import com.finops.financial_operations_platform.Exceptions.InvalidTransactionStateTransitionException;
import com.finops.financial_operations_platform.enums.TransactionStatus;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TransactionStateMachine {
    private final Map<TransactionStatus, Set<TransactionStatus>> rules = new HashMap<>();

    public TransactionStateMachine() {
        rules.put(TransactionStatus.INITIATED, Set.of(TransactionStatus.PROCESSING));
        rules.put(TransactionStatus.PROCESSING, Set.of(TransactionStatus.COMPLETED, TransactionStatus.FAILED));
        rules.put(TransactionStatus.COMPLETED, Set.of(TransactionStatus.REVERSED));
//        rules.put(TransactionStatus.CANCELLED, Set.of());
    }

    private boolean isTransitionAllowed(TransactionStatus current, TransactionStatus requested) {
        if (rules.containsKey(current)){
            return rules.get(current).contains(requested);
        }
        return false;
    }

    public void validateTransition(TransactionStatus current, TransactionStatus requested){
        if(!isTransitionAllowed(current, requested)){
            throw new InvalidTransactionStateTransitionException
                    ("Invalid transaction state transition: " + current + " -> " + requested);
        }
    }
}
