import akka.actor.{ActorSystem, Actor, ActorLogging, ActorRef, Props}
import javax.sound.midi.Receiver

case class Graj04a(lista: List[ActorRef])

class Gracz04a extends Actor with ActorLogging {
    def receive: Receive = {
      case Graj04a(lista) =>
        lista.head ! Graj04a((lista.head :: lista.tail.reverse).reverse)
        log.info(s"${lista}")
        context.become(ruszaj(lista, lista.head))
      
    }

    def ruszaj(lista: List[ActorRef], przeciwnik: ActorRef): Receive = {
      case Piłeczka =>
        log.info(s"Ping ${przeciwnik}")
        przeciwnik ! Piłeczka
    }
}

@main def zad_04: Unit = {
  val system = ActorSystem("PingPong")
  // val gracz1 = system.actorOf(Props[Gracz04a](), "gracz1")
  // val gracz2 = system.actorOf(Props[Gracz04a](), "gracz2")
  // val gracz3 = system.actorOf(Props[Gracz04a](), "gracz3")
  // val gracz4 = system.actorOf(Props[Gracz04a](), "gracz4")
  

  // gracz1 ! Graj04a(List(gracz2, gracz3, gracz4, gracz1))
  // gracz1 ! Piłeczka
  val n = 5
  val zawodnicy = (for (i <- 1 to n) yield system.actorOf(Props[Gracz04a](), s"zawodnik${i}")).toList
  println(zawodnicy)
  // val zawodnicy2 = zawodnicy.foldLeft(List[ActorRef]())((acc, curr) => 
  //   curr :: acc )
  val zawodnicy2 = (zawodnicy.head :: zawodnicy.tail.reverse).reverse
  println(s"\n\n$zawodnicy2\n\n")
  zawodnicy(0) ! Graj04a(zawodnicy2)
  zawodnicy(0) ! Piłeczka
}


// class Gracz04a extends Actor with ActorLogging {
//   def receive: Receive = { case Graj04a(listaGraczy) =>
//     listaGraczy.head ! Graj04a(listaGraczy.tail)
//     context.become(dajMiTąPiłeczkę(listaGraczy.head))
//   }

//   def dajMiTąPiłeczkę(przeciwnik: ActorRef): Receive = {
//     case Graj04a(listaGraczy) =>
//       if (listaGraczy.length == 0) {
//         przeciwnik ! Piłeczka
//       }
//     case Piłeczka =>
//       log.info(s"Ping ${self.path}")
//       przeciwnik ! Piłeczka
//   }
// }
