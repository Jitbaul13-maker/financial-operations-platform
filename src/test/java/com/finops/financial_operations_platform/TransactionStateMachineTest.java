package com.finops.financial_operations_platform;

import com.finops.financial_operations_platform.Exceptions.InvalidTransactionStateTransitionException;
import com.finops.financial_operations_platform.enums.TransactionStatus;
import org.junit.jupiter.api.BeforeEach;
import com.finops.financial_operations_platform.businesslogics.TransactionStateMachine;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class TransactionStateMachineTest {

    private TransactionStateMachine stateMachine;

    @BeforeEach
    void setUp() {
        stateMachine = new TransactionStateMachine();
    }

    private Stream<Arguments> validateSuccessfulTransition(){

        return Stream.of(
                Arguments.of(TransactionStatus.INITIATED, TransactionStatus.PROCESSING),
                Arguments.of(TransactionStatus.PROCESSING, TransactionStatus.FAILED),
                Arguments.of(TransactionStatus.PROCESSING, TransactionStatus.COMPLETED),
                Arguments.of(TransactionStatus.COMPLETED, TransactionStatus.REVERSED)
        );
    }

    @ParameterizedTest
    @MethodSource("validateSuccessfulTransition")
    void validTransitionsShouldNotThrow(TransactionStatus current, TransactionStatus requested) {
        assertDoesNotThrow(
                () -> stateMachine.validateTransition
                        (current, requested));
    }

    @ParameterizedTest
    @CsvSource({
            "INITIATED, INITIATED",
            "INITIATED, COMPLETED",
            "INITIATED, FAILED",
            "INITIATED, REVERSED",

            "PROCESSING, INITIATED",
            "PROCESSING, PROCESSING",
            "PROCESSING, REVERSED",

            "COMPLETED, INITIATED",
            "COMPLETED, PROCESSING",
            "COMPLETED, COMPLETED",
            "COMPLETED, FAILED",

            "FAILED, INITIATED",
            "FAILED, PROCESSING",
            "FAILED, COMPLETED",
            "FAILED, FAILED",
            "FAILED, REVERSED",

            "REVERSED, INITIATED",
            "REVERSED, PROCESSING",
            "REVERSED, COMPLETED",
            "REVERSED, FAILED",
            "REVERSED, REVERSED"
    })
    void validTransitionsShouldThrow(TransactionStatus current, TransactionStatus requested) {
        assertThrows(InvalidTransactionStateTransitionException.class,
                () -> stateMachine.validateTransition
                        (current, requested));
    }
}
