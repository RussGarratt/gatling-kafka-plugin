package io.cosmospf.gatling.kafka

import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.session._
import org.apache.kafka.common.header.internals.RecordHeaders
import org.apache.kafka.common.header.{Header, Headers}
import io.cosmospf.gatling.kafka.checks.KafkaCheckSupport
import io.cosmospf.gatling.kafka.protocol.{KafkaProtocol, KafkaProtocolBuilder, KafkaProtocolBuilderNew}
import io.cosmospf.gatling.kafka.request.KafkaSerdesImplicits
import io.cosmospf.gatling.kafka.request.builder.{KafkaRequestBuilderBase, RequestBuilder}

import scala.jdk.CollectionConverters._

trait KafkaDsl extends KafkaCheckSupport with KafkaSerdesImplicits {

  val kafka: KafkaProtocolBuilder.type = KafkaProtocolBuilder

  val kafkaConsumer: KafkaProtocolBuilderNew.type = KafkaProtocolBuilderNew

  def kafka(requestName: Expression[String]): KafkaRequestBuilderBase =
    KafkaRequestBuilderBase(requestName)

  implicit def kafkaProtocolBuilder2kafkaProtocol(builder: KafkaProtocolBuilder): KafkaProtocol = builder.build

  implicit def kafkaProtocolBuilderNew2kafkaProtocol(builder: KafkaProtocolBuilderNew): KafkaProtocol = builder.build

  implicit def kafkaRequestBuilder2ActionBuilder[K, V](builder: RequestBuilder[K, V]): ActionBuilder = builder.build

  implicit def listHeaderToHeaders(lh: Expression[List[Header]]): Expression[Headers] = lh.map(l => new RecordHeaders(l.asJava))

  implicit def listHeaderToExpression(lh: List[Header]): Expression[Headers] = listHeaderToHeaders(lh.expressionSuccess)

}
