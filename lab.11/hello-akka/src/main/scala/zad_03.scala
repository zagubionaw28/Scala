import akka.actor.{ActorSystem, Actor, ActorLogging, ActorRef, Props}
import javax.sound.midi.Receiver

case class Graj03(przeciwnik1: ActorRef, przeciwnik2: ActorRef)
case object Mes

class Gracz03 extends Actor with ActorLogging {
    def receive: Receive = {
      case Graj03(przeciwnik1, przeciwnik2) =>  // GRACZ1 gRAJ (gracz2, gracz3)
        log.info(s"${przeciwnik1}, ${przeciwnik2} +++++++")
        przeciwnik1 ! Graj03(przeciwnik2, self) // gracz2 ! Graj(gracz3, gracz1)
        context.become(rzucamyPingPangPong(przeciwnik1))  // gracz1 przeciwnik: gracz
      // case Piłeczka =>
        // log.info("Hello")
    }



    def rzucamyPingPangPong(przeciwnik: ActorRef, licznik: Int = 0): Receive = {
      case Graj03(przeciwnik1, przeciwnik2) => // GRACZ1 (GRACZ2, GRACZ1)
          log.info(s"${przeciwnik1} ${przeciwnik2} -------")
          context.become(rzucamyPingPangPong(przeciwnik, licznik + 1)) //gracz2
          przeciwnik1 ! Piłeczka  // gracz2
      case Piłeczka =>
        if (licznik == 0) {
          log.info(s"Pong ${licznik} ${przeciwnik}")  // gracz2: Pong
          context.become(rzucamyPingPangPong(przeciwnik, licznik + 1))
          // przeciwnik ! Piłeczka // gracz2
        } else if (licznik == 1) {
          log.info(s"Ping ${licznik} ${przeciwnik}")
          context.become(rzucamyPingPangPong(przeciwnik, licznik + 1))
          // przeciwnik ! Piłeczka
        } else {
          log.info(s"Pang ${licznik} ${przeciwnik}")
          // przeciwnik ! Piłeczka
          context.system.terminate()
          context.become(rzucamyPingPangPong(przeciwnik, 0))
        }
        przeciwnik ! Piłeczka
      case Mes =>
    }
}




@main def zad_03: Unit = {
  val system = ActorSystem("PingPong")
  val gracz1 = system.actorOf(Props[Gracz03](), "gracz1")
  val gracz2 = system.actorOf(Props[Gracz03](), "gracz2")
  val gracz3 = system.actorOf(Props[Gracz03](), "gracz3")

  gracz1 ! Graj03(gracz2, gracz3)
}



//  11111111111111111111111111111



// class Gracz03 extends Actor with ActorLogging {
//     def receive: Receive = {
//       case Graj03(przeciwnik1, przeciwnik2, przeciwnik3) =>
//         przeciwnik2 ! Piłeczka  // przez chwilę gracz 1 do 2
//         context.become(oddajMiTąPiłeczkę(przeciwnik1, przeciwnik2, przeciwnik3))
//         // przeciwnik3 ! Piłeczka
//       case Piłeczka =>
//         log.info("Ping")
//         sender() ! Piłeczka
//     }
//     // gracz1 do gracz3
//     def oddajMiTąPiłeczkę(przeciwnik1: ActorRef, przeciwnik2: ActorRef, przeciwnik3: ActorRef): Receive = {
//       case Piłeczka =>
//         log.info("Pang")
//         przeciwnik3 ! Piłeczka
//         context.become(tyMiJąWeźOddajBoZabrałeś(przeciwnik1, przeciwnik2, przeciwnik3))
//     }
//     // gracz3 do gracz1
//     def tyMiJąWeźOddajBoZabrałeś(przeciwnik1: ActorRef, przeciwnik2: ActorRef, przeciwnik3: ActorRef): Receive = {
//       case Piłeczka =>
//         log.info("Pong")
//         przeciwnik2 ! Piłeczka
        
//         // context.become(miMi(przeciwnik1, przeciwnik2, przeciwnik3))
//     }
//     // // gracz1 do gracz2
//     // def miMi(przeciwnik1: ActorRef, przeciwnik2: ActorRef, przeciwnik3: ActorRef): Receive = {
//     //   case Piłeczka =>
//     //     log.info("Ping")
//     //     przeciwnik2 ! Piłeczka
//     //     context.become(oddajMiTąPiłeczkę(przeciwnik1, przeciwnik2, przeciwnik3))
//     // }

// }






// @main def zad_03: Unit = {
//   val system = ActorSystem("PingPong")
//   val gracz1 = system.actorOf(Props[Gracz03](), "gracz1")
//   val gracz2 = system.actorOf(Props[Gracz03](), "gracz2")
//   val gracz3 = system.actorOf(Props[Gracz03](), "gracz3")

//   gracz1 ! Graj03(gracz1, gracz2, gracz3)
// }

 

//  22222222222222222222



// case class Graj03(przeciwnik1: ActorRef, przeciwnik2: ActorRef)
// case object Piłeczka

// class Gracz03 extends Actor with ActorLogging {
//   def receive: Receive = { case Graj03(przeciwnik1, przeciwnik2) =>
//     przeciwnik1 ! Graj03(przeciwnik2, self)
//     context.become(oddajMiTąPiłeczke(przeciwnik1))
//   }

//   def oddajMiTąPiłeczke(przeciwnik: ActorRef): Receive = {
//     case Graj03(przeciwnik1, przeciwnik2) =>
//       przeciwnik ! Piłeczka
//     case Piłeczka =>
//       log.info(s"Ping ${self.path}")
//       przeciwnik ! Piłeczka
//   }
// }
// @main def zad_03: Unit = {
//   val system = ActorSystem("PingPong3")
//   val gracz1 =
//     system.actorOf(Props[Gracz03](), "gracz1") // 1t, wysyła mes -> 2t
//   val gracz2 = system.actorOf(
//     Props[Gracz03](),
//     "gracz2"
//   ) // 1t, odbiera mes, wysyła mes -> 2t
//   val gracz3 = system.actorOf(
//     Props[Gracz03](),
//     "gracz3"
//   ) // 1t, odbiera mes, wysyła mes -> 2t

//   gracz1 ! Graj03(gracz2, gracz3)
// }
