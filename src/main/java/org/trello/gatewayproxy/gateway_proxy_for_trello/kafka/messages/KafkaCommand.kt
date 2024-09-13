package org.trello.gatewayproxy.gateway_proxy_for_trello.kafka.messages

import org.trello.gatewayproxy.gateway_proxy_for_trello.kafka.KafkaActions
import java.util.UUID

/**
 * @author Azizbek Toshpulatov
 */
data class KafkaCommand <T> (val commandId: UUID, val data: T, val action: KafkaActions)