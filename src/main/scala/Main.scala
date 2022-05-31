import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.kstream.Printed
import org.apache.kafka.streams.{KafkaStreams, StreamsBuilder, StreamsConfig}

import java.util.Properties
import scala.io.StdIn.readInt

object Main extends App {
  val sb = new StreamsBuilder
  val wordToUpper = sb.stream[String,String]("wordsIn")
  wordToUpper
    .mapValues((value: String) => value.toUpperCase)
    .to("upperWords")
  wordToUpper.print(Printed.toSysOut[String,String])
  val multiplier = readInt()
  val numberMultiplied = sb.stream[String,String]("numbersIn")
    numberMultiplied
      .mapValues((value: String) => value.toInt * multiplier)
      .to("multiplied")

  val topology = sb.build()
  val props = new Properties()
  props.put(StreamsConfig.APPLICATION_ID_CONFIG,"kafka-streams-task")
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,":9092")
  props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass)
  props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass)
  val streamsConfig = new StreamsConfig(props)
  val kafkaStream = new KafkaStreams(topology,streamsConfig)
  kafkaStream.start()
}
