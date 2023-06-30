package zad_03_02

import akka.actor.{ActorSystem, Actor, ActorLogging, ActorRef, Props}
import javax.sound.midi.Receiver

case class Graj031(przeciwnik: ActorRef, mess: String)
case object Piłeczka

class Gracz031 extends Actor with ActorLogging {
    def receive: Receive = {
      case Graj031(przeciwnik, mess) =>
        // przeciwnik ! Piłeczka
        context.become(rzucaj(przeciwnik, mess))
      // case Piłeczka =>
      //   sender() ! Piłeczka

    }

    def rzucaj(przeciwnik: ActorRef, mess: String): Receive = {
      case Piłeczka =>
        log.info(s"${mess} ${przeciwnik}")
        przeciwnik ! Piłeczka
    }
}



@main def zad_03_02: Unit = {
  val system = ActorSystem("PingPong")
  val Artur1 = system.actorOf(Props[Gracz031](), "Artur1")
  val Pawel2 = system.actorOf(Props[Gracz031](), "Pawel2")
  val Filu3 = system.actorOf(Props[Gracz031](), "Filu3")
  Artur1 ! Graj031(Pawel2, "Ping")
  Pawel2 ! Graj031(Filu3, "Pang")
  Filu3 ! Graj031(Artur1, "Pong")

  Artur1 ! Piłeczka
}