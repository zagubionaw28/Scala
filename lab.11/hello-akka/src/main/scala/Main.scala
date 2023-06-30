import akka.actor.{ActorSystem, Actor, ActorLogging, ActorRef, Props}

case class Graj01(przeciwnik: ActorRef)
case object Piłeczka


class Gracz01 extends Actor with ActorLogging {
    def receive: Receive = {
      case Graj01(przeciwnik) =>
        przeciwnik ! Piłeczka
        context.become(oddajMiTąPiłeczkę)
      case Piłeczka =>
        log.info("Ping")
        sender() ! Piłeczka
    }

    def oddajMiTąPiłeczkę: Receive = {
      case Piłeczka =>
        log.info("Pong")
        sender() ! Piłeczka
    }
}





@main def zad_01: Unit = {
  val system = ActorSystem("PingPong")
  val gracz1 = system.actorOf(Props[Gracz01](), "gracz1")
  val gracz2 = system.actorOf(Props[Gracz01](), "gracz2")
  gracz1 ! Graj01(gracz2)
}









// case object Piłeczka
// case class Graj01(przeciwnik: ActorRef)

// class Gracz01 extends Actor with ActorLogging {
//   def receive: Receive = {
//     case Graj01(przeciwnik) =>
//       przeciwnik ! Piłeczka
//       context.become(dajMiTąPiłeczkę)
//     case Piłeczka =>
//       log.info("Pong")
//       sender() ! Piłeczka
//   }

//   def dajMiTąPiłeczkę: Receive = { case Piłeczka =>
//     log.info("Ping")
//     sender() ! Piłeczka
//   }
// }

// @main def main: Unit = {
//   val system = ActorSystem("PingPong")
//   val gracz1 = system.actorOf(Props[Gracz01](), "gracz1")
//   val gracz2 = system.actorOf(Props[Gracz01](), "gracz2")
//   gracz2 ! Graj01(gracz1)
// }
