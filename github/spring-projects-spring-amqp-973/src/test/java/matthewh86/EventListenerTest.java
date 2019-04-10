package matthewh86;

import static matthewh86.RabbitConfiguration.EXCHANGE_NAME;
import static matthewh86.RabbitConfiguration.ROUTING_KEY;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {RabbitTestConfiguration.class})
class EventListenerTest {

    private EventListener underTest;

    private static final String LISTENER_CONTAINER_ID = "eventHandler";

    @Autowired
    private RabbitListenerTestHarness harness;
    @Autowired
    private TestRabbitTemplate testRabbitTemplate;

    @Mock(answer = RETURNS_DEEP_STUBS)
    private Message message;

    @BeforeEach
    void setUp() {
        underTest = harness.getSpy(LISTENER_CONTAINER_ID);
        assertNotNull(underTest);
    }

    @AfterEach
    void tearDown() {
        reset(underTest);
    }

    @Test
    void givenBlankApplicationEvent_handle_throwsMessageConversionExceptionToAvoidRequeue() {
        doReturn("".getBytes()).when(message).getBody();

        Throwable throwable = assertThrows(ListenerExecutionFailedException.class,
            () -> testRabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, message)).getCause();

        assertAll(
            () -> assertEquals("Woops.", throwable.getMessage()),
            () -> assertEquals(AmqpRejectAndDontRequeueException.class, throwable.getClass())
        );

        verify(underTest).handle(message);
    }
}
