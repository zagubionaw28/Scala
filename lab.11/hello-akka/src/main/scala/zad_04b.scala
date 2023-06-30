import akka.actor.{ActorSystem, Actor, ActorLogging, ActorRef, Props}
import javax.sound.midi.Receiver
import java.util.Random
case class Graj04b(listaGraczy: List[ActorRef])

class Gracz04b extends Actor with ActorLogging {
    def receive: Receive = {
      case Graj04b(listaGraczy) =>
        val randomowyInniNiżSelf = listaGraczy.filter(n => n != self)(Random().nextInt(listaGraczy.length - 1))
        // randomowyInniNiżSelf ! Graj04b(listaGraczy)
        for (i <- 0 to listaGraczy.length) listaGraczy(i) ! Graj04b(listaGraczy)
        // log.info(s"${randomowyInniNiżSelf}")
        context.become(rzucaj(listaGraczy))
    }

    def rzucaj(listaGraczy: List[ActorRef]): Receive = {
      case Graj04b(listaGraczy) => 
      case Piłeczka =>
         val randomowyInniNiżSelf = listaGraczy.filter(n => n != self)(Random().nextInt(listaGraczy.length - 1))
          randomowyInniNiżSelf ! Piłeczka
          log.info(s"Ping ${randomowyInniNiżSelf}")
    }
}

@main def zad_04b: Unit = {
  val system = ActorSystem("PingPong")
  val gracz1 = system.actorOf(Props[Gracz04b](), "gracz1")
  val gracz2 = system.actorOf(Props[Gracz04b](), "gracz2")
  val gracz3 = system.actorOf(Props[Gracz04b](), "gracz3")
  val gracz4 = system.actorOf(Props[Gracz04b](), "gracz4")
  val gracz5 = system.actorOf(Props[Gracz04b](), "gracz5")
  val gracz6 = system.actorOf(Props[Gracz04b](), "gracz6")
  gracz1 ! Graj04b(List(gracz2, gracz3, gracz4, gracz5, gracz6, gracz1))
  gracz1 ! Piłeczka
  // println(Random().nextInt(6))
}


// class Gracz04b extends Actor with ActorLogging {
//   def receive: Receive = { case Graj04b(listaGraczy) =>
//     val oponent = listaGraczy.filter((n) => n != self)(
//       new Random().nextInt(listaGraczy.length - 1)
//     )
//     oponent ! Graj04b(listaGraczy)
//     oponent ! Piłeczka
//     context.become(oddajMiTąPiłeczkę(listaGraczy))
//   }

//   def oddajMiTąPiłeczkę(listaGraczy: List[ActorRef]): Receive = {
//     case Piłeczka =>
//       log.info(s"Ping ${self.path}")
//       val oponent = listaGraczy.filter((n) => n != self)(
//         new Random().nextInt(listaGraczy.length - 1)
//       )
//       oponent ! Graj04b(listaGraczy)
//       oponent ! Piłeczka
//     case _ =>
//   }
// }