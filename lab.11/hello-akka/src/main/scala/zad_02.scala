import akka.actor.{ActorSystem, Actor, ActorLogging, ActorRef, Props}
import javax.sound.midi.Receiver

case class Graj02(przeciwnik: ActorRef, maks: Int)


class Gracz02 extends Actor with ActorLogging {
    def receive: Receive = {
      case Graj02(przeciwnik, maks) =>
        przeciwnik ! Piłeczka
        context.become(oddajMiTąPiłeczkę(maks))
      case Piłeczka =>
        log.info("Ping")
        sender() ! Piłeczka
    }

    def oddajMiTąPiłeczkę(maks: Int, licznik: Int = 2): Receive = {
      case Piłeczka =>
        if (licznik < maks) {
          log.info(s"Pong ${licznik}")
          sender() ! Piłeczka
          context.become(oddajMiTąPiłeczkę(maks, licznik + 2))
        } else {
            if (maks % 2 == 0) {
              log.info(s"Pong ${licznik}")
              context.system.terminate()
            } else {
              context.system.terminate()
            }
          }
    }
}

@main def zad_02: Unit = {
  val system = ActorSystem("PingPong")
  val gracz1 = system.actorOf(Props[Gracz02](), "gracz1")
  val gracz2 = system.actorOf(Props[Gracz02](), "gracz2")
  gracz1 ! Graj02(gracz2, 1)
}








// class Gracz02 extends Actor with ActorLogging {
//   def receive: Receive = {
//     case Graj02(przeciwnik, maks) =>
//       przeciwnik ! Piłeczka
//       context.become(oddajMiTąPiłeczkę(maks))
//     case Piłeczka =>
//       log.info("Pong")
//       sender() ! Piłeczka
//   }

//   def oddajMiTąPiłeczkę(maks: Int, rzuty: Int = 2): Receive = {
//     case Piłeczka => // maks
//       if (rzuty < maks) {
//         context.become(oddajMiTąPiłeczkę(maks, rzuty + 2))
//         log.info(s"Ping $rzuty")
//         sender() ! Piłeczka
//       } else {
//         if (maks % 2 == 0) {
//           log.info(s"Ping $rzuty")
//           context.system.terminate()
//         } else context.system.terminate()
//       }
//   }

//   // def tyMiJąWeźOddajBoZabrałeś(maks: Int, rzuty: Int): Receive = {
//   //   case
//   // }
// }

// @main def zad_02: Unit = {
//   val system = ActorSystem("PingPong")
//   val gracz1 = system.actorOf(Props[Gracz02](), "gracz1")
//   val gracz2 = system.actorOf(Props[Gracz02](), "gracz2")
//   gracz1 ! Graj02(gracz2, 27)
// }
