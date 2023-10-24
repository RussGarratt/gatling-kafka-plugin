package io.cosmospf.gatling.kafka.client

import org.apache.kafka.clients.producer.{KafkaProducer, Producer, RecordMetadata}
import io.cosmospf.gatling.kafka.request.KafkaProtocolMessage

import scala.concurrent.{ExecutionContext, Future}
import scala.jdk.CollectionConverters._
import scala.util.{Failure, Success}

trait KafkaSender {
  def send(protocolMessage: KafkaProtocolMessage)(
      onSuccess: RecordMetadata => Unit,
      onFailure: Throwable => Unit,
  ): Unit
  def close(): Unit
}

object KafkaSender {
  private final class Impl(producer: Producer[Array[Byte], Array[Byte]])(implicit ec: ExecutionContext) extends KafkaSender {
    override def send(
        protocolMessage: KafkaProtocolMessage,
    )(onSuccess: RecordMetadata => Unit, onFailure: Throwable => Unit): Unit = {
      Future(producer.send(protocolMessage.toProducerRecord).get()).onComplete {
        case Success(value)     => onSuccess(value)
        case Failure(exception) => onFailure(exception)
      }

    }

    override def close(): Unit =
      producer.close()

  }

  def apply(producerSettings: Map[String, AnyRef])(implicit ec: ExecutionContext): KafkaSender = {
    val producer = new KafkaProducer[Array[Byte], Array[Byte]](producerSettings.asJava)
    new Impl(producer)
  }
}
