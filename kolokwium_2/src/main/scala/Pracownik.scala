package kolokwium_2PP

import akka.actor.{Actor, ActorLogging, Props, ActorRef}

abstract class DoPracownika

class Pracownik extends Actor with ActorLogging {
  def receive: Receive = {
    case W(słowo) => 
      self ! W(słowo)
      context.become(zapamiętam(Map(), 0, ""))
  }

  def zapamiętam(mapaLiterActorów: Map[Char, ActorRef], licznik: Int, całeSłowo: String): Receive = {
    case W(słowo) =>
      if (słowo.length > 0) {
        if (mapaLiterActorów.contains(słowo.head)) {
        mapaLiterActorów(słowo.head) ! W(słowo.tail)
      } else {
        val pracownik = context.actorOf(Props(Pracownik()))
        pracownik ! W(słowo.tail)
        context.become(zapamiętam(mapaLiterActorów + (słowo.head -> pracownik), licznik, całeSłowo))
      }
    } else {
      context.become(zapamiętam(mapaLiterActorów, licznik + 1, całeSłowo))
    }

    case CałeSłowo(całesłowo) => 
      context.become(zapamiętam(mapaLiterActorów, licznik, całesłowo))

    case I(słowo) =>
      if (słowo.length > 0) {
        if (mapaLiterActorów.contains(słowo.head)) {
          mapaLiterActorów(słowo.head) ! I(słowo.tail)
        } else {
          context.parent ! Ile(całeSłowo, 0)
        }
      } else {
        context.parent ! Ile(całeSłowo, licznik)
      }
    case Ile(słowo, n) =>
      context.parent ! Ile(całeSłowo, n)
  }

}