package org.trello.gatewayproxy.gateway_proxy_for_trello.kafka.messages

import java.util.UUID

/**
 *
 * @author Azizbek Toshpulatov
 */
data class KafkaEvent <T> (val commandId: UUID, val data: T)
