package org.galaxio.gatling.kafka.protocol

import io.gatling.core.CoreComponents
import io.gatling.core.protocol.ProtocolComponents
import io.gatling.core.session.Session
import org.galaxio.gatling.kafka.client.{KafkaSender, KafkaMessageTrackerPool}

case class KafkaComponents(
    coreComponents: CoreComponents,
    kafkaProtocol: KafkaProtocol,
    trackersPool: Option[KafkaMessageTrackerPool],
    sender: KafkaSender,
) extends ProtocolComponents {

  override def onStart: Session => Session = Session.Identity

  override def onExit: Session => Unit = ProtocolComponents.NoopOnExit
}
