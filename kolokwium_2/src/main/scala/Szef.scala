package kolokwium_2

import akka.actor.{Actor, ActorLogging, Props, ActorRef}
import javax.sound.midi.Receiver

abstract class DoSzefa
case class W(słowo: String) extends DoSzefa
case class I(słowo: String) extends DoSzefa
case class CałeSłowo(słowo: String) extends DoSzefa
case class Ile(słowo: String, n: Int) extends DoSzefa
case object Czekam extends DoSzefa
// case class CałeSłowo(słowo: String) extends DoSzefa

class Szef extends Actor with ActorLogging {
  def receive: Receive = {
    case W(słowo) => 
      self ! W(słowo)
      context.become(zapamiętam(Map()))
  }

  def zapamiętam(mapaLiterActorów: Map[Char, ActorRef]): Receive = {
    case W(słowo) =>
      // log.info(s"$mapaLiterActorów")
      if (mapaLiterActorów.contains(słowo.head)) {
        mapaLiterActorów(słowo.head) ! W(słowo.tail)
      } else {
        val pracownik = context.actorOf(Props(Pracownik()))
        pracownik ! W(słowo.tail)
        context.become(zapamiętam(mapaLiterActorów + (słowo.head -> pracownik)))
      }
    
    case I(słowo) => 
      // log.info(s"$słowo $mapaLiterActorów")
      if (mapaLiterActorów.contains(słowo.head)) {
        mapaLiterActorów(słowo.head) ! CałeSłowo(słowo)
        mapaLiterActorów(słowo.head) ! I(słowo.tail)
      } else {
        log.info(s"Nie ma takiego słowa w danych - ${słowo}")
      }
    
    case Ile(słowo, n) =>
      log.info(s"$słowo ----- $n")
  }
}